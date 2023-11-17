# docker常用命令

## 零、开篇

此处只记录我认为重要的命令，一些非常常见简单的命令不在其中。

## 一、docker 相关命令

### 1. docker 启动与停止

```bash
//启动docker
systemctl start docker

//停止docker
systemctl stop docker

//重启docker
systemctl restart docker

//设置docker开机自启
systemctl enable docker
```

### 2. 镜像相关命令

```bash
//删除单个镜像
docker rmi 镜像ID/镜像名称[:版本号]

//-f:表示强制删除，强制删除单个镜像
docker rmi -f 镜像ID/镜像名称[:版本号]

//删除多个镜像
docker rmi [-f] 镜像1ID/镜像1名称[:版本号] 镜像2ID/镜像2名称[:版本号]

//删除全部镜像
docker rmi [-f] $(docker images -aq)

//获取 docker 镜像元信息
docker inspect 镜像ID/镜像名称[:版本号]
```

### 3. 容器相关命令

#### 3.1 容器运行

**docker run 相关命令的主要用法为：** `docker run [OPTIONS] IMAGE [COMMAND] [ARG...]`，即 docker run [docker 容器命令选项] 镜像 [命令] [参数]，`中括号[]`表示该命令选项 `可选可不选`

常用的 docker run 容器命令选项有：

`-i` ：表示以交互模式运行容器，通常与`-t`结合使用

`-t`：为容器重新分配一个伪输入终端，通常与`-i`结合使用

`-d`：后台运行容器，并返回容器 ID，即启动守护式容器 (这样创建的容器不会分配一个伪输入终端，如果是以`-it`两个参数启动，启动后则会分配一个伪输入终端)

`-p`：指定端口映射，格式为：`-p 主机(宿主机)端口:容器映射端口`，可以使用多个`-p`做多个端口映射

`-v`：指定挂载主机目录 / 文件 到容器目录 / 文件 上，即挂载容器数据卷，格式为：`-v 主机(宿主机)目录/文件的绝对路径:容器内目录/文件的绝对路径[:读取权限]`，可以使用多个`-v`做多个目录或文件映射，默认为`rw读写模式`，`ro表示只读`。 

`rw读写模式`：表示宿主机能对数据卷进行读取和更改，容器也能对其进行读取和更改。
`ro表示只读`：表示宿主机能对数据卷进行读取和更改，容器只能对其进行读取不能更改。

`--name`：为创建的容器指定一个名称，格式为：`--name=容器名称`

#### 3.2 容器日志

**docker logs 相关命令的主要用法为：** `docker logs [OPTIONS]`CONTAINER，即 docker ps [docker 容器命令选项] 容器，`中括号[]`表示该命令选项 `可选可不选`

常用的 docker logs 容器命令选项有：

`-f` ：显示最新的打印日志

`-t`：显示时间戳

`--tail 数字`：显示最后多少条日志

```bash
//查看容器日志并显示时间戳
docker logs -t 容器ID/容器名称

//持续输出容器日志
docker logs -f 容器ID/容器名称

//查看最后n条容器日志
docker logs --tail n 容器ID/容器名称

//查看容器日志
docker logs -f -t 容器ID/容器名称
```

#### 3.3 其他命令

```bash
//查看容器内部运行的进程
docker top 容器ID/容器名称

//查看容器内部元信息
docker inspect 容器ID/容器名称

//进入正在运行的容器内并以命令行交互
//以exec方式进入到容器
docker exec -it 容器ID/容器名称 /bin/bash 或 /bin/sh

//以attach方式进入到容器
docker attach 容器ID/容器名称

//如果不想进入容器，直接获取相关指令的运行结果，可在后面填写相关操作指令
docker exec -it 容器ID/容器名称 相关命令

//exec 与 attach 的区别：
//exec：是在容器中打开新的终端，并且可以启动新的进程 (推荐)
//attach：是直接进入容器启动命令的终端，不会启动新的进程

//从容器内拷贝文件到宿主机
docker cp 容器ID/容器名称:容器内目录/文件的绝对路径 宿主机目录/文件的绝对路径

//从宿主机中拷贝文件到容器内
docker cp 宿主机目录/文件的绝对路径 容器ID/容器名称:容器内目录/文件的绝对路径
```

