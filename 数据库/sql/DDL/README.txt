DDL:数据库定义语言
	用于操作对象和对象的属性，这种对象包括数据库本身，以及数据库对象，像：表、视图等等.
	”对象“包括对象及对象的属性，而且对象最小也比记录大个层次。
		以表举例：Create创建数据表，Alter可以更改该表的字段，Drop可以删除这个表，从这里我们可以看到，DDL所站的高度，他不会对具体的数据进行操作。
DDL的主要操作:
   Create语句：可以创建数据库和数据库的一些对象。
   Drop语句：可以删除数据表、索引、触发程序、条件约束以及数据表的权限等。
   Alter语句：修改数据表定义及属性。
DDL之库表操作常识：
	库：
		CREATE DATABASE  adv  - 建新库
		ALTER DATABASE - 修改数据库
	表：
		CREATE TABLE - 创建新表
		ALTER TABLE - 改变数据库表
			为表添加字段：
				ALTER TABLE tbl_adv_infoitem ADD  client_id  VARCHAR(16) NULL;
			约束：
				外键约束：
		DROP TABLE - 删除表
	字符集及校对规则
		https://www.cnblogs.com/geaozhang/p/6724393.html#MySQLyuzifuji
		字符集：
			指的是一种从二进制编码到某类字符符号的映射。
			MySQL每个数据库以及每张数据表都有自己的默认值，他们逐层继承。
			例：
				UTF8字符集

		校对规则：
			介绍：
				是在字符集内用于字符比较和排序的一套规则。
			校对规则命名约定：
				以相关的字符集名开始。
				中间包括一个语言名。
				并且以_ci（大小写不敏感）、_cs（大小写敏感）或_bin（二元）?结束。
				例：
					utf8_general_ci
			附：
				每个字符集有一个默认校对规则：
					utf8字符集默认校对规则是utf8_general_ci。
		？
			如果是二进制类型或二进制关键字那么字符集这些没用吧
索引:
	./索引/mysql索引.txt
存储过程:
	./mysql存储过程.txt
附：
	阿里mysql规范:
		https://zhuanlan.zhihu.com/p/88425702
		阿里巴巴Java开发.pdf goto：数据库

	字段长度：
		https://blog.csdn.net/weixin_41588181/article/details/83929611?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1
		https://zhidao.baidu.com/question/2121425850053132267.html?qbl=relate_question_0&word=mysql%B3%A4%B6%C8%D6%B8%B5%C4%CA%C7%CA%B2%C3%B4