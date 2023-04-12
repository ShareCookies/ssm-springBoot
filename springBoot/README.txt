官网：
	https://spring.io/projects/spring-boot/
	spring官方文档：
		所有文档地址：https://docs.spring.io/spring-boot/docs
		最新文档地址：https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-kotlin
介绍:
	虽然Spring的组件代码是轻量级的，但它的配置却是重量级的。
	Spring Boot 是由 Pivotal 团队提供用来简化 Spring 的搭建和开发过程的全新框架。
	Spring Boot 去除了大量的 xml 配置文件，简化了复杂的依赖管理，配合各种 starter 使用，基本上可以做到自动化配置。
		Spring Boot 靠 Starter依赖来做到依赖管理和自动化配置的。
	hcg：
		使用Spring boot+ssm，比只用ssm（Spring+SpringMVC+Mybatis）简单许多了。
		只用ssm你还需要springmvc调度器等的配置，但用Spring Boot你只要引入start依赖和用少量的注解即可搭建一个springWeb等应用。
		附：
			1.其实只用Spring boot也可以进行web开发，但是对于多表多条件分页查询，Spring boot就有点麻烦了，所以整合了Mybatis。
			2.Spring boot内置了类似tomcat这样的中间件，所以，只要运行DemoApplication中的main方法就可以启动项目了。
使用SpringBoot构建应用：p647
	使用 Maven 搭建一个Spring Boot 项目:
		1.新建一个 基本 Maven 项目结构：
			src\main\java (项目java源码文件)
			src\main\resources (项目资源文件)
			pom.xml (maven项目构建说明文件)
			maven规定项目要为以上这种结构。
		然后编辑 pom.xml 来引入 基于springBoot 的web项目所需依赖：
			<!-- springBoot web依赖 -->
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-web</artifactId>
			</dependency>
			<!-- springBoot mybatis依赖 -->
			<dependency>
				<groupId>org.mybatis.spring.boot</groupId>
				<artifactId>mybatis-spring-boot-starter</artifactId>
				<version>1.3.1</version>
			</dependency>
			<!-- mysql数据库配置 begin -->
			<dependency>
				<groupId>mysql</groupId>
				<artifactId>mysql-connector-java</artifactId>
			</dependency>
		2.包中创建一个springBoot启动类
			详：
				./SpringBoot启动类与配置文件.txt
			附：
				1.Spring Boot应用无需任何配置，但需要个特殊的类来启动（运行main方法）Spring Boot应用。
				2.到这里SpringBoot 已经搭建完成，但是没有控制权可供访问。
		3.controller 包创建一个基本的springMVC控制器类，
			./spring/springMVC/README.txt goto: 4.编写控制器
			附：
				到这里web网站已建好
				默认情况下springBoot内嵌tomcat的端口为 8080（可在springboot配置文件中更改端口） ，以http: //localhost:8080访问
		4.集成mybaits：
			1.引入springBoot mybatis依赖 2.编写mapper文件 3.springBoot启动类使用注解扫描到mapper即可。
		附：
			到这一步本地跑完全没问题了。
		5.构建项目：（打包）
			1.构建成jar包：
				clean package spring-boot:repackage -Dmaven.test.skip=true --update-snapshots
				pom.xml新增插件：
				<plugin>
					<!--https://blog.csdn.net/lwj_199011/article/details/54881277-->
					<!--1.使用了该插件springboot的jar包中 MANIFEST.MF文件里面才会有springboot启动类的信息。-->
					<!--2.打的jar包里面才会有maven依赖的jar包和spring boot的启动类。-->
					<!--注：-->
					<!--1.不加该插件 jar运行报错:no main manifest attribute，就不能独立启动-->
					<!--spring boot jar包形式加上这个插件，才可以使用Java -jar命令来启动jar包-->
					<!--2.如果用了spring boot但是不需要独立启动，就不要加这个插件，否则spring boot会因为找不到启动类而导致报错。-->
					<!--3. 在用idea调试的时候加不加插件都可以启动，看不出来不同，所以必须要独立启动jar包才可以看出来。-->
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
				</plugin>
			2.构建成war包：
				p658
		6.运行项目：
			运行jar包：
				按照传统的方式，这意味着要将应用的WAR文件部署到Servlet容器中，如Tomcat或WebSphere。
				但是在这里，我们甚至没有WAR文件，只有构建形成的是一个JAR文件。
				我们可以按照如下的方式从命令行运行它：
					nohup java -jar ...jar
				应用已经启动完成。
	附：
		Spring Initializr：
			Spring Initializr 但它能为你提供一个基本的Spring Boot项目结构，以及一个用于构建代码的Maven或Gradle构建说明文件。
				Spring Initializr从本质上来说就是一个Web应用程序，
			Spring Initializr有几种用法。
				通过Web界面使用。
					https://start.spring.io/
				通过Spring Tool Suite使用。
				通过IntelliJ IDEA使用。
					https://blog.csdn.net/wya1993/article/details/79578677
				使用Spring Boot CLI使用。
附：
Spring Boot四个特性:
	Spring Boot提供的四个特性来简化Spring应用的开发。
	Starter依赖：（起步依赖）
		SpringBootStarter将常用的依赖分组进行了整合，将其合并到一个依赖中，这样就可以一次性添加到项目的Maven或Gradle构建中。
		解析：
			起步依赖就是利用Maven和Gradle的传递依赖解析，把常用库聚合在一起，组成了几个为特定功能而定制的依赖。
		SpringBoot所有Starter依赖：
			p640
		注：
			1.比起减少依赖数量，起步依赖还引入了一些微妙的变化。
			向项目中添加了Web起步依赖，实际上指定了应用程序所需的一类功能。
			如果需要安全功能，那就加入security起步依赖。
			简而言之，你不再需要考虑支持某种功能要用什么库了，引入相关起步依赖就行。
			2.此外，Spring Boot的起步依赖还把你从“需要这些库的哪些版本”这个问题里解放了出来。
			起步依赖引入的库的版本都是经过测试的，因此你可以完全放心，它们之间不会出现不兼容的情况。
		附录：
			依赖传递：
				依赖关系为：C—>B—>A。
				那么我们执行项目C时，会自动把B、A都下载导入到C项目的jar包文件夹中。
	自动配置：p643（约定优先于配置）
		Spring Boot的自动配置功能削减了Spring配置的数量。
		它在实现时，会考虑应用中的其他因素并推断你所需要的Spring配置，然后帮你自动配置。
		即自动配置利用了Spring 4对条件化配置的支持，合理地推测应用所需的bean并自动化配置它们；
		例：
			如果Spring Boot的Web自动配置探测到 Spring MVC位于类路径下，
			它将会自动配置支持Spring MVC的多个 bean，包括视图解析器、资源处理器以及消息转换器等。

	附：
		starter原理：
			如果我要使用redis，我直接引入redis驱动jar包就行了，何必要引入starter包。
			starter和普通jar包的区别在于，它能够实现自动配置，和Spring Boot无缝衔接。
			https://www.jianshu.com/p/30ce49fc2f25

			./自动配置实现原理
	Actuator：p665
		让你能够深入运行中的Spring Boot应用程序，一探究竟。
		只需将Actuator Starter依赖添加到项目中，即可启用Actuator。
		例：
			./附属功能/健康监控/
			启用Actuator后，如果想查看Spring应用上下文中所有的bean，那么可以访问http://localhost:8080/beans。
			
	命令行界面：
		Spring Boot CLI（command-line interface，命令行界面）是Spring Boot的非必要组成部分。
		虽然它为Spring带来了惊人的力量，大大简化了开发，但也引入了一套不太常规的开发模型。
	附：
		从本质上来说，Spring Boot就是做了spring的样板配置，常说的Spring Boot项目就用到了起步依赖和自动配置而已。
附:
	学习时第三方框架或应用时推荐原生，使用时推荐结合springboot：
		只有原生使用才能理解其大概使用机制。
		使用springboot那么类似是黑盒操作，由于你对改知识并不是很熟悉中间有问题，跟踪起来就较麻烦。



