<head>
	<title>配置搜索</title>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="span12">
			<#include "/WEB-INF/pages/system/global-search-form.ftl">
		</div>
	</div>
</div>
<@s.if test="hasSearched">
<div class="container">
	<div class="row">
		<div class="span12" id="config-list-container">
			<#include "/WEB-INF/pages/system/global-search-table.ftl">
		</div>
	</div>
</div>
</@s.if>
</body>
