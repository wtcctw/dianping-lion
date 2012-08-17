<#--内容栏-->
<table id="table-job-list"
	class="table table-bordered table-striped table-condensed">
	<thead>
		<tr>
			<th style="width: 30px">序号</th>
			<th style="width: 100px">任务名称</th>
			<th>上次执行时间</th>
			<th>任务开关</th>
			<th>负责人邮箱</th>
			<th style="width: 80px">操作
				<a data-toggle="modal" href="<@s.url action='jobAddAjax' namespace='/system'/>"
				rel="tooltip" title="添加任务"> <i class="icon-plus pull-right" />&nbsp&nbsp</a></th>
		</tr>
	</thead>
	<tbody>
		<#list jobExecTimeList as jobExecTime>
		<tr>
			<td>${jobExecTime_index + 1}</td>
			<td>${jobExecTime.jobName}</td>
			<td>${jobExecTime.lastJobExecTime?string("yyyy-MM-dd HH:mm:ss")}</td>
			<td><#if jobExecTime.switcher>开<#else>关</#if></td>
			<!-- <@s.if test="%{jobExecTime.switcher}">value="开"</@s.if> 
			<#if jobExecTime.switcher>开<#else>关</#if>-->
			<td>${jobExecTime.failMail}</td>
			<td style="text-align: center;">
			<a data-toggle="modal" href="<@s.url action='jobEditAjax' namespace='/system'/>?jobId=${jobExecTime.id}"
				rel="tooltip" title="修改任务"> <i class="icon-edit"></i> </a>
			&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
			<a class="deletelink" href="<@s.url action='jobDeleteAjax' namespace='/system'/>?jobId=${jobExecTime.id}"
				rel="tooltip" title="删除任务"> <i class="icon-remove"></i> </a>
			</td>
		</tr>
		</#list>
	</tbody>
</table>
