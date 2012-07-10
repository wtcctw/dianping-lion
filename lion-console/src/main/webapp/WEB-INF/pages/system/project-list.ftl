<#include "/WEB-INF/pages/system/project-header.ftl"> 
<div class="row">
<div class="span12">
<table class="table table-bordered table-striped table-condensed">
	  <thead>
	    <tr>
	      <th>序号</th>
	      <th>业务组</th>
	      <th>所属产品线</th>
	      <th>项目名</th>
	      <th>创建时间</th>
	      <th>更新时间</th>
	      <th>操作<i class="icon-plus pull-right"/></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list projectList as project>
	  		<tr>
	      		<td>${project_index + 1}</td>
	      		<td>${project.teamName}</td>
	      		<td>${project.productName}</td>
	      		<td>${project.name}</td>
	      		<td>${project.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	      		<td>${project.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	      		<td>
	      		    <table class="table">
	      		    <tbody>
	      		    <tr>
				    <td><i class="icon-edit"></i></td>
					<td><i class="icon-remove"></i></td>
					</tr>
					</tbody>
					</table>
			     </td>
	      	</tr>
	  	</#list>
	  </tbody>
</table>
</div>
</div>
<#include "/WEB-INF/pages/system/project-footer.ftl"> 