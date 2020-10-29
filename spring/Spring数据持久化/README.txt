https://spring.io/projects/spring-data

Spring操作数据库的三种方式：
	
	1. 通过spring的数据访问模板来操作数据库
		例：
			通过JdbcTemplate、HibernateTemplate等来访问数据库
			./Spring数据访问模板化.txt
		附：
			springBoot集成JdbcTemplate：https://www.jianshu.com/p/414ef5b49a69
	2. 与JPA框架集成，操作数据库。
		spring第11章
		附：
			springBoot集成jpa：https://www.jianshu.com/p/414ef5b49a69
	3. 与MyBatis集成，操作数据库。
		...
		附：
			../springBoot/附属功能/springBoot集成Mybaits
注：
	原生java操作数据库:
		./jdbc和jpa.txt
	所有的底层都是通过jdbc实现的吧：
		https://www.cnblogs.com/aeolian/p/10735122.html
		是的
附：
	多个数据源：
		https://blog.csdn.net/qq_41076797/article/details/82889770




















废弃：
	Transaction与SqlSession,Connection的关系
		https://blog.csdn.net/wuqinduo/article/details/103750005
	jdbc操作数据库：
		./例/jdbc/
		或原生java进行jdbc：https://blog.csdn.net/wuqinduo/article/details/103750005
		java JDBC编程流程步骤：
			https://www.cnblogs.com/lightandtruth/p/9473862.html
	spring持久化:
		DAO模式:
			DAO(DataAccessObject数据访问对象)
			dao模式操作表:
				1.实体类，这里暂定为User，即该类为有关于客户的属性和行为。
				2.UserDAO接口，即该接口中规定了insert，update等方法，等待具体实现。
				3.UserDaoImpl类，该类要求实现UserDAO接口。
					注:该类要求一个DataSource属性，和其对应的getset。
					操作数据库具体步骤:
						1.通过DataSource获取连接对象
						2.调用连接对象的createStatement()获取声明对象
						3.通过声明对象的.excute("sql语句")
				4.通过工厂获取User和UserDaoImpl对象，调用UserDaoImpl的方法即可操作数据库。
					定义DataSource:
					1.在applicationContext.xml中定义一个JavaBean名为DataSource的数据源，它是DriverManagerDataSource类(class的值)的实例。
					&2.为DataSource注入数据库地址，数据库账户，数据库密码。
					注:
						要把DataSource注入到UserDaoImpl中。
			spring事务管理:
				事务是一系列任务组成的工作单元，事务中所有任务必须同时执行，且执行结果只有全部成功和全部失败。
				spring的事务是基于aop实现，而springaop以方法为单位。
				编程式事务管理:
					TransactionTemplate实现:
					1.在spring配置中用bean定义
					1.事务管理器和2.TransactionTemplate模板
					2.创建一个类
					介绍:
					该类中用transactionTemplate.execute()来启用事务并对数据库操作。
					属性:
					DataSource,TransactionManager,TransactionTemplate
					方法:
					创建一个方法里头进行事务的操作:
					事务模板.execute()
					…实战。
					3.用工厂调用2的类，执行里头方法即可实验事务。
					PlatformTransactionManager接口实现:?
				声明式事务管理:
					介绍:
						不需编写任何代码事务管理，通过aop实现事务管理。
					TransactionProxyFactory实现:
						1.spring配置文件中定义TransactionProxy。
						该bean需要注入以下属性:
							1.事务管理器，
							2.代理的目标对象。
							注:代理的目标对象中要注入DataSource。
							3.代理目标对象生成方式
							4.事务属性，即指定对那些方法开启事务。
						2.编写目标对象类，即该类操作数据库。
						3.创建工厂类，获取目标对象调用其操作数据库方法即可实现事务管理。
		JdbcTemplate操作数据库:
		与hibernate整合操作数据库:
			1.spring配置文件中定义个LocalSessionFactoryBean类的bean。
			该bean要注入属性：
				1.DataSource
					注：
					datasource中要注入数据库驱动，数据库url，数据库账户，数据库密码
				2.hibernate属性
					即设置hibernate的一些配置如数据库方法，是否输出sql，是否格式化sql
				3.hibernate的映射文件
					即hibernate的持久化类的配置文件如User.hbm.xml
						注：
							要是注释类型的可以不配吗？
				配置好后即可使用spring提供的支持hibernate的类来对数据库操作，
				例SessionFactoryBean（估计是L..的子类）。
			2.把配置好的1bean注入到一个bean的SessionFactory属性中，
				例注入到UserDaoImpl的SessionFactory中。
				即可调用SessionFactory.openSession().save()；操作数据库
			3.通过工厂获取bean，调用bean中操作数据库的方法
				即可实现对数据库的修改。
				注:
				整个生命周期保存一个SessionFactory即可。