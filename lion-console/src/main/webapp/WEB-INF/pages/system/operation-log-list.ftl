<#--搜索栏-->
<table class="table table-bordered table-striped table-condensed">

<table>

<#--内容栏-->
<table class="table table-bordered table-striped table-condensed">
	  <thead>
	    <tr>
	      <th>序号</th>
	      <th>操作类型</th>
	      <th>操作人员</th>
	      <th>操作主机</th>
	      <th>环境</th>
	      <th>创建时间</th>
	      <th>项目</th>
	      <th>内容</th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list operationLogs as operationLog>
	  		<tr>
	      		<td>${operationLog_index + 1}</td>
	      		<td>${operationLog.opName}</td>
	      		<td>${operationLog.opUserName}</td>
	      		<td>${operationLog.opUserIp}</td>
	      		<td>${operationLog.envName}</td>
	      		<td>${operationLog.opTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	      		<td>${operationLog.projectName}</td>
	      		<td style="max-width:300px;word-break:break-all;">${operationLog.content}</td>
	      	</tr>
	  	</#list>
	  </tbody>
</table>
