var modal_service_edited = false;
var $deleteAlert;
var $commonAlert;

$(function(){
	$(".icon-intro").popover();
	bindServiceTableEvents();

	$deleteAlert = $("<div>确认删除服务? [<font color='green'>不可恢复</font>]</div>")
		.dialog({
			autoOpen : false,
			resizable : false,
			modal : true,
			title : "删除服务",
			height : 140,
			buttons : {
				"是" : function() {
					$(this).dialog("close");
					$.ajax("/service/deleteServiceAjax.vhtml".prependcontext(), {
						data: $.param({
							"serviceId" : $(this).data("serviceId")
						}, true),
						dataType: "json",
						success: function(result) {
							if (result.code == Res_Code_Success) {
								reloadServiceListTable();
							} else if (result.code == Res_Code_Error) {
								$commonAlert.html(result.msg).dialog("open");
							}
						}
					});
				},
				"否" : function() {$(this).dialog("close");}
			}
		}
	);

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

	$("#edit-service-modal").on("show", function() {
		modal_service_edited = false;
	});

	$("#edit-service-modal").on("hidden", function() {
		if (modal_service_edited) {
			reloadServiceListTable();
		}
	});

	$("#service-save-btn").click(function() {
		if (validateEditServiceForm()) {
			$.ajax("/service/saveServiceAjax.vhtml".prependcontext(), {
				data : $.param({
					"envId" : $("#edit-service-modal [name='env-id']").val(),
					"pid" : $("#edit-service-modal [name='project-id']").val(),
					"serviceId" : $("#edit-service-modal [name='service-id']").val(),
					"serviceName" : $("#edit-service-name").val(),
					"serviceDesc" : $("#edit-service-desc").val(),
					"serviceGroup" : $("#edit-service-group").val(),
					"serviceHosts" : $("#edit-service-hosts").val()
				}, true),
				dataType: "json",
				success : function(result) {
					$("#edit-service-modal").hideAlerts();
					if (result.code == Res_Code_Success) {
						modal_service_edited = true;
						$("#edit-config-modal .form-info").flashAlert("保存服务成功。", 1500);
					} else if (result.code == Res_Code_Error) {
						$("#edit-config-modal .form-error").showAlert(result.msg);
					} else if (result.code == Res_Code_Warn) {
						modal_service_edited = true;
						$("#edit-config-modal .form-warn").showAlert(result.msg);
					}
					$('#edit-service-modal').modal('hide');
				}
			});
		}
	});

	function reloadServiceListTable() {
		$("#service-list-container").load("/service/getServiceListAjax.vhtml".prependcontext(), $.param({
			"pid" : $("[name='pid']").val(),
			"envId" : $("[name='envId']").val()
		}, true), function() {
			bindServiceTableEvents();
		});
	}

	function validateEditServiceForm() {
		var checkPass = true;
		resetServiceFormValidation();

		var serviceName = $("#edit-service-name").val().trim();
		if (serviceName.length == 0) {
			setValidateError($("#edit-service-name"));
			checkPass = false;
		}
		var serviceHosts = $("#edit-service-hosts").val().trim();
		if (serviceHosts.length == 0) {
			setValidateError($("#edit-service-hosts"));
			checkPass = false;
		}
		return checkPass;
	}

	function setValidateError($element) {
		$element.parents(".control-group").addClass("error");
		$element.nextAll(".message").show();
	}

	function clearValidateError($element) {
		$element.parents(".control-group").removeClass("error");
		$element.nextAll(".message").hide();
	}

	function resetEditServiceForm() {
		$("#edit-service-modal").hideAlerts();
		resetServiceFormValidation();
		$("#service-save-btn").attr("disabled", false);
	}

	function resetServiceFormValidation() {
		$(".control-group").removeClass("error");
		$(".message").hide();
	}

	function getServiceId($element_in_row) {
		return $element_in_row.parents(".config_row").find("[name='service-id']").val();
	}

	function getServiceName($element_in_row) {
		return $element_in_row.parents(".config_row").find(".service-name").text().trim();
	}

	function getServiceDesc($element_in_row) {
		return $element_in_row.parents(".config_row").find(".service_desc").text().trim();
	}

	function getServiceGroup($element_in_row) {
		return $element_in_row.parents(".config_row").find(".service-group").text().trim();
	}

	function getServiceHosts($element_in_row) {
		return $element_in_row.parents(".config_row").find(".service-hosts").text().trim();
	}

	function bindServiceTableEvents() {
		$("[rel=tooltip]").tooltip({delay : {show : 800}});

		$(".delete-service-btn").click(function() {
			$deleteAlert.dialog("open");
			$deleteAlert.data("serviceId", getServiceId($(this)));
			return false;
		});

		$("#create-service-btn").click(function() {
			resetEditServiceForm();
			$("#edit-service-title").text("新建服务");
			$("#edit-service-modal [name='project-id']").val($("#projectId").val());
			$("#edit-service-modal [name='env-id']").val($("#envId").val());
			$("#edit-service-modal [name='service-id']").val(-1);
			$("#edit-service-name").val("");
			$("#edit-service-desc").val("");
			$("#edit-service-group").val("");
			$("#edit-service-hosts").val("");
			$("#edit-service-name").removeAttr('readonly');
			$("#edit-service-group").removeAttr('readonly');
			$("#edit-service-modal").modal({
				backdrop : "static",
				keyboard : false
			});
			return false;
		});

		$(".update-service-btn").click(function() {
			resetEditServiceForm();
			$("#edit-service-title").text("编辑服务");
			$("#edit-service-modal [name='project-id']").val($("#projectId").val());
			$("#edit-service-modal [name='env-id']").val($("#envId").val());
			$("#edit-service-modal [name='service-id']").val(getServiceId($(this)));
			$("#edit-service-name").val(getServiceName($(this)));
			$("#edit-service-desc").val(getServiceDesc($(this)));
			$("#edit-service-group").val(getServiceGroup($(this)));
			$("#edit-service-hosts").val(getServiceHosts($(this)));
			$("#edit-service-name").attr('readonly','readonly');
			$("#edit-service-group").attr('readonly','readonly');
			$("#edit-service-modal").modal({
				backdrop : "static",
				keyboard : false
			});
			return false;
		});
	}

});




