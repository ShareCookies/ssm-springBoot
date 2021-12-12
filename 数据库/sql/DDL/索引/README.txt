https://dev.mysql.com/doc/refman/5.7/en/create-index.html
索引：
	介绍：
		附：
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
				索引会降低更新表的速度，当进行其他DML时，要更新索引文件。
				建立索引会创建占用磁盘空间的索引文件。

		mysql索引原理：	
			索引的存储结构：
				./聚簇与非聚簇索引.txt
			mysql单列索引原理：
				./mysql索引原理.txt
			mysql联合索引原理：
				./单列索引、联合索引.txt
索引优化：
	
索引的常见操作：
	创建索引：
		https://www.cnblogs.com/tommy-huang/p/4483684.html
		MYSQL语法：	
			CREATE [UNIQUE|FULLTEXT]  INDEX <索引名> ON <表名> （列名 [,...n](length)(sorting)）
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
				单列索引、联合索引：
					./单列索引、联合索引.txt
				length：
					可以指定索引长度
						比如指定100的长度在a列上,那么就是根据a列内容的前100长度的内容去建立索引
						例：
							a列有4个值 abc,efghijkl,aa.
							a列上建立长度3的索引,那么参与建立索引的数据 abc,efg,ijk,aa
				sorting：
					可以指定索引字段排序。默认升序
		例：
			CREATE UNIQUE INDEX sIndex ON Student(sname,lastName)
			附：
				使用ALTER 命令添加：
					ALTER TABLE tbl_name ADD PRIMARY KEY (column_list): 该语句添加一个主键，这意味着索引值必须是唯一的，且不能为NULL。
					ALTER TABLE tbl_name ADD UNIQUE index_name (column_list): 这条语句创建索引的值必须是唯一的（除了NULL外，NULL可能会出现多次）。
					ALTER TABLE tbl_name ADD INDEX index_name (column_list): 添加普通索引，索引值可出现多次。
					ALTER TABLE tbl_name ADD FULLTEXT index_name (column_list):该语句指定了索引为 FULLTEXT ，用于全文索引。
		
					
	删除索引：
		语法：
			Mysql: DROP INDEX <索引名> on <表名>
			SqlServer: drop index <表名>.<索引名>
	附：
		查看表上拥有的索引：
			show index from <表名>;