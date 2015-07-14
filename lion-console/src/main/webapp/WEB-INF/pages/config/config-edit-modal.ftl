<div id="edit-config-modal" class="modal hide fade">
	<div class="modal-header">
      <a class="close" data-dismiss="modal" >&times;</a>
      <h3>编辑配置项[<span class="config-key"></span>]</h3>
      <#include "/WEB-INF/pages/common/form-alert.ftl">
    </div>
    <div class="modal-body">
    	<input type="hidden" name="config-id">
    	<form class="form-horizontal" onSubmit="return false;">
    		<fieldset>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="edit-config-type-selector">
			      	<span rel="tooltip" data-original-title="仅用于Value输入值校验">类型:</span>
			      </label>
			      <div class="controls lion-controls">
			        <select id="edit-config-type-selector">
			        	<@s.iterator value="%{@com.dianping.lion.entity.ConfigTypeEnum@values()}">
			        		<option value="${value}">${label?html}</option>
			        	</@s.iterator>
			        </select>
			        ( 仅作输入校验 )&nbsp;
			        <input type="checkbox" id="edit-trim-checkbox" checked="checked"><label for="edit-trim-checkbox" class="help-inline">trim</label>
			      </div>
			    </div>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label">环境:</label>
			      <div class="controls lion-controls2" style="width:390px;">
			      		<input type="checkbox" id="edit-select-all-env"/><label for="edit-select-all-env" class="help-inline">全选线下</label>
			      		&nbsp;&nbsp;
                   <input type="checkbox" id="edit-select-all-online-env"/><label for="edit-select-all-online-env" class="help-inline">全选线上</label>
                   &nbsp;&nbsp;
			      	<@s.iterator value="environments" status="envStatus">
			      		<input type="checkbox" name="edit-config-env" online="${online?string("true","false")}" id="edit-config-env-${id}" value="${id}"
			      		><label for="edit-config-env-${id}" class="help-inline">${label}</label>
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
			    <!--
			    <div class="control-group control-lion-group">
			    	<label class="control-label control-lion-label">&nbsp;</label>
			    	<div class="controls lion-controls">
			    		<select id="edit-operation-type" style="width:175px;">
			    			<option value="save">仅保存</option>
			    			<option value="forpub">保存并下次发布时生效</option>
			    			<option value="deploy" selected="selected">保存并推送到配置服务器</option>
			    		</select>
			    		<span id="edit-if-push-container">
			    			<input type="checkbox" id="edit-if-push"><label class="help-inline" for="edit-if-push">并推送到应用(应用无需重启即生效)</label>
			    		</span>
			    	</div>
			    </div>
			    -->
		    </fieldset>
    	</form>
    </div>
    <div class="modal-footer">
      <a href="#" class="btn" data-dismiss="modal">关闭</a>
      <input id="edit-save-btn" type="button" class="btn btn-primary" value=" 保存 ">
      <input id="edit-more-btn" type="button" class="btn btn-info" value=" 编辑泳道配置 ">
    </div>
</div>