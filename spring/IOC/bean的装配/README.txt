bean的装配：
	介绍：
		定义那些类要注册为spring容器的bean。
	方式1：在XML中进行显式配置。p76
	方式2：Spring自动发现和装配bean。（推荐）
		./bean自动加载/springBean的自动加载.txt
	方式3：在Java中进行显式配置。
		./JavaConfig显式配置.txt
	附：
		混合配置：
			介绍：
				Spring的bean配置风格是可以互相搭配的。
				所以你可以选择使用XML装配一些bean，
				使用Spring基于Java的配置（JavaConfig）来装配另一些bean，
				而将剩余的bean让Spring去自动发现。		