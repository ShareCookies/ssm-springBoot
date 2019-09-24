介绍：
	Spring事务管理功能，分为编程式事务和声明式事务。
	存放内容：
		编程式事务（TransactionTemplate）和声明式事务（TransactionProxyFactoryBean）的实现
注：
	spring事务启动后，当发生错误时会自动回滚，无需手动回滚。
	即：如果trycatch住了，就不会回滚，这时要在catch中回滚还会报事务提交两次错误。
	写一个update方法,然后手动抛出一个RuntimeException，然后看看有没有生成update的sql语句，如果生成了而且抛出了异常并且数据库的数据没有发生改变，说明事物生效了。