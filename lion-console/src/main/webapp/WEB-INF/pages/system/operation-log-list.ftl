<#--内容栏-->
<#include "/WEB-INF/pages/common/paginater.ftl">
<table id="table-oplog-list" class="table table-bordered table-striped table-condensed table-page">
	  <thead>
	    <tr>
	      <th width="30">序号</th>
	      <th width="150">操作类型</th>
	      <th width="100">操作人员</th>
	      <!--<th width="60">操作主机</th>-->
	      <th width="50">环境</th>
	      <th width="110">操作时间</th>
	      <th width="150">项目</th>
	      <th>内容</th>
	    </tr>
	  </thead>
	  <tbody>
	  	<@s.iterator value="paginater.results" var="operationLog" status="logStatus">
	  		<tr>
	      		<td>${logStatus.index}</td>
	      		<td><@s.if test="%{#operationLog.opTypeEnum != null}">${opTypeEnum.label}</@s.if></td>
	      		<td>${opUserName}</td>
	      		<td>${envName}</td>
	      		<td>${opTime?string("yy/MM/dd HH:mm:ss")}</td>
	      		<td>${projectName}</td>
	      		<td style="word-break:break-all;">
	      			<@s.if test="%{#operationLog.opTypeEnum == @com.dianping.lion.entity.OperationTypeEnum@API_TakeEffect
	      				|| #operationLog.opTypeEnum == @com.dianping.lion.entity.OperationTypeEnum@API_Rollback}">
	      				${content} &nbsp;&nbsp;
	      				<span class="label label-info pointer" onclick="viewdetail('middle', ${id}, 'Param:key5');">&nbsp;&nbsp;url&nbsp;&nbsp;</span>
	      				<@s.if test="%{#operationLog.key2 == 'false'}">
	      					<span class="label label-warning pointer" onclick="viewdetail('big', ${id}, 'Exception:key6');">error</span>
	      				</@s.if>
	      			</@s.if>
	      			<@s.elseif test="%{#operationLog.opTypeEnum == @com.dianping.lion.entity.OperationTypeEnum@API_SetConfig}">
	      				${content} &nbsp;&nbsp;
	      				<@s.if test="%{#operationLog.key1 == 'true'}">
	      					<span class="label label-info pointer" onclick="viewdetail('middle', ${id}, 'Param:key5');">&nbsp;&nbsp;url&nbsp;&nbsp;</span>
	      				</@s.if>
	      				<@s.if test="%{#operationLog.key2 == 'false'}">
	      					<span class="label label-warning pointer" onclick="viewdetail('big', ${id}, 'Exception:key6');">error</span>
	      				</@s.if>
	      			</@s.elseif>
	      			<@s.elseif test="%{#operationLog.opTypeEnum == @com.dianping.lion.entity.OperationTypeEnum@Config_Edit}">
		      			${content}&nbsp;&nbsp;
		      			<@s.if test="%{#operationLog.key5 == 'true'}">
		      				<span class="label label-success pointer" onclick="viewdetail('middle', ${id}, 'Before:key3,After:key4');">&nbsp;&nbsp;b/a&nbsp;&nbsp;</span>
		      			</@s.if>
	      			</@s.elseif>
	      			<@s.else>
		      			${content}
	      			</@s.else>
	      		</td>
	      	</tr>
	    </@s.iterator>
	  </tbody>
</table>
<#include "/WEB-INF/pages/common/paginater.ftl">
<div id="big-log-modal" class="modal hide modal-big">
    <div class="modal-body" style="min-height:480px;max-height: 540px;overflow-y: auto;">
    </div>
</div>
<div id="middle-log-modal" class="modal hide modal-middle">
    <div class="modal-body" style="min-height:90px;max-height: 540px;overflow-y: auto;">
    </div>
</div>




