DML:数据库操作语言
	CURD：代表创建（Create）、更新（Update）、读取（Retrieve）和删除（Delete）操作。
	增加:
		例：
		INSERT INTO user( name, password, realname) VALUES ('1','1','1')
		注：括号不能省略
	删除:
		例：
		DELETE FROM Websites WHERE name='百度' AND country='CN';
		多列删除：
			delete from 表 where id in ('37','38')
	修改:
		例：
		UPDATE user SET password='admin'  where 'password'=11	--修改指定数据
		注：
			UPDATE user SET password=11 
				此时省略了 WHERE， 则默认为WHERE 1，那么会将password列的所有值设为11
		附：
			旧值是新值的一部分
				https://blog.csdn.net/u010865136/article/details/77337800
		附：
			drop：drop table 表名
				 删除内容和定义，并释放空间。执行drop语句，将使此表的结构一起删除。
			truncate：truncate table 表名
				清空表中的数据。也就是保留表的数据结构
			delete语句是数据库操作语言(dml)，truncate、drop 是数据库定义语言(ddl)。
	查询：
		例：
			select * from fund;
			简写:select rec.name "rowCode1" from fund rec;
			
	查询常用关键字:
		as 取别名，union 合并结果集，distinct：选取不重复的行
		表连接
		子查询，exists 子查询检查,IN 列查询,BETWEEN 列范围 ，
		比较符运算符，逻辑运算符，CASE WHEN :(条件语句，类似if else)，LIKE 模糊查询
		ORDER BY 排序，limit 返回数限制，group by:分组
		解释：
			./sql关键字.txt


			
