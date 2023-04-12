---
title: hsf微服务改造教程
date: 2020-07-14
author: 何财贵
categories:
 - hsf
tags:
 - 浙江高院
---



# [使用PandoraBoot开发HSF应用](https://help.aliyun.com/document_detail/91226.html?spm=a2c4g.11186623.6.619.2f4f3af2mUOyry)

> 此文档目的：此文档仅为了快速使用PandoraBoot开发HSF应用，阿里官网原文，已经很简洁清晰了。
>
> 如果不进行该文档的准备工作 那么无法启动oa总后端。

## 准备工作：

### [启动轻量级配置及注册中心](https://help.aliyun.com/document_detail/44163.html?spm=a2c4g.11186623.2.13.1a4042e48dt0za#task-2310117)  

> 1. 下载注册中心：
>
> https://edas.oss-cn-hangzhou.aliyuncs.com/edas-res/edas-lightweight-server-1.0.0.zip?spm=a2c4g.44163.0.0.6f0a30a8cnvozF&file=edas-lightweight-server-1.0.0.zip
>
> 
>
> 2. 启动命令：/work2/zjsgy/hsf/edas-lightweight/bin/startup.sh
>
>    附：服务器地址192.168.210.171
>
> 介绍：
>
> ​	配置及注册中心最基本的作用：1.接受微服务接口的注册 2.提供对微服务接口的调用支持
>
> 注:
>
> ​	1.此处无需我们安装并启动启动轻量级配置及注册中心，仅需我们编辑文件host文件使jmenv.tbsite.net 域名指向注册中心机器ip：
>
> ​	Windows 系统host文件位置：C:\Windows\System32\drivers\etc\hosts
>
> ​	公司内网提供的地址： 192.168.210.171  jmenv.tbsite.net
>
> ​	2. 测试是否配置成功：访问 jmenv.tbsite.net:8080
>
> ​	注：如果确认host配置了，那么可能是host没生效，百度host修改了无法生效。


### [Maven 中配置 EDAS 的私服地址](https://help.aliyun.com/knowledge_detail/66643.html?spm=a2c4g.11186623.6.620.db793af2sr4Ghr)   

> 这一步为了：Maven配置文件 中新增 EDAS 的私服地址,便于获取hsf框架的相关包.
>
> 注：这一步最好要进行不然可能会缺失部分依赖。
>
> maven的settings.xml调整：
>
> 且最好屏蔽掉mirrors，因为有mirror 会优先对应的mirror 那么新增的阿里远程仓库可能就不生效。
>
> 附：./参考settings.xml
>
> 	
> 

## [使用Pandora Boot开发HSF应用](https://help.aliyun.com/document_detail/99943.html?spm=a2c4g.11186623.6.621.751a42e4isQbPT)

###  [1.Pandora 需要的maven依赖：](https://help.aliyun.com/document_detail/99943.html?spm=a2c4g.99943.0.0.5aa67062U0Unb4)
	建议直接看阿里提供的demo工程。
	附： 消费者和提供者的Maven依赖相同 

```
<properties>
      <java.version>1.8</java.version>
      <spring-boot.version>2.1.6.RELEASE</spring-boot.version>
      <pandora-boot.version>2019-06-stable</pandora-boot.version>
  </properties>

  <dependencies>
      <dependency>
          <groupId>com.alibaba.boot</groupId>
          <artifactId>pandora-hsf-spring-boot-starter</artifactId>
      </dependency>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
  </dependencies>

  <dependencyManagement>
      <dependencies>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-dependencies</artifactId>
              <version>${spring-boot.version}</version>
              <type>pom</type>
              <scope>import</scope>
          </dependency>
          <dependency>
              <groupId>com.taobao.pandora</groupId>
              <artifactId>pandora-boot-starter-bom</artifactId>
              <version>${pandora-boot.version}</version>
              <type>pom</type>
              <scope>import</scope>
          </dependency>
      </dependencies>
  </dependencyManagement>

  <build>
      <plugins>
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-compiler-plugin</artifactId>
              <version>3.7.0</version>
              <configuration>
                  <source>1.8</source>
                  <target>1.8</target>
              </configuration>
          </plugin>
          <plugin>
              <groupId>com.taobao.pandora</groupId>
              <artifactId>pandora-boot-maven-plugin</artifactId>
              <version>2.1.11.8</version>
              <executions>
                  <execution>
                      <phase>package</phase>
                      <goals>
                          <goal>repackage</goal>
                      </goals>
                  </execution>
              </executions>
          </plugin>
      </plugins>
  </build>
```



### 2. 代码开发：

	前言：
		HSF服务框架基于接口进行服务通信。
		当接口定义好之后，生产者将通过该接口实现具体的服务并发布。
		消费者也是基于此接口去订阅和消费服务。
	
	1. 定义服务接口：
		public interface HelloService {
			String echo(String string);
		}
	
	2. 书写服务提供者：
		介绍：把服务注册到注册中心，便于他人调用。
		//添加服务提供者的具体实现类，并通过注解方式发布服务。
		@HSFProvider(serviceInterface = HelloService.class, serviceVersion = "1.0.0")
		public class HelloServiceImpl implements HelloService {
		  @Override
		  public String echo(String string) {
			  return string;
		  }
		}
		附：在HSF应用中，接口名和服务版本才能唯一确定一个服务。
```

```
	3. 书写服务调用者：
		将服务提供者所发布的API服务接口（包括包名）拷贝到本地，如com.alibaba.edas.HelloService。
		public interface HelloService {
	    	String echo(String string);
		}
		通过注解的方式将服务消费者的实例注入到Spring的Context中。
		@Configuration
		public class HsfConfig {
			@HSFConsumer(clientTimeout = 3000, serviceVersion = "1.0.0")
			private HelloService helloService;
		}
		
	4. 测试 
	   //为了便于测试，使用SimpleController来暴露一个/hsf-echo/*的HTTP接口，/hsf-echo/*接口内部实现调用了HSF服务提供者。
	   @RestController
	     public class SimpleController {
	   
	         @Autowired
	         private HelloService helloService;
	   
	         @RequestMapping(value = "/hsf-echo/{str}", method = RequestMethod.GET)
	         public String echo(@PathVariable String str) {
	             return helloService.echo(str);
	         }
	     }


​	


## 附:  一些额外知识介绍
* RPC:
    ```
	https://www.jianshu.com/p/2accc2840a1b
	https://www.jianshu.com/p/7d6853140e13
	Remote Procedure Call远程过程调用，简单的理解是一个节点请求另一个节点提供的服务
	如果addAge()这个方法在服务端，执行函数的函数体在远程机器上，如何告诉机器需要调用这个方法呢。
		1.
			首先客户端需要告诉服务器，需要调用的函数，这里函数和进程ID存在一个映射，客户端远程调用时，需要查一下函数，找到对应的ID，然后执行函数的代码。
		2.
			客户端需要把本地参数传给远程函数，本地调用的过程中，直接压栈即可，但是在远程调用过程中不再同一个内存里，无法直接传递函数的参数，因此需要客户端把参数转换成字节流，传给服务端，然后服务端将字节流转换成自身能读取的格式，是一个序列化和反序列化的过程。
		3.
			数据准备好了之后，如何进行传输？网络传输层需要把调用的ID和序列化后的参数传给服务端，然后把计算好的结果序列化传给客户端，因此TCP层即可完成上述过程，gRPC中采用的是HTTP2协议。
	附：
		本地过程调用：
			如果需要将本地student对象的age+1，可以实现一个addAge()方法，将student对象传入，对年龄进行更新之后返回即可，本地方法调用的函数体通过函数指针来指定。
	```
	
* [EDAS介绍]( https://help.aliyun.com/document_detail/42934.html?spm=a2c4g.11174283.4.1.6b094632Fmr4GJ)：  
    ```
    Enterprise Distributed Application Service企业级分布式应用服务，
    是一个应用托管和微服务管理的 PaaS 平台，提供应用开发、部署、监控、运维等全栈式解决方案。
    EDAS 支持 Spring Cloud、Dubbo 和 HSF 三种微服务应用框架，您可以使用 Spring Cloud、Dubbo 和 HSF 应用，然后托管到 EDAS 中。
    ```
    
* [HSF介绍](https://help.aliyun.com/document_detail/100087.html?spm=a2c4g.11186623.6.606.17f13af24Unnsf)：  
    ```
    High-speed Service Framework高速服务框架，是在阿里巴巴广泛使用的分布式RPC服务框架。
    应用开发方式:
        使用HSF框架开发应用有Ali-Tomcat和Pandora Boot两种方式。  
        Ali-Tomcat： 依赖Ali-Tomcat和Pandora，提供了完整的HSF功能，包括服务注册与发现、隐式传参、异步调用、泛化调用和调用链路Filter扩展。应用程序须以WAR包方式部署。  
        Pandora Boot：依赖Pandora，提供了比较完整的HSF功能，包括服务注册与发现、异步调用。应用程序编译为可运行的JAR包并部署即可。
    ```

    
# 遇过的一些报错:  

* 本地jar包启动，微服务接口发布报空指针异常：

  ```
  1. 原因：
  	获取服务机器地址失败
  2. 解决方案：
  	启动命令加上 -Dhsf.server.ip=本机ip -Dhsf.server.port=12200
  3. 分析过程：
  	开发hsf应用时，碰到框架方面等问题要从各个类型的日志中进行观察。
	在hsf.log日志中发现：
  		01 2020-07-25 01:10:46.888 WARN [metrics-bin-reporter-1-thread-1:t.hsf] 、[unknow_project_name] [] [] Can not get the server IP address ERR-CODE: [HSF-0051], Type: [BIZ], More: [http://console.taobao.net/help/HSF-0051]
  	经过资料查询和测试：
  		https://helpcdn.aliyun.com/knowledge_detail/44546.html
  		服务发布空指针异常与获取服务机器地址失败有关。
  	注：
  		[使用日志排查问题](https://helpcdn.aliyun.com/document_detail/44193.html?spm=a2c4g.11186623.6.1045.c21517e3slXaAC)
  ```
  
* #  Could not initialize class com.taobao.diamond.client.impl.DiamondEnvRepo：

* ```
配置中心未启动
  ```

# 如何发布快照包：

  ```
	mvn deploy:deploy-file -DgroupId=com.zjpth -DartifactId=stzx-common-api -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dfile=C:\Users\Administrator\Desktop\stzx-common-api-1.0-SNAPSHOT.jar -Durl=http://admin:admin123@192.168.0.40:8081/repository/snapshots/ -DrepositoryId=snapshots
  ```

