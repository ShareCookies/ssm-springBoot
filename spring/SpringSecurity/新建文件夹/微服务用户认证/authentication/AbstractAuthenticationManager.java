package com.rongji.egov.security.gateway;

import com.rongji.egov.security.SecurityUser;
import com.rongji.egov.security.gateway.user.UserService;
import com.rongji.egov.user.model.RmsLoginLog;
import com.rongji.egov.user.model.RmsSystem;
import com.rongji.egov.user.model.UmsOrg;
import com.rongji.egov.user.model.UmsUser;
import com.rongji.egov.user.model.constant.SystemConstant;
import com.rongji.egov.utils.common.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

/**
 * @author daihuabin
 */
public abstract class AbstractAuthenticationManager implements AuthenticationManager {
    public static final Logger logger = LoggerFactory.getLogger(AbstractAuthenticationManager.class);

    public static final String SEC_INIT_PASS_ENABLED = "1";
    public static final String DEFAULT_PASSWORD = "12345678";

    /**
     * 用户停用状态
     */
    public final static String STOP_STATUS = "0";
    public static final long MINUTE_MILLISECOND = 60000;

    protected GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    protected UserService userService;

    public AbstractAuthenticationManager(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication token, ServerWebExchange exchange) {
        return Mono.just(token)
                .publishOn(Schedulers.elastic())
                .flatMap(t -> {
                    return doAuthenticate(t, exchange);//登陆确认？
                })
				//登陆后的一些操作处理！
                .filter(a -> a != null && a.isAuthenticated()).doOnNext(authentication -> {
                    SecurityUser principal = (SecurityUser) authentication.getPrincipal();
                    // 构建登陆日志
                    UmsUser umsUser = exchange.getAttribute("CURRENT_USER");
                    //记录登录日志
                    RmsLoginLog rmsLoginLog = new RmsLoginLog();
                    rmsLoginLog.setId(IdUtil.getUID());
                    rmsLoginLog.setUserNo(umsUser.getUserNo());
                    rmsLoginLog.setUserName(umsUser.getUserName());

                    UmsOrg dept = this.userService.getBelongsDept(umsUser.getUserNo());
                    UmsOrg unit = this.userService.getBelongsUnit(umsUser.getUserNo());

                    rmsLoginLog.setDeptNo(dept.getOrgNo());

                    rmsLoginLog.setDeptName(dept.getOrgName());
                    rmsLoginLog.setUnitNo(unit.getOrgNo());
                    rmsLoginLog.setUnitName(unit.getOrgName());
                    rmsLoginLog.setLoginStatus(SystemConstant.SUCCESS);
                    rmsLoginLog.setLoginTime(new Timestamp(System.currentTimeMillis()));
                    rmsLoginLog.setLoginType(SystemConstant.HTTP);
                    rmsLoginLog.setIpAddress(IpUtils.getIp(exchange.getRequest()));
                    rmsLoginLog.setLoginDesc("用户：" + rmsLoginLog.getUserName() + "-登录成功");
                    rmsLoginLog.setSystemNo(principal.getSystemNo());
                    this.userService.insertLoginLog(rmsLoginLog);
                    logger.info("用户：" + umsUser.getUserNo() + "-登录成功");
                    //设置日志id
                    exchange.getSession().doOnNext(session -> {
                        session.getAttributes().put("loginId", rmsLoginLog.getId());
                    }).subscribe();
                    // 如果存在登陆失败次数， 清除登陆失败次数
                    Integer loginFailTimes = umsUser.getLoginFailTimes();
                    UmsUser query = new UmsUser();
                    query.setId(umsUser.getId());
                    query.setUserNo(umsUser.getUserNo());
                    if (loginFailTimes != null && loginFailTimes != 0) {
                        query.setLoginFailTimes(0);
                        this.userService.updateUserByIdSelective(query);
                    }
                }).doOnError(e -> {
                    this.handleAuthenticationFail(token, exchange, e);
                });
    }

    public Mono<Authentication> doAuthenticate(Authentication authentication, ServerWebExchange exchange) throws AuthenticationException {
        SimpleAuthenticationToken token = (SimpleAuthenticationToken) authentication;

        return this.obtainUmsUser(authentication, exchange).flatMap(umsUser -> {
            return this.afterObtainUmsUser(authentication, exchange).thenReturn(umsUser);
        }).map(user -> {
            this.cacheLoginUser(user, exchange);
            token.setPrincipal(user.getUserNo());

            // 如果未启用，直接抛禁用异常
            if (STOP_STATUS.equals(user.getStatus())) {
                throw new DisabledException("账号已被禁用");
            }

//        String systemNo = umsUser.getSystemNo();
//
//        if (StringUtils.isBlank(systemNo)) {
//            // 根据
//            // 登录组织编号
//            String orgNo = umsUser.getLoginOrgNo();
//            if (StringUtils.isBlank(orgNo)) {
//                // 获取本职orgNo
//                orgNo = this.userService.getPrimaryOrgByUserNo(orgNo).getOrgNo();
//            }
//            // 根据orgNo获取组织信息
//            UmsOrg umsOrg = orgNo == null ? null : this.umsOrgMng.getUmsOrgByNo(orgNo);
//            if (umsOrg == null) {
//                throw new DisabledException("账号没有所属组织，无法使用，请联系管理员");
//            }
//            // 用户系统编码
//            systemNo = umsOrg.getSystemNo();
//        }


            // 获取登陆系统
            RmsSystem loginSystem = this.userService.getSystemByUserNo(user.getUserNo());
            // 获取登录组织
            UmsOrg loginOrg = this.userService.getLoginOrgByUserNo(user.getUserNo());

            // 缓存
            this.cacheLoginSystem(loginSystem, exchange);
            this.cacheLoginOrg(loginOrg, exchange);

            // 检查是否被锁定
            this.checkLock(loginSystem, user);

            if (this.checkCredentials(authentication, exchange)) {
                this.afterValidCredentials(authentication, exchange);
                SecurityUser securityUser = this.buildSecurityUser(exchange);
                return this.buildAuthenticationToken(securityUser, authentication, exchange);
            } else {
                this.handleBadCredentials(this.getBadCredentialsBaseMsg(), exchange);
                return null;
            }
        });
    }

    /**
     * 在获取用户前
     *
     * @param authentication
     * @param exchange
     */
    public Mono<Void> afterObtainUmsUser(Authentication authentication, ServerWebExchange exchange) {
        return Mono.empty();
    }

    /**
     * 构建安全用户
     *
     * @param exchange
     * @return
     */
    public SecurityUser buildSecurityUser(ServerWebExchange exchange) {
        UmsUser user = this.getLoginUser(exchange);
        RmsSystem loginSystem = this.getLoginSystem(exchange);
        UmsOrg loginOrg = this.getLoginOrg(exchange);
        // 身份验证通过后
        SecurityUser securityUser = new SecurityUser();
        securityUser.setAccountNonExpired(true);
        securityUser.setAccountNonLocked(true);
        securityUser.setCredentialsNonExpired(true);
        securityUser.setEnabled(true);
        securityUser.setUsername(user.getUserNo());
        securityUser.setPassword(user.getPassword());
        securityUser.setSystemNo(loginSystem.getSystemNo());
        securityUser.setOrgNo(loginOrg.getOrgNo());
        // 计算用户的权限
        Collection<GrantedAuthority> authorities = this.userService.listGrantedAuthority(user.getUserNo(), loginSystem.getSystemNo());
        securityUser.setAuthorities(authorities);
        return securityUser;
    }

    /**
     * 缓存登陆系统
     *
     * @param system
     * @param exchange
     */
    public void cacheLoginSystem(RmsSystem system, ServerWebExchange exchange) {
        if (system != null) {
            exchange.getAttributes().put("CURRENT_USER_SYSTEM", system);
        }
    }

    /**
     * 缓存登陆组织
     *
     * @param org
     * @param exchange
     */
    public void cacheLoginOrg(UmsOrg org, ServerWebExchange exchange) {
        if (org != null) {
            exchange.getAttributes().put("CURRENT_USER_ORG", org);
        }
    }

    /**
     * 缓存登陆用户
     *
     * @param umsUser
     * @param exchange
     */
    public void cacheLoginUser(UmsUser umsUser, ServerWebExchange exchange) {
        if (umsUser != null) {
            exchange.getAttributes().put("CURRENT_USER", umsUser);
        }
    }

    /**
     * 获取登陆系统
     *
     * @param exchange
     * @return
     */
    public RmsSystem getLoginSystem(ServerWebExchange exchange) {
        return (RmsSystem) exchange.getAttributes().get("CURRENT_USER_SYSTEM");
    }

    /**
     * 获取登陆系统
     *
     * @param exchange
     * @return
     */
    public UmsOrg getLoginOrg(ServerWebExchange exchange) {
        return (UmsOrg) exchange.getAttributes().get("CURRENT_USER_ORG");
    }

    /**
     * 获取登陆用户
     *
     * @param exchange
     * @return
     */
    public UmsUser getLoginUser(ServerWebExchange exchange) {
        return (UmsUser) exchange.getAttributes().get("CURRENT_USER");
    }

    /**
     * 凭证有效后的处理，检查用户的一些状态
     *
     * @param exchange
     */
    public void afterValidCredentials(Authentication authentication, ServerWebExchange exchange) {

    }

    /**
     * 处理登陆失败, 比如记录登陆信息
     */
    public void handleAuthenticationFail(Authentication token, ServerWebExchange exchange, Throwable e) {

    }


    /**
     * 检查是否锁定
     *
     * @param loginSystem
     * @param user
     */
    public void checkLock(RmsSystem loginSystem, UmsUser user) {
        // 获得系统的限制登陆次数
        Integer secPassLimitTime = loginSystem.getLoginLockTime();
        Integer secPassLimit = loginSystem.getLoginLimitTimes();

        Integer loginFailTimes = user.getLoginFailTimes();
        Date loginLastTime = user.getLoginLastTime();

        // 系统启用了登陆次数失败过多锁定功能
        if (secPassLimitTime != null && secPassLimitTime > 0 && secPassLimit != null && secPassLimit > 0) {
            if (loginFailTimes != null && loginFailTimes >= secPassLimit) {
                // 账号锁定
                if (System.currentTimeMillis() - secPassLimitTime * MINUTE_MILLISECOND < loginLastTime.getTime()) {
                    long second = loginLastTime.getTime() / 1000L + secPassLimitTime * 60L - System.currentTimeMillis() / 1000L;
                    throw new LockedException(String.format("账号已被锁定，请%d秒后重试", second));
                }
            }
        }
    }

    /**
     * 处理凭证错误
     *
     * @param baseMsg
     * @param exchange
     */
    public void handleBadCredentials(String baseMsg, ServerWebExchange exchange) {
        RmsSystem loginSystem = this.getLoginSystem(exchange);
        UmsUser user = this.getLoginUser(exchange);
        String msg = baseMsg;

        // 获得系统的限制登陆次数
        Integer secPassLimitTime = loginSystem.getLoginLockTime();
        Integer secPassLimit = loginSystem.getLoginLimitTimes();

        Integer loginFailTimes = user.getLoginFailTimes();
        Date loginLastTime = user.getLoginLastTime();

        // 系统启用了登陆次数失败过多锁定功能
        if (secPassLimitTime != null && secPassLimitTime > 0 && secPassLimit != null && secPassLimit > 0) {
            // 如果之前没有登录过系统， 最后一次登录时间会是null，如果是null的话也是算过期，失败次数重置为1
            boolean expired = loginLastTime == null ||
                    (System.currentTimeMillis() - secPassLimitTime * MINUTE_MILLISECOND > loginLastTime.getTime());

            // loginFailTimes存放的是实际已经失败次数
            int realLoginFailTimes = loginFailTimes == null || loginFailTimes > secPassLimit || expired ? 0 : loginFailTimes;
            if (realLoginFailTimes + 1 >= secPassLimit) {
                msg = String.format("%s，由于您失败次数过多，账号已被锁定，请%d分钟后重试", baseMsg, secPassLimitTime);
            } else if (realLoginFailTimes + 1 > 1) {
                msg = String.format("%s，您已失败%d次，超过%d次，账号将被锁定%d分钟", baseMsg, realLoginFailTimes + 1, secPassLimit - 1, secPassLimitTime);
            }
        }
        throw new BadCredentialsException(msg);
    }

    /**
     * 获取用户
     *
     * @param authentication
     * @param exchange
     * @return
     */
    public abstract Mono<UmsUser> obtainUmsUser(Authentication authentication, ServerWebExchange exchange);


    /**
     * 检查凭证，获取到用户后，比如比较密码
     *
     * @return
     */
    public boolean checkCredentials(Authentication authentication, ServerWebExchange exchange) {
        return true;
    }

    /**
     * 获取错误凭证时的基础提示信息
     *
     * @return
     */
    public String getBadCredentialsBaseMsg() {
        return "";
    }


    /**
     * 构建认证成功后的token
     *
     * @param securityUser
     * @param authentication
     * @param exchange
     * @return
     */
    public abstract Authentication buildAuthenticationToken(SecurityUser securityUser, Authentication authentication, ServerWebExchange exchange);

}
