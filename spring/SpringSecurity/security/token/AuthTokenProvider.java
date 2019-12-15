package com.china.hcg.eas.business.base.security.token;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


/**
 * @autor hecaigui
 * @date 2019-11-21
 * @description tokenProvider通常具有两个方法，即：生成token，验证token。
 */
@Component
public class AuthTokenProvider {
    private  String secretKey = "2019-11-27-eas-hcg";
    private  int tokenValidity = 1;
    public AuthTokenProvider() {
    }
    public AuthTokenProvider(String secretKey, int tokenValidity) {
        this.secretKey = secretKey;
        this.tokenValidity = tokenValidity;
    }
    /**
     * @description 生成token
     * @param  用户对象 userDetails
     */
    public AuthToken createToken(UserDetails  userDetails) {
        long expires = System.currentTimeMillis() + 1000L * tokenValidity;
        String token = computeSignature(userDetails, expires);
        return new AuthToken(token, expires);
    }
    /**
     * @description 验证token
     * @param  用户对象 userDetails
     */
    public boolean validateToken(String authToken, UserDetails  userDetails) {
        //check token
        String userName = this.getUserNameFromToken(authToken);
        return true;
    }

    // 从token中识别用户
    public String getUserNameFromToken(String authToken) {
        // ……
        return authToken;
    }
    public String computeSignature(UserDetails userDetails, long expires) {
        // 一些特有的信息组装 ,并结合某种加密活摘要算法
        //https://blog.csdn.net/menglinjie/article/details/84390503#commentBox
        //https://blog.csdn.net/qq_32967665/article/details/86347263
        //https://www.jb51.net/article/162549.htm
        //todo:
        return userDetails.getUsername();
    }
}
