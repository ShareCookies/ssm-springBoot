package com.china.hcg.eas.business.base.security;

import com.china.hcg.eas.business.base.security.token.AuthTokenProvider;
import com.china.hcg.eas.business.base.security.token.configuration.MyAuthTokenFilterConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @autor hecaigui
 * @date 2019-11-30
 * @description
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;

    //1.security配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() //authorizeRequests() 定义哪些URL需要被保护、哪些不需要被保护。
                    //.antMatchers("/product/**").hasRole("USER") //user 角色，能访问/product/**。
                    //.antMatchers("/admin/**").hasRole("ADMIN") //ADMIN 可以访问所有资源！
                    .antMatchers("/user/loginByAccount").permitAll() // 配置需要忽略的请求（即不需要登录也可访问，但会经过spring security的过滤器链）。
                    .antMatchers("/user/loginByAccount2").permitAll()
                    .anyRequest().authenticated() //其余所有的url均需要认证
                .and()
                //.formLogin().and() // 定义当需要用户登录时候，转到的登录页面。spring security默认提供了一个登录页面，以及登录控制器。重启应用然后继续访问http://localhost:8080/hello，会发现自动跳转到一个登录页面了。为了登录系统，我们需要知道用户名密码，spring security 提供的用户认证默认的用户名是user，密码在启动日志中可以看到。或在配置文件指定用户和密码：security.user.name=admin  security.user.password=admin  登录成功会跳转到了/hello。
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //security session 设为无状态
                .and()
                //.httpBasic().and()
                .csrf().disable() //关闭csrf保护
                .apply(securityConfigurerAdapter());// 应用securityConfigurerAdapter; security中增加自己的代码
    }
    @Autowired
    private AuthTokenProvider tokenProvider;
    // 增加方法
    private MyAuthTokenFilterConfigurer securityConfigurerAdapter() {
        return new MyAuthTokenFilterConfigurer(userDetailsService, tokenProvider);
    }

    //2.配置用户存储 。 （即如何加载用户）
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //通过自定义userDetailsService，从数据库中加载用户
        auth.userDetailsService(userDetailsService)// 该service中自定义了用户的加载。
                .passwordEncoder(passwordEncoder());// 设置密码编码器
        //从内存中加载用户
        /*auth
            .inMemoryAuthentication()
            .withUser("admin1")
                .password("admin1")
                .roles("ADMIN", "USER")
                .and()
            .withUser("user1").password("user1")
                .roles("USER");*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }



    //无法找到AuthenticationManager的bean，所以手动注册一个bean
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
