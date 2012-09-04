<div id="login-modal" class="modal hide">
	<div class="modal-header">
      <a class="close" data-dismiss="modal" >&times;</a>
      <font class="lion-h3">用户登陆</font>
      <span style="margin-left:83px;" id="login-msg"></span>
    </div>
    <div class="modal-body">
    		<form class="form-horizontal">
		  <fieldset>
		    <div class="control-group">
		      <label class="control-label" for="login-name">域账号:</label>
		      <div class="controls">
		        <input type="text" class="input-xlarge" id="login-name">
		        <span class="help-inline hide message">必填!</span>
		      </div>
		    </div>
		    <div class="control-group">
		      <label class="control-label" for="login-passwd">密码:</label>
		      <div class="controls">
		        <input type="password" class="input-xlarge" id="login-passwd">
		        <span class="help-inline hide message">必填!</span>
		      </div>
		    </div>
		  </fieldset>
		</form>
    </div>
    <div class="modal-footer" style="text-align:center;">
      <input id="login-btn" type="button" class="btn btn-primary" value="  登陆  ">&nbsp;&nbsp;&nbsp;&nbsp;
      <input id="login-close-btn" type="button" class="btn" data-dismiss="modal" value="  关闭  ">
    </div>
</div>