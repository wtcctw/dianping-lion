<legend style="font-size:14px; margin-bottom:5px;">
	<strong>管理用户 [角色: <span id="u_role_name" style="color:#51A351;">${role.name}</span>]</strong>
	<input type="hidden" id="u-role-id" value="${userCriteria.roleId}"/>&nbsp;&nbsp;
	<input type="text" id="user-search" style="margin-bottom:1px;width:170px;">
	<input type="hidden" id="user-search-id">
	<a href="#" id="add-role-user" class="no-dec" style="font-size:13px;">添加</a>
	&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="text" id="user-search-name" value="${userCriteria.userName}" style="margin-bottom:1px;">
	<input type="hidden" id="user-paginater-num" value="${paginater.pageNumber}">
	<a href="#" id="search-role-user" class="no-dec" style="font-size:13px;">搜索</a>
</legend>
<table class="table table-bordered table-striped table-condensed" style="margin-bottom:4px;">
	<thead>
		<tr>
			<th width="200">域账号</th>
			<th>姓名</th>
			<th width="50">&nbsp;</th>
		</tr>
	</thead>
	<tbody>
	<@s.iterator value="paginater.results">
	  	<tr class="role-user-row">
	  		<td>
	  			${loginName}
	  			<input type="hidden" name="role-user-id" value="${id}"/>
	  		</td>
			<td>${name}</td>
			<td><a href="#" class="del-role-user no-dec">删除</a></td>
	  	</tr>
	</@s.iterator>
	</tbody>
</table>  
<#include "/WEB-INF/pages/common/paginater.ftl">