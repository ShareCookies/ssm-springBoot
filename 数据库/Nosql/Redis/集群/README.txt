https://blog.csdn.net/miss1181248983/article/details/90056960
https://blog.csdn.net/u013058742/article/details/80004893
Redis不同集群方案优缺点分析：
	https://www.cnblogs.com/johnvwan/p/12083024.html
springBoot连接Sentinel主从：
	./springBoot连接Sentinel主从.txt
Redis有三种集群模式，分别是：主从模式，Sentinel模式，Cluster模式
主从模式：
	在主从主从模式中，数据库分为两类：主数据库(master)和从数据库(slave)。
	主从复制特点：
		* 主数据库可以进行读写操作，当读写操作导致数据变化时会自动将数据同步给从数据库
		* 从数据库一般都是只读的，并且接收主数据库同步过来的数据
		* 一个master可以拥有多个slave，但是一个slave只能对应一个master
		* slave挂了不影响其他slave的读和master的读和写，重新启动后会将数据从master同步过来
		* master挂了以后，不影响slave的读，但redis不再提供写服务，master重启后redis将重新对外提供写服务
		安全设置：
			当master节点设置密码后，
			客户端访问master需要密码
			启动slave需要密码，在配置文件中配置即可
			客户端访问slave不需要密码
	工作机制：
		当slave启动后，主动向master发送SYNC命令。
		master接收到SYNC命令后在后台保存快照（RDB持久化）和缓存保存快照这段时间的命令，然后将保存的快照文件和缓存的命令发送给slave。
		slave接收到快照文件和命令后加载快照文件和缓存的执行命令。
		复制初始化后，master每次接收到的写命令都会同步发送给slave，保证主从数据一致性。
	主从模式搭建
		环境准备：
			master节点                  192.168.30.128
			slave节点                   192.168.30.129
			slave节点                   192.168.30.130
		全部下载安装：
			# cd /software
			# wget http://download.redis.io/releases/redis-5.0.4.tar.gz
			# tar zxf redis-5.0.4.tar.gz && mv redis-5.0.4/ /usr/local/redis
			# cd /usr/local/redis && make && make install
			# echo $?
		（附）全部配置成服务：
			服务文件
				# vim /usr/lib/systemd/system/redis.service
				[Unit]
				Description=Redis persistent key-value database
				After=network.target
				After=network-online.target
				Wants=network-online.target
				[Service]
				ExecStart=/usr/local/bin/redis-server /usr/local/redis/redis.conf --supervised systemd
				ExecStop=/usr/libexec/redis-shutdown
				Type=notify
				User=redis
				Group=redis
				RuntimeDirectory=redis
				RuntimeDirectoryMode=0755
				[Install]
				WantedBy=multi-user.target
		修改配置：
			192.168.30.128
				# mkdir -p /data/redis
				# vim /usr/local/redis/redis.conf
				bind 192.168.30.128               #监听ip，多个ip用空格分隔
				daemonize yes               #允许后台启动
				logfile "/usr/local/redis/redis.log"                #日志路径
				dir /data/redis                 #数据库备份文件存放目录
				#masterauth 123456               #slave连接master密码，master可省略
				#requirepass 123456              #设置master连接密码，slave可省略
				appendonly yes                  #在/data/redis/目录生成appendonly.aof文件，将每一次写操作请求都追加到appendonly.aof 文件中
				# echo 'vm.overcommit_memory=1' >> /etc/sysctl.conf
				# sysctl -p
			192.168.30.129
				# mkdir -p /data/redis
				# vim /usr/local/redis/redis.conf
				bind 192.168.30.129
				daemonize yes
				logfile "/usr/local/redis/redis.log"
				dir /data/redis
				replicaof 192.168.30.128 6379
				#masterauth 123456
				#requirepass 123456
				appendonly yes
				# echo 'vm.overcommit_memory=1' >> /etc/sysctl.conf
				# sysctl -p
			192.168.30.130
				# mkdir -p /data/redis
				# vim /usr/local/redis/redis.conf
				bind 192.168.30.130
				daemonize yes
				logfile "/usr/local/redis/redis.log"
				dir /data/redis
				replicaof 192.168.30.128 6379
				#masterauth 123456
				#requirepass 123456
				appendonly yes
				# echo 'vm.overcommit_memory=1' >> /etc/sysctl.conf
				# sysctl -p
		全部启动redis：
			# systemctl start redis
		查看集群状态：
			./src/redis-cli -p 6379 -a 密码
			info replication		
		测试：
			可以看到，在master节点写入的数据，很快就同步到slave节点上，而且在slave节点上无法写入数据。
Sentinel模式
	Sentinel模式介绍
		主从模式的弊端就是不具备高可用性，当master挂掉以后，Redis将不能再对外提供写入操作，因此sentinel应运而生。
		sentinel模式的作用就是监控redis集群的运行状况，是建立在主从模式的基础上。
		当master挂了以后，sentinel会在slave中选择一个做为master，并修改它们的配置文件，其他slave的配置文件也会被修改。
			比如
				slaveof属性会指向新的master，
				当主从模式配置密码时，sentinel也会同步将配置信息修改到配置文件中，
			当master重新启动后，它将不再是master而是做为slave接收新的master的同步数据
		sentinel因为也是一个进程有挂掉的可能，所以sentinel也会启动多个形成一个sentinel集群
			多sentinel配置的时候，sentinel之间也会自动监控
			一个sentinel或sentinel集群可以管理多个主从Redis，多个sentinel也可以监控同一个redis
		当使用sentinel模式的时候，客户端就不要直接连接Redis，而是连接sentinel的ip和port，由sentinel来提供具体的可提供服务的Redis实现。
			这样当master节点挂掉以后，sentinel就会感知并将新的master节点提供给使用者。
	工作机制：
		* 每个sentinel以每秒钟一次的频率向它所知的master，slave以及其他sentinel实例发送一个 PING 命令 
		* 如果一个实例距离最后一次有效回复 PING 命令的时间超过 down-after-milliseconds 选项所指定的值， 则这个实例会被sentinel标记为主观下线。 
		* 如果一个master被标记为主观下线，则正在监视这个master的所有sentinel要以每秒一次的频率确认master的确进入了主观下线状态
		* 当有足够数量的sentinel（大于等于配置文件指定的值）在指定的时间范围内确认master的确进入了主观下线状态， 则master会被标记为客观下线 
			* 在一般情况下， 每个sentinel会以每 10 秒一次的频率向它已知的所有master，slave发送 INFO 命令 
		* 当master被sentinel标记为客观下线时，sentinel向下线的master的所有slave发送 INFO 命令的频率会从 10 秒一次改为 1 秒一次 
			* 若没有足够数量的sentinel同意master已经下线，master的客观下线状态就会被移除；
			若master重新向sentinel的 PING 命令返回有效回复，master的主观下线状态就会被移除
	配置文件：
		配置一个或多个sentinel
		例：
			./redis-he-sentinel.conf
	全部启动sentinel：
		./redis-server sentinel.conf --sentinel
	测试：
		将master服务杀死
			kill -15 pid
			再观察一段时间，slave从服务器成为主服务器
	附：
		SENTINEL常用命令：
			https://www.jianshu.com/p/a29050278b71
			SENTINEL masters ：列出所有被监视的主服务器，以及这些主服务器的当前状态。
		springBoot连接Sentinel主从.txt
Cluster模式介绍
	介绍：
		cluster模式的出现就是为了解决单机Redis容量有限的问题，对存储的数据进行分片，将Redis的数据根据一定的规则分配到多台机器（多个Redis实例中）。
		cluster集群特点：
			* 多个redis节点网络互联，数据共享
				* 客户端可以连接任何一个主节点进行读写
				是怎么实现的了，我连a，a没数据它是如何从别的地方获取到数据了？
					应该要先了解下redis的分配规则，当你读取数据时可能会根据关键特征寻找节点读取数据。
					https://blog.csdn.net/weixin_43145146/article/details/98851126
			* 所有的节点都是一主一从（也可以是一主多从），其中从不提供服务，仅作为备用
				主从都挂了。那么整个集群进入fail状态。
				附：
					https://www.cnblogs.com/dadonggg/p/8628735.html
					1、集群是如何判断是否有某个节点挂掉
					　　节点之间通过互相的ping-pong判断是否节点可以连接上。如果有一半以上的节点去ping一个节点的时候没有回应，集群就认为这个节点宕机了，然后去连接它的备用节点。
					2、集群进入fail状态的必要条件
						A、某个主节点和所有从节点全部挂掉，我们集群就进入faill状态。
						B、如果集群超过半数以上master挂掉，无论是否有slave，集群进入fail状态.
						C、如果集群任意master挂掉,且当前master没有slave.集群进入fail状态				
			* 不支持同时处理多个key（如pipeline，MSET/MGET），即不支持批处理操作	
			因为redis需要把key均匀分布在各个节点上， 并发量很高的情况下同时创建key-value会降低性能并导致不可预测的行为
				？为什么了，举个例子
				redis集群会根据其分配规则来把数据分配到指定节点，如果用批量操作即高并发可能会导致数据分配错节点等情况，那么查找时可能就找不到数据了等未知情况，所以redis的Jedis(Redis 的 Java 客户端(api))不主动提供集群的批处理支持方法？
			* 支持在线增加、删除节点
				？redis集群数据是均摊的吗，新加入的节点会被分配数据吗
				应该不是，还是要了解分配规则 https://blog.csdn.net/qq_40718168/article/details/89454996
		redis cluster集群是去中心化的，每个节点都是平等的，连接哪个节点都可以获取和设置数据。
			平等指的是master节点，因为slave节点根本不提供服务，只是作为对应master节点的一个备份。
			master节点如果挂掉，它的slave节点变为新master节点继续对外提供服务，而原来的master节点如果重启，则变为新master节点的slave节点。
	集群搭建：
		https://blog.csdn.net/miss1181248983/article/details/90056960
		1. 修改配置文件：
			主要是将redis配置文件中的cluster-enable配置打开即可
		2. 启动所有的redis服务：
			附：如果redis版本比较低，则需要安装ruby。任选一台机器安装ruby即可
		3.创建集群
			# redis-cli -a 123456 --cluster create 192.168.30.128:7001 192.168.30.128:7002 192.168.30.129:7003 192.168.30.129:7004 192.168.30.130:7005 192.168.30.130:7006 --cluster-replicas 1
		附：
		集群的操作
			登录集群：
				# redis-cli -c -h 192.168.30.128 -p 7001 -a 123456                  # -c，使用集群方式登录
			查看集群信息
				CLUSTER INFO 
			列出节点信息
				CLUSTER NODES
				注：这里与nodes.conf文件内容相同