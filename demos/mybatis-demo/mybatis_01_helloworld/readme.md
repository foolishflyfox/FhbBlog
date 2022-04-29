mybatis_01_helloworld

实现步骤：
1. 新建 student 表
2. 加入 maven 的 mybatis 坐标，mysql 驱动的坐标
3. 创建实体类，Student -- 保存表中的一行数据
4. 创建持久层的 dao 接口，定义操作数据库的方法
5. 创建一个 mybatis 使用的配置文件
    叫做 sql 映射文件，写sql语句的。一般一个表一个 sql 映射文件。
    这个文件是 xml 文件。
6. 创建 mybatis 的主配置文件：一般是一个项目就一个主配置文件。
    主配置文件提供了数据库的连接信息和 sql 映射文件的位置信息
7. 创建使用 mybatis 类。通过 mybatis 访问数据库。 