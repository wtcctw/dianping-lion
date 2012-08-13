<table class="table table-bordered table-striped table-condensed">
	<thead>
    <tr>
      <th width="30">序号</th>
      <th width="290">KEY</th>
      <th width="230">DESC</th>
      <th>VALUE</th>
      <th width="65">Status</th>
      <th width="110">
      	操作 &nbsp;&nbsp;<input type="checkbox" id="display-all-btn" rel="tooltip" data-original-title="显示全部操作">
      	<a id="add-config-btn" href="#" rel="tooltip" data-original-title="添加配置项" class="pull-right">
      		<i class="icon-plus"></i>
      	</a>
      </th>
    </tr>
  </thead>
  <tbody>
  	<@s.iterator value="configVos" var="configVo" status="configStatus">
  	<tr class="config_row">
  		<td>
  			${configStatus.index}
  			<input type="hidden" value="${config.type}" name="config_type">
  			<input type="hidden" value="${config.key}" name="config_key">
  			<input type="hidden" value="${config.id}" name="config_id">
  		</td>
  		<td>
  			<@s.if test="%{#configVo.hasInstance}">${config.key}</@s.if>
  			<@s.else><font color="#D14836"><s>${config.key}</s></font></@s.else>
  		</td>
  		<td><span<@s.if test="%{#configVo.config.isLongDesc()}"> rel="tooltip" data-original-title="${config.desc?html}"</@s.if>>${config.abbrevDesc?html}</span></td>
  		<td>
  			<span style="margin-right: 10px;" <@s.if test="%{#configVo.defaultInstance != null && #configVo.defaultInstance.isLongValue()}">
  				rel="tooltip" data-original-title="${defaultInstance.moreValue?html}"</@s.if>>
  				<@s.property value="defaultInstance.abbrevValue"/>
  			</span>
  			<@s.if test="%{#configVo.hasInstance && #configVo.hasContextInst}">
  			(<i class="icon-indent-left" rel="tooltip" data-original-title="存在基于上下文的配置项值"></i>)
  			</@s.if>
  		</td>
  		<td>
  			<@s.if test="%{#configVo.hasInstance}">
  				已设置
  			</@s.if>
  			<@s.else><font color="#D14836">未设置</font></@s.else>
  		</td>
  		<td class="config-btn-group">
  			<a href="#" class="edit-config-btn"><i class="icon-edit" rel="tooltip" data-original-title="编辑配置值"></i></a>
  			<@s.if test="%{#configVo.defaultInstance != null}">
  			<a href="#" class="clear-config-btn"><i class="icon-trash" rel="tooltip" data-original-title="清除配置值"></i></a>
  			</@s.if>
			<@s.else>
			<i class="icon-trash icon-white"></i>
			</@s.else>
  			<a href="#" class="remove-config-btn"><i class="icon-remove" rel="tooltip" data-original-title="删除配置项"></i></a>
  			<a href="#" class="moveup-config-btn optional hide"><i class="icon-arrow-up" rel="tooltip" data-original-title="上移"></i></a>
  			<a href="#" class="movedown-config-btn optional hide"><i class="icon-arrow-down" rel="tooltip" data-original-title="下移"></i></a>
  		</td>
  	</tr>
  	</@s.iterator>
  </tbody>
</table>