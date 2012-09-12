<div id="add-config-modal" class="modal hide fade">
	<div class="modal-header">
      <a class="close" data-dismiss="modal" >&times;</a>
      <h3>创建配置项</h3>
      <#include "/WEB-INF/pages/common/form-alert.ftl">
    </div>
    <div class="modal-body">
    	<form class="form-horizontal" onSubmit="return false;">
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
			        ( 仅作输入校验 )&nbsp;
			        <input type="checkbox" id="trim-checkbox" checked="checked"><label for="trim-checkbox" class="help-inline">trim</label>
			      </div>
			    </div>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label">环境:</label>
			      <div class="controls lion-controls2">
			      		<input type="checkbox" id="select-all-env"/><label for="select-all-env" class="help-inline">全选线下</label>
			      		&nbsp;&nbsp;
			      	<@s.iterator value="environments" status="envStatus">
			      		<@s.set name="hasAddPrivilege" value="%{hasAddPrivilege(pid, id)}"/>
			      		<input type="checkbox" name="config-env" online="${online?string("true","false")}" id="config-env-${envStatus.index}" value="${id}"
			      		<@s.if test="%{#envStatus.index == 0 && #hasAddPrivilege}"> checked="checked"</@s.if>
			      		<@s.if test="%{#envStatus.first || !#hasAddPrivilege}"> disabled="disabled"</@s.if>
			      		><label for="config-env-${envStatus.index}" class="help-inline">${label}</label>
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
			    <!--
			    <div class="control-group control-lion-group">
			    	<label class="control-label control-lion-label">&nbsp;</label>
			    	<div class="controls lion-controls">
			    		<select id="operation-type" style="width:175px;">
			    			<option value="save">仅保存</option>
			    			<option value="forpub">保存并下次发布时生效</option>
			    			<option value="deploy" selected="selected">保存并推送到配置服务器</option>
			    		</select>
			    		<span id="if-push-container">
			    			<input type="checkbox" id="if-push"><label class="help-inline" for="if-push">并推送到应用(应用无需重启即生效)</label>
			    		</span>
			    	</div>
			    </div>
			    -->
		    </fieldset>
    	</form>
    </div>
    <div class="modal-footer">
      <a href="#" class="btn" data-dismiss="modal">关闭</a>
      <input id="save-btn" type="button" class="btn btn-primary" value=" 保存 ">
    </div>
</div>