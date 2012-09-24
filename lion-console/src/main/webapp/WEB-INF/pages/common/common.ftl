<script type="text/javascript">
var contextpath = "${base}";
var dsProjectId = <@s.property value="%{@com.dianping.lion.ServiceConstants@PROJECT_DB_ID}"/>;
var sharedProjectId = <@s.property value="%{@com.dianping.lion.ServiceConstants@PROJECT_SHARED_ID}"/>;
<@s.iterator value="%{@com.dianping.lion.entity.ConfigStatusEnum@values()}" var="stat">var status_${stat.name()}_label = "${label}";</@s.iterator>
</script>
<script type="text/javascript" src="<@s.url value="/js/lion.js"/>"></script>