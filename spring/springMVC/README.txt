# Spring MVC框架介绍
​	Spring MVC 是 Spring 提供的 Web 开发框架，用来构建Web应用程序。
​	springMVC处理请求过程：P175
​	​	springMvc执行流程：
​	​		https://www.cnblogs.com/tengyunhao/p/7518481.html
​			1、前端控制器DispatcherServlet：（一切的起源）
​				用户发来一个请求，首先接受的肯定是servlet容器（DispatcherServlet）。			
​			2、处理器映射器HandlerMapping（返回controller）
​				前端控制器（DispatcherServlet）收到请求后会解析请求，并通过处理器映射器（HandlerMapping）根据url信息找到我们需要的处理器（Handler）（也可以叫Controller）,返回给前端控制器。
​				例：
​					/**
​					 * Return the HandlerExecutionChain for this request.
​					 * <p>Tries all handler mappings in order.
​					 * @param request current HTTP request
​					 * @return the HandlerExecutionChain, or {@code null} if no handler could be found
​					 */
​					@Nullable
​					protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
​						if (this.handlerMappings != null) {
​							for (HandlerMapping mapping : this.handlerMappings) {
​								HandlerExecutionChain handler = mapping.getHandler(request);
​								if (handler != null) {
​									return handler;
​								}
​							}
​						}
​						return null;
​					}
​			3、处理器执行链HandlerExecutionChain（里头包含了controller）
​				处理器映射器返回的其实是处理器执行链(HandlerExecutionChain)，只是处理器执行链里包含了处理器。
​				而处理器就是我们写的controller层方法。
​			4、处理器适配器HandlerAdapter（通过适配器执行controller）
​				前端控制器(DispatcherServlet)查找合适的处理器适配器(HandlerAdapter)，通过处理器适配器执行处理器。
​				例：
​					HandlerExecutionChain mappedHandler = null;
​					...
​					ModelAndView mv = null;
​					// Determine handler adapter for the current request.
​					HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
​					// Actually invoke the handler.
​					mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
​				附：
​					控制器方法执行完后会有两种类型返回：1.视图名和模型 2. restful形式，返回的mv为null，即不走第5步。
​					1.返回视图名和模型：
​						控制器方法没有直接产生浏览器中渲染所需的HTML。
						控制器方法处理完后，会将请求连同模型和视图名塞入ModelAndView，发送回DispatcherServlet
						注：
				​			模型:!
				​				控制器在完成逻辑处理后，通常会产生一些要返回前端的数据信息，这些信息会被放入模型中（model）。
				​			视图:
								控制器通常会返回一个String类型的值，这个就是控制器名。
				​				非Ajax的话仅仅给前端数据信息是不够的，还得把这些信息与HTML一起整合。
​				所以，信息需要发送给一个视图（view），通常会是JSP。
​						附：
​							MVC思想：
​								介绍：
​									SpringMVC框架提供面向Web应用的MVC设计实现。
​									通过Model，View，Controller分离，将web层进行职责解耦。
​						​				把复杂的web应用分成逻辑清晰的几部分，简化开发，减少出错，方便组内开发人员之间的配合。
​						​			m:模型由JavaBean来构成，存放实体对象！
​						​			v:MVC容纳了大量视图技术，其中包括JSP、POI等，而视图是一个接口，负责实现模型，
​						​			c:控制器则实现逻辑代码。
​	
​								springMvc实现：
​									将控制器中请求处理的逻辑和视图中的渲染实现解耦是Spring MVC的 一个重要特性。
​									如果控制器中的方法直接负责产生HTML的话，就很 难在不影响请求处理逻辑的前提下，维护和更新视图。控制器方法和 视图的实现会在模型内容上达成一致，这是两者的最大关联，除此之 外，两者应该保持足够的距离。	
​			5、视图解析器ViewResolver
​				前端控制器接到ModelAndView之后，交由我们的视图解析器去查找合适的视图，最然进行视图渲染，渲染完成塞入response最终返回给用户。
​				例：
​					for (ViewResolver viewResolver : this.viewResolvers) {
​						View view = viewResolver.resolveViewName(viewName, locale);
​						if (view != null) {
​							return view;
​						}
​					}
​					视图渲染。
​					view.render(mv.getModelInternal(), request, response);
					//把模型数据交付给视图后，前端控制器的任务就完成了，视图将使用模型数据进行渲染然后输出，这个输出会通过响应对象直接传递给客户端。
​				附：
​					Spring自带的视图解析器：
​						Spring自带了13个视图解析器，能够将逻辑视图名转换为物理实现.
​						InternalResourceViewResolver：
​							将视图解析为Web应用的内部资源（一般为 JSP）.
​							InternalResourceViewResolver会根据约定，在视图名上添加前缀和后缀，进而确定一个 Web应用中视图资源的物理路径。
​							例：
​								控制器返回视图逻辑名home，它将会解析为“/WEB-INF/views/home.jsp”
​								nternalResourceViewResolver resolver = new Internal ResourceViewResolver() ;
​								resolver.setPrefix("/WEB-INF/views/");
​								resolver.setSuffix(".jsp") ;
​						UrlBasedViewResolver：
​							直接根据视图的名称解析视图，视图的名称会 匹配一个物理视图的定义
​							...
​					自定义视图解析器：
​						编写ViewResolver和 View的实现...
​	
# Spring MVC框架的使用：
##  1.配置DispatcherServlet：
​		1.使用Java将DispatcherServlet配置在Servlet容器中：p177
​			...
​		附：
​			xml配置DispatcherServlet：
​				1.如果是将应用部署到Servlet 3.0容器中，
​				那么Spring提供了多种方式来注册Servlet（包括DispatcherServlet）、Filter和Listener，而不必创建web.xml文件。
​				2.但如果你需要将应用部署到不支持Servlet 3.0的容器中（或者你只是希望使用web.xml文件），
​				那么我们就要按照传统的方式，通过web.xml配置Spring MVC。
​			添加其他的Servlet和Filter：p248
​				例：
​					./附属功能/拦截器
##  2.启用Spring MVC:p180
​		基于Java进行配置：
​			配置类上加上@EnableWebMvc注解，即可启用springMVC。

##  3.编写控制器：
		1. 编写基本的控制器：
			1. 控制器类注册为spring容器的bean。
				./控制层注解.txt goto: @Controller
				例：
					@Controller
					public class TestController {}
			通过@Controller，和@RequestMapping（springMvc请求映射注解）等来编写控制器。
			2. 定义一个方法来处理指定的请求：
				./控制层注解.txt goto: @RequestMapping
			3. 数据返回
				返回的数据可分为2种 ：1.html页面 2.其他某种格式的数据（如json，xml等）。
			
				1. 编写模型视图类型（mvc）控制器：（返回html页面）
					配置视图解析器：p181 p210（配置一次就好）
						控制器方法返回的字符串将会被Spring MVC解读为要渲染的视图名称。
						DispatcherServlet会要求视图解析器将这个逻辑名称解析为实际的视图。
						例：
							@Bean
							public ViewResolver viewResolver() {
								InternalResourceViewResolver resolver = new Internal ResourceViewResolver() ;
								resolver.setPrefix("/WEB-INF/views/");
								resolver.setSuffix(".jsp") ;
								//resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);//让InternalResourceViewResolver将视图解析 为JstlView（jsp更进一步），而不是InternalResourceView
								return resolver ;
							}
					1. 控制器返回模型与视图名：
						@RequestMapping(value="/register",method=GET)
						public StringshowRegistrationForm(Modelmodel){//1.返回模型
							model.addAttribute(newSpitter());//新增了一 个Spitter实例到模型中。//模型中的key是spitter，是根据对象类型推断得到的
							return"registerForm";//2.返回视图名
						}
						附：
							控制器的处理方法完成时，通常会返回一个逻辑视图名。
							如果方法不直接返回逻辑视图名（例如方法返回void），那么逻辑视图名会根据请求的URL判断得出。
					2. 视图读取模型数据
						registerForm.jsp：
						<sf:form method="POST" commandName="spitter">
							FirstName:<sf:input path="firstName"/>...
						</sf:form>
					附：
						./图片/传递模型数据到视图中.png
						./图片/视图中访问模型数据.png
				2. 编写REST类型控制器：(返回json或xml类型数据)
					什么是rest：
						./REST/README.txt
					编写REST类型控制器:
						为控制器方法使用@ResponseBody注解，即可返回json或xml类型数据（看Accept请求头）。
						例：
							./控制层注解.txt goto: @ResponseBody
							@ResponseBody
							public String loginSuccess(HttpServletRequest request){  
								return "loginSuccess";  
							}
						详情：
							./REST/spring实现RESTful架构.txt
		2. 接收请求的输入：p193
			./控制层注解.txt goto: 数据的获取
		3. 提供资源之外的其他内容
			请求头设置或错误信息等 p516
			处理异常：
				./附属功能/异常处理.txt
		附:
			控制器编写注意事项：
				设计良好的控制器本身只处理很少甚至不处理工作，而是将业务逻辑委托给一个或多个服务对象进行处理。
				附：控制器只是用来对外开放请求的，顶多对请求参数做一点处理，所有的业务等逻辑要放到服务层中进行。
			输入的校验：
				./附属功能/输入校验/
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
	附:
		sprnigBoot中应用springMvc：
			SpringBoot为我们自动配置好了SpringMVC框架，所以通过SpringBoot使用Spring MVC框架，我们可以只需引入mvc的start启动依赖，@EnableWebMvc启用springMVC框架，编写控制器即可。
		springMvc小技巧：
			1.Spring MVC获取所有注册的url
				https://blog.csdn.net/aitcax/article/details/54089686
			2.控制器单元测试P186