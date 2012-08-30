<div id="modalcontent" class="modal hide fade" id="myModal">
<div class="modal-header">
<button type="button" class="close" data-dismiss="modal">×</button>
<h3><#if id == 0>添加<#else>修改</#if>业务团队</h3>
</div>
 <div class="modal-body">
        	<form class="form-horizontal">
        		<fieldset>
        		    <div class="control-group control-lion-group hidden">
				      <label class="control-label control-lion-label">团队ID</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-team-id" <@s.if test="%{id != 0}">value="${team.id}"</@s.if>/>
				      </div>
				    </div>
        			<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">团队</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-team-name" <@s.if test="%{id != 0}">value="${team.name}"</@s.if>/>
				        <span class="help-inline hide message" id="span-team-name-error"></span>
				      </div>
				    </div>
			    </fieldset>
        	</form>        </div>
        <div class="modal-footer">
          <a class="btn" data-dismiss="modal" >关闭</a>
          <a id="a-team-save" class="btn btn-primary" onclick="<#if id == 0>saveTeam()<#else>updateTeam()</#if>">保存</a>
        </div>
</div>
