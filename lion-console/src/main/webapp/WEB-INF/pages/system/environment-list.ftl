<#--内容栏-->
<table id="table-env-list"
	class="table table-bordered table-striped table-condensed">
	<thead>
		<tr>
			<th width="65">序号</th>
			<th width="100">Name</th>
			<th width="100">Label</th>
			<th>zookeeper地址</th>
			<th width="100">是否线上环境</th>
			<th width="100">顺序</th>
			<th width="100">操作
				<a data-toggle="modal" href="<@s.url action='envAddAjax' namespace='/system'/>"
				> <i class="icon-plus pull-right" rel="tooltip" title="添加环境"/>&nbsp&nbsp</a></th>
		</tr>
	</thead>
	<tbody>
		<#list environmentList as environment>
		<tr>
			<td>${environment_index + 1}</td>
			<td>${environment.name}</td>
			<td>${environment.label}</td>
			<td>${environment.ips}</td>
			<td><#if environment.online>是<#else>否</#if></td>
			<td>${environment.seq}</td>
			<td style="text-align: center;">
			<a data-toggle="modal" href="<@s.url action='envEditAjax' namespace='/system'/>?envId=${environment.id}"
				rel="tooltip" title="修改环境"><i class="icon-edit"></i></a>
			&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
			<a class="deletelink" href="<@s.url action='envDelete' namespace='/system'/>?envId=${environment.id}"
				rel="tooltip" title="删除环境"><i class="icon-remove"></i></a></td>
		</tr>
		</#list>
	</tbody>
</table>
