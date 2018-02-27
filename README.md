# Mi-RPC
前身：阿里架构师业余作品-InsistRPC框架
<br/>
Java RPC分布式调用框架，性能及功能完备性虽不及淘宝HSF，但绝对是小巧精致，性能优越，中小型企业微服务架构首选开源产品
# Mi-RPC框架使用方略 #
<hr/>
### 1、搭建zookeeper集群 ###
<br/>
这一步骤可以参考网络教程，也可以部署单机模式。
<br/>
### 2、启动Mi-Monitor服务注册中心 ###
<br/>
此应用属于Spring Boot应用，直接命里行
<br/>
java -jar xxx.jar启动即可
<br/>
### 3、修改系统Hosts文件追加Mi-Monitor应用机器域名映射
<br/>
127.0.0.1 www.ahstu.org
<br/>
### 4、RPC服务端配置示例 ###
<br/>
![](http://i.imgur.com/L87R5HZ.png)
<br/>
<img src="http://i.imgur.com/L87R5HZ.png"/>
<br/>
### 5.RPC消费端配置示例 ###
<br/>
![](http://i.imgur.com/vqAa7yc.png)
<br/>
<img src="http://i.imgur.com/vqAa7yc.png"/>
<br/>
