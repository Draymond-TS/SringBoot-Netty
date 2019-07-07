# Java聊天软件 高性能后台+移动客户端  

## 技术栈
> 前端：MUI H5+  
> 后端：Springboot netty mybatis Hikari  
> 工具: fastFDS nginx 


## 数据库
<details><summary>数据表设计</summary> 


```sql
DROP TABLE IF EXISTS `chat_msg`;
CREATE TABLE `chat_msg` (
  `id` varchar(64) NOT NULL,
  `send_user_id` varchar(64) NOT NULL,
  `accept_user_id` varchar(64) NOT NULL,
  `msg` varchar(255) NOT NULL,
  `sign_flag` int(1) NOT NULL COMMENT '消息是否签收状态\r\n1：签收\r\n0：未签收\r\n',
  `create_time` datetime NOT NULL COMMENT '发送请求的事件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
> Table structure for friends_request
```sql
DROP TABLE IF EXISTS `friends_request`;
CREATE TABLE `friends_request` (
  `id` varchar(64) NOT NULL,
  `send_user_id` varchar(64) NOT NULL,
  `accept_user_id` varchar(64) NOT NULL,
  `request_date_time` datetime NOT NULL COMMENT '发送请求的事件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
> Table structure for my_friends
```sql
DROP TABLE IF EXISTS `my_friends`;
CREATE TABLE `my_friends` (
  `id` varchar(64) NOT NULL,
  `my_user_id` varchar(64) NOT NULL COMMENT '用户id',
  `my_friend_user_id` varchar(64) NOT NULL COMMENT '用户的好友id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `my_user_id` (`my_user_id`,`my_friend_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
> Table structure for users
```sql
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(64) NOT NULL,
  `username` varchar(20) NOT NULL COMMENT '用户名，账号，慕信号',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `face_image` varchar(255) NOT NULL COMMENT '我的头像，如果没有默认给一张',
  `face_image_big` varchar(255) NOT NULL,
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `qrcode` varchar(255) NOT NULL COMMENT '新用户注册后默认后台生成二维码，并且上传到fastdfs',
  `cid` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
</details>


## FastDFS安装以及使用

> [参考一下博客](https://blog.csdn.net/qq_31463999/article/details/82768466)


## 个人知识总结

> Spring事务
- 1.PROPAGATION_REQUIRED – 支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。
- 2.PROPAGATION_SUPPORTS – 支持当前事务，如果当前没有事务，就以非事务方式执行。
- 3.PROPAGATION_MANDATORY – 支持当前事务，如果当前没有事务，就抛出异常。
- 4.PROPAGATION_REQUIRES_NEW – 新建事务，如果当前存在事务，把当前事务挂起。
- 5.PROPAGATION_NOT_SUPPORTED – 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
- 6.PROPAGATION_NEVER – 以非事务方式执行，如果当前存在事务，则抛出异常。
- 7.PROPAGATION_NESTED – 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与PROPAGATION_REQUIRED类似的操作。
