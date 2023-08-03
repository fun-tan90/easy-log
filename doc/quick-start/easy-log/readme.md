# docker版easy-log安装教程

#### 1、创建网络!
**_如果在安装ES集群时，已创建网络忽略该步骤_**
```shell
# --driver：驱动程序类型
# --gateway：主子网的IPV4和IPV6的网关  可修改
# --subnet：代表网段的CIDR格式的子网   可修改
# el_net：自定义网络名称                 
docker network create --driver=bridge --gateway=172.18.0.1 --subnet=172.18.0.0/16 el_net
```
#### 2、选择部署方式
```shell
# single  单机

# cluster 集群, 实例数可以横向扩展
```
#### 3、修复.env环境变量
```shell
# 修改.env中image的版本信息以及堆内存大小
```

#### 4、启动Easy-Log

```shell
docker-compose up -d
```