<table class="table table-bordered table-striped table-condensed">
	<thead>
	<tr>
		<th width="80">环境</th>
		<th width="200">项目名称</th>
		<th width="200">KEY</th>
		<th width="400">VALUE</th>
		</th>
	</tr>
	</thead>
	<tbody>
	<#list globalSearches as globalSearch>
	<tr class="search_row">
		<td>${globalSearch.envLabel}</td>
		<td><a href="/navigate.vhtml?menu=project&pid=${globalSearch.id}">${globalSearch.name}</a></td>
		<td>${globalSearch.key}</td>
        <td <#if globalSearch.isLongValue()>title="${globalSearch.moreValue?html}"</#if>>
			${globalSearch.getAbbrevValue()?html}
        </td>
	</tr>
	</#list>
	</tbody>
</table>
