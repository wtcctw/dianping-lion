<head>
	<title>配置比较</title>
</head>
<body>
<div class="container">
	<#include "/WEB-INF/pages/system/global-config-diff-form.ftl">
</div>
<@s.if test="tableDisplay">
<div class="container">
	<#include "/WEB-INF/pages/system/global-config-diff-table.ftl">
</div>
</@s.if>
</body>
