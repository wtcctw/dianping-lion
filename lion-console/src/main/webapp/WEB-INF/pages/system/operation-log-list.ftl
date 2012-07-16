<#--内容栏-->
<table id="table-oplog-list" class="table table-bordered table-striped table-condensed">
	  <thead>
	    <tr>
	      <th style="width: 30px">序号</th>
	      <th style="width: 100px">操作类型</th>
	      <th style="width: 60px">操作人员</th>
	      <th style="width: 60px">操作主机</th>
	      <th style="width: 30px">环境</th>
	      <th style="width: 70px">创建时间</th>
	      <th style="width: 120px">项目</th>
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
