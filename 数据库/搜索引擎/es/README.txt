链接：
	官方网址：https://www.elastic.co/guide/index.html
	Github：https://github.com/elastic/elasticsearch
	es参考手册：
		https://www.elastic.co/guide/en/elasticsearch/reference/master/index.html
		https://www.elastic.co/guide/cn/elasticsearch/guide/current/foreword_id.html
	elasticsearch中文社区：https://elasticsearch.cn/
	es教程：https://www.yuque.com/yanyulou-jxe2g/iz0eoq/ehidqr
ElasticSearch介绍：
	Elasticsearch is a real-time, distributed storage, search, and analytics engine
	Elasticsearch是一个实时的分布式存储、 搜索、分析的引擎。		
	Elasticsearch是用Java语言开发的，基于Lucene的分布式（全文）搜索服务器。
		ES基于Lucene成熟的索引方案，隐藏了Lucene的复杂性。
		es本身就是分布式实现。
		多客户端：
			并对外提供Restful web接口来操作索引、搜索等。
			官方客户端在Java、.NET（C#）、PHP、Python、Apache Groovy、Ruby和许多其他语言中都是可用的。
	
	es优点缺陷：
		优点：
			高性能
			高可用
			实时性
		缺点：
	附：
		ES 分布式架构原理：
			es原理：ES基于Lucene成熟的索引方案，在加上一些分布式的实现，最终实现了分布式全文搜索服务器。
			es分布式架构原理：就是在多台机器上启动多个 ES 进程实例，来组成了一个 ES 集群。
				!为什么要叫架构原理了
			...这里有点乱
		Lucene:
			Lucene 是Apache一个开放源代码的全文检索引擎工具包，
			但它不是一个完整的全文检索引擎，而是一个全文检索引擎的架构
				提供了完整的查询引擎和索引引擎，部分文本分析引擎（英文与德文两种西方语言）。？
			Lucene的目的是为软件开发人员提供一个简单易用的工具包，以方便的在目标系统中实现全文检索的功能，或者是以此为基础建立起完整的全文检索引擎。
				即直接基于lucene开发,开发复杂、要深入理解原理 (各种索引结构)等。

			
		es和solr选择哪个：
			根据DB-Engines的排名显示，Elasticsearch是最受欢迎的企业搜索引擎，其次是Apache Solr，也是基于Lucene。
			1.如果你公司现在用的solr可以满足需求就不要换了。
			2.如果你公司准备进行全文检索项目的开发，建议优先考虑elasticsearch，因为像Github这样大规模的搜索都在用它。
				1.扩展性好，可部署上百台服务器集群，处理PB级数据。
				2.近实时的去索引数据、搜索数据。

		ES的常见使用场景：
			搜索引擎、全文检索、海量数据近实时处理


			
ES 的核心概念
	Near Realtime：(近实时)
		Elasticsearch是一个近乎实时的搜索平台。
		从写入数据到数据可以被搜索到只有一个小延迟（大概是 1s）
				
	Document & field
		document (文档)是 ES 中最小的数据单元，文档通常用JSON数据结构表示！。
			即文档是可以被索引的基本信息单元
		field 数据字段。
			一个 document 里面有多个 field(字段)。
			元数据：
				_id元数据：
					代表document的唯一标识，与index和type一起，可以唯一标识和定位一个document. 
					我们可以手动指定，也可以不指定,由es自动为我们创建一个id.
						注：
							自动生成的id，长度为20个字符，URL安全，base64编码， GUID, 分布式系统并行生成时不可能会发生冲突。
				_index元数据：
					代表一个document存放在哪个index中。
					附:通常类似的数据放在一个索引， 非类似的数据放不同索引。
				_type元数据(已弃用将要被移除)
					代表document属于index中的哪个类别(type) 。
					一个索引通常会划分为多个type,逻辑上对index中有些许不同的几类数据进行分类:因为一批相同的数据，可能有很多相同的fields,但是还是可能会有一些轻微的不同。
				
		例：
			一条商品document
			{
			  "product_id": "1",
			  "product_name": "iPhone X",
			  "product_desc": "苹果手机",
			  "category_id": "2",
			  "category_name": "电子产品"
			}		
		附：
			辅助理解：
				一个文档相当于mysql里的一条数据，对数据的搜素可以归结为对文档的搜索。
	Index
		索引是具有相似结构的文档集合。
			即：一个索引包含很多 document，一个索引就代表了一类相似或者相同的 ducument。
		附：
			ES 中存储数据的基本单位是索引
				例：
					要在 ES 中存储一些订单数据，你就应该在 ES 中创建一个索引 order_idx ，所有的订单数据就都写到这个索引里面去。
			辅助理解：
				
				索引就像数据库，用户可以向索引写入文档或者从索引中读取文档。
				ElasticSearch将数据存储在一个或多个索引(index)中。
		附：
			索引名称必须是小写的，不能用下划线开头，不能包含逗号
	mapping
		mapping数据类型是index(或index中的type)对应一种数据结构和相关配置
			辅助理解：
				类似表结构定义，描述数据在index内如何存储，mapping数据类型指明Elasticsearch要如何处理这些域。
				es就是通过mapping来实现全文检索等。
		

	集群、节点、分片、副本：
		./分布式/分布式.txt
	附:
		Elasticsearch数据是如何存储的:
			https://blog.csdn.net/RuiKe1400360107/article/details/109245344
			https://elasticsearch.cn/article/6178
			es的数据存在/var/lib/elasticsearch下。
			附：
				具体什么位置看配置文件的path.data 
				vi /etc/elasticsearch/elasticsearch.yml
			node/0/indices/ 当前节点是0吗？
			当前节点es数据都存该目录下，indices下的目录即为索引id，
			索引id目录下面的目录即为lucene的数据存储文件了，所以es的文件实际是存在lucene中的，即文档是调用luceneApi进行存储的。
			...
			
			？分片位置那那
	注：
		types 这个概念在 ElasticSearch 8. X 已被完全移除
		Type
			type 类型，是 index 的一个逻辑分类，用来对index中数据进行细分类。
			每个索引里可以有一个或者多个 type，每个type都可以存储多条 document。
			例：
				比如商品 index 下有多个 type：日化商品 type、电器商品 type、生鲜商品 type。
				每个 type 下的 document 的 field 可能不太一样。
		为什么废除type：
			https://blog.csdn.net/qq_34182808/article/details/99620548
			1. 在ES中，由于在不同类型映射中具有相同名称的字段在内部由相同的Lucene字段支持，因此在相同的索引中，如果给不同type设置相同filed，但是filed的属性不同的话，在操作比如删除时，就可能会删除失败。
			2. 除此之外，存储在同一索引中具有很少或没有共同字段的不同实体会导致数据稀疏，并影响Lucene有效压缩文档的能力。
			因此在es7的时，废除类mapping types 的设置。
		
	es分布式：
		./分布式.txt
es安装：
	./安装.txt
es状态：
	./es状态.txt
es操作：
	./es操作/es操作.txt
	./es操作/es搜索.txt
	
链接：
	https://www.cnblogs.com/cjsblog/p/9439331.html
	https://www.ruanyifeng.com/blog/2017/08/elasticsearch.html
	https://www.jianshu.com/p/7d687c9dba4f
	https://www.jianshu.com/p/2ea5170dfd9d