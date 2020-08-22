junit：
	https://blog.csdn.net/sinat_27933301/article/details/102987545
	https://blog.csdn.net/cml_blog/article/details/82820753

5、断言测试
	断言测试也就是期望值测试，是单元测试的核心也就是决定测试结果的表达式。
	Assert.assertEquals	对比两个值相等
	Assert.assertNotEquals	对比两个值不相等
	Assert.assertSame	对比两个对象的引用相等
	Assert.assertArrayEquals	对比两个数组相等
	Assert.assertTrue	验证返回是否为真
	Assert.assertFlase	验证返回是否为假
	Assert.assertNull	验证null
	Assert.assertNotNull	验证非null
	断言结果：
		成功：
			无提示
		失败：
			org.junit.ComparisonFailure: 
			Expected :222
			Actual   :111
单元测试springMVC：
	Mock
	？那security了：
		https://blog.csdn.net/sinat_27933301/article/details/102987545
JUnit使用
	1、添加JUnit依赖。
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	2.测试代码
		./例/