
<head>
	<title>项目设置</title>
	<script type="text/javascript" src="<@s.url value="/js/biz/system/project-list.js"/>"></script>
</head>
<body>
<input type="hidden" id="teamSelected" value="${teamSelect}" />
<input type="hidden" id="productSelected" value="${productSelect}" />
<div class="container">
		
<#include "/WEB-INF/pages/system/project-span.ftl"> 

<form id="projectQuery" class="form-inline lion" action="<@s.url action='projectList' namespace='/system'/>" method="post">

	
	            <label class="control-label" for="teamSelect">业务组</label>
	              <select id="teamSelect">
	                <option value=0>所有的</option>
	                <#list teamList as team>
	                	<option value=${team.id}>${team.name}</option>
					</#list>
	              </select>
	   


	            <label class="control-label" for="productSelect">产品线</label>
	              <select id="productSelect">
	                <option team=0 value=0>所有的</option>
	                <#list teamList as team>
	                	<#list team.products as product>
	                	<option team=${team.id} value=${product.id}>${product.name}</option>
	                	</#list>
					</#list>
	              </select>

	<button id="query_btn" type="submit" class="btn">查询</button>

</form>
<div class="row">
<div class="span12">
<table class="table table-bordered table-striped table-condensed">
	  <thead>
	    <tr>
	      <th width="65">序号</th>
	      <th width="120">业务组</th>
	      <th width="120">所属产品线</th>
	      <th>项目名</th>
	      <th width="230">创建时间</th>
	      <th width="230">更新时间</th>
	      <th width="100">操作<a href="#" id="add_project_btn" rel="tooltip" title="添加项目"><i class="icon-plus pull-right"/>&nbsp&nbsp</a></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list projectList as project>
	  		<tr class="project_row">
	  			<input name="projectId" type="hidden" value="${project.id}" />
	      		<td>${project_index + 1}</td>
	      		<td>${project.teamName}</td>
	      		<td>${project.productName}</td>
	      		<td>${project.name}</td>
	      		<td>${project.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	      		<td>${project.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>
				<td style="text-align:center;">
					<a href="#" id="edit_project_btn" productId="${project.productId}" 
					projectName="${project.name}" rel="tooltip" title="修改项目" 
					onclick="editOpen(${project.id},'${project.name}',${project.productId},'<#list userList as user><#if user.id==project.managerId>${user.name}@${user.loginName}@${user.id}</#if></#list>','<#list userList as user><#if user.id==project.techLeaderId>${user.name}@${user.loginName}@${user.id}</#if></#list>','<#list userList as user><#if user.id==project.operId>${user.name}@${user.loginName}@${user.id}</#if></#list>');return false;"
					><i class="icon-edit"></i></a>&nbsp;&nbsp;&nbsp;
					<a href="#"class="edit-member-btn"><i rel="tooltip" title="成员管理" class="icon-user"></i></a>&nbsp;&nbsp;&nbsp;
	      		    <a class="deletelink" href="<@s.url action='projectDel' namespace='/system'/>?projectId=${project.id}" rel="tooltip" title="删除项目"
	      		    ><i class="icon-remove"></i></a>&nbsp;&nbsp;&nbsp;
			     </td>
	      	</tr>
	  	</#list>
	  </tbody>
</table>
</div>
</div>
</div>

<div id="add-project-modal" class="modal hide fade">
		<div class="modal-header">
          <a class="close" data-dismiss="modal" >&times;</a>
          <h3>创建项目</h3>
        </div>
        <div class="modal-body">
        	<form class="form-horizontal">
        		<fieldset>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="productSelectAdd">产品线:</label>
				      <div class="controls lion-controls">
				        <select id="productSelectAdd">
					       <#list teamList as team>
					       	<#list team.products as product>
					       	<option value=${product.id}>${product.name}</option>
					       	</#list>
							</#list>
					     </select>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="projectName">项目名:</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-middle" id="projectName">
				      </div>
				    </div>
				    <div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for=" id="manager">PM:</label>
				      <div class="controls lion-controls">
				        <input id="manager" type="text" class="input-middle" style="margin: 0 auto;" data-provide="typeahead" data-items="4" 
						     data-source="[<#list userList as user><#if user_index != 0>,</#if>&quot;${user.name}@${user.loginName}@${user.id}&quot;</#list>]">
				        	<span id="managerWarn" class="help-inline">
							可输入名字或域账号提示
	      				 	</span> 
				        </div>
				    </div>
				    <div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for=" id="techLeader">TechLeader:</label>
				      <div class="controls lion-controls">
				        <input id="techLeader" type="text" class="input-middle" style="margin: 0 auto;" data-provide="typeahead" data-items="4" 
						     data-source="[<#list userList as user><#if user_index != 0>,</#if>&quot;${user.name}@${user.loginName}@${user.id}&quot;</#list>]">
				        	<span id="techLeaderWarn" class="help-inline">
							可输入名字或域账号提示
	      				 	</span> 
				        </div>
				    </div>
				    <div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="oper">业务运维:</label>
				      <div class="controls lion-controls">
				        <input id="oper" type="text" class="input-middle" style="margin: 0 auto;" data-provide="typeahead" data-items="4" 
						     data-source="[<#list userList as user><#if user_index != 0>,</#if>&quot;${user.name}@${user.loginName}@${user.id}&quot;</#list>]">
						 <span id="operWarn" class="help-inline">
							可输入名字或域账号提示
	      				 </span> 
				      </div>
				    </div>
			    </fieldset>
        	</form>
        </div>
        <div class="modal-footer">
          <a href="#" class="btn" data-dismiss="modal" >关闭</a>
          <a href="#" id="addProject" class="btn btn-primary">保存</a>
        </div>
	</div>
<div id="edit-project-modal" class="modal hide fade">
		<div class="modal-header">
          <a class="close" data-dismiss="modal" >&times;</a>
          <h3>创建项目</h3>
        </div>
        <div class="modal-body">
        	<form class="form-horizontal">
        		<fieldset>
        		<input type="hidden" id="projectIdEdit" value="" />
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="productSelectAdd">产品线:</label>
				      <div class="controls lion-controls">
				        <select id="productSelectEdit">
					       <#list teamList as team>
					       	<#list team.products as product>
					       	<option value=${product.id}>${product.name}</option>
					       	</#list>
							</#list>
					     </select>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="projectName">项目名:</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-middle" id="projectNameEdit">
				      </div>
				    </div>
				    <div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for=" id="managerEdit">PM:</label>
				      <div class="controls lion-controls">
				        <input id="managerEdit" type="text" class="input-middle" style="margin: 0 auto;" data-provide="typeahead" data-items="4" 
						     data-source="[<#list userList as user><#if user_index != 0>,</#if>&quot;${user.name}@${user.loginName}@${user.id}&quot;</#list>]">
				        	<span id="managerWarnEdit" class="help-inline">
							可输入名字或域账号提示
	      				 	</span> 
				        </div>
				    </div>
				    <div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for=" id="techLeaderEdit">TechLeader:</label>
				      <div class="controls lion-controls">
				        <input id="techLeaderEdit" type="text" class="input-middle" style="margin: 0 auto;" data-provide="typeahead" data-items="4" 
						     data-source="[<#list userList as user><#if user_index != 0>,</#if>&quot;${user.name}@${user.loginName}@${user.id}&quot;</#list>]">
				        	<span id="techLeaderWarnEdit" class="help-inline">
							可输入名字或域账号提示
	      				 	</span> 
				        </div>
				    </div>
				    <div class="control-group control-lion-group">
				      <label class="control-label control-lion-label" for="operEdit">业务运维:</label>
				      <div class="controls lion-controls">
				        <input id="operEdit" type="text" class="input-middle" style="margin: 0 auto;" data-provide="typeahead" data-items="4" 
						     data-source="[<#list userList as user><#if user_index != 0>,</#if>&quot;${user.name}@${user.loginName}@${user.id}&quot;</#list>]">
						 <span id="operWarnEdit" class="help-inline">
							可输入名字或域账号提示
	      				 </span> 
				      </div>
				    </div>
			    </fieldset>
        	</form>
        </div>
        <div class="modal-footer">
          <a href="#" class="btn" data-dismiss="modal" >关闭</a>
          <a href="#" id="editProject" class="btn btn-primary">保存</a>
        </div>
	</div>
	<#include "/WEB-INF/pages/system/project-member-edit.ftl">
</body>


