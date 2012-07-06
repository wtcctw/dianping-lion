<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<#assign decorator=JspTaglibs["/WEB-INF/tld/sitemesh-decorator.tld"]>
<#assign page=JspTaglibs["/WEB-INF/tld/sitemesh-page.tld"]>
<#assign s=JspTaglibs["/WEB-INF/tld/struts-tags.tld"]>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<title><@decorator.title default="Dianping配置中心"/></title>
	<link href="<@s.url value="/css/bootstrap.min.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/bootstrap-responsive.min.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/google-prettify.css"/>" rel="stylesheet" type="text/css" />
	<link href="<@s.url value="/css/lion.css"/>" rel="stylesheet" type="text/css" />
	<link rel="shortcut icon" href="<@s.url value="/img/favicon.ico"/>">
    <style type="text/css">
		html,body{width:100%;height:100%}
	</style>
    <@decorator.head/>
</head>
<body data-spy="scroll" data-target=".subnav" data-offset="50">
	<!-- Navbar================================================== -->
    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
			<button type="button"class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
	        <a class="brand" href="./index.html">Lion</a>
	          
	        <ul class="nav pull-right">
	        	<li><a href="#">登录</a></li>
	        </ul>
          	
          	<ul class="nav pull-right">
          	<li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">主站 <b class="caret"></b></a>
              <ul class="dropdown-menu">
              	<li class="nav-header">shop</li>
                <li><a href="#">shop-web</a></li>
                <li><a href="#">shop-service</a></li>
                <li><a href="#">shopsearch-web</a></li>
                <li class="divider"></li>
                <li class="nav-header">user</li>
                <li><a href="#">user-web</a></li>
                <li><a href="#">user-service</a></li>
              </ul>
            </li>
          	<li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">团购 <b class="caret"></b></a>
              <ul class="dropdown-menu">
              	<li class="nav-header">tuangou</li>
                <li><a href="#">tuangou-api</a></li>
                <li><a href="#">tuangou-paygate</a></li>
                <li><a href="#">tuangou-wap</a></li>
                <li class="divider"></li>
                <li class="nav-header">businessCenter</li>
                <li><a href="#">bc-tuangou-web</a></li>
                <li><a href="#">saleproduct-web</a></li>
              </ul>
            </li>
          	<li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">手机 <b class="caret"></b></a>
               <ul class="dropdown-menu">
              	<li class="nav-header">mobile</li>
                <li><a href="#">mobile-api-web</a></li>
                <li><a href="#">mobile-recom-store</a></li>
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">架构 <b class="caret"></b></a>
              <ul class="dropdown-menu">
              	<li class="nav-header">soa</li>
                <li><a href="#">pigeon</a></li>
                <li><a href="#">cat</a></li>
                <li><a href="#">lion-console</a></li>
                <li class="divider"></li>
                <li class="nav-header">base</li>
                <li><a href="#">swallow</a></li>
                <li><a href="#">zebra</a></li>
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">系统选项 <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="#">产品线配置</a></li>
                <li><a href="#">系统设置</a></li>
                <li><a href="#">用户设置</a></li>
                <li><a href="#">权限设置</a></li>
                <li><a href="#">操作日志</a></li>
                <li class="divider"></li>
                <li><a href="#">关于Lion</a></li>
              </ul>
            </li>
          </ul>
          
        </div><!-- /.nav-collapse -->
      </div>
    </div>

    <div>
		<div class="clear_div">
			<@decorator.body/>
		</div>
	</div>
	
	<script type="text/javascript" src="<@s.url value="/js/jquery.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/js/lion.js"/>"></script>
</body>
</html>
