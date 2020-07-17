https://www.jianshu.com/p/5687e2a38fbc
*：3
介绍：
	spring支持编程式事务管理和声明式事务管理两种方式。
	编程式事务指的是通过编码方式实现事务；
	声明式事务基于 AOP,将具体业务逻辑与事务处理解耦看，使业务代码逻辑不受污染, 因此在实际使用中声明式事务用的比较多。
	
声明式事务管理：
	介绍：
		声明式事务有两种方式，一种是在配置文件（xml）中做相关的事务规则声明，另一种是基于@Transactional 注解的方式。
	解析：
		声明式事务管理建立在AOP之上的。
		其本质是对方法前后进行拦截，然后在目标方法开始之前创建或者加入一个事务，在执行完目标方法之后根据执行情况提交或者回滚事务。
	
	声明式事务之配置文件：
		...
	声明式事务之@Transactional注解：
		*介绍：
			使用@Transactional的相比传统的我们需要手动开启事务，然后提交事务来说。
			它提供如下方便：
				根据你的配置，设置是否自动开启事务
				自动提交事务或者遇到异常自动回滚
				
			(附录)@Transactional基本原理如下：
				配置文件开启注解驱动，在相关的类和方法上通过注解@Transactional标识。
				spring 在启动的时候会去解析生成相关的bean，这时候会查看拥有相关注解的类和方法，并且为这些类和方法生成代理，
				并根据@Transaction的相关参数进行相关配置注入，这样就在代理中为我们把相关的事务处理掉了（开启正常提交事务，异常回滚事务）。
				真正的数据库层的事务提交和回滚是通过binlog或者redo log实现的。
		*@Transactional：
			使用@Transactional，即可启用事务。
			加在方法上:
				表示对该方法应用事务。
			加在类上:
				表示对该类里面所有的方法都应用相同配置的事务。
			参数解析：
				transactionManager()事务管理器：
					...
				isolation()表示隔离级别：
					...
				propagation()表示事务的传播属性：
					...
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
						加了这个注解，那么这个类里面的方法抛出异常，就会回滚，数据库里面的数据也会回滚。
						不加，只会在遇到RuntimeException的时候才会回滚。
						附：
							1.异常分为Error和Exception。
							Exception分为RuntimeException（运行时异常）和非运行时异常。
							@Transactional只会对RuntimeException进行回滚。
							注：
								非运行时异常：
								如IOException、SQLException等以及用户自定义的Exception异常。
								对于这种异常，JAVA编译器强制要求我们必需对出现的这些异常进行catch并处理，否则程序就不能编译通过。
								所以，面对这种异常不管我们是否愿意，只能自己去写一大堆catch块去处理可能的异常。
						
				rollbackForClassName():
				noRollbackFor():
				noRollbackForClassName:
				
					
		*@Transactional 注意事项：
			1. 默认情况下，如果在事务中抛出了未检查异常（继承自 RuntimeException 的异常）或者 Error，则 Spring 将回滚事务；除此之外，Spring 不会回滚事务。
			你如果想要在特定的异常回滚可以考虑rollbackFor()等属性
			2. @Transactional 只能应用到 public 方法才有效。
			3. Spring 的 AOP 的自调用问题：
				在 Spring 的 AOP 代理下，只有目标方法由外部调用，目标方法才由 Spring 生成的代理对象来管理，这会造成自调用问题。
				若同一类中的其他没有@Transactional注解的方法内部调用有@Transactional注解的方法，有@Transactional注解的方法的事务被忽略，不会发生回滚。
				这个问题是由于Spring AOP 代理造成的(如下面代码所示）。
				另外一个问题是只能应用在public方法上。
				1.为解决这两个问题，使用 AspectJ 取代 Spring AOP 代理。
				2.使用自注入来解决！
注：
	事务的传播机制propagation：!!!
		可以控制调用另一个配置了事务的方法时如何参与当前事务。
		https://blog.csdn.net/qq_30336433/article/details/83111675
		附：
			https://segmentfault.com/q/1010000004263937?_ea=549435
		

			



			