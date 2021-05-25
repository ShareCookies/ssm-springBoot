package com.rongji.egov.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.rongji.egov.security.SecurityBaseConfiguration;
import com.rongji.egov.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

/**
 * @author xxx
 */

@Configuration
@Import(SecurityBaseConfiguration.class)
public class SecurityServiceConfiguration {

    @Bean
    SimpleModule serviceSecurityJacksonModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(SecurityUser.class, new SecurityUserDeserializer());
        return simpleModule;
    }


    @EnableWebSecurity
    @Order(200)
    @Configuration
    public static class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
        @Autowired
        ObjectMapper objectMapper;

        @Bean
        FeignRequestInterceptor feignRequestInterceptor() {
            return new FeignRequestInterceptor(objectMapper);
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
            requestHeaderAuthenticationFilter.setContinueFilterChainOnUnsuccessfulAuthentication(true);
            requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false);
            requestHeaderAuthenticationFilter.setPrincipalRequestHeader("X-Principal");
            requestHeaderAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
            http.csrf().disable().headers().frameOptions().disable();
            http.authorizeRequests().anyRequest().permitAll();
            http.addFilterBefore(requestHeaderAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);

        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
            AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preAuthenticatedAuthenticationTokenAuthenticationUserDetailsService = new PreAuthenticationUserDetailsService(this.objectMapper);
            preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(preAuthenticatedAuthenticationTokenAuthenticationUserDetailsService);
            auth.authenticationProvider(preAuthenticatedAuthenticationProvider);
        }
    }
}

