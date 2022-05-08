# Redis

## Redis 快速入门

Redis 是键值型数据库。

### 初识 Redis

#### 认识 NoSQL

||SQL|NoSQL|
|--|---|---|
|数据结构|结构化(Structured)|非结构化|
|数据关联|关联的(Relational)|无关联的|
|查询方式|SQL 查询|非 SQL|
|事务特性|ACID|BASE|
|存储方式|磁盘|多数在内存|
|扩展性|垂直|水平|
|使用场景|1.数据结构固定<br/>2.相关业务对数据安全性、<br/>一致性要求高|1. 数据结构不固定<br/>2. 对一致性、安全性要求不高<br/>3. 对性能要求|

结构化是指表的结构定义，在插入数据之前就要定义各个字段的约束。

#### 认识 Redis

Redis 诞生于 2009 年，全称为 Remote Dictionary Server，远程字典服务，是一个基于内存的键值型 NoSQL 数据库。

特征：
- 键值(KEY-VALUE)型，value 支持多种不同数据结构，功能丰富。
- 单线程，每个命令具备原子性。
- 低延迟、速度快(基于内存、IO多路复用、良好的编码)。
- 支持数据持久化(定期持久化)。
- 支持主从集群(读写分离，提高可用性，提高效率)、分片集群(提高可存储数据量【水平扩展】，提高效率)
- 支持多语言客户端

#### Redis 安装与配置

- redis 安装
    - 从 redis.io 下载 redis 的源码压缩包 https://download.redis.io/releases/redis-6.2.7.tar.gz 到 */usr/local/src* 记录
    - 因为 redis 是 C 语言编写的，因此如果想要编译 redis 源码，需要安装相应的编译器 gcc
    - 解压，通过 `make && make install` 安装 redis

- redis.conf 文件的配置在
    - `bind`: 改为 0.0.0.0 或注释掉，表示任意 IP 可以访问
    - `daemonize`: 改为 yes，表示后台运行
    - `requirepass`: 设置密码
    - `port`: 表示监听的端口
    - `dbfilename`: 表示 redis 存放数据文件的名称，默认为 *dump.rdb*
    - `dir`: 工作目录，默认是当前目录 ./，与 *redis-server* 在同一个目录
    - `database`: 指定库的数量，设置为 1，表示只用1个库，默认有16个库，编号 0-15
    - `maxmemory`: 设置 redis 能够使用的最大内存，例如 `maxmemory 512mb`
    - `logfile`: 日志文件，默认为空，不记录日志，可以指定日志文件名
    - `stop-writes-on-bgsave-error yes`: 当 Redis 无法写入磁盘，直接关闭 Redis 的写操作，避免数据丢失
    - `rdbcompression yes`: 持久化文件允许压缩。对于存储到磁盘中的快照，可以设置是否进行压缩，如果是的话，redis 会采用 LZF 算法进行压缩。如果你不想消耗 CPU 来压缩的话，可以设置关闭此功能
    - `rdbchecksum yes`: 检查完整性，在存储快照后，还可以让 redis 使用 CRC64 算法进行数据校验，但是这样会增加大约 10% 的性能消耗，如果希望获取到最大的性能提升，可以关闭此功能，推荐 yes
    - `save <seconds> <changes>`: RDB 是整个内存压缩过的 snapshot，可以配置符合的快照触发条件，`save 60 10000` 表示1分钟内改了1w次，`save 300 100` 表示5分钟内改了100次，`save 3600 1` 表示1小时内改了1次，上述为默认配置。
    - `appendonly no`: 是否开始 AOF，默认不开启。
    - `appendfilename "appendonly.aof"`: AOF 文件名。
    - `appendfsync`
        - `appendfsync everysec`: 每秒同步，每秒记入日志一次，如果宕机，本秒的数据可能丢失
        - `appendfsync always`: 始终同步，每次 Redis 的写入都会立刻记入日志，性能较差，但数据完整性比较好；
        - `appendfsync no`: redis 不主动同步，将同步时机交给操作系统；
    
#### 安装建议

- 安装目录: */usr/local/bin/*
- 配置文件: */usr/local/etc/redis.conf*
    - 修改存放数据目录: `dir /usr/local/data/redis`
- 数据目录: */usr/local/data/redis/* ，该目录存放数据文件 *dump.rdb*，日志文件 *redis.log* 等
- 创建文件 */usr/local/bin/redis* ，添加如下命令，并将其设为可执行(`chmod u+x /usr/local/bin/redis`)，之后就可以通过 `sudo redis` 启动 redis 服务了。
```sh
#/bin/bash
/usr/local/bin/redis-server /usr/local/etc/redis.conf
```

#### 开机自启动

1. 新建一个系统服务文件：`vi /etc/systemd/system/redis.service`

内容如下：
```
[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/bin/redis-server /usr/local/src/redis-6.2.7/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

2. 重载系统服务 `systemctl daemon-reload`，表示 redis 纳入到系统管理中
    - 通过 `systemctl start redis` 可以启动 redis-server 服务
    - 通过 `systemctl status redis` 可以查看 redis-server 服务的状态
    - 通过 `systemctl stop redis` 可以关闭 redis-server 服务
    - 通过 `systemctl restart redis` 可以重启 redis-server 服务

3. 通过 `systemctl enable redis` 设置 redis 开机自启动，输出如下内容：`Created symlink /etc/systemd/system/multi-user.target.wants/redis.service → /etc/systemd/system/redis.service.`

### Redis 客户端

安装完成 Redis，我们可以通过 Redis 客户端进行操作。包括：
- 命令行客户端
- 图形化桌面客户端
- 编程客户端

#### Redis 命令行客户端

使用命令 `redis-cli [options] [commands]`。

常见的 options 有:
- `-h 127.0.0.1`: 指定要连接的 redis 节点的 ip 地址，默认为 127.0.0.1；
- `-p 6379`: 指定要连接的 redis 节点的端口，默认是 6379；
- `-a 123321`: 指定 redis 的访问密码。也可以在连接后，通过 `auth 123321` 命令指定密码。

commonds 就是 Redis 的操作命令，例如：
- `ping`: 与 redis 服务端做心跳测试，服务端正常会返回 pong

#### Redis GUI 客户端

redis-desktop-manager 可以从 https://www.macwk.com/ 下载。

## Redis 常见命令

### Redis 数据结构介绍

Redis 是一个 key-value 的数据库，key 一般是 String 类型，不过 value 的类型多种多样。

- 基本类型
    - String: hello world
    - Hash: {name: "Jack", age: 21}
    - List: [A -> B -> C -> C]
    - Set: {A, B, C}
    - SortedSet: {A:1, B:2, C:3}
- 特殊类型
    - GEO: {A:(120.3, 30.5)}
    - BitMap: 011011010100110101
    - HyperLog: 011011010100110101

### Redis 命令

#### 通用命令

帮助 `help @generic`。

常用命令：
- `keys pattern`: 查看符合模板的所有 key。不建议在生产环境设备上使用。
- `del key [key ...]`
- `exists key [key ...]`
- `expire key seconds`: 给一个 key 设置有效期，到期后被删除
- `ttl key`: 查看一个 key 的剩余有效期
- `info replication`: 查看分区信息
- `slaveof host port`: 成为指定主机的从机   

#### String 类型

String 类型，也就是字符串类型，是 Redis 中最简单的存储类型。其 value 是字符串，不过根据字符串的格式不同，又可以分为 3 类。
- string: 普通字符串
- int: 整数类型，可以做自增、自减操作
- float: 浮点类型，可以做自增、自减操作

不管是哪种格式，底层都是字节数组形式存储，只不过编码方式不同。字符串类型的最大空间不超过 512m。

命令：
- `set`: 添加或修改已经存在的一个 String 类型的键值对
- `get`: 获取值
- `mset`: 批量添加或修改
- `mget`: 批量获取值
- `incr`: 指定 key 自增
- `incrby`: 指定 key 自增指定值
- `incrbyfloat`: 指定 key 视为小数，自增指定值
- `setnx`: set no exist
- `setex`: set and expire

**key 的结构**：Redis 的 key 允许有多个单词形成层级结构，多个单词之间用 `:` 隔开。例如：`项目名:业务名:类型:id`。

这个格式并非固定，也可以根据自己的需求来删除或添加词条。

例如，我们的项目名称叫 heima，有 user 和 product 两种不同类型的数据，我们可以这样定义 key：
- user 相关的 key: `heima:user:1`
- product 相关的 key: `heima:product:1`

#### hash 类型

Hash 类型，也叫散列，其 value 是一个无序字典，类似于 Java 中的 HashMap 结构。

String 结构是将对象序列化为 JSON 字符串后存储，当需要修改对象某个字段时很不方便。Hash 结构可以将对象中的每个字段独立存储，可以针对单个字段进行 CRUD。

Hash 类型的常见命令：
- `hset key field value`: 添加或修改hash类型key的field值
- `hget key field`: 获取一个 hash 类型 key 的field 值
- `hmget key field [field ...]`: 获取多个字段值
- `hgetall key`: 获取一个 hash 类型的 key 中的所有 field
- `hkeys key`: 获取一个 hash 类型的 key 中的所有 field
- `hvals key`: 获取一个 hash 类型的 key 中的所有 value
- `hincrby key field increment`: 让一个 hash 类型 key 的字段自增并指定步长
- `hsetnx key field value`: 如果 key 的 field 字段不存在，则设置该字段的值

废弃的命令：
- hmget: As of Redis version 4.0.0, this command is regarded as deprecated. It can be replaced by HSET with multiple field-value pairs when migrating or writing new code.
#### list 类型

Redis 中的 List 类型与 Java 中的 LinkedList 类似，可以看作是一个双向链表结构。既可以支持正向索引，也可以支持反向检索。

特征业余 LinkedList 类似：
- 有序
- 元素可以重复
- 插入和删除快
- 查询速度一般

List 常见命令：
- `lpush`: 向列表左侧插入一个或多个元素
- `rpush`: 向列表右侧插入一个或多个元素
- `lpop`: 移除并返回列表左侧的第一个元素，没有返回 nil
- `rpop`: 移除并返回列表右侧的第一个元素，没有返回 nil
- `lrange`: 返回一段角标范围内的所有元素
- `blpop` 和 `brpop`: 与 lpop 和 rpop 类似，只不过 b 表示 block，在没有元素时等待指定时间，而不是直接返回 nil

#### set 类型

Redis 的 Set 类型与 Java 中的 HashSet 类型，可以看作是一个 value 为 null 的 HashMap。因为也是一个 hash 表，因此具备与 HashSet 类似的特征。

- 无序
- 元素不可重复
- 查找快
- 支持交集、并集、差集等功能

set 的常见命令：
- `sadd key member ...`: 向 set 中添加元素
- `srem key member ...`: 移除 set 中的指定元素
- `scard key`: 获取 set 中元素数量
- `sismember key member`: 判断一个元素是否存在于 set 中
- `smembers key`: 获取 set 中的元素
- `sinter key1 key2 ...`: 求多个集合的交集
- `sunion key1 key2 ...`: 求多个集合的并集
- `sdiff key1 key2 ..`: 求多个集合的差集

#### SortedSet 类型

Redis 的 SortedSet 是一个可排序的 set 集合，与 java 中的 TreeSet 类似，但底层数据结构差别很大。SortedSet 中的每一个元素都带有一个 score 属性，可以基于 score 属性对元素排序，底层的实现是一个跳表(SkipList)加hash表。

SortedSet 具备以下特性：
- 可排序
- 元素不重复
- 查询速度快

因为 SortedSet 的可排序特性，经常被用来实现排行榜这样的功能。

- `zadd key score member`: 添加或修改元素
- `zrem key member`: 删除元素
- `zscore key member`: 查询分数
- `zrank key member`: 查询排名
- `zcard key`: 查询元素个数
- `zcount key min max`: 统计 score 值在指定范围内的所有元素个数
- `zincrby key increment member`: 自增
- `zrange key min max`: 按 score 排序后，获取指定排名范围内的元素
- `zrangebyscore key min max`: 按 score 排序后，获取 score 范围内的元素
- `zdiff` / `zinter` / `zunion`

降序在 `z` 后加 `rev`。

### Redis 新数据类型

#### Bitmaps

命令：
- `setbit key offset value`

实例：每个独立用户是否访问过网站存放在 Bitmaps 中。将访问过的用户标记为1，没有访问过的用户标记为0，用偏移量作为用户的 id。

设置键的第 offset 个位的值。假设有 20 个用户，userid = 1,6,11,5,19的用户对网站进行了访问，命令为：
```
setbit user:20220507 1 1
setbit user:20220507 6 1
setbit user:20220507 11 1
setbit user:20220507 5 1
setbit user:20220507 19 1
```
注意：
- 很多应用的用户 id 以一个指定数字开头(例如10000)开头，直接将用户 id 和 Bitmaps 的偏移量对应会造成一定的浪费，通常的做法是每次做 setbit 操作时将用户 id 减去这个指定数字。
- 在第一次初始化 Bitmaps 时，如果偏移量非常大，那么整个初始化过程会比较慢，可能造成 redis 阻塞

- `getbit key offset`: 取出存放的值
- `bitcount key [start end]`: 统计从 start 字节到 end 字节比特值为 1 的数量。-1 表示最后一个字节。
- `bitop operation destkey key [key ...]`: operation 包括 AND, OR, XOR 和 NOT

#### HyperLogLog

在工作中，我们经常会遇到与统计相关的功能，比如统计网站的 PV(Page View 页面访问量)，可以使用 Redis 的 incr、incrby 轻松实现。

但是像 UV(Unique Visitor，独立访客)、独立 IP 数、搜索记录数等需要去重合计数的问题如何解决？这种求集合中不重复元素个数的问题称为基数问题。

解决基数问题有很多方案：
1. 数据存储在 MySQL 表中，使用 distinct count 计算不重复个数
2. 使用 Redis 提供的 hash、set、bitmaps 等数据结构来处理

以上的方案结果精确，但随着数据不断增加，导致占用空间越来越大，对于非常大的数据集是不切实际的。

Redis 推出了 HyperLogLog，能够降低一定的精度来减少存储压力。

Redis HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是：在输入元素的数量体积非常非常大时，计算基数所需的空间总是固定的、并且是很小的。

在 Redis 中，每个 HyperLogLog 键只需花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基数。

因为 HyperLogLog 只会根据输入元素来计算基数，而不会存储输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。

什么是基数？例如数据集 {1,3,5,7,5,7,8}，那么这个数据集的基数集为 {1,3,5,7,8}，基数为5。基数估计就是在误差可接受的范围内，快速计算基数。

命令：
- `pfadd key element [element ...]`: 添加元素
- `pfcount key`: 统计不重复元素个数
- `pfmerge destkey sourcekey [sourcekey ...]`: 合并基数集合

#### Geospatial

Redis 3.2 中增加了对 GEO 类型的支持。GEO，Geographic，地理信息的缩写，该类型就是元素的二维坐标，在地图上就是经纬度。redis 基于该类型，提供了经纬度设置、查询、范围查询、距离查询、经纬度 Hash 等常见操作。

命令：
- `geoadd key 经度 纬度 城市名`
- `geopos key 城市名`
- `geodist key 城市1 城市2` 
- `georadius key 经度 纬度 半径`: 取指定地点半径内的城市

### Redis 的发布和订阅

#### 什么是发布与订阅

Redis 发布订阅(pub/sub)是一种消息通信模式：发送者(pub)发送消息，订阅者(sub)接收消息。

Redis 客户端可以订阅任意数量的频道。
![redis 订阅与发布](http://assets.processon.com/chart_image/62762aebe0b34d07586244fe.png)

#### 使用发布和订阅

1. 打开一个客户端订阅 channel1: `subscribe channel1`
```
ubuntu-01:6379> subscribe channel1
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "channel1"
3) (integer) 1
```
2. 另一个客户端发布消息: `publish channel1 hello`，第一个客户端将收到数据。

#### redis vs kafka

redis 和 kafka 都有消息发布与订阅。两者的区别如下：

- Redis 支持基于推送的消息传递，这意味着推送到 Redis 的每条消息都将自动传递给所有订阅者。而 Kafka 支持基于拉式的消息，客户端可以根据自己的消费能力主动拉取数据。
- Redis 不提供消息的持久化，不能多次消费同一条消息。Kafka 可以通过设置保留策略设置消息保留时间，并且通过重置消费位点实现消息的多次消费。


### Redis 的 Java 客户端

Jedis: 以 Redis 命令作为方法名称，学习成本低，简单实用。但是 Jedis 实例是线程不安全的，多线程环境中需要基于连接池来使用。
Lettuce：Lettuce 是基于 Netty 实现的，支持同步、异步和响应式编程方式，并且是线程安全的。支持 Redis 的哨兵模式、集群模式和管道模式。
Redisson: Redisson 是一个基于 Redis 实现的分布式、可伸缩的 Java 数据结构集合。包含了诸如 Map、Queue、Lock、Semaphore、AtomicLong 等强大功能。

### Jedis

Jedis 的官网地址：https://github.com/redis/jedis 。

#### Jedis 基本使用

1. 引入依赖
```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.2.0</version>
</dependency>
```

2. 建立连接
```java
    private Jedis jedis = null;

    @Before
    void setUp() {
        // 建立连接
        jedis = new Jedis("ubuntu-01", 6379);
        // 设置密码
        jedis.auth("12345678");
        // 选择库
        jedis.select(0);
    }
```

3. 测试 String

```java
    @Test
    public void testString() {
        // 插入数据，方法名称就是 redis 命令名称
        String result = jedis.set("name", "zhangsan");
        System.out.println("result = " + result);
        // 获取数据
        String name = jedis.get("name");
        Assert.assertEquals("zhangsan", name);
    }
```

4. 释放资源

```java
    @After
    public void tearDown() {
        if (null != jedis) {
            jedis.close();
            jedis = null;
        }
    }
```

#### Jedis 连接池

Jedis 本身是线程不安全的，并且频繁创建和销毁连接会有性能损耗，因此推荐使用 Jedis 连接池替代 Jedis 的直连方式。

```java
public class JedisConnectionFaction {
    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大连接，表示连接池中最多能有多少个连接
        jedisPoolConfig.setMaxTotal(8);
        // 最大空闲连接，表示处于空闲状态的连接数量的上限
        jedisPoolConfig.setMaxIdle(8);
        // 最小空闲连接，如果连接长时间处于空闲就会被释放，该值设置被释放后最少留多少空闲连接
        jedisPoolConfig.setMinIdle(0);
        // 设置最长等待时间, ms。表示连接池中没有连接可用，最长等待多久
        jedisPoolConfig.setMaxWaitMillis(2000);
        jedisPool = new JedisPool(jedisPoolConfig, "ubuntu-01", 6379,
                1000, "12345678");
    }

    // 获取 Jedis 对象
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
```
测试：
```java
public class JedisPoolTest {
    private Jedis jedis;

    @Before
    public void before() {
        jedis = JedisConnectionFaction.getJedis();
    }

    @Test
    public void test01() {
        jedis.set("hello", "world");
        System.out.println(jedis.get("hello"));
    }

    @After
    public void after() {
        // 如果是在连接池中，并不会真正关闭
        jedis.close();
    }
}
```

#### Jedis 实例-手机验证码

要求：
1. 输入手机号，点击发送后随机生成6位数字码，2分钟有效 
2. 输入验证码，点击验证，返回成功或失败 
3. 每个手机号每天只能输入3次 

代码：
```java
public class PhoneCode {
    private static final long EXPIRE_SECONDS = 120;
    private static final String REDIS_HOST = "ubuntu-01";
    private static final int REDIS_PORT = 6379;
    private static Random random = new Random();

    public static void main(String[] args) {
        String phone = "13678765432";
        // 模拟验证码发送
         verifyCode(phone);
        // 验证码确认
        // getRedisCode(phone, "436388");
    }

    // 1. 生成 6 位数字验证码
    public static String getCode() {
        return String.format("%06d", random.nextInt(1000000));
    }

    // 2. 每个手机每天只能发三次，验证码放到 redis 中，设置过期时间
    public static void verifyCode(String phone) {
        // 连接 redis
        Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        // 拼接 key
        // 手机发送次数 key
        String countKey = getCountKey(phone);
        // 验证码 key
        String codeKey = getCodeKey(phone);
        if (jedis.get(countKey) == null) {
            jedis.setex(countKey, 24 * 60 * 60, "0");
        }
        Long count = jedis.incr(countKey);
        if (count > 3) {
            System.out.println("今天发送次数已经测过3次");
            jedis.close();
            return;
        }
        // 验证码放入 redis
        jedis.setex(codeKey, EXPIRE_SECONDS, getCode());
        System.out.println("验证码发送成功");
        jedis.close();
    }

    // 3. 验证码校验
    public static void getRedisCode(String phone, String code) {
        Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        String targetCode = jedis.get(getCodeKey(phone));
        if (Objects.equals(targetCode, code)) {
            System.out.println("验证码校验成功");
        } else {
            System.out.println("验证码校验失败");
        }
        jedis.close();
    }

    private static String getCountKey(String phone) {
        return "VerifyCode:" + phone + ":count";
    }
    private static String getCodeKey(String phone) {
        return "VerifyCode:" + phone + ":code";
    }
}
```

### Spring Data Redis

Spring Data 是 Spring 中数据操作的模块，包含对各种数据库的继承，其中对 Redis 的集成模块就叫做 SpringDataRedis。官网地址: https://spring.io/projects/spring-data-redis

- 提供对不同的 Redis 客户端的整合(Lettuce 和 Jedis)
- 提供对 RedisTemplate 统一 API 来操作 Redis
- 支持 Redis 的发布订阅模式
- 支持 Redis 哨兵和 Redis 集群
- 支持基于 Lettuce 的响应式编程
- 支持基于 JDK、JSON、字符串、Spring 对象的数据序列化与反序列化
- 支持基于 Redis 的 JDK Collection 实现

SpringDateRedis 中提供了 RedisTemplate 工具类，封装了对 Redis 的操作。并且将不同数据类型的操作 API 封装到不同的类型中。

|API|返回值类型|说明|
|---|---|---|
|redisTemplate.opsForValue()|ValueOperations|操作 String 类型|
|redisTemplate.opsForHash()|HashOperations|操作 Hash 类型|
|redisTemplate.opsForList()|ListOperations|操作 List 类型|
|redisTemplate.opsForSet()|SetOperations|操作 Set 类型|
|redisTemplate.opsForZSet()|ZSetOperations|操作 SortedSet 类型||
|redisTemplate||通用的命令|

#### SpringDataRedis 快速入门

SpringBoot 已经提供了对 SpringDataRedis 的支持，使用非常简单：

1. 引入依赖
```xml
        <!-- Reids 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- 连接池依赖 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
```
2. 配置文件
打开 application.yaml 文件，写入如下内容：
```yaml
spring:
  redis:
    host: 192.168.155.129
    port: 6379
    password: 12345678
    lettuce:
      pool:
        max-active: 8  # 最大连接
        max-idle: 8  # 最大空闲连接
        min-idle: 0  # 最小空闲连接
        max-wait: 100  # 连接等待时间
```

3. 注入 RedisTemplate
```java
@Autowired
private RedisTemplate redisTemplate;
```

4. 编写测试
```java
@SpringBootTest
class SpringbootRedisDemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    // @BeforeEach
    // void configRedisTemplate() {
    //     RedisSerializer stringSerializer = new StringRedisSerializer();
    //     // 解决写入 redis 时出现 \xac\xed\x00\x05t\x00\x04 前缀问题
    //     redisTemplate.setKeySerializer(stringSerializer);
    //     redisTemplate.setValueSerializer(stringSerializer);
    //     redisTemplate.setHashKeySerializer(stringSerializer);
    //     redisTemplate.setHashValueSerializer(stringSerializer);
    // }

    @Test
    void testString() {
        // 插入一条 string 类型数据
        redisTemplate.opsForValue().set("name", "foolishflyfox");
        // 读取一条 string 类型数据
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }
}
```

#### SpringDataRedis 的序列化方式

RedisTemplate 可以接收任意 Object 作为值写入 Redis，只不过写入前会把 Object 序列化为字节形式，默认是采用 JDK 序列化，存在例如 `\xac\xed\x00\x05t\x00\x04` 的前缀。

缺点：
- 可读性查
- 内存占用较大

我们可以自定义 RedisTemplate 的序列化方式，首先要导入 jackson 包：
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

java 代码如下：

```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建 Template
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置序列化工具
        // key 和 hashKey 采用 string 序列化
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // value 和 hashValue 采用 JSON 序列化
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashValueSerializer(RedisSerializer.json()
        return redisTemplate;
    }
}
```
测试代码为：
```java
@SpringBootTest
class SpringbootRedisDemoApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testString() {
        // 插入一条 string 类型数据
        redisTemplate.opsForValue().set("name", "笨飞狐");
        // 读取一条 string 类型数据
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }
}
```


**对象序列化**: 下面测试对象的序列化，定义一个 User 类型：
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private Integer age;
}
```
测试代码：
```java
@Test
void testSaveUser() {
    // 写入对象
    redisTemplate.opsForValue().set("user:100", new User("张三", 21));
    // 获取数据
    User user = (User) redisTemplate.opsForValue().get("user:100");
    System.out.println("result = " + user);
}
```
通过 RDM 图形化工具看到写入的 redis 内容为：
```json
{
  "@class": "com.bfh.redis.User",
  "name": "张三",
  "age": 21
}
```
通过 `@class` 字段实现对 json 的反序列化。

尽管 JSON 的序列化方式可以满足我们的需求，但依然存在一些问题。例如 `@class` 字段就会带来额外的内存开销。

为了节省内存空间，我们并不会使用 JSON 序列化器来处理 value，而是统一使用 String 序列化器，要求只存储 String 类型的 key 和 value。当需要存储 Java 对象时，手动完成对象的序列化和反序列化。

#### StringRedisTemplate

Spring 默认提供了一个 StringRedisTemplate 类，它的 key 和 value 的序列化方式默认就是 String 方式，省去了我们自定义 RedisTemplate 的过程：
```java
@Autowired
private StringRedisTemplate redisTemplate;
@Test
void testSaveUser() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    // 创建对象
    User user = new User("李四", 22);
    // 手动序列化
    String json = mapper.writeValueAsString(user);
    // 写入数据
    redisTemplate.opsForValue().set("user:200", json);
    // 获取数据
    String jsonUser = redisTemplate.opsForValue().get("user:200");
    // 手动反序列化
    User user1 = mapper.readValue(jsonUser, User.class);
    System.out.println("user1 = " + user1);
}
```
在 redis 中存放的内容为：
```json
{
  "name": "李四",
  "age": 22
}
```

#### hash 使用 redisTemplate

```java
@Test
void testHash() {
    redisTemplate.opsForHash().put("user:400", "name", "小明");
    Map<String, Object> map = new HashMap<>();
    map.put("email", "xiaoming@qq.com");
    map.put("age", "18");
    redisTemplate.opsForHash().putAll("user:400", map);

    Map<Object, Object> entries = redisTemplate.opsForHash().entries("user:400");
    System.out.println("entries = " + entries);
}
```

## Redis 事务

### Redis 事务定义

Redis 事务是一个单独的隔离操作：事务中所有命令都会序列化、按顺序地执行。事务在执行过程中不会被其他客户端发送来的命令打断。

Redis 事务的主要作用就是**串联多个命令**，防止别的命令插队。

### Multi、Exec、discard

从输入 Multi 命令开始，输入的命令会依次进入命令队列中，但不会执行，直到输入 Exec 后，Redis 会将之前的命令队列中的命令依次执行。

组队过程中可以通过 discard 来放弃组队。

### 事务的错误处理

组队中某个命令出现了报告错误，执行时整个队列会被取消。(例如输入命令格式错误)

- `mult`: 开始一个事务，进入组队阶段
- `exec`: 执行一个事务，进入执行阶段
- `discard`: 取消一个事务

执行阶段某个命令报出错误，则只有报错命令不会被执行，其他的命令都会被执行。(例如对一个字符串自增)。

- `watch key [key ...]`: 在执行 multi 之前，先执行该命令，如果在事务在执行之前这些 key 被其他命令所改动，那么事务将被打断。

### Redis 事务三特性

- 单独的隔离操作: 事务中所有命令都会序列化、按顺序地执行。事务在执行的过程中不会被其他客户端发来的命令请求所打断；
- 没有隔离级别的概念: 队列中命令没有提交之前都不会实际被执行，因为事务提交前任何指令都不会被执行
- 不保证原子性: 事务中如果有一条命令执行失败，其后的命令仍然会被执行，没有回滚

### Redis 事务案例 - 秒杀案例

代码见项目：*demos/redis-demo/jedis-demo* 。
秒杀业务: *SecKillDemo.java*
业务测试: *SecKillTest.java*

Lua 脚本在 Redis 中的优势：将复杂的或者多步的 redis 操作写为一个脚本，一次提交给 redis 执行，减少反复连接 redis 的次数，提升性能。

Lua 脚本是类似于 redis 事务，有一定的原子性，不会被其他命令插队，可以完成一些 redis 的事务操作。

但是注意 redis 的 lua 脚本功能只有在 Redis 2.6 以上版本才能使用。可以利用 lua 脚本淘汰用户，解决超卖问题。

## Redis 持久化

Redis 提供了两种不同形式的持久化方式：
- RDB (Redis Database)
- AOF (Append Of File)

### RDB

#### RDB 是什么

在指定的时间间隔内将内存中的数据集快照写入磁盘，也就是所谓的 Snapshot 快照，它恢复时是将快照文件直接读入内存中。

#### 备份是如何执行的

Redis 会单独创建 (fork) 一个子进程来完成持久化，先将数据写入到一个临时文件，待持久化过程都结束，再用这个临时文件替换上次持久化好的文件。整个过程中，主进程不进行任何 IO 操作，这就确保了极高的性能。如果需要进行大规模的数据恢复，且对于数据恢复的完整性不是非常敏感，那么 RDB 方式要比 AOF 方式更加高效。

RDB 的缺点是最后一次持久化后的数据可能丢失。

#### Fork

Fork 的使用是复制一个与当前进程一样的进程。新进程的所有数据(变量、环境变量、程序计数器等)数值和原进程一致，但是是一个全新的进程，并作为原进程的子进程。

在 Linux 程序中，fork() 会产生一个和父进程完全相同的子进程，但子进程在此后多会用 exec 系统调用，处于效率考虑，Linux 中引入了"写时复制技术"。

一般情况父进程和子进程会共用一段为例内存，只有进程空间的各段的内容要发生变化时，才会将父进程的内容复制一份给子进程。

#### 优势

- 适合大规模的数据恢复
- 对数据完整性和一致性要求不高更适合使用
- 节省磁盘空间
- 恢复速度快

#### 劣势

- 可能存在数据丢失

### AOF

AOF 是 Append Only File 的简称。

#### AOF 是什么

以日志的形式记录每个写操作(增量保存)，将 Redis 执行过的所有写命令记录下来(读操作不记录)，只许最佳文件，但不可以改写文件。redis 启动之初会读取该文件重新构建数据，换言之，redis 重启就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作。
#### AOF持久化流程

1. 客户端的请求写命令会被 append 追加到 AOF 缓冲区中；
2. AOF 缓冲区根据 AOF 持久化策略 [always, everyseec, no] 将操作 sync 同步到磁盘的 AOF 文件中；
3. AOF 文件大小超过重写策略或手动重写时，会对 AOF文件 rewrite 重写，压缩 AOF 文件容量。
4. Redis 重写时，会重新 load 加载 AOF 文件中的写操作达到数据恢复的目的；

#### AOF 默认不开启

可以在 redis.conf 中配置文件名称，默认为 appendonly.aof。

AOF 文件的保存路径，同 RDB 的路径一致。

当 AOF和 RDB 同时开启时，系统默认读取 AOF 的数据(数据不存在丢失)。

AOF 启动、修复、恢复：
- AOF 的备份机制和性能虽然和 RDB 不同，但是备份和恢复的操作同 RDB 一样，都是拷贝备份，需要恢复时在拷贝到 Redis 工作目录，启动系统即加载。

#### Rewrite 压缩

AOF 采用文件追加方式，文件会越来越大，为了避免出现这种情况，新增了重写机制，当 AOF 文件的大小超过所设定的阈值时，Redis 就会启动 AOF 文件的内容压缩，只保留可以恢复数据的最小指令集。

## Redis 主从复制

主机数据更新后，根据配置和策略，自动同步到备机的 **master/slaver 机制**,Master 以写为主，Slave 以读为主。

好处：
- 读写分离
- 容灾快速恢复

- 配置一主多从: `slaveof ip port`
- 宕机的情况
    - 从机宕机后，主机查询 `info replication` 中 **connected_slaves** 的值会减一；
    - 从机宕机重启后，需要通过 `slaveof` 重新设置主服务器
    - 主服务器宕机后，从服务器不会变为主服务器，从服务器会显示主服务器已经 down，主服务器重启后还是主服务器。
- 薪火相传: A 是 B 的主服务器，B 是 C 的主服务器
- 反客为主: 从服务器变为主服务器
    - 手动操作: `slaveof no one`
    - 哨兵模式(反客为主的自动版): 能够后台监控主机是否发生故障，如果故障了根据投票数自动降从库转换为主库
        1. 新建 *redis-sentinel.conf* ，内容为 `sentinel monitor mymaster ubuntu-01 6379 1` ，最后的1表示多少个哨兵同意，就进行切换
        2. 启动哨兵：`redis-sentinel /usr/local/etc/redis-sentinel.conf`

主从复制的原理：
1. 从服务器连接上主服务器后，从服务器向主服务器发送进行数据同步的消息；
2. 主服务器收到从服务器发送的消息，主服务器会进行持久化 reb 文件，把 rdb 文件发送到从服务器，从服务器拿到 rdb 进行读取
3. 每次主服务器进行写操作后，和从服务器进行数据同步（主服务器主动）

## Redis 集群

集群解决问题：
- 容量不够问题
- 并发写问题

## 应用问题

### 缓存穿透

缓存穿透的现象：
1. 应用服务器压力变大
2. redis 命中率降低
3. 一直查询数据库

问题产生的原因：
1. redis 查询不到数据库
2. 出现很多非正常 url 访问

常见的解决方案：
1. 对空值做缓存：如果一个查询返回的数据为空(不管数据是否不存在)，我们仍然将这个空结果缓存，设置空结果的过期时间很短，最长不超过5分钟
2. 设置可访问的名单(白名单)
3. 使用布隆过滤器
4. 进行实时监控：当发现 Redis 的命中率开始急速降低时，排查访问对象和访问的数据，和运维人员配合，可以设置黑名单限制服务

### 缓存击穿

缓存击穿: 
1. 数据前访问压力瞬时增加
2. redis 里面没有大量key过期
3. redis 正常运行

可能的原因：
1. redis 某个 key 过期了，大量访问使用这个 key

解决方案：
1. 预先设置热门数据：在 redis 高峰访问之前，把一些热门数据提前传入到 redis 中，加大这些热门数据 key 的时长
2. 实时调整：现场监控哪些数据热门，实时调整 key 的过期时间
3. 使用锁

### 缓存雪崩

现象：
1. 数据库压力变大，服务器崩溃

原因：
1. 在极少时间段内，查询大量 key 的集中过期情况

解决方案：
1. 构建多级缓存架构
2. 使用锁或队列
3. 设置过期标志更新缓存
4. 将缓存失效时间分散

### 分布式锁

分布式锁主流的实现方案：
1. 基于数据库实现分布式锁
2. 基于缓存(redis等)
3. 基于 ZooKeeper

redis 版解决方案：
1. 使用 `setnx` 上锁，使用 `expire` 设置过期时间，使用 `del` 释放锁，同时上锁加设置过期时间 `set user 10 nx ex 20`

## Redis 企业实战

可参考: 
- https://icode.best/i/10243847050154

黑马点评使用 Redis，包括如下功能：
- 短信登录: Redis 的共享 Session 应用
- 商户查询缓存: 企业的缓存使用技巧，包括缓存雪崩、穿透等问题解决
- 达人探店: 基于 List 的点赞列表，基于 SortedSet 的点赞排行榜
- 优惠券秒杀: Redis 的计数器、Lua 脚本 Redis、分布式锁、Redis 的三种消息队列
- 好友关注: 基于 Set 集合的关注、取消关注、共同关注、消息推送等功能
- 附近商户: Redis 的 GeoHash 应用
- 用户签到: Redis 的 BitMap 数据统计功能
- UV 统计: Redis 的 HyperLog / Bitmap 的统计功能

### 导入后端项目并运行

- 导入黑马点评项目
    - 数据库初始化
        - 创建 hmdp 库: `create database hmdp;`
        - 使用 hdmp 库: `use hmdp`
        - 导入数据: `source hmdp.sql`
    - 其中的表有
        - tb_user: 用户表
        - tb_user_info: 用户详情表
        - tb_shop: 商户信息表
        - tb_shop_type: 商户类型表
        - tb_blog: 用户日记表(达人探店日记)
        - tb_follow: 用户关注表
        - tb_voucher: 优惠券表
        - tb_voucher_order: 优惠券的订单表

黑马点评项目架构:

![](http://assets.processon.com/chart_image/6275d62c0791290711fc4f28.png)


启动项目后，在浏览器访问: http://localhost:8081/shop-type/list ，如果能看到数据则证明项目没有问题。

### 导入前端项目

采用前后端分离的方式进行部署，前端项目部署在虚拟机 ubuntu-01 中。

1. 安装 Nginx: `sudo apt install nginx`;
1. 将前端项目拷贝到虚拟机中;
1. 修改 Nginx 的配置文件 */etc/nginx/nginx.conf* ，添加如下内容：
```
user www-data;
worker_processes auto;
pid /run/nginx.pid;
include /etc/nginx/modules-enabled/*.conf;

events {
	worker_connections 768;
	# multi_accept on;
}

http {

	##
	# Basic Settings
	##

	sendfile on;
	tcp_nopush on;
	tcp_nodelay on;
	keepalive_timeout 65;
	types_hash_max_size 2048;
	# server_tokens off;

	# server_names_hash_bucket_size 64;
	# server_name_in_redirect off;

	include /etc/nginx/mime.types;
	default_type application/octet-stream;

    # === 添加内容开始 ===
	server {
        # 服务端口号
		listen 8080;
		server_name localhost;
		location / {
            # 前端项目目录
			root /home/huabinfeng/servers/hmdp-nginx-1.18.0/html/hmdp;
			index index.html index.html;
		}
		error_page 500 502 503 504 /50x.html;
		location = /50x.html {
			root	html;
		}
		location /api {
			default_type  application/json;
			#internal;
			keepalive_timeout   30s;
			keepalive_requests  1000;
			#支持keep-alive
			proxy_http_version 1.1;
			rewrite /api(/.*) $1 break;
			proxy_pass_request_headers on;
			#more_clear_input_headers Accept-Encoding;
			proxy_next_upstream error timeout;
			proxy_pass http://backend;
		}
	}
    # 设置后端项目地址
	upstream backend {
        # 192.168.1.2 为后端项目启动机子的 ip
		server 192.168.1.2:8081 max_fails=5 fail_timeout=10s weight=1;
		keepalive 100;
	}
    # === 添加内容结束 ===

}

```

### 短信登录

#### 基于 Session 实现登录

![基于session实现登录流程](http://assets.processon.com/chart_image/6276157f0791290711fd71f4.png)



- 集群的 session 共享问题
- 基于 Redis 实现共享 session 登录






