package com.china.hcg.eas.business.base.security.token;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @autor hecaigui
 * @date 2019-11-21
 * @description 传输对象token
 */
public class AuthToken {
    String token;
    /**
     * 过期时间
     */
    Long expires;

    public AuthToken(String token, Long expires) {
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }
}
