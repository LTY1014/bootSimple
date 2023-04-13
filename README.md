## bootSimple

### 简介

快速生成springboot简单后端项目，满足基本web开发

内容如下：

- AOP(鉴权、请求)
- 封装基本请求
- 数据库MySQL+MybatisPlus
- 异常处理
- 工具类(IP、密码、雪花算法)
- 接入API文档



表结构：user+book

> 说明

数据库+后端 `全部是驼峰命名！！！` 

主键默认都是自增 (可修改为雪花算法)

后端请求用到的是session(前端要种cookie来保存一致性)

鉴权设计主要是userRole来判断(未考虑大型项目复杂化)

生成新表可参考book表设计完成(根据项目需要，可不实现DTO和VO)



### 快速上手

> 可以直接查找todo，根据提示使用

- yml

修改bootSimple(application.name)

修改mysql配置




## Controller

### Index

/ 默认

/hello    带参数访问

/getPort

/resource  	资源页

/adminResource  管理员资源页

/getIp   获取IP地址


## 全局异常

### ErrorCode

```
SUCCESS(0, "ok"),
PARAMS_ERROR(40000, "请求参数错误"),
NOT_LOGIN_ERROR(40100, "未登录"),
NO_AUTH_ERROR(40101, "无权限"),
NOT_FOUND_ERROR(40400, "请求数据不存在"),
FORBIDDEN_ERROR(40300, "禁止访问"),
SYSTEM_ERROR(50000, "系统内部异常"),
OPERATION_ERROR(50001, "操作失败");
```



### 异常工具类

ThrowUtils    满足条件抛出异常

## 增删改查

请求最好实现序列化接口

```
public class PageRequest implements Serializable
```



### 新建表

```
create table book
(
    id         bigint comment 'id'
        primary key,
    bookName   varchar(256)                       null comment '书名称',
    author     varchar(256)                       not null comment '作者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '书';
```



### 项目使用

1.编写表结构

2.插件(mybatis-X)生成

3.文件拷贝(注意加TableLogic及修改文件路径)

4.添加验证规则

5.请求、查询、修改的各请求标识

6.替换变量(注意大小写)



## utils

### id生成器

- tableId使用

```
@Id
    @TableId
    @ApiModelProperty(value = "唯一标识")
    private String id = SnowFlakeUtil.nextId().toString();
```



## 用户设计

### 用户表设计

```
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                           null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userRole     varchar(256) default 'user'            not null comment '用户角色',
    userName     varchar(256)                           null comment '用户昵称',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    gender       tinyint      default 1                 null comment '性别 0-未知;1-男;2-女',
    phone        varchar(128)                           null comment '电话',
    email        varchar(512)                           null comment '邮箱',
    userStatus   int          default 0                 not null comment '状态 0 - 正常',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint      default 0                 not null comment '是否删除'
)
    comment '用户';
```



### 注册设计

1. 用户在前端输入账户和密码、以及校验码（todo）
2. 校验用户的账户、密码、校验密码，是否符合要求
   1. 非空
   2. 账户长度 **不小于** 4 位
   3. 密码就 **不小于** 6位
   4. 账户不能重复
   5. 账户不包含特殊字符？
   6. 密码和校验密码相同？
3. 对密码进行加密（密码千万不要直接以明文存储到数据库中）
4. 向数据库插入用户数据



### 用户登录

1. 校验用户账号和密码是否合理
2. 校验密码是否正确
3. 用户是否存在
4. 记录用户的登录态(session)

```
request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
```

4. 返回用户信息(controller脱敏)



### 获取当前用户

1. 判断用户是否已经登录
2. 从数据库查询(可加缓存)
3. 返回用户



### 是否管理员

根据HttpServletRequest判断返回布尔值



### 注销用户

1. 判断用户是否登录
2. 移除登录态



### 用户修改

个人或管理员才可修改





