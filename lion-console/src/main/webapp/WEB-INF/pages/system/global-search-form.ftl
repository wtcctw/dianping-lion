<@s.form id="globalSearchForm" cssClass="form-inline lion" action="/system/globalSearch.vhtml" method="get">
	<label class="control-label" for="key">KEY：</label>
	<@s.textfield id="key" name="criteria.key" cssClass="input-medium" maxlength="100"/>
	<label class="control-label" for="value">VALUE：</label>
	<@s.textfield id="value" name="criteria.value" cssClass="input-medium" maxlength="50"/>
	<label class="control-label" for="env">ENVS：</label>
	<@s.select id="env" name="criteria.envId" list="%{environments}" listKey="id"
	listValue="label" value="criteria.envId" cssClass="input-medium"/>
	<button type="submit" class="btn">搜索</button>
</@s.form>

