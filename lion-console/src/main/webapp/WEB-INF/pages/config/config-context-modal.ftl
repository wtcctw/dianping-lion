<div id="edit-context-modal" class="modal hide fade">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">&times;</a>
        <h3 id="edit-context-title">编辑泳道配置</h3>
        <#include "/WEB-INF/pages/common/form-alert.ftl">
    </div>
    <div class="modal-body">
        <input type="hidden" name="env-id">
        <input type="hidden" name="config-id">
        <form class="form-horizontal" onSubmit="return false;">
            <fieldset>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-context-env">环境</label>
                    <div class="controls lion-controls">
                        <input type="text" id="edit-context-env"/>
                    </div>
                </div>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-context-key">Key</label>
                    <div class="controls lion-controls">
                        <input type="text" id="edit-context-key"/>
                    </div>
                </div>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-context-name">泳道</label>
                    <div class="controls lion-controls">
                        <input type="text" id="edit-context-name"/>
                    </div>
                </div>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-context-desc">描述</label>
                    <div class="controls lion-controls">
                        <input type="text" id="edit-context-desc"/>
                    </div>
                </div>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-context-value">Value</label>
                    <div class="controls lion-controls">
                        <textarea rows="3" style="width: 350px;" id="edit-context-value"></textarea>
                        <span class="help-inline hide message">必填！</span>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">关闭</a>
        <input id="context-save-btn" type="button" class="btn btn-primary" value="保存 ">
    </div>
</div>
