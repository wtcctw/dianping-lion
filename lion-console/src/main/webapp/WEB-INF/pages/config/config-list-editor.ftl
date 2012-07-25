<div id="list-editor" class="modal hide fade">
	<div class="modal-header">
      <a class="close" data-dismiss="modal">&times;</a>
      <h3 class="list-editor-title">List类型编辑器</h3>
      <#include "/WEB-INF/pages/common/form-alert.ftl">
    </div>
	<div class="modal-body" style="min-height:400px;max-height: 412px;overflow-y: auto;">
	</div>
	<div class="modal-footer">
		<div class="pull-left">
			<input type="checkbox" class="trim_check" id="list_trim_check" checked="checked">
			<label for="list_trim_check" style="display:inline;">trim input</label>
		</div>
		<a href="#" class="btn" data-dismiss="modal">关闭</a>
		<a href="#" class="btn btn-primary ok-btn">确认</a>
	</div>
</div>