<#--内容栏-->
<table id="table-oplog-list" class="table table-bordered table-striped table-condensed">
	  <thead>
	    <tr>
	      <th width="30">序号</th>
	      <th width="100">操作类型</th>
	      <th width="60">操作人员</th>
	      <th width="60">操作主机</th>
	      <th width="50">环境</th>
	      <th width="70">操作时间</th>
	      <th width="120">项目</th>
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
	      		<td style="word-break:break-all;">${operationLog.content}</td>
	      	</tr>
	  	</#list>
	  </tbody>
</table>
