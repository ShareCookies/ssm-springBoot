Spring Cloud Zuul 微服务化使用：
	url映射的方式来实现zull的转发局限性很大。
		比如每增加一个服务就需要配置一条内容，另外后端的服务如果是动态来提供，就不能采用这种方案来配置了。
	微服务架构中，服务名与服务实例地址的关系在eureka server中已经存在了，
	所以只需要将Zuul注册到eureka server上，去发现其他服务就可以实现对serviceId的映射。
	1、添加依赖
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-eureka</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-zuul</artifactId>
		</dependency>
	2、配置文件：
		spring.application.name=gateway-service-zuul
		server.port=8888
		# 所有访问http://localhost:8888/producer/**的地址，都会转发到spring-cloud-producer
		zuul.routes.api-a.path=/producer/**
		zuul.routes.api-a.serviceId=spring-cloud-producer

		eureka.client.serviceUrl.defaultZone=http://localhost:8000/eureka/
		注：
			网关的默认路由规则：
				spring cloud zuul已经帮我们做了默认配置。
				默认情况下，Zuul会代理所有注册到Eureka Server的微服务，
				并且Zuul的路由规则如下：
				http://ZUUL_HOST:ZUUL_PORT/微服务在Eureka上的serviceId/**会被转发到serviceId对应的微服务。
				例：
					http://localhost:8888/spring-cloud-producer/hello?name=test
					会被转发到spring-cloud-producer微服务。
	3、测试
		启动项目，访问：http://localhost:8888/producer/hello?name=test，返回：hello test，this is first messge。
		说明访问gateway-service-zuul-eureka的请求自动转发到了spring-cloud-producer，并且将结果返回。
	？
		2.producer服务多个，zuul成功调用了producer服务并且做了均衡负载。
		谁做的负载了，eureka还是zuul？
附：
	Spring Cloud Zuul简单使用：
		1、添加依赖
		引入spring-cloud-starter-zuul包
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-zuul</artifactId>
		</dependency>
		
		2、配置文件

		spring.application.name=gateway-service-zuul
		server.port=8888

		#这里的配置表示，访问/it/** 直接重定向到http://www.ityouknow.com/**
		zuul.routes.baidu.path=/it/**
		zuul.routes.baidu.url=http://www.ityouknow.com/
		3、启动类添加@EnableZuulProxy，支持网关路由。

		@SpringBootApplication
		@EnableZuulProxy
		public class GatewayServiceZuulApplication {
			...
		}
		4、测试
			启动gateway-service-zuul-simple项目，在浏览器中访问：http://localhost:8888/it/spring-cloud，
			看到页面返回了：http://www.ityouknow.com/spring-cloud 页面的信息.
