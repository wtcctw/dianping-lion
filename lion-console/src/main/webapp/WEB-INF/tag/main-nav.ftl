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
						<li><a href="<@s.url action="configList" namespace="/config"/>?c1=project&pid=${id}"><@s.property value="name"/></a></li>
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
    <li><a href="#"><@s.property value="name"/></a></li>
    </@s.iterator>
    <@s.if test="!#productStatus.last"><li class="divider"></li></@s.if>
    </@s.iterator>
  	</ul>
	</@s.else>
</li>
</@s.iterator>
