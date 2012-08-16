<#--内容栏-->
<table id="table-env-list"
	class="table table-bordered table-striped table-condensed">
	<thead>
		<tr>
			<th style="width: 30px">序号</th>
			<th style="width: 100px">Name</th>
			<th style="width: 100px">Label</th>
			<th>zookeeper地址</th>
			<th>顺序</th>
			<th style="width: 80px">操作
				<a data-toggle="modal" href="<@s.url action='envAddAjax' namespace='/system'/>"
				rel="tooltip" title="添加环境"> <i class="icon-plus pull-right" />&nbsp&nbsp</a></th>
		</tr>
	</thead>
	<tbody>
		<#list environmentList as environment>
		<tr>
			<td>${environment_index + 1}</td>
			<td>${environment.name}</td>
			<td>${environment.label}</td>
			<td>${environment.ips}</td>
			<td>${environment.seq}</td>
			<td style="text-align: center;">
			<a data-toggle="modal" href="<@s.url action='envEditAjax' namespace='/system'/>?envId=${environment.id}"
				rel="tooltip" title="修改环境"> <i class="icon-edit"></i> </a>
			&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
			<a class="deletelink" href="<@s.url action='envDelete' namespace='/system'/>?envId=${environment.id}"
				rel="tooltip" title="删除环境"> <i class="icon-remove"></i> </a></td>
		</tr>
		</#list>
	</tbody>
</table>
