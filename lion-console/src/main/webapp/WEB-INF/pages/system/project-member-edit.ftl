<div id="edit-project-member-modal" class="modal hide fade">
    <div class="modal-body" style="min-height:430px;max-height: 442px;overflow-y: auto;">
		<input type="hidden" id="member_projectId">
		<div class="tabbable">
			<ul class="nav nav-tabs">
			    <li class="active"><a href="#owner" id="owner-link" data-toggle="tab">管理员</a></li>
				<li><a href="#member" data-toggle="tab">项目成员</a></li>
			    <li><a href="#operator" data-toggle="tab">运维</a></li>
			    &nbsp;
				<input type="text" id="user-search" style="margin-bottom:1px;">
	    			<input type="hidden" id="user-search-id">&nbsp;
	    			<a href="#" class="no-dec" id="add-member">添加</a>
				<a class="close" data-dismiss="modal" >&times;</a>
			</ul>
			<div class="tab-content" id="member-container">
		    
		  	</div>
		</div>
    </div>
    <div class="modal-footer" style="text-align:left;">
      	<a href="#" class="btn pull-right" data-dismiss="modal">关闭</a>
    </div>
</div>