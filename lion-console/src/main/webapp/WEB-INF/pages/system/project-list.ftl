<#include "/WEB-INF/pages/system/project-header.ftl"> 
<form class="form-horizontal">
<div class="row">

<div class="span3">
	
		<div class="control-group">
	            <label class="control-label" style="width:50px" for="teamSelect">业务组</label>
	            <div class="controls" style="margin-left:60px">
	              <select id="teamSelect">
	                <option value=0>所有的</option>
	                <#list teamList as team>
	                	<option value=${team.id}>${team.name}</option>
					</#list>
	              </select>
	            </div>
	    </div>
	   
	</form>
</div>
<div class="span3">
		<div class="control-group">
	            <label class="control-label" style="width:50px" for="productSelect">产品线</label>
	            <div class="controls" style="margin-left:60px">
	              <select id="productSelect">
	                <option value=0>所有的</option>
	                <#list teamList as team>
	                	<#list team.products as product>
	                	<option team=${team.id} value=${product.id}>${product.name}</option>
	                	</#list>
					</#list>
	              </select>
	            </div>
	    </div>
</div>
</div>
</form>
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
	      <th >操作<a href="<@s.url action='projectAdd' namespace='/system'/>" rel="tooltip" title="添加项目"><i class="icon-plus pull-right"/>&nbsp&nbsp</a></th>
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
				<td style="text-align:center;">
					<a href="<@s.url action='projectDdit' namespace='/system'/>" rel="tooltip" title="修改项目">
	      		    <i class="icon-edit"></i>
	      		    </a>
	      		    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
	      		    <a href="<@s.url action='projectDel' namespace='/system'/>" rel="tooltip" title="删除项目">
					<i class="icon-remove"></i>
					</a>
			     </td>
	      	</tr>
	  	</#list>
	  </tbody>
</table>
</div>
</div>
<script language="javascript">
	$(document).ready(function(){
		$("#teamSelect").change(function(){
			$("option[team]").removeClass("hide");
			$("#productSelect option:first").attr("selected","true");
			if($(this).children('option:selected').val() > 0){
				
				$("option[team]").addClass("hide");
				$("option[team="+$(this).children('option:selected').val()+"]").removeClass("hide");
			}
		});
		
	});
</script>
<#include "/WEB-INF/pages/system/project-footer.ftl"> 

