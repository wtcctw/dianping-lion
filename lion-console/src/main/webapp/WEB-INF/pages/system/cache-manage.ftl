<head>
	<title>缓存管理</title>
	<script type="text/javascript" src="<@s.url value="/js/biz/system/cachemanage.js"/>"></script>
</head>
<body>
	<ul class="noborder smallpadding smallbottommargin breadcrumb">
		<li>
		    系统设置 <span class="divider">></span>
		</li>
		<li>缓存管理</li>
	</ul>
	<br/>
	<table class="table table-bordered table-striped table-condensed">
		<thead>
	    <tr>
	      <th width="30">序号</th>
	      <th width="390">Name</th>
	      <th>CacheEntity</th>
	      <th width="100">操作</th>
	    </tr>
	    </thead>
	    <tbody>
		<@s.iterator value="caches" status="cacheStatus">
		<tr>
			<td>${cacheStatus.index + 1}</td>
			<td>${key}</td>
			<td>${value.cacheEntities}</td>
			<td>
				<a href="#" onClick="clearcache('${key}');" class="clear-btn no-dec">清除</a>
			</td>
		</tr>
		</@s.iterator>
		</tbody>
	</table>
</body>