package aop;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @autor hecaigui
 * @date 2020-8-22
 * @description
 */
@Configuration
//启用Aspectj自动代理
@EnableAspectJAutoProxy
@ComponentScan
public class Config {
//    @Bean
//    public Audience audience ( ) {
//        return new Audience() ;
//    }
    public static void main(String[] args) {
        SpringApplication.run(Config.class, args);
    }
}