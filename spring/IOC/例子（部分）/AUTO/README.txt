介绍：
	分别实现了以下两个功能：
		自动加载spring配置文件。
		自动装配属性。
简易实现步骤：
	1.自动加载spring配置文件:
		介绍：
			在web.xml中配置ContextLoaderListener，
			实现web项目启动时自动装配ApplicationContext的配置信息到程序中，
			即直接引用Spring容器管理的bean，无需手动亲自实例话容器。
		实现步骤：
			web.xml配置：
				<listener>
					<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
				</listener>
				<context-param>
					<param-name>contextConfigLocation</param-name>
					<param-value>classpath:applicationContext.xml</param-value>
				</context-param>
		获取bean：
			介绍：在类中即可用以下方式获取指定bean值
			User user=(User)ContextLoaderListener.getCurrentWebApplicationContext().getBean("user")；
	2.自动装配：
		https://blog.csdn.net/heyutao007/article/details/5981555
		介绍:
			使用注释@Autowired ，
			对类成员变量、方法及构造函数进行标注，
			完成自动装配的工作。
			即无需在配置文件进行bean的属性注入，
			和bean本身无需setget即可进行属性的注入。
		实现步骤：
		applicationContext.xml：
			<!-- 该 BeanPostProcessor 将自动对标注 @Autowired 的 Bean 进行注入 -->     
			<bean class="org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"/> 
			<!-- 声明要进行属性注入的bean。注：无需手动配置注入，spring会自动注入 -->     
			<bean id="boss" class="com.baobaotao.Boss"/>    
		boss类：
			//要注入的属性用该注释，spring就会自动往该属性注入值
			@Autowired    
			private Car car;   
注：*Test结尾为测试入口