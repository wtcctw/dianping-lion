<@s.set name="hasEditAttrPrivilege" value="%{hasEditAttrPrivilege(pid)}"/>
<@s.set name="hasLockPrivilege" value="%{hasLockPrivilege()}"/>
<div id="edit-config-attr-modal" class="modal hide fade">
	<div class="modal-header">
      <a class="close" data-dismiss="modal">&times;</a>
      <h3>编辑配置属性</h3>
      <#include "/WEB-INF/pages/common/form-alert.ftl">
    </div>
    <div class="modal-body">
    <input type="hidden" name="config-id">
    <input type="hidden" name="success">
    	<form class="form-horizontal" onSubmit="return false;">
    		<fieldset>
        		<div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="config-key">DESC:</label>
			      <div class="controls lion-controls">
			      	<input type="text" style="width:350px;" id="attr-config-desc" maxlength="60"<@s.if test="!#hasEditAttrPrivilege"> readonly="readonly"</@s.if>>
			        <span class="help-inline hide message">必填!</span>
			      </div>
			    </div>
        		<div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="config-key">线上是否公开:</label>
			      <div class="controls lion-controls">
			      	<input type="radio"<@s.if test="!#hasLockPrivilege"> disabled="disabled"</@s.if> name="config-public" 
			      		id="config-public-1" value="true"><label for="config-public-1" class="help-inline">是</label>
			      	<input type="radio"<@s.if test="!#hasLockPrivilege"> disabled="disabled"</@s.if> name="config-public" 
			      		id="config-public-2" value="false"><label for="config-public-2" class="help-inline">否</label>
			      </div>
			    </div>
		</fieldset>
    	</form>
    </div>
    <div class="modal-footer">
      <a href="#" class="btn" data-dismiss="modal">关闭</a>
      <input id="attr-save-btn" type="button" class="btn btn-primary" value=" 保存 ">
    </div>
</div>