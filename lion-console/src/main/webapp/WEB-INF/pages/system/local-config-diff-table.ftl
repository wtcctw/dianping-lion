<table style="table-layout: fixed" class="table table-bordered table-striped table-condensed">
	<thead>
	<tr>
		<th width="150">KEY</th>
		<th width="200">${lenvironment.label}</th>
		<th width="200">${renvironment.label}</th>
	</tr>
	</thead>
	<tbody>
	<#list localConfigDiffList as localConfigDiff>
	<tr class="search_row">
		<#if !localConfigDiff.equal>
            <td title="${localConfigDiff.key}">
				${localConfigDiff.key}
            </td>
            <td <#if localConfigDiff.isLongLValue()>title="${localConfigDiff.moreLValue?html}"</#if>>
				${localConfigDiff.getAbbrevLValue()?html}
            </td>
            <td <#if localConfigDiff.isLongRValue()>title="${localConfigDiff.moreRValue?html}"</#if>>
				${localConfigDiff.getAbbrevRValue()?html}
            </td>
		</#if>
	</tr>
	</#list>
	</tbody>
</table>
