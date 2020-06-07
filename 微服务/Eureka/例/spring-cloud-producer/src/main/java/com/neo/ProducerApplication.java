package com.neo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//启用服务注册与发现。
//添加@EnableDiscoveryClient注解后，此时这个项目就会被注册到eureka服务中心，客户端就可以访问到改项目中所有的controller了。
@EnableDiscoveryClient
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}
}
