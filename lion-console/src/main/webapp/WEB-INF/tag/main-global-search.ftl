<@s.if test="listSearchPage">
<script type="text/javascript" src="/js/biz/system/global-config-search.js"></script>
<div style="position:absolute;top:0;bottom:0;left:0;right:0;margin:auto;height:6%;width:63%;">
<@s.form id="global-config-search-form" cssClass="form-inline lion" style="text-align:center" action="/system/globalSearch.vhtml" method="get">
<label class="control-label" for="key">Key：</label>
	 <@s.textfield id="key" name="criteria.key" cssClass="input-medium" maxlength="100"/>
<label class="control-label" for="value">Value：</label>
	 <@s.textfield id="value" name="criteria.value" cssClass="input-medium" maxlength="50"/>
<label class="control-label" for="env">环境：</label>
	 <@s.select id="env" name="criteria.envId" list="%{environments}" listKey="id"
	 listValue="label" value="criteria.envId" cssClass="input-medium"/>
<button type="submit" class="btn">搜索</button>
</@s.form>
</div>
</@s.if>