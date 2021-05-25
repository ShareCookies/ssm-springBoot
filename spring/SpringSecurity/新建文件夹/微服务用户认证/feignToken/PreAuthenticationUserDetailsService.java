package com.rongji.egov.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rongji.egov.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;

/**
 * @author daihuabin
 */
public class PreAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    public static final Logger logger = LoggerFactory.getLogger(PreAuthenticationUserDetailsService.class);

    private ObjectMapper objectMapper;

    public PreAuthenticationUserDetailsService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper == null ? new ObjectMapper() : objectMapper;
    }


    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String principal = (String) token.getPrincipal();
        try {
            return this.objectMapper.readValue(principal, SecurityUser.class);
        } catch (IOException e) {
            logger.warn("IO异常", e);
            throw new UsernameNotFoundException("反序列化失败", e);
        }
    }
}
