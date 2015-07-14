<div class="container">
    <div class="row">
        <div class="span12" style="padding:1px;">
            <strong>配置服务：</strong>
            <i class="icon-edit icon-intro" data-original-title="编辑服务" data-content="编辑服务"></i>
            <i class="icon-remove icon-intro" style="margin-left: 10px;" data-original-title="删除服务" data-content="删除服务"></i>
            <label class="pull-right" style="color:orange;"><b>${environment.label}</b></label>
        </div>
    </div>
    <div class="row">
        <div class="span12" id="service-list-container">
            <#include "/WEB-INF/pages/service/service-list-table.ftl">
        </div>
    </div>
</div>