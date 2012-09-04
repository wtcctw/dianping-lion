<head>
	<title>缓存管理</title>
	<script type="text/javascript" src="<@s.url value="/js/biz/system/cachemanage.js"/>"></script>
</head>
<body>
<table class="table table-bordered table-striped table-condensed">
	<thead>
    <tr>
      <th width="30">序号</th>
      <th width="390">Name</th>
      <th>CacheEntity</th>
      <th width="100">操作</th>
    </tr>
    </thead>
    <tbody>
	<@s.iterator value="caches" status="cacheStatus">
	<tr>
		<td>${cacheStatus.index}</td>
		<td>${key}</td>
		<td>${value.cacheEntities}</td>
		<td>
			<@s.if test="%{hasPrivilege()}">
			<a href="#" onClick="clearcache('${key}');" class="clear-btn no-dec">清除</a>
			</@s.if>
		</td>
	</tr>
	</@s.iterator>
	</tbody>
</table>
<!--
<div id="clear-cache-modal" class="modal hide">
	<div class="modal-header">
      <a class="close" data-dismiss="modal" >&times;</a>
      <h5>清除缓存</h5>
    </div>
    <div class="modal-body">
    	<input type="hidden" name="config-id">
    	<form class="form-horizontal" onSubmit="return false;">
    		<fieldset>
			    <div class="control-group control-lion-group">
			      <label class="control-label control-lion-label" for="cache-key">
			      	Key: 
			      </label>
			      <div class="controls lion-controls">
			      	<input type="text" id="cache-key">&nbsp;&nbsp;
			      	<input id="clear-cache-btn" type="button" class="btn btn-primary" value=" 确定 ">
			      </div>
			    </div>
		</fieldset>
    	</form>
    </div>
</div>
-->
</body>