mybatis generato：
	介绍：该工具是根据数据库表自动生成，dao接口，entity对象，mapper映射文件。
	1.配置maven的generato插件：
	  <plugin>
		<groupId>org.mybatis.generator</groupId>
		<artifactId>mybatis-generator-maven-plugin</artifactId>
		<version>1.3.2</version>
		<configuration>
		  <!--允许移动生成的文件-->
		  <verbose>true</verbose>
		  <!--允许覆盖生成的文件-->
		  <overwrite>true</overwrite>
		</configuration>
	  </plugin>
	2.以下两个文件放至项目resource目录下，并修改文件部分字段即可
		generatorConfig.xml	maven插件的配置文件
		generator.properties	一些数据库信息
		图方便的话，也可以将两个合并成一个文件
		
	注：
		因为与spring整合，要先去dao层*mapper文件加一个@Repository，表明它是数据访问组件,即DAO组件
		分模块的使用要注意!我碰到过只有在父模块引入插件才生效的情况？

