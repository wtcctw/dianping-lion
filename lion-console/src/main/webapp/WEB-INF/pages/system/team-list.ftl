<table id="table-team-list" class="table table-bordered table-striped table-condensed">
	  <thead>
	    <tr>
	      <th style="width: 30px">序号</th>
	      <th style="width: 80px">部门</th>
	      <th>创建时间</th>
	      <th>更新时间</th>
	      <th style="width: 100px">操作<a data-toggle="modal" href="<@s.url action='teamAddAjax' namespace='/system'/>"
				rel="tooltip" title="添加部门"> <i class="icon-plus pull-right" />&nbsp&nbsp</a></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list teamList as team>
	  		<tr>
	      		<td>${team_index + 1}</td>
	      		<td>${team.name}</td>
	      		<td>${team.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	      		<td>${team.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>
				<td style="text-align:center;">
			<a data-toggle="modal" href="<@s.url action='teamEditAjax' namespace='/system'/>?id=${team.id}"
				rel="tooltip" title="修改部门"> <i class="icon-edit"></i> </a>
			&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
			<a class="deletelink" href="<@s.url action='teamDelete' namespace='/system'/>?id=${team.id}"
				rel="tooltip" title="删除部门"> <i class="icon-remove"></i> </a>
					</a>
			     </td>
	      	</tr>
	  	</#list>
	  </tbody>
</table>


