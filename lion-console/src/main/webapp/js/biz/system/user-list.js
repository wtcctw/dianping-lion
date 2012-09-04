var jump2Page;

$(function(){
	bindUserTableEvents();
	
	jump2Page = function(pageNo) {
		$("#searchForm [name='paginater.pageNumber']").val(pageNo);
		$("#searchForm").submit();
		event.preventDefault();
	};
	
	$("#edit-user-modal").on("show", function() {
		var user_id = $("#edit-user-modal [name='user-id']").val();
		$.ajax("/system/getUserAjax.vhtml".prependcontext(), {
			data : $.param({
				"id" : user_id
			}, true),
			dataType : "json",
			success : function(result) {
				if (result.code == Res_Code_Success) {
					var user = $.parseJSON(result.result);
					$("[name='edit-user-locked'][value='" + user.locked + "']").attr("checked", true);
					$("[name='edit-user-configview'][value='" + user.onlineConfigView + "']").attr("checked", true);
				} else if (result.code == Res_Code_Error) {
					$("#edit-user-modal .form-error").showAlert(result.msg);
					$("#user-edit-btn").attr("disabled", true);
				}
			}
		});
	});
	
	$("#edit-user-modal").on("hidden", function() {
		$("#edit-user-modal").hideAlerts();
		$("#user-edit-btn").attr("disabled", false);
	});
	
	$("#user-edit-btn").click(function() {
		var user_id = $("#edit-user-modal [name='user-id']").val();
		$.ajax("/system/updateUserAjax.vhtml".prependcontext(), {
			data : $.param({
				"user.id" : user_id,
				"user.locked" : $(":radio[name='edit-user-locked']:checked").val(),
				"user.onlineConfigView" : $(":radio[name='edit-user-configview']:checked").val()
			}, true),
			dataType : "json",
			success : function(result) {
				if (result.code == Res_Code_Success) {
					$("#edit-user-modal").modal("hide");
					reloadUserListTable();
				} else if (result.code == Res_Code_Error) {
					$("#edit-user-modal .form-error").showAlert(result.msg);
				}
			}
		});
	});
	
	function reloadUserListTable() {
		$("#user-list-container").load("/system/userListAjax.vhtml".prependcontext(), $.param({
			"userCriteria.name" : $("#search-name").val(),
			"userCriteria.loginName" : $("#search-loginName").val(),
			"userCriteria.system" : $("#search-system").val(),
			"userCriteria.locked" : $("#search-locked").val(),
			"userCriteria.onlineConfigView" : $("#search-onlineConfigView").val(),
			"paginater.pageNumber" : $("input[name='pageNo']").val()
		}, true), function() {
			bindUserTableEvents();
		});
	}
	
	function bindUserTableEvents() {
		$("[rel=tooltip]").tooltip({delay : {show : 800}});
		$(".edit-user-btn").click(function() {
			$("#edit-user-modal [name='user-id']").val(getUserId($(this)));
			$("#edit-user-modal").modal({
				backdrop : "static", 
				keyboard : false
			});
			return false;
		});
	}
	
	function getUserId($element_in_row) {
		return $element_in_row.parents(".user_row").find("[name='user_id']").val();
	}
});