<head>
	<title>项目设置</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<ul class="noborder smallpadding smallbottommargin breadcrumb">
					  <li>
					    系统设置 <span class="divider">></span>
					  </li>
					  <li>
					    <a href="<@s.url action="projectList" namespace="/system"/>">项目设置</a> <span class="divider">></span>
					  </li>
					  <li class="active">项目</li>
				</ul>
				<ul class="nav nav-tabs">
					<li
				    <#if active="project">
						class="active"
					<#else>
					>
				    
				    
				    <a href="<@s.url action="projectList" namespace="/system"/>">项目</a></li>
				    <li><a href="<@s.url action="productList" namespace="/system"/>" >产品线</a></li>
				    <li><a href="<@s.url action="teamList" namespace="/system"/>" >业务团队</a></li>
			 	</ul>
			</div>
		</div>
	</div>
</body>
