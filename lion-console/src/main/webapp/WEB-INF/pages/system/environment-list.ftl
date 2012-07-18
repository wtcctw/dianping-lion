<head>
	<script type="text/javascript" src="<@s.url value="/js/biz/system/environment.js"/>"></script>
</head>
<div class="container">
	<div class="row">
		<div class="span12">
			<ul class="noborder smallpadding smallbottommargin breadcrumb">
			  <li>
			 	  环境设置 <span class="divider"></span>
			  </li>
			</ul>
			<#--内容栏-->
			<table id="table-env-list" class="table table-bordered table-striped table-condensed">
				  <thead>
				    <tr>
				      <th style="width: 30px">序号</th>
				      <th style="width: 100px">环境</th>
				      <th>zookeeper地址</th>
				      <th style="width: 80px">操作<a data-toggle="modal" href="<@s.url action='envAddAjax' namespace='/system'/>" rel="tooltip" title="添加环境">
				      	<i class="icon-plus pull-right"/>&nbsp&nbsp</a></th>
				    </tr>
				  </thead>
				  <tbody>
				  	<#list environmentList as environment>
				  		<tr>
				      		<td>${environment_index + 1}</td>
				      		<td>${environment.label}</td>
				      		<td>${environment.ips}</td>
				      		<td style="text-align:center;">
								<a href="<@s.url action='projectDdit' namespace='/system'/>" rel="tooltip" title="修改环境">
				      		    <i class="icon-edit"></i>
				      		    </a>
				      		    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
				      		    <a href="<@s.url action='projectDel' namespace='/system'/>" rel="tooltip" title="删除环境">
								<i class="icon-remove"></i>
								</a>
			     			</td>
				      	</tr>
				  	</#list>
				  </tbody>
			</table>
				<div class="modal hide" id="myModal">
				  <div class="modal-header">
				    <button type="button" class="close" data-dismiss="modal">×</button>
				    <h3>添加lion环境</h3>
				  </div>
				  <div class="modal-body">
				    <table>
				    	<tr>
				    		<td><label class="control-label">环境名称</label></td>
				    		<td><input type="text"></input></td>
				    	</tr>
				    	<tr>
				    		<td><label class="control-label">zookeeper地址</label></td>
				    		<td><input type="text"></input></td>
				    	</tr>
				    </table>
				  </div>
				  <div class="modal-footer">
				    <a href="#" class="btn" data-dismiss="modal">关闭</a>
				    <a href="#" class="btn btn-primary">提交</a>
				  </div>
				</div>
		</div>
	</div>
</div>
