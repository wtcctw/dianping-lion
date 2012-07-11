<@s.iterator value="navMenus.menuOrGroups" var="menuOrGroup">
	<@s.if test="%{#menuOrGroup instanceof com.dianping.lion.web.tag.MenuManager$Menu}">
		<@s.if test="%{#menuOrGroup.name == 'project'}">
			<@s.iterator value="teams" var="team">
			<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown"><@s.property value="name"/> <b class="caret"></b></a>
				<@s.if test="%{#team.hasProjectsMoreThan(12)}">
				<ul class="dropdown-menu big-menu">
					<div class="pop-panel pp_location" id="G_loc-panel" style="visibility: visible; ">
						<@s.iterator value="#team.products" var="product">
						<dl>
							<dt><@s.property value="name"/></dt>
							<dd>
								<ul class="Fix">
									<@s.iterator value="#product.projects" var="project">
									<li><a href="<@s.url action="navigate" namespace="/"/>?menu=project&pid=${id}"><@s.property value="name"/></a></li>
									</@s.iterator>
								</ul>
							</dd>
						</dl>
						</@s.iterator>
					</div>
				</ul>
				</@s.if>
				<@s.else>
				<ul class="dropdown-menu">
				<@s.iterator value="#team.products" var="product" status="productStatus">
			  	<li class="nav-header"><@s.property value="name"/></li>
			  	<@s.iterator value="#product.projects" var="project">
			    <li><a href="<@s.url action="navigate" namespace="/"/>?menu=project&pid=${id}"><@s.property value="name"/></a></li>
			    </@s.iterator>
			    <@s.if test="!#productStatus.last"><li class="divider"></li></@s.if>
			    </@s.iterator>
			  	</ul>
				</@s.else>
			</li>
			</@s.iterator>
		</@s.if>
		<@s.else>
			<li><@s.if test="%{#menuOrGroup.hasSubMenu()}"><a href="<@s.url action="navigate" namespace="/"/>?menu=${name}"></@s.if>
			<@s.else><a href="<@s.url value="%{#menuOrGroup.url}"/>"></@s.else><@s.property value="label"/></a></li>
		</@s.else>
	</@s.if>
	<@s.else>
		<li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><@s.property value="label"/> <b class="caret"></b></a>
          <ul class="dropdown-menu">
          	<@s.iterator value="#menuOrGroup.menuOrGroups" var="menuOrGroup_" status="itemStatus">
	          	<@s.if test="%{#menuOrGroup_ instanceof com.dianping.lion.web.tag.MenuManager$MenuGroup}">
	          	<li class="nav-header"><@s.property value="label"/></li>
	          		<@s.iterator value="#menuOrGroup_.menuOrGroups" var="menu">
	          		<li>
	          			<@s.if test="%{#menu.hasSubMenu()}"><a href="<@s.url action="navigate" namespace="/"/>?menu=${name}"></@s.if>
	          			<@s.else><a href="<@s.url value="%{#menu.url}"/>"></@s.else>
	          			<@s.property value="label"/></a>
	          		</li>
	          		</@s.iterator>
	          		<@s.if test="!#itemStatus.last"><li class="divider"></li></@s.if>
	          	</@s.if>
	          	<@s.elseif test="%{#menuOrGroup_.seprator}">
	          	<li class="divider"></li>
	          	</@s.elseif>
	          	<@s.else>
	          	<li>
	          		<@s.if test="%{#menuOrGroup_.hasSubMenu()}"><a href="<@s.url action="navigate" namespace="/"/>?menu=${name}"></@s.if>
	          		<@s.else><a href="<@s.url value="%{#menuOrGroup_.url}"/>"></@s.else>
	          		<@s.property value="label"/></a>
	          	</li>
	          	</@s.else>
            </@s.iterator>
          </ul>
        </li>
	</@s.else>
</@s.iterator>


