<#assign s=JspTaglibs["/WEB-INF/tld/struts-tags.tld"]>
<head>
	<title>404-页面NotFound</title>
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
				<img src="<@s.url value="/img/lion-404.png"/>" class="pull-right">
			</div>
			<div class="span3">
				<div class="alert alert-error" style="margin-left:-40px;margin-top:70px;">
					<h2>404</h2>
					<strong>Sorry, 页面不存在...</strong>
				</div>
			</div>
			<div class="span3">&nbsp;</div>
		<div>
	</div>
</body>