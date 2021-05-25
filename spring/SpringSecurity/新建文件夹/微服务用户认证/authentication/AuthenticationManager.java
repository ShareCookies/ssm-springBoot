package com.rongji.egov.security.gateway;

import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author daihuabin
 */
public interface AuthenticationManager {
    Mono<Authentication> authenticate(Authentication token, ServerWebExchange exchange);
}
