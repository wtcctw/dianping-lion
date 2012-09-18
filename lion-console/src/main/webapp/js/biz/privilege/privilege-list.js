var jump2Page;
var $commonAlert;
var $deleteRoleAlert;
var $deleteRoleUserAlert;

$(function(){
	$("[rel=tooltip]").tooltip({delay : {show : 800}});
	bindRoleTableEvents();
	
	$commonAlert = $("<div class='alert-body'></div>")
		.dialog({
			autoOpen : false, 
			resizable : false,
			modal : true,
			title : "信息框",
			height : 140,
			buttons : {
				"确定" : function() {$(this).dialog("close");}
			}
		}
	);
	
	$deleteRoleAlert = $("<div>确认删除该角色? <br/>将解除所有用户对该角色的绑定.</div>")
		.dialog({
			autoOpen : false,
			resizable : false,
			modal : true,
			title : "提示框",
			height : 140,
			buttons : {
				"是" : function() {
					$(this).dialog("close");
					$.ajax("/privilege/deleteRoleAjax.vhtml".prependcontext(), {
						data: $.param({
							"roleId" : $(this).data("roleId")
						}, true),
						dataType: "json",
						success: function(result) {
							if (result.code == Res_Code_Success) {
								$("#role-related-container").html("");
								reloadRoleListTable();
							} else if (result.code == Res_Code_Error) {
								$commonAlert.html("角色删除失败!").dialog("open");
							}
						}
					});
				},
				"否" : function() {$(this).dialog("close");}
			}
		}
	);
	
	$deleteRoleUserAlert = $("<div>确认移除该用户?</div>")
		.dialog({
			autoOpen : false,
			resizable : false,
			modal : true,
			title : "提示框",
			height : 140,
			buttons : {
				"是" : function() {
					$(this).dialog("close");
					$.ajax("/privilege/deleteRoleUserAjax.vhtml".prependcontext(), {
						data: $.param({
							"userCriteria.roleId" : $("#u-role-id").val(),
							"userCriteria.userName" : $("#user-search-name").val(),
							"paginater.pageNumber" : $("#user-paginater-num").val(),
							"userId" : $(this).data("userId") 
						}, true),
						dataType: "html",
						success: function(result) {
							$("#role-related-container").html(result);
							bindUserTableEvents();
						}
					});
				},
				"否" : function() {$(this).dialog("close");}
			}
		}
	);
	
	$("#add-role-modal").on("hidden", function() {
		resetRoleForm();
	});
	
	$("#add-role-modal-btn").click(function() {
		$("#add-role-modal").hideAlerts();
		resetRoleFormValidation();
		var roleName = $("#add-role-name").val().trim();
		if (roleName.isBlank()) {
			setValidateError($("#add-role-name"));
			$("#add-role-name").select();
			return;
		}
		$.ajax("/privilege/createRoleAjax.vhtml".prependcontext(), {
			data: $.param({
				"role.name" : roleName,
				"role.remark" : $("#add-role-remark").val().trim()
			}, true),
			dataType: "json",
			success: function(result) {
				if (result.code == Res_Code_Success) {
					$("#add-role-modal .form-info").showAlert("创建成功，可继续创建.");
					$("#add-role-name").val("");
					reloadRoleListTable();
				} else if (result.code == Res_Code_Error) {
					$("#add-role-modal .form-error").showAlert("创建失败.");
				}
			}
		});
	});
	
	$("#edit-role-modal").on("hidden", function() {
		resetRoleForm();
	});
	
	$("#edit-role-modal-btn").click(function() {
		$("#edit-role-modal").hideAlerts();
		resetRoleFormValidation();
		var roleName = $("#edit-role-name").val().trim();
		if (roleName.isBlank()) {
			setValidateError($("#edit-role-name"));
			$("#edit-role-name").select();
			return;
		}
		$.ajax("/privilege/updateRoleAjax.vhtml".prependcontext(), {
			data: $.param({
				"roleId" : $("#edit-role-id").val(),
				"role.name" : roleName,
				"role.remark" : $("#edit-role-remark").val().trim()
			}, true),
			dataType: "json",
			success: function(result) {
				if (result.code == Res_Code_Success) {
					$("#edit-role-modal").modal("hide");
					reloadRoleListTable();
				} else if (result.code == Res_Code_Error) {
					$("#edit-role-modal .form-error").showAlert(result.msg);
				}
			}
		});
	});
	
	function resetRoleFormValidation() {
		$(".control-group").removeClass("error");
		$(".message").hide();
	}
	
	function resetRoleForm() {
		resetRoleFormValidation();
		$("#add-role-modal,#edit-role-modal").hideAlerts();
		$("#add-role-modal,#edit-role-modal").find("input[type='text'], textarea").val("");
	}
	
	function reloadRoleListTable() {
		$("#role-list-container").load("/privilege/roleListAjax.vhtml".prependcontext(), $.param({
		}, true), function() {
			bindRoleTableEvents();
		});
	}
	
	function bindPrivilegeTableEvents() {
		$(".category-check").click(function() {
			var check_id = $(this).attr("id");
			$(":checkbox[id^=" + check_id + "]").attr("checked", $(this).is(":checked"));
		});
		$(".save-privilege-btn").click(function() {
			var privilegeIds = new Array();
			$(":checkbox[name='privilege']:checked").each(function() {privilegeIds.push($(this).val());});
			$.ajax("/privilege/saveRolePrivilegesAjax.vhtml".prependcontext(), {
				data: $.param({
					"roleId" : $("#priv-role-id").val(),
					"privilegeIds" : privilegeIds
				}, true),
				dataType: "json",
				success: function(result) {
					if (result.code == Res_Code_Success) {
						$commonAlert.html("权限设置成功.").dialog("open");
					} else if (result.code == Res_Code_Error) {
						$commonAlert.html("权限设置失败.").dialog("open");
					}
				}
			});
			return false;
		});
	}
	
	function bindUserTableEvents() {
		$("#user-search").autocomplete("/system/getUsersAjax.vhtml".prependcontext(), {
			minChars : 1,
			width : 180,
			max : 15,
			delay : 300,
			matchContains : false,
			hidden : "user-search-id",
			formatItem: function(data, i, n, value) {
				return data.loginName + " -- " + data.name;
			},
			formatResult : function(data, value) {
				return data.loginName;
			},
			formatHidden : function(data , value) {
				return data.id;
			}
		});
		
		$("#add-role-user").click(function() {
			$.ajax("/privilege/addRoleUserAjax.vhtml".prependcontext(), {
				data: $.param({
					"userCriteria.roleId" : $("#u-role-id").val(),
					"userId" : $("#user-search-id").val()
				}, true),
				dataType: "html",
				success: function(result) {
					$("#role-related-container").html(result);
					bindUserTableEvents();
				}
			});
			return false;
		});
		
		$("#search-role-user").click(function() {
			$("#role-related-container").load("/privilege/roleUserListAjax.vhtml".prependcontext(), $.param({
				"userCriteria.roleId" : $("#u-role-id").val(),
				"userCriteria.userName" : $("#user-search-name").val()
			}, true), function() {
				bindUserTableEvents();
				$(this).addClass("thumbnail");
			});
			return false;
		});
		
		$(".del-role-user").click(function() {
			$deleteRoleUserAlert.dialog("open");
			$deleteRoleUserAlert.data("userId", $(this).parents(".role-user-row").find("[name='role-user-id']").val());
			return false;
		});
	}
	
	function bindRoleTableEvents() {
		$(".role_name").popover();
		$("#add-role-btn").click(function() {
			$("#add-role-modal").modal({
				backdrop : "static", 
				keyboard : false
			});
			return false;
		});
		
		$(".edit-role-btn").click(function() {
			$("#edit-role-id").val(getRoleId($(this)));
			$("#edit-role-name").val(getRoleName($(this)));
			$("#edit-role-remark").val(getRoleRemark($(this)));
			$("#edit-role-modal").modal({
				backdrop : "static", 
				keyboard : false
			});
			return false;
		});
	
		$(".view-privilege-btn").click(function() {
			var roleId = getRoleId($(this));
			var roleName = getRoleName($(this));
			$("#role-related-container").load("/privilege/rolePrivilegeListAjax.vhtml".prependcontext(), $.param({
				"roleId" : roleId
			}, true), function() {
				bindPrivilegeTableEvents();
				$(this).addClass("thumbnail");
				$(this).find("#p_role_name").text(roleName);
			});
			return false;
		});
		
		$(".edit-user-btn").click(function() {
			var roleId = getRoleId($(this));
			$("#role-related-container").load("/privilege/roleUserListAjax.vhtml".prependcontext(), $.param({
				"userCriteria.roleId" : roleId
			}, true), function() {
				bindUserTableEvents();
				$(this).addClass("thumbnail");
			});
			return false;
		});
		
		$(".remove-role-btn").click(function() {
			$deleteRoleAlert.dialog("open");
			$deleteRoleAlert.data("roleId", getRoleId($(this)));
			return false;
		});
	}
	
	function setValidateError($element) {
		$element.parents(".control-group").addClass("error");
		$element.nextAll(".message").show();
	}
	
	function getRoleId($element_in_row) {
		return $element_in_row.parents(".role_row").find("[name='role_id']").val();
	}
	
	function getRoleName($element_in_row) {
		return $element_in_row.parents(".role_row").find(".role_name").text();
	}
	
	function getRoleRemark($element_in_row) {
		return $element_in_row.parents(".role_row").find("[name='role_remark']").val();
	}
	
	jump2Page = function(pageNo) {
		$("#role-related-container").load("/privilege/roleUserListAjax.vhtml".prependcontext(), $.param({
			"userCriteria.roleId" : $("#u-role-id").val(),
			"userCriteria.userName" : $("#user-search-name").val(),
			"paginater.pageNumber" : pageNo
		}, true), function() {
			bindUserTableEvents();
			$(this).addClass("thumbnail");
		});
		event.preventDefault();
	};
	
});