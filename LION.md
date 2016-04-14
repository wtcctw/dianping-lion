# Lion 使用文档
-----

## 1 Lion 简介

Lion 是一个配置管理平台，可以实时推送配置变更。

### 1.1 Lion 架构
![Lion 架构](http://code.dianpingoa.com/arch/lion/raw/master/lion-arch.png)

### 1.2 Lion 配置加载顺序
1. 环境变量
2. 本地配置文件
3. zookeeper

## 2 Lion 使用
### 2.1 引入 Maven 依赖
```xml
<dependency>
     <groupId>com.dianping.lion</groupId>
     <artifactId>lion-client</artifactId>
     <version>0.5.0</version>
</dependency>
```
### 2.2 定义 Lion 运行环境
#### 2.2.1 环境信息从以下文件读取
1. **$WAR_ROOT**/appenv
2. /data/webapps/appenv

**$WAR_ROOT**/appenv 的存在是为了兼容老版本，优先读取<br/>
如果文件不存在则读取 /data/webapps/appenv<br/>
如果这个文件也不存在，则默认 **dev** 环境。<br/><br/>
在 Windows 环境下，根据程序运行目录的不同，appenv 文件需要放在

* C:/data/webapps/appenv 或者
* D:/data/webapps/appenv 或者
* E:/data/webapps/appenv 等

#### 2.2.2 appenv 文件格式如下

<table><tbody>
<tr><td>deployenv</td><td>：环境，如 dev</td></tr>
<tr><td>zkserver</td><td>：ZK 服务器地址，如 dev.lion.dp:2181</td></tr>
<tr><td>swimlane</td><td>：泳道（可选），一般为空</td></tr>
</tbody></table>

#### 2.2.3 各环境标准 appenv 文件如下
* dev

```
deployenv=dev
zkserver=dev.lion.dp:2181
```

* alpha

```
deployenv=alpha
zkserver=alpha.lion.dp:2182
```

* qa

```
deployenv=qa
zkserver=qa.lion.dp:2181
```

### 2.3 API 使用方式

使用  Lion.setSwimlaneReadRollback(false) 设置为 false 则对应 swimlane 为 null 时不从 default 泳道中读取数值
使用  Lion.setReadFromCache(false) 设置为 false 则不从本地 cache 中读取，从数据源直接拿数据

```java
public class Lion {

    public static String get(String key)

    public static String get(String key, String defaultValue)

    public static String getStringValue(String key)

    public static String getStringValue(String key, String defaultValue)

    public static byte getByteValue(String key)

    public static byte getByteValue(String key, byte defaultValue) 

    public static short getShortValue(String key)

    public static short getShortValue(String key, short defaultValue)

    public static int getIntValue(String key)

    public static int getIntValue(String key, int defaultValue)

    public static long getLongValue(String key)

    public static long getLongValue(String key, long defaultValue)

    public static float getFloatValue(String key)

    public static float getFloatValue(String key, float defaultValue)

    public static double getDoubleValue(String key)

    public static double getDoubleValue(String key, double defaultValue)

    public static boolean getBooleanValue(String key)

    public static boolean getBooleanValue(String key, boolean defaultValue) 

    public static void addConfigChangeListener(ConfigChange configChange)

    public static void removeConfigChangeListener(ConfigChange configChange)
    
    public static void setSwimlaneReadRollback(Boolean swimlaneReadRollback)
    
    public static void setReadFromCache(Boolean readFromCache)

}
```

### 2.4 Spring 集成
#### 2.4.1 初始化 Lion

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:context="http://www.springframework.org/schema/context"
            xmlns:tx="http://www.springframework.org/schema/tx" 
            xmlns:lion="http://code.dianping.com/schema/lion"
            xsi:schemaLocation="
                http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
                http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd
                http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
                http://code.dianping.com/schema/lion http://code.dianping.com/schema/lion/lion-1.0.xsd">

    <context:component-scan base-package="com.dianping" />
    
    <lion:config />

    <bean name="configHolder" class="com.dianping.lion.ConfigHolder">
        <property name="local" value="${lion-test.local}" />
        <property name="remote" value="${lion-test.remote}" />
    </bean>
    
</beans>
```

\<lion:config /\> 可以指定属性：

* propertiesPath 用于指定自定义的配置文件的位置，默认值是`config/applicationContext.properties`
* includeLocalProps 用于指定是否需要在线上环境使用自定义配置文件中的值，默认值是 `false`
* order 用于指定 Lion 配置的加载顺序

#### 2.4.2 自定义配置文件说明
applicationContext.properties 有两个功能：

1. 配置覆盖
配置覆盖只在dev和alpha环境有效，其它环境自动失效，除非在 Lion 初始化时指定`includeLocalProps=true`
比如在配置文件中指定:`lion-test.local=local`
可以用上面的 key-value 覆盖dev环境开发时 Lion 系统上的配置，可以在本地开发调试时自己定义自己的配置信息，在上其他测试环境和生产环境时自动失效，避免忘记删除此配置导致其他环境使用自定义配置信息
2. 配置引用
<br/>配置引用在所有环境中有效，比如在配置文件中指定:`lion-test.local=${lion-test.remote}`
这时在 Spring 配置文件中遇到占位符`${lion-test.local}`时，会自动使用`${lion-test.remote}`的配置

#### 2.4.3 通过占位符使用 Lion 配置

如上面的例子所示，在 Spring 配置文件中通过占位符 **${lion-test.local}** 来使用 Lion 上的配置

#### 2.4.4 通过 annotation 使用 Lion 配置
```java
public class ConfigHolder {

    private String local;
    @LionConfig("lion-test.remote")
    private String remote;
    @LionConfig(":mmmmmm")
    private String multi;

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getMulti() {
        return multi;
    }

    public void setMulti(String multi) {
        this.multi = multi;
    }

}
```

Annotation 为 **@LionConfig**，需要指定在 field 上。可以通过 Annotation 指定 key 和默认值。<br/>

**@LionConfig** 的 pattern 如下：

* @LionConfig("key") 指定 key，没有默认值
* @LionConfig("key:defaultValue") 指定 key 和默认值，当 key 不存在时使用默认值
* @LionConfig(":defaultValue") 只指定默认值，key 由 Lion 生成，生成规则见下
* @LionConfig 什么都不指定，key 由 Lion 生成<br/>

Lion 生成 key 的前提是必须要有 app.name，规则如下：<br>

```
key = app.name + "." + (camel case => dotted string)
```

假设 app.name = "lion-test"，示例如下：

<table><tbody>
<tr><td>Field</td><td> Key</td></tr>
<tr><td>zookeeperAddress</td><td>=> lion-test.zookeeper.address</td></tr>
<tr><td>propertiesPath</td><td>=> lion-test.properties.path</td></tr>
<tr><td>includeLocalProps</td><td>=> lion-test.include.local.props</td></tr>
</tbody></table>


## 3 Lion-API 接口说明

### 3.1.lion sso 权限验证

为了保证接口的调用安全，调用 lion-api 需要通过 sso 接口获得 token 或者 使用点评账号密码调用 lion-api
下面两种调用方式二选一

当使用下面两种方式中任意一种调用 lion-api 接口的时候，任何接口中所需的参数 id 可以不必填写。

#### 3.1.1 传递access_token 方法:
从 sso 获得 token 的文档参考 5.2 节: http://code.dianpingoa.com/ba-base/cas-server/tree/master
	SSO 需要的参数  
    		client_id=lion_api
    		client_secret=4qSXANX2wHdMVzcr
    		
获得 token 之后在 调用时增加参数 access_token=XXXXXX 
例如 http://lionapi.dp:8080/product2/list?team=架构&access_token=XXXXX

#### 3.1.2.直接传递账号密码的方法:
在调用lion-api的接口的时候添加参数account和参数password
	account为你的点评账号
	password为你的密码
	例如 http://lionapi.dp:8080/product2/list?team=架构&account=myaccount&password=mypassword

### 3.2.Lion-API 接口

#### 公共参数
	1. id 标识调用者身份，用于记录操作日志 例如 id=2
	2. env 指定环境 可选值：dev | alpha | qa | prelease | product | performance

#### 返回值
	
	返回值是一个 json 格式的对象，字段如下
	1. status 值为 success 或者 error
	2. message 具体出错信息
	3. result 调用返回值， json 格式
#### 团队接口
	- 路径 /team2
	- 动作 
		/list 列出团队 
	- 样例 http://lionapi.dp:8080/team2/list
	
#### 产品线接口
	- 路径 /product2
	- 动作
		/list 获取团队列表
	- 参数
		team
	- 样例 http://lionapi.dp:8080/product2/list?team=架构
	
#### 项目管理接口
	- 路径 /project2
	- 动作
		/list 	获取项目列表 	参数 product, team
		/create 创建项目 		参数 id, project, product, owner
		/delete 删除项目 		参数 id, project
		/update 更新项目 		参数 id, project, product, name, owner, member, operator
	- 参数
		project 	项目名 		project=lion
		team		团队名		team=架构
		product		产品线名		product=中间件
		owner		项目负责人	owner=chen.hua
		member		项目成员		member=chen.hua
		operator	项目运维		operator=chen.hua
		name		新项目名		name=pigeon2
	- URL样例
		* 创建项目 http://lionapi.dp:8080/project2/create?id=2&project=lion-xxx&product=中间件&owner=enlight.chen
		* 获取项目列表：http://lionapi.dp:8080/project2/list?product=中间件
#### 配置管理接口
	- 路径 /config2
	- 动作
		/get	获取配置			参数 env, id, key, keys, prefix, group
		/set 	设置配置			参数 env, id, key, group, value
		/list 	获取配置列表		参数 prefix
		/create 创建配置			参数 id, project, key, desc
	- 参数
		key		配置名		样例 key=pigeon.timeline.enabled
		keys	配置列表		样例 keys=pigeon.timeline.enabled,pigeon.monitor.enabled
		prefix	配置前缀		样例 prefix=pigeon
		group 	泳道名		样例 group=tuangou
		value	配置值		样例 value=enabled
	- URL 样例：
		* 获取配置列表：http://lionapi.dp:8080/config2/list?prefix=pigeon
		* 获取单个配置值：http://lionapi.dp:8080/config2/get?env=dev&id=2&key=pigeon.timeline.enabled
		* 获取多个配置值：http://lionapi.dp:8080/config2/get?env=dev&id=2&keys=pigeon.timeline.enabled,pigeon.monitor.enabled
		* 获取项目配置值：http://lionapi.dp:8080/config2/get?env=dev&id=2&prefix=pigeon
		* 设置配置：http://lionapi.dp:8080/config2/set?env=dev&id=2&key=lion-test.host&value=1.1.1.1
		* 创建配置：http://lionapi.dp:8080/config2/create?id=2&project=lion-test&key=lion-test.xxx&desc=xxx		

#### 密文，明文转换接口
    - URL 样例:
    	* 获取明文：http://lionapi.dp:8080/encode?text=abcde
    	* 获取密文：http://lionapi.dp:8080/decode?text=~{f4f7f2d48796b0f270f65900f0f0e6b64f10396}

## 4 分布式锁使用
### 4.1 分布式锁使用事项
    锁的个数要 O(1) 不能随着请求的增长而增长
### 4.2 分布式锁使用方式
    以项目名加一个前缀"/"来命名 
    总共两种锁 ReentrantLock 和 ReentrantReadWriteLock 含义与 Java 中的锁相同
    使用方式
    String lockName = "/testlock";
    lock = new ReentrantLock(lockName);
    包名 com.dianping.lion.lock 
   
## 5 接手美团的项目的配置问题
### 5.1 有默认值的配置 比如 ${localKey:abc} 
    在lion 的地方 加上    <property name="useDefaultValue" value="true" />
### 5.2 美团侧不同环境 import 不同的配置文件
    参考 http://stackoverflow.com/questions/16481206/spring-property-placeholder-not-working
