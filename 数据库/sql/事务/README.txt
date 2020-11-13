事务介绍：
	事务（transaction）是指访问并可能更新数据库中数据项的一个程序执行单元(unit)。
	事务要满足以下四个特性：
		1、A (Atomicity 原子性) 
			事务中各项操作，要么全做要么全不做，任何一项操作的失败都会导致整个事务的失败；
				附：事务成功的条件是事务里的所有操作都成功，只要有一个操作失败，整个事务就失败，需要回滚。
			例：
				比如银行转账，从A账户转100元至B账户，分为两个步骤：1）从A账户取100元；2）存入100元至B账户。这两步要么一起完成，要么一起不完成，如果只完成第一步，第二步失败，钱会莫名其妙少了100元。
		2、C (Consistency 一致性) 	
			事务使数据库从一个一致性状态变到另一个一致性状态。
				附：在事务开始之前和事务结束以后，数据库的完整性没有被破坏。
			辅助理解：
				维基中文：在事务开始之前和事务结束以后，数据库的完整性没有被破坏。这表示写入的资料必须完全符合所有的预设约束、触发器、级联回滚等。
					符：
						
						1.事务使数据库从一个一致性状态变到另一个一致性状态。
						
						事务T开始时，此时数据库处于一种状态，如数据库完整性约束正确，日志状态一致等，当事务T提交后，这时数据库又有了一个新的状态，但此时约束，数据，索引，日志等MySQL各种对象还是要保持一致性（正确性）。 这就是 从一个一致性的状态，变到另一个一致性的状态，也就是事务执行后，一切都是对的。
						https://blog.csdn.net/evilcry2012/article/details/78014685?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
						2.数据一致性：
							数据一致性则要由事务一致性配合业务代码进行约束操作实现。	
							例：现有完整性约束a+b=10，如果一个事务改变了a，那么必须得改变b，使得事务结束后依然满足a+b=10，否则事务失败。
							（丢失修改了？）
						3.完整性约束：
							https://zhuanlan.zhihu.com/p/108927381
						MySQL 是怎么保证数据一致性的：
							https://www.cnblogs.com/xibuhaohao/p/11835896.html
							https://blog.51cto.com/xxr007/1965600

				Wikipedia：
					Consistency
						Consistency ensures that a transaction can only bring the database from one valid state to another, maintaining database invariants: any data written to the database must be valid according to all defined rules, including constraints, cascades, triggers, and any combination thereof. This prevents database corruption by an illegal transaction, but does not guarantee that a transaction is correct. Referential integrity guarantees the primary key – foreign key relationship.
					Consistency failure
						Consistency is a very general term, which demands that the data must meet all validation rules. In the previous example, the validation is a requirement that A + B = 100. All validation rules must be checked to ensure consistency. Assume that a transaction attempts to subtract 10 from A without altering B. Because consistency is checked after each transaction, it is known that A + B = 100 before the transaction begins. If the transaction removes 10 from A successfully, atomicity will be achieved. However, a validation check will show that A + B = 90, which is inconsistent with the rules of the database. The entire transaction must be cancelled and the affected rows rolled back to their pre-transaction state. If there had been other constraints, triggers, or cascades, every single change operation would have been checked in the same way as above before the transaction was committed. Similar issues may arise with other constraints. We may have required the data types of both A and B to be integers. If we were then to enter, say, the value 13.5 for A, the transaction will be cancelled, or the system may give rise to an alert in the form of a trigger (if/when the trigger has been written to this effect). Another example would be with integrity constraints, which would not allow us to delete a row in one table whose primary key is referred to by at least one foreign key in other tables.
				...看看mysql管网文档
		3、I (Isolation 隔离性) 
			并发执行的事务彼此无法看到对方的中间状态
				附：即指并发的事务之间不会互相影响。如果一个事务要访问的数据正在被另外一个事务修改，只要另外一个事务未提交，它所访问的数据就不受未提交事务的影响。
			例：
				现在有个交易是从A账户转100元至B账户，在这个交易还未完成的情况下，如果此时B查询自己的账户，是看不到新增加的100元的。
			hcg:
				并发的事务之间不会互相影响。
				（所以这里就会可能发生丢失修改吗！但你可以提高事务访问级别，来避免这个问题，或事务中sql加排他锁来避免该问题？）
		4、D (Durability持久性) 
			事务完成后所做的改动都会被持久化，即使发生灾难性的失败。通过日志和同步备份可以在故障发生后重建数据。
		注：
			MySQL事务的四个特性中ACD三个特性是通过Redo Log（重做日志）和Undo Log 实现的，而 I（隔离性）是通过Lock（锁）来实现。
事务隔离级别：
	介绍：
		事务并发问题：
			./事务隔离级别.txt goto：事务并发问题
		事务隔离级别：
			./事务隔离级别.txt goto：事务隔离级别
			事务隔离级别用来解决事务并发导致的问题。
	？
		事务的隔离级别通过锁的机制来实现。
			为什么？
			应该是事务的部分功能是通过锁来实现，部分是通过日志等来实现吧。！
				事务具有ACID（原子性、一致性、隔离性和持久性），而锁只是用于解决隔离性的一种机制，锁只是属于事务隔离级别的一部分。
				那么锁到底实现了隔离级别的什么了？
		开启事务就自动加锁?
			并不是吧，我认为sql默认是无锁的（包括事务中sql）。
			例：
				会话1：
					begin;
					SELECT * FROM EGOV_RECEIVAL FOR UPDATE; //加排他锁
					// SELECT * FROM EGOV_RECEIVAL WHERE ID = 'W-0h3-SwQwQ9PFzZ' // 如果值被别的事务改，但当前事务还是读取到原来的值。如果不手动加锁会有丢失修改现象哦
					// UPDATE EGOV_RECEIVAL SET SOURCE_UNIT = '2' WHERE ID = 'W-0h3-SwQwQ9PFzZ'
				会话2
					BEGIN;
					SELECT * FROM EGOV_RECEIVAL // 什么锁都不加，所以既然别的事务有排他锁，也不影响这个语句读取数据
					SELECT * FROM EGOV_RECEIVAL  LOCK IN SHARE MODE; // 加共享锁
					SELECT * FROM EGOV_RECEIVAL FOR UPDATE; // 加排他锁
		
锁机制：
	锁机制介绍：
		./锁机制.txt
		锁机制作用：
			锁机制主要用来实现事务的隔离性?
			我认为锁机制是用来避免修改丢失功能的。
				例： ./事务隔离级别.txt goto：如何避免丢失修改
	附：
		1个提问：
			方法逻辑：先读取数据，数据存在则更新，不存在则新增。
			那么如果该方法并发被调用两次，都先判断到无数据，然后新增，该问题如何解决了。
			解决方式1：java锁
			解决方式2：乐观锁，数据库加个冗余字段字段值设为唯一，手动递增这样添加数据时就会报错。
		事务隔离级别和锁关系:
			事务隔离是靠锁机制来实现的。
			https://blog.csdn.net/zjj972326230/article/details/79003318
			那mysql事务具体是如何靠锁来实现的了？（有3个问号都是同一个问题）
事务的使用：
	spring中使用事务：	
		/spring/AOP/spring事务管理/
		https://github.com/yuyumyself/ssm-springBoot/tree/master/spring/AOP/spring%E4%BA%8B%E5%8A%A1%E7%AE%A1%E7%90%86
	MYSQL 事务处理：
		https://www.runoob.com/mysql/mysql-transaction.html
		1、用 BEGIN, ROLLBACK, COMMIT实现一事务操作：
			BEGIN 开始一个事务
			ROLLBACK 事务回滚
			COMMIT 事务确认
		附：
			1.
				第一个begin开启事务后，不提交，此时再一个begin，那么
				第二个begin，会默认提交第一个begin的结果，隐式提交。
			2. 事务控制语句：
				BEGIN 或 START TRANSACTION 显式地开启一个事务；
				COMMIT 也可以使用 COMMIT WORK，不过二者是等价的。COMMIT 会提交事务，并使已对数据库进行的所有修改成为永久性的；
				ROLLBACK 也可以使用 ROLLBACK WORK，不过二者是等价的。回滚会结束用户的事务，并撤销正在进行的所有未提交的修改；
				SAVEPOINT identifier，SAVEPOINT 允许在事务中创建一个保存点，一个事务中可以有多个 SAVEPOINT；
				RELEASE SAVEPOINT identifier 删除一个事务的保存点，当没有指定的保存点时，执行该语句会抛出一个异常；
				ROLLBACK TO identifier 把事务回滚到标记点；
				SET TRANSACTION 用来设置事务的隔离级别。InnoDB 存储引擎提供事务的隔离级别有READ UNCOMMITTED、READ COMMITTED、REPEATABLE READ 和 SERIALIZABLE。
			3.查看正在执行的事务：
				SELECT * FROM information_schema.innodb_trx;
	注：
		1.MySQL 的事务是自动提交的：
			可用 SET 来改变 MySQL 的自动提交模式:
				SET AUTOCOMMIT=0 #禁止自动提交 #即执行一条更新sql语句，并不会生效，要commit才会生效。
				SET AUTOCOMMIT=1 #开启自动提交 
				show variables like 'autocommit'; #查看自动提交状态
			附：
			一个sql语句本身算事务：
				MySQL 的事务默认是自动提交的。
				未显示开启事务的情况下，一个sql语句本身就是一个事务，执行一个sql就是提交一个事务了。
				附：
					https://bbs.csdn.net/topics/300178999
					SQL Server 以下列事务模式运行。
						自动提交事务 
						        每条单独的语句都是一个事务。
						显式事务 
						       每个事务均以 BEGIN TRANSACTION 语句显式开始，以 COMMIT 或 ROLLBACK 语句显式结束。
						隐式事务 
						       在前一个事务完成时新事务隐式启动，但每个事务仍以 COMMIT 或 ROLLBACK 语句显式完成。
						附：
							自动提交事务---数据库引擎的默认模式。每个单独的 Transact-SQL 语句都在其完成后提交。不必指定任何语句来控制事务。
							即一个sql就算一个事务。
							但是你也可以把事务的控制交给程序，这要通过显式事务来完成.
		
附：
	关系型数据库:
		关系型数据库事务都遵循ACID规则
	