package com.rongji.egov.security.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.rongji.egov.elasticjob.ElasticJobConfiguration;
import com.rongji.egov.security.SecurityBaseConfiguration;
import com.rongji.egov.security.audit.*;
import com.rongji.egov.security.gateway.user.ResourceService;
import com.rongji.egov.security.gateway.user.UserService;
import com.rongji.egov.security.session.RedisFlushMode;
import com.rongji.egov.security.session.config.annotation.web.server.EnableRedisWebSession;
import com.rongji.egov.user.client.UserClientConfiguration;
import com.rongji.egov.utils.mybatis.configuration.MybatisConfiguration;
import com.rongji.egov.utils.spring.formatter.SortFormatter;
import io.elasticjob.lite.config.JobCoreConfiguration;
import io.elasticjob.lite.config.LiteJobConfiguration;
import io.elasticjob.lite.config.simple.SimpleJobConfiguration;
import io.elasticjob.lite.reg.base.CoordinatorRegistryCenter;
import io.elasticjob.lite.spring.api.SpringJobScheduler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.ResourceHandlerRegistrationCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.HttpStatusReturningServerLogoutSuccessHandler;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.authorization.ExceptionTranslationWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.reactive.config.ResourceHandlerRegistration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.session.WebSessionIdResolver;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

/**
 * @author daihuabin
 */
@Configuration
@ComponentScan
@Import({SecurityBaseConfiguration.class, UserClientConfiguration.class, MybatisConfiguration.class, ElasticJobConfiguration.class, AuditAccessLogConfiguration.class})
@EnableAutoConfiguration(exclude = ReactiveUserDetailsServiceAutoConfiguration.class)
@EnableConfigurationProperties({SecurityGatewayProperties.class,AuditAccessLogProperties.class})
public class SecurityGatewayConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(SecurityGatewayConfiguration.class, args);
    }


    /**
     * 注意 initMethod = "init" 参数是必须的
     * 方法名为 小写开头job名称 + Scheduler
     *
     * @param registryCenter
     * @param expiredOnlineSessionClearJob
     * @return
     */
    @Bean(initMethod = "init")
    public SpringJobScheduler expiredOnlineSessionClearJobScheduler(CoordinatorRegistryCenter registryCenter, ExpiredOnlineSessionClearJob expiredOnlineSessionClearJob) {
        // 定义作业核心配置 newBuilder的第1个参数为 小写开头job名称， 第2个参数为cron表达式， 第3个参数为分片数量， 同步骤4简单用法用1即可，高级用法见官方文档
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder("expiredOnlineSessionClearJob", "0 0/15 * * * ?", 1).build();
        // 定义SIMPLE类型配置 固定写法， XxlJob换成你的Job类名
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, ExpiredOnlineSessionClearJob.class.getCanonicalName());
        // 定义Lite作业根配置 固定写法，高级用法见官方文档 overwrite(true) 选项为覆盖zookeeper上的配置
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
        return new SpringJobScheduler(expiredOnlineSessionClearJob, registryCenter, liteJobConfiguration);
    }


    /**
     * 注册排序格式化器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    SortFormatter sortFormatter() {
        return new SortFormatter();
    }

    @Bean
    @ConditionalOnMissingBean
    GlobalExceptionHandlerAdvice globalExceptionHandlerAdvice() {
        return new GlobalExceptionHandlerAdvice();
    }

    @Bean
    public AddPrincipalHeadersFilter addPrincipalHeadersFilter(ObjectMapper objectMapper) {
        return new AddPrincipalHeadersFilter(objectMapper);
    }


    @Bean
    DefaultKaptcha captchaProducer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "10");
        defaultKaptcha.setConfig(new Config(properties));
        return defaultKaptcha;
    }

    @Bean
    @ConditionalOnMissingBean(PasswordStrengthCheckService.class)
    PasswordStrengthCheckService passwordStrengthCheckService() {
        return new DefaultPasswordStrengthCheckService();
    }


    @Configuration
    @EnableRedisWebSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
    public static class SessionConfiguration {
        /**
         * http session rest token
         *
         * @return
         */
        @Bean
        public WebSessionIdResolver webSessionIdResolver(SecurityGatewayProperties securityGatewayProperties) {
            CustomWebSessionIdResolver customWebSessionIdResolver = new CustomWebSessionIdResolver();
            // 如果设置了tokenName，则以tokenName为准
            if (StringUtils.isNotBlank(securityGatewayProperties.getTokenCookieName())) {
                customWebSessionIdResolver.setTokenCookieName(securityGatewayProperties.getTokenCookieName());
            }
            return customWebSessionIdResolver;
        }

//        /**
//         * session 事件发布者
//         *
//         * @return
//         */
//        @Bean
//        public HttpSessionEventPublisher httpSessionEventPublisher() {
//            return new HttpSessionEventPublisher();
//        }
    }

    @Configuration
    public static class StaticResourceWebFluxConfigurer implements WebFluxConfigurer {

        private final ResourceHandlerRegistrationCustomizer resourceHandlerRegistrationCustomizer;

        @Autowired
        ResourceProperties resourceProperties;

        public StaticResourceWebFluxConfigurer(
                ObjectProvider<ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizer
        ) {
            this.resourceHandlerRegistrationCustomizer = resourceHandlerRegistrationCustomizer.getIfAvailable();
        }



        /**
         * 配置静态访问资源
         *
         * @param registry
         */
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // /和 /index.html不缓存
            ResourceHandlerRegistration indexResourceHandlerRegistration = registry.addResourceHandler("/index.html*", "/")
                    .addResourceLocations(resourceProperties.getStaticLocations())
                    .setCacheControl(CacheControl.noStore().mustRevalidate());//用 noStore 才起效
            if(this.resourceHandlerRegistrationCustomizer != null) {
                this.resourceHandlerRegistrationCustomizer.customize(indexResourceHandlerRegistration);
            }
        }
    }

    @Configuration
    public static class MultiHttpSecurityConfig {

//        public static final int DEFAULT_MAXIMUM_SESSIONS = -1;
//        public static final boolean DEFAULT_MAX_SESSIONS_PREVENTS_LOGIN = false;

//        @Resource
//        SessionRepository sessionRepository;
//
//        @Bean
//        SessionRegistry sessionRegistry() {
//            if (this.sessionRepository instanceof FindByIndexNameSessionRepository) {
//                return new SpringSessionBackedSessionRegistry((FindByIndexNameSessionRepository) this.sessionRepository);
//            } else {
//                return new SessionRegistryImpl();
//            }
//        }

//        @Bean(name = "rememberMeServices")
//        @ConditionalOnMissingBean(name = "rememberMeServices")
//        RememberMeServices rememberMeServices() {
//            SpringSessionRememberMeServices rememberMeServices =
//                    new SpringSessionRememberMeServices();
//            // optionally customize
//            rememberMeServices.setAlwaysRemember(false);
//            rememberMeServices.setRememberMeParameterName("remember");
//            return rememberMeServices;
//        }

//        @Bean
//        @ConditionalOnMissingBean(name = "sessionAuthenticationStrategy")
//        SessionAuthenticationStrategy sessionAuthenticationStrategy(
//                SessionRegistry sessionRegistry
//        ) {
//            // 会话认证策略
//            List<SessionAuthenticationStrategy> sessionAuthenticationStrategies = new ArrayList<>();
//
//            // 自定义的并发会话控制认证策略
//            CustomConcurrentSessionControlAuthenticationStrategy ccscas = new CustomConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
//            ccscas.setMaximumSessions(DEFAULT_MAXIMUM_SESSIONS);
//            ccscas.setExceptionIfMaximumExceeded(DEFAULT_MAX_SESSIONS_PREVENTS_LOGIN);
//
//            // 寄存会话认证侧率
//            RegisterSessionAuthenticationStrategy registerSessionStrategy = new RegisterSessionAuthenticationStrategy(
//                    sessionRegistry);
//
//            sessionAuthenticationStrategies.add(ccscas);
//            sessionAuthenticationStrategies.add(registerSessionStrategy);
//
//            // 复合认证策略
//            return new CompositeSessionAuthenticationStrategy(sessionAuthenticationStrategies);
//        }

//        @Bean("sessionInformationExpiredStrategy")
//        @ConditionalOnMissingBean(name = "sessionInformationExpiredStrategy")
//        SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
//            return new RestSessionInformationExpiredStrategy();
//        }


//        @Bean(name = "concurrentSessionFilterBean")
//        @ConditionalOnMissingBean(name = "concurrentSessionFilterBean")
//        FilterRegistrationBean concurrentSessionFilter(
//                SessionRegistry sessionRegistry,
//                SessionInformationExpiredStrategy sessionInformationExpiredStrategy
//        ) {
//            // 并发会话过滤器
//            ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry, sessionInformationExpiredStrategy);
//            FilterRegistrationBean registrationBean = new FilterRegistrationBean(concurrentSessionFilter);
//            registrationBean.setEnabled(false);
//            return registrationBean;
//        }


        @Bean
        @ConditionalOnMissingBean(name = "serverSecurityContextRepository")
        ServerSecurityContextRepository serverSecurityContextRepository() {
            return new WebSessionServerSecurityContextRepository();
        }

        @Bean
        @ConditionalOnMissingBean(name = "concurrentSessionControlAuthenticationStrategy")
        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy(OnlineSessionService onlineSessionService) {
            return new ConcurrentSessionControlAuthenticationStrategy(onlineSessionService);
        }

        @Bean
        @ConditionalOnMissingBean(name = "logServerLogoutHandler")
        LogServerLogoutHandler logServerLogoutHandler(UserService userService) {
            return new LogServerLogoutHandler(userService);
        }

        @Bean
        @ConditionalOnMissingBean(name = "usernamePasswordAuthenticationManager")
        UsernamePasswordAuthenticationManager usernamePasswordAuthenticationManager(UserService userService, SecurityGatewayProperties securityGatewayProperties) {
            return new UsernamePasswordAuthenticationManager(userService, securityGatewayProperties);
        }

        @Bean
        @ConditionalOnMissingBean(name = "tokenidAuthenticationManager")
        TokenidAuthenticationManager tokenidAuthenticationManager(UserService userService, SecurityGatewayProperties securityGatewayProperties) {
            return new TokenidAuthenticationManager(userService, securityGatewayProperties);
        }

        @Bean
        @ConditionalOnMissingBean(name = "returnUserInfoServerAuthenticationSuccessHandler")
        ReturnUserInfoServerAuthenticationSuccessHandler returnUserInfoServerAuthenticationSuccessHandler(ObjectMapper objectMapper) {
            return new ReturnUserInfoServerAuthenticationSuccessHandler(objectMapper);
        }

        @Bean
        ThrowExceptionServerAuthenticationFailureHandler throwExceptionServerAuthenticationFailureHandler() {
            return new ThrowExceptionServerAuthenticationFailureHandler();
        }

        @Bean(name = "tokenidAuthenticationFilterBean")
        @ConditionalOnMissingBean(name = "tokenidAuthenticationFilterBean")
        WebFilterRegistrationBean tokenidAuthenticationFilterBean(
                TokenidAuthenticationManager tokenidAuthenticationManager,
                ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy,
                ServerSecurityContextRepository serverSecurityContextRepository,
                ReturnUserInfoServerAuthenticationSuccessHandler returnUserInfoServerAuthenticationSuccessHandler,
                ThrowExceptionServerAuthenticationFailureHandler throwExceptionServerAuthenticationFailureHandler
        ) {
            TokenidAuthenticationFilter filter = new TokenidAuthenticationFilter(
                    tokenidAuthenticationManager,
                    serverSecurityContextRepository,
                    returnUserInfoServerAuthenticationSuccessHandler,
                    throwExceptionServerAuthenticationFailureHandler,
                    concurrentSessionControlAuthenticationStrategy
            );
            WebFilterRegistrationBean registrationBean = new WebFilterRegistrationBean(filter);
            registrationBean.setEnabled(false);
            return registrationBean;
        }

        @Bean(name = "usernamePasswordAuthenticationFilterBean")
        @ConditionalOnMissingBean(name = "usernamePasswordAuthenticationFilterBean")
        WebFilterRegistrationBean usernamePasswordAuthenticationFilterBean(
                UsernamePasswordAuthenticationManager usernamePasswordAuthenticationManager,
                ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy,
                ServerSecurityContextRepository serverSecurityContextRepository,
                ReturnUserInfoServerAuthenticationSuccessHandler returnUserInfoServerAuthenticationSuccessHandler,
                ThrowExceptionServerAuthenticationFailureHandler throwExceptionServerAuthenticationFailureHandler
        ) {
            UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter(
                    usernamePasswordAuthenticationManager,
                    serverSecurityContextRepository,
                    returnUserInfoServerAuthenticationSuccessHandler,
                    throwExceptionServerAuthenticationFailureHandler,
                    concurrentSessionControlAuthenticationStrategy
            );
            WebFilterRegistrationBean registrationBean = new WebFilterRegistrationBean(filter);
            registrationBean.setEnabled(false);
            return registrationBean;
        }

        @Bean
        ServerHttpRequestMetadataSource serverHttpRequestMetadataSource(ResourceService resourceService) {
            return new ServerHttpRequestMetadataSource(resourceService);
        }

        private static List<String> DEFAULT_IGNORED = Arrays.asList("/", "/css/**", "/js/**",
                "/images/**", "/webjars/**", "/**/favicon.ico", "/static/**", "/video/**", "/index.html",
                "/services/**", "/admin/api/**", "/actuator/**", "/swagger-resources/**", "/v2/**", "/webjars/**", "/swagger-ui.html",
                "/getLoginCaptcha", "/getUserinfoByTokenid");

        @Bean
        IgnoredMatcherProvider ignoredMatcherProvider() {
            IgnoredMatcherProvider ignoredMatcherProvider = new IgnoredMatcherProvider();
            ignoredMatcherProvider.addIgnoredPaths(new HashSet<>(DEFAULT_IGNORED));
            return ignoredMatcherProvider;
        }

        /**
         * @author daihuabin
         * web 安全配置
         */
        @Configuration
        @EnableWebFluxSecurity
        public static class WebSecurityConfiguration {
            //            @Autowired
//            RememberMeServices rememberMeServices;
            @Resource
            LogServerLogoutHandler logServerLogoutHandler;
            @Autowired(required = false)
            List<WebFilterRegistrationBean> filters;
            @Autowired
            ServerHttpRequestMetadataSource serverHttpRequestMetadataSource;

            @Autowired(required = false)
            List<IgnoredPathsWrapper> ignoredPathsWrappers;

            @Autowired
            IgnoredMatcherProvider ignoredMatcherProvider;

            @Autowired
            AuditAccessLogProperties auditAccessLogProperties;
            @Autowired
            AuditAccessLogRepository auditAccessLogRepository;

            @Bean
            @Order(200)
            public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ObjectMapper objectMapper) {

                //@formatter:off

//                http
//                    .authorizeExchange()
//                    .matchers(
//                            ServerWebExchangeMatchers.pathMatchers(ignoredPaths.toArray(new String[0]))
//                    )
//                    .permitAll();

                http
                    .requestCache()
                        .disable()
                    .csrf()
                        .disable()
                    .cors()
                        .disable()
                    .headers()
                        .frameOptions().disable();

                http

                    .logout()
                        .logoutHandler(new DelegatingServerLogoutHandler(logServerLogoutHandler, new InvalidSessionServerLogoutHandler()))
                        .logoutSuccessHandler(new HttpStatusReturningServerLogoutSuccessHandler(HttpStatus.OK))
                        .and();
//                    .rememberMe()
//                        .rememberMeServices(rememberMeServices)
//                        .and()
//                    .sessionManagement()
//                        .sessionAuthenticationStrategy(sessionAuthenticationStrategy)
//                        .disable();
                //@formatter:on

                ExceptionTranslationWebFilter exceptionTranslationWebFilter = new ExceptionTranslationWebFilter();
                exceptionTranslationWebFilter.setAuthenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));

                http.addFilterAt(exceptionTranslationWebFilter, SecurityWebFiltersOrder.EXCEPTION_TRANSLATION);

                // 授权过滤器
                AuthorizationWebFilter authorizationWebFilter = new AuthorizationWebFilter(
                        new SimpleReactiveAuthorizationManager<>(serverHttpRequestMetadataSource, ignoredMatcherProvider)
                );

                http.addFilterAt(authorizationWebFilter, SecurityWebFiltersOrder.AUTHORIZATION);

                http.addFilterAt(new ExceptionCatchFilter(objectMapper), SecurityWebFiltersOrder.FIRST);

                // 访问日志
                if(auditAccessLogProperties.getEnabled()){
                    http.addFilterAt(new AuditAccessLogFilter(auditAccessLogProperties, auditAccessLogRepository), SecurityWebFiltersOrder.AUTHENTICATION);
                }

                // 并发会话过滤器
//                ConcurrentSessionFilter concurrentSessionFilter = (ConcurrentSessionFilter) concurrentSessionFilterBean.getFilter();
//                http.addFilter(concurrentSessionFilter);

                // 注册认证处理过滤器
                if (this.filters != null) {
                    for (WebFilterRegistrationBean filterRegistrationBean : filters) {
                        WebFilter filter1 = filterRegistrationBean.getFilter();
                        if(filter1 instanceof AuthenticationFilter) {
                            http.addFilterAt(filter1, SecurityWebFiltersOrder.AUTHENTICATION);
                        }
                    }
                }
                return http.build();
            }
        }
    }

}
