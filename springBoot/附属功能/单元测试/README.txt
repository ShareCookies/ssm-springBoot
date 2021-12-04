junit：
	https://blog.csdn.net/sinat_27933301/article/details/102987545
简介
	JUnit是一款优秀的开源Java单元测试框架，开发工具Eclipse和IDEA对JUnit都有很好的支持，JUnit主要用于白盒测试和回归测试。
	附：
		白盒测试：把测试对象看作一个打开的盒子，程序内部的逻辑结构和其他信息对测试人 员是公开的；
		回归测试：软件或环境修复或更正后的再测试；
		单元测试：最小粒度的测试，以测试某个功能或代码块。一般由程序员来做，因为它需要知道内部程序设计和编码的细节；
	附：	
		超时测试
			@Test(timeout = 1000)
		断言测试
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
	附：
		单元测试注解：
			@RunWith	标识为JUnit的运行环境。
			@SpringBootTest	获取启动类、加载配置，确定装载Spring Boot。
				通过@SpringBootTest(classes=...) 指明springboot配置类 或 配置类有用注解 @ContextConfiguration
			@Test	声明需要测试的方法。
			@BeforeClass	针对所有测试，只执行一次，且必须为static void。
			@AfterClass	针对所有测试，只执行一次，且必须为static void。
			@Before	每个测试方法前都会执行的方法。
			@After	每个测试方法前都会执行的方法。
			@Ignore	忽略方法。
		单元测试springMVC：
			Mock
			？那security了：
				https://blog.csdn.net/sinat_27933301/article/details/102987545
		如何快速单元测试
			1. https://blog.csdn.net/cml_blog/article/details/82820753
			2. 单独写个简洁的配置类，便于单元测试引用
JUnit使用
	1、添加JUnit依赖。
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	2.测试代码
		./例/
