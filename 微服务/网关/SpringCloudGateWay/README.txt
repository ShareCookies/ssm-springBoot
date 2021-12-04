https://cloud.spring.io/spring-cloud-gateway/reference/html/

Spring Cloud Gateway介绍：
	Spring Cloud Gateway 是 Spring Cloud 的一个全新项目，目标是替代 Netflix Zuul，
	其不仅提供统一的路由方式，并且基于 Filter 链的方式提供了网关基本的功能，例如：安全，监控/指标，和限流。
		Spring Cloud Gateway 基于 Spring 5.0，Spring Boot 2.0 和 Project Reactor 等技术开发，使用 netty+webflux 实现
	
相关概念:
	Route（路由）：
		The basic building block of the gateway. 
			这是网关的基本构建块。
		It is defined by an ID, a destination URI, a collection of predicates, and a collection of filters. 
			它由一个 ID，一个目标 URI，一组断言和一组过滤器定义。
		A route is matched if the aggregate predicate is true.	
			如果断言为真则路由匹配
	Predicate（断言）：
		This is a Java 8 Function Predicate. The input type is a Spring Framework ServerWebExchange.
		This lets you match on anything from the HTTP request, such as headers or parameters.
		
		Predicate就是指 Java 8 的 Predicate。
		在 Spring Cloud Gateway 中 Spring 利用 Predicate 的特性实现了各种路由匹配规则。
			如通过 Header、请求参数等不同的条件来进行作为条件匹配到对应的路由。（如果断言为真，则路由匹配。）
		附：
			Predicate 介绍
				Predicate 是 Java 8 中引入的一个内置函数接口，Predicate 接受一个输入参数，返回一个布尔值结果。
					
			网上有一张图总结了 Spring Cloud 内置的几种 Predicate 的实现：
				...
			
	Filter（过滤器）：
		These are instances of Spring Framework GatewayFilter that have been constructed with a specific factory. 
		Here, you can modify requests and responses before or after sending the downstream request.
		
		Filter可以修改请求和响应。
网关工作原理：
	网关工作原理图：
		spring_cloud_gateway_diagram.png
	介绍：
		Clients make requests to Spring Cloud Gateway. 
		If the Gateway Handler Mapping determines that a request matches a route, it is sent to the Gateway Web Handler. 
		This handler runs the request through a filter chain that is specific to the request. 
		The reason the filters are divided by the dotted line is that filters can run logic both before and after the proxy request is sent.
			过滤器由虚线分隔的原因是,过滤器可以在发送代理请求之前和之后运行逻辑。
		All “pre” filter logic is executed. Then the proxy request is made. After the proxy request is made, the “post” filter logic is run.
			？
				“post” filter 被执行，在代理请求被建立后，
				就是指修改response吗？
				还是指代理请求被建立？
				还是指代理请求被执行完？
路由：
	附：
		URIs defined in routes without a port get default port values of 80 and 443 for the HTTP and HTTPS URIs, respectively.
4. Configuring Route Predicate Factories and Gateway Filter Factories

	There are two ways to configure predicates and filters: shortcuts and fully expanded arguments. Most examples below use the shortcut way.
		快捷方式和全扩展参数
	The name and argument names will be listed as code in the first sentance or two of the each section. The arguments are typically listed in the order that would be needed for the shortcut configuration.
	4.1Shortcut Configuration：
		application.yml
		
		spring:
		  cloud:
			gateway:
			  routes:
			  - id: after_route
				uri: https://example.org
				predicates:
				- Cookie=mycookie,mycookievalue
	4.2. Fully Expanded Arguments
		Fully expanded arguments appear more like standard yaml configuration with name/value pairs. 
		Typically, there will be a name key and an args key.
		The args key is a map of key value pairs to configure the predicate or filter.
			args键是用于配置谓词或过滤器的键值对的映射。
			？
				to 介词，这里前面应该是名词(还是句子了)
		例：
			application.yml
			spring:
			  cloud:
				gateway:
				  routes:
				  - id: after_route
					uri: https://example.org
					predicates:
					- name: Cookie
					  args:
						name: mycookie
						regexp: mycookievalue
	
			这是上面显示的Cookie谓词的快捷方式配置的完整配置。
			This is the full configuration of the shortcut configuration of the Cookie predicate shown above.
7.1. Combined Global Filter and GatewayFilter Ordering
When a request matches a route, the filtering web handler adds all instances of GlobalFilter and all route-specific instances of GatewayFilter to a filter chain. 
This combined filter chain is sorted by the org.springframework.core.Ordered interface, which you can set by implementing the getOrder() method.

As Spring Cloud Gateway distinguishes between “pre” and “post” phases for filter logic execution (see How it Works), the filter with the highest precedence is the first in the “pre”-phase and the last in the “post”-phase.
	由于Spring Cloud Gateway区分了执行过滤器逻辑的“前”阶段和“后”阶段（请参见工作原理），因此具有最高优先级的过滤器在“前”阶段中是第一个，在“后”阶段中是最后一个， 阶段。
The following listing configures a filter chain:			
Example 53. ExampleConfiguration.java
	@Bean
	public GlobalFilter customFilter() {
		return new CustomGlobalFilter();
	}

	public class CustomGlobalFilter implements GlobalFilter, Ordered {

		@Override
		public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
			log.info("custom global filter");
			return chain.filter(exchange);
		}

		@Override
		public int getOrder() {
			return -1;
		}
	}


快速上手：
	1.添加依赖：
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
		
	2.网关实现转发功能：
		Spring Cloud Gateway 网关路由有两种配置方式：
			1.在配置文件 yml 中配置
			2.通过@Bean自定义 RouteLocator，在启动主类 Application 中配置
			这两种方式是等价的，建议使用 yml 方式进配置。
		方式1. 通过配置实现转发功能：
			server:
			  port: 8080
			spring:
			  cloud:
				gateway:
				  routes:
				  - id: neo_route
					uri: http://www.ityouknow.com
					predicates:
					- Path=/spring-cloud
			各字段含义如下：
				id：我们自定义的路由 ID，保持唯一
				uri：目标服务地址
				predicates：路由条件，Predicate 接受一个输入参数，返回一个布尔值结果。该接口包含多种默认方法来将 Predicate 组合成其他复杂的逻辑（比如：与，或，非）。
				filters：过滤规则，本示例暂时没用。
				
			上面这段配置的意思是：
				配置了一个 id 为 neo_route 的路由规则，
				当访问地址 http://localhost:8080/spring-cloud时会自动转发到地址：http://www.ityouknow.com/spring-cloud。
		方式2. 通过代码来实现转发功能：
			在启动类 GateWayApplication 中添加方法 customRouteLocator() 来定制转发规则。
			@SpringBootApplication
			public class GateWayApplication {

				public static void main(String[] args) {
					SpringApplication.run(GateWayApplication.class, args);
				}

				
				@Bean
				public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
					return builder.routes()
							//配置了一个 id 为 path_route 的路由，
							//当访问地址http://localhost:8080/about时会自动转发到地址：http://www.ityouknow.com/about
							.route("path_route", r -> r.path("/about")
									.uri("http://ityouknow.com"))
							.build();
				}

			}
		上面两个示例中 uri 都是指向了原作者博客网站，在实际项目使用中可以将 uri 指向对外提供服务的项目地址，统一对外输出接口。
					
路由规则
	上面只是使用了 predicates 进行了简单的条件匹配， Spring Cloud Gataway 内置了很多 路由 Predicates 实现。

	通过时间匹配
		Predicate 支持设置一个时间，在请求进行转发的时候，可以通过判断在这个时间之前或者之后进行转发。
		可以用在限时抢购的一些场景中。	
		例：
			比如我们现在设置只有在请求时间在 2018年1月20日6点6分6秒之后的请求都转发到地址http://ityouknow.com。
			在之前的页面会报 404 没有找到地址
			spring:
			  cloud:
				gateway:
				  routes:
				   - id: time_route
					uri: http://ityouknow.com
					predicates:
					 - After=2018-01-20T06:06:06+08:00[Asia/Shanghai]
			
			After Route Predicate：
				是指在这个时间之后的请求都转发到目标地址。
			Before Route Predicate：
				刚好相反，在某个时间之前的请求的请求都进行转发。
				例：
					- Before=2018-01-20T06:06:06+08:00[Asia/Shanghai]
			Between Route Predicate：
				限制路由请求在某一个时间段范围内
				例：
					- Between=2018-01-20T06:06:06+08:00[Asia/Shanghai], 2019-01-20T06:06:06+08:00[Asia/Shanghai]
			时间设置：
				+08:00是指时间和UTC时间相差八个小时，时间地区为Asia/Shanghai。
			
				附：
					Spring 是通过 ZonedDateTime 来对时间进行的对比，
					ZonedDateTime 是 Java 8 中用于表示带时区的日期与时间信息的类。
					ZonedDateTime 支持通过时区来设置时间，中国的时区是：Asia/Shanghai。

	通过 Cookie 匹配
		Cookie Route Predicate 可以接收两个参数，一个是 Cookie name和一个是正则表达式，
		路由规则会获取对应的 Cookie name 值和正则表达式去匹配，如果匹配上就会执行路由，如果没有匹配上则不执行。

		spring:
		  cloud:
			gateway:
			  routes:
			   - id: cookie_route
				 uri: http://ityouknow.com
				 predicates:
				 - Cookie=ityouknow, kee.e
		使用 curl 测试，命令行输入:
			curl http://localhost:8080 --cookie "ityouknow=kee.e"
			则会返回页面代码，如果去掉--cookie "ityouknow=kee.e"，后台汇报 404 错误。
	通过 Header 属性匹配
		Header Route Predicate 可接收 2 个参数，一个 header 中属性名称和一个正则表达式，这个属性值和正则表达式匹配则执行。

		spring:
		  cloud:
			gateway:
			  routes:
			  - id: header_route
				uri: http://ityouknow.com
				predicates:
				- Header=X-Request-Id, \d+
		使用 curl 测试，命令行输入:
			curl http://localhost:8080  -H "X-Request-Id:666666" 
			则返回页面代码证明匹配成功。将参数-H "X-Request-Id:666666"改为-H "X-Request-Id:neo"再次执行时返回404证明没有匹配。
	通过 Host 匹配
		Host Route Predicate 接收一组参数，一组匹配的域名列表，这个模板是一个 ant 分隔的模板，用.号作为分隔符。它通过参数中的主机地址作为匹配规则。

		spring:
		  cloud:
			gateway:
			  routes:
			  - id: host_route
				uri: http://ityouknow.com
				predicates:
				- Host=**.ityouknow.com
		使用 curl 测试，命令行输入:

		curl http://localhost:8080  -H "Host: www.ityouknow.com" 
		curl http://localhost:8080  -H "Host: md.ityouknow.com" 
		经测试以上两种 host 均可匹配到 host_route 路由，去掉 host 参数则会报 404 错误。
	通过请求方式匹配
		可以通过是 POST、GET、PUT、DELETE 等不同的请求方式来进行路由。

		spring:
		  cloud:
			gateway:
			  routes:
			  - id: method_route
				uri: http://ityouknow.com
				predicates:
				- Method=GET
		使用 curl 测试，命令行输入:

		# curl 默认是以 GET 的方式去请求
		curl http://localhost:8080
		测试返回页面代码，证明匹配到路由，我们再以 POST 的方式请求测试。

		# curl 默认是以 GET 的方式去请求
		curl -X POST http://localhost:8080
		返回 404 没有找到，证明没有匹配上路由
	通过请求路径匹配
		Path Route Predicate 接收一个匹配路径的参数来判断是否走路由。

		spring:
		  cloud:
			gateway:
			  routes:
			  - id: host_route
				uri: http://ityouknow.com
				predicates:
				- Path=/foo/{segment}
		如果请求路径符合要求，则此路由将匹配，例如：/foo/1 或者 /foo/bar。

		使用 curl 测试，命令行输入:

		curl http://localhost:8080/foo/1
		curl http://localhost:8080/foo/xx
		curl http://localhost:8080/boo/xx
		经过测试第一和第二条命令可以正常获取到页面返回值，最后一个命令报404，证明路由是通过指定路由来匹配。
	通过请求参数匹配
		Query Route Predicate 支持传入两个参数，一个是属性名一个为属性值，属性值可以是正则表达式。

		spring:
		  cloud:
			gateway:
			  routes:
			  - id: query_route
				uri: http://ityouknow.com
				predicates:
				- Query=smile
		这样配置，只要请求中包含 smile 属性的参数即可匹配路由。

		使用 curl 测试，命令行输入:

		curl localhost:8080?smile=x&id=2
		经过测试发现只要请求汇总带有 smile 参数即会匹配路由，不带 smile 参数则不会匹配。

		还可以将 Query 的值以键值对的方式进行配置，这样在请求过来时会对属性值和正则进行匹配，匹配上才会走路由。

		spring:
		  cloud:
			gateway:
			  routes:
			  - id: query_route
				uri: http://ityouknow.com
				predicates:
				- Query=keep, pu.
		这样只要当请求中包含 keep 属性并且参数值是以 pu 开头的长度为三位的字符串才会进行匹配和路由。

		使用 curl 测试，命令行输入:

		curl localhost:8080?keep=pub
		测试可以返回页面代码，将 keep 的属性值改为 pubx 再次访问就会报 404,证明路由需要匹配正则表达式才会进行路由。
	通过请求 ip 地址进行匹配
		Predicate 也支持通过设置某个 ip 区间号段的请求才会路由，RemoteAddr Route Predicate 接受 cidr 符号(IPv4 或 IPv6 )字符串的列表(最小大小为1)，例如 192.168.0.1/16 (其中 192.168.0.1 是 IP 地址，16 是子网掩码)。

		spring:
		  cloud:
			gateway:
			  routes:
			  - id: remoteaddr_route
				uri: http://ityouknow.com
				predicates:
				- RemoteAddr=192.168.1.1/24
		可以将此地址设置为本机的 ip 地址进行测试。

		curl localhost:8080
		果请求的远程地址是 192.168.1.10，则此路由将匹配。
	组合使用
		上面为了演示各个 Predicate 的使用，我们是单个单个进行配置测试，其实可以将各种 Predicate 组合起来一起使用。

		例如：

		spring:
		  cloud:
			gateway:
			  routes:
			   - id: host_foo_path_headers_to_httpbin
				uri: http://ityouknow.com
				predicates:
				- Host=**.foo.org
				- Path=/headers
				- Method=GET
				- Header=X-Request-Id, \d+
				- Query=foo, ba.
				- Query=baz
				- Cookie=chocolate, ch.p
				- After=2018-01-20T06:06:06+08:00[Asia/Shanghai]
		各种 Predicates 同时存在于同一个路由时，请求必须同时满足所有的条件才被这个路由匹配。

		一个请求满足多个路由的谓词条件时，请求只会被首个成功匹配的路由转发
getway微服务化：（getway与网关结合使用）
	快速使用中是网关代理单个服务的使用语法。
	但服务中心往往注册了很多服务，如果每个服务都需要单独配置网关的话，很麻烦。
	Spring Cloud Gateway 提供了一种默认转发的能力，只要将 Spring Cloud Gateway 注册到服务中心，Spring Cloud Gateway 默认就会代理服务中心的所有服务。
	依赖：
		<!-- eureka 的客户端依赖包-->
		<!-- 使用getway建议注册中心升级为Finchley.SR2-->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
	配置文件:application.yml
		server:
		  port: 8888
		spring:
		  application:
			name: cloud-gateway-eureka
		  cloud:
			gateway:
			 discovery:
				locator:
				 enabled: true
		eureka:
		  client:
			service-url:
			  defaultZone: http://localhost:8000/eureka/
		logging:
		  level:
			org.springframework.cloud.gateway: debug
		配置说明：
			spring.cloud.gateway.discovery.locator.enabled
				是否与服务注册与发现组件进行结合，默认为 false。
				开启后将会通过服务中心的自动根据 serviceId 创建路由。
			eureka.client.service-url.defaultZone
				指定注册中心的地址，以便使用服务发现功能
			logging.level.org.springframework.cloud.gateway 
				调整相 gateway 包的 log 级别，以便排查问题
	测试
		1.启动 cloud-gateway-eureka 项目，访问注册中心地址 http://localhost:8000/ 即可看到名为 CLOUD-GATEWAY-EUREKA的服务。

		2.将 Spring Cloud Gateway 注册到服务中心之后，网关会自动代理所有在注册中心的服务，通过网关访问这些服务的语法为：
			http://网关地址：端口/服务中心注册 serviceId/服务具体的url	
			例：http://localhost:8888/SPRING-CLOUD-PRODUCER/hello
网关高级功能：
	均衡负载：
		getway是自动实现负载均衡的。
		例：
			注册中心有两个名为 SPRING-CLOUD-PRODUCER的服务，通过网关http://localhost:8888/SPRING-CLOUD-PRODUCER/hello
			页面交替返回以下信息：
				hello world 1!
				hello world 2!
				说明网关自动进行了均衡负载。
	Gateway  Filter：
		利用 GatewayFilter 可以修改请求的 Http 的请求或者响应，或者根据请求或者响应做一些特殊的限制。
		Gateway 按范围划分：
			Spring Cloud Gateway 的 Filter 分为两种：GatewayFilter 与 GlobalFilter。
			GlobalFilter 会应用到所有的路由上
				Spring Cloud Gateway 内置了9种 GlobalFilter，比如 Netty Routing Filter、Websocket Routing Filter 等
			GatewayFilter 将应用到单个路由或者一个分组的路由上。
		Gateway 按生命周期划分：
			Spring Cloud Gateway 的 Filter 的生命周期不像 Zuul 的那么丰富，它只有两个：“pre” 和 “post”。
				PRE： 
					这种过滤器在请求被路由之前调用。
					我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。
				POST：
					这种过滤器在路由到微服务以后执行。
					这种过滤器可用来为响应添加标准的 HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。
		快速使用Filter：
			例：
				给每个匹配的请求添加上foo=bar参数。
				http://www.ityouknow.com/springcloud/2019/01/19/spring-cloud-gateway-service.html
		Filter的一些常用功能：
			http://www.ityouknow.com/springcloud/2019/01/26/spring-cloud-gateway-limit.html
			修改请求路径的过滤器：
				例：
			限速路由器：
				例：
			熔断路由器：
				Spring Cloud Gateway 也可以利用 Hystrix 的熔断特性，在流量过大时进行服务降级。
				例：
					
			重试路由器：
				例：
				
		
附：
	Gateway中使用SpringSecurity进行网关鉴权与权限控制
		https://www.cnblogs.com/Lyn4ever/p/12702331.html
	zuul网关整合security
		https://blog.csdn.net/Grain_Rain_tx/article/details/105875025
	RouteDefinition
		https://www.jianshu.com/p/b02c7495eb5e
		RouteDefinition路由定义，Spring-Cloud-Gateway通过RouteDefinition来转换生成具体的路由信息。