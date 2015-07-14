<table style="table-layout: fixed" class="table table-bordered table-striped table-condensed">
    <thead>
    <tr>
        <th width="150">业务团队</th>
        <th width="150">业务分支</th>
        <th width="200">项目名</th>
        <th width="100">差异数目</th>
        <th width="100">遗漏数目</th>
        <th width="150">更新时间</th>
    </tr>
    </thead>
    <tbody>
	<@s.hidden name="criteria.levnId" value="${criteria.lenvId}"/>
	<@s.hidden name="criteria.revnId" value="${criteria.renvId}"/>
	<#list globalConfigDiffs as globalConfigDiff>
    <tr class="search_row">
		<@s.hidden name="criteria.projectId" value="${globalConfigDiff.projectId}"/>
		<td>${globalConfigDiff.teamName}</td>
		<td>${globalConfigDiff.productName}</td>
		<td><a href="/navigate.vhtml?menu=project&pid=${globalConfigDiff.projectId}">${globalConfigDiff.projectName}</a></td>
        <td>
			<a href="/system/localConfigDiffNumDetail.vhtml?criteria.projectId=${globalConfigDiff.projectId}&criteria.lenvId=${criteria.lenvId}&criteria.renvId=${criteria.renvId}">
				${globalConfigDiff.diffNum}
			</a>
		</td>
		<td>
            <a href="/system/localConfigOmitNumDetail.vhtml?criteria.projectId=${globalConfigDiff.projectId}&criteria.lenvId=${criteria.lenvId}&criteria.renvId=${criteria.renvId}">
				${globalConfigDiff.omitNum}
			</a>
		</td>
		<td>${globalConfigDiff.updateTime?string("yyyy-MM-dd HH:mm:ss")}</td>
    </tr>
	</#list>
    </tbody>
</table>
