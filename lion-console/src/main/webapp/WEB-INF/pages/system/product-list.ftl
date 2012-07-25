<table id="table-product-list" class="table table-bordered table-striped table-condensed">
	  <thead>
	    <tr>
	      <th style="width: 30px">序号</th>
	      <th style="width: 80px">产品线</th>
	      <th style="width: 50px">负责人</th>
	      <th style="width: 50px">部门</th>
	      <th>创建时间</th>
	      <th>更新时间</th>
	      <th style="width: 100px">操作<a data-toggle="modal" href="<@s.url action='productAddAjax' namespace='/system'/>"
				rel="tooltip" title="添加产品线"> <i class="icon-plus pull-right" />&nbsp&nbsp</a></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<#list productList as product>
	  		<tr>
	      		<td>${product_index + 1}</td>
	      		<td>${product.name}</td>
	      		<td>${product.productLeaderName}</td>
	      		<td>${product.teamName}</td>
	      		<td>${product.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	      		<td>${product.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>
				<td style="text-align:center;">
			<a data-toggle="modal" href="<@s.url action='productEditAjax' namespace='/system'/>?id=${product.id}"
				rel="tooltip" title="修改产品"> <i class="icon-edit"></i> </a>
			&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
			<a class="deletelink" href="<@s.url action='productDelete' namespace='/system'/>?id=${product.id}"
				rel="tooltip" title="删除产品"> <i class="icon-remove"></i> </a>
			     </td>
	      	</tr>
	  	</#list>
	  </tbody>
</table>


