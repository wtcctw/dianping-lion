<head>
	<title>用户管理</title>
	<script type="text/javascript" src="<@s.url value="/js/biz/system/user-list.js"/>"></script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<ul class="noborder smallpadding smallbottommargin breadcrumb">
					<li>
					    系统设置 <span class="divider">></span>
					</li>
					<li>用户管理</li>
				</ul>
				<br/>
				<@s.form id="searchForm" cssClass="form-inline lion" action="/system/userList.vhtml" method="get">
					<input type="hidden" id="search-pageNo" name="paginater.pageNumber" value="1"/>
					<label class="control-label" for="name">Name：</label>
					<@s.textfield id="search-name" name="userCriteria.name" cssClass="input-medium" maxlength="50"/>
					<label class="control-label" for="loginName">域帐号：</label>
					<@s.textfield id="search-loginName" name="userCriteria.loginName" cssClass="input-medium" maxlength="50"/>
					<label class="control-label" for="system">系统用户：</label>
					<@s.select id="search-system" name="userCriteria.system" list=r"#{'':'任意', '1':'是', '0':'否'}" listKey="key"
						listValue="value" cssClass="input-small"/>
					<label class="control-label" for="locked">Locked：</label>
					<@s.select id="search-locked" name="userCriteria.locked" list=r"#{'':'任意', '1':'是', '0':'否'}" listKey="key"
						listValue="value" cssClass="input-small"/>
					<label class="control-label" for="onlineConfigView">可看线上配置：</label>
					<@s.select id="search-onlineConfigView" name="userCriteria.onlineConfigView" list=r"#{'':'任意', '1':'是', '0':'否'}" listKey="key"
						listValue="value" cssClass="input-small"/>
					<button type="submit" class="btn">搜索</button>
				</@s.form>
			</div>
		</div>
		<div class="row">
			<div class="span12" id="user-list-container">
				<#include "/WEB-INF/pages/system/user-list-table.ftl">
			</div>
		</div>
	</div>
	<div id="edit-user-modal" class="modal hide fade">
		<div class="modal-header">
	      <a class="close" data-dismiss="modal" >&times;</a>
	      <h3>管理用户</h3>
	      <#include "/WEB-INF/pages/common/form-alert.ftl">
	    </div>
	    <div class="modal-body">
	    	<input type="hidden" name="user-id">
	    	<form class="form-horizontal" onSubmit="return false;">
	    		<fieldset>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="user-locked">Locked:</label>
				      <div class="controls lion-controls">
				        <input type="radio" name="edit-user-locked" id="user-locked-1" value="true"
				        	><label for="user-locked-1" class="help-inline">是</label>
				      	<input type="radio" name="edit-user-locked" id="user-locked-0" value="false"
				      		><label for="user-locked-0" class="help-inline">否</label>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="user-onlineconfigview">可查看线上配置:</label>
				      <div class="controls lion-controls">
				        <input type="radio" name="edit-user-configview" id="user-configview-1" value="true"
				        	><label for="user-configview-1" class="help-inline">是</label>
				      	<input type="radio" name="edit-user-configview" id="user-configview-0" value="false"
				      		><label for="user-configview-0" class="help-inline">否</label>
				      </div>
				    </div>
				</fieldset>
	    	</form>
	    </div>
	    <div class="modal-footer">
	      <a href="#" class="btn" data-dismiss="modal">关闭</a>
	      <input id="user-edit-btn" type="button" class="btn btn-primary" value=" 保存 ">
	    </div>
	</div>
</body>