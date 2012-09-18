<legend style="font-size:14px; margin-bottom:5px;">
	<strong>权限设置 [角色: <span id="p_role_name" style="color:#51A351;"></span>]</strong>
	<input type="hidden" id="priv-role-id" value="${roleId}"/>
	<a href="#" class="save-privilege-btn no-dec pull-right" style="margin-right:10px;font-size:13px;">保存</a>
</legend>
<@s.iterator value="categories" var="category">
<table class="table table-condensed lion" style="margin:0 0 0;border-bottom:1px solid #DDD;">
	<thead>
	    <tr>
			<th colspan="4">
				<input type="checkbox" id="pc-${id}" class="category-check"
				/><label for="pc-${id}" class="help-inline" style="margin-bottom:1px;"><strong style="color:#08C;">${name}</strong></label>
			</th>
	    </tr>
	</thead>
	<@s.if test="%{!privileges.isEmpty()}">
		<tbody>
			<@s.set name="index" value="0"/>
			<@s.iterator value="privileges" var="privStatus">
			<@s.if test="%{#index % 5 == 0}"><tr style="height:22px;"></@s.if>
				<td width="20%" style="border-top:none;">
					<input type="checkbox" id="pc-${category.id}-p-${id}" name="privilege" value="${id}" 
					<@s.if test="%{privilegeIds.contains(id)}">checked="checked"</@s.if>/><label for="pc-${category.id}-p-${id}" style="margin-bottom:0px;">${name}</label>
				</td>
			<@s.if test="%{(#index + 1) % 5 == 0}"></tr></@s.if>
			<@s.set name="index" value="%{#index + 1}"/>
			</@s.iterator>
			<@s.if test="%{#index % 5 != 0}">
				<@s.iterator begin="4" end="%{#index % 5}" step="-1">
					<td width="20%" style="border-top:none;">&nbsp;</td>
				</@s.iterator>
				</tr>
			</@s.if>
		</tbody>
	</@s.if>
</table>
</@s.iterator>