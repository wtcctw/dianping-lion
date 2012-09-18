<table class="table table-bordered table-striped table-condensed">
	<thead>
    <tr>
      <th width="30">序号</th>
      <th>角色</th>
      <th width="60">创建者</th>
      <th width="30">内建</th>
      <th width="130">创建时间</th>
      <th width="70">
      	操作
      	<a id="add-role-btn" href="#" rel="tooltip" data-original-title="添加角色" class="pull-right">
			<i class="icon-plus"></i>
		</a>
      </th>
    </tr>
    </thead>
    <tbody>
	<@s.iterator value="roles" status="roleStatus">
	<tr class="role_row">
		<td>
			${roleStatus.index + 1}
			<input type="hidden" value="${id}" name="role_id">
			<input type="hidden" value="${remark}" name="role_remark">
		</td>
		<td class="role_name"<@s.if test="%{remark != null && remark != ''}"> data-original-title="备注" data-content="${remark}"</@s.if>>${name}</td>
		<td>${createUserName}</td>
		<td><@s.if test="%{internal}">是</@s.if><@s.else>否</@s.else></td>
		<td>${createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
		<td>
			<@s.if test="%{!internal}">
			<a href="#" class="edit-role-btn"><i class="icon-edit" rel="tooltip" data-original-title="编辑角色"></i></a>
			</@s.if>
			<@s.else><i class="icon-edit icon-white"></i></@s.else>
			<a href="#" class="view-privilege-btn"><i class="icon-list-alt" rel="tooltip" data-original-title="编辑权限"></i></a>
			<a href="#" class="edit-user-btn"><i class="icon-user" rel="tooltip" data-original-title="添加用户"></i></a>
			<@s.if test="%{!internal}">
			<a href="#" class="remove-role-btn"><i class="icon-remove" rel="tooltip" data-original-title="删除角色"></i></a>
			</@s.if>
			<@s.else><i class="icon-remove icon-white"></i></@s.else>
		</td>
	</tr>
	</@s.iterator>
	</tbody>
</table>