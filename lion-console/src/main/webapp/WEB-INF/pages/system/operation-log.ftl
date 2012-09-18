<head>
	<script type="text/javascript" src="<@s.url value="/js/biz/system/operation-log.js"/>"></script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<ul class="noborder smallpadding smallbottommargin breadcrumb">
					<li>
					    系统设置 <span class="divider">></span>
					</li>
					<li>操作日志</li>
				</ul>
				<#--搜索栏-->
				<@s.form id="searchForm" action="/system/oplog.vhtml" method="get">
				<input type="hidden" name="paginater.pageNumber" value="1"/>
				<table class="table table-bordered nohover lion">
				<tr>
					 <td width="23%">
				            <label class="control-label" for="select02">操作类型</label><br/>
							<@s.select id="log-opType" name="logCriteria.opType" list="opTypes" listKey="key" listValue="value"/> 
					 </td>
					 <td width="23%" style="border: 0;">
				            <label class="control-label" for="select03">操作人员</label><br/>
				            <@s.textfield id="op-user-loginname" name="logCriteria.userLoginName"/>
				            <@s.hidden id="op-user-id" name="logCriteria.userId"/>
					 </td>
					 <td width="23%" style="border: 0;">
				            <label class="control-label" for="select05">开始时间</label><br/>
				            <@s.date name="logCriteria.from" var="fromDate" format="yyyy-MM-dd HH:mm:ss"/>
				            <@s.textfield id="log-search-fromdate" name="logCriteria.from" value="%{#fromDate}" cssClass="Wdate"/>
					 </td>
					 <td style="border: 0;"  width="23%">
					 		<label class="control-label" for="select06">结束时间</label><br/>
				            <@s.date name="logCriteria.to" var="toDate" format="yyyy-MM-dd HH:mm:ss"/>
				            <@s.textfield id="log-search-todate" name="logCriteria.to" value="%{#toDate}" cssClass="Wdate"/>
				    </td>
				    <td rowspan="2" style="text-align:center;">
				    		<button id="button-query-log" type="submit" class="btn">检索</button>  
					</td>
					 </tr>
					 <tr>
					 	<td style="border-top: 0;">
					 		<label class="control-label" for="select01">项目</label><br/>
							<@s.select id="log-project" name="logCriteria.projectId" disabled="!logCriteria.projectRelated" list="projects" listKey="id" listValue="name" headerKey="" headerValue="任意项目"/>
					 	</td>
					 	<td style="border: 0;">
					 		 <label class="control-label" for="select04">环境</label><br/>
							<@s.select id="log-env" name="logCriteria.envId" disabled="!logCriteria.projectRelated" list=r"envs" listKey="id" listValue="label" headerKey="" headerValue="任意环境"/>
					 	</td>
					 	<td colspan="2" style="border: 0;">
					 		<label>内容</label><br/>
				             <@s.textfield name="logCriteria.content" cssStyle="width:485px;"/>
					 	</td>
					 </tr>
				</table>
				</@s.form>
				<#include "/WEB-INF/pages/system/operation-log-list.ftl"> 
			</div>
		</div>
	</div>
</body>