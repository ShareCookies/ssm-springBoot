https://github.com/Snailclimb/JavaGuide/blob/master/docs/database/MySQL.md

SQL可分为两个部分：
	数据操作语言 (DML DataManipulateLanguage) 和 数据定义语言 (DDL)。
存储引擎：
	hcg：什么是引擎，汽车通过引擎来使汽车前进，数据库管理系统则通过存储引擎来从数据库中读取和存入数据。
	5.7后MySQL默认的存储引擎是InnoDB,且只有 InnoDB 是事务性存储引擎。即只有 InnoDB 支持事务
	MVCC ：
		https://segmentfault.com/a/1190000012650596

事务：
	./事务.txt
	
优化：
	./数据库优化.txt
	池化设计思想：
		...
附：
	查询缓存：
		执行查询语句的时候，会先查询缓存。不过，MySQL 8.0 版本后移除，因为这个功能不太实用