package com.rongji.egov.security.gateway;

import com.google.code.kaptcha.Constants;
import com.rongji.egov.security.SecurityUser;
import com.rongji.egov.security.gateway.user.UserService;
import com.rongji.egov.user.model.RmsLoginLog;
import com.rongji.egov.user.model.RmsSystem;
import com.rongji.egov.user.model.UmsOrg;
import com.rongji.egov.user.model.UmsUser;
import com.rongji.egov.user.model.constant.SystemConstant;
import com.rongji.egov.user.model.vo.UserSearchWhere;
import com.rongji.egov.utils.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author daihuabin
 */
public class UsernamePasswordAuthenticationManager extends AbstractAuthenticationManager {
    public static final Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationManager.class);
    public static final String SEC_INIT_PASS_ENABLED = "1";
    public static final String DEFAULT_PASSWORD = "12345678";

    SecurityGatewayProperties securityGatewayProperties;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private PasswordEncoder passwordEncoder = new RongJiPasswordEncoder();
    private PasswordStrengthCheckService passwordStrengthCheckService = new DefaultPasswordStrengthCheckService();

    public UsernamePasswordAuthenticationManager(UserService userService, SecurityGatewayProperties securityGatewayProperties) {
        super(userService);
        this.securityGatewayProperties = securityGatewayProperties;
    }

    @Override
    public Mono<Void> afterObtainUmsUser(Authentication authentication, ServerWebExchange exchange) {
        if (this.securityGatewayProperties.getValidateLoginCaptcha()) {
            // 与session内的验证码比较
            return exchange.getSession().flatMap(session -> {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
                String captcha = token.getCaptcha();
                // 移除session内的验证码
                String realCaptcha = (String) session.getAttributes().remove(Constants.KAPTCHA_SESSION_KEY);
                // 如果验证码相等
                if (StringUtils.equalsIgnoreCase(captcha, realCaptcha)) {
                    return Mono.empty();
                } else {
                    throw new BadCaptchaAuthenticationException("验证码错误");
                }
            });
        } else {
            return Mono.empty();
        }

    }

    @Override
    public Mono<UmsUser> obtainUmsUser(Authentication authentication, ServerWebExchange exchange) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String username = token.getName();
        String password = (String) token.getCredentials();
        String accountType = token.getAccountType();

        // 根据用户名和密码进行登录
        UserSearchWhere userSearchWhere = new UserSearchWhere();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(UserSearchWhere.class);

        if (StringUtils.isBlank(accountType)) {
            Set<String> loginUsernameSearchWhere = this.securityGatewayProperties.getLoginUsernameSearchWhere();

            if (loginUsernameSearchWhere == null || loginUsernameSearchWhere.isEmpty()) {
                loginUsernameSearchWhere = SecurityGatewayProperties.DEFAULT_LOGIN_USER_NAME_SEARCH_WHERE;
            }
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (loginUsernameSearchWhere.contains(propertyDescriptor.getName())) {
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    try {
                        writeMethod.invoke(userSearchWhere, true);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.warn("设置用户搜索属性失败 {}", propertyDescriptor.getName());
                    }
                }
            }
        } else {
            try {
                PropertyDescriptor found = null;
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (Objects.equals(accountType, propertyDescriptor.getName())) {
                        found = propertyDescriptor;
                        break;
                    }
                }
                if (found != null) {
                    Method writeMethod = found.getWriteMethod();
                    writeMethod.invoke(userSearchWhere, true);
                } else {
                    throw new BusinessException("账户类型无效");
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BusinessException("账户类型无效");
            }
        }

        // 根据用户名获取用户列表
        List<UmsUser> users = this.userService.listUser(username, userSearchWhere);

        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("用户不存在");
        } else if (users.size() > 1) {
            // 查看是否有匹配密码的用户
            int samePasswordCount = 0;
            UmsUser samePasswordUser = null;
            for (UmsUser user : users) {
                if (this.passwordEncoder.matches(password, user.getPassword())) {
                    samePasswordCount++;
                    if (samePasswordCount > 1) {
                        break;
                    }
                    samePasswordUser = user;
                }
            }
            // 如果只找到一个相同密码的用户，以该用户身份登陆
            if (samePasswordCount == 1) {
                return Mono.just(samePasswordUser);
            } else {
                List<DuplicationNameUser> duplicationNameUsers = new LinkedList<>();
                for (UmsUser user : users) {
                    UmsOrg dept = this.userService.getBelongsDept(user.getUserNo());
                    UmsOrg unit = this.userService.getBelongsUnit(user.getUserNo());
                    String orgName = dept.getOrgName();
                    if (!dept.getOrgNo().equals(unit.getOrgNo())) {
                        orgName = orgName + "/" + unit.getOrgName();
                    }
                    duplicationNameUsers.add(new DuplicationNameUser(
                            user.getUserName(), user.getShortName(), orgName, user.getUserNo()
                    ));
                }
                // 重名用户
                throw new BusinessException(
                        "无法确定您的身份",
                        409,
                        duplicationNameUsers
                );
            }
        }
        return Mono.just(users.get(0));
    }

    @Override
    public boolean checkCredentials(Authentication authentication, ServerWebExchange exchange) {
        String password = String.valueOf(authentication.getCredentials());
        UmsUser loginUser = this.getLoginUser(exchange);
        return this.passwordEncoder.matches(password, loginUser.getPassword());
    }

    @Override
    public String getBadCredentialsBaseMsg() {
        return "账号或密码错误";
    }

    @Override
    public Authentication buildAuthenticationToken(SecurityUser securityUser, Authentication authentication, ServerWebExchange exchange) {
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                securityUser, authentication.getCredentials(),
                authoritiesMapper.mapAuthorities(securityUser.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public void afterValidCredentials(Authentication authentication, ServerWebExchange exchange) {
        RmsSystem loginSystem = this.getLoginSystem(exchange);
        String credentials = (String) authentication.getCredentials();
        UmsUser loginUser = this.getLoginUser(exchange);
        if (DEFAULT_PASSWORD.equals(credentials)) {
            // 存储在当前请求中
            exchange.getAttributes().put("CURRENT_USER_INIT_PASS_MATCH", true);
            // 密码验证通过后，如果系统设置了初始密码强制修改并且密码是初始密码，则抛出异常
            if (SEC_INIT_PASS_ENABLED.equals(loginSystem.getChangePassword())) {
                throw new CredentialsExpiredException("密码已过期，请修改后登陆");
            }
        }
        // 密码强度是否匹配
        try {
            this.passwordStrengthCheckService.checkPassword(credentials, loginSystem.getPasswordStrength());
        } catch (Exception e) {
            // 存储在当前请求中
            exchange.getAttributes().put("CURRENT_USER_PASS_STRENGTH_NOT_MATCH", true);
        }
        // 验证密码有效期
        Integer secPassTime = loginSystem.getPasswordValidity();
        if (secPassTime != null && secPassTime > 0) {
            // 用户修改时间
            Timestamp modifyPwdTime = loginUser.getModifyPwdTime();
            if (modifyPwdTime == null || System.currentTimeMillis() - secPassTime * 24 * 60 * 60 * 1000L > modifyPwdTime.getTime()) {
                // 不匹配
                // 存储在当前请求中
                exchange.getAttributes().put("CURRENT_USER_PASS_TIME_LIMIT_NOT_MATCH", true);
            }
        }
    }

    @Override
    public void handleAuthenticationFail(Authentication token, ServerWebExchange exchange, Throwable e) {
        // 非验证码错误时记录日志
        if (!(e instanceof BadCaptchaAuthenticationException)) {
            // 登陆日志
            String username = token.getName();
            RmsLoginLog rmsLoginLog = new RmsLoginLog();
            rmsLoginLog.setUserName(username);
            rmsLoginLog.setLoginStatus(SystemConstant.FAIL);
            rmsLoginLog.setLoginTime(new Timestamp(System.currentTimeMillis()));
            rmsLoginLog.setLoginType("01");
            rmsLoginLog.setIpAddress(IpUtils.getIp(exchange.getRequest()));
            rmsLoginLog.setLoginDesc("用户：" + username + "-登录失败-" + e.getMessage());
            this.userService.insertLoginLog(rmsLoginLog);
            logger.info("用户： {} -登录失败", username);

            // 登陆失败需要增加登陆失败次数
            UmsUser umsUser = (UmsUser) exchange.getAttribute("CURRENT_USER");
            if (umsUser != null) {
                Integer loginFailTimes = umsUser.getLoginFailTimes();
                Date loginLastTime = umsUser.getLoginLastTime();
                UmsUser query = new UmsUser();
                query.setId(umsUser.getId());
                query.setUserNo(umsUser.getUserNo());

                // 自上次登陆失败的时间如果大于系统规定时间间隔，则重新开始计数
                int newLoginFailTimes = 1;

                RmsSystem system = this.userService.getSystemByUserNo(umsUser.getUserNo());
                if (system != null) {
                    Integer secPassLimitTime = system.getLoginLockTime();
                    Integer secPassLimit = system.getLoginLimitTimes();
                    // 系统启用了登陆次数失败过多锁定功能
                    if (secPassLimitTime != null && secPassLimitTime > 0 && secPassLimit != null && secPassLimit > 0) {
                        // 如果之前没有登录过系统， 最后一次登录时间会是null，如果是null的话也是算过期，失败次数重置为1
                        boolean expired = loginLastTime == null ||
                                (System.currentTimeMillis() - secPassLimitTime * MINUTE_MILLISECOND > loginLastTime.getTime());
                        if (!expired) {
                            // 没过期的话累加
                            newLoginFailTimes = ++loginFailTimes;
                        }
                        query.setLoginFailTimes(newLoginFailTimes);
                        query.setLoginLastTime(new Date(System.currentTimeMillis()));
                        this.userService.updateUserByIdSelective(query);
                    }
                }
            }
        }
    }
}
