# Docker 命令

- docker 信息查看
    - `docker network ls`: 查看host上docker创建的网络
- dockerhub
    - 搜索镜像: `docker search 镜像名`
    - 通过 url 获取指定镜像存在的版本，例如：`https://registry.hub.docker.com/v1/repositories/debian/tags` 可以查看 debian 存在的镜像版本。
- 镜像 Image
    - 获取镜像
        - 从远程仓库获取镜像: `docker pull 镜像名`
    - 查看镜像
        - 查看所有镜像 `docker images`
        - 查看指定镜像 `docker images 镜像名`
        - 查看镜像分层 `docker history 镜像名`
    - 创建镜像
        - 使用指定容器创建镜像: `docker commit 容器名 镜像名`
        - 从 Dockerfile 创建: `docker build [OPTIONS] PATH | URL`
    - 为镜像打标: `docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]`
    - 将镜像保存为镜像文件
        - 使用语法：`docker save [OPTIONS] IMAGE [IMAGE...]`，例如`docker save -o 文件名.tar 镜像名`
    - 将镜像文件加载到容器
        - 使用语法: `docker load [OPTIONS]` ，例如 `docker load < 文件名.tar`
- 容器 Container
    - 从镜像启动一个容器: `docker run [OPTIONS] IMAGE [COMMAND] [ARG...]`，OPTIONS 常用选项如下
        - `-d`: 以后台方式启动容器
        - `--name`: 指定容器名称
        - `--restart`: 设置容器自动重启
        - `--network=none`: 不使用网络
        - `--network=host`: 直接使用 Docker host 的网络，最大的好处是性能。不便之处是牺牲一些灵活性，比如要考虑灵活性。
    - 停止容器 
        - `docker stop [OPTIONS] CONTAINER [CONTAINER...]`: 向容器进程发送一个 SIGTERM 信号
            - `-t, --time int`: 指定在关停容器前等待的时间，默认为10s，如果为 0 表示立即停止。
        - `docker kill [OPTIONS] CONTAINER [CONTAINER...]`: 向容器进程发送指定的信号
            - `-s, --signal string   Signal to send to the container (default "KILL")`
    - 启动已经停止的容器 `docker start [OPTIONS] CONTAINER [CONTAINER...]`
    - 重启容器 `docker restart [OPTIONS] CONTAINER [CONTAINER...]`
    - 暂停容器 `docker pause CONTAINER [CONTAINER...]`
    - 恢复被暂停的容器 `docker unpause CONTAINER [CONTAINER...]`
    - 删除容器 `docker rm [OPTIONS] CONTAINER [CONTAINER...]`
    - 进入一个容器
        - `docker attach`: 可以 attach 到容器启动命令的终端；
        - `docker exec [OPTIONS] CONTAINER COMMAND [ARG...]`: 例如：`docker exec -it 容器名 bash`
    - 创建但不运行容器 `docker create [OPTIONS] IMAGE [COMMAND] [ARG...]`
    - 开始运行创建的容器 `docker start [OPTIONS] CONTAINER [CONTAINER...]`

- Dockerfile 命令
    - `FROM` : 指定 base 镜像
    - `MAINTAINER`: 指定镜像作者
    - `COPY`: 将文件从 build context 复制到镜像，例如 `COPY src dest` 或 `COPY ["src", "dest"]`，dest 可以是文件名也可以是文件夹名。
    - `ADD`: 与 COPY 类似，从 build context 复制文件到镜像。不同的是，如果 src 是归档文件(tar,zip,tgz,xz,tar.bz2等)，文件会被**自动解压**到 dest。可以用 ADD 替代 COPY。
    - `ENV`: 设置环境变量，环境变量可以被后面的指令使用。例如 `ENV PATH $PATH:.` 将当前目录加入到 PATH。
    - `EXPOSE`: 指定容器中的进程会监听某个端口，Docker 可以将该端口暴露出来。
    - `VOLUMN`: 将文件或目录声明为 volume。
    - `WORKDIR`: 为后面的 RUN 、CMD、ENTRYPOINT、ADD 或 COPY 指令设置镜像中的当前目录。
    - `RUN`: 在容器中运行指定的命令。
    - `CMD`: 在容器启动时运行指定的命令。Dockerfile 中可以有多个 CMD 指令，但只有最后一个生效。CMD 可以被 docker run 之后的参数替换。
    - `ENTRYPOINT`
- Dockerfile 命令的 Shell 格式和 Exec 格式
    - shell 格式: `<instruction> <command>`,例如 `RUN apt-get install python3`, `CMD echo "hello,world"`, `ENTRYPOINT echo "hello"`。指令执行时，shell 格式底层会调用 `/bin/sh -c [command]`。
    - Exec 格式: `<instruction> ["executable", "param1", "param2", ...]`，例如 `RUN ["apt-get", "install", "python3"]`, `CMD ["/bin/echo", "hello"]`, `ENTRYPOINT ["echo", "world"]`。
    - CMD 和 ENTRYPOINT 推荐使用 Exec 格式。RUN 两种都可以。
    - CMD 命令可以被 docker run 指定的参数忽略，而 ENTRYPOINT 命令不会。
    - 可以使用 `ENTRYPOINT` 指定执行的命令，使用 `CMD` 提供默认参数，在 run 时指定运行时参数。例如下面的 dockerfile
```Dockerfile
FROM busybox
CMD ["Cmd"]
ENTRYPOINT ["echo", "Hello,"]
```
执行结果为：
```shell
$ docker build -t busybox-test .
$  docker run --rm busybox-test
Hello, Cmd
$ docker run --rm busybox-test good world 
Hello, good world
```


一个 Dockerfile 的示例：
在当前目录下有以下文件：
- *Dockerfile* : 构建镜像的 Dockerfile 文件
- *sources.list* : 更新软件源的文件

其中 `source.list` 的内容为：
```
deb http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse
```

Dockerfile 的内容为：
```Dockerfile
FROM ubuntu:20.04

COPY sources.list /etc/apt

RUN apt update
RUN apt install -y netcat
# 安装 ifconfig
RUN apt install net-tools
# 安装 ping
RUN apt install -y inetutils-ping
RUN apt install -y curl

WORKDIR /root
CMD /bin/bash
```
执行：`docker build -t fhb-ubuntu20 .` ,完成镜像 fhb-ubuntu20 的创建。



