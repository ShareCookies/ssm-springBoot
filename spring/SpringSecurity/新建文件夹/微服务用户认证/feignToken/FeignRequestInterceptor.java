package com.rongji.egov.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author daihuabin
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(FeignRequestInterceptor.class);

    private ObjectMapper objectMapper;

    public FeignRequestInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String xPrincipal = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 取请求的header中的身份
            xPrincipal = request.getHeader("X-Principal");
        }

        if (xPrincipal == null) {
            // fix: 请求可能会被主线程销毁，所以取当前用户身份序列化
            SecurityContext context = SecurityContextHolder.getContext();
            if (context != null && context.getAuthentication() != null && context.getAuthentication().getPrincipal() != null) {
                try {
                    xPrincipal = this.objectMapper.writeValueAsString(context.getAuthentication().getPrincipal());
                } catch (JsonProcessingException e) {
                    logger.warn("X-Principal序列化失败", e);
                }
            }
        }
        if (xPrincipal != null) {
            //添加token
            requestTemplate.header("X-Principal", xPrincipal);
        }
    }
}
