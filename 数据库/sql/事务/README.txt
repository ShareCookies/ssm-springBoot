https://guobinhit.blog.csdn.net/article/details/61200815?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-12.control&dist_request_id=a3a2ba39-036d-4552-abd7-1eac3e380c28&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-12.control


事务介绍：
	事务（transaction）是指访问并可能更新数据库中数据项的一个程序执行单元(unit)。
	关系型数据库事务要满足以下四个特性：
        附：
            按照严格的标准，只有同时满足 ACID 特性才是事务，但是在各大数据库厂商的实现中，真正满足 ACID 的事务少之又少。
            例如，MySQL innodb “可重复读”的就不完美满足隔离性中的丢失修改。
            所以因此与其说 ACID 是事务必须满足的条件，不如说它们是衡量事务的四个维度。
		1、A (Atomicity 原子性) 
			原子性，是指一个事务是一个不可分割的工作单位。事务中各项操作，要么全做要么全不做，任何一项操作的失败都会导致整个事务的失败；
				即：事务成功的条件是事务里的所有操作都成功，只要有一个操作失败，整个事务就失败，需要回滚。
			例：
				比如银行转账，从A账户转100元至B账户，分为两个步骤：1）从A账户取100元；2）存入100元至B账户。这两步要么一起完成，要么一起不完成，如果只完成第一步，第二步失败，钱会莫名其妙少了100元。

			实现原理：
				./redo log、binlog、undo log.txt # undo log
		4、D (Durability持久性)
			定义：
				持久性，是指事务一旦提交，它对数据库的改变就应该是永久性的，接下来的其他操作或故障不应该对其有任何影响。
			实现原理：
				./redo log、binlog、undo log.txt # redo log
		3、I (Isolation 隔离性)
			介绍：
				与原子性、持久性侧重于研究事务本身不同，隔离性研究的是不同事务之间的相互影响。
				定义：
					并发执行的事务彼此无法看到对方的中间状态。
						即隔离性是指事务内部的操作与其他事务是隔离的，并发执行的各个事务之间不能互相干扰。
				附：
					严格的隔离性，对应了事务隔离级别中的Serializable（可串行化），但实际应用中出于性能方面的考虑很少会使用可串行化，一般为可重复读。
			隔离性的实现：
                隔离性追求的是并发情形下事务之间互不干扰。
                下面我们仅考虑最简单的读操作和写操作
					（不考虑带锁读等特殊操作，因为读默认无锁，cud默认排他锁，
					我认为如果手动加锁等就可归属于用户手动实现隔离性），
                那么隔离性的探讨，主要可以分为两个方面：
                    1. （一个事务）写操作对（另一个事务）读操作的影响：
						1. 那么并发情况下，读操作可能存在的三类问题：
							脏读、不可重复读和幻读
							详：
								./事务隔离级别.txt #事务并发问题
						附：解决方案
							1. 事务隔离级别
								为了避免上面出现的几种情况，标准SQL规范中定义了4个事务隔离级别，不同的隔离级别能避免不同的事务问题。
								详：
									./事务隔离级别.txt goto：事务隔离级别
								附：
									单机情况下mysql的RR隔离级别是不会有这3类问题的。
                    2. （一个事务）写操作对（另一个事务）写操作的影响：
                        1. 写事务并发情况下，可能存在的问题：
                            回滚丢失、覆盖丢失
                            详：
								./事务隔离级别.txt goto: 丢失修改（Lost To Modify）


                        2. 解决方案：
                            1. 回滚丢失：
								mysql的RR隔离级别是不会造成回滚丢失的。
								详：
									./事务隔离级别.txt goto: 可重复读下，避免回滚丢失的证明案例
                            2. 覆盖丢失：
								mysql可重复读情况下
									1. 直接sql中以字段值为基础值，可以避免覆盖丢失修改
									2. 但要是用户直接赋值，那就无法避免丢失修改了，需要用户手动避免丢失修改(乐观锁)
                                详：
									./事务隔离级别.txt goto: 可重复读下，覆盖丢失
                         

                    附：
					   锁机制：
							锁机制的基本原理可以概括为：
								事务在修改数据之前，需要先获得相应的锁；获得锁之后，事务才可以修改数据；
								该事务操作期间，这部分数据是锁定的，其他事务如果需要修改数据，需要等待当前事务提交后释放锁。
							详：
								./锁机制.txt
							
                        事务并发问题、事务隔离级别、锁的关系：
                            事务并发问题：
								描述了事务并发可能会导致的问题。
                            事务隔离级别：		
								一个sql标准，其规定了4个级别的事务并发标准，4个级别能避免不同的事务并发问题。
								具体实现由数据库厂商来。
                            锁：
								mysql通过锁、mvvc、undo来实现事务隔离级别。
                  
		2、C (Consistency 一致性)
		    定义：
                一致性，是指事务执行结束后，数据库的完整性约束没有被破坏。事务执行的前后都是合法的数据状态。
                数据库的完整性约束包括但不限于：
                    实体完整性（如行的主键存在且唯一）、列完整性（如字段的类型、大小、长度要符合要求）、外键约束、
                    用户自定义完整性（如转账前后，两个账户余额的和应该不变）。

            实现方式：
                可以说，一致性是事务追求的最终目标：前面提到的原子性、持久性和隔离性，都是为了保证数据库状态的一致性。
                实现一致性的措施包括：
                    保证原子性、持久性和隔离性，如果这些特性无法保证，事务的一致性也无法保证；
                    数据库本身提供保障。
                        例如不允许向整形列插入字符串值、字符串长度不能超过列的限制等；
                    应用层面进行保障。(除了数据库层面的保障，一致性的实现也需要应用层面进行保障。)
                        例如如果转账操作只扣除转账者的余额，而没有增加接收者的余额，无论数据库实现的多么完美，也无法保证状态的一致。
			附：
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

附：
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
			1.隐式提交
				第一个begin开启事务后，不提交，此时再一个begin，那么第二个begin，会默认提交第一个begin的结果。
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
		1.MySQL 的事务默认是自动提交的：(一个sql语句本身算事务)
		    MySQL 的 InnoDB 存储引擎，其默认是开启autocommit配置的，即自动提交事务。
            1. 在自动提交模式下，如果没有以start transaction显式地开始一个事务，那么每条 SQL 语句都会被当做一个事务执行提交操作。
                即：自动提交模式下，未显示开启事务的情况下，一个sql语句本身就是一个事务，执行一个sql就是提交一个事务了。
            2. 如果关闭了autocommit，则所有的 SQL 语句都在一个事务中，直到执行commit或rollback，该事务结束，并同时开始另外一个新的事务。

            附：
                可用 SET 来改变 MySQL 的自动提交模式:
                    SET AUTOCOMMIT=0 #禁止自动提交 #即执行一条更新sql语句，并不会生效，要commit才会生效。
                    SET AUTOCOMMIT=1 #开启自动提交
                    show variables like 'autocommit'; #查看自动提交状态
                    注：autocommit参数是针对连接的，在一个连接中修改了参数，不会对其他连接产生影响。
            附：
                除此之外，在 MySQL 中，还存在一些特殊的命令，如果在事务中执行了这些命令，则会强制执行commit命令提交事务，
                如 DDL 语句（create table/drop table/alter table）、lock tables语句等。不过，我们常用的select、insert、update和delete命令，都不会强制提交事务。

			附：废弃
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