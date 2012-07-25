<div id="add-config-modal" class="modal hide fade">
	<div class="modal-header">
      <a class="close" data-dismiss="modal" >&times;</a>
      <h3>创建配置项</h3>
      <#include "/WEB-INF/pages/common/form-alert.ftl">
    </div>
    <div class="modal-body">
    	<form class="form-horizontal">
    		<fieldset>
        		<div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="config-key">Key:</label>
			      <div class="controls lion-controls">
			        <input type="text" style="width:350px;" id="config-key" value="${project.name}." maxlength="60">
			        <span class="help-inline hide message">必填!</span>
			      </div>
			    </div>
        		<div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="config-desc">描述:</label>
			      <div class="controls lion-controls">
			        <input type="text" style="width:350px;" id="config-desc" maxlength="60">
			        <span class="help-inline hide message">必填!</span>
			      </div>
			    </div>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="config-type-selector">
			      	<span rel="tooltip" data-original-title="仅用于Value输入值校验">类型:</span>
			      </label>
			      <div class="controls lion-controls">
			        <select id="config-type-selector">
			        	<@s.iterator value="%{@com.dianping.lion.entity.ConfigTypeEnum@values()}">
			        		<option value="${value}">${label?html}</option>
			        	</@s.iterator>
			        </select>
			        <input type="checkbox" id="trim-checkbox" checked="checked"><label for="trim-checkbox" class="help-inline">trim while string</label>
			      </div>
			    </div>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label">环境:</label>
			      <div class="controls lion-controls">
			      		<input type="checkbox" id="select-all-env"/><label for="select-all-env" class="help-inline">全选</label>
			      		&nbsp;&nbsp;
			      	<@s.iterator value="environments" status="envStatus">
			      		<input type="checkbox" name="config-env" id="config-env-${envStatus.index}" value="${id}"
			      		<@s.if test="%{#envStatus.index == 0}"> checked="checked"</@s.if>
			      		<@s.if test="%{#envStatus.first}"> disabled="disabled"</@s.if>
			      		><label for="config-env-${envStatus.index}" class="help-inline"
			      		<@s.if test="%{#envStatus.last}"> rel="tooltip" data-original-title="${label}环境需要单独创建"</@s.if>>${label}</label>
			      		&nbsp;&nbsp;
			      	</@s.iterator>
			      </div>
			    </div>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="config-value">Value:</label>
			      <div class="controls lion-controls" id="config-value-container">
			        <textarea rows="7" style="width:350px;" id="config-value"></textarea>
			      </div>
			    </div>
			    <div class="control-group control-lion-group">
			    	<label class="control-label control-lion-label">&nbsp;</label>
			    	<div class="controls lion-controls">
			    		<input type="checkbox" value="true" id="if-deploy"><label class="help-inline" for="if-deploy">推送到配置服务器</label>&nbsp;&nbsp;
			    		<input type="checkbox" value="true" id="if-push" disabled="disabled"><label class="help-inline" for="if-push">并推送到应用(应用无需重启即生效)</label>
			    	</div>
			    </div>
		    </fieldset>
    	</form>
    </div>
    <div class="modal-footer">
      <a href="#" class="btn" data-dismiss="modal">关闭</a>
      <input id="save-btn" type="button" class="btn btn-primary" value=" 保存 ">
    </div>
</div>