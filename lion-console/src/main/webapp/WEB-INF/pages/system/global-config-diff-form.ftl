<script type="text/javascript" src="/js/biz/system/global-config-diff.js"></script>
<@s.form id="global-config-diff-form" cssClass="form-inline lion" action="/system/globalConfigDiff.vhtml" method="get">
	<label class="control-label" for="business-team">业务团队</label>
	<@s.select id="business-team" name="criteria.teamId" list="%{selectorTeams}" listKey="id"
		listValue="name" value="criteria.teamId" cssClass="input-medium"/>
	<label class="control-label" for="business-product">业务分支</label>
	<@s.select id="business-product" name="criteria.productId" list="%{selectorProducts}" listKey="id"
		listValue="name" value="criteria.productId" cssClass="input-medium"/>
	<label class="control-label" for="business-project">项目</label>
	<@s.select id="business-project" name="criteria.projectId" list="%{selectorProjects}" listKey="id"
		listValue="name" value="criteria.projectId" cssClass="input-medium"/>

	<label class="control-label" for="left-env-id">环境1</label>
	<@s.select id="left-env-id" name="criteria.lenvId" list="%{environments}" listKey="id"
		listValue="label" value="criteria.lenvId" cssClass="input-medium"/>
	<label class="control-label" for="right-env-id">环境2</label>
	<@s.select id="right-env-id" name="criteria.renvId" list="%{environments}" listKey="id"
		listValue="label" value="criteria.renvId" cssClass="input-medium"/>
	<button type="submit" class="btn">配置比较</button>
</@s.form>
