<div id="edit-config-modal" class="modal hide fade">
	<div class="modal-header">
      <a class="close" data-dismiss="modal" >&times;</a>
      <h3>编辑配置项[<span class="config-key"></span>]</h3>
      <#include "/WEB-INF/pages/common/form-alert.ftl">
    </div>
    <div class="modal-body">
    	<input type="hidden" name="config-id">
    	<form class="form-horizontal">
    		<fieldset>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="edit-config-type-selector">
			      	<span rel="tooltip" data-original-title="仅用于Value输入值校验">类型:</span>
			      </label>
			      <div class="controls lion-controls">
			        <select id="edit-config-type-selector" disabled="disabled">
			        	<@s.iterator value="%{@com.dianping.lion.entity.ConfigTypeEnum@values()}">
			        		<option value="${value}">${label?html}</option>
			        	</@s.iterator>
			        </select>
			        <input type="checkbox" id="edit-trim-checkbox" checked="checked"><label for="edit-trim-checkbox" class="help-inline">trim while string</label>
			      </div>
			    </div>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label">环境:</label>
			      <div class="controls lion-controls">
			      		<input type="checkbox" id="edit-select-all-env"/><label for="edit-select-all-env" class="help-inline">全选</label>
			      		&nbsp;&nbsp;
			      	<@s.iterator value="environments" status="envStatus">
			      		<input type="checkbox" name="edit-config-env" id="edit-config-env-${envStatus.index}" value="${id}"
			      		<@s.if test="%{#envStatus.last}"> disabled="disabled"</@s.if>
			      		><label for="edit-config-env-${envStatus.index}" class="help-inline"
			      		<@s.if test="%{#envStatus.last}"> rel="tooltip" data-original-title="${label}环境需要单独创建"</@s.if>>${label}</label>
			      		&nbsp;&nbsp;
			      	</@s.iterator>
			      </div>
			    </div>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="edit-config-value">Value:</label>
			      <div class="controls lion-controls" id="edit-config-value-container">
			        <textarea rows="7" style="width:350px;" id="edit-config-value"></textarea>
			      </div>
			    </div>
		    </fieldset>
    	</form>
    </div>
    <div class="modal-footer">
      <a href="#" class="btn" data-dismiss="modal">关闭</a>
      <input id="edit-save-btn" type="button" class="btn btn-primary" value=" 保存 ">
      <input id="edit-save-deploy-btn" type="button" class="btn btn-primary" value=" 保存并部署 ">
    </div>
</div>