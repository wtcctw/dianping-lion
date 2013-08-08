<table style="table-layout: fixed; width: 100%" class="table table-bordered table-striped table-condensed">
    <thead>
    <tr>
      <th width="30">ID</th>
      <th width="30%">Name</th>
      <th width="15%">Description</th>
      <th width="60">Group</th>
      <th width="30%">Hosts</th>
      <th width="60">
        操作 &nbsp;&nbsp;
        <@s.if test="%{hasAddPrivilege(pid, envId)}">
        <a id="create-service-btn" href="#" rel="tooltip" data-original-title="添加服务" class="pull-right">
            <i class="icon-plus"></i>
        </a>
        </@s.if>
      </th>
    </tr>
  </thead>
  <tbody>
    <@s.iterator value="services" var="service" status="status">
    <tr class="config_row">
        <td>
            ${status.index + 1}
            <input type="hidden" value="${service.id}" name="service-id">
        </td>
        <td class="service-name">
            ${service.name}
        </td>
        <td class="service-desc">
            ${service.desc}
        </td>
        <td class="service-group">
            ${service.group}
        </td>
        <td style="word-wrap: break-word;" class="service-hosts">
            ${service.hosts}
        </td>
        <td class="config-btn-group">
            <@s.if test="%{hasAddPrivilege(pid, envId)}">
                <a href="#" class="update-service-btn"><i class="icon-edit" rel="tooltip" data-original-title="编辑服务"></i></a>
                <a href="#" class="delete-service-btn"><i class="icon-remove" rel="tooltip" data-original-title="删除服务"></i></a>
            </@s.if>
        </td>
    </tr>
    </@s.iterator>
  </tbody>
</table>
