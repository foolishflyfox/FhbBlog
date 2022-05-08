
1. 解决功能性的问题：Java、Jsp、RDBMS、Tomcat、HTML、Linux、JDBC、SVN
2. 解决扩展性问题：Structs、Spring、SpringMVC、Hibernate、MyBatis
3. 解决性能问题：NoSQL、Java 线程、Hadoop、Nginx、MQ、ElasticSearch

- 库操作
    - 清空库: `flushdb`
- 查看有哪些 key: `keys *`

## Redis 介绍相关知识

redis 端口 6379 的来源，对应 Merz。

默认16个数据库，类似数组，从0开始，初始默认使用 0 号库。

使用命令 `select <dbid>` 来切换数据库。例如 `select 8`。统一密码管理，所有库使用同样的密码。

与 Memcache 三点不同：支持多数据类型、支持持久化、单线程+多路复用。

## Redis 数据类型

### Redis 字符串 (String)

String 是 Redis 中最基本的类型，你可以理解成与 Memcached 一模一样的类型，一个 key 对应一个 value。

String类型是二进制安全的。意味着 Redis 的 string 可以包含任何数据，比如 jpg 图片或者序列化的对象。

String 类型是 Redis 最基本的数据类型，一个 Redis 中字符串 value 最多可以是 512 M。

常用命令：
- `set <key> <value>`
- `get <key>`
- `append <key> <value>`: 追加
- `strlen <key>`: 获得值的长度
- `setnx <key> <value>`: set if not exist
- `incr <key>`: 如果 key 对应的是整数，则加1
- `decr <key>`: 如果 key 对应的是整数，则减一
- `incrby <key> <increment>`: 加指定值
- `decrby <key> <decrement>`: 减指定值
- `mset key value [key value ....]`: 批量设置 key value
- `mget key [key ...]`: 批量获取多个 key 的值
- `msetnx key value [key value ...]`: 如果不存在才设置，是原子性的，有一个失败则都失败
- `getrange key start end`: 获取范围的值，前包、后包
- `setrange key offset value`: 设置指定位置开始的值
- `setex key seconds value`: 设置key的存活时间
- `ttl key`: 指定 key 的存活时间
- `getset key value`: 重新设置值，返回原值
- `del key [key ....]`: 删除指定 key

原子性：所谓原子操作是指不会被线程调度机制打断的操作。

这种操作一旦开始，就一直运行到结束，中间不会有任何 context switch(切换到另一个线程)。
1. 在单线程中，能够在单条指令中完成的操作都可以认为是“原子操作”，因为中断只能发生在指令之间；
2. 在多线程中，不能被其他进程(线程)打断的操作就叫原子操作。

Redis 单命令的原子性主要得益于 Redis 的单线程。

**数据结构**：String 的数据结构为简单动态字符串(Simple Dynamic String, SDS)。是可以修改的字符串，内部结构实现上类似 Java 的 ArrayList，采用预分配冗余空间的方式来减少内存的频繁分配。

当字符串长度小于 1M 时，扩容都是加倍现有空间，如果超过 1M，扩容时一次只会多扩 1M 的空间。注意：字符串最大长度为 512M。

### Redis 列表(List)

单键多值。Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部或尾部。它的底层实际上是一个双向链表，对两端的操作性能很高，通过索引下标的操作中间节点的性能比较差。

常用操作：
- `lpush key element [element ...]`
- `rpush key element [element ...]`
- `lrange key start stop`: 从 start 开始到 stop 的元素，0 表示左边第一个，-1表示右边第一个，-2 表示右边第二个元素，以此类推；
- `lpop key [count]`
- `rpop key [count]`
- `rpoplpush sourceKey destinationKey`: 从 sourceKey 列表右边吐出一个值，插到 destinationKey 的左边
- `lindex key index`: 获取指定位置元素
- `llen key`: 求数组长度
- `linsert key BEFORE|AFTER pivot element`: element 表示数组中一个元素的值
- `lrem key count element`: 从左边开始删除count个element指定的元素
- `lset key index element`: 在指定位置的值设置为 element

值在键在，值光键亡。

**数据结果**

List 的数据结构为快速链表 quickList。首先在列表元素较少的情况下会使用一块连续的内存存储，这个结构是 ziplist，也就是压缩列表。

当数据比较多的时候才会改用 quickList。因为普通链表需要的附加指针空间太大，会比较浪费空间。

Redis 将链表和 ziplist 结合起来组成了 quicklist，也就是将多个 ziplist 使用双向指针串起来使用，这就既满足了快速地插入删除性能，又不会出现太大的空间冗余。

### Redis 集合

Redis set 对外提供的功能与 list 类似，是一个列表的功能，特殊之处在于 set 是自动去重的。并且 set 提供了判断某个成员是否在一个 set 集合内的重要接口，这个也是 list 所不能提供的。

Redis 的 Set 是 string 类型的无序集合，它底层其实是一个 value 为 null 的hash 表，所以添加、删除和查找的时间复杂度都是 O(1)。

常用命令：
- `sadd key member [member ...]`
- `smembers key`
- `sismember key member`: 判断 member 是否为 key 的成员，是返回1，否返回0
- `srem key member [member ...]`
- `spop key`
- `srandmember key [count]`: 随机取得 count 个元素
- `smove source destination member`: 把集合 source 中的 member 加到 destination 中
- `sinter key [key ...]`: 求交集
- `sunion key [key ...]`: 求并集
- `sdiff key [key ...]`: 求差集

Java 中的 HashSet 的内部实现使用的是 HashMap，只不过所有的 Value 都指向同一个对象。Redis 的 set 结构也是一样，它的内部也使用 hash 结构，所有的 value 都指向同一个内部值。

### Redis 哈希(Hash)

Redis hash 是一个键值对集合。Redis hash 是一个 string 类型的 field 和 value 的映射表，hash 特别适合存储对象。类似 Java 中的 `Map<String, Object>`。

命令：
- `hset key field value [field value ...]`
- `hget key field`
- `hexists key field`
- `hkeys key`: 查看 key 有哪些域
- `hvals key`
- `hsetnx key field value`
- `hgetall key`

数据结构：Hash 类型对应的数据结构有两种，ziplist(压缩列表)，hashtable(哈希表)。当 field-value 长度较短，且个数较少时，使用 ziplist，否则使用 hashtable。

### Redis 有序集合 Zset (Sorted set)

Redis 有序集合 zset 与普通集合 set 非常相似，是一个没有重复元素的字符串集合。

不同之处是有序集合中的每个成员都关联了一个评分(score)，这个评分被用来按从低到高的方式排序集合中的成员，集合中的成员是唯一的，但是评分是可以重复的。

访问有序集合中的中间元素非常快，因此你能够使用有序集合作为一个没有重复成员的智能列表。

命令：
- `zadd key score member [score member ...]`
- `zrange key min max`
- `zrangebyscore key min max`
- `zrevrangebyscore key min max`
- `zincrby key increment member`
- `zrem key member`
- `zcount key min max`: 查询指定分数内的数量
- `zrank key member`

SortedSet (zset) 是 Redis 提供的一个非常特别的数据结构，一方面它等价于 java 的数据结构 `Map<String, Double>`，可以给每个元素 value 赋予一个权重 score，另一方面它又类似于 TreeSet，内部的元素会按照权重 score 进行排序，可以得到每个元素的名次，还可以通过 score 的范围来获取元素列表。

zset 底层使用了两个数据结构：
- hash，hash 的作用就是关联元素 value 和权重 score，保障元素 value 的唯一性，可以通过元素 value 找到相应的 score 值。
- 跳跃表，跳跃表的目的在于给元素 value 排序，根据 score 的范围获取元素列表。


## Redis 的发布和订阅

### 什么是发布和订阅

Redis 发布订阅(pub/sub)是一种消息通信模式：发送者(pub)发送消息，订阅者(sub)接收消息。

Redis 客户端可以订阅任意数量的频道。

## Redis 约定

### key 的设置约定

- 数据库中的热点数据 key 命名惯例
    - `表名:主键名:主键值:字段名`

## Redis 使用场景

- 指定时间后可以再次投票
    - 设置过期时间 `setex`
- mysql 数据库id
    - 自增 `incr / incrby`
- 购物车场景
    - 以客户 id 作为 key，每个客户创建一个 hash 存储结果存储对应的购物车信息
    - 将商品 id 作为 field，购买数量作为 value 进行存储
    - 添加商品：追加全新的 field 与 value
    - 浏览：遍历 hash
    - 更改数量：自增、自减，设置 value 值
    - 删除商品：删除 field
    - 清空：删除 key