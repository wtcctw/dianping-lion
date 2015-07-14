<head>
    <title>配置搜索</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="span12">
            <ul class="noborder smallpadding smallbottommargin breadcrumb">
                <li>
                    系统配置 <span class="divider">></span>
                </li>
                <li>
                    配置查询
                </li>
            </ul>
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
