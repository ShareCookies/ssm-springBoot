RESTful架构：http://www.ruanyifeng.com/blog/2011/09/restful.html
RESTful API 最佳实践:http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html
附：
	RESTful:https://stackoverflow.com/questions/671118/what-exactly-is-restful-programming
RESTful：
	RepresentationalStateTransfer(表现层状态转化)
	介绍：
		1.使用简单的HTTP协议来实现调用
			附：不是CORBA, RPC 或者 SOAP等负责的机制。
		2.RESTFul风格的请求：
			RESTFul风格的请求主要由以下两点实现：
			资源的暴露：（即如何定义对外的url）
				就是url定义要符合以下形式：
					1.每一个URI代表一种资源，即url命名符合动名词形式。
					2.资源使用以下动词进行操作。
						创建资源 :  HTTP POST
						获取资源 :  HTTP GET
						更新资源 :  HTTP PUT 
						删除资源 :  HTTP DELETE 
				附：
					最常见的一种设计错误，就是URI包含动词。
					因为"资源"表示一种实体，所以应该是名词，URI不应该有动词，动词应该放在HTTP协议中。
					正确的写法是把动词transfer改成名词transaction，资源不能是动词，但是可以是一种服务.
					POST /accounts/1/transfer/500/to/2
					POST /accounts/transaction?from=1&to=2&amount=500.00
			资源的表述：（即使用什么格式来传输数据）
				客户端和服务器端针对某一资源是如何通信的。
					例：文本可以用txt格式表现，也可以用HTML格式、XML格式、JSON格式表现，甚至可以采用二进制格式；
				尽管没有限制必须返回的类型，但是一般基于Web services的Rest返回JSON或者XML作为响应。
				附：
					在HTTP请求的头信息中用Accept和Content-Type字段指定，这两个字段才是对"表现层"的描述。
					控制器本身通常并不关心资源如何表述。
					控制器完成了它的工作之后，资源才会被转化成最适合客户端的形式。
			
		hcg：
			RESTful一种api设计风格。即：controller层的url定义，符合RESTFul风格，就称它为RESTful架构。
	例：
		./spring实现RESTful架构.txt
	附：
		如果一个架构符合REST原则，就称它为RESTful架构。