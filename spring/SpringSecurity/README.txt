Spring Security简介：
	Spring Security是为基于Spring的应用程序提供安全保护的安全性框架。
	它能够在Web请求级别，或方法调用级别，处理身份认证和授权。
	Spring Security能够解决两种安全性问题:
		1.保护Web请求
			Security使用了Servlet规范中的Filter保护Web请求并限制URL级别的访问。
		2.保护方法调用
			Security使用了Spring AOP保护方法调用，能够确保只有具备适当权限的用户才能访问安全保护的方法。
使用security保护Web请求：
	sprig security 结合 token 实现用户认证：
		./Spring Security快速使用.txt
	./使用security保护Web请求.txt
	附：
		Spring Security 用户认证过程：
			https://blog.csdn.net/my_learning_road/article/details/79833802
		《Spring实战》（第4版）第9章，主要是Spring Security保护Web层的安全。
使用security保护方法调用：
	...
	《Spring实战》（第4版）第14章中，主要是Spring Security如何保护方法的调用。

附：
	spring security 管方文档：
		https://docs.spring.io/spring-security/site/docs/4.2.1.RELEASE/reference/htmlsingle/#getting-started
	1. 认证 (Authentication) 和授权 (Authorization)的区别：
		认证 (Authentication)： 你是谁。
		授权 (Authorization)： 你有权限干什么。
	什么是token：
		Token即无需在服务端存放 Session 等信息，只需在客户端保存服务端返回的 Token 就能实现身份验证。
		JWT （JSON Web Token）：
			token的一种实现。
			JWT 本质上就一段签名的 JSON 格式的数据。由于它是带有签名的，因此接收者便可以验证它的真实性。
			JWT 由 3 部分构成:
				Header :描述 JWT 的元数据。定义了生成签名的算法以及 Token 的类型。
				Payload（负载）:用来存放实际需要传递的数据
				Signature（签名）：服务器通过Payload、Header和一个密钥(secret)使用 Header 里面指定的签名算法（默认是 HMAC SHA256）生成。
		实现过程：
			在基于 Token 进行身份验证的的应用程序中，服务器通过Payload、Header和一个密钥(secret)创建令牌（Token）并将 Token 发送给客户端，客户端将 Token 保存在 Cookie 或者 localStorage 里面，以后客户端发出的所有请求都会携带这个令牌。
		附：
			你可以把它放在 Cookie 里面自动发送，但是这样不能跨域，所以更好的做法是放在 HTTP Header 的 Authorization字段中： Authorization: Bearer Token。
		https://github.com/Snailclimb/JavaGuide/blob/master/docs/system-design/authority-certification/basis-of-authority-certification.md
	附：
		什么是OAuth 2.0：
			OAuth 是一个行业的标准授权协议，主要用来授权第三方应用获取有限的权限。
				实际上它就是一种授权机制，它的最终目的是为第三方应用颁发一个有时效性的令牌token，使得第三方应用能够通过该令牌获取相关的资源。
待定，废弃：
	Spring Security具有的模块:p306
		Spring Security 3.2分为11个模块。
		注：
			I.
				应用程序的类路径下至少要包含Core和Configuration这两个模块。
				Spring Security经常被用于保护Web应用，所以还需添加Web模块。
		
	用户的认证：
		添加自定义的登录页：p311
		启用HTTP Basic认证：p333
			HTTP Basic认证（HTTP Basic Authentication）会直接通过HTTP请求本身，对要访问应用程序的用户进行认证。
			HTTP Basic要求请求中包含一个用户名和密码，否则会产生HTTP 401响应。
		启用Remember-me功能：p334
			Spring Security使得为应用添加Remember-me功能变得非常容易。
			为了启用这项功能，只需在configure()方法所传入的HttpSecurity对象上调用rememberMe()即可。
			默认情况下，这个功能是通过在cookie中存储一个token完成的，这个token默认两周内有效。
		退出：