注：页码指的是《Spring实战》（第4版）
IOC：(Inversion of Control) 控制反转
	https://blog.csdn.net/qq_22654611/article/details/52606960/
	介绍:
		1. 
		IOC不是什么技术，而是一种设计思想。
		IOC即你将类的实例化交给容器控制，而不是由用户来new， 而是从容器获取对象。
		2. Spring中IOC控制的类，称为bean。
		3.spring中通过BeanFactory(容器)来实现IOC(来产生和管理Bean)，这个过程又依赖了DI和工厂方法.
		对类的实例化和对象属性的注入都由容器来进行。
			即spring来负责控制对象的生命周期和对象间的关系。
		附：DI (dependency injection 依赖注入)
			介绍：
				1. IoC的一个重点是在系统运行中，需要动态的向某个对象提供它所需要的其他对象。
				这一点是通过DI（Dependency Injection，依赖注入）来实现的。
				
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
		附：
		Spring Core：
			Spring Core 核心模块，提供Spring框架的基本功能。
				其他的Spring模块都要基于核心容器模块。
			其中主要提供IOC功能，何其他企业服务 如E-mail、JNDI访问、EJB集成和调度等。
		什么是Bean：
			1.由Spring IoC容器管理的类就会被认为是“Bean”。
			2. ioc管理的类默认要符合javaBean格式，但也可以不符合。
				附：
					JavaBean：
						类的属性使用getter和setter来访问等。
					POJO：
						一个不受任何限制的Java对象，除了Java语言规范。

IOC容器：(spring的bean管理器)
	https://www.cnblogs.com/xiaoxi/p/5846416.html
	介绍：
		Spring使用容器来管理所有的Bean对象。
			即程序用容器来加载bean，并为bean注入相关的bean。
		IOC容器可划分为两种类型，BeanFactory和ApplicationContext两个系列。
			1.实现BeanFactory接口的简单容器，具备最基本功能。
			2.实现ApplicationContext接口的复杂容器，具备高级功能。
		BeanFactory接口：
			介绍:
				BeanFactory接口仅实现了IOC功能。
				通常只提供注册（put），获取（get）这两个功能（低级容器）。
			实现类:
				XmlBeanFactory:
				所需参数:
					new XmlPathResource("applicationContext.xml")
		ApplicationContext接口:
			介绍:
				该接口继承了BeanFactory接口，实现了更多功能。
					？例如...功能
				可称为企业级IOC容器（高级容器）。
			实现类:
				各种应用上下文的实现，它们之间主要的区别仅在于如何加载bean配置。
				FileSystemXmlApplicationContext:
					介绍：通过参数指定配置文件位置。即可获得类路径之外的资源。
					例：
						ApplicationContext context = new FileSystemXmlApplicationContext("C:/knight.xml") ;
				ClassthPathXmlApplicationContext:
					介绍：
						使用XML文件配置的bean，可选择ClassPathXmlApplicationContext来加载。
						该类加载位于应用程序类路径下的XML配置文件。
					例：
						ApplicationContextcontext = new ClassPathXm1App1icationContext("knight.xm1") ;
					附：
						FileSystemXmlApplicationContext和ClassPathXmlApp-licationContext的主要区别在于：
						FileSystemXmlApplicationContext在指定的文件系统路 径下查找knight.xml文件；
						而ClassPathXmlApplicationContext 则是在应用类路径下的resource中查找 knight.xml文件。

				WebApplicationContext:
					介绍：spring的Web应用容器
				...
        附：
                BeanFactory和ApplicationContext的区别：
                https://blog.csdn.net/pythias_/article/details/82752881
                装载bean的时机不同：
                    BeanFactroy采用的是延迟加载形式来注入Bean的，即只有在使用到某个Bean时(调用getBean())，才对该Bean进行加载实例化。
                        这样，我们就不能发现一些存在的Spring的配置问题。如果Bean的某一个属性没有注入，BeanFacotry加载后，直至第一次使用调用getBean方法才会抛出异常。
                    ApplicationContext，它是在容器启动时，一次性创建了所有的SingletonBean。（默认初始化所有的Singleton，也可以通过配置取消预初始化。）
                        这样，在容器启动时，我们就可以发现Spring中存在的配置错误，这样有利于检查所依赖属性是否注入。
                        附：
                            因为ApplicationContext启动后预载入所有的单实例Bean，通过预载入单实例bean,确保当你需要的时候，你就不用等待，因为它们已经创建好了。
                        附：
                            bean属性的注入可以分为2种，属性注入和构造方法注入。
                            如果bean的属性是用set注入(注解注入)的，那么该属性其实并没有被注入，真实注入时机是在需要该bean的时候。
                            如果bean的属性是用构造方法注入，那么其就是bean初始化时就被注入的。此处还涉及到了bean初始化顺序问题，即循环依赖问题。
                            所以这个bean初始化了 但属性并不一定被注入。
                ？
                    （3）BeanFactory通常以编程的方式被创建，ApplicationContext还能以声明的方式创建，如使用ContextLoader。

                    （4）BeanFactory和ApplicationContext都支持BeanPostProcessor、BeanFactoryPostProcessor的使用，但两者之间的区别是：BeanFactory需要手动注册，而ApplicationContext则是自动注册。?

	容器使用：
		1.创建容器：
			方式1：web应用无需手动创建，而是配置要使用的容器：
				1.在servlet的web.xml配置spring的ContextLoaderListener监听器
				或2.修改web.xml在其中添加一个servlet，定义使用spring的ContextLoaderServlet类。
			方式2：手动创建创建容器
		2.bean的装配：
			./bean的装配/

		3.通过容器手动获取bean:
			例:获取user对象
				(User)factory.getBean("user");
			详：
				./spring高级装配/手动获取bean.txt
	
	
	springBean的生命周期：
	./spring高级装配/springBean的生命周期/