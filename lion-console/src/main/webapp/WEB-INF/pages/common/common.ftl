<script type="text/javascript">
var contextpath = "${base}";
<@s.iterator value="%{@com.dianping.lion.entity.ConfigStatusEnum@values()}" var="stat">var status_${stat.name()}_label = "${label}";</@s.iterator>
</script>
<script type="text/javascript" src="<@s.url value="/js/lion.js"/>"></script>