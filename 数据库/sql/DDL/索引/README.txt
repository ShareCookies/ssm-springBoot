https://dev.mysql.com/doc/refman/5.7/en/create-index.html

索引：
	介绍：
		索引的简易理解：
			索引可以简单的理解为一张表，该表保存索引字段，并指向实体表的记录。
			数据查找过程：
				数据库管理系统，执行一个在Student表上根据指定的Sno，查找该学生信息的语句。
				没有索引：
					如果没有索引，则数据库管理系统需要从Student表的第一行开始，并逐行检索指定的Sno值。
				有索引：
					数据库管理系统发现查找条件中有索引项，
					然后在索引表查找Sno，找到sno后根据其对应的数据的存储位置，直接检索出需要的信息。
		索引优缺点:
		　	优点：索引可以极大的提高数据的查询速度。 
			缺点：
				建立索引会创建占用磁盘空间的索引文件。
				索引会降低更新表的速度，当进行其他DML时，会更新索引文件。

		索引的存储结构：
			介绍：
				MySQL索引使用的数据结构主要有BTree索引 和 哈希索引 。
				mysql默认BTree索引。
			哈希索引:
				底层的数据结构是哈希表.
				优缺点:
					hash索引，等值查询效率高
					不能排序,不能进行范围查询
			BTree索引:
				介绍：
					BTree按搜索关键字排序(就是用索引构成一个b树)，因此可以在组成搜索关键字的，任何字词条集合上进行高效搜索。
						例：
							对一个有A,B,C三个列组成的索引，可以在A以及A,B和A,B,C上对其进行高效搜索。
							goto：最左前缀原则
					MySQL的BTree索引都使用了索引来构成B+Tree，但两种主流存储引擎的BTree索引数据存储方式是不同的。
					    即使用索引构成一个B+树，但索引对应的实际数据存储方式不同。
                    InnoDB:
                        InnoDB索引的数据文件就存储在索引上，即叶节点上。
                        聚簇索引：
                            goto： 聚簇与非聚簇索引
                    MyIsam：
                        MyISAM索引文件和数据文件是分离的。
                        非聚簇索引：
                            goto： 聚簇与非聚簇索引

				附：
				聚簇与非聚簇索引：(数据存储方式)
					https://blog.51cto.com/2839840/2057806?utm_source=oschina-app
					注：
						MyISAM采用非聚集索引，InnoDB 采用聚簇索引。
					介绍：
						我们非常容易想象出一个只有单关键字组成的表如何使用B+树进行索引，只要将关键字存储到树的节点即可。
						当数据库一条记录里包含多个字段时，一棵B+树就只能存储主键，如果检索的是非主键字段，则主键索引失去作用，又变成顺序查找了。这时应该在第二个要检索的列上建立第二套索引,这个索引由独立的B+树来组织。
						?
							多字段，但又无主键了，如何组织了？
						有两种常见的方法可以解决多个B+树访问同一套表数据的问题，一种叫做聚簇索引（clustered index ），一种叫做非聚簇索引（secondary index）。
						附：
							这两个名字虽然都叫做索引，但这并不是一种单独的索引类型，而是一种数据存储方式。
						聚簇索引：
						    1. 对于聚簇索引存储来说，行数据和主键B+树存储在一起，行数据存储在主键B+树叶子节点上 。
						        即构成BTREE节点的关键字，是数据表的主键，树的叶节点保存了完整的数据记录(data域)。
                                ？
                                    那主键B树所有叶节点有都读取到缓存中吗，应该没有吧，不然不是太大了
						    2. 辅助键B+树叶子节点存储主键，主键和非主键B+树是两棵独立但有关联的树。
						        附：
						            因此辅助索引需要两次查询，先查询到主键，然后再通过主键查询到数据。
						            主键不应该过大，因为主键太大，其他索引也都会很大。
						                是指其他索引的叶节点会过大吧！
							附：
								因此 InnoDB 必须要有主键？
								通过主键索引效率很高。
								    在根据主索引搜索时，直接找到对应的叶节点数据即可；
                                ？
                                    也不建议使用非单调的字段作为主键！，这样会造成主索引频繁分裂。
							总结：
                                构成BTREE索引节点的关键字是数据表的主键，叶节点则是数据文件本身。
                                而其余的索引都作为辅助索引，辅助索引的叶节点(data域)存储则存储主键。
						非聚簇索引:
							对于非聚簇索引存储来说，主键B+树在叶子节点存储指向真正数据行的指针，而非主键。
							...
							主键索引和辅助索引则是完全是独立的树,各自都保存了指向数据的指针。
		mysql索引原理：
			./mysql索引原理.txt
索引优化：
	索引建设原则：(索引创建建议)
		1．为搜索条件、联合等操作的字段建立索引.
			1. 搜索条件: 
				为常作为查询条件的字段建立索引.
				附：
				    MySQL只对<，<=，=，>，>=，BETWEEN，IN，以及某些时候的LIKE才会使用索引。
				    原因：
				        《mysql索引原理》
			2. 联合：
			    对于两表连接的字段，应该建立索引。
			    附：
			        因为表关联原理是嵌套循环查询+where条件筛选
			        具体goto：../../DML/表连接/README.txt
			附：
                3. 排序：
                    如果经常在某表的一个字段进行Order By 则也可考虑创建索引。
                    具体goto：./排序与索引关系.txt
                    附：
                        如果想优化排序还是以explain实战为准
                4. 连接子句?
                分组？
		2.尽量使用短索引
			对字段创建索引时，如果可能应该指定一个前缀长度。
			如此可以减小索引树高度就可减少io次数，加快了查找速度对新增更新也有利处。
			例：
				如果有一个CHAR(255)的列，如果在前10个或20个字符内，多数值是惟一的，那么就不要对整个列进行索引。
				
				短索引不仅可以提高查询速度而且可以节省磁盘空间和I/O操作。
			原因：
				通过 ./mysql索引原理.txt 的分析，我们知道IO次数取决于b+数的高度h。
				假设当前数据表的数据为N，每个磁盘块的数据项的数量是m，则有h=㏒(m+1)N，？当数据量N一定的情况下，m越大，h越小；而m = 磁盘块的大小 / 数据项的大小，磁盘块的大小也就是一个数据页的大小，是固定的，如果数据项占的空间越小，数据项的数量越多，树的高度越低。这就是为什么每个数据项，即索引字段要尽量的小，比如int占4字节，要比bigint8字节少一半。这也是为什么b+树要求把真实的数据放到叶子节点而不是内层节点，一旦放到内层节点，磁盘块的数据项会大幅度下降，导致树增高。当数据项等于1时将会退化成线性表。

		3．离散型越高越好：
			因为离散度越高，通过索引最终确定的范围越小，最终扫面的行数也就越少。
			附：
				mysql中索引长度与区分度的选择：
					https://blog.csdn.net/heyuqing32/article/details/80966301
                ？
                    我很想知道比如0，1两种索引，应该是只对存储了2个指针位吧，那叶节点是如何存那么多行数据的？。那先找到第一个指针位数据块，这个数据块后面又跟了下一个指针位数据块吗？

		4. 适度设置索引列
			设置索引时要考虑设置合适的列，不要造成“过多的索引列”。
			原因：	
				因为每个索引需要额外的磁盘空间，并降低写更操作的性能。
				在修改表内容的时候，索引会进行更新，更有甚至需要重构，索引列越多，所花费的时间就会越长。所以只保持需要的索引有利于查询即可。
			附：
				可考虑组合索引替换掉部分单列索引：
					goto：组合索引:
	索引使用注意事项：（优化注意事项）
		and or：
			and：
				1. 用and会优先使用有索引的搜索条件。
				2. 用and只会使用一个索引，且mysql会自我选择使用效率最高的那个索引。
				例：
                    #主键ID 索引DOC_SEQUENCE
                    EXPLAIN SELECT * FROM EGOV_RECEIVAL WHERE id = 'XYCtqoSukpvSznD2' #type:const rows:1
                    EXPLAIN SELECT * FROM EGOV_RECEIVAL WHERE id = 'XYCtqoSukpvSznD2' AND DOC_SEQUENCE = '20190124'  #type:const rows:1
			or：
				1. 用or 那么只要or条件中有非索引的，那么还是全表检索
				2. or 能使用多个索引。
				    即各自走辅助索引并把结果混合起来。
                例：
                    #主键ID 索引DOC_SEQUENCE
                    or有一个非索引：
                        EXPLAIN SELECT * FROM EGOV_RECEIVAL WHERE id = 'XYCtqoSukpvSznD2' OR SUBJECT = '索引性能测试' #type:ALL rows:613
                    or两个均索引：
                        EXPLAIN SELECT * FROM EGOV_RECEIVAL WHERE id = 'XYCtqoSukpvSznD2' OR DOC_SEQUENCE = '20190124' #type:index_merge rows:2
		LIKE使用索引:
		  	使用LIKE时要注意，只有使用字母开头才会应用到索引。
			like在以通配符%和_开头作查询时，MySQL不会使用索引。
				因为goto：mysql索引原理
			例:
				如下句会使用索引：
			  　　	SELECT * FROM mytable WHERE username like'admin%'
			 	而下句就不会使用：
			 　　 	SELECT * FROM mytable WHEREt Name like'%admin'
				！：
					具体b树是怎么对比的了，是先比a，在比d这些？
		不要在列上进行运算:
			在列上进行运算,将在每个行上进行运算，这将导致索引失效而进行全表扫描，
			例：
                select * from users where YEAR(adddate)<2007;
                因此我们可以改成
              　　 select * from users where adddate<'2007-01-01';
			！
				NOW()等函数了
		慎用NOT IN、exits、NOT exits：
			这3个应该都是嵌套(索引)循环查询，即把外面的所有数据拿去里面走循环。
			所以这3个仅适用与外小内大。
			5.7后in则没有这种限制，应该是mysql优化了。
			具体goto：
				/数据库/sql/DML/SQL中in、exist效率.txt
        附
		含有NULL值的列：
		    mysql5.7：？
                索引不会包含有NULL值的列
                    即只要列中包含有NULL值都将不会被包含在索引中。
                例：
                    3.where status!=4 不会列出（比较）为null的
                    1.判断某字段为空	is null
                    2.某字段不为空	is not null
                附：
                    ？
                        复合索引中只要有一列含有 NULL值，那么这一列对于此复合索引就是无效的。
                    ！
                        所以我们在数据库设计时不要让字段的默认值为NULL。
                ?
                    is null为什么可以走索引了：
                        select * from WRONG_IMG_copy1 WHERE CREATOR_ID is null
                        解析：1	SIMPLE	WRONG_IMG_copy1		ref	CREATOR_ID	CREATOR_ID	99	const	1	100.00	Using index condition
            mysql5.8：
                mysql应该是没索引不包含null的限制了。
                例：
                    where status!=4 也能找出为null的行。
                    where status is null 也会走索引。
        <>:
			看情况使用不等于，通常不等于的不走索引而是在server层实现过滤。
			注：
			    https://www.v2ex.com/t/425142
			    只有一个字段的话且使用了不等于，数据库优化器会判断候选结果集是否超出了全部结果的 5  %（估计），如果超过，那么就全表扫描。
 				附：所以像> < in 这种操作，走不走索引要看本身可能的结果数量。
 			例：
 			    场景：
                    CREATE_TIME	CREATE_TIME	NORMAL	0	A	115			0
                    CREATOR_ID	CREATOR_ID	NORMAL	0	A	4			0
 			    select * from WRONG_IMG_copy1 WHERE CREATE_TIME > '2021-01-03' AND CREATOR_ID <> 'hx'
 			    解析：
 			        1	SIMPLE	WRONG_IMG_copy1		range	CREATE_TIME,CREATOR_ID	CREATE_TIME	6		31	34.43	Using index condition; Using where
 			        此处解析结果中有Using where，则说明表示存储引擎返回的记录并不是所有的都满足查询条件,需要在server层进行过滤。
 			        所以不等于其实是在server层过滤。
 			        即CREATE_TIME走索引，但CREATOR_ID不走而是把数据在server层在进行过滤。
索引的常见操作：
	创建索引：
		https://www.cnblogs.com/tommy-huang/p/4483684.html
		MYSQL语法：	
			CREATE [UNIQUE|FULLTEXT]  INDEX <索引名> ON <表名> （列名 [,...n](length)）
				索引类型：
					NORMAL 普通索引:默认
						
					UNIQUE 唯一索引：
						表示要创建的索引是唯一索引	
						不允许重复的索引，如果该字段信息保证不会重复例如身份证号用作索引时，可设置为unique
					FULLTEXT：？
						表示 全文搜索的索引。 FULLTEXT 用于搜索很长一篇文章的时候，效果最好。
						用在比较短的文本，如果就一两行字的，普通的 INDEX 也可以。
						注：
							旧版的MySQL的全文索引只能用在MyISAM表格的char、varchar和text的字段上。！
					SPATIAL？
					注：
						索引的类别应由索引对应字段内容特性来决定。
				length：
					可以指定索引长度
						比如指定100的长度在a列上,那么就是根据a列内容的前100长度的内容去建立索引
						例：
							a列有4个值 abc,efghijkl,aa.
							a列上建立长度3的索引,那么参与建立索引的数据 abc,efg,ijk,aa

		例：
			CREATE UNIQUE INDEX sIndex ON Student(sname,lastName)
			附：
				使用ALTER 命令添加：
					ALTER TABLE tbl_name ADD PRIMARY KEY (column_list): 该语句添加一个主键，这意味着索引值必须是唯一的，且不能为NULL。
					ALTER TABLE tbl_name ADD UNIQUE index_name (column_list): 这条语句创建索引的值必须是唯一的（除了NULL外，NULL可能会出现多次）。
					ALTER TABLE tbl_name ADD INDEX index_name (column_list): 添加普通索引，索引值可出现多次。
					ALTER TABLE tbl_name ADD FULLTEXT index_name (column_list):该语句指定了索引为 FULLTEXT ，用于全文索引。
		单列索引:
			即一个索引只包含单个列.
			附：
				一个表可以有多个单列索引：
					附：多个单列索引，并不是组合索引，mysql会根据情况，决定应用那些索引。
					例：
						场景：
							3个字段：nameno, city, age、
							如果分别在 nameno 建立的是唯一索引，city，age上建立普通单列索引，此时该表有3个单列索引。
						只应用了一个索引：
							使用了类似以下的查询语句：
							select * FROM UMS_LOGIN_LOG WHERE UNIT_NAME LIKE '%吕' and nameno = '1300';
							使用explain发现此时这里就只应用了一个索引nameno。
							虽然有三个索引，但MySQL内部分析后觉的只应用一个索引就能达到目的。
						查询时使用了多个单列索引，最后取交集或者并集，再去主索引查：
							select * FROM UMS_LOGIN_LOG WHERE city = 'beijing' or age = '13';
							使用explain发现为type为index_merge,索引也应用了多个。
                辅助索引什么时候用：
                    解析type为index_merge即应用了多个索引。
                    1. 有or时会使用辅助索引来进行查找
                    例：
                        上面案例中:查询时使用了多个单列索引.
		组合索引:
			https://dev.mysql.com/doc/refman/5.7/en/multiple-column-indexes.html
			一个索引包含多个列。

			介绍：
				当b+树的数据项是复合的数据结构，比如(name,age,sex)的时候，b+数是按照从左到右的顺序来建立搜索树的。
				因此组合索引被使用时以最左边的索引列为起点任何连续的索引都能匹配上，直到遇到范围查询(>、<、between、like)就会停止匹配。
				附：
					优化器会自动调整顺序
						如果建立(a,b,c,d)的索引则都可以用到，a,b,c,d的顺序可以任意，因为优化器会自动调整顺序。
					什么时候要用组合索引了：
						看是否会应用到多字段查询
						索引越少则更新成本越低
					同时存在联合索引和单列索引（字段有重复的），这个时候查询mysql会怎么用索引呢。
						这个涉及到mysql本身的查询优化器策略了，当一个表有多条索引可走时, Mysql 根据查询语句的成本来选择走哪条索引；

			例：
				建立组合索引
					ALTER TABLE mytable ADD INDEX name_city_age (name(10),city,age);
				能应用到该组合索引的搜索条件为：
					1. name =、范围查询
						2. 只会应用到name：
							name = ，age = 、范围
					3. name =，city = 、范围
					4. name =，city = ，age  = 、范围
				    附：排序也算这里头

			原理：（组合索引怎么构成、如何查找）
				https://www.cnblogs.com/lanqi/p/10282279.html
				索引的底层是一颗B+树，那么联合索引当然还是一颗B+树，只不过联合索引的健值数量不是一个，而是多个。
				但一颗B+树只能根据一个值来构建，因此数据库依据联合索引最左的字段来构建B+树。
				    注：这里b树构成并不是只有最左字段，而是以最左字段+第二字段+...值一起构成b树。
				    只是最左字段是B树节点排序的依据，因此最左字段就是查询的关键，因为只有它匹配下，才能应用后面的字段。
				例：
					创建一个（a,b)的联合索引，那么它的索引树是这样的:
						./联合索引B树.png

					1. a b 组合成 BTree的关键字，以a为核心构成了一个b树(即以a值作为节点划分的对比参照物)。

						例：
							可以看到 a的值是有顺序的 1，1，2，2，3，3，而b的值是没有顺序的1，2，1，4，1，2。
							因为a是节点划分参照物，节点上a小于1的都在左边，大于1的都在右边。
						附：查找规则1：
							如果跳过a，直接查 b = 2 这种查询条件就没有办法利用索引，因为不知道b为2的节点是在，左磁盘还是右磁盘。
					2. a值相等的情况下，b值又是按顺序排列的，这种顺序是相对的(即在a值定值(a=1)的节点上，b是有顺序的)。
						例：
							a = 1 and b = 2 a,b字段都可以使用索引，因为在a值确定的情况下b是相对有序的。
						附：查找规则2：
							根据这个特性，我们可以得出最左匹配原则(索引的最左匹配特性)。
								即遇上范围查询就会停止匹配，剩下的字段都无法使用索引。
								例：
									而a>1and b=2，a字段可以匹配上索引，但b值不可以，因为a的值是一个范围，在这个范围中b是无序的。
									此处a找完后，b因为无序所以后续节点只能遍历，b其实就没用上索引，所以为什么叫遇上范围查询就会停止。
							a、c了：
								辅助关键字的顺序是相对与前一个辅助关键字，即如果有c，那么b值定值(a=1 b=2)情况下，c是有顺序的。！
								例：
									如果是a，b，c组合索引。那我 a = 1 and c = 2
									答：错
										https://blog.csdn.net/sinat_41917109/article/details/88944290
										select * from table_name where a = '1' and c = '3'
										如果不连续时，只用到了a列的索引，b列和c列都没有用到
						附：
							为什么要a固定下b有序了：
								因为就是这么设计的，如果不这么设计，那么就只能应用道a字段了，b就用不上了，那么组合索引就失去了它的意义。
					附：查找规则总结：
						ALTER TABLE mytable ADD INDEX name_city_age (name(10),city,age);
						1. 当数据为(张三，20,F)b+树会优先比较name来确定下一步的所搜方向，如果name相同再依次比较age和sex，最后得到检索的数据；
						2. 当数据为(张三，>20,F)b+树会优先比较name来确定下一步的所搜方向，如果name相同再依次比较age，但age因为是范围查询，导致后续数据F是无规律的，后续节点就遍历了；
                    	3. 但当(20,F)这样的没有name的数据来的时候，b+树就不知道下一步该查哪个节点，因为建立搜索树的时候name就是第一个比较因子，必须要先根据name来搜索才能知道下一步去哪里查询。
                    	4. 比如当(张三,F)这样的数据来检索时，b+树可以用name来指定搜索方向，但下一个字段age的缺失，所以只能把名字等于张三的数据都找到，然后再遍历所有张三数据尝试匹配性别是F的数据了，即索引的最左匹配特性。
					
	删除索引：
		语法：
			Mysql: DROP INDEX <索引名> on <表名>
			SqlServer: drop index <表名>.<索引名>
	附：
		查看表上拥有的索引：
			show index from <表名>;