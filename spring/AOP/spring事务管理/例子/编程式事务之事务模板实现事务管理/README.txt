介绍：
	使用TransactionTemplate，进行编程式事务管理。
简易实现步骤：
	1.在spring配置中用bean定义：
		1.事务管理器和2.TransactionTemplate模板
	2.创建一个类
		介绍:
			该类中用transactionTemplate.execute()来启用事务并对数据库操作。
		属性:
			DataSource,TransactionManager,TransactionTemplate
		方法:
			transactionTemplate.execute()
				该方法需要参数:
					实现了TransactionCallback回调接口或TransactionCallbackWithoutResult回调接口的匿名函数。
					在接口规定的方法中操作数据库。
				
	3.用工厂调用2的类，执行里头方法即可实验事务。
注：
	1.*Test结尾为测试入口
	
	2.事物回滚失败？手动回滚事物提示重复提交事物，因为事务的回滚与提交权不在我们身上？
	我们不能catch住错误，那样事物模版就捕捉不到错误无法回滚了。那如何catch错误了？
	https://blog.csdn.net/liaohaojian/article/details/70139151
	https://www.ibm.com/developerworks/cn/education/opensource/os-cn-spring-trans/