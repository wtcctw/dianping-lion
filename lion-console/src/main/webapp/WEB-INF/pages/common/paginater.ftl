<#--内容栏-->
<div class="pagination">
	<input type="hidden" name="pageNo" value="${paginater.pageNumber}"/>
	<ul>
		<li><a href="#" onclick="jump2Page(1, this);return false;">首页</a></li>
		<li><a href="#" onclick="jump2Page(${paginater.prevPage}, this);return false;">前一页</a></li>
		<@s.iterator value="paginater.pageNumbers" var="pageNum">
			<li<@s.if test="%{#pageNum == paginater.pageNumber}"> class="active"</@s.if>
				><a href="#" onclick="jump2Page(${pageNum}, this);return false;">${pageNum}</a></li>
		</@s.iterator>
	    <li><a href="#" onclick="jump2Page(${paginater.nextPage}, this);return false;">后一页</a></li>
	    <li><a href="#" onclick="jump2Page(${paginater.totalPages}, this);return false;">尾页</a></li>
	    <li><a href="javascript: return false;">共${paginater.totalPages}页 | ${paginater.totalCount}条 | 每页${paginater.maxResults}条</a></li>
	</ul>
</div>