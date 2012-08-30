<#--内容栏-->
<div class="pagination">
	<ul>
		<li><a href="javascript: jump2Page(1);">首页</a></li>
		<li><a href="javascript: jump2Page(${paginater.prevPage});">前一页</a></li>
		<@s.iterator value="paginater.pageNumbers" var="pageNum">
			<li<@s.if test="%{#pageNum == paginater.pageNumber}"> class="active"</@s.if>
				><a href="javascript: jump2Page(${pageNum});">${pageNum}</a></li>
		</@s.iterator>
	    <li><a href="javascript: jump2Page(${paginater.nextPage});">后一页</a></li>
	    <li><a href="javascript: jump2Page(${paginater.totalPages});">尾页</a></li>
	    <li><a href="javascript: return false;">共${paginater.totalPages}页 | ${paginater.totalCount}条 | 每页${paginater.maxResults}条</a></li>
	</ul>
</div>