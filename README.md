# Java聊天软件 高性能后台+移动客户端  

## 技术栈
> 前端：MUI H5+  
> 后端：Springboot netty mybatis Hikari  
> 工具: fastFDS nginx mybatis-generator


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


## 个人总结

<details>
<summary>总结</summary>

> Spring事务
- 1.PROPAGATION_REQUIRED – 支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。
- 2.PROPAGATION_SUPPORTS – 支持当前事务，如果当前没有事务，就以非事务方式执行。
- 3.PROPAGATION_MANDATORY – 支持当前事务，如果当前没有事务，就抛出异常。
- 4.PROPAGATION_REQUIRES_NEW – 新建事务，如果当前存在事务，把当前事务挂起。
- 5.PROPAGATION_NOT_SUPPORTED – 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
- 6.PROPAGATION_NEVER – 以非事务方式执行，如果当前存在事务，则抛出异常。
- 7.PROPAGATION_NESTED – 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与PROPAGATION_REQUIRED类似的操作。

> DO VO BO DTO POJO的概念与区别
- 1.DAO（Data Access Object）数据访问对象，它是一个面向对象的数据库接口，负责持久层的操作，为业务层提供接口，主要用来封装对数据库的访问，常见操作无外乎 CURD。我们也可以认为一个 DAO 对应一个 POJO 的对象，它位于业务逻辑与数据库资源中间，可以结合 PO 对数据库进行相关的操作。
- 2.PO（Persistent Object）持久层对象，它是由一组属性和属性的get和set方法组成，最简单的 PO 就是对应数据库中某个表中的一条记录（也就是说，我们可以将数据库表中的一条记录理解为一个持久层对象），多个记录可以用 PO 的集合，PO 中应该不包含任何对数据库的操作。PO 的属性是跟数据库表的字段一一对应的，此外 PO 对象需要实现序列化接口。
- 3.BO（Business Object）业务层对象，是简单的真实世界的软件抽象，通常位于中间层。BO 的主要作用是把业务逻辑封装为一个对象，这个对象可以包括一个或多个其它的对象。举一个求职简历的例子，每份简历都包括教育经历、项目经历等，我们可以让教育经历和项目经历分别对应一个 PO，这样在我们建立对应求职简历的 BO 对象处理简历的时候，让每个 BO 都包含这些 PO 即可。
- 4.VO（Value Object）值对象，通常用于业务层之间的数据传递，和 PO 一样也是仅仅包含数据而已，但 VO 应该是抽象出的业务对象，可以和表对应，也可以不对应，这根据业务的需要。 如果锅碗瓢盆分别为对应的业务对象的话，那么整个碗柜就是一个值对象。此外，VO 也可以称为页面对象，如果称为页面对象的话，那么它所代表的将是整个页面展示层的对象，也可以由需要的业务对象进行组装而来。
- 5.DTO（Data Transfer Object）数据传输对象，主要用于远程调用等需要大量传输对象的地方，比如我们有一个交易订单表，含有 25 个字段，那么其对应的 PO 就有 25 个属性，但我们的页面上只需要显示 5 个字段，因此没有必要把整个 PO 对象传递给客户端，这时我们只需把仅有 5 个属性的 DTO 把结果传递给客户端即可，而且如果用这个对象来对应界面的显示对象，那此时它的身份就转为 VO。使用 DTO 的好处有两个，一是能避免传递过多的无用数据，提高数据的传输速度；二是能隐藏后端的表结构。常见的用法是：将请求的数据或属性组装成一个 RequestDTO，再将响应的数据或属性组装成一个 ResponseDTO.
- 6.POJO（Plain Ordinary Java Object）简单的 Java 对象，实际就是普通的 JavaBeans，是为了避免和 EJB（Enterprise JavaBean）混淆所创造的简称。POJO 实质上可以理解为简单的实体类，其中有一些属性及其getter和setter方法的类，没有业务逻辑，也不允许有业务方法，也不能携带有connection之类的方法。POJO 是 JavaEE 世界里面最灵活的对象，在简单系统中，如果从数据库到页面展示都是 POJO 的话，它可以是 DTO；如果从数据库中到业务处理中都是 POJO 的话，它可以是 BO；如果从数据库到整个页面的展示的话，它也可以是 VO.


</details>
