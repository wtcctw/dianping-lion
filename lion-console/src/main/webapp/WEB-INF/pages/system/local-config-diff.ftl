<head>
	<title>配置比较</title>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="span12">
			<#include "/WEB-INF/pages/system/local-config-diff-form.ftl">
		</div>
	</div>
</div>
<div class="container">
	<div class="row">
		<div class="span12">
			<#include "/WEB-INF/pages/system/local-config-diff-info.ftl">
		</div>
	</div>
</div>
<div class="container">
	<div class="row">
		<div class="span12" id="config-list-container">
			<#include "/WEB-INF/pages/system/local-config-diff-table.ftl">
		</div>
	</div>
</div>
</body>
