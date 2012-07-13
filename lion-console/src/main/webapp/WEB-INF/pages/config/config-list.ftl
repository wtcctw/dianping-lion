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
		<div class="row">
			<div class="span12">
				<table class="table table-bordered table-striped table-condensed">
					<thead>
				    <tr>
				      <th width="80">序号</th>
				      <th width="250">KEY</th>
				      <th>VALUE</th>
				      <th width="80">Status</th>
				      <th width="120">操作</th>
				    </tr>
				  </thead>
				  <tbody>
				  	<@s.iterator value="configInsts" var="inst" status="instStatus">
				  	<tr>
				  		<td>${instStatus.index}</td>
				  		<td>
				  			<@s.if test="%{#inst.existsInstance()}">
				  				<@s.if test="instance.active">${config.key}</@s.if>
				  				<@s.else><font color="#C67605">${config.key}</font></@s.else>
				  			</@s.if>
				  			<@s.else>
				  			<font color="#D14836"><s>${config.key}</s></font>
				  			</@s.else>
				  			
				  		</td>
				  		<td><@s.property value="instance.value"/></td>
				  		<td>
				  			<@s.if test="%{#inst.existsInstance()}">
				  				<@s.if test="instance.active">已生效</@s.if><@s.else><span style="color:#C67605;">待生效</span></@s.else>
				  			</@s.if>
				  			<@s.else><font color="#D14836">未设置</font></@s.else>
				  		</td>
				  		<td>
				  			<i class="icon-edit"></i>
				  			<i class="icon-remove"></i>
				  			<a href="<@s.url action="configMoveUp" namespace="/config"/>?${queryStr}&configId=${config.id}"><i class="icon-arrow-up"></i></a>
				  			<a href="<@s.url action="configMoveDown" namespace="/config"/>?${queryStr}&configId=${config.id}"><i class="icon-arrow-down"></i></a>
				  		</td>
				  	</tr>
				  	</@s.iterator>
				  </tbody>
				</table>
			</div>
		</div>
	</div>
</body>
