mysql官网：
	https://www.mysql.com/
	https://dev.mysql.com/doc/refman/8.0/en/
	https://www.mysqlzh.com/ 5.1中文文档
https://github.com/Snailclimb/JavaGuide/blob/master/docs/database/MySQL.md	

SQL可分为两个部分：
 	结构化查询语言(Structured Query Language)简称SQL，是一种数据库查询和程序设计语言.
 	SQL可分为两个部分：
 		数据操作语言 (DML DataManipulateLanguage) 和 数据定义语言 (DDL Data Definition Language)。
		DML:
			./DML/README.txt
		DDL
			./DDL/README.txt
存储引擎：
	存储引擎就是对于数据库文件的一种存取机制。是对存储查询数据,为储存的数据建立索引,等技术方法的实现。
		hcg：什么是引擎，汽车通过引擎来使汽车前进，数据库管理系统则通过存储引擎来从数据库中读取和存入数据。
		https://blog.csdn.net/qq_46138725/article/details/106802402
	5.7后MySQL默认的存储引擎是InnoDB,且只有 InnoDB 是事务性存储引擎。
		即只有 InnoDB 支持事务
	mysql存储引擎总共有九种,常用的数据引擎有MyISAM,InnDB,MEMORY,ARCHIVE;
		MyISAM
			优缺点:
				优点：
					MyISAM的优势在于占用空间小,处理速度快
				缺点：
					就是不支持事务的完整性和并发性
				附：
					如果表中绝大多数都只是读查询，可以考虑 MyISAM
			MyISAM 非聚集索引。



		innodb
			优缺点:
				优点：InnoDB的优势在于提供了良好的事务处理,崩溃修复能力和并发控制,
				缺点：
					是读写效率较差,
					占用的数据空间相对较大.？
			InnoDB 是聚簇索引。
		memory
			用的hash散列
			基于内存非常快，不能进行持久化
		...
		附：
			一个数据库中多个表可以使用不同引擎以满足各种性能和实际需求，使用合适的存储引擎，将会提高整个数据库的性能。
	附：
        查看引擎：
            show ENGINES;

事务：
 	./事务/事务.txt

索引:
	./DML/索引/mysql索引.txt
存储过程:
	./DML/mysql存储过程.txt
	
优化：
	./数据库优化.txt
	池化设计思想：
		...
附：
	MySql 缓冲池(buffer pool)：！
		https://www.cnblogs.com/myseries/p/11307204.html
		
	查询缓存：
		执行查询语句的时候，会先查询缓存。不过，MySQL 8.0 版本后移除，因为这个功能不太实用
	mysql日志：
		./优化/诊断.txt goto:MySQL的日志
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