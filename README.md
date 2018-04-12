# KillSystem
商品秒杀系统

一、项目简介

网站管理员能上架商品，设置商品信息及图片介绍，需要设置商品秒杀的活动时间，供用户在规定的时间内进行抢购
网站管理员能管理用户订单信息
用户需要注册后登陆进入系统，查看管理员提供的商品秒杀列表，在规定时间内选择所需抢购的商品进行抢购，抢购成功后在支付二维码失效前扫码支付，否则抢购失败。

目前经过ab压力测试模拟10000并发抢购操作，平均响应时间14.2ms，失败请求24个
由于系统缓存不足无法进行20000并发，所以还未进行20000并发的测试

二、环境部署：

windows 10上搭建web服务器(tomcat 8.5)、nginx服务器(1.12.2)、FTP服务器(使用FTPServer工具)、mysql(5.7.19)、redis(3.0.503)、maven(3.5.2)

（查阅相关博文进行安装，如果需要相关压缩包，留下邮箱，我会尽快打包发你）

ROOT文件夹内容为nginx中配置的静态文件访问区域，KillSystem文件夹为web服务器下部署的项目。
将ROOT文件夹内容粘贴到tomcat路径下的静态资源ROOT目录，KillSystem文件夹为部署在tomcat上的项目，部署成功后，如我的电脑中的状态为：
D:\tomcat\apache-tomcat-8.5.16\wtpwebapps目录中有KillSystem和ROOT两个文件夹

在nginx中配置html/js/css/png等静态资源的访问路径为D:\tomcat\apache-tomcat-8.5.16\wtpwebapps\ROOT

在eclipse下导入maven项目KillSystem

条件允许建议在linux服务器下进行开发，redis的性能在linux下表现卓越。

另外，

(1) 项目中使用了ssm框架(ps:配置文件druid-db.xml等同于其他项目中的application.xml)

(2) pageHelper分页插件

(3) druid连接池

(4) JedisPool连接池

(5) log4j日志管理

(6) 支付宝当面付sdk

所需jar包已引入依赖，pom.xml无需修改。

三、数据库表设计

mysql数据库表：
       
       user表{tel_num varchar(11) PRI,username varchar(20) NOT NULL,passwd varchar(20) NOT NULL,register_date date}、
       
       goods表{goods_id int(11) PRI,goods_name varchar(20) NOT NULL,goods_price int(11) NOT NULL,goods_stock int(11) NOT NULL,begin_time datetime NOT NULL,end_time datetime NOT NULL,goods_info mediumtext}、
       
       goods_order表{order_id varchar(32) PRI,tel_num varchar(11) NOT NULL,address varchar(20) NOT NULL,goods_id int(11) NOT NULL,
       create_time datetime NOT NULL,is_success int(11) NOT NULL}、
       
       shippingAddress表{address_id int(11) NOT NULL,user_id varchar(20) NOT NULL,address varchar(50) NOT NULL,
       tel_num varchar(20) NOT NULL,name varchar(20) NOT NULL}

按照该表结构建表，并导入测试所需数据。

redis中数据的结构：

       订单{key,value}->{order_id,order.getTel_num() + "," + order.getAddress() 
       + ","+ order.getGoods_id() + "," + DateTime.now().toString("YYYY-MM-dd HH-mm-ss")}、
       
       支付订单{key,value}->{order_id + "_pay",(0 or 1)}
       
       库存{key,value}->{goods_id,(Integer)}



四、项目开发

由于模块有主次之分，主要写一下关于主要功能模块的开发：
       
       (1)[管理员商品管理模块](https://blog.csdn.net/xc1158840657/article/details/79901031) 
       
       (2)[商品抢购模块](https://blog.csdn.net/xc1158840657/article/details/79912822)
       
       (3)[支付模块](https://blog.csdn.net/xc1158840657)
       




如有不足之处望指出，欢迎交流学习！
