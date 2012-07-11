<@s.iterator value="mainMenu.subMenuOrGroups" var="menuOrGroup">
	<@s.if test="%{#menuOrGroup instanceof com.dianping.lion.web.tag.MenuManager$SubMenu}">
	<li><a href="<@s.url value="%{#menuOrGroup.url.contains(\"?\") ? #menuOrGroup.url + \"&\" + query: #menuOrGroup.url + \"?\" + query}"/>">${label}</a></li>
	</@s.if>
	<@s.else>
		<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">${label} <b class="caret"></b></a>
		<ul class="dropdown-menu">
		<@s.iterator value="%{#menuOrGroup.menuOrGroups}" var="menuOrGroup_" status="itemStatus">
			<@s.if test="%{#menuOrGroup_ instanceof com.dianping.lion.web.tag.MenuManager$MenuGroup}">
				<li class="nav-header">${label}</li>
				<@s.iterator value="#menuOrGroup_.menuOrGroups" var="menu">
				<li><a href="<@s.url value="%{#menu.url.contains(\"?\") ? #menu.url + \"&\" + query: #menu.url + \"?\" + query}"/>">${label}</a></li>
				</@s.iterator>
				<@s.if test="!#itemStatus.last"><li class="divider"></li></@s.if>
			</@s.if>
			<@s.elseif test="%{#menuOrGroup_.seprator}">
				<li class="divider"></li>
			</@s.elseif>
			<@s.else>
				<li><a href="<@s.url value="%{#menuOrGroup_.url.contains(\"?\") ? #menuOrGroup_.url + \"&\" + query: #menuOrGroup_.url + \"?\" + query}"/>">${label}</a></li>
			</@s.else>
		</@s.iterator>
		</ul>
		</li>
	</@s.else>
</@s.iterator>


