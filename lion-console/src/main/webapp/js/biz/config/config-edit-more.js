var $commonAlert;
var modal_context_edited = false;

$(function(){

    $("[rel=tooltip]").tooltip({delay : {show : 800}});
    
    $("#create-context-btn").click(
		function() {
			if(!validate())
				return;
			$.ajax("/config/saveContextValueAjax.vhtml".prependcontext(), {
				type : "POST",
				data : $.param({
					"envId" : $("#env-id").val(),
					"configId" : $("#config-id").val(),
					"context" : $("#context-name").val(),
					"value" : $("#context-value").val()
				}, true),
				dataType: "json",
				success : function(result) {
					if (result.code == Res_Code_Success) {
						$commonAlert.html("添加成功").dialog("open");
						reloadContextListTable();
					} else if (result.code == Res_Code_Error) {
						$commonAlert.html(result.msg).dialog("open");
					}
				}
			}
			);
		}
	);
    
    bindContextTableEvents();
    
    function bindContextTableEvents() {

        $(".remove-context-btn").click(
            function() {
                $.ajax("/config/deleteContextValueAjax.vhtml".prependcontext(), {
                    type : "POST",
                    data : $.param({
                        "envId" : getEnvId($(this)),
                        "configId" : $("#config-id").val(),
                        "context" : getContext($(this))
                    }, true),
                    dataType: "json",
                    success : function(result) {
                        if (result.code == Res_Code_Success) {
                            $commonAlert.html("删除成功").dialog("open");
                            reloadContextListTable();
                        } else if (result.code == Res_Code_Error) {
                            $commonAlert.html(result.msg).dialog("open");
                        }
                    }
                });
            }
        );

        $(".edit-context-btn").click(
        	function() {
    			$("#edit-context-title").text("编辑泳道配置");
    			$("#edit-context-modal [name='env-id']").val(getEnvId($(this)));
    			$("#edit-context-modal [name='config-id']").val($("#config-id").val());
    			$("#edit-context-env").val(getEnvLabel($(this)));
    			$("#edit-context-key").val($("#config-key").val());
    			$("#edit-context-name").val(getContext($(this)));
    			$("#edit-context-desc").val("");
    			$("#edit-context-value").val(getValue($(this)));
    			$("#edit-context-env").attr('readonly','readonly');
    			$("#edit-context-key").attr('readonly','readonly');
    			$("#edit-context-name").attr('readonly','readonly');
    			$("#edit-context-modal").modal({
    				backdrop : "static",
    				keyboard : false
    			});
    			return false;
        	}
        );
    }

    $("#context-save-btn").click(
		function() {
			$.ajax("/config/updateContextValueAjax.vhtml".prependcontext(), {
				type: "POST",
				data: $.param({
					"envId" : $("#edit-context-modal [name='env-id']").val(),
					"configId" : $("#edit-context-modal [name='config-id']").val(),
					"context" : $("#edit-context-name").val(),
					"value" : $("#edit-context-value").val()
				}, true),
				dataType: "json",
				success: function(result) {
					$('#edit-context-modal').modal('hide');
					if (result.code == Res_Code_Success) {
						modal_context_edited = true;
						$commonAlert.html("更新成功").dialog("open");
					} else if (result.code == Res_Code_Error) {
						$commonAlert.html(result.msg).dialog("open");
					}
				}
			});
		}
    );
    
    function validate() {
    	var context = $("#context-name").val().trim();
		if (context.length == 0) {
			$commonAlert.html("泳道不能为空").dialog("open");
			return false;
		}
		
		var value = $("#context-value").val().trim();
		if(value.length == 0) {
			$commonAlert.html("Value不能为空").dialog("open");
			return false;
		}
		
		return true;
    }
    
    $("#edit-context-modal").on("show", function() {
		modal_context_edited = false;
	});

    $("#edit-context-modal").on("hidden", function() {
		if (modal_context_edited) {
			reloadContextListTable();
		}
	});

	function reloadContextListTable() {
		$("#context-list-container").load("/config/getContextListAjax.vhtml".prependcontext(), $.param({
			"configId" : $("#config-id").val(),
			"envId" : $("#env-id").val()
		}, true), function() {
			bindContextTableEvents();
		});
	}

    function getEnvId($element_in_row) {
        return $element_in_row.parents(".tab-pane").find("#env-id").val();
    }

    function getEnvLabel($element_in_row) {
    	return $element_in_row.parents(".tab-pane").find("#env-label").val();
    }

    function getContext($element_in_row) {
        return $element_in_row.parents(".context-row").find("#context").text().trim();
    }

    function getValue($element_in_row) {
        return $element_in_row.parents(".context-row").find("#value").text().trim();
    }

    $commonAlert = $("<div class='alert-body'></div>")
        .dialog({
            autoOpen : false,
            resizable : false,
            modal : true,
            title : "信息框",
            height : 180,
            buttons : {
                "确定" : function() {$(this).dialog("close");}
            }
        }
    );

});
