<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<#assign decorator=JspTaglibs["/WEB-INF/tld/sitemesh-decorator.tld"]>
<#assign page=JspTaglibs["/WEB-INF/tld/sitemesh-page.tld"]>
<#assign s=JspTaglibs["/WEB-INF/tld/struts-tags.tld"]>
<#assign lion=JspTaglibs["/WEB-INF/tld/lion-tags.tld"]>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Lion-<@decorator.title default="Dianping配置中心"/></title>
	<link href="<@s.url value="/css/bootstrap.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/bootstrap-responsive.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/google-prettify.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/jquery-ui-1.8.21.custom.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/lion.css"/>" rel="stylesheet" type="text/css" />
	<link rel="shortcut icon" href="<@s.url value="/img/favicon.ico"/>">
	<#include "/WEB-INF/pages/common/common.ftl">
	<script type="text/javascript" src="<@s.url value="/js/jquery.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/jquery-ui-1.8.21.custom.min.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/bootstrap.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/lion.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/Mootools-core.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/my97/WdatePicker.js"/>"></script>
    <style type="text/css">
		body {
			position: relative;
			padding-top: 60px;
			padding-bottom: 10px;
		}
	</style>
    <@decorator.head/>
</head>
<body data-spy="scroll" data-target=".subnav" data-offset="50">
	<!-- Navbar================================================== -->
    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
			<button type="button"class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
	        <a class="brand" href="<@s.url value="/"/>">Lion</a>
	        <div class="nav-collapse">
		        <ul class="nav">
		        	<@lion.SubNav/>
		        	<!--
		        	<li><a href="#">配置项</a></li>
		        	<li><a href="#">人员管理</a></li>
		        	-->
		        </ul>
		        <ul class="nav pull-right">
		        	<li><a href="#">登录</a></li>
		        </ul>
	          	<ul class="nav pull-right">
	          		<@lion.MainNav/>
	          	</ul>
        	</div><!-- /.nav-collapse -->
        </div>
      </div>
    </div>
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12"><@decorator.body/></div>
		</div>
	</div>
	
	<div class="container">
		<footer class="footer"><center>©2012 Dianping平台架构, Mail: <a href="mailto:www@dianping.com">www@dianping.com</a></center></footer>
	</div>
</body>
</html>
