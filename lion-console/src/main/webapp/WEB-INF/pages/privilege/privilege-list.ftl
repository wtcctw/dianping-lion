<head>
	<title>业务配置</title>
	<script type="text/javascript" src="<@s.url value="/js/biz/privilege/privilege-list.js"/>"></script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<ul class="noborder smallpadding smallbottommargin breadcrumb">
					<li>
					    系统设置 <span class="divider">></span>
					</li>
					<li>权限管理</li>
				</ul>
			</div>
		</div>
		<div class="row">
			<div id="role-list-container" class="span5">
				<#include "/WEB-INF/pages/privilege/role-list-table.ftl">
			</div>
			<div id="role-related-container" class="span7" style="min-height:500px;">
			</div>
		</div>
	</div>
	<br/>
	
	<div id="add-role-modal" class="modal hide fade modal-small">
		<div class="modal-header">
	      <a class="close" data-dismiss="modal" >&times;</a>
	      <h3>创建角色</h3>
	      <#include "/WEB-INF/pages/common/form-alert.ftl">
	    </div>
	    <div class="modal-body">
	    	<form class="form-horizontal" onSubmit="return false;">
	    		<fieldset>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="config-key">角色名:</label>
				      <div class="controls lion-controls">
				        <input type="text" id="add-role-name" maxlength="60" style="width:230px;">
				        <span class="help-inline hide message">必填!</span>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="config-key">备注:</label>
				      <div class="controls lion-controls">
				      	<textarea rows="4" style="width:230px;" id="add-role-remark"></textarea>
				      </div>
				    </div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
	      <a href="#" class="btn" data-dismiss="modal">关闭</a>
	      <input id="add-role-modal-btn" type="button" class="btn btn-primary" value=" 保存 ">
	    </div>
	</div>
	
	<div id="edit-role-modal" class="modal hide fade modal-small">
		<div class="modal-header">
	      <a class="close" data-dismiss="modal" >&times;</a>
	      <h3>编辑角色</h3>
	      <#include "/WEB-INF/pages/common/form-alert.ftl">
	    </div>
	    <div class="modal-body">
	    	<input type="hidden" id="edit-role-id">
	    	<form class="form-horizontal" onSubmit="return false;">
	    		<fieldset>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="config-key">角色名:</label>
				      <div class="controls lion-controls">
				        <input type="text" id="edit-role-name" maxlength="60" style="width:230px;">
				        <span class="help-inline hide message">必填!</span>
				      </div>
				    </div>
				    <div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="config-key">备注:</label>
				      <div class="controls lion-controls">
				      	<textarea rows="4" style="width:230px;" id="edit-role-remark"></textarea>
				      </div>
				    </div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
	      <a href="#" class="btn" data-dismiss="modal">关闭</a>
	      <input id="edit-role-modal-btn" type="button" class="btn btn-primary" value=" 保存 ">
	    </div>
	</div>
</body>