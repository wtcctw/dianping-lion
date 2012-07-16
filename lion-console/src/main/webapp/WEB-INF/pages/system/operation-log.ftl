<head>
	<script type="text/javascript" src="<@s.url value="/js/biz/system/operation-log.js"/>"></script>
</head>
	<div class="container">
		<div class="row">
			<div class="span12">
<#--搜索栏-->
<table class="table table-bordered">
<tr>
	 <td>
            <label class="control-label" for="select01">项目</label>  
			<@s.select id="log-project" list=r"projects" listKey="id" listValue="name" theme="simple" headerKey="-1" headerValue="任意项目"/>  
            <label class="control-label" for="select02">操作类型</label>  
            <div class="controls">  
			<@s.select id="log-opType" list=r"opTypes" listKey="key" listValue="value" theme="simple"/> 
	 </td>
	 <td style="border: 0px">
            <label class="control-label" for="select03">操作人员</label>  
<@s.select id="log-user" list=r"users" listKey="id" listValue="name" theme="simple" headerKey="-1" headerValue="任意人员"/>
            <label class="control-label" for="select04">环境</label>  
<@s.select id="log-env" list=r"envs" listKey="id" listValue="name" theme="simple" headerKey="-1" headerValue="任意环境"/>
	 </td>
	 <td style="border: 0px">
            <label class="control-label" for="select05">开始时间</label>
			<input type="text" id="input-time-start-log"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',lang:'zh-cn'})"/>
            <label class="control-label" for="select06">结束时间</label>
			<input type="text" id="input-time-end-log"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd  HH:mm',lang:'zh-cn'})"/>
	 </td>
	 <td style="border: 0px">
             <label>内容</label>  
           <textarea class="input-xlarge" id="log-textarea" rows="3"></textarea>
    </td>
    <td style="text-align:right;border: 0px;vertical-align: bottom">
	      <div>
            <button id="button-query-log" type="submit" class="btn"">检索</button>  
          </div> 
	 </td>
	 </tr>
</table>

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
</div>
</div>
</div>
