# Kafka

## 简介

### 消息队列简介

**什么是消息队列**：Message Queue，经常缩写为 MQ。从字面上理解，消息队列是一种用来存放消息的队列。

消息队列就是将需要传输的数据存放在队列中。

#### 消息队列中间件

消息队列中间件就是用来存储消息的软件(组件)。

目前市面上的消息队列中间件有很多，例如 Kafka、RabbitMQ、ActiveMQ、RocketMQ、ZeroMQ 等。

很多时候消息队列不是永久存储的，是作为临时存储存在的(设定一个期限)。

### 应用场景

#### 异步处理

![消息队列-异步处理](http://assets.processon.com/chart_image/62749339e0b34d07585d2295.png)

因为发送注册短信和发送邮件延时对任务是可以接受的，因此可以将这两个任务通过异步完成。

可以将一些比较耗时的操作放在其他系统中，通过消息队列将需要处理的消息进行存储，其他系统可以消费消息队列中的数据进行异步处理。

#### 消息解耦

![消息队列-消息解耦](http://assets.processon.com/chart_image/627495f2e0b34d07585d32b5.png)

原先一个微服务是通过接口(dubbo、grpc、http restful)调用另一个微服务，会出现紧耦合，只要被调方的接口改变，调用方也需要修改。

使用消息队列可以将系统解耦，一个微服务将消息放入消息队列中，另一个微服务可以从消息队列中把消息取出处理。

#### 流量削峰

![消息队列-流量削峰](http://assets.processon.com/chart_image/627498d40791290711f8d834.png)

因为消息队列是低延时、高可靠、高吞吐的，可以应对大量并发。

#### 日志处理

![](http://assets.processon.com/chart_image/62749b00f346fb18e7be6c15.png)

大数据领域常见。日志量通常会非常大，需要通过消息队列作为临时存储或通信管道。


### 消息队列的两种模式

![消息队列的两种模式](http://assets.processon.com/chart_image/6274beb80e3e7413eeb8eeb1.png)
#### 点对点模式

消息发送者生产消息发送到消息队列中，然后消息接收者从消息队列中取出并消费消息。消息被消费后，消息队列中不再有存储，所以消息接受者不可能消费到已经被消费过的消息。

点对点模式特点：
- 每个消息只有一个接收者(Consumer)，一旦被消费，消息就不再在消息队列中了；
- 发送者和接收者没有依赖性，发送者发送消息后，不管有没有接收者在运行，都不会影响到发送者下次发送消息；
- 接收者在成功接收消息后，需要向消息队列应答成功，以便消息队列删除当前接收的消息；

#### 发布订阅模式

发布-订阅模式特点：
- 每个消息可以由多个订阅者
- 发布者和订阅者之间有时间上的依赖性。针对某个整体 topic 的订阅者，它必须创建一个订阅者后，才能消费发布者的消息
- 为了消费消息，订阅者需要提前订阅该角色主题，并保持在线运行

### 什么是 Kafka

Kafka 是由 Apache 软件基金会开发的一个开源流平台，由 Scala 和 Java 编写。

一个分布式的流平台应该包括3点关键的能力：
1. 发布和订阅数据流，类似于消息队列或是企业消息传递系统
2. 以容错的持久化方式存储数据流
3. 处理数据流

### Kafka 的应用场景

我们通常将 Apache Kafka 用在两类程序：
1. 建立实时数据管道，以可靠地在系统或应用程序之间传递消息；
2. 构建实时流应用程序，以转换或响应数据流

![](http://assets.processon.com/chart_image/6274ced81e08532771684d64.png)

图中有如下的角色：
1. Producers: 可以由很多的应用程序，将消息数据放入到 Kafka 集群中；
2. Consumers: 可以由很多的应用程序，将消息数据从 Kafka 集群中拉取出来；
3. Connectors: Kafka 的连接器可以将数据库中的数据导入到Kafka，也可以将 Kafka 的数据导出到数据库中；
4. Stream Processors: 流处理器可以从 Kafka 中拉取数据，也可以将数据写入到 Kafka 中。

### Kafka 诞生的背景

为了解决 Linkedin 的数据管道问题，起初 Linkedin 常用 ActiveMQ 来进行数据交换。2010年前后，ActiveMQ 还无法满足 Linkedin 对数据传递系统的要求，经常由于各种缺陷而导致消息阻塞或服务无法正常访问，为了解决这个问题，Linkedin 决定研发自己的消息传递系统。当时 Linkedin 的首席架构师 jay kreps 便开始组织团队进行消息传递系统的研发。

Kafka 的优势：

|特性|ActiveMQ|RabbitMQ|Kafka|RocketMQ|
|---|---|---|---|---|
|所属|Apache|Mozilla|Apache|Apache/Ali|
|成熟度|成熟|成熟|成熟|比较成熟|
|生产组-消费组模式|支持|支持|支持|支持|
|发布-订阅|支持|支持|支持|支持|
|Request-Replay|支持|支持|-|支持|
|API 完备性|高|高|高|低|
|多语言支持|支持|语言无关|支持|支持|
|单机吞吐量|万级|万级|十万级|十万级|
|消息延迟|-|微秒级|毫秒级|-|
|可用性|高(主从)|高(主从)|非常高(分布式)|高|
|消息丢失|-|低|理论上不会丢失|-|
|消息重复|-|可控制|理论上有重复|-|
|事务|支持|不支持|支持|支持|
|文档完备性|高|高|高|中|
|首次部署难度|-|低|中|高|

### Kafka 生态圈

Apache kafka 经过多年发展，目前有一个较为庞大的生态圈。

Kafka 生态圈地址: https://cwiki.apache.org/confluence/display/KAFKA/Ecosystem 。

## 环境搭建

### 搭建 Kafka 集群

1. 将 Kafka 的安装包 [kafka_2.12-2.4.1.tgz](https://archive.apache.org/dist/kafka/2.4.1/kafka_2.12-2.4.1.tgz) 上传到虚拟机，并解压。
```shell
cd /export/software
tar -xvf kafka_2.12-2.4.1.tgz -C ../server
cd /export/server/kafka_2.12-2.4.1
```
2. 修改 server.properties
```
cd /export/server/kafka_2.12-2.4.1/config
vim server.properties
```
修改如下参数：
- `broker.id=0`: 指定 broker 的 id
- `log.dirs=/export/server/kafka_2.12-2.4.1/data`: 指定 Kafka 数据的位置

3. 将安装好的 kafka 复制到另外两台服务器的相同目录中。

4. 配置 KAFKA_HOME 环境变量
```
vim /etc/profile
export KAFKA_HOME=/export/server/kafka_2.12-2.4.1
export PATH=$PATH:$KAFKA_HOME/bin
```

5. 启动3台机器的 zookeeper，通过 *zk.sh* 启动(需要在3台机器上先安装 zookeeper)，*zk.sh* 的内容如下：

```sh
#!/bin/bash

case $1 in
"start") {
    for i in ubuntu-01 ubuntu-02 ubuntu-03
    do
        echo -------- zookeeper $i 启动 --------
        ssh root@$i "/export/server/zookeeper-3.5.7/bin/zkServer.sh start"
    done
}
;;
"stop") {
    for i in ubuntu-01 ubuntu-02 ubuntu-03
    do
        echo -------- zookeeper $i 停止 --------
        ssh root@$i "/export/server/zookeeper-3.5.7/bin/zkServer.sh stop"
    done
}
;;
"status") {
    for i in ubuntu-01 ubuntu-02 ubuntu-03
    do
        echo -------- zookeeper $i 状态 --------
        ssh root@$i "/export/server/zookeeper-3.5.7/bin/zkServer.sh status"
    done
}
;;
esac
```
安装 zookeeper 的方式：
- 下载 zookeeper 压缩包 [apache-zookeeper-3.5.7-bin.tar.gz](https://archive.apache.org/dist/zookeeper/zookeeper-3.5.7/apache-zookeeper-3.5.7-bin.tar.gz)
- 解压，创建 zkData 文件夹
- 在 zkData 文件夹中创建 myid 文件，指定 zookeeper 服务器的 id
- 将 *conf/zoo_sample.cfg* 复制为 *conf/zoo.cfg*
- 修改 zoo.cfg 中的 `dataDir` 为 zkData 文件夹
- 添加集群信息，例如：
```
###### cluster ######
server.1=ubuntu-01:2888:3888
server.2=ubuntu-02:2888:3888
server.3=ubuntu-03:2888:3888
```
- 启动 `bin/zkServer.sh start`

6. 启动 kafka: `nohup bin/kafka-server-start.sh config/server.properties &`

7. 查看 kafka 是否启动成功: `bin/kafka-topics.sh --bootstrap-server ubuntu-02:9092 --list`

注意：
- Kafka 2.8.0 之前，集群要有 ZooKeeper (Kafka 2.8.0 之后移除对 ZooKeeper的依赖)
- 每一个 Kafka 的节点都需要修改 broker.id (每个节点的标识，不能重复)
- log.dir 数据存储目录需要配置
### 目录结构分析

- *bin*: 存放 Kafka 所有执行脚本
- *config*: Kafka 所有配置文件
- *libs*: Kafka 所需 jar 包
- *logs*: Kafka 所有日志
- *site-docs*: Kafka 帮助文档

## 基本操作

### 创建 topic

创建一个 topic(主题)。Kafka 中所有的消息都是保存在主题中的(类似 mysql 的建表过程)，要生产消息到 Kafka，必须先确定一个主题。
```sh
# 创建名为 test 的主题
bin/kafka-topics.sh --create --bootstrap-server ubuntu-01:9092 --topic test
# 查看目前 Kafka 中的主题
bin/kafka-topics.sh --list --bootstrap-server ubuntu-01:9092 
```

### 生产消息到 Kafka

使用 Kafka 内置的测试程序，生产消息到 kafka 的test 主题中。

```
bin/kafka-console-producer.sh --broker-list ubuntu-01:9092 --topic test
```
该脚本读取标准输入的数据，并放入 topic 中。

### 从 Kafka 消费消息

使用下面的命令来消费 test 主题中的消息。
```
bin/kafka-console-consumer.sh --bootstrap-server ubuntu-01:9092 --topic test --from-beginning
```

推荐使用 Kafka Tool 工具，可以：
- 浏览 Kafka 集群节点，有多少个 topic，多少个分区
- 创建 topic / 删除 topic
- 浏览 ZooKeeper 中的数据

