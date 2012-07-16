<div class="row">
	<div class="span12">
		<ul class="noborder smallpadding smallbottommargin breadcrumb">
			  <li>
			    系统设置 <span class="divider">></span>
			  </li>
			  <li>
			    <a href="<@s.url action='projectList' namespace='/system'/>">项目设置</a> <span class="divider">></span>
			  </li>
			  <li class="active">
			  	<#if active == "project">
					项目
				<#elseif active == "product">
					产品线
				<#elseif active == "team">
					业务团队
				</#if>
			 </li>
		</ul>
		<ul class="nav nav-tabs">
			
		    <#if active == "project">
				<li class="active">
			<#else>
				<li>
			</#if>
		    
		    <a href="<@s.url action='projectList' namespace='/system'/>">项目</a></li>
		    <#if active == "product">
				<li class="active">
			<#else>
				<li>
			</#if>
			<a href="<@s.url action='productList' namespace='/system'/>" >产品线</a></li>
		    <#if active == "team">
				<li class="active">
			<#else>
				<li>
			</#if>
			<a href="<@s.url action='teamList' namespace='/system'/>" >业务团队</a></li>
	 	</ul>
	</div>
</div>