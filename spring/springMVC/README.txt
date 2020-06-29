Spring MVC框架介绍
	Spring MVC 是 Spring 提供的 Web 开发框架，用来构建Web应用程序。
	附：
		设计思想基于MVC设计模式：
			MVC设计模式
			http://c.biancheng.net/view/4391.html
springMVC处理请求过程：P175
	跟踪请求：
		请求从客户端发起，
		经过Spring MVC组件（Spring将请 求在DispatcherServlet、处理器映射（handler mapping）、控制器以及视图 解析器（view resolver）之间移动），
		最终再返回到客户端的。
		例：
			./图片/springMVC处理请求的过程.png
		https://www.cnblogs.com/tengyunhao/p/7518481.html
	控制器的选择：
		DispatcherServlet的任务是将请求发送给Spring MVC控制器。
		I.请求旅程的第一站是Spring的DispatcherServlet。	
			注：
				与大多数基于Java的Web框架一样，Spring MVC所有的请求都会通过一个前端控制器Servlet。
				前端控制器是常用的Web应用程序模式，在这里一个单实例的Servlet将请求委托给应用程序的其他组件来执行实际的处理。
		II.DispatcherServlet会查询一个或多个处理器映射（handler mapping） 来确定将请求发送到那个控制器。
			注：
				处理器映射会根据请求所携带的URL信息来进行决策。
	控制器的处理过程：
		I.到了控制器，请求会卸下其负载（用户提交的信息）并耐心等待控制器处理这些信息。
		注：
			设计良好的控制器本身只处理很少甚至不处理工作，而是将业务逻辑委托给一个或多个服务对象进行处理。
		II.处理完后控制器会将请求连同模型和视图名发送回DispatcherServlet。
		注：
			模型:!
				控制器在完成逻辑处理后，通常会产生一些要返回前端的数据信息，这些信息可被称为模型（model）。
			视图:
				非Ajax的话仅仅给前端数据信息是不够的，还得把这些信息与HTML一起整合。
				所以，信息需要发送给一个视图（view），通常会是JSP。
		III.DispatcherServlet处理控制器返回的数据：
			DispatcherServlet将会使用视图解析器（view resolver）来将逻辑视图名与一个特定的视图匹配，它可能是也可能不是JSP。
			把模型数据交付给视图后，请求的任务就完成了。
			视图将使用模型数据进行渲染然后输出，这个输出会通过响应对象传递给客户端 。？
			注：
				数据时如何交付给视图的？response?
				视图是如何渲染数据的？jsp自带的?
				渲染后是把视图转成html，然后通过http协议的响应传递给前端的吗？
Spring MVC框架的使用：
	1.配置DispatcherServlet：
		1.使用Java将DispatcherServlet配置在Servlet容器中：p177
			...
		注：
			xml配置DispatcherServlet和java配置DispatcherServlet区别：
				1.如果是将应用部署到Servlet 3.0容器中，那么Spring提供了多种方式来注册Servlet（包括DispatcherServlet）、Filter和Listener，而不必创建web.xml文件。
				2.但如果你需要将应用部署到不支持Servlet 3.0的容器中（或者你只是希望使用web.xml文件），那么我们完全可以按照传统的方式，通过web.xml配置Spring MVC。

			添加其他的Servlet和Filter：p248
				...
	2.启用Spring MVC:p180
		基于Java进行配置：
			配置类上加上@EnableWebMvc注解，即可启用springMVC。
	3.配置视图解析器：p181 p210
		控制器方法返回的字符串将会被Spring MVC解读为要渲染的视图名称。
			DispatcherServlet会要求视图解析器将这个逻辑名称解析为实际的视图。
		附：
			视图解析的基础知识：p211
			使用Spring的JSP库：p216
	4.编写控制器：
		编写基本的控制器：
			通过@Controller，和@RequestMapping（springMvc请求映射注解）等来编写控制器。
			./控制层注解.txt
			./请求参数的映射.txt
		传递模型数据到视图中：
			例：
				./图片/传递模型数据到视图中.png
				./图片/视图中访问模型数据.png
		接收请求的输入：
			p193
		附:
			输入的校验：
				./附属功能/输入校验/

			处理异常：
				./附属功能/异常处理.txt > spring控制层异常的处理
			控制器通知:
				P268
			请求重定向并传递数据：
				p269		
			文件上传:
				1.multipart形式的数据:
					./图片/表单之multipart形式的数据.png
				I.配置multipart解析器:p255
					介绍：
						编写控制器方法处理文件上传之前，我们必须要配置一个multipart解析器，
						通过它来告诉DispatcherServlet该如何读取multipart请求。
					？：
						设置临时文件路径。？
				II.处理multipart请求：p259
					编写控制器方法来接收上传的文件。
					I.最常见的方式就是在某个控制器方法参数上添加@RequestPart注解。
						1.添加byte数组参数:p259
							其中一种方式是添加byte数组参数，并为其添加@RequestPart注解。
						2.添加MultipartFile接口参数:
						3.添加Part接口参数:p262
							如果你将应用部署到Servlet 3.0的容器中，那么会有MultipartFile的一个替代方案。
							Spring MVC也能接受javax.servlet.http.Part作为控制器方法的参数,来接收文件。
							注：
								如果在编写控制器方法的时候，通过Part参数的形式接受文件上传，那么就没有必要配置MultipartResolver（multipart解析器）了。
								只有使用MultipartFile的时候，我们才需要MultipartResolver。？？？

					*注：
						<form>标签现在将enctype属性设置为multipart/formdata。
						这会告诉浏览器以multipart数据的形式提交表单，而不是以表单数据的形式进行提交。在multipart中，每个输入域都会对应一个part。
						
				附：
					springboot文件上传： https://blog.csdn.net/qq_36595528/article/details/88806885
	注：
		sprnigBoot中应用springMvc：？
			...
			SpringBoot为我们自动配置好了SpringMVC框架，所以 ，Spring MVC框架的使用的4个步骤只需最后一个步骤即可。
			
			
			https://www.jianshu.com/p/f7e55a5ed0fa
			https://www.cnblogs.com/wujunstart/p/12170038.html
			https://blog.csdn.net/hejian_0534/article/details/100692557
		springMvc支持rest风格编程：!(待整理)
			./REST/
	附:
		springMvc小技巧：
			1.Spring MVC获取所有注册的url
				https://blog.csdn.net/aitcax/article/details/54089686
			2.控制器单元测试P186