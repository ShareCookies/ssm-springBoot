https://www.jianshu.com/p/5687e2a38fbc
介绍：
	spring支持编程式事务管理和声明式事务管理两种方式。
		1.编程式事务指的是通过编码方式实现事务。
			即使用TransactionTemplate来手动在代码里实现事务。
			例:
				./例子/编程式事务之事务模板实现事务管理/
		2. 声明式事指的是可通过注解等非侵入式即可实现事务。
			详：
				goto：声明式事务管理
			例：
				./例子/声明式事务之代理工厂实现事务管理/
		附：声明式事务优缺点：
			声明式事务管理要优于编程式事务管理，这正是spring倡导的非侵入式的开发方式，使业务代码不受污染，只要加上注解就可以获得完全的事务支持。
			唯一不足地方是，最细粒度只能作用到方法级别，无法做到像编程式事务那样可以作用到代码块级别。

声明式事务管理：
	介绍：
		声明式事务有两种方式，一种是在配置文件（xml）中做相关的事务规则声明，另一种是基于@Transactional 注解的方式。
	
	声明式事务之配置文件：
		...
	声明式事务之注解实现事务：
		注解实现事务：
			1. 配置文件上开启注解驱动。
			例：
			    @EnableTransactionManagement
			2. 在相关的类和方法上使用注解@Transactional，
			    即可为对应方法添加事务功能。
			    @Transactional添加的事务具有以下特性：
					1. 自动提交事务
					2. 遇到异常自动回滚
				
		@Transactional的使用：
			加在方法上:
				表示对该方法应用事务。
			加在类上:
				表示对该类里面所有的方法都应用相同配置的事务。
			参数解析：
				transactionManager()：事务管理器？
					https://blog.csdn.net/m0_37556444/article/details/83146804
					介绍：
						Spring为事务提供了统一的抽象,它并不做任何事务的具体实现。
						他只是提供了个事务管理的接口PlatformTransactionManager，具体内容由就由各个事务管理器来实现。
					Spring提供了许多内置事务管理器实现：
						JDBC DataSourceTransactionManager
							介绍：JDBC中是通过Connection对象进行事务管理，通过commit方法进行提交，rollback方法进行回滚，如果不提交，则数据不会真正的插入到数据库中
						JPA JapTransactionManager
						Hibernate HibernateTransactionManager
							介绍：Hibernate中是通过Transaction进行事务管理，处理方法与JDBC中类似。
						JDO JdoTransactionManager
						分布式事务 JtaTransactionManager
						...
					
					附：		
						默认空？
						事务：
							事务是对一系列的数据库操作（比如插入多条数据）进行统一的提交或回滚操作，如果插入成功，那么一起成功，如果中间有一条出现异常，那么回滚之前的所有操作，这样可以防止出现脏数据，防止数据库数据出现问题。
				isolation()：
					goto：spring事务的隔离级别
				*propagation()：
					goto：spring事务的传播行为
				readOnly()：
				timeout ：
					https://blog.csdn.net/qq_18860653/article/details/79907984
				readOnly() 只读事务:
					从这一点设置的时间点开始（时间点a）到这个事务结束的过程中，其他事务所提交的数据，该事务将看不见！（查询中不会出现别人在时间点a之后提交的数据）。
					注意是一次执行多次查询来统计某些信息，这时为了保证数据整体的一致性，要用只读事务
				*rollbackFor():
					例：
						@Transactional(rollbackFor=Exception.class)
						https://www.cnblogs.com/clwydjgs/p/9317849.html
						加了(rollbackFor=Exception.class)，那么方法抛出异常时(运行时异常、非运行时异常)才会回滚。
						不加，只会在遇到RuntimeException的时候才会回滚。
						附：
							1.异常分为Error和Exception。
							Exception分为RuntimeException（运行时异常）和非运行时异常。
							
				rollbackForClassName():
				noRollbackFor():
				noRollbackForClassName:
				
					
		注：
			1. 默认情况下，如果在事务中抛出了未检查异常（继承自 RuntimeException 的异常）或者 Error，则 Spring 将回滚事务；
			除此之外，Spring 不会回滚事务。
			你如果想要在特定的异常回滚可以考虑rollbackFor()等属性
			2. @Transactional 只能应用到 public 方法才有效。
				因为就是这么实现，若不是 public，就不会获取@Transactional 的属性配置信息，最终会造成不会用 TransactionInterceptor 来拦截该目标方法进行事务管理。
				解析：
					这是因为在使用 Spring AOP 代理时，Spring 在调用 TransactionInterceptor在目标方法执行前后进行拦截之前。
					会通过DynamicAdvisedInterceptor（CglibAopProxy的内部类）的 intercept方法
					或 JdkDynamicAopProxy 的 invoke 方法
					先间接调用 AbstractFallbackTransactionAttributeSource（Spring 通过这个类获取@Transactional 注解的事务属性配置属性信息）的 computeTransactionAttribute 方法，进行判断事务方法是否为public。
						protected TransactionAttribute computeTransactionAttribute(Method method, @Nullable Class<?> targetClass) {
						   //这里判断是否是public方法
							if(this.allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
								return null;
							}
							...
						}
					若不是 public，就不会获取@Transactional 的属性配置信息，最终会造成不会用 TransactionInterceptor 来拦截该目标方法进行事务管理。
			3. Spring 的 AOP 的自调用问题：
				Spring的AOP代理，只有目标方法由外部调用，目标方法才由 Spring生成的代理对象来管理，这会造成自调用问题。
				即：内部调用(例：this.)有@Transactional注解的方法，有@Transactional注解的方法的事务被忽略，不会发生回滚。
				这个问题就是Spring AOP 代理造成的。
				解决方式：
					使用自注入来解决：
					@Autowired 
					UserService userService; //自注入来解决 
					//因为该方式获取的bean还是从spring工厂获取到的，即实际获取到的是由Spring生成的代理对象
				？
					另外一个问题是只能应用在public方法上。
					1.为解决这两个问题，使用 AspectJ 取代 Spring AOP 代理。
spring事务的传播行为：
	https://blog.csdn.net/qq_30336433/article/details/83111675
	spring事务的传播行为说的是，当多个事务同时存在的时候，spring如何处理这些事务的行为。
		场景：
			a方法嵌套b方法，a方法开启了事务，b方法开启了事务。
			那么a方法是最先开启一个事务，然后b方法默认情况会使用a方法的事务。
	事务的传播机制：
		控制调用另一个配置了事务的方法时如何参与当前事务。
		1.PROPAGATION_REQUIRED（默认）：
			如果当前没有事务，就创建一个新事务，如果当前存在事务，就加入该事务，该设置是最常用的设置。
		2.PROPAGATION_SUPPORTS：
			如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行。
		3.PROPAGATION_MANDATORY：
			如果当前存在事务，就加入该事务，如果当前不存在事务，就抛出异常。
		4.PROPAGATION_REQUIRES_NEW：
			创建新事务，无论当前存不存在事务，都创建新事务。
			存在了？
		5.PROPAGATION_NOT_SUPPORTED：
			以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
			！只有b方法才是非事务吧
		6.PROPAGATION_NEVER：
			以非事务方式执行，如果当前存在事务，则抛出异常。
		7.PROPAGATION_NESTED：
			如果当前存在事务，则在嵌套事务内执行。
			如果当前没有事务，则按REQUIRED属性执行。
	？
		传播具体是怎么实现的了
	附：
		https://segmentfault.com/q/1010000004263937?_ea=549435
spring事务的隔离级别：
	TransactionDefinition接口中定义了五个表示隔离级别的常量。
	ISOLATION_DEFAULT：（默认的隔离级别）
		使用数据库默认的事务隔离级别，MySQL默认采用的REPEATABLE_READ隔离级别，Oracle默认采用的READ_COMMITTED隔离级别。
	ISOLATION_READ_UNCOMMITTED：
		读未提交。允许读取尚未提交的的数据变更。
		可能会导致脏读、幻读或不可重复读。（就是所有，脏、幻、不可、丢失）
	ISOLATION_READ_COMMITTED：
		读已提交。允许读取事务已经提交的数据。
		可以阻止脏读，但是幻读或不可重复读仍有可能发生。
	ISOLATION_REPEATABLE_READ：
		可重复读。对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改。
		可以阻止脏读和不可重复读，但幻读仍有可能发生。
	ISOLATION_SERIALIZABLE：
		序列化，最高的隔离级别，完全服从ACID的隔离级别。
		所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰。
		也就说，该级别可以阻止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。
事务原理：
	spring为什么能实现事务:
		spring能实现事务管理，是因为数据库对事务提供了支持。
		没有数据库的事务支持，spring是无法提供事务功能的。
		即spring实现事务是调用数据库对应接口的。
		附：
		    而数据库层的事务实现 ，提交和回滚是通过binlog或者redo log实现的。
    编程式事务原理：
        编程式事务原理很简单 就是调用对应的数据库事务接口就好了。
	声明式事务原理：
		声明式事务是建立在AOP之上的。
		其本质是通过aop对方法前后进行(环绕)拦截，然后在目标方法开始之前创建或者加入一个事务，在执行完目标方法之后根据执行情况提交或者回滚事务。
		附：
			@Transactional原理：
				spring 在启动的时候会去解析生成相关的bean，这时候会查看拥有相关注解的类和方法，并且为这些类和方法生成代理，
				并根据@Transaction的相关参数进行相关配置注入，这样就在代理中为我们把相关的事务处理掉了（开启正常提交事务，异常回滚事务）。
		源码：