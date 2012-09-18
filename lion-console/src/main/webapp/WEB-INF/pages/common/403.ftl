<#assign s=JspTaglibs["/WEB-INF/tld/struts-tags.tld"]>
<head>
	<title>403-无权限</title>
</head>
<body>
	<div class="container" style="min-height:550px;">
		<div class="row" style="height:50px;">
			<div class="span12">
			</div>
		</div>
		<div class="row">
			<div class="span3">&nbsp;</div>
			<div class="span3">
				<img src="<@s.url value="/img/lion-403.png"/>" class="pull-right">
			</div>
			<div class="span3">
				<div class="alert alert-error" style="margin-left:-40px;margin-top:70px;">
					<h2>403</h2>
					<strong>无权访问, 确认是否登陆或联系管理员授权...</strong>
				</div>
			</div>
			<div class="span3">&nbsp;</div>
		<div>
	</div>
</body>