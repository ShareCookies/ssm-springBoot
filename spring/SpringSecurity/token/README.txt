1.怎么生成token（即怎么登录）：
	与security进行交互进行身份认证，并返回token
	例：
		@Autowired
		private AuthenticationManager authenticationManager;
		@Autowired
		UserDetailsService userDetailsService;

		//token的生成。（即如何登入）
		//1,2,3,4步骤都是和spring security交互的。只有第5步是我们自己定义的
		//客户端可以通过该请求来获得一个token了{"token":"abcd","expires":1234567890}。
		// 那么下次请求的时候，带上 token=abcd这个参数（或者也可以是自定义的请求头中）
		@PostMapping("/user/loginByAccount2")
		public AuthToken loginByAccount2(@RequestParam(value = "name") String username, @RequestParam String password) {
			// 1 创建UsernamePasswordAuthenticationToken
			UsernamePasswordAuthenticationToken token
					= new UsernamePasswordAuthenticationToken(username, password);
			// 2 认证
			Authentication authentication = this.authenticationManager.authenticate(token);
			// 3 保存认证信息
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// 4 加载UserDetails
			UserDetails details = this.userDetailsService.loadUserByUsername(username);
			// 5 生成自定义token
			User user = new User();
			user.setName(username);
			user.setPassword(password);
			AuthTokenProvider authTokenProvider = new AuthTokenProvider();
			return authTokenProvider.createToken(user);
		}
		注：
			AuthTokenProvider：
				token工具类通常具有两个方法，即生成token，验证token。
				例：
					./AuthTokenProvider.java
			AuthToken：
				例：
					./AuthToken.java
2.spring security怎么根据token得到授权认证信息：
	客户端有token后，请求的时候带上token=abcd参数。
	spring security中使用一个自定义的filter解析token，复原无状态下的"session"，让当前请求处理线程中具有认证授权数据，后面的业务逻辑才能执行。。
	自定义的token解析filter：
		./filter/MyAuthTokenFilter.java
	将自定义的filter整合到spring security中:
		1.编写一个类，继承SecurityConfigurerAdapter。
		（可能是security配置使用了装饰器模式，所以为security配置添加新功能，你加进配置的类要符合它的接口要求）
		例：
			./configuration/MyAuthTokenFilterConfigurer.java
		2.加入到配置类中：
		    // 增加方法
			private MyAuthTokenConfigurer securityConfigurerAdapter() {
			  return new MyAuthTokenConfigurer(userDetailsService, tokenProvider);
			}

			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http
					.and()
					.apply(securityConfigurerAdapter());// 增加自定义的securityConfigurerAdapter
			}
	注：
		获取当前登录用户信息：
			在控制器中获取当前登录用户的信息。
			例：
				@RequestMapping("/info")
				@ResponseBody
				public String productInfo(){
					String currentUser = "";
					//通过SecurityContextHolder来获取当前登入用户信息
					Object principl = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					if(principl instanceof UserDetails) {
						currentUser = ((UserDetails)principl).getUsername();
					}else {
						currentUser = principl.toString();
					}
					return " some product info,currentUser is: "+currentUser;
				}