<#include "/WEB-INF/pages/common/paginater.ftl">
<table class="table table-bordered table-striped table-condensed">
	<thead>
    <tr>
      <th width="40">序号</th>
      <th width="130">Name</th>
      <th width="200">域帐号</th>
      <th>系统用户</th>
      <th>Locked</th>
      <th>可查看线上配置</th>
      <th width="100">操作</th>
    </tr>
    </thead>
    <tbody>
	<@s.iterator value="paginater.results" var="user" status="userStatus">
	<tr class="user_row">
		<td>
			${userStatus.index + 1}
			<input type="hidden" value="${id}" name="user_id">
		</td>
		<td>${name}</td>
		<td>${loginName}</td>
		<td><@s.if test="%{#user.system}">是</@s.if><@s.else>否</@s.else></td>
		<td><@s.if test="%{#user.locked}">是</@s.if><@s.else>否</@s.else></td>
		<td><@s.if test="%{#user.onlineConfigView}">是</@s.if><@s.else>否</@s.else></td>
		<td>
			<a href="#" class="edit-user-btn"><i class="icon-edit" rel="tooltip" data-original-title="管理用户"></i></a>
		</td>
	</tr>
	</@s.iterator>
	</tbody>
</table>
<#include "/WEB-INF/pages/common/paginater.ftl">