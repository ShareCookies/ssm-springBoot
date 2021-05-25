/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rongji.egov.security.gateway;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationFilter {
    public UsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            ServerSecurityContextRepository securityContextRepository,
            ServerAuthenticationSuccessHandler authenticationSuccessHandler,
            ServerAuthenticationFailureHandler authenticationFailureHandler,
            ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy) {
        super(authenticationManager, securityContextRepository, authenticationSuccessHandler, authenticationFailureHandler, concurrentSessionControlAuthenticationStrategy);
    }

    private ServerAuthenticationConverter authenticationConverter = new ServerAuthenticationConverter() {
        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            return Mono.zip(
                    exchange.getSession(),
                    exchange.getFormData()
            ).map(tuple -> {
                WebSession session = tuple.getT1();
                MultiValueMap<String, String> data = tuple.getT2();
                String username = data.getFirst("username");
                String password = data.getFirst("password");
                String captcha = data.getFirst("captcha");
                // 账户类型
                String accountType = data.getFirst("accountType");
                if (username == null) {
                    username = "";
                }

                if (password == null) {
                    password = "";
                }

                if (captcha == null) {
                    captcha = "";
                }


                username = username.trim();
                captcha = captcha.trim();
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                authRequest.setAccountType(accountType);
                authRequest.setCaptcha(captcha);
//                authRequest.setDetails(details);
                return authRequest;
            });
        }
    };

    private ServerWebExchangeMatcher requiresAuthenticationMatcher = new PathPatternParserServerWebExchangeMatcher("/login", HttpMethod.POST);

    @Override
    public ServerAuthenticationConverter getAuthenticationConverter() {
        return this.authenticationConverter;
    }

    @Override
    public ServerWebExchangeMatcher getRequiresAuthenticationMatcher() {
        return this.requiresAuthenticationMatcher;
    }
}
