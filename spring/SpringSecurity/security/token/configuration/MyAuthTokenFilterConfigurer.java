package com.china.hcg.eas.business.base.security.token.configuration;

import com.china.hcg.eas.business.base.security.token.AuthTokenProvider;
import com.china.hcg.eas.business.base.security.token.filter.MyAuthTokenFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @autor hecaigui
 * @date 2019-11-30
 * @description
 */
//将自定义的内容整合到spring security中。然后将该类整合到 SecurityConfiguration配置类中。
public class MyAuthTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AuthTokenProvider tokenProvider;  // 我们自定义的 token功能类
    private UserDetailsService detailsService;// 我们实现的UserDetailsService

    public MyAuthTokenFilterConfigurer(UserDetailsService detailsService, AuthTokenProvider tokenProvider) {
        this.detailsService = detailsService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        MyAuthTokenFilter customFilter = new MyAuthTokenFilter(detailsService, tokenProvider);
		//这里确保了自定义的filter会优先于u.filter执行。（?u.filter是第一个过滤器吗）
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
