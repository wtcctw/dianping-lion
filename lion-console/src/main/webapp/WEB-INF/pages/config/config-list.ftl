<head>
	<title>业务配置</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
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
				  <li class="active">配置项</li>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<ul class="nav nav-tabs">
					<@s.iterator value="environments" var="env">
					<li<@s.if test="%{#env.id == envId}"> class="active"</@s.if>>
						<a href="<@s.url action="configList" namespace="/config"/>?menu=${menu}&pid=${pid}&envId=${id}">${label}</a>
					</li>
					</@s.iterator>
				</ul>
			</div>
		</div>
		<#assign queryStr="menu=" + menu + "&pid=" + pid + "&envId=" + envId />
		<#assign criteriaStr="criteria.key=" + criteria.key + "&criteria.status=" + criteria.status />
		<div class="row">
			<div class="span12">
			<@s.form cssClass="form-inline lion" action="/config/configList.vhtml?${queryStr}">
				<label class="control-label" for="key">KEY：</label>
				<@s.textfield id="key" name="criteria.key" cssClass="input-medium"/>
				<label class="control-label" for="status">Status：</label>
				<@s.select id="status" name="criteria.status" list="%{@com.dianping.lion.entity.ConfigStatusEnum@values()}" listKey="value"
					listValue="label" headerValue="所有的" headerKey="-1" value="criteria.status" cssClass="input-medium"/>
				<button type="submit" class="btn">搜索</button>
			</@s.form>
			</div>
		</div>
		<div class="row">
			<div class="span12" style="padding:1px;">
				<strong>Tooltips：</strong>
				<i class="icon-remove icon-intro" data-original-title="清除配置值" data-content="清除指定配置项在当前环境下的设定值."></i>
				<i class="icon-trash icon-intro" style="margin-left: 10px;" data-original-title="删除配置项" data-content="完全删除该配置项，所有环境下不再存在该配置项值."></i>
				<i class="icon-arrow-up icon-intro" style="margin-left: 10px;" data-original-title="上移" data-content="上移配置项的位置，将相关的配置项编排在一起."></i>
				<i class="icon-arrow-down icon-intro" style="margin-left: 10px;" data-original-title="下移" data-content="下移配置项的位置，将相关的配置项编排在一起."></i>
			</div>
		</div>			
		<#include "/WEB-INF/pages/common/message.ftl">
		<div class="row">
			<div class="span12">
				<table class="table table-bordered table-striped table-condensed">
					<thead>
				    <tr>
				      <th width="80">序号</th>
				      <th width="250">KEY</th>
				      <th>VALUE</th>
				      <th width="80">Status</th>
				      <th width="120">
				      	操作
				      	<a href="<@s.url action="add" namespace="/config"/>?${queryStr}" rel="tooltip" data-original-title="添加配置项" class="pull-right">
				      		<i class="icon-plus"></i>
				      	</a>
				      </th>
				    </tr>
				  </thead>
				  <tbody>
				  	<@s.iterator value="configVos" var="configVo" status="configStatus">
				  	<tr>
				  		<td>${configStatus.index}</td>
				  		<td>
				  			<@s.if test="%{#configVo.hasInstance}">
				  				<@s.if test="%{#configVo.status.status >= @com.dianping.lion.entity.ConfigStatusEnum@Deployed.value}">${config.key}</@s.if>
				  				<@s.else><font color="#C67605">${config.key}</font></@s.else>
				  			</@s.if>
				  			<@s.else>
				  			<font color="#D14836"><s>${config.key}</s></font>
				  			</@s.else>
				  		</td>
				  		<td>
				  			<span style="margin-right: 20px;"><@s.property value="defaultInstance.value"/></span>
				  			<@s.if test="%{#configVo.hasInstance && #configVo.hasContextInst}">
				  			( <i class="icon-indent-left" rel="tooltip" data-original-title="存在基于上下文的配置项值"></i> )
				  			</@s.if>
				  		</td>
				  		<td>
				  			<@s.if test="%{#configVo.hasInstance}">
				  				<@s.if test="%{#configVo.status.status >= @com.dianping.lion.entity.ConfigStatusEnum@Deployed.value}">${status.statusEnum.label}</@s.if>
				  				<@s.else><span style="color:#C67605;">${status.statusEnum.label}</span></@s.else>
				  			</@s.if>
				  			<@s.else><font color="#D14836">未设置</font></@s.else>
				  		</td>
				  		<td>
				  			<i class="icon-edit"></i>
				  			<@s.if test="%{#configVo.hasInstance}">
				  			<a href="<@s.url action="clearInstance" namespace="/config"/>?${queryStr}&${criteriaStr}&configId=${config.id}" rel="tooltip" data-original-title="清除配置值">
				  				<i class="icon-remove"></i>
				  			</a>
				  			</@s.if>
				  			<a href="<@s.url action="delete" namespace="/config"/>?${queryStr}&configId=${config.id}" rel="tooltip" data-original-title="删除配置项">
				  				<i class="icon-trash"></i>
				  			</a>
				  			<a href="<@s.url action="configMoveUp" namespace="/config"/>?${queryStr}&configId=${config.id}" rel="tooltip" data-original-title="上移">
				  				<i class="icon-arrow-up"></i>
				  			</a>
				  			<a href="<@s.url action="configMoveDown" namespace="/config"/>?${queryStr}&configId=${config.id}" rel="tooltip" data-original-title="下移">
				  				<i class="icon-arrow-down"></i>
				  			</a>
				  		</td>
				  	</tr>
				  	</@s.iterator>
				  </tbody>
				</table>
			</div>
		</div>
	</div>
	<script language="javascript">
		$("[rel=tooltip]").tooltip();
		$(".icon-intro").popover();
	</script>
</body>
