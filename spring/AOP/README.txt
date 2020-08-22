附：
	历史：
		oop的（Object Oriented Programming 面向对象编程）封装要求将功能分散到不同的对象中去，这在软件设计中往往称为职责分配。
		这样代码就分散到一个个的类中去了。
		这样做的好处是降低了代码的复杂程度，使类可重用。但是在分散代码的同时，也增加了代码的重复性。
			系统由许多不同的组件组成，每一组件除了实现自身核心的功能之外，还经常承担着额外的职责。
			诸如日志等，如果将这些关注点（关注点就是日志指日志等）分散到多个组件中去，你的代码将会带来双重的复杂性，
			即使你把这些关注点抽象为一个独立的模块，其他模块只是调用它的方法，但方法的调用还是会重复出现在各个模块中。
		接着aop思想就应运而生，它使各组件仅需实现自身核心功能，无需关心其他额外的功能，而是由对应的aop组件来实现。
		附：
			如果要重用通用功能的话，最常见的面向对象技术是继承，inheritance)或委托( delegation)。
			但是，如果在整个应用中都使用相同的基类，继承往往会导致一个脆弱的对象体系;而使用委托可能需要对委托对象进行复杂的调用。
			切面提供了取代继承和委托的另一种可选方案，而且在很多场景下更清晰简洁。
			在使用面向切面编程时，我们仍然在一个地方定义通用功能，但是可以通过声明的方式定义这个功能要以何种方式在何处应用，而无需修改受影响的类。
			横切关注点可以被模块化为特殊的类，这些类被称为切面(aspect) 。
			这样做有两个好处:首先，现在每个关注点都集中于-一个地方，而不是分散到多处代码中;其次，服务模块更简洁，因为它们只包含主要关注点(或核心功能)的代码，而次要关注点的代码被转移到切面中了。
AOP编程思想介绍：
	AOP aspect-oriented programming 面向切面编程。
	在运行时，动态地将代码切入到类的指定方法的指定位置上的编程思想就是面向切面编程。
		
	附：
		aop思想使各组件仅需实现自身核心功能，无需关心其他额外的功能，而是由对应的aop组件来实现。
		附：
			aop实现的核心技术是通过java代理和反射。
AOP术语:
	注：
		术语是对某一学科（或技术），的某一专业名词（或知识点）的称呼。
	通知（Advice）
		切面的要完成的工作被称为通知。
		通知还解决了何时执行这个工作的问题。即：方法前置还是后置等。
		注：
			一个切面有多个通知，即：一个切面要完成多个工作。
		Spring切面可以应用5种类型的通知：
			前置通知（Before）：在目标方法被调用之前调用通知功能；
			后置通知（After）：在目标方法完成之后调用通知，此时不会关心方法的输出是什么；
			返回通知（After-returning）：在目标方法成功执行之后调用通知；
			异常通知（After-throwing）：在目标方法抛出异常后调用通知；
			环绕通知（Around）：通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为。
		附：
			Spring通知是Java编写的。
			Spring在运行时通知对象。
				./spring以代理实现aop的原理图.png
	连接点（Join point）:
		能够被插入切面的一个点，就算连接点。
		这个点可以是调用方法时、抛出异常时、甚至修改一个字段时。切面代码可以利用这些点插入到应用的正常流程之中，并添加新的行为。
		注：
			能插入切面的地方都算连接点，无需用代码定义连接点。
		注：
			Spring只支持方法级别的连接点。
	切点（Poincut）
		切点的定义，会匹配通知织入到一个或多个连接点。
		即：
			切点用于准确定位应该在什么地方应用切面的通知。
	切面（Aspect）
		切面是通知和切点的结合。通知和切点共同定义了切面的全部内容。
			即：何处（切点），何时，做什么（通知）。
		
	引入（Introduction）p159
		切面的引入功能允许我们向现有的类添加新方法或属性。
		注：
			解析：
			当引入接口的方法被调用时，代理会把此调用委托给实现了新接口的某个其他对象。
			实际上，一个bean的实现被拆分到了多个类中。
		例：
			./aop引入.png
	织入（Weaving）
		切面的织入功能能够为能够为现有的方法增加额外的功能
			注：
				织入是把切面应用到目标对象并创建新的代理对象的过程。
		例：
			goto：创建切面类。
		附：
			目标对象生命周期能进行织入时间点：
				编译期：
				类加载期：
				运行期：
					切面在应用运行的某个时刻被织入。
					一般情况下，在织入切面时，AOP容器会为目标对象动态地创建一个代理对象。
					Spring AOP就是以这种方式织入切面的。
			?:
				但是被通知类中没有这个方法啊，调用了不会报错吗。

Spring实现AOP的四种方式： 
	注：
		前三种都是Spring AOP实现的变体。
			（即前3种的实现原理都是一样的。）
		Spring AOP构建在动态代理基础之上，因此，Spring对AOP的支持局限于方法拦截。

	基于代理的经典Spring AOP：	
		介绍：
			曾经Spring的经典AOP编程模型的确非常棒，但是现在就显得非常笨重和过于复杂，直接使用ProxyFactory Bean会让人感觉厌烦。
			现在Spring提供了更简洁和干净的面向切面编程方式，引入了简单的声明式AOP和基于注解的AOP。
		例：
			./例子/SpringAOPAchieveSimply/
			./例子/SpringAOPAchieveWithDefaultPointcutAdvisor/
			./例子/SpringAOPAchieveWithNameMatchMethodPointcutAdvisor/
	纯POJO切面；(XML中声明切面)p160
		介绍：
			借助Spring的aop命名空间，我们可以将纯POJO转换为切面。
				实际上，这些POJO只是提供了满足切点条件时所要调用的方法。
			遗憾的是，这种技术需要XML配置，但这的确是声明式地将对象转换为切面的简便方式。
		附：
			1.
			面向注解的切面声明有一个明显的劣势，你必须能够为通知类添加注解。为了做到这一点，必须要有源码。 
			如果你没有源码的话，可考虑在Spring XML配置文件中声明切面。
	@AspectJ注解驱动的切面；
		注解创建切面：
			介绍：
				Spring借鉴了AspectJ的切面，提供注解驱动的AOP，编程模型几乎与AspectJ一致。
					注：本质上依然是Spring基于代理来实现的AOP。
				这种AOP风格的好处在于能够不使用XML来完成功能。
				
			1.定义一个切面类:p148
				类使用@Aspect标注,标明这是一个切面类。
				通知和切点是切面的最基本元素。
				（即包含了通知和切点的类+切面声明注解，这个类就是切面）
				例：
					./定义切面.png	
				附：
					切面类只是一个Java类，只不过它通过注解表明会作为切面使用而已。

			2.编写切点(编写AspectJ风格的切点)：
				介绍：
					通过切点来选择连接点，即切点用于准确定位应该在什么地方应用切面的通知。
					在Spring AOP中，要使用AspectJ的切点表达式语言来定义切点.
					切点表达式：
						https://blog.csdn.net/corbin_zhang/article/details/80576809
						切点指示器：
							切点指示器仅是构成切点表达式的一部分。
							execution指示器是实际执行匹配的，而其他的指示器都是用来限制匹配的。
									因此execution指示器是我们在编写切点定义时最主要使用的指示器。
									在此基础上，我们使用其他指示器来限制所匹配的切点。
							
							附：
								1.Spring AOP所支持的AspectJ切点指示器。P145
									Spring仅支持AspectJ切点指示器( pointcut designator)的一个子集。
									因为springAop是基于代理的，而某些切点表达式springAop无法支持。
								2.在Spring中尝试使用AspectJ其他指示器时，将会拋出Illegal Argument-Exception异常
								3.bean()指示器：p147
									除了表4.1所列的指示器外，Spring还引入了一个新的bean()指示器。
									允许我们在切点表达式中使用，bean ID或bean名称作为参数来限制切点只匹配特定的bean。
									例：
										execution(* concert.Performance.perform() ) and bean('woodstock')
										在这里，我们希望在执行Performance的perform()方法时应用通知，但限定bean的ID为woodstock.
										例2：
											execution(* concert.Performance.perform() ) and !bean('woodstock')

				例：p146					
					例1：
					该切点表达式能够设置当Performance的perform()方法执行时触发通知的调用：
					
						execution(* concert.Performance.perform(..))

						我们使用execution ()指示器选择Performance的perform()方法。
						方法表达式以*号开始，表明了我们不关心方法返回值的类型。
						然后，我们指定了全限定类名(包名.类名)和方法名。
						对于方法参数列表，我们使用两个点号(..)表明切点要选择任意的perform()方法，无论该方法的入参是什么。
						./AspectJ切点表达式.png
					例2：
					用within()指示器限制切点范围
						execution (* concert.Performance.perform(..) )&& within ( concert.*))
						...
				附
					重用切点表达式：
						@Pointcut注解能够在一个@AspectJ切面内定义可重用的切点。
						例：p150
						注：
							@Pointcut注解标注的方法并不重要，方法本身只是一个标识，供@Pointcut注解依附。
			3.定义通知：
				注：
					1.通知注解都要给定了一个切点表达式作为它的参数，
					表明这个通知会在什么地点被调用。
					
					2.使用了通知注解来表明通知方法（切面中的方法）应该在什么时候调用。
					AspectJ提供了五个注解来定义通知：p149
							@After 通知方法会在目标方法返回或抛出异常后调用
							@AfterReturning 通知方法会在目标方法返回后调用
							@AfterThrowing 通知方法会在目标方法抛出异常后调用
							@Around 通知方法会将目标方法封装起来
								环绕通知:p152
									环绕通知就像在一个通知方法中同时编写前置通知和后置通知。
							@Before 通知方法会在目标方法调用之前执行
					3.处理通知中的参数：
						如何在通知中访问和使用传递给被通知方法的参数。
						例：
							p154
							@Before(execution (* soundsystem.CompactDisc.playTrack(int) ) && args ( trackNumber ))
							public void countTrack(int trackNumber){
							}
							切点表达式中的args (trackNumber)限定符，它表明传递给playTrack()方法的int类型参数也会传递到通知中去。
				例：
					./定义切面.png	
													
			4.启用自动代理：
				介绍：
					如果你就此止步的话，切面类只会是容器中的一个bean。
					即便使用了AspectJ注解，但它并不会被视为切面，这些注解不会解析，spring也不会创建将切面类转换为切面的代理。
					AspectJ自动代理会为使用@Aspect注解的bean创建一个代理，这个代理会围绕着所有该切面的切点所匹配的bean。		

				JavaConfig中启用AspectJ注解的自动代理：p151
					如果你使用JavaConfig的话，可以在配置类的类级别上通过使用@EnableAspectJAutoProxy注解启用自动代理功能。
					例：
						@Configuration 
						//启用Aspectj自动代理
						@EnableAspectJAutoProxy
						@ComponentScan
						public class ConcertConfig {
							@Bean
							public Audience audience ( ) {
								return new Audience() ; 
							}
						}

				xml中启用AspectJ注解的自动代理：
					p151
			例：
				完整案例代码
				./例子/SpringAspectjAnnotaion/
		注解让切面实现引入功能：
			p157
	注入式AspectJ切面(适用于Spring各版本)：p169
		介绍：	
			如果你的AOP需求超过了简单的方法调用（如构造器或属性拦截）， 那么你需要考虑使用AspectJ来实现切面。
			在这种情况下，上文所示的第四种类型能够帮助你将值注入到AspectJ驱动的切面中。
		附：
			AspectJ提供了Spring AOP所不能支持的许多类型的切点。

	

