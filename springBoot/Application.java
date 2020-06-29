package com.china.hcg.eas.business;


import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author
 * <p>
 * 该类的作用是提供spring配置
 * @Configuration 标识该类是一个spring配置类，该配置类会被springBoot自动扫描并加载
 * @ComponentScan 默认扫描该包及子包下的spring的组件
 * @MappserScan 扫描指定包的mybatis mapper
 * @EnableTransactionManagement 启用事务管理
 * @EnableCaching 启用缓存
 * @EnableAutoConfiguration 启用spring boot的自动配置
 */
@Configuration
@ComponentScan
//@MapperScan({"com.china.hcg.eas.business.*.mapper"})
@EnableTransactionManagement
@EnableCaching
@EnableAutoConfiguration
// 导入别的jar包（pom引入的项目）的配置文件
//@Import({UserWebConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

// 注册一个bean到spring容器
//    @Bean
//    public RedisConnectionFactory jedisConnectionFactory() {
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
//                .master("mymaster")
//                .sentinel("106.54.209.129", 6370);
//        return new JedisConnectionFactory(sentinelConfig);
//    }

}
