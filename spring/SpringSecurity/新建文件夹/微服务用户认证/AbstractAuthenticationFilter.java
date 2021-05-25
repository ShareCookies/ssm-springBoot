package com.rongji.egov.security.gateway;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author daihuabin
 
package com.rongji.egov.security.gateway;
public interface AuthenticationFilter extends WebFilter {}
package org.springframework.web.server;
public interface WebFilter {
    Mono<Void> filter(ServerWebExchange var1, WebFilterChain var2);
}
 */


public abstract class AbstractAuthenticationFilter implements AuthenticationFilter {

    protected AuthenticationManager authenticationManager;

    protected ServerAuthenticationSuccessHandler authenticationSuccessHandler;


    protected ServerSecurityContextRepository securityContextRepository;

    protected ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy;

    protected ServerAuthenticationFailureHandler authenticationFailureHandler;


    /**
     * Creates an instance
     *
     * @param authenticationManager the authentication manager to use
     */
    public AbstractAuthenticationFilter(
            AuthenticationManager authenticationManager,
            ServerSecurityContextRepository securityContextRepository,
            ServerAuthenticationSuccessHandler authenticationSuccessHandler,
            ServerAuthenticationFailureHandler authenticationFailureHandler,
            ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy) {
        this.authenticationManager = authenticationManager;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.securityContextRepository = securityContextRepository;
        this.concurrentSessionControlAuthenticationStrategy = concurrentSessionControlAuthenticationStrategy;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public ConcurrentSessionControlAuthenticationStrategy getConcurrentSessionControlAuthenticationStrategy() {
        return concurrentSessionControlAuthenticationStrategy;
    }

    public void setConcurrentSessionControlAuthenticationStrategy(ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy) {
        this.concurrentSessionControlAuthenticationStrategy = concurrentSessionControlAuthenticationStrategy;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return this.getRequiresAuthenticationMatcher().matches(exchange) //路径匹配
                .filter(matchResult -> matchResult.isMatch()) //对匹配结果判断！
                .flatMap(matchResult -> this.getAuthenticationConverter().convert(exchange)) //根据请求参数生成.AuthenticationToken.
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))//判断是否值
                .flatMap(token -> authenticate(exchange, chain, token));//用户认证(调用authenticationManager)
    }

    private Mono<Void> authenticate(ServerWebExchange exchange,
                                    WebFilterChain chain, Authentication token) {
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        return this.authenticationManager.authenticate(token, exchange)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalStateException("No provider found for " + token.getClass()))))
                .flatMap(authentication -> onAuthenticationSuccess(authentication, webFilterExchange))
                .onErrorResume(AuthenticationException.class, e -> this.authenticationFailureHandler
                        .onAuthenticationFailure(webFilterExchange, e));
    }

    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);

        return this.securityContextRepository.save(exchange, securityContext)
                .then(this.authenticationSuccessHandler
                        .onAuthenticationSuccess(webFilterExchange, authentication))
                .then(
                        Mono.justOrEmpty(
                                this.concurrentSessionControlAuthenticationStrategy
                        ).flatMap((concurrentSessionControlAuthenticationStrategy) -> {
                            return concurrentSessionControlAuthenticationStrategy.onAuthentication(authentication, exchange);
                        })
                ).subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    /**
     * Sets the repository for persisting the SecurityContext. Default is {@link NoOpServerSecurityContextRepository}
     *
     * @param securityContextRepository the repository to use
     */
    public void setSecurityContextRepository(
            ServerSecurityContextRepository securityContextRepository) {
        Assert.notNull(securityContextRepository, "securityContextRepository cannot be null");
        this.securityContextRepository = securityContextRepository;
    }

    /**
     * Sets the authentication success handler. Default is {@link WebFilterChainServerAuthenticationSuccessHandler}
     *
     * @param authenticationSuccessHandler the success handler to use
     */
    public void setAuthenticationSuccessHandler(ServerAuthenticationSuccessHandler authenticationSuccessHandler) {
        Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    /**
     * Sets the failure handler used when authentication fails. The default is to prompt for basic authentication.
     *
     * @param authenticationFailureHandler the handler to use. Cannot be null.
     */
    public void setAuthenticationFailureHandler(
            ServerAuthenticationFailureHandler authenticationFailureHandler) {
        Assert.notNull(authenticationFailureHandler, "authenticationFailureHandler cannot be null");
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    /**
     * 身份转换器
     *
     * @return
     */
    public abstract ServerAuthenticationConverter getAuthenticationConverter();

    /**
     * 路径匹配器
     *
     * @return
     */
    public abstract ServerWebExchangeMatcher getRequiresAuthenticationMatcher();
}
