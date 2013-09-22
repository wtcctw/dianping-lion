<ul class="nav nav-tabs">
    <@s.iterator value="environments" var="env">
    <li<@s.if test="%{#env.id == envId}"> class="active"</@s.if>>
        <a href="#tab-content-${label}" data-toggle="tab">${label}</a>
    </li>
    </@s.iterator>
</ul>
<div class="tab-content" style="min-height: 500px;">
    <@s.iterator value="environments" var="env">
        <div class="tab-pane<@s.if test="%{#env.id == envId}"> active</@s.if>" id="tab-content-${label}">
            <input type="hidden" id="env-id" value="${env.id}"/>
            <input type="hidden" id="env-label" value="${env.label}"/>
            <table class="table table-bordered table-condensed">
                <thead>
                    <tr>
                        <th width="50">序号</th>
                        <th width="200">泳道</th>
                        <th width="200">描述</th>
                        <th>VALUE</th>
                        <th width="50">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <@s.iterator value="instanceMap[#env.id]" var="inst" status="instStatus">
                    <@s.if test="context != ''">
                    <tr class="context-row <@s.if test="#instStatus.odd == true">clion</@s.if><@s.else>xlion</@s.else>">
                        <td>${instStatus.index}</td>
                        <td id="context">
                            ${context}
                        </td>
                        <td>${desc}</td>
                        <td id="value">
                            <span<@s.if test="%{#inst.isLongValue()}"> rel="tooltip" data-original-title="${moreValue?html}"</@s.if>>
                                <@s.property value="%{#inst.getAbbrevValue(80)}"/>
                            </span>
                        </td>
                        <td style="vertical-align: middle;">
                            <a href="#" class="edit-context-btn"><i class="icon-edit" rel="tooltip" data-original-title="编辑配置值"></i></a>
                            <a href="#" class="remove-context-btn"><i class="icon-remove" rel="tooltip" data-original-title="删除配置项"></i></a>
                        </td>
                    </tr>
                    </@s.if>
                    <@s.else>
                    <tr class="xlion">
                        <td>${instStatus.index}</td>
                        <td style="color:grey">[默认]</td>
                        <td>${desc}</td>
                        <td>
                            <span<@s.if test="%{#inst.isLongValue()}"> rel="tooltip"
                                data-original-title="${moreValue?html}"</@s.if>>
                                <@s.property value="%{#inst.getAbbrevValue(80)}"/>
                            </span>
                        </td>
                        <td></td>
                    </tr>
                    </@s.else>
                    </@s.iterator>
                  </tbody>
            </table>
        </div>
    </@s.iterator>
</div>
