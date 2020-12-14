mysql官网：
	https://www.mysql.com/
	https://dev.mysql.com/doc/refman/8.0/en/
	https://www.mysqlzh.com/ 5.1中文文档
https://github.com/Snailclimb/JavaGuide/blob/master/docs/database/MySQL.md	

SQL可分为两个部分：
	结构化查询语言(Structured Query Language)简称SQL，是一种数据库查询和程序设计语言.
	SQL可分为两个部分：
		数据操作语言 (DML DataManipulateLanguage) 和 数据定义语言 (DDL Data Definition Language)。
		./DML/
		./DDL/
存储引擎：
	hcg：什么是引擎，汽车通过引擎来使汽车前进，数据库管理系统则通过存储引擎来从数据库中读取和存入数据。
	5.7后MySQL默认的存储引擎是InnoDB,且只有 InnoDB 是事务性存储引擎。即只有 InnoDB 支持事务
	MVCC ：
		https://segmentfault.com/a/1190000012650596

事务：
	./事务/事务.txt
	
优化：
	./数据库优化.txt
	池化设计思想：
		...
附：
	查询缓存：
		执行查询语句的时候，会先查询缓存。不过，MySQL 8.0 版本后移除，因为这个功能不太实用
附2：

	mysql数据备份：
		https://www.runoob.com/mysql/mysql-database-export.html
		navicat导出数据库:
			要导出的数据库上右击鼠标->点击“转储SQL 文件”->“数据跟结构”。
		navicat自带备份功能。
		mysql自带备份功能。
	mysql回滚数据库：
		http://www.cppcns.com/shujuku/mysql/173077.html
		https://blog.csdn.net/lvshaorong/article/details/80631133
	mysql常见控制台命令：
		quit：
			退出mysql
		show tables;
			https://www.cnblogs.com/123456789qq/p/5891289.html
			显示数据库
		导入|导出：
			https://www.cnblogs.com/jwlfpzj/articles/7998395.html
			导入数据库source d:/dbname.sql
		改密码：
			use mysql;//此时要会提示Database changed
			update user set password=password("") where user="root";
			flush privileges; //刷新权限,使密码生效
		命令帮助手册：
			例 help drop indexa
		注：
			linux调用mysql控制台：
				mysql -uroot -p
			linux mysql的启动：
				https://www.cnblogs.com/microcat/p/6610963.html
			linux mysql的启动服务
				service mysql start　　　
			linux mysql的关闭服务
				service mysql stop
	mysql命令：
		查看数据库版本：
			select @@version;
		MySQL查看数据库和表容量大小：
			https://blog.csdn.net/fdipzone/article/details/80144166