Spring Cloud学习资料：
	大部分内容是对该博客的学习笔记：http://www.ityouknow.com/spring-cloud.html
	学习资料汇总：http://www.ityouknow.com/springcloud/2016/12/30/springcloud-collect.html
	spring cloud中文官网：https://www.springcloud.cc/
微服务：
	微服务与微服务架构
		微服务架构是一种架构模式(风格)， 
		它提倡将单一应用程序划分成一组小的服务, 
		服务之间互相协调、互相配合,为用户提供最终价值
		。
			即：每个服务运行在其独立的进程中，服务与服务间采用轻量级的通信机制互相协作。
	微服务的优缺点
		优点：扩容方便、服务高内聚 代码松耦合...
		缺点：
			运维更复杂、分布式系统使开发更复杂了点、服务间通信成本
		
Spring Cloud介绍：
	Spring Cloud就是一微服务框架，它是一系列框架的有序集合。
		Spring cloud制定了微服务框架规范，然后它将目前各家公司开发的比较成熟的服务框架组合起来，
		并通过Spring Boot进行再封装屏蔽掉了复杂的配置和实现原理，
		最终给开发者留出了一套简单易懂微服务框架。
	使用springCloud可以便利的搭建出一个微服务框架，来管理一系列的微服务项目。
		微服务:
			微服务是可以独立部署、水平扩展、独立访问（或者有独立的数据库）的服务单元。
Spring Cloud微服务框架的搭建：
	搭建微服务框架至少需要以下几个模块：
		Eureka：用于服务的注册于发现
			./Eureka/README.txt
		Feign：用于支持服务对服务的调用以及均衡负载。
			./Eureka/Eureka使用.txt > springCloud eureka 客户端的使用 > 服务调用
		Spring Cloud Zuul（或Spring Cloud Gateway）  api网关负责与第三方（或前端）调用端的通信。
			./网关/README.txt
		结合以上模块就可搭建一个微服务框架。
	附：
		Spring Cloud Sleuth	分布式系统调用跟踪:???
			http://www.ityouknow.com/springcloud/2018/02/02/spring-cloud-sleuth-zipkin.html
			https://www.cnblogs.com/roadlandscape/p/12934407.html
			使用Spring Cloud Sleuth和Zipkin进行分布式链路跟踪.
			随着业务发展，系统拆分导致系统调用链路愈发复杂一个前端请求可能最终需要调用很多次后端服务才能完成.
			当整个请求变慢或不可用时，我们是无法得知该请求是由那些后端服务引起的，这时就需要快读定位服务故障点，以对症下药。
		错误集成？
		Spring Cloud Config 服务集群配置中心。
			./配置中心/README.txt
		Spring Cloud Bus 集群中传播状态的变化（通常配置变化事件）。
			./配置中心/SpringCloudBus.txt
Spring Cloud附：
	Spring Cloud 各依赖模块简要介绍：
		Spring Cloud和Spring Boot 是什么关系
			Spring Boot 是 Spring 的一套快速配置脚手架，可以基于Spring Boot 快速开发单个微服务（单个spring应用）.
			Spring Cloud是一个基于Spring Boot实现的云应用开发工具；
			Spring Boot专注于快速、方便集成的单个个体，Spring Cloud是关注全局的服务治理框架；
		
		Netflix Eureka
			服务中心
			Eureka 是一个基于 REST 的服务，主要用来 定位服务来进行中间层服务器的负载均衡和故障转移。
			解释：
				这个可是springcloud最牛鼻的小弟，服务中心，任何小弟需要其它小弟支持什么都需要从这里来拿，同样的你有什么独门武功的都赶紧过报道，方便以后其它小弟来调用；它的好处是你不需要直接找各种什么小弟支持，只需要到服务中心来领取，也不需要知道提供支持的其它小弟在哪里，还是几个小弟来支持的，反正拿来用就行，服务中心来保证稳定性和质量。
		Feign：
			一个声明式WebService调用客户端。
			且其已经集成了Ribbon， Hystrix。
			Netflix Hystrix
				熔断器			
			Netflix Ribbon：
				客户端负载均衡服务调用组件
		Netflix Zuul
			Zuul 是在云平台上提供动态路由,监控,弹性,安全等边缘服务的框架。
			Zuul 相当于是设备和 Netflix 流应用的 Web 网站后端所有请求的前门。当其它门派来找大哥办事的时候一定要先经过zuul,看下有没有带刀子什么的给拦截回去，或者是需要找那个小弟的直接给带过去。

		Netflix Archaius
			配置管理API，包含一系列配置管理API，提供动态类型化属性、线程安全配置操作、轮询框架、回调机制等功能。
			可以实现动态获取配置， 原理是每隔60s（默认，可配置）从配置源读取一次内容，这样修改了配置文件后不需要重启服务就可以使修改后的内容生效，前提使用archaius的API来读取。
			
		Spring Cloud Config
			配置中心，让你可以把配置放到远程服务器，集中化管理集群配置，目前支持本地存储、Git以及Subversion。
			就是以后大家武器、枪火什么的东西都集中放到一起，别随便自己带，方便以后统一管理、升级装备。
		Spring Cloud Bus
			事件、消息总线，用于在集群（例如，配置变化事件）中传播状态变化，可与Spring Cloud Config联合实现热部署。
			相当于水浒传中日行八百里的神行太保戴宗，确保各个小弟之间消息保持畅通。
		Spring Cloud for Cloud Foundry
			Cloud Foundry是VMware推出的业界第一个开源PaaS云平台，它支持多种框架、语言、运行时环境、云平台及应用服务，
			使开发人员能够在几秒钟内进行应用程序的部署和扩展，无需担心任何基础架构的问题
			其实就是与CloudFoundry进行集成的一套解决方案，抱了Cloud Foundry的大腿。
		Spring Cloud Cluster
			Spring Cloud Cluster将取代Spring Integration。
			提供在分布式系统中的集群所需要的基础功能支持，如：选举、集群的状态一致性、全局锁、tokens等常见状态模式的抽象和实现。
			如果把不同的帮派组织成统一的整体，Spring Cloud Cluster已经帮你提供了很多方便组织成统一的工具。
		Spring Cloud Consul
			Consul 是一个支持多数据中心分布式高可用的服务发现和配置共享的服务软件,
			Consul 支持健康检查,并允许 HTTP 和 DNS 协议调用 API 存储键值对.
		Spring Cloud Consul 
			封装了Consul操作，consul是一个服务发现与配置工具，与Docker容器可以无缝集成。
		
		Spring Cloud Stream:
			https://www.cnblogs.com/dengpengbo/p/11103943.html
			Stream实现了消息中间件和服务的高度解耦。
			Stream能够让开发人员无感使用消息中间件，因为Stream对消息中间件的进一步封装，可以做到代码层面对中间件的无感知，甚至于动态的切换中间件(rabbitmq切换为kafka)，使得微服务开发的高度解耦，服务可以关注更多自己的业务流程
	高并发架构的演进思路：
		https://www.jianshu.com/p/9f985bbc9c70