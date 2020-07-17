> 此文档仅为了快速开发，阿里官网原文，已经很简洁清晰了。

## [使用PandoraBoot开发HSF应用](https://help.aliyun.com/document_detail/91226.html?spm=a2c4g.11186623.6.619.2f4f3af2mUOyry)

1. [启动轻量级配置及注册中心](https://help.aliyun.com/document_detail/44163.html?spm=a2c4g.11186623.2.13.1a4042e48dt0za#task-2310117)  
    附：
    ```
        或不管这一步，直接用阿里提供的公网配置及注册中心地址：
            121.43.108.125  jmenv.tbsite.net
            步骤：
                在需要使用轻量级配置及注册中心开发、测试应用的机器上配置轻量级配置及注册中心的 hosts，
                即在 DNS（hosts 文件）中将 jmenv.tbsite.net 域名指向启动了轻量级配置及注册中心的机器 IP。
                1.打开 hosts 文件。
                    Windows 操作系统：
                        C:\Windows\System32\drivers\etc\hosts
                    Unix 操作系统：
                        /etc/hosts
                2.添加轻量级配置及注册中心配置。
                    121.43.108.125  jmenv.tbsite.net
        测试配置与注册中心时候能用：
            直接访问轻量级配置及注册中心   域名 + 端口 jmenv.tbsite.net:8080。
     ```

2. [Maven 中配置 EDAS 的私服地址](https://help.aliyun.com/knowledge_detail/66643.html?spm=a2c4g.11186623.6.620.db793af2sr4Ghr)   
    * Maven配置文件 中新增 EDAS 的私服地址,便于获取hsf框架的相关包.
    ```
        <profiles>
            <profile>
                <id>nexus</id>
                <repositories>
                    <repository>
                        <id>central</id>
                        <url>http://repo1.maven.org/maven2</url>
                        <releases>
                            <enabled>true</enabled>
                        </releases>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                    </repository>
                </repositories>
                <pluginRepositories>
                    <pluginRepository>
                        <id>central</id>
                        <url>http://repo1.maven.org/maven2</url>
                        <releases>
                            <enabled>true</enabled>
                        </releases>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                    </pluginRepository>
                </pluginRepositories>
            </profile>
            <profile>
                <id>edas.oss.repo</id>
                <repositories>
                    <repository>
                        <id>edas-oss-central</id>
                        <name>taobao mirror central</name>
                        <url>http://edas-public.oss-cn-hangzhou.aliyuncs.com/repository</url>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                        <releases>
                            <enabled>true</enabled>
                        </releases>
                    </repository>
                    </repositories>
                <pluginRepositories>
                    <pluginRepository>
                        <id>edas-oss-plugin-central</id>
                        <url>http://edas-public.oss-cn-hangzhou.aliyuncs.com/repository</url>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>
                        <releases>
                            <enabled>true</enabled>
                        </releases>
                    </pluginRepository>
                </pluginRepositories>
            </profile>
        </profiles>
        <activeProfiles>
            <activeProfile>nexus</activeProfile>
            <activeProfile>edas.oss.repo</activeProfile>
        </activeProfiles>
    ```
    * 在命令行执行命令 mvn help:effective-settings，验证配置是否成功。
    无报错，表明 setting.xml 文件格式没问题。
    ```
        附：
        profiles 中包含 edas.oss.repo 这个 profile，表明私服已经配置到 profiles 中。
        activeProfiles 中包含 edas.oss.repo 属性，表明 edas.oss.repo 私服已激活。
        说明 如果在命令行执行 Maven 打包命令无问题，IDE 仍无法下载依赖，请关闭 IDE 重新打开再尝试，或自行查找 IDE 配置 Maven 的相关资料。
    ```
3. [使用Pandora Boot开发HSF应用](https://help.aliyun.com/document_detail/99943.html?spm=a2c4g.11186623.6.621.751a42e4isQbPT)
    ```
        maven依赖：
            看原文
        书写服务提供者：
            看原文
        书写服务调用者：
            看原文
        打包运行：
            mvn clean install -U
            java -Djmenv.tbsite.net=127.0.0.1 -Dpandora.location=D:/programming/versionControl/apache-maven-3.6.1-bin/repository/com/taobao/pandora/taobao-hsf.sar/2019-06-stable/taobao-hsf.sar-2019-06-stable.jar -jar zjrsdemo-0.0.1-SNAPSHOT.jar >> nohup.log 2>&1 &
    ```
    
附:  
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