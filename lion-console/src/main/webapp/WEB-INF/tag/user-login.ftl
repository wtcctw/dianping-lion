<@s.if test="currentUser == null"><a href="#" id="login_link">登录</a></@s.if>
<@s.else>
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">${currentUser.name} <b class="caret"></b></a>
	    <ul class="dropdown-menu">
	    	<!--
    		<li><a href="<@s.url value="/"/>">工作台</a></li>
    		<li><a href="<@s.url value="/"/>">操作日志</a></li>
    		-->
    		<li><a href="<@s.url value="/logout.vhtml"/>">登出</a></li>
	    </ul>
    </li>
</@s.else>