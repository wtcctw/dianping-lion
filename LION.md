# Lion 使用文档
-----

## 1 Lion 简介

Lion 是一个配置管理平台，可以实时推送配置变更。

### 1.1 Lion 架构
![Lion 架构](http://code.dianpingoa.com/arch/lion/blob/master/lion-arch.png)

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

<table>
<tr><td>deployenv</td><td>：环境，如 dev</td></tr>
<tr><td>zkserver</td><td>：ZK 服务器地址，如 dev.lion.dp:2181</td></tr>
<tr><td>swimlane</td><td>：泳道（可选），一般为空</td></tr>
</table>

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
<br/>配置覆盖只在dev和alpha环境有效，其它环境自动失效，除非在 Lion 初始化时指定`includeLocalProps=true`<br/>
比如在配置文件中指定:`lion-test.local=local`<br/>
可以用上面的 key-value 覆盖dev环境开发时 Lion 系统上的配置，可以在本地开发调试时自己定义自己的配置信息，在上其他测试环境和生产环境时自动失效，避免忘记删除此配置导致其他环境使用自定义配置信息
2. 配置引用
<br/>配置引用在所有环境中有效，比如在配置文件中指定:`lion-test.local=${lion-test.remote}`<br/>
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

<table>
<tr><td>Field</td><td> Key</td></tr>
<tr><td>zookeeperAddress</td><td>=> lion-test.zookeeper.address</td></tr>
<tr><td>propertiesPath</td><td>=> lion-test.properties.path</td></tr>
<tr><td>includeLocalProps</td><td>=> lion-test.include.local.props</td></tr>
</table>


