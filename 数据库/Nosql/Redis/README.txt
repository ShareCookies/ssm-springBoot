官网：
	https://redis.io/
	http://www.redis.cn
	https://github.com/antirez/redis
教程：
	https://www.runoob.com/redis/redis-commands.html
	https://blog.csdn.net/hellozpc/article/details/81267030
redis 和 memcached 对比：
	1. redis支持更丰富的数据类型。
	memcache支持简单的数据类型key-value(String、二进制(新版增加))。!
	5. redis支持更多的操作命令。
		redis
		批量操作
			事务支持[虽然是假的事务]
			每个类型不同的CRUD
		Memcached
			CRUD
			少量的其他命令
	3. 集群模式：
		但是 redis 原生支持 cluster 模式.
		memcached没有原生的集群模式，需要依靠客户端来实现往集群中分片写入数据；
	
	4. redis同设备性能更好。
		Redis使用单线程的多路 IO 复用模型。
		Memcached是多线程，非阻塞IO复用的网络模型；？
	
	2. Redis支持数据的持久化.
		而Memecache把数据全部存在内存之中。

redis介绍：
	Redis(Remote Dictionary Server)  是 C 语言开发的、一个开源（遵从 BSD 协议）、高性能、键值对（key-value）的、内存数据库。
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
					因为 Redis 完全是基于内存的操作，CPU 不是 Redis 的瓶颈，Redis的瓶颈最有可能是机器内存的大小或者网络带宽。
					因此就使用了单线程。
						单线程更简单.
						避免了不必要的上下文切换和竞争条件等性能消耗。
					
					：
						如果基于磁盘，那么由于磁盘io读取的缓慢，单进程单线程程序就得等待数据的读取，那么此时就会阻塞效率低下
		高可用
			主从、主从哨兵、集群
			
		一种 NoSQL（not-only sql，泛指非关系型数据库）的数据库
		提供多种语言的 API
		可持久化
			可以将内存中数据保存在磁盘中，重启时加载。
		
		支持lua脚本：
			Lua 是一种轻量小巧的脚本语言，用标准C语言编写并以源代码形式开放， 其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能。
	附：
		redis用途：
			可以用作缓存、消息中间件等
redis基础概念：
	./教程/redis基础概念.txt
redis安装：
	./linux安装redis.txt
redis客户端：
	Redis 支持的 Java 客户端都有哪些：
		Redisson、Jedis、lettuce 等等
		官方推荐使用 Redisson。

	Redisson：
		Redisson是一个高级的分布式协调 Redis 客服端，能帮助用户在分布式环境中轻松实现一些Java 的对象 (Bloom filter, BitSet, Set, SetMultimap, ScoredSortedSet, SortedSet, Map, ConcurrentMap, List, ListMultimap, Queue, BlockingQueue, Deque, BlockingDeque, Semaphore, Lock, ReadWriteLock, AtomicLong, CountDownLatch, Publish / Subscribe, HyperLogLog) 。

	Jedis 与 Redisson 对比：
		Jedis 是 Redis 的 Java 实现的客户端，其 API 提供了比较全面的 Redis 命令的支持；

		Redisson 实现了分布式和可扩展的 Java 数据结构，和 Jedis 相比，功能较为简单，不支持字符串操作，不支持排序、事务、管道、分区等 Redis 特性。
		Redisson 的宗旨是促进使用者对 Redis 的关注分离，从而让使用者能够将精力更集中地放在处理业务逻辑上。
Spring Boot 使用 Redis:
	./spring/
Redis集群：
	./集群/
Redis的内存淘汰策略:
	./教程/Redis内存淘汰策略.txt
redis持久化：
	./教程/redis持久化.txt
redis事务：
	./教程/
注1：
	Redis常用命令：
		./教程/redis常用命令.txt
	redis的应用场景：
		./redis应用.txt

