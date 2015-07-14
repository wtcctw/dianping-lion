<div id="edit-service-modal" class="modal hide fade">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">&times;</a>
        <h3 id="edit-service-title">编辑服务</h3>
        <#include "/WEB-INF/pages/common/form-alert.ftl">
    </div>
    <div class="modal-body">
        <input type="hidden" name="service-id">
        <input type="hidden" name="project-id">
        <input type="hidden" name="env-id">
        <form class="form-horizontal" onSubmit="return false;">
            <fieldset>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-service-name">Name</label>
                    <div class="controls lion-controls">
                        <textarea rows="3" style="width: 350px;" id="edit-service-name"></textarea>
                        <span class="help-inline hide message">必填！</span>
                    </div>
                </div>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-service-desc">Description</label>
                    <div class="controls lion-controls">
                        <textarea rows="3" style="width: 350px;" id="edit-service-desc"></textarea>
                    </div>
                </div>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-service-group">Swimlane</label>
                    <div class="controls lion-controls">
                        <input type="text" id="edit-service-group"/>
                    </div>
                </div>
                <div class="control-group control-lion-group">
                    <label class="control-label control-lion-label" for="edit-service-hosts">Hosts</label>
                    <div class="controls lion-controls">
                        <textarea rows="6" style="width: 350px;" id="edit-service-hosts"></textarea>
                        <span class="help-inline hide message">必填！</span>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">关闭</a>
        <input id="service-save-btn" type="button" class="btn btn-primary" value="保存 ">
    </div>
</div>
