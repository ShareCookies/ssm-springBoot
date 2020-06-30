mybaits教程：
	官网：
		https://blog.mybatis.org/ 
		官网中文手册：https://mybatis.org/mybatis-3/zh/index.html
	https://www.yiibai.com/mybatis/install_configure.html
	http://www.dba.cn/book/mybatis/
	MyBatis从入门到精通.pdf 刘增辉
	https://mp.baomidou.com/guide/page.html
	https://baomidou.oschina.io/mybatis-plus-doc/#/?id=%E7%AE%80%E4%BB%8B
介绍：
	MyBatis 是一款优秀的支持自定义 SQL 查询、存储过程和高级映射的持久层框架。
	MyBatis消除了几乎所有的 JDBC 代码和参数的手动设置以及结果集的检索 。
	
	MyBatis特性：
		1.MyBatis 使用 XML 进行 SQL与Java 方法的关联。
		2.MyBatis 通过将参数映射到配置好的 SQL-》形成最终执行的 SQL 语句-》最后将执行SQL 结果映射成 Java 对象返回。
		注：
			1.sql语句与java方法的映射也可以使用注解实现，不使用xml。
			2.与 ORM （对象关系映射）框架不同， Mybtis 并没有将 Java 对象与数据库表关联起来，
			实体类只是个数据值对象，即实体类只是用来便于存放数据的。
			3.（多余）实体类名无需与数据库表名对上，实体类名和mapper.xml文件名对上即可。
			因为mybaits是通过sql语句的from与数据库表匹配上的。
			推荐：实体类名和数据库表名对上，便于程序员查找数据。
		mybaits执行过程的简易解析：
			MyBatis通过使用一种内建的类XML 表达式语言， SQL 语句可以被动态生成。
			MyBatis底层使用 JDBC执行 SQL ，获得查询结果集。
			MyBatis 通过一个映射引擎，声明式地将结果集与对象树映射起来。
			
mybaits的简易使用:
	/springBoot/附属功能/springBoot集成Mybaits/
	https://github.com/yuyumyself/ssm-springBoot/tree/master/springBoot/%E9%99%84%E5%B1%9E%E5%8A%9F%E8%83%BD/springBoot%E9%9B%86%E6%88%90Mybaits
	附：
		./Mybaits的多种使用方式.txt
附：
	mapper代码自动生成：
		https://github.com/yuyumyself/UtilsProject/tree/master/autoGenerateMapper