<script type="text/javascript" src="<@s.url value="/js/biz/system/local-config-diff.js"/>"></script>
<div style="float: left">
<@s.form id="config-diff-form" cssClass="form-inline lion" action="/system/localConfigDiff.vhtml" method="get">
	<@s.hidden name="project.id" id="projectId" value="${pid}"/>
	<label class="control-label" for="diff-config-env1">环境1：</label>
	<@s.select id="diff-config-env1" name="criteria.lenvId" list="%{environments}" listKey="id"
	listValue="label" value="criteria.lenvId" cssClass="input-medium"/>
	<label class="control-label" for="diff-config-env2">环境2：</label>
	<@s.select id="diff-config-env2" name="criteria.renvId" list="%{environments}" listKey="id"
	listValue="label" value="criteria.renvId" cssClass="input-medium"/>
	<button id="diff-config-btn" type="submit" class="btn">配置比较</button>
	<span id="config-diff-warning"></span>
</@s.form>
</div>
<div style="float: left">
<button class="btn" onclick="location.href='/navigate.vhtml?menu=project&pid=${project.id}'">返回配置项</button>
</div>
