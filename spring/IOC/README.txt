注：页码指的是《Spring实战》（第4版）
Spring Core(核心容器模块):
	介绍:
		核心容器提供Spring框架的基本功能。
		1.该模块是spring的核心模块，主要实现了IOC功能（控制反转），	提供Spring框架的基本功能。其他的Spring模块都要基于核心容器模块。
			2.IOC能让Spring以bean的方式组织和管理Java应用中的各个组件及其关系。Spring使用BeanFactory（容器）来产生和管理Bean。
		3. 除了容器实现类，该模块也提供了许多企业服务，例如E-mail、JNDI访问、EJB集成和调度。
		
	什么是Bean：
		1.由Spring IoC容器管理的类就会被认为是“Bean”。
		2.虽然Spring用bean（或者JavaBean）来表示应用组件，但并不意味着Spring组件必须要遵循JavaBean规范，一个Spring组件可以是任何形式的POJO。
			附：
				JavaBean：
					类的属性使用getter和setter来访问等。
				POJO：
					一个不受任何限制的Java对象，除了Java语言规范。
	spring的bean管理器：
		Spring通过容器，全权负责对象的创建和组装。
		Spring自带了多个容器实现，可划分为两种类型，bean工厂和应用上下文（推荐）。
		Spring自带了多种应用上下文的实现，它们之间主要的区别仅仅在于如何加载bean配置。
		例：
			使用XML文件配置的bean，选择ClassPathXmlApplicationContext作为应用上下文相对是比较合适的。
			该类加载位于应用程序类路径下的一个或多个XML配置文件。
			main()方法调用ClassPathXmlApplicationContext加载knights.xml，再获得bean对象的引用。
	
	springBean的生命周期：
		./springBean的生命周期/
IOC:(Inversion of Control) 控制反转
	https://blog.csdn.net/qq_22654611/article/details/52606960/
	IOC不是什么技术，而是一种设计思想。
	IOC意味着将你设计好的对象交给容器控制，而不是传统的程序员在对象内部直接控制。
	即
		对类的实例化和对象属性的注入由控制器来进行。（spring来负责控制对象的生命周期和对象间的关系）
	hcg：
		即spring中通过容器来实现IOC，这个过程又依赖了DI和工厂方法.
	附：
	DI(dependency injection 依赖注入):
		介绍：
			IoC的一个重点是在系统运行中，需要动态的向某个对象提供它所需要的其他对象。
			这一点是通过DI（Dependency Injection，依赖注入）来实现的。
			附：
				DI能够让相互协作的软件组件保持松散耦合
		依赖注入三种实现方式：
			1.接口注入
			2.Setter注入
				介绍:
				该类型基于JavaBean的
				setter方法为属性赋值。
			3.构造器注入
				介绍:
				该类型基于构造方法为属性
				赋值。
				注:容器通过调用类的构造方法将
			所需的依赖关系注入其中。
			注：spring支持后两种。		
	IOC容器：
		https://www.cnblogs.com/xiaoxi/p/5846416.html
		介绍：
			Spring使用容器来管理所有的Bean对象。
				即程序用容器来加载bean，并为bean注入相关的bean。
			在Srping Ioc容器中，有BeanFactory和ApplicationContext两个系列。
				1.实现BeanFactory接口的简单容器，具备最基本功能。
				2.实现ApplicationContext接口的复杂容器，具备高级功能。
		BeanFactory接口：
			介绍:
				BeanFactory接口仅实现了IOC功能。
				通常只提供注册（put），获取（get）这两个功能，可称为IOC容器（低级容器）。
			实现类:
				XmlBeanFactory:
				所需参数:
					new XmlPathResource("applicationContext.xml")
		ApplicationContext接口:
			介绍:
				该接口继承了BeanFactory接口，实现了更多功能。
				可称为企业级IOC容器（高级容器）。
			实现类:
				FileSystemXmlApplicationContext:
						介绍：通过参数指定配置文件位置。即可获得类路径之外的资源。
						例：
							ApplicationContext context = new FileSystemXmlApplicationContext("C:/knight.xml") ;
				ClassthPathXmlApplicationContext:
					介绍：从当前类路径中检索配置文件并加载配置文件来创建容器
					例：
						ApplicationContextcontext = new ClassPathXm1App1icationContext ( "knight.xm1") ;
					附：
						使用FileSystemXmlApplicationContext和使 用ClassPathXmlApp-licationContext的区别在 于：FileSystemXmlApplicationContext在指定的文件系统路 径下查找knight.xml文件；
						而ClassPathXmlApplicationContext 是在所有的类路径（包含JAR文件）下查找 knight.xml文件。
				

				WebApplicationContext:
					介绍：spring的Web应用容器
					...
		附：
			BeanFactory和ApplicationContext的区别：https://blog.csdn.net/pythias_/article/details/82752881
			装载bean的时机不同：	
				BeanFactroy采用的是延迟加载形式来注入Bean的，即只有在使用到某个Bean时(调用getBean())，才对该Bean进行加载实例化。
					这样，我们就不能发现一些存在的Spring的配置问题。如果Bean的某一个属性没有注入，BeanFacotry加载后，直至第一次使用调用getBean方法才会抛出异常。
				ApplicationContext，它是在容器启动时，一次性创建了所有的SingletonBean。（默认初始化所有的Singleton，也可以通过配置取消预初始化。）
					这样，在容器启动时，我们就可以发现Spring中存在的配置错误，这样有利于检查所依赖属性是否注入。 ApplicationContext启动后预载入所有的单实例Bean，通过预载入单实例bean,确保当你需要的时候，你就不用等待，因为它们已经创建好了。
					附：
						这个与set注入并不冲突，如果bean的属性是用set注入的，那么该属性其实并没有被注入，真实注入时机是在需要该bean的时候。
			...
				（3）BeanFactory通常以编程的方式被创建，ApplicationContext还能以声明的方式创建，如使用ContextLoader。

				（4）BeanFactory和ApplicationContext都支持BeanPostProcessor、BeanFactoryPostProcessor的使用，但两者之间的区别是：BeanFactory需要手动注册，而ApplicationContext则是自动注册。?
		容器使用：
			1.创建容器：
				方式1：web应用无需手动创建，而是配置要使用的容器：
					1.在servlet的web.xml配置spring的ContextLoaderListener监听器
					或2.修改web.xml在其中添加一个servlet，定义使用spring的ContextLoaderServlet类。
				方式2：手动创建创建容器
			2.bean的装配：
				goto：bean的三种主要装配机制
			3.通过容器手动获取bean:
				例:获取user对象(User)factory.getBean("user");
				注:获取bean前还得进行bean配置。
bean的三种主要装配机制：
	介绍：
		定义那些类要注册为spring容器的bean。
	方式1：在XML中进行显式配置。p76
	方式2：Spring自动发现和装配bean。（推荐）
		./bean自动加载/springBean的自动加载.txt
	方式3：在Java中进行显式配置。
		./JavaConfig显式配置.txt
	附：
		混合配置：
			介绍：
				Spring的bean配置风格是可以互相搭配的。
				所以你可以选择使用XML装配一些bean，
				使用Spring基于Java的配置（JavaConfig）来装配另一些bean，
				而将剩余的bean让Spring去自动发现。
		通过容器手动获取bean：
			./spring高级装配/手动获取bean.txt
