<head>
	<title>业务配置</title>
	<script type="text/javascript" src="<@s.url value="/js/biz/config/config-list.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/biz/config/config-list-editor.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/biz/config/config-map-editor.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/biz/service/service.js"/>"></script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<input type="hidden" id="projectName" name="projectName" value="${project.name}"/>
				<ul class="noborder smallpadding smallbottommargin breadcrumb">
				  <li>
				    ${project.product.team.name}<span class="divider">></span>
				  </li>
				  <li>
				    ${project.product.name}<span class="divider">></span>
				  </li>
				  <li>
				    ${project.name}<span class="divider">></span>
				  </li>
				  <li>配置列表<span class="divider">></span></li>
				  <li class="active">${environment.label}</li>
				</ul>
			</div>
		</div>
		<#assign queryStr="menu=" + menu + "&pid=" + pid + "&envId=" + envId />
		<#assign criteriaStr="criteria.key=" + criteria.key + "&criteria.value=" + criteria.value + "&criteria.hasValue=" + criteria.hasValue />
		<div class="row">
			<div class="span12">
				<ul class="nav nav-tabs">
					<@s.iterator value="environments" var="env">
					<li<@s.if test="%{#env.id == envId}"> class="active"</@s.if>>
						<a href="<@s.url action="configList" namespace="/config"/>?menu=${menu}&pid=${pid}&envId=${id}&${criteriaStr}">${label}</a>
					</li>
					</@s.iterator>
				</ul>
			</div>
		</div>
		<@s.hidden id="queryStr" value="${queryStr}"/>
		<@s.hidden id="criteriaStr" value="${criteriaStr}"/>
		<@s.hidden name="projectId" id="projectId" value="${pid}"/>
		<@s.hidden name="projectName" id="projectName" value="${project.name}"/>
		<div class="row">
			<div class="span12">
			<@s.form cssClass="form-inline lion" action="/config/configList.vhtml" method="get">
				<@s.hidden name="menu"/><@s.hidden name="pid"/><@s.hidden name="envId" id="envId"/>
				<label class="control-label" for="key">KEY：</label>
				<@s.textfield id="key" name="criteria.key" cssClass="input-medium" maxlength="50"/>
				<label class="control-label" for="value">VALUE：</label>
				<@s.textfield id="value" name="criteria.value" cssClass="input-medium" maxlength="50"/>
				<label class="control-label" for="status">Status：</label>
				<@s.select id="status" name="criteria.hasValue" list="%{@com.dianping.lion.vo.HasValueEnum@values()}" listKey="value"
					listValue="label" value="criteria.hasValue" cssClass="input-medium"/>
				<button type="submit" class="btn">搜索</button>
			</@s.form>
			</div>
		</div>
		<div class="row">
			<div class="span12" style="padding:1px;">
				<strong>配置属性：</strong>
				<i class="icon-edit icon-intro" data-original-title="编辑配置值" data-content="编辑配置项在当前环境下的设定值."></i>
				<i class="icon-list-alt icon-intro" style="margin-left: 10px;" data-original-title="编辑配置属性" data-content="编辑配置项除值以外的其他属性值."></i>
				<i class="icon-trash icon-intro" style="margin-left: 10px;" data-original-title="清除配置值" data-content="清除配置项在当前环境下的设定值."></i>
				<i class="icon-remove icon-intro" style="margin-left: 10px;" data-original-title="删除配置项" data-content="完全删除该配置项，所有环境下不再存在该配置项值."></i>
				<i class="icon-arrow-up icon-intro" style="margin-left: 10px;" data-original-title="上移" data-content="上移配置项的位置，将相关的配置项编排在一起."></i>
				<i class="icon-arrow-down icon-intro" style="margin-left: 10px;" data-original-title="下移" data-content="下移配置项的位置，将相关的配置项编排在一起."></i>
				<label class="pull-right" style="color:orange;"><b>${environment.label}</b></label>
			</div>
		</div>
		<#include "/WEB-INF/pages/common/message.ftl">
		<div class="row">
			<div class="span12" id="config-list-container">
				<#include "/WEB-INF/pages/config/config-list-table.ftl">
			</div>
		</div>
	</div>
	<br/>
    <#include "/WEB-INF/pages/service/service-config.ftl">
    <br/>
	<#include "/WEB-INF/pages/config/config-add-modal.ftl">
	<#include "/WEB-INF/pages/config/config-edit-modal.ftl">
	<#include "/WEB-INF/pages/config/config-attr-modal.ftl">
	<#include "/WEB-INF/pages/config/config-list-editor.ftl">
	<#include "/WEB-INF/pages/config/config-map-editor.ftl">
	<#include "/WEB-INF/pages/config/config-refdb-editor.ftl">
	<#include "/WEB-INF/pages/config/config-refshared-editor.ftl">
	<#include "/WEB-INF/pages/config/config-ref-list.ftl">
    <#include "/WEB-INF/pages/service/service-edit-modal.ftl">
</body>
