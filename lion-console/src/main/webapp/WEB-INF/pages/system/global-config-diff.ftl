<head>
    <title>配置比较</title>
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
                    配置比较
                </li>
            </ul>
		  <#include "/WEB-INF/pages/system/global-config-diff-form.ftl">
        </div>
    </div>
</div>
<@s.if test="tableDisplay">
<div class="container">
	 <#include "/WEB-INF/pages/system/global-config-diff-table.ftl">
</div>
</@s.if>
</body>
