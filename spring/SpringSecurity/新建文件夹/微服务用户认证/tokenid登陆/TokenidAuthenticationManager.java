package com.rongji.egov.security.gateway;

import com.rongji.egov.security.SecurityUser;
import com.rongji.egov.security.gateway.user.UserService;
import com.rongji.egov.user.model.UmsOrg;
import com.rongji.egov.user.model.UmsUser;
import com.rongji.egov.user.model.vo.UserSearchWhere;
import com.rongji.egov.utils.exception.BusinessException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author daihuabin
 */
public class TokenidAuthenticationManager extends AbstractAuthenticationManager {
    public static final Logger logger = LoggerFactory.getLogger(TokenidAuthenticationManager.class);
    public static final String DEFAULT_CHARSET = "UTF-8";


    SecurityGatewayProperties securityGatewayProperties;


    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private PasswordEncoder passwordEncoder = new RongJiPasswordEncoder();

    public TokenidAuthenticationManager(UserService userService,
                                        SecurityGatewayProperties securityGatewayProperties) {
        super(userService);
        this.securityGatewayProperties = securityGatewayProperties;
    }

    @Override
    public Mono<UmsUser> obtainUmsUser(Authentication authentication, ServerWebExchange exchange) {

        String base64EncodedToken = (String) authentication.getCredentials();

        try {
            String token = URLDecoder.decode(new String(Base64.decodeBase64(base64EncodedToken), DEFAULT_CHARSET), DEFAULT_CHARSET);
            Map<String, String> params = new HashMap<>(16);
            String[] queryArr = token.split("&");
            for (int i = 0; i < queryArr.length; i++) {
                String string = queryArr[i];
                String[] kv = string.split("=", 2);
                if (kv.length != 2) {
                    params.put(kv[0], "");
                } else {
                    params.put(kv[0], kv[1]);
                }
            }
            String account = params.get("account");
            boolean newWay = StringUtils.isNotBlank(account);
            String username = newWay ? params.get("subaccount") : params.get("username");
            String dt = params.get("dt");
            String code = params.get("code");
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(dt) || StringUtils.isEmpty(code)) {
                throw new BadCredentialsException("TOKEN无效");
            }
            if (this.securityGatewayProperties.getValidateSsoTokenPeriod()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(dt));
                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE, -5);
                if (now.after(calendar)) {
                    throw new BadCredentialsException("TOKEN无效");
                }
                now.add(Calendar.MINUTE, 10);
                if (now.before(calendar)) {
                    throw new BadCredentialsException("TOKEN无效");
                }
            }
            if (!DigestUtils.md5Hex(newWay ? account + username + dt : username + dt).equalsIgnoreCase(code)) {
                throw new BadCredentialsException("TOKEN无效");
            }

            UserSearchWhere where = new UserSearchWhere();
            if ("userNo".equals(params.get("accountType"))) {
                where.setUserNo(true);
            } else if ("hrNo".equals(params.get("accountType"))) {
                where.setHrNo(true);
            } else {
                where.setShortName(true);
            }

            // 根据用户名获取用户列表
            List<UmsUser> users = this.userService.listUser(username, where);

            if (users == null || users.isEmpty()) {
                throw new UsernameNotFoundException("用户不存在");
            } else if (users.size() > 1) {
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
            return Mono.just(users.get(0));
        } catch (UnsupportedEncodingException | ParseException e) {
            throw new BadCredentialsException("TOKEN无效", e);
        }
    }

    @Override
    public Authentication buildAuthenticationToken(SecurityUser securityUser, Authentication authentication, ServerWebExchange exchange) {
        TokenidAuthenticationToken result = new TokenidAuthenticationToken(
                securityUser, authentication.getCredentials(),
                authoritiesMapper.mapAuthorities(securityUser.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }
}
