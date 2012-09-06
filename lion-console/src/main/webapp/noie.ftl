<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<#assign s=JspTaglibs["/WEB-INF/tld/struts-tags.tld"]>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>404-页面NotFound</title>
	<link href="<@s.url value="/css/bootstrap.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/bootstrap-responsive.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/google-prettify.css"/>" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="container-fluid" style="min-height:550px;">
		<div class="row-fluid">
			<div class="span12">
				<div class="container" style="min-height:550px;">
					<div class="row" style="height:150px;">
						<div class="span12">
						</div>
					</div>
					<div class="row">
						<div class="span3">&nbsp;</div>
						<div class="span2">
							<img src="<@s.url value="/img/lion-noie.jpeg"/>" class="pull-right">
						</div>
						<div class="span4">
							<div class="alert alert-info" style="margin-left:-20px;margin-top:70px;">
								<h2>IE_NotSupported</h2>
								<strong>这货不支持IE, 赶紧换Chrome, Firefox, Safari吧</strong>
							</div>
						</div>
						<div class="span3">&nbsp;</div>
					<div>
				</div>
			</div>
		</div>
	</div>
</body>