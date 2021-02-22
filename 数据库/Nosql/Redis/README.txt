官网：
	https://redis.io/
	http://www.redis.cn
	https://github.com/antirez/redis
教程：
	https://www.runoob.com/redis/redis-commands.html
	https://blog.csdn.net/hellozpc/article/details/81267030

redis介绍：
	Redis 是 C 语言开发的、一个开源（遵从 BSD 协议）、高性能、键值对（key-value）的、内存、数据库。
	特性：
		高性能
			数据在内存中，读写速度非常快，官方数据支持 10W QPS(每秒内的查询次数(Query Per Second) )！。
			单进程单线程
				采用 IO 多路复用机制。
					判断数据是否已到达没的话先去读别的，可redis数据不是在内存中吗!
					所以这里的io复用应该是对请求，这也是redis为什么能单机承受10w并发原因之一吧！

				是线程安全的
			附
				为什么redis是单线程：
					因为 Redis 完全是基于内存的操作，CPU 不是 Redis 的瓶颈，Redis 的瓶颈最有可能是机器内存的大小或者网络带宽。
					因此就使用了单线程。
						单线程更简单.
						避免了不必要的上下文切换和竞争条件等性能消耗。
		高可用
			主从复制、哨兵
		也是一种 NoSQL（not-only sql，泛指非关系型数据库）的数据库
		提供多种语言的 API
		可持久化
			可以将内存中数据保存在磁盘中，重启时加载。

	附：
		Redis的三个客户端框架比较：
			Jedis,Redisson,Lettuce
			Jedis：
				是 Redis 官方首选的 Java 客户端开发包。
			...
		redis用途：
			可以用作缓存、消息中间件等
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

redis安装：
	./linux安装redis.txt
Spring Boot 使用 Redis:
	./spring/	
Redis集群：
	./集群/

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
		1. noeviction(无受害者no eviction)(默认策略)：当内存使用超过配置的时候会返回错误，不会驱逐任何键
		2. allkeys-lru(Least Recently Used最近最少使用)：加入键的时候，如果过限，首先通过LRU算法驱逐最久没有使用的键
			Least Recently Used 是一种常用的页面置换算法，选择最近最久未使用的页面予以淘汰。？
		3. volatile-lru：加入键的时候如果过限，首先从设置了过期时间的键集合中驱逐最久没有使用的键
		4. allkeys-random：加入键的时候如果过限，从所有key随机删除
		5. volatile-random：加入键的时候如果过限，从过期键的集合中随机驱逐
		6. volatile-ttl：从配置了过期时间的键中驱逐马上就要过期的键
		7. volatile-lfu：从所有配置了过期时间的键中驱逐使用频率最少的键
		8. allkeys-lfu：从所有键中驱逐使用频率最少的键



注1：
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
		3. 批处理命令：
			...
	redis的应用场景：
		./redis应用.txt
附：
	pipeline：?
		redis的pipeline可以一次性发送多个命令去执行，在执行大量命令时，可以减少网络通信次数提高效率。
		redis集群使用pipeline：
			jedisCluster（redis的集群连接api）并不支持pipeline语法（只是不提供相应的方法而已）。
			不过只要稍稍看下jedis的源码，就可以发现虽然没有现成的轮子，但是却很好造。
			造轮子：
				https://blog.csdn.net/weixin_30765577/article/details/97804057?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_v2~rank_v29-1-97804057.nonecase&utm_term=redis%E9%9B%86%E7%BE%A4%E4%BD%BF%E7%94%A8pipeline&spm=1000.2123.3001.4430
				https://my.oschina.net/u/4554374/blog/4306457
	redis持久化：
		1. Redis 为了保证效率，数据缓存在了内存中，但是会周期性的把更新的数据写入磁盘(或者把修改操作写入追加的记录文件中)，以保证数据的持久化。
			附：
			持久化：持久化就是把内存的数据写到磁盘中去，防止服务宕机了内存数据丢失。
		2. 当 Redis 重启的时候，它会优先使用 AOF 文件来还原数据集，因为 AOF 文件保存的数据集通常比 RDB 文件所保存的数据集更完整。
		！
			默认不是rdb，同时有是，是aof吧
		附：
			你也可以关闭持久化功能，让数据只在服务器运行时存。
		
		Redis 提供了两种持久化方式:RDB（默认） 和AOF
			Redis 默认是快照 RDB 的持久化方式。
			RDB（Redis DataBase）：
				RDB：快照形式是直接把内存中的数据保存到一个dump.rdb(二进制文件)的文件中，定时保存，保存策略。
				功能的核心函数rdbSave(生成RDB文件)和rdbLoad（从文件加载内存）两个函数
				
				RDB工作原理：
					...

			AOF（Append-only file）:
				AOF：把所有的对 Redis 的服务器进行修改的命令都存到一个文件里(命令的集合)。
				每当执行服务器(定时)任务或者函数时flushAppendOnlyFile 函数都会被调用， 这个函数执行以下两个工作
					aof写入保存：
					WRITE：根据条件，将 aof_buf 中的缓存写入到 AOF 文件
					SAVE：根据条件，调用 fsync 或 fdatasync 函数，将 AOF 文件保存到磁盘中。
				AOF工作原理：
					...
			附
				比较：
					1、aof文件比rdb更新频率高，优先使用aof还原数据。
					2、aof比rdb更安全也更大
					3、rdb性能比aof好
					4、如果两个都配了优先加载AOF
					...
					要使用那个了！
						https://baijiahao.baidu.com/s?id=1660009541007805174&wfr=spider&for=pc