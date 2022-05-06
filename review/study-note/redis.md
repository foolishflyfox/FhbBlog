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
    - `bind`: 改为 0.0.0.0 表示任意 IP 可以访问
    - `daemonize`: 改为 yes，表示后台运行
    - `requirepass`: 设置密码
    - `port`: 表示监听的端口
    - `dbfilename`: 表示 redis 存放数据文件的名称，默认为 *dump.rdb*
    - `dir`: 工作目录，默认是当前目录 ./，与 *redis-server* 在同一个目录
    - `database`: 指定库的数量，设置为 1，表示只用1个库，默认有16个库，编号 0-15
    - `maxmemory`: 设置 redis 能够使用的最大内存，例如 `maxmemory 512mb`
    - `logfile`: 日志文件，默认为空，不记录日志，可以指定日志文件名

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

## Redis 常见命令

### Redis 数据结构介绍

Reis 是一个 key-value 的数据库，key 一般是 String 类型，不过 value 的类型多种多样。

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

### Redis 的 Java 客户端

Jedis: 以 Redis 命令作为方法名称，学习成本低，简单实用。但是 Jedis 实例是线程不安全的，多线程环境中需要基于连接池来使用。
Lettuce：Lettuce 是基于 Netty 实现的，支持同步、异步和响应式编程方式，并且是线程安全的。支持 Redis 的哨兵模式、集群模式和管道模式。
Redisson: Redisson 是一个基于 Redis 实现的分布式、可伸缩的 Java 数据结构集合。包含了诸如 Map、Queue、Lock、Semaphore、AtomicLong 等强大功能。

### Jedis

Jedis 的官网地址：https://github.com/redis/jedis 。

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
