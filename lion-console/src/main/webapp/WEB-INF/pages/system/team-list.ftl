<@s.set name="hasEditPrivilege" value="%{hasEditPrivilege()}"/>
<table id="table-team-list" class="table table-bordered table-striped table-condensed">
	  <thead>
	    <tr>
	      <th width="65">序号</th>
	      <th width="120">部门</th>
	      <th>创建时间</th>
	      <th>更新时间</th>
	      <th width="100">操作
	      	<@s.if test="%{#hasEditPrivilege}">
	      	<a data-toggle="modal" href="<@s.url action='teamAddAjax' namespace='/system'/>">
	      		<i class="icon-plus pull-right" rel="tooltip" title="添加团队"></i>
	      	</a>
	      	</@s.if>
	      </th>
	    </tr>
	  </thead>
	  <tbody>
	  	<@s.iterator value="teamList" var="team" status="teamStatus">
	  		<tr>
	      		<td>${teamStatus.index}</td>
	      		<td>${name}</td>
	      		<td>${createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	      		<td>${modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>
				<td style="text-align:center;">
					<@s.if test="%{#hasEditPrivilege}">
					<a data-toggle="modal" href="<@s.url action='teamEditAjax' namespace='/system'/>?id=${id}"
						rel="tooltip" title="修改部门"><i class="icon-edit"></i></a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="deletelink" href="<@s.url action='teamDeleteAjax' namespace='/system'/>?id=${id}"
						rel="tooltip" title="删除部门"><i class="icon-remove"></i></a>
					</@s.if>
			     </td>
	      	</tr>
	  	</@s.iterator>
	  </tbody>
</table>


