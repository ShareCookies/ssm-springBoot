官网：
	https://redis.io/
	http://www.redis.cn
	https://github.com/antirez/redis
教程：
	https://www.runoob.com/redis/redis-commands.html
	https://blog.csdn.net/hellozpc/article/details/81267030
redis安装：
	./linux安装redis.txt
Spring Boot 使用 Redis:
	/springBoot/附属功能/redis/
	https://github.com/yuyumyself/ssm-springBoot/tree/master/springBoot/%E9%99%84%E5%B1%9E%E5%8A%9F%E8%83%BD/redis
Redis集群：
	./集群/
	Redis架构模式：
		单机版
			特点：简单
			问题：
				1、内存容量有限 2、处理能力有限 3、无法高可用。
		主从
			主从复制：
				Redis 的复制（replication）功能允许用户根据一个 Redis 服务器来创建任意多个该服务器的复制品，其中被复制的服务器为主服务器（master），而通过复制创建出来的服务器复制品则为从服务器（slave）。 只要主从服务器之间的网络连接正常，主从服务器两者会具有相同的数据，主服务器就会一直将发生在自己身上的数据更新同步 给从服务器，从而一直保证主从服务器的数据相同。
				特点：
				1、master/slave 角色
				2、master/slave 数据相同
				3、降低 master 读压力在转交从库
				问题：
					无法保证高可用
					没有解决 master 写的压力
			主从+哨兵：
				Redis sentinel 是一个分布式系统中监控 redis 主从服务器，并在主服务器下线时自动进行故障转移。其中三个特性：
				监控（Monitoring）：    Sentinel  会不断地检查你的主服务器和从服务器是否运作正常。
				提醒（Notification）： 当被监控的某个 Redis 服务器出现问题时， Sentinel 可以通过 API 向管理员或者其他应用程序发送通知。
				自动故障迁移（Automatic failover）： 当一个主服务器不能正常工作时， Sentinel 会开始一次自动故障迁移操作。
				特点：
					1、保证高可用
					2、监控各个节点
					3、自动故障迁移
					缺点：
						主从模式，切换需要时间丢数据
						没有解决 master 写的压力
		集群（proxy 型）：
			Twemproxy 是一个 Twitter 开源的一个 redis 和 memcache 快速/轻量级代理服务器； Twemproxy 是一个快速的单线程代理程序，支持 Memcached ASCII 协议和 redis 协议。
			特点：
				1、多种 hash 算法：MD5、CRC16、CRC32、CRC32a、hsieh、murmur、Jenkins 
				2、支持失败节点自动删除
				3、后端 Sharding 分片逻辑对业务透明，业务方的读写方式和操作单个 Redis 一致
				缺点：增加了新的 proxy，需要维护其高可用。
				failover 逻辑需要自己实现，其本身不能支持故障的自动转移可扩展性差，进行扩缩容都需要手动干预
		集群（直连型）：
			从redis 3.0之后版本支持redis-cluster集群，Redis-Cluster采用无中心结构，每个节点保存数据和整个集群状态,每个节点都和其他所有节点连接。
			特点：
				1、无中心架构（不存在哪个节点影响性能瓶颈），少了 proxy 层。
				2、数据按照 slot 存储分布在多个节点，节点间数据共享，可动态调整数据分布。
				3、可扩展性，可线性扩展到 1000 个节点，节点可动态添加或删除。
				4、高可用性，部分节点不可用时，集群仍可用。通过增加 Slave 做备份数据副本
				5、实现故障自动 failover，节点之间通过 gossip 协议交换状态信息，用投票机制完成 Slave到 Master 的角色提升。
				缺点：
					1、资源隔离性较差，容易出现相互影响的情况。
					2、数据通过异步复制,不保证数据的强一致性
redis介绍：
	Redis 是 C 语言开发的、一个开源（遵从 BSD 协议）、高性能、键值对（key-value）的、内存、数据库。
	特性：
		提供多种语言的 API
		高性能!
			数据在内存中，读写速度非常快，支持并发 10W QPS(每秒查询率(Query Per Second) )！。
			单进程单线程
				采用 IO 多路复用机制。
				是线程安全的
		也是一种 NoSQL（not-only sql，泛指非关系型数据库）的数据库
		可持久化
			可以将内存中数据保存在磁盘中，重启时加载。
		高可用
			主从复制、哨兵
	附：
		可以用作缓存、消息中间件等
	附：
		Redis的三个客户端框架比较：
			Jedis,Redisson,Lettuce
			Jedis：
				是 Redis 官方首选的 Java 客户端开发包。
			...
redis基础概念：
	附：
		redisObject？
			Redis 内部使用一个 redisObject 对象来表示所有的 key 和 value。

	键：
		一个键可以存储redis的一个类型
		附：
			列出所有的key
				redis> keys *
			列出匹配的key
				redis>keys apple*
				1) apple1
				2) apple2
	数据类型：
		字符串(String)：
			string类型是Redis最基本的数据类型，一个键最大能存储512MB。
			string类型是二进制安全的。即redis的string可以包含任何数据，比如jpg图片或者序列化的对象。
			数据格式: 
				key value
			附：
				二进制安全：
					https://blog.csdn.net/wuliecho/article/details/72770437
					二进制安全是指，在传输数据时，保证二进制数据的信息安全，也就是不被篡改、破译等，如果被攻击，能够及时检测出来。
					二进制安全包含了密码学的一些东西，比如加解密、签名等。
		哈希(Hash)
			Redis hash 是一个 string 类型的 field 和 value 的映射表，hash 特别适合用于存储对象。
			Redis 中每个 hash 可以存储 2的32次方（40多亿） - 1 键值对(key=>value)。
			数据格式: 
				hmsetName  
					key1 value1
					key2 value2
		列表(List)
			List是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）,左边出栈或右边出栈。
			数据格式: 
				listName  
					1 value1
					2 value2
				附：这里的1,2是指插入顺序
			操作：
				lpush、rpush、lpop、rpop、lrange（获取列表片段）...
				https://blog.csdn.net/nangeali/article/details/81735443
				在 key 对应 list 的头部添加字符串元素
					格式: lpush  name  value
				在 key 对应 list 的尾部添加字符串元素
					格式: rpush  name  value
				...
			附：
				实现方式：？
					Redis List 的实现是一个双向链表，既可以支持反向查找和遍历，更方便操作，不过带来了额外的内存开销。
				应用场景：
					可以用来当消息队列用...
		集合(Set)
			Set是string类型的无序集合，不允许重复的成员。
			附：
				集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。
			数据格式: 
				setName
					1 value1
					2 value2
					附：这里的1,2并没有任何含义，只是指返回数据排序，并不固定
		有序集合(zset(sorted set))
			zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。
			不同的是每个元素都会关联一个double类型的分数,redis正是通过分数来为集合中的成员进行从小到大的排序。
			zset的成员是唯一的,但分数(score)却可以重复。
			数据格式: 
				zsetName
					1 value1 score
					2 value2 score
			附：
				实现方式：？
					Sorted Set 的内部使用 HashMap 和跳跃表（skipList）来保证数据的存储和有序，HashMap 里放的是成员到 Score 的映射。
					而跳跃表里存放的是所有的成员，排序依据是 HashMap 里存的 Score，使用跳跃表的结构可以获得比较高的查找效率，并且在实现上比较简单。

		附：
			list set zset的区别：
				数据结构	是否允许重复元素	是否有序	有序实现方式	应用场景
				列表		是					是			索引下标		时间轴、消息队列等
				集合		否					否			无				标签、社交等
				有序集合	否					是			分值			排行榜系统、社交等
			命名空间：
				https://blog.csdn.net/HeliosJ/article/details/103258441
	有效期：
		expire(key, seconds)：
			设置key-value的有效期为seconds秒。
			例：
				expire confirm 100 设置confirm这个key100秒过期
		ttl confirm 获取confirm 这个key的有效时长
redis配置：
	https://www.runoob.com/redis/redis-conf.html
	配置 redis 外网可访问：
		https://www.runoob.com/w3cnote/redis-external-network.html
	设置密码：
		https://www.cnblogs.com/tenny-peng/p/11543440.html
		# requirepass foobared
		requirepass ***     //注意，行前不能有空格
		重启：
			./redis-cli -h 127.0.0.1 -p 6379 shutdown
			./redis-server ../redis.conf 
		验证密码：
			一旦设置密码，必须先验证通过密码，否则所有操作不可用
			auth password验证密码
			>auth 123456
redis持久化：
	持久化就是把内存的数据写到磁盘中去，防止服务宕机了内存数据丢失。
	Redis 提供了两种持久化方式:RDB（默认） 和AOF 
	RDB（Redis DataBase）：
		功能的核心函数rdbSave(生成RDB文件)和rdbLoad（从文件加载内存）两个函数
	AOF（Append-only file）:
		每当执行服务器(定时)任务或者函数时flushAppendOnlyFile 函数都会被调用， 这个函数执行以下两个工作
			aof写入保存：
			WRITE：根据条件，将 aof_buf 中的缓存写入到 AOF 文件
			SAVE：根据条件，调用 fsync 或 fdatasync 函数，将 AOF 文件保存到磁盘中。
	比较：
		1、aof文件比rdb更新频率高，优先使用aof还原数据。
		2、aof比rdb更安全也更大
		3、rdb性能比aof好
		4、如果两个都配了优先加载AOF
Redis常用命令：
	1、键值相关命令
		  keys * 取出当前所有的key
		  exists name 查看n是否有name这个key
		  del name 删除key name
		...  
		  select 0 选择到0数据库 redis默认的数据库是0~15一共16个数据库
		  move confirm 1 将当前数据库中的key移动到其他的数据库中，这里就是把confire这个key从当前数据库中移动到1中
		  persist confirm 移除confirm这个key的过期时间
		  randomkey 随机返回数据库里面的一个key
		  rename key2 key3 重命名key2 为key3
		  type key2 返回key的数据类型
	2、服务器相关命令
		  ping PONG返回响应是否连接成功
		  echo 在命令行打印一些内容
		  select 0~15 编号的数据库
		  quit  /exit 退出客户端
		  dbsize 返回当前数据库中所有key的数量
		  info 返回redis的相关信息
		  config get dir/* 实时传储收到的请求
		  flushdb 删除当前选择数据库中的所有key
		  flushall 删除所有数据库中的数据库
	setnx(key, value)：“set if not exits”，若该key-value不存在，则成功加入缓存并且返回1，否则返回0。
	get(key)：获得key对应的value值，若不存在则返回nil。
	getset(key, value)：先获取key对应的value值，若不存在则返回nil，然后将旧的value更新为新的value。
Redis的内存淘汰策略：
	https://zhuanlan.zhihu.com/p/105587132
	过期策略
		Redis中同时使用了惰性过期和定期过期两种过期策略。
		定期删除
			redis 会将每个设置了过期时间的 key 放入到一个独立的字典中，以后会定期遍历这个字典来删除到期的 key。
			Redis 默认会每秒进行十次过期扫描（100ms一次），过期扫描不会遍历过期字典中所有的 key，而是采用了一种简单的贪心策略。
				1.从过期字典中随机 20 个 key；
				2.删除这 20 个 key 中已经过期的 key；
				3.如果过期的 key 比率超过 1/4，那就重复步骤 1；
			redis默认是每隔 100ms就随机抽取一些设置了过期时间的key，检查其是否过期，如果过期就删除。注意这里是随机抽取的。为什么要随机呢？你想一想假如 redis 存了几十万个 key ，每隔100ms就遍历所有的设置过期时间的 key 的话，就会给 CPU 带来很大的负载。
		惰性删除
			所谓惰性策略就是在客户端访问这个key的时候，redis对key的过期时间进行检查，如果过期了就立即删除，不会给你返回任何东西。
	为什么需要淘汰策略
		因为redis的过期策略不是精准的删除(会存在key没有被删除掉的场景),且Redis有内存大小限制，
		那在内存用完的时候，还继续往Redis里面添加数据不就没内存可用了会出现异常，因此Redis定义了几种内存淘汰策略用来处理这种情况。
		附：
			如果不设置最大内存大小或者设置最大内存大小为0，在64位操作系统下不限制内存大小，在32位操作系统下最多使用3GB内存
	内存淘汰策略
		1. noeviction(默认策略)：当内存使用超过配置的时候会返回错误，不会驱逐任何键
		2. allkeys-lru：加入键的时候，如果过限，首先通过LRU算法驱逐最久没有使用的键
		3. volatile-lru：加入键的时候如果过限，首先从设置了过期时间的键集合中驱逐最久没有使用的键
		4. allkeys-random：加入键的时候如果过限，从所有key随机删除
		5. volatile-random：加入键的时候如果过限，从过期键的集合中随机驱逐
		6. volatile-ttl：从配置了过期时间的键中驱逐马上就要过期的键
		7. volatile-lfu：从所有配置了过期时间的键中驱逐使用频率最少的键
		8. allkeys-lfu：从所有键中驱逐使用频率最少的键