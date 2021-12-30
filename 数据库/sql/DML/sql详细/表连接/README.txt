临时表：
	介绍：将查询结果作为另一个查询语句的来源
	例：
		select * from(select * from test)  a
	详细：explain.txt#using temporary
表连接:
	介绍：
		表连接有内连接、外连接、笛卡儿积连接
		内连接：是保证两个表中所有的行都要满足连接条件。
		外连接：某些不满条件的列也会显示出来。
			分左连接、右连接、全连接三种。
		笛卡儿积连接：
			具体看goto： 笛卡尔积连接
	inner join：内连接（简写 join...on）
		介绍：
			内连接(join...on)，返回左右表均有的值。
		例：
			写法1(inner join)：select * from client as c inner join financial_account as f on c.idcard = f.idcard
			写法2(join)：select * from client  c join financial_account  f on c.idcard = f.idcard
		注：
			内连接可以不加on，结果就是笛卡儿乘积。
			具体看goto： 笛卡尔积连接
	left outer join：左连接（left join）
		介绍：
		    简写left join。
		    左连接，左表数据全部显示，且左表有的右表没有的则显示为null。
		    注：
		        左表全展示，并不是指左连接结果数量一定与左表相等，而是左连接结果数量可能大等于左表。
		        因为左表一条记录在右表可能有2条对应的连接记录，那么左连接结果集中就有有两条记录。
		        注：
		            不只左连接，所有的连接都有这个逻辑。
		例：
		    ./左连接/
	right outer join：右连接（right join）
		介绍：
			简写right join。
			右连接，右表数据全部显示，且右表有的左表没有的则显示为null。
		例：
	full out join：全连接（full join）
		介绍：
			简写full join 。
			全连接，返回两表所有数据未匹配项用null代替。
				即：左连接结果与右连接结果的合并。
		注：
			MySQL中不支持 FULL OUTER JOIN，但我们可以用union模拟。
			mysql模拟实现全连接：
				SELECT * FROM t1
				LEFT JOIN t2 ON t1.id = t2.id
				UNION
				SELECT * FROM t1
				RIGHT JOIN t2 ON t1.id = t2.id
			上面的 SQL 语句还可以加上where条件从句，对记录进行筛选
	cross join：（笛卡尔积连接、交叉连接）
		介绍：
			表 A（共有 n 条记录），表 B （共有 m 条记录），两表连接后，会产生一张包含 n x m 条记录的新表。
			附：
				笛卡儿乘积：第一个表中的每一行被联合到第二个表中的每一行
			附：
				通常用于表 A 和表 B 不存在关联字段
		例：
			select * from client  c cross join financial_account as f
		注：
			cross join 和 逗号连接  和 INNER JOIN 区别：
			CROSS JOIN 与 INNER JOIN：
				MySQL中，CROSS JOIN与INNER JOIN本质上是一样的。
				它们只是语法上有点区别，两者是可以互相等价替换。
				
				附：
					在标准SQL中，两者是不等同的。？
			逗号连接 与 inner join：
				INNER JOIN和 逗号连接 本质上是一样的。
				它们只是语法上有点区别，两者是可以互相等价替换。
					附：
					维基百科
						SQL 定义了两种不同语法方式表示"连接"：
							首先是"显式连接符号"：
								它显式地使用关键字 JOIN
							其次是"隐式连接符号"：
								它使用所谓的"隐式连接符号"。即表之间用逗号分隔。
						附：
							SQL 89标准只支持内部连接与交叉连接，因此只有隐式连接这种表达方式；SQL 92标准增加了对外部连接的支持，这才有了 JOIN 表达式。
				逗号连接语法：
					SELECT * FROM 逗号连接 WHERE 条件

					附:
						逗号连接，只能用where设置连接条件，无法使用on
			总结：
				CROSS JOIN 与 INNER JOIN 与 逗号连接 其实就是同一个东西，只是语法上略有区别
				例：
					以下语句效果都一样。
					且explain解析也都是一样的，没有区别。
					附：去掉连接条件结果都是笛卡儿积。
					逗号连接：
						select * from `user` , WRONG_IMG where user.id = WRONG_IMG.CREATOR_ID
					inner join：
						select * from `user` inner JOIN WRONG_IMG on user.id = WRONG_IMG.CREATOR_ID
						select * from `user` inner JOIN WRONG_IMG where user.id = WRONG_IMG.CREATOR_ID
					cross JOIN：
						select * from `user` cross JOIN WRONG_IMG on user.id = WRONG_IMG.CREATOR_ID
						select * from `user` cross JOIN WRONG_IMG where user.id = WRONG_IMG.CREATOR_ID
				
	附：
		部分表连接案例：
			自连接:
				样例:
					select a.id,b.id,b.pid,b.name
					from test as a,test as b
					where a.id=b.pid
			多表连接：
				写法1：select * from student inner join teacher on student.id=teacher.sid inner join project on student.id=project.sid;
				写法2：select * from student,teacher,project where student.id=teacher.sid and student.id=project.sid;

	附：?
		USING
			...

		NATURAL连接：
			...
		非等连接：
			介绍：使用等值以外的条件来匹配左、右两个表中的行 select A.c1,B.c2 from A join B on A.c3 != B.c3;
mysql表连接原理：
	mysql如何执行关联查询.txt
表连接优化事项：
	1. 尽量用有索引的where来优化驱动表的查询
	2. 尽量要有关联条件，关联条件尽量有索引
	原因：
		因为where条件筛选使驱动表更小，然后嵌套次数就会更少。
		嵌套循环查询时，通过索引查询才能提高效率。
	优化准则：
		关联sql优化中，重要的点就是要用*小表*驱动大表*索引*

