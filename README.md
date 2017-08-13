# Mi-RPC
Java RPC分布式调用框架，性能及功能完备性虽不及淘宝HSF，但绝对是小巧精致，性能优越，中小型企业微服务架构首选开源产品
# Mi-RPC框架使用方略 #
<hr/>
### 1、搭建zookeeper集群 ###
这一步骤可以参考网络教程，也可以部署单机模式。
### 2、启动Mi-Monitor服务注册中心 ###
此应用属于Spring Boot应用，直接命里行
java -jar xxx.jar启动即可
### 3、修改系统Hosts文件追加Mi-Monitor应用机器域名映射
127.0.0.1 www.ahstu.org
### 4、RPC服务端配置示例
![](http://i.imgur.com/L87R5HZ.png)
<img src="http://i.imgur.com/L87R5HZ.png"/>
### 5.RPC消费端配置示例
![](http://i.imgur.com/vqAa7yc.png)
<img src="http://i.imgur.com/vqAa7yc.png"/>
