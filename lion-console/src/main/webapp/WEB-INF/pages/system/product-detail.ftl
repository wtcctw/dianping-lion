<div class="modal hide fade" id="myModal">
<div class="modal-header">
<button type="button" class="close" data-dismiss="modal">×</button>
<h3><#if id == 0>添加<#else>修改</#if>产品线</h3>
</div>
 <div class="modal-body">
        	<form class="form-horizontal">
        		<fieldset>
        		    <div class="control-group control-lion-group hidden">
				      <label class="control-label control-lion-label">标识</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-product-id" <@s.if test="%{id != 0}">value="${product.id}"</@s.if>/>
				      </div>
				    </div>
        			<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">名称</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-product-name" <@s.if test="%{id != 0}">value="${product.name}"</@s.if>/>
				        <span class="help-inline hide message" id="span-product-name-error"></span>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">负责人</label>
				      <div class="controls lion-controls">
				        <@s.select id="input-product-productLeaderName" list=r"userList" listKey="id" listValue="name" value="${selectedUserValue}"/>
				         <!-- theme="simple" <@s.if test="%{id != 0}">headerKey="${product.productLeaderId}" headerValue="${product.productLeaderName}"</@s.if> -->
				        <span class="help-inline hide message" id="span-product-productLeaderName-error"></span>
				      </div>
				    </div>
	        		<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">所属部门</label>
				      <div class="controls lion-controls">
				        <@s.select id="input-product-teamName" list=r"teamList" listKey="id" listValue="name" value="${selectedTeamValue}"/>
				        <!--  -->
				        <span class="help-inline hide message" id="span-product-teamName-error"></span>
				      </div>
				    </div>
				    <!--<div class="control-group control-lion-group">
				      <label class="control-label control-lion-label">顺序</label>
				      <div class="controls lion-controls">
				        <input type="text" class="input-xlarge" id="input-product-seq" <@s.if test="%{id != 0}">value="${product.seq}"</@s.if>>
				        <span class="help-inline hide message" id="span-product-seq-error"></span>
				      </div>
				    </div>-->
			    </fieldset>
        	</form>        </div>
        <div class="modal-footer">
          <a class="btn" data-dismiss="modal" >关闭</a>
          <a id="a-product-save" class="btn btn-primary" onclick="<#if id == 0>saveProduct()<#else>updateProduct()</#if>">保存</a>
        </div>
</div>
