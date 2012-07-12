<head>
	<title>业务配置</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<ul class="nav nav-tabs">
					<@s.iterator value="environments" var="env">
					<li<@s.if test="%{#env.id == envId}"> class="active"</@s.if>>
						<a href="<@s.url value="/config/configList"/>">${name}</a>
					</li>
					</@s.iterator>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<table class="table table-bordered table-striped table-condensed">
					<thead>
				    <tr>
				      <th>序号</th>
				      <th>KEY</th>
				      <th>VALUE</th>
				      <th>操作</th>
				    </tr>
				  </thead>
				  <tbody>
				  	<@s.iterator value="configInsts" status="instStatus">
				  	<tr>
				  		<td>${instStatus.index}</td>
				  		<td>${config.key}</td>
				  		<td><@s.property value="instance.value"/></td>
				  		<td>
				  			<i class="icon-edit"></i>
				  			<i class="icon-remove"></i>
				  		</td>
				  	</tr>
				  	</@s.iterator>
				  </tbody>
				</table>
			</div>
		</div>
	</div>
</body>
