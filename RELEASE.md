# Lion Release Notes

### 0.5.0
* 增加了 @LionConfig Annotation 支持
* 增加了 Lion 包装类，方便代码直接使用，不再需要使用 ConfigCache.getInstance()
* 增加了 Lion 自身的 xmlns 和 schema，可以使用 <lion:config/> 和 Spring 集成
* 对于公用组件的配置（如 pigeon，avatar-cache 的配置），支持优先取用项目相关的值，即会优先去找 appName.pigeon.xxx 的配置
* 增加了 Lead 选举的实现，用于分布式选举 Leader
* 优化 zookeeper 连接管理，周期性检测 zookeeper 连接，对于假死的连接会自动重连
* 修复了一个创建配置时，监听不到配置的 bug

### 0.4.8
* 分离 Lion 日志，记录到独立文件 /data/applogs/lion/lion.log
* 增加 Cat 打点，记录 zookeeper 连接状态的改变
* 优化不存在的 key，不会每次都去 zookeeper 取
* 优化周期性的配置同步

### 0.4.7
* 使用 Curator 替换自有实现管理 Lion 和 zookeeper的连接
* 重构原有读取 appenv 文件的方式
* 移除 pigeon 原有代码
* 使用 slf4j 替换 log4j





