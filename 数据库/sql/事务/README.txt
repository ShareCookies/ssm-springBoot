https://guobinhit.blog.csdn.net/article/details/61200815?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-12.control&dist_request_id=a3a2ba39-036d-4552-abd7-1eac3e380c28&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-12.control


事务介绍：
	事务（transaction）是指访问并可能更新数据库中数据项的一个程序执行单元(unit)。
	关系型数据库事务要满足以下四个特性：
        附：
            按照严格的标准，只有同时满足 ACID 特性才是事务，但是在各大数据库厂商的实现中，真正满足 ACID 的事务少之又少。
            例如，MySQL innodb “可重复读”的就不满足隔离性中的丢失修改，需要用户手动介入来实现。
            所以因此与其说 ACID 是事务必须满足的条件，不如说它们是衡量事务的四个维度。
		1、A (Atomicity 原子性) 
			原子性，是指一个事务是一个不可分割的工作单位。事务中各项操作，要么全做要么全不做，任何一项操作的失败都会导致整个事务的失败；
				附：事务成功的条件是事务里的所有操作都成功，只要有一个操作失败，整个事务就失败，需要回滚。
			例：
				比如银行转账，从A账户转100元至B账户，分为两个步骤：1）从A账户取100元；2）存入100元至B账户。这两步要么一起完成，要么一起不完成，如果只完成第一步，第二步失败，钱会莫名其妙少了100元。

			实现原理：undo log
				InnoDB 存储引擎提供了两种事务日志：
					redo log（重做日志）和undo log（回滚日志）。
					其中，redo log用于保证事务持久性；undo log则是事务原子性和隔离性实现的基础。

				实现原子性的关键，是当事务回滚时能够撤销所有已经成功执行的 SQL 语句。
				InnoDB 实现回滚，靠的是undo log：
					介绍：
						当事务对数据库进行修改时，InnoDB 会生成对应的undo log；
						如果事务执行失败或调用了rollback，导致事务需要回滚，便可以利用undo log中的信息将数据回滚到修改之前的样子。
						？
							undo log具体记录了什么：
				                ...
				                cud类型、修改前数据、修改后数据
					实现思路：
						undo log属于逻辑日志，它记录的是 SQL 执行的相关信息。当发生回滚时，InnoDB 会根据undo log的内容做与之前相反的工作：
							对于每个insert，回滚时会执行delete；
							对于每个delete，回滚时会执行insert；
							对于每个update，回滚时会执行一个相反的update，把数据改回去。
						例：
							以update操作为例：
							当事务执行update时，其生成的undo log中会包含被修改行的主键（以便知道修改了哪些行）、修改了哪些列、这些列在修改前后的值等信息，回滚时便可以使用这些信息将数据还原到update之前的状态。
		4、D (Durability持久性)
			定义：
				持久性，是指事务一旦提交，它对数据库的改变就应该是永久性的，接下来的其他操作或故障不应该对其有任何影响。
			实现原理：redo log
				下面先聊一下redo log存在的背景：

					InnoDB 作为 MySQL 的存储引擎，数据是存放在磁盘中的，但如果每次读写数据都需要磁盘 IO，效率会很低。
					为此，InnoDB 提供了缓存（Buffer Pool），Buffer Pool中包含了磁盘中部分数据页的映射，作为访问数据库的缓冲：
					当从数据库读取数据时，会首先从Buffer Pool中读取，如果Buffer Pool中没有，则从磁盘读取后放入Buffer Pool；
					当向数据库写入数据时，会首先写入Buffer Pool，Buffer Pool中修改的数据会定期刷新到磁盘中，这一过程称为“刷脏”。

					Buffer Pool的使用大大提高了读写数据的效率，但是也带了新的问题：如果 MySQL 宕机，而此时Buffer Pool中修改的数据还没有刷新到磁盘，就会导致数据的丢失，事务的持久性无法保证。于是，redo log被引入来解决这个问题。
				redo log实现持久性：
					当数据修改时，除了修改Buffer Pool中的数据，还会在redo log记录这次操作；
						当事务提交时，会调用fsync接口对redo log进行刷盘。
						fsync是个同步接口，只有改接口执行完才提交事务。
					如果 MySQL 宕机，重启时可以读取redo log中的数据，对数据库进行恢复。
					附：
						！
							redo log采用的是 WAL（Write-ahead logging，预写式日志），所有修改先写入日志，再更新到Buffer Pool，保证了数据不会因 MySQL 宕机而丢失，从而满足了持久性要求。
				附：
					既然redo log也需要在事务提交时将日志写入磁盘，为什么它比直接将Buffer Pool中修改的数据写入磁盘（即刷脏）要快：
						主要有以下两方面的原因：

						1. 刷脏是随机 IO，因为每次修改的数据位置随机，但写redo log是追加操作，属于顺序 IO ？。
						2. 刷脏是以数据页（Page）为单位的，MySQL 默认页大小是 16 KB，一个Page上一个小修改都要整页写入；而redo log中只包含真正需要写入的部分？，无效 IO 大大减少。
					binlog（二进制日志）：
						我们知道，在 MySQL 中还存在binlog（二进制日志）也可以记录写操作并用于数据的恢复，但二者是有着根本的不同的：
							作用不同：
								redo log是用于crash recovery的，保证 MySQL 宕机也不会影响持久性；
								binlog是用于point-in-time recovery的，保证服务器可以基于时间点恢复数据，此外binlog还用于主从复制。
							层次不同：
								redo log是 InnoDB 存储引擎实现的;
								而binlog是 MySQL 的服务器层实现的，同时支持 InnoDB 和其他存储引擎。
							内容不同：redo log是物理日志，内容基于磁盘的Page；binlog的内容是二进制的，根据binlog_format参数的不同，可能基于 SQL 语句、基于数据本身或者二者的混合。
							写入时机不同：
								binlog在事务提交时写入；
								redo log的写入时机相对多元：
									前面曾提到当事务提交时会调用fsync对redo log进行刷盘;
									redolog写入策略：
										这是默认情况下的策略，修改innodb_flush_log_at_trx_commit参数可以改变该策略，但事务的持久性将无法保证。
										除了事务提交时，还有其他刷盘时机，如master thread每秒刷盘一次redo log等，这样的好处是不一定要等到commit时刷盘，commit速度大大加快。
					redolog是自动执行的吗：
						innodb的恢复行为：
							在启动innodb的时候，不管上次是正常关闭还是异常关闭，总是会进行恢复操作。

							因为redo log记录的是数据页的物理变化，因此恢复的时候速度比逻辑日志(如二进制日志)要快很多。而且，innodb自身也做了一定程度的优化，让恢复速度变得更快。

							重启innodb时，checkpoint表示已经完整刷到磁盘上data page上的LSN，因此恢复时仅需要恢复从checkpoint开始的日志部分。
								例如，当数据库在上一次checkpoint的LSN为10000时宕机，且事务是已经提交过的状态。启动数据库时会检查磁盘中数据页的LSN，如果数据页的LSN小于日志中的LSN，则会从检查点开始恢复。

								还有一种情况，在宕机前正处于checkpoint的刷盘过程，且数据页的刷盘进度超过了日志页的刷盘进度。这时候一宕机，数据页中记录的LSN就会大于日志页中的LSN，在重启的恢复过程中会检查到这一情况，这时超出日志进度的部分将不会重做，因为这本身就表示已经做过的事情，无需再重做。

						？
							binlog二进制日志应该不会被自动执行吧

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
                下面我们仅考虑最简单的读操作和写操作（不考虑带锁读等特殊操作，因为读默认无锁，cud默认排他锁，我认为如果手动加锁等就可归属于用户手动实现隔离性），
                那么隔离性的探讨，主要可以分为两个方面：
                    1. （一个事务）写操作对（另一个事务）读操作的影响：
                            1. 那么并发情况下，读操作可能存在的三类问题：
                                脏读、不可重复读和幻读
                                ./事务隔离级别.txt goto：事务并发问题
                            附：
                                 事务隔离级别
                                    为了避免上面出现的几种情况，标准SQL规范中定义了4个事务隔离级别，不同的隔离级别能避免不同的事务问题。
                                    ./事务隔离级别.txt goto：事务隔离级别
                            2. mysql是这么解决 脏读、不可重复读和幻读的：
                                ./事务隔离级别.txt goto：MySQL RR隔离级别

                    2. （一个事务）写操作对（另一个事务）写操作的影响：
                        1. 写事务并发情况下，可能存在的问题：
                            回滚丢失、覆盖丢失
                            ./事务隔离级别.txt goto: 丢失修改（Lost To Modify）


                        2. mysql 的解决方案：
                            1. 回滚丢失：
                                mysql的写操作默认都是排他锁的，所以一般情况是不会造成回滚丢失的。
                            2. 覆盖丢失：
                                有以下三种解决方案：
                                    1. 提高隔离级别。
                                        可以尝试提高某类型事务的隔离级别。
                                    2. 为读加排他锁。
                                    3. 使用乐观锁思想，让我们手动来解决覆盖丢失问题.
                                        ./事务隔离级别.txt goto：使用乐观锁
                            锁机制：
                                ./锁机制.txt
                                锁机制的基本原理可以概括为：
                                    事务在修改数据之前，需要先获得相应的锁；获得锁之后，事务便可以修改数据；
                                    该事务操作期间，这部分数据是锁定的，其他事务如果需要修改数据，需要等待当前事务提交或回滚后释放锁。

                    附：
                        事务并发问题、事务隔离级别、锁的关系：
                            事务并发问题：描述了事务并发可能会导致的问题。
                            事务隔离级别：一个sql标准，定义了4个级别的事务并发标准，4个级别能避免不同的事务并发问题。具体实现由数据库厂商来。
                            锁：mysql通过锁、mvvc、undo来实现事务隔离级别。
                    附：
                        1个提问：
                            方法逻辑：先读取数据，数据存在则更新，不存在则新增。
                            那么如果该方法并发被调用两次，都先判断到无数据，然后新增，该问题如何解决了。
                            解决方式1：java锁
                            解决方式2：乐观锁，数据库加个冗余字段字段值设为唯一，手动递增这样添加数据时就会报错。
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