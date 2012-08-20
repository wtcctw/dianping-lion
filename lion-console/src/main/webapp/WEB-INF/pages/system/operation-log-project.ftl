<head>
	<script type="text/javascript" src="<@s.url value="/js/biz/system/operation-log.js"/>"></script>
</head>
	<div class="container">
		<div class="row">
			<div class="span12">
<#--搜索栏-->
<table class="table table-bordered nohover">
<tr>
	 <td>
            <label class="control-label" for="select01">项目</label>
            <input type="text" id="input-project-log" value="<@s.property value="projectName"/>" readonly="readonly"/>
            <input type="hidden" id="log-project" value="<@s.property value="pid"/>"></input>
            <label class="control-label" for="select02">操作类型</label>  
            <div class="controls">  
			<@s.select id="log-opType" list=r"opTypes" listKey="key" listValue="value" theme="simple"/> 
	 </td>
	 <td style="border: 0px">
            <label class="control-label" for="select03">操作人员</label>  
<@s.select id="log-user" list=r"users" listKey="id" listValue="name" theme="simple" headerKey="-1" headerValue="任意人员"/>
            <label class="control-label" for="select04">环境</label>  
<@s.select id="log-env" list=r"envs" listKey="id" listValue="label" theme="simple" headerKey="-1" headerValue="任意环境"/>
	 </td>
	 <td style="border: 0px">
            <label class="control-label" for="select05">开始时间</label>
			<input type="text" id="input-time-start-log"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',lang:'zh-cn'})"/>
            <label class="control-label" for="select06"  style="margin-top:9px;">结束时间</label>
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

<#include "/WEB-INF/pages/system/operation-log-list.ftl"> 
</div>
</div>
</div>
