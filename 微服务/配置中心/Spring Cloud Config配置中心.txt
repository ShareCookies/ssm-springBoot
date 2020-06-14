前言：
	随着线上项目变的日益庞大，每个项目都散落着各种配置文件，配置文件随着服务增加而不断增多。
	当某一个基础服务信息变更，需要进行一系列的更新和重启，运维苦不堪言也容易出错。
	配置中心便是解决此类问题的灵丹妙药。

Spring Cloud Config：
	Spring Cloud Config项目是一个解决分布式系统配置的管理方案。
	它包含了Client和Server两个部分。
		Spring cloud使用git或svn存放配置文件，默认情况下使用git。
		server负责将git（svn）中存储的配置文件发布成REST接口.
		client通过接口获取数据、并依据此数据初始化自己的应用。

Spring Cloud Config的简易使用：

	配置文件的存放：
		通过git存放配置文件：
			在github上面创建了一个文件夹config-repo用来存放配置文件。
			例：
				为了模拟生产环境，在config-repo下创建三个配置文件。
				每个配置文件中都写一个属性neo.hello,属性值分别是 hello im dev/test/pro 
				// 开发环境
				neo-config-dev.properties
				// 测试环境
				neo-config-test.properties
				// 生产环境
				neo-config-pro.properties
		通过svn存放配置文件:
			...
	server端：
		服务端为客户端（各微服务项目）提供配置信息。
		负责将git（svn）中存储的配置文件发布成REST接口.
		1、添加依赖
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-config-server</artifactId>
			</dependency>

		2、配置文件
		server:
		  port: 8001
		spring:
		  application:
			name: spring-cloud-config-server
		  cloud:
			config:
			  server:
				git:
				  uri: https://github.com/ityouknow/spring-cloud-starter/     # 配置git仓库的地址
				  search-paths: config-repo                             # git仓库地址下的相对地址，可以配置多个，用,分割。
				  username:                                             # git仓库的账号
				  password:                                             # git仓库的密码
		
		附：
			Spring Cloud Config也提供本地存储配置的方式：
				1.只需要设置属性spring.profiles.active=native，Config Server会默认从应用的src/main/resource目录下检索配置文件。
				2.也可以通过spring.cloud.config.server.native.searchLocations=file:E:/properties/属性来指定配置文件的位置。
		3、启动类
			启动类添加@EnableConfigServer，激活对配置中心的支持
		4.测试
			首先测试server端是否可以读取到github上面的配置信息。
			直接访问：			
				http://localhost:8001/配置文件
				会返回配置文件的信息，
				例
					http://localhost:8001/neo-config-dev.properties
		附：
			1.server端会自动读取最新提交的内容
				是用git存储才会自动读取吗。我用本地存储，修改了不自动读取要重启。
			2.仓库中的配置文件会被转换成web接口，访问可以参照以下的规则：
				/{application}/{profile}[/{label}]
				/{application}-{profile}.yml
				/{label}/{application}-{profile}.yml
				/{application}-{profile}.properties
				/{label}/{application}-{profile}.properties
				例：
					以neo-config-dev.properties为例子，
					它的application是neo-config，profile是dev。
					label？#对应git的分支。如果配置中心使用的是本地存储，则该参数无用
	client端：
		各业务项目中添加config客户端，来从config服务端获取配置信息。

		1、添加依赖
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-starter-config</artifactId>
			</dependency>
		2、配置文件
			新增一个配置文件bootstrap.properties
			bootstrap.properties如下：
				#对应配置文件的 {application}部分 #对应配置文件rest化的 {application}部分
				spring.cloud.config.name=neo-config
				#对应配置文件的 {profile}部分#对应配置文件rest化的 {profile}部分
				spring.cloud.config.profile=dev
				#配置中心服务端地址
				spring.cloud.config.uri=http://localhost:8001/
				#对应git的分支。如果配置中心使用的是本地存储，则该参数无用
				spring.cloud.config.label=master
			附：
				config的相关配置要先于application.properties加载才行，
				而bootstrap.properties的加载是先于application.properties的。
				所以bootstrap.properties中必须要有以上属性，不然config的相关配置无法正确加载。
		4、测试
			使用@Value注解来获取配置中心配置文件的值
			@RestController
			class HelloController {
				@Value("${neo.hello}")
				private String hello;

				@RequestMapping("/hello")
				public String from() {
					return this.hello;
				}
			}

	refresh:
		介绍：
			github配置文件信息更新后，你会发现客户端项目的配置参数并没有实时更新。
			因为springboot项目只有在启动的时候才会获取配置文件的值。
			这个问题可以通过引入refresh功能来解决，引入refresh后每个客户端可以通过请求来触发各自的refresh功能，来更新客户端客户端配置。
		refressh功能的引入：
			1、客户端添加依赖
			<dependency>
			  <groupId>org.springframework.boot</groupId>
			  <artifactId>spring-boot-starter-actuator</artifactId>
			</dependency>
			spring-boot-starter-actuator可以监控程序在运行时状态，具有一套监控的功能，其中就包括/refresh的功能。
			2、 开启更新机制
			在需要加载配置变量的类上面加载@RefreshScope，当执行POST http://客户端地址/refresh的时候，对应客户端就会更新此类下面的变量值。

			@RestController
			@RefreshScope // 使用该注解的类，会在接到refresh刷新请求时自动将新的配置更新到该类对应的字段中。
			class HelloController {
				...
			}
			3、测试
				关闭安全认证：
					在配置文件application.properties添加：
						management.security.enabled=false #springboot 1.5.X 以上默认开通了安全认证,所以要先关掉
				修改配置信息，用该请求去刷新对应客户端的 POST http://客户端地址/refresh
		自动更新：
			这样子还是需要每次手动刷新各个客户端也很麻烦
			方式1.可以配合github的WebHook等来实现自动更新各个客户端。
				但这个方式当客户端多的时候，webhook的配置也麻烦
			方式2（推荐）.使用消息总线spring cloud bus来刷新：
				http://www.ityouknow.com/springcloud/2017/05/26/springcloud-config-eureka-bus.html
				1、添加依赖
					<!-- 引入spring-cloud-starter-bus-amqp包，增加对消息总线的支持(spring cloud bus刷新时使用) -->
					<dependency>
						<groupId>org.springframework.cloud</groupId>
						<artifactId>spring-cloud-starter-bus-amqp</artifactId>
					</dependency>
				2.
					#增加RebbitMq的相关配置，关闭安全验证。这样server端代码就改造完成了。
					spring:
					  rabbitmq:
						host: 192.168.0.6
						port: 5672
						username: admin
						password: 123456
					management:
					  security:
						 enabled: false
				3.客户端原有基础也添加以上配置
				3、测试
					触发server端bus/refresh.看客户端是否拿到了最新配置文件的信息
					curl -X POST http://localhost:8001/bus/refresh				 
				附：
					spring cloud bus使用rabbitmq等作消息组件
	附：
		使用svn+Spring Cloud Config来做配置中心：
			http://www.ityouknow.com/springcloud/2017/05/23/springcloud-config-svn-refresh.html
Spring Cloud Config服务化和高可用：
	把配置中心注册到注册中心，客户端去配置中心获取配置中心服务。
	./配置中心服务化.txt
