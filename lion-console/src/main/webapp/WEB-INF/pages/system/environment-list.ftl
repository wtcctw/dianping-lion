<#assign lion=JspTaglibs["/WEB-INF/tld/lion-tags.tld"]>
<#--内容栏-->
<table id="table-env-list"
	class="table table-bordered table-striped table-condensed">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="100">Name</th>
			<th width="100">Label</th>
			<th>zookeeper地址</th>
			<th width="100">是否线上环境</th>
			<th width="50">顺序</th>
			<th width="80">操作
				<@lion.Security resource="res_env_add">
				<a data-toggle="modal" href="<@s.url action='envAddAjax' namespace='/system'/>">
					<i class="icon-plus pull-right" rel="tooltip" title="添加环境"></i>
				</a>
				</@lion.Security>
			</th>
		</tr>
	</thead>
	<tbody>
		<#list environmentList as environment>
		<tr class="env-row">
			<td>
				${environment_index + 1}
				<input type="hidden" name="env-id" value="${environment.id}"/>
			</td>
			<td class="env-name">${environment.name}</td>
			<td>${environment.label}</td>
			<td>${environment.ips}</td>
			<td><#if environment.online>是<#else>否</#if></td>
			<td>${environment.seq}</td>
			<td>
			<@lion.Security resource="res_env_edit">
			<a data-toggle="modal" href="<@s.url action='envEditAjax' namespace='/system'/>?envId=${environment.id}"
				rel="tooltip" title="修改环境"><i class="icon-edit"></i></a>
			</@lion.Security>
			<@lion.Security resource="res_env_delete">
			<a class="deletelink" href="#" rel="tooltip" title="删除环境"><i class="icon-remove"></i></a></td>
			</@lion.Security>
		</tr>
		</#list>
	</tbody>
</table>
