<div class="modal hide fade" id="myModal">
<div class="modal-header">
<button type="button" class="close" data-dismiss="modal">×</button>
<h3><#if jobId == 0>添加<#else>修改</#if>任务</h3>
</div>
 <div class="modal-body">
        	<form class="form-horizontal">
        		<fieldset>
        		    <div class="control-group control-lion-group hidden">
				      <label class="control-label control-lion-label">任务ID</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-job-id" <@s.if test="%{jobId != 0}">value="${jobExecTime.id}"</@s.if>/>
				      </div>
				    </div>
        			<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">任务名称</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-job-name" <@s.if test="%{jobId != 0}">value="${jobExecTime.jobName}" readonly="readonly"</@s.if>/>
				        <span class="help-inline hide message" id="span-job-name-error"></span>
				      </div>
				    </div>
				    <div class="control-group control-lion-group">
			      	<label class="control-label control-lion-label" for="config-value">任务开关:</label>
			      	<div class="controls lion-controls" id="input-job-switcher-id">
				      <input type="radio" name="input-job-switcher" onclick="changeRadioStatus('input-job-switcher', 'config-value-yes')" 
				      id="config-value-yes" value="true" <@s.if test="%{jobId == 0 || jobExecTime.switcher}">checked="checked"</@s.if>/>
				     	 <label for="config-value-yes" class="help-inline">开</label>
				      <input type="radio" name="input-job-switcher" onclick="changeRadioStatus('input-job-switcher', 'config-value-no')" 
				      id="config-value-no" value="false" <@s.if test="%{jobId != 0 && !jobExecTime.switcher}">checked="checked"</@s.if>/>
				      <label for="config-value-no" class="help-inline">关</label>
			      </div>
			    	</div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">任务负责人邮箱</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-job-failMail" <@s.if test="%{jobId != 0}">value="${jobExecTime.failMail}"</@s.if>>
				        <span class="help-inline hide message" id="span-job-failMail-error"></span>
				      </div>
				    </div>
			    </fieldset>
        	</form>        </div>
        <div class="modal-footer">
          <a class="btn" data-dismiss="modal" >关闭</a>
          <a id="a-job-save" class="btn btn-primary" onclick="<#if jobId == 0>saveJob()<#else>updateJob()</#if>">保存</a>
        </div>
</div>
