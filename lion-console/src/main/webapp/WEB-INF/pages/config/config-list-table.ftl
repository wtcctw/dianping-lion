<#assign queryStr="menu=" + menu + "&pid=" + pid + "&envId=" + envId />
<#assign criteriaStr="criteria.key=" + criteria.key + "&criteria.status=" + criteria.status />
<table class="table table-bordered table-striped table-condensed">
	<thead>
    <tr>
      <th width="40">序号</th>
      <th width="310">KEY</th>
      <th width="250">DESC</th>
      <th>VALUE</th>
      <th width="60">Status</th>
      <th width="120">
      	操作
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
  			<@s.if test="%{#configVo.hasInstance}">
  				<@s.if test="%{#configVo.status.status >= @com.dianping.lion.entity.ConfigStatusEnum@Deployed.value}">${config.key}</@s.if>
  				<@s.else><font color="#C67605">${config.key}</font></@s.else>
  			</@s.if>
  			<@s.else>
  			<font color="#D14836"><s>${config.key}</s></font>
  			</@s.else>
  		</td>
  		<td><span<@s.if test="%{#configVo.config.isLongDesc()}"> rel="tooltip" data-original-title="${config.desc?html}"</@s.if>>${config.abbrevDesc?html}</span></td>
  		<td>
  			<span style="margin-right: 20px;" <@s.if test="%{#configVo.defaultInstance.isLongValue()}">
  				rel="tooltip" data-original-title="${defaultInstance.moreValue?html}"</@s.if>>
  				<@s.property value="defaultInstance.abbrevValue"/>
  			</span>
  			<@s.if test="%{#configVo.hasInstance && #configVo.hasContextInst}">
  			( <i class="icon-indent-left" rel="tooltip" data-original-title="存在基于上下文的配置项值"></i> )
  			</@s.if>
  		</td>
  		<td>
  			<@s.if test="%{#configVo.hasInstance}">
  				<@s.if test="%{#configVo.status.status >= @com.dianping.lion.entity.ConfigStatusEnum@Deployed.value}">${status.statusEnum.label}</@s.if>
  				<@s.else><span style="color:#C67605;">${status.statusEnum.label}</span></@s.else>
  			</@s.if>
  			<@s.else><font color="#D14836">未设置</font></@s.else>
  		</td>
  		<td>
  			<a href="#" class="edit-config-btn"><i class="icon-edit"></i></a>
  			<@s.if test="%{#configVo.hasInstance}">
  			<a href="<@s.url action="clearInstance" namespace="/config"/>?${queryStr}&${criteriaStr}&configId=${config.id}" class="clearLink"><i class="icon-trash"></i></a>
  			</@s.if>
  			<@s.else>
  			<i rel="tooltip" class="icon-trash icon-white" data-original-title="不存在配置值"></i>
  			</@s.else>
  			<a href="<@s.url action="delete" namespace="/config"/>?${queryStr}&configId=${config.id}" class="deleteLink"><i class="icon-remove"></i></a>
  			<a href="<@s.url action="configMoveUp" namespace="/config"/>?${queryStr}&configId=${config.id}"><i class="icon-arrow-up"></i></a>
  			<a href="<@s.url action="configMoveDown" namespace="/config"/>?${queryStr}&configId=${config.id}"><i class="icon-arrow-down"></i></a>
  		</td>
  	</tr>
  	</@s.iterator>
  </tbody>
</table>