--设置密码
set password for root@localhost = password(''); 

select p.id,p.user_id,p.title,p.price,p.edittext,p.date,u.name,u.portrait from postshare p join user u   on p.user_id=u.id where p.id=6

--创建表:
--留言
Create table message_board (
id int(10) primary key AUTO_INCREMENT,
leaver_id int(7) not null COMMENT'留言者id' ,
leaver_name varchar(12) not null,
leaver_portrait varchar(50) not null,
message varchar(256) COMMENT  '留言',
time datetime not null,
son_id int(10) comment '子编号',
son_name varchar(15) COMMENT '子名字',
son_message varchar(256) COMMENT  '子留言',
article_id int(9) comment '研究生发表的文章id'
)



--用户
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(7)  NOT NULL AUTO_INCREMENT,
  `name` varchar(15) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `portrait` varchar(50) NOT NULL DEFAULT '58xuejia.cn/fei/common/default_portrait.jpg' COMMENT '肖像',
  `phone` bigint(11) DEFAULT NULL unique,
  `qq` bigint(15) DEFAULT NULL unique,
  `wechat` varchar(15) DEFAULT NULL  unique COMMENT '微信',
  `verification` int(5) DEFAULT NULL COMMENT '验证码',
  `isverify` int(1) NOT NULL DEFAULT '0' COMMENT '是否激活手机0为否',
  `postgraduate` int(1)  NOT NULL DEFAULT '0' COMMENT '是否注册为研究生0为否',
  `school` varchar(12)  DEFAULT NULL,
  `department` varchar(10)  DEFAULT NULL COMMENT '系别',
  `major` varchar(10)  DEFAULT NULL COMMENT '专业',
  `qqopenid` varchar(100) DEFAULT NULL unique,
  `wechatopenid` varchar(100) DEFAULT NULL unique,
  PRIMARY KEY (`id`)
--  UNIQUE KEY `phone` (`phone`,`qq`,`wechat`),这么写无效为啥
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--文章发表
--为安全可以用外键
CREATE TABLE IF NOT EXISTS `article` (
  `id` int(9) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(6) NOT NULL, 
  `title` varchar(15) CHARACTER SET utf8 NOT NULL unique,
  `cover` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '暂无' COMMENT '封面',
  `service` text CHARACTER SET utf8 NOT NULL COMMENT '研究生提供服务介绍',
  `school_intro` text CHARACTER SET utf8 NOT NULL COMMENT '研究生所在学校介绍',
  `person_intro` text CHARACTER SET utf8 NOT NULL COMMENT '研究生个人介绍',
  `publish_date` date   NOT NULL  COMMENT '文章发表时间',
  `taobao_link` varchar(200) CHARACTER SET utf8 NOT NULL  COMMENT '淘宝链接',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '是否完成审核',
  `province` varchar(5) CHARACTER SET utf8 NOT NULL,
  `city` varchar(5) CHARACTER SET utf8 NOT NULL,
  `school` varchar(10) CHARACTER SET utf8 NOT NULL,
  `major` varchar(15) CHARACTER SET utf8 NOT NULL,
  `major_direction` varchar(15) CHARACTER SET utf8 NOT NULL COMMENT '专业方向',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `answer_question_time` (
  `id` int(9) unsigned NOT NULL AUTO_INCREMENT,
  `postgraduate_id` int(6) NOT NULL, 
  `student_id` int(6) NOT NULL, 
  `time` int(4) NOT NULL default 8 comment '剩余时间', 
  PRIMARY KEY (`id`),
  UNIQUE  INDEX  duplicate(`postgraduate_id`,`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
-- 不能用于mysql CREATE unique NONCLUSTERED INDEX duiplicate ON answer_question_time(postgraduate_id,student_id)

--数据插入：
INSERT INTO  `activityof1_1`.`find_partner` (
`id` ,
`qq` ,
`content` ,
`time` ,
`state`
)
VALUES (
NULL ,  '11',  '找福州大学美女',  '',  '0'
), (
NULL ,  '22',  '找福州大学美女',  '',  '0'
);
--创建零临时表：
Create TEMPORARY table test (Select * from find_partner);
--零临时表查询，临时表在断开与mysql的链接后就会消失，在那个库里建的临时表就在那个库。用phpMyadmin不能用临时表。
Select * from test
--两表查询结果合并输出UNION
SELECT id,content FROM find_partner UNION SELECT id,content FROM wait_partner ORDER BY id DESC LIMIT 0 , 6

--以下实践都失败
--存储过程
--业务逻辑和sql写在begin和end之间
create procedure porcedureName () 
begin 
    select name from user; 
end;  
-- 调用过程 
call porcedureName ();  
--删除存储过程
DROP PROCEDURE IF EXISTS porcedureName; -- 没有括号() 
--http://database.51cto.com/art/201608/516661.htm

--不知道为啥放sql中就会出错，但这个语句我是从phpMyadmin中复制过来
CREATE PROCEDURE  `threePeople` ( ) COMMENT  '从find_partner和wait_partner各选最新的三人内容输出' DETERMINISTIC NO SQL SQL SECURITY DEFINER BEGIN CREATE TEMPORARY TABLE find1(
SELECT id, content
FROM find_partner
ORDER BY id DESC 
LIMIT 0 , 3
);

CREATE TEMPORARY TABLE wait1(
SELECT id, content
FROM wait_partner
ORDER BY id DESC 
LIMIT 0 , 3
);

SELECT * 
FROM find1
UNION SELECT * 
FROM wait1;

END ;
CALL `threePeople` ();
--以上实践都失败












Create table graduate (
id int(10) primary key AUTO_INCREMENT,
leaver_id int(7) not null COMMENT'留言者id' ,
leaver_name varchar(12) not null,
leaver_portrait varchar(50) not null,
message varchar(256) COMMENT  '留言',
time datetime not null,
son_id int(10) comment '子编号',
son_name varchar(15) COMMENT '子名字',
son_message varchar(256) COMMENT  '子留言',
goods_id int(9) comment 'postshare表的物品id',
)


Create table Jiaoshi (
jiaoshihao char(11),
jiaoshiming char(10) not null,
xingbie char(2) check(xingbie='男'or xingbie='女'),
jiaoshixibu varchar(20) default'计算机系',
youxiang varchar(20),
primary key(jiaoshihao))

create table Kecheng(
kechenghao char(10) primary key,
kechengming varchar(20) not null unique,
jiaoshihao char(11) not null,
xuefen int check (xuefen>0),
xueshi tinyint not null,
foreign key (jiaoshihao) references Jiaoshi(jiaoshihao))

create table Chengji(
xuehao char(11),
kechenghao char(10),
kechengming varchar(20),
chengji tinyint check(chengji>=0 AND chengji<=100)
foreign key(xuehao) references Xuesheng(xuehao),
foreign key(kechenghao) references Kecheng(kechenghao),
foreign key(kechengming) references Kecheng(kechengming)
)

--索引：
ALTER TABLE Chengji DROP CONSTRAINT FK__Chengji__chengji__20C1E124
ALTER TABLE Xuesheng DROP CONSTRAINT PK__Xuesheng__745779A87F60ED59
CREATE CLUSTERED INDEX jujixingming ON Xuesheng( xingming)

CREATE CLUSTERED INDEX jujichengji ON Chengji(chengji)

--数据插入：
INSERT INTO Xuesheng         
     VALUES
           ('S211506362','呵呵','男',21,'计算机系','软件工程')
GO
INSERT INTO Xuesheng         
     VALUES
           ('S211506366','小明','男',21,'计算机系','通信工程'),
           ('S211506367','小节','女',23,'计算机系','通信工程')
GO
--SELECT * FROM Xuesheng

INSERT INTO Kecheng         
     VALUES
 	   ('1','PAPA课','JIAOSHI1',2,15),
           ('2','草草课','JIAOSHI2',2,16)

GO
--SELECT * FROM Kecheng

INSERT INTO Jiaoshi         
     VALUES
           ('JIAOSHI1','傻啥','男','计算机系','7854254@qq.com')
GO
INSERT INTO Jiaoshi         
     VALUES
           ('JIAOSHI2','二丫','女','计算机系','8548254@qq.com')
GO
--SELECT * FROM Jiaoshi   

INSERT INTO Chengji        
     VALUES
           ('S211506362','1','PAPA课',21)
GO

INSERT INTO Chengji        
     VALUES
           ('S211506362','2','草草课',60)
GO
INSERT INTO Chengji        
     VALUES
           ('S211506366','2','草草课',60),
           ('S211506367','2','草草课',66)
GO
--SELECT * FROM Chengji

--视图：
create view s211506362v1 as
select c.xuehao,c.kechenghao,c.kechengming,c.chengji,x.xingming
from Chengji c join Xuesheng x on c.xuehao=x.xuehao
--select * from s211506362v1
--视图V2查询学生的学号、所选的课程及成绩；
create view s211506362v2 as
select xuehao,kechengming,chengji
from Chengji 
-- select * from s211506362v2
--视图v3查询姓名、选修课程名称及讲授课程的教师姓名；
create view s211506362v3 as
select x.xingming,c.kechengming,Jiaoshi.jiaoshiming
from Chengji c join Xuesheng x on c.xuehao=x.xuehao 
join Kecheng on c.kechenghao=kecheng.kechenghao
join Jiaoshi on Jiaoshi.jiaoshihao=Kecheng.jiaoshihao
--select * from s211506362v3
--创建视V4查询选课成绩不及格的学生学号、课程号和成绩。
create view s211506362v4 as
select c.xuehao,c.kechenghao,c.chengji
from Chengji c where chengji<60
--select * from s211506362v4

--存储过程:

--创建存储过程，统计各个专业每门课程的平均成绩（课程和专业为输入参数）
create PROC s211506362 @kecheng char(20) = '草草课',@zhuanye char(20) = '通信工程'
AS
SELECT Xuesheng.zhuangye,AVG(chengji) as '专业平均成绩'
FROM Chengji join Xuesheng on Chengji.xuehao=Xuesheng.xuehao where Chengji.kechengming=@kecheng and Xuesheng.zhuangye=@zhuanye
group by Xuesheng.zhuangye
--exec s211506362 @zhuanye='通信工程'

--触发器:
--创建触发器，禁止删除不及格学生的成绩
 CREATE Trigger s211506362_xiaoyu60 ON Chengji AFTER delete
AS
 IF(
   SELECT chengji FROM deleted
   WHERE  xuehao not in(SELECT xuehao FROM inserted) 
   )<60
 ROLLBACK
 
 --DELETE FROM Chengji where chengji<60

--数据库用户创建若出现问题请参考https://wenku.baidu.com/view/e48f75d576eeaeaad1f3307d.html
--出现问题则是·你还未启用sqlwindow混合登入模式
--1、创建一个名为“u1”的登录用户，密码为111；其相应的数据库用户名为“admin”，并向他授予学生成绩表的查询权、学生表的查询、插入权限；
create login u1 with password='111'
create user admin for login u1
grant select on Chengji to admin
grant select,insert on Xuesheng to admin
	
--2、创建一个名为“u2”的登录用户，密码为111；其相应的数据库用户名为“teacher”，并赋予teacher登记和更改成绩的权限。
create login u2 with password='111'
create user teacher for login u2
grant insert,update on Chengji to teacher	
--3、创建一个名为“u3”的登录用户，密码为111；其相应的数据库用户名为“辅导员”，并赋予辅导员查询学生的成绩权限。
create login u3 with password='111'
create user 辅导员 for login u3
grant select on Chengji to 辅导员	
--4.角色的创建（角色名：教务处）
教务处角色对课程表有查询、修改和删除的权限。
sp_addrole @rolename=教务处
grant select,update,delete on Kecheng to 教务处


--复制.mdf和.ldf文件出现问题，打开服务，关掉有关sql的服务就好