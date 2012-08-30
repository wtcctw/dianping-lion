<div class="modal hide fade" id="myModal">
<div class="modal-header">
<button type="button" class="close" data-dismiss="modal">×</button>
<h3><#if envId == 0>添加<#else>修改</#if>lion环境</h3>
</div>
 <div class="modal-body">
        	<form class="form-horizontal">
        		<fieldset>
        		    <div class="control-group control-lion-group hidden">
				      <label class="control-label control-lion-label">环境ID</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-env-id" <@s.if test="%{envId != 0}">value="${environment.id}"</@s.if>/>
				      </div>
				    </div>
        			<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">环境名称</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-env-name" <@s.if test="%{envId != 0}">readonly="readonly" value="${environment.name}"</@s.if>/>
				        <span class="help-inline hide message" id="span-env-name-error"></span>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">环境别名</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-env-label" <@s.if test="%{envId != 0}">value="${environment.label}"</@s.if>/>
				        <span class="help-inline hide message" id="span-env-label-error"></span>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">zookeeper地址</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-env-ips" <@s.if test="%{envId != 0}">value="${environment.ips}"</@s.if>>
				        <span class="help-inline hide message" id="span-env-ips-error"></span>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">是否线上环境</label>
				      <div class="controls lion-controls">
				      	<input type="radio" name="input-env-online" id="input-env-online1" value="true"
				      		<@s.if test="%{environment.online}">checked="checked"</@s.if>><label for="input-env-online1" class="help-inline">是</label>
				      	<input type="radio" name="input-env-online" id="input-env-online2" value="false"
				      		<@s.if test="%{environment == null || !environment.online}">checked="checked"</@s.if>><label for="input-env-online2" class="help-inline">否</label>
				      </div>
				    </div>
				    <div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">环境顺序</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-env-seq" <@s.if test="%{envId != 0}">value="${environment.seq}"</@s.if>>
				        <span class="help-inline hide message" id="span-env-seq-error"></span>
				      </div>
				    </div>
			    </fieldset>
        	</form>        </div>
        <div class="modal-footer">
          <a class="btn" data-dismiss="modal" >关闭</a>
          <a id="a-env-save" class="btn btn-primary" onclick="<#if envId == 0>saveEnv()<#else>updateEnv()</#if>">保存</a>
        </div>
</div>
