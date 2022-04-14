https://zhuanlan.zhihu.com/p/356001849
nginx:
	官网：http://nginx.org/en/download.html
	Nginx：
		Nginx("engine x")是一款是由俄罗斯的程序设计师Igor Sysoev，
		开发的高性能 Web（Http） 和 反向代理 服务器，也是一个 IMAP/POP3/SMTP 代理服务器。	
		ng特点：
			能够支持高达 50,000 个并发连接数的响应。
			使用ng能够让你达到不间断服务的情况下进行软件版本的升级。
			消耗小，Nginx采用C进行编写，不论是系统资源开销还是CPU使用效率都比 Perlbal 要好很多。
			稳定几乎可以做到7*24不间断运行，即使运行数个月也不需要重新启动。
	nginx的安装：
		./linux安装nginx.txt
		附：
			./ng配置文件介绍.txt
ng特性：
	代理：
		https://www.cnblogs.com/anruy/p/4989161.html
		介绍：
			所谓“代理”，是指在内网边缘 设置一个硬件/软件转发请求；
			“正向”还是“反向”的说法，取决于转发的是"出站请求"还是"入站请求".
			正向代理：
				处理来自客户端的出站请求，将其转发到Internet，然后将生成的响应返回给客户端。
			反向代理：
				处理来自Internet的入站请求，将其转发给后端工作程序，然后将响应返回给Internet。
		反向代理的两种应用：
			可以作为内容服务器的替身，也可以作为内容服务器集群的负载均衡器。
			1. 作内容服务器的替身：
				Nginx 反向代理的指令不需要新增额外的模块，ng默认自带 proxy_pass 指令，只需要修改配置文件就可以实现反向代理。
				https://blog.csdn.net/linlin_0904/article/details/89633150
                配置文件：
				    server {
						listen 9888;
						server_name  58xuejia.cn;
						location / {
							proxy_pass https://www.baidu.com;   #代理地址，代理百度
							#proxy_redirect off; #禁止跳转
							# 便于获取用户真实ip https://blog.csdn.net/qq_28796345/article/details/88685245
							#proxy_set_header Host $host;
							#proxy_set_header  X-Real-IP        $remote_addr;
							#proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;
							#proxy_set_header X-NginX-Proxy true;

						}
					}
					附：
					    #rewrite重写URL
						location ^~ /api/ {
							rewrite ^/api/(.*) /$1 break;
							proxy_pass http://localhost:8080;
						}
					注：
						动静分离:
							前端部署在ng上，后端通过url重写等进行跳转代理。
						例：
							#前端
							location / {
								error_page 405 =200 /index.html;
								index index.html;
								root /work2/egov/frontend;
							}
							#后端
							location ^~ /api/ {
								rewrite ^/api/(.*) /$1 break;
								proxy_pass http://localhost:8080;
							}
							
				附：代理服务器附加功能
					1. 代理可解决跨域问题
					2. 
					可提高内容服务器的安全性。
						如在防火墙外部设置一个代理服务器作为内容服务器的替身。
						当客户机向站点提出请求时，请求将转到代理服务器。然后，代理服务器通过防火墙中的特定通路，将客户机的请求发送到内容服务器。内容服务器再通过该通道将结果回传给代理服务器。代理服务器将内容发送给客户机。
						这样，代理服务器就在安全数据库和可能的恶意攻击之间提供了又一道屏障。与有权访问整个数据库的情况相对比，就算是侥幸攻击成功，作恶者充其量也仅限于访问单个事务中所涉及的信息。未经授权的用户无法访问到真正的内容服务器，因为防火墙通路只允许代理服务器有权进行访问。
						附：
							可以配置防火墙路由器，使其只允许特定端口上的特定服务器有权通过防火墙进行访问，而不允许其他任何机器进出。
					3. 对流量执行操作、使用缓存或压缩来提高性能：
						...
						节省带宽:
							支持gzip压缩
							对静态资源开启gzip压缩
							location / {
										gzip on;
										gzip_types application/javascript text/css image/jpeg;
							}

			2.作为内容服务器集群的负载均衡器：
				./负载均衡.txt
			
	Keepalived:高可用
		https://www.cnblogs.com/wang-meng/p/5861174.html
		Keepalived工作流程：？
			首先Keepalived可以在主机上产生一个虚拟的ip,192.168.200.150, keepalived会将这个virtual ip绑定到交换机上.
			当用户访问主机:ng1时, 交换机会通过这个ip和虚拟ip的对应找到192.168.200.129上的Nginx进行处理.
			
			如果当有一天192.168.200.129上的Nginx挂掉的时候, Keepalived会立即在备机上生成一个相同的vip: 192.168.200.150, 当用户继续访问192.168.200.129时, 交换机上已经重新绑定了virtual ip, 这时发现这个vip是存在于192.168.200.130上面的, 所以直接将请求转发到了备机上. 
			
			如果主机被修复好能够继续对外提供服务时, 这时keepalived会将主机上继续生成这个virtual ip, 同时回收在备机上生成的 virtual ip. 
				这个是通过心跳检查来判断主机已恢复使用.
		搭建Keepalived：
			1.安装openssl
				Keepalived需要依赖openssl
				附：
					检测是否安装了openssl
						rpm -qa | grep openssl
			2.安装Keepalived:
				https://www.cnblogs.com/wang-meng/p/5861174.html		
	ng有高并发吗？
	限流？
	
		
	ng支持https：
		https://www.cnblogs.com/jingxiaoniu/p/6745254.html
		注：
			1.从nginx 1.15以及更新的版本，不在支持ssl on;的写法 了
			2.当一个域名开启https后，谷歌浏览器就会自动对该域名应用http转https，即使你切换端口也会。
		实现：
		1.开启nginx的ssl模块
			...
		2.配置文件：
			server{
				#listen 80;#监听端口
				listen 80;
				listen 443 ssl;
				#ssl证书(公钥.发送到客户端的)
				ssl_certificate ssl/1_58xuejia.cn_bundle.crt;
				#ssl私钥,
				ssl_certificate_key ssl/2_58xuejia.cn.key;
				server_name 58xuejia.cn;#域名
				
				location / {
					#root /myspace/developEnvironment/ng/nginxRunningEnvironment/html;#站点目录
					root /myspace/developEnvironment/projects/eas-fe-h5/;#站点目录
					index  index.html index.htm;
				}
			}
		附：
			ng配https作用？