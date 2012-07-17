
<head>
	<title>项目设置</title>
</head>
<body>
<input type="hidden" id="teamSelected" value="${teamSelect}" />
<input type="hidden" id="productSelected" value="${productSelect}" />
<div class="container">
		
<#include "/WEB-INF/pages/system/project-span.ftl"> 

<form id="projectQuery" class="form-inline lion" action="projectList.vhtml" method="post">

	
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

	function linkage(){
		$("option[team]").removeClass("hide");
		if($("#teamSelect").children('option:selected').val() != $("#productSelect").children('option:selected').attr("team")){
			$("#productSelect option").removeAttr("selected");
			$("#productSelect option:first").attr("selected","true");
		}
		
		if($("#teamSelect").children('option:selected').val() > 0){
			
			$("option[team]").addClass("hide");
			$("option[team="+$("#teamSelect").children('option:selected').val()+"]").removeClass("hide");
		}
	}

	$(document).ready(function(){
		
		$("#teamSelect").find("option").removeAttr("selected");
		var teamS = "option[value='"+$("#teamSelected").attr("value")+"']"
		$("#teamSelect").find(teamS).attr("selected","true");
		
		$("#productSelect").find("option").removeAttr("selected");
		var productS = "option[value='"+$("#productSelected").attr("value")+"']"
		$("#productSelect").find(productS).attr("selected","true");
		
		linkage();
		//联动select
		$("#teamSelect").change(linkage);
		//提交
		$("#query_btn").click(function(){
			var purl = "projectList.vhtml?teamSelect="+$("#teamSelect").children('option:selected').val()+"&productSelect="+$("#productSelect").children('option:selected').val();
			$("#projectQuery").attr("action",purl);
			$("#projectQuery").submit();
		});
	});
	
	
</script>

	</div>
</body>


