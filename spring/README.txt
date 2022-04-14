spring框架知识体系串联：
	spring框架起点：
		spring框架是以springcore为核心的，spring框架的所有组件都要依赖springcore的ioc、aop功能。
		springcore通过ioc容器（bean工厂、ApplicationContext 应用上下文）来提供ioc和aop功能。
			？上下文具体实现原理？怎么提供ioc和aop的源码分析
			？
				spring和tomcat是怎么结合的
		ioc：控制反转
			控制反转核心概念就是对象的实例化不由用户进行，而是程序控制。
			Spring就使用ioc容器来控制管理所有的Bean对象，当应用上下文加载好bean配置后就开始创建bean。（控制反转）
				工厂是用到bean时才创建。
			bean的生命周期则有：
				构造器初始化(bean加载顺序)、属性注入(di)、
				接着就是各种注入回调、初始化回调、销毁回调等。
					对于这些回调可通过实现指定接口来应用。
			bean创建好后就可以从容器中开始获取bean并开始使用，此时就涉及到bena作用域问题。
			bena作用域：
				用户用一对象一般new一次，但从容器中获取却不是new的，而是很早就创建 你每次获取的都是这个对象。(单例作用域)
				其余作用域：单例、原型、请求、会话
		aop：面向切面编程
			原理：aop就是为一对象创建代理，当你调用对象方法时 你调的其实是代理的方法 然后代理进行一些操作后才会决定是否调用对象方法。
			
			应用：编写切面、编写切点、编写通知
			springaop实现，当代理接口时就用java动态代理、类时就用cglib。
			spring中aop的典型应用就是spirngtransaction。
	
	springmvc.
		springmvc通过dispatcherServlet拦截请求，然后将其转发到我们写的控制器，
			？
				别的servlet就接收不到吗
			？
				spring的filter在dispatcherServlet前面吗，
				spring的filter原理
		调度器根据控制器返回内容，决定是用模型渲染还是用消息转换器转换后返回。
			？
				为什么restReseponse修饰的就会应用消息转换器。
	数据持久化：
		1. 怎么连接数据库并进行操作
			集成mybatis：
				...


		2. 连接上数据库后，怎么开启事务后，在执行sql
		spirngtransaction
			原理：aop代理对应方法，然后用事务模板来为方法提供事务支持。
				???
					为什么事务模板可以实现事务

			通过spirngtransaction相关注解我们就可以为方法加上事务功能。
			当方法中抛出异常时，就可以回滚方法中执行的sql。

			注：
			1. 调用事务方法时，一定要通过来容器获取bean并调用其方法，而不能直接.this，因为.this就不走代理了 而是当前对象。
				属性里注入的bean其实是代理对象
			2. 只应用public方法
			3. (rollbackFor=Exception.class)
			4. 当有方法嵌套时，spirng为我们提供了事务传递性， 来决定各方法事务是独立的 还是公用一个事务模板
				？
					事务已被标记回滚异常，是因为其实就用一个事务模板导致的吗
	springboot
		spirng的快速开发框架，其主要提供了以下功能来实现快速开发web应用。
		starter启动、约定优先于配置、和springboot的快速开发注解
	springsecurity
		web应用开发完后，总得有用户权限认证吧，security就起到这个作用。
		security通过filter保护web请求，通过认证管理器找到对应的认证提供者对请求进行校验 通过则放行。
		
		
		
		
		
		
		
		
		
		
		
		
		
		

集成mybatis:
	...
	？
		https://www.zhihu.com/question/485758860/answer/2369788533
	mybatis是个半 ORM（对象关系映射）框架，mybatis能映射java对象到查询参数和结果，但在查询关联对象等时，需要手动编写SQL来完成，所以，被称之为半自动ORM映射工具。
	MyBatis使用过程
		MyBatis基本使用的过程大概可以分为这么几步：
		 1. 创建SqlSessionFactory
		 2. 通过SqlSessionFactory创建SqlSession
			SqlSession（会话）可以理解为程序和数据库之间的桥梁
			？会话和jdbc连接区别
		3、 通过sqlsession执行数据库操作
			1. 可以通过 SqlSession 实例来直接执行已映射的 SQL 语句：
			
			2. 更常用的方式是先获取Mapper(映射)，然后再执行SQL语句：
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = mapper.selectBlog(101);


		4、 调用session.commit()提交事务
			如果是更新、删除语句，我们还需要提交一下事务。
		5、 调用session.close()关闭会话
	生命周期：
		一般说的MyBatis生命周期就是这些组件的生命周期。

		SqlSessionFactoryBuilder：
			一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的生命周期只存在于方法的内部。
		SqlSessionFactory
			SqlSessionFactory 是用来创建SqlSession的，相当于一个数据库连接池？，每次创建SqlSessionFactory都会使用数据库资源，多次创建和销毁是对资源的浪费。
			所以SqlSessionFactory是应用级的生命周期，而且应该是单例的。
			
		SqlSession
		
			SqlSession相当于JDBC中的Connection，SqlSession 的实例不是线程安全的？，因此是不能被共享的，
			所以它的最佳的生命周期是一次请求或一个方法。
		Mapper
			映射器是一些绑定映射语句的接口。映射器接口的实例是从 SqlSession 中获得的，它的生命周期在sqlsession事务方法之内，一般会控制在方法级。
	与spring集成：
		MyBatis通常也是和Spring集成使用
		Spring可以帮助我们创建线程安全的、基于事务的 SqlSession 和映射器，并将它们直接注入到我们的 bean 中，我们不需要关心它们的创建过程和生命周期，那就是另外的故事了。
		集成原理？
		
		
我们已经大概知道了MyBatis的工作流程，按工作原理，可以分为两大步：生成会话工厂、会话运行。
会话运行
	会话运行是MyBatis最复杂的部分，它的运行离不开四大组件的配合：
Executor（执行器）
	Executor起到了至关重要的作用，SqlSession只是一个门面，相当于客服，真正干活的是是Executor。
	它提供了相应的查询和更新方法，以及事务方法。
	Environment environment = this.configuration.getEnvironment();
	TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment);
	tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
	//通过Configuration创建executor
	Executor executor = this.configuration.newExecutor(tx, execType);
	var8 = new DefaultSqlSession(this.configuration, executor, autoCommit);

StatementHandler（数据库会话器）
	处理数据库会话的。
	我们以SimpleExecutor为例，看一下它的查询方法，先生成了一个StatementHandler实例，再拿这个handler去执行query。
		public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
			Statement stmt = null;

			List var9;
			try {
			Configuration configuration = ms.getConfiguration();
			StatementHandler handler = configuration.newStatementHandler(this.wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
			stmt = this.prepareStatement(handler, ms.getStatementLog());
			var9 = handler.query(stmt, resultHandler);
			} finally {
			this.closeStatement(stmt);
			}

			return var9;
		}
PreparedStatementHandler:
	再以最常用的PreparedStatementHandler看一下它的query方法，其实在上面的prepareStatement已经对参数进行了预编译处理，到了这里，就直接执行sql，使用ResultHandler处理返回结果。
	public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
	PreparedStatement ps = (PreparedStatement)statement;
	ps.execute();
	return this.resultSetHandler.handleResultSets(ps);
	}

	ParameterHandler （参数处理器）
	PreparedStatementHandler里对sql进行了预编译处理
	public void parameterize(Statement statement) throws SQLException {
	this.parameterHandler.setParameters((PreparedStatement)statement);
	}

	这里用的就是ParameterHandler，setParameters的作用就是设置预编译SQL语句的参数。
	里面还会用到typeHandler类型处理器，对类型进行处理。
	public interface ParameterHandler {
			Object getParameterObject();

		void setParameters(PreparedStatement var1) throws SQLException;
		}

ResultSetHandler（结果处理器）
	我们前面也看到了，最后的结果要通过ResultSetHandler来进行处理，handleResultSets这个方法就是用来包装结果集的。Mybatis为我们提供了一个DefaultResultSetHandler，通常都是用这个实现类去进行结果的处理的。
	public interface ResultSetHandler {
	<E> List<E> handleResultSets(Statement var1) throws SQLException;

	<E> Cursor<E> handleCursorResultSets(Statement var1) throws SQLException;

	void handleOutputParameters(CallableStatement var1) throws SQLException;
	}
	它会使用typeHandle处理类型，然后用ObjectFactory提供的规则组装对象，返回给调用者。
整体上总结一下会话运行：
	
