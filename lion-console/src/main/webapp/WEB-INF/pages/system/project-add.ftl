
<head>
	<title>项目添加</title>
</head>
<body>
<div class="container">
		
<#include "/WEB-INF/pages/system/project-span.ftl"> 

<form id="projectQuery" class="form-horizontal" action="projectList.vhtml" method="post">
<fieldset>
<legend>添加项目</legend>
  <div class="control-group">
	<label class="control-label" for="productSelect">产品线</label>
	<div class="controls">
     <select id="productSelect">
       <#list teamList as team>
       	<#list team.products as product>
       	<option team=${team.id} value=${product.id}>${product.name}</option>
       	</#list>
		</#list>
     </select>
     </div>
   </div>
   	<div class="control-group">
            <label class="control-label" for="projectName">项目名</label>
            <div class="controls">
              <input type="text" class="input-xlarge" id="projectName">
    		</div>
    </div>
  <div class="control-group">
	<label class="control-label" for="techLeader">TechLeader</label>
	<div class="controls">
     <input type="text" class="span3" style="margin: 0 auto;" data-provide="typeahead" data-items="4" 
     data-source="[
     <#list userList as user>
     	'${user.name}'
     </#list>
     ]">
     </div>
   </div>
   <div class="control-group">
	<label class="control-label" for="oper">业务运维</label>
	<div class="controls">
     <select id="oper">
       <#list teamList as team>
       	<#list team.products as product>
       	<option team=${team.id} value=${product.id}>${product.name}</option>
       	</#list>
		</#list>
     </select>
     </div>
   </div>
   <div class="form-actions">
         <button type="submit" class="btn">保  存</button>
   </div>
   </fieldset>
</form>

</div>
</body>


