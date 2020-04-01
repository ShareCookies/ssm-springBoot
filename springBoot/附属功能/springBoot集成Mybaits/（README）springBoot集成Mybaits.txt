springBoot集成Mybaits：
	1.导入依赖
        <!-- mybatis依赖 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
		介绍：
			MyBatis 为了方便 SpringBoot 集成 MyBatis 专门提供了 个符合 SpringBoot规范的starter项目 mybatis- spring-boot- starter.
			只需要在 pom.xml 添加上面的依赖即可 ，符合 Spring Boot 规范的 starter 依赖都会，
			按照默认的配置方式在系统启动时自动配置好，因此不用对 MyBatis 进行任何额外的配置，MyBatis就集成好了。
	2.配置文件
		在 src/main/resources 目录新建配置文件 application.properties（或application.yml）。
		1.在配置文件中添加以下配置：
			1.配置数据库连接信息：
			spring:
			  datasource:
				driver-class-name: com.mysql.jdbc.Driver
				url:  jdbc:mysql://ip:3306/数据库?characterEncoding=utf8&useSSL=false
				useSSL: false
				username: root
				password: root	
			有了以上配置信息后Spring Boot 就可以自动配置数据源了。
	3.编程实现操作数据库：
		./mybaits使用接口编程.txt
附：
	spring boot 打印mybatis sql日志：
		可以在你的application.properties文件里添加代码
		logging:
		  level:
			com.test.business: debug
		#com.test.business 是包名
		https://blog.csdn.net/Dongguabai/article/details/80742219
	Spring boot启动报错(If you want an embedded database , please put it on the classpath)：	
		https://blog.csdn.net/qq_27046951/article/details/82850394