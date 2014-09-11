var Type_String = 10;
var Type_Number = 20;
var Type_Bool = 30;
var Type_List_Str = 40;
var Type_List_Num = 45;
var Type_Map = 50;
var Type_Ref_Shared = 60;
var Type_Ref_DB = 70;

/**是否在新打开的模态窗口中创建过新的config*/
var modal_config_created = false;
var modal_config_edited = false;
var display_all_btn = false;
var $config_list_editor;
var $config_map_editor;
var $clearAlert;
var $deleteAlert;
var $commonAlert;

var projectName;
var jump2Page;

$(function(){
	projectName = $("#projectName").val();
	$(".icon-intro").popover();
	bindConfigTableEvents();
	$config_list_editor = $("#list-editor").listeditor();
	$config_map_editor = $("#map-editor").mapeditor();

	$clearAlert = $("<div>确认清除该配置项值? [<font color='green'>不可恢复</font>]</div>")
		.dialog({
			autoOpen : false,
			resizable : false,
			modal : true,
			title : "提示框",
			height : 140,
			buttons : {
				"是" : function() {
					$(this).dialog("close");
					$.ajax("/config/clearInstanceAjax.vhtml".prependcontext(), {
						data: $.param({
							"configId" : $(this).data("configId"),
							"envId" : $("#envId").val()
						}, true),
						dataType: "json",
						success: function(result) {
							if (result.code == Res_Code_Success) {
								reloadConfigListTable();
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

	$deleteAlert = $("<div>确认删除该配置项? [<font color='green'>不可恢复</font>]<br/>将清除所有环境的配置值.</div>")
		.dialog({
			autoOpen : false,
			resizable : false,
			modal : true,
			title : "提示框",
			height : 140,
			buttons : {
				"是" : function() {
					$(this).dialog("close");
					$.ajax("/config/deleteAjax.vhtml".prependcontext(), {
						data: $.param({
							"configId" : $(this).data("configId")
						}, true),
						dataType: "json",
						success: function(result) {
							if (result.code == Res_Code_Success) {
								reloadConfigListTable();
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

	$("#add-config-modal").on("hidden", function() {
		resetConfigForm();
		if (modal_config_created) {
			reloadConfigListTable();
		}
	});

	$("#add-config-modal").on("show", function() {
		modal_config_created = false;
		resetConfigForm();	//fix firefox bug: 选中下拉项，刷新页面，再打开下拉项仍是刚才的选中项
	});

	$("#edit-config-modal").on("show", function() {
		modal_config_edited = false;
		var config_id = $("#edit-config-modal [name='config-id']").val();
		var env_id = $("[name='envId']").val();
		$.ajax("/config/defaultValueAjax.vhtml".prependcontext(), {
			data : $.param({
				"configId" : config_id,
				"envId" : env_id
			}, true),
			dataType : "json",
			success : function(result) {
				$("#edit-config-modal").hideAlerts();
				if (result.code == Res_Code_Success) {
					for (var envId in result.privilege) {
						$("#edit-config-env-" + envId).attr("disabled", !result.privilege[envId]);
					}
					$("[name='edit-config-env'][value='" + $("[name='envId']").val() + "']:enabled").attr("checked", true);
					var config_type = parseInt($("#edit-config-type-selector").val());
					if (config_type == Type_Bool) {
						$("[name='edit-config-value'][value='" + result.value + "']").attr("checked", true);
					} else {
						$("#edit-config-value").val(result.value);
					}
					if (!result.msg.isBlank()) {
						$("#edit-config-modal .form-info").showAlert(result.msg);
					}
				} else if (result.code == Res_Code_Error) {
					$("#edit-config-modal .form-error").showAlert(result.msg);
					$("#edit-save-btn,#edit-more-btn").attr("disabled", true);
				}
			}
		});
	});

	$("#edit-config-modal").on("hidden", function() {
		if (modal_config_edited) {
			reloadConfigListTable();
		}
	});

	$("#edit-config-attr-modal").on("show", function() {
		var config_id = $("#edit-config-attr-modal [name='config-id']").val();
		$.ajax("/config/loadConfigAjax.vhtml".prependcontext(), {
			data : $.param({
				"configId" : config_id
			}, true),
			dataType : "json",
			success : function(result) {
				$("#edit-config-attr-modal").hideAlerts();
				if (result.code == Res_Code_Success) {
					var config = $.parseJSON(result.result);
					if (config != null) {
						$("[name='config-public'][value='" + !config.privatee + "']").attr("checked", true);
						$("#attr-config-desc").val(config.desc);
					} else {
						loadConfigAttrFailed("配置不存在.");
					}
				} else {
					loadConfigAttrFailed("加载数据失败.");
				}
			},
			error : function() {loadConfigAttrFailed("加载数据失败.");}
		});
	});

	$("#edit-config-attr-modal").on("hidden", function() {
		$("#edit-config-attr-modal").hideAlerts();
	});

	$("#refshared-editor").on("show", function() {
		$("[name='ref-config-key']").val("");
		loadSelectableConfigs(dsProjectId, 0, $(this));
	});

	$("#refdb-editor").on("show", function() {
		$("[name='ref-config-key']").val("");
		loadSelectableConfigs(dsProjectId, 0, $(this));
	});

	$("#ref-list-modal").on("show", function() {

	});

	function loadSelectableConfigs(projectId, pageNo, $container) {
		$.ajax("/config/configList2Ajax.vhtml".prependcontext(), {
			data : $.param({
				"criteria.projectId" : projectId,
				"criteria.key" : $container.find("[name='ref-config-key']").val().trim(),
				"paginater.pageNumber" : pageNo
			}, true),
			dataType: "html",
			success : function(result) {
				$container.find(".modal-body").html(result);
				$container.find(".modal-body .config_row").click(function() {
					$(this).find("[name='configkey']").attr("checked", true);
				});
			}
		});
	}

	jump2Page = function(pageNo, this_) {
		var elements = $(this_).parents("#refdb-editor");
		if (elements.length > 0) {
			loadSelectableConfigs(dsProjectId, pageNo, $("#refdb-editor"));
			return;
		}
		elements = $(this_).parents("#refshared-editor");
		if (elements.length > 0) {
			loadSelectableConfigs(dsProjectId, pageNo, $("#refshared-editor"));
			return;
		}
		$("#configForm [name='paginater.pageNumber']").val(pageNo);
		$("#configForm").submit();
		event.preventDefault();
	};

	function loadConfigAttrFailed(error) {
		$("#edit-config-attr-modal .form-error").showAlert(error);
		$("#attr-save-btn").attr("disabled", true);
	}

	$("#edit-save-btn").click(function() {
		if (validateEditConfigForm()) {
			var envs = new Array();
			$(":checkbox[name='edit-config-env']:checked").each(function() {envs.push($(this).val());});
			if (envs.length > 0) {
				$.ajax("/config/saveDefaultValueAjax.vhtml".prependcontext(), {
					type : "POST",
					data : $.param({
						"configId" : $("#edit-config-modal [name='config-id']").val(),
						"envIds" : envs,
						"trim" : $("#edit-trim-checkbox").is(":checked"),
						"value" : $("#edit-config-value").length > 0 ? $("#edit-config-value").val() : $(":radio[name='edit-config-value']:checked").val()
					}, true),
					dataType: "json",
					success : function(result) {
						$("#edit-config-modal").hideAlerts();
						if (result.code == Res_Code_Success) {
							modal_config_edited = true;
							$("#edit-config-modal .form-info").flashAlert("设置配置项值成功.", 1500);
						} else if (result.code == Res_Code_Error) {
							$("#edit-config-modal .form-error").showAlert(result.msg);
						} else if (result.code == Res_Code_Warn) {
							modal_config_edited = true;
							$("#edit-config-modal .form-warn").showAlert(result.msg);
						}
					}
				});
			}
		}
	});

	$("#edit-more-btn").click(function() {
		$(location).attr("href", ("/config/editMore.vhtml?" + $("#queryStr").val() + "&" + $("#criteriaStr").val()).prependcontext()
			+ "&configId=" + $("#edit-config-modal [name='config-id']").val());
	});

	$("#save-btn").click(function() {
		if (validateConfigForm()) {
			var envs = new Array();
			$(":checkbox[name='config-env']:checked").each(function() {envs.push($(this).val());});
			$.ajax("/config/createConfigAjax.vhtml".prependcontext(), {
				data: $.param({
					"config.key" : $("#config-key").val().trim(),
					"config.desc" : $("#config-desc").val().trim(),
					"config.type" : $("#config-type-selector").val(),
					"config.projectId" : $("#projectId").val(),
					"trim" : $("#trim-checkbox").is(":checked"),
					"envIds" : envs,
					"value" : $("#config-value").length > 0 ? $("#config-value").val() : $(":radio[name='config-value']:checked").val()
				}, true),
				dataType: "json",
				success: function(result) {
					if (result.code == Res_Code_Success) {
						modal_config_created = true;
						resetConfigForm(["config-env"]);
						$("#add-config-modal .form-info").flashAlert("创建成功，请继续添加.", 4000);
					} else if (result.code == Res_Code_Error) {
						resetConfigAlerts();
						$("#add-config-modal .form-error").showAlert(result.msg);
					} else if (result.code == Res_Code_Warn) {
						modal_config_created = true;
						resetConfigForm(["config-env"]);
						$("#add-config-modal .form-warn").showAlert(result.msg);
					}
				}
			});
		}
	});

	$("#attr-save-btn").click(function() {
		var config_id = $("#edit-config-attr-modal [name='config-id']").val();
		$.ajax("/config/editConfigAttrAjax.vhtml".prependcontext(), {
			data : $.param({
				"configId" : config_id,
				"config.desc" : $("#attr-config-desc").val(),
				"configAttr.public" : $(":radio[name='config-public']:checked").val()
			}, true),
			dataType: "json",
			success : function(result) {
				$("#edit-config-attr-modal").hideAlerts();
				if (result.code == Res_Code_Success) {
					$("#edit-config-attr-modal").modal("hide");
					reloadConfigListTable();
				} else {
					$("#edit-config-attr-modal .form-error").showAlert("保存失败.");
				}
			}
		});
	});

	$("#refshared-ok-btn,#refdb-ok-btn").click(function() {
		var $modal = $(this).parents(".modal");
		var configKey = $modal.find("[name='configkey']:checked").val();
        if (configKey != void 0) {
            var backfill = $modal.data("backfill");
            if ($(this).attr("id") == "refdb-ok-btn") {
                $("#" + backfill).val("$ref{" + configKey + "?i=15&m=5&M=30}");
            } else {
                $("#" + backfill).val("${" + configKey + "}");
            }
            $modal.modal("hide");
        }
		return false;
	});

	$("#refshared-search").click(function() {
		var $modal = $(this).parents(".modal");
		loadSelectableConfigs(dsProjectId, 0, $modal);
		return false;
	});

	$("#refdb-search").click(function() {
		var $modal = $(this).parents(".modal");
		loadSelectableConfigs(dsProjectId, 0, $modal);
		return false;
	});

	$("#refshared-clear-btn,#refdb-clear-btn").click(function() {
		var $modal = $(this).parents(".modal");
		var backfill = $modal.data("backfill");
		$("#" + backfill).val("");
		$modal.modal("hide");
		return false;
	});

    $("#submit-button").click(function() {
        $("#configForm [name='paginater.pageNumber']").val(1);
    });

	function reloadConfigListTable() {
		$("#config-list-container").load("/config/configListAjax.vhtml".prependcontext(), $.param({
			"pid" : $("[name='pid']").val(),
			"envId" : $("[name='envId']").val(),
			"criteria.key" : $("#key").val(),
			"criteria.value" : $("#value").val(),
			"criteria.status" : $("#status").val(),
			"paginater.pageNumber" : $("#configForm [name='paginater.pageNumber']").val()
		}, true), function() {
			bindConfigTableEvents();
			$("#display-all-btn").attr("checked", display_all_btn).triggerHandler("click");
		});
	}

	$("#select-all-env").click(function() {
		$(":checkbox[name='config-env'][online='false']:enabled").attr("checked", $(this).is(":checked"));
	});

	$("#edit-select-all-env").click(function() {
		$(":checkbox[name='edit-config-env'][online='false']:enabled").attr("checked", $(this).is(":checked"));
	});

	$("#config-type-selector").change(function() {
		var type = parseInt($(this).val());
		clearValidateError($("#config-value"));
		$("#config-value-container").html(generateValueComponent(type, "config-value"));
	});

	$("#edit-config-type-selector").change(function() {
		var type = parseInt($(this).val());
		clearValidateError($("#edit-config-value"));
		$("#edit-config-value-container").html(generateValueComponent(type, "edit-config-value"));
	});

    $("#submit-button").click(function() {
        $("#configForm [name='paginater.pageNumber']").val(1);
    })

	function generateValueComponent(type, inputId) {
		switch (type) {
			case Type_String : return generateStringComponent(inputId);
			case Type_Number : return generateNumberComponent(inputId);
			case Type_Bool : return generateBoolComponent(inputId);
			case Type_List_Num : return generateListComponent(inputId, true);
			case Type_List_Str : return generateListComponent(inputId, false);
			case Type_Map : return generateMapComponent(inputId);
			case Type_Ref_Shared : return generateRefSimpleComponent(inputId);
			case Type_Ref_DB : return generateRefDBComponent(inputId);
		}
	}

	function generateStringComponent(inputId) {
		return "<textarea id='" + inputId + "' rows='7' style='width:350px;'></textarea>";
	}

	function generateNumberComponent(inputId) {
		return "<input type='text' id='" + inputId + "' class='input-medium'>"
			+ "<span class='help-inline hide message'>数字,必填!</span>";
	}

	function generateBoolComponent(inputId) {
		return "<input type='radio' name='" + inputId + "' id='" + inputId + "-yes' value='true' checked='checked'"
			+ "><label for='" + inputId + "-yes' class='help-inline'>true</label>"
			+ "<input type='radio' name='" + inputId + "' id='" + inputId + "-no' value='false'"
			+ "><label for='" + inputId + "-no' class='help-inline'>false</label>";
	}

	function generateListComponent(inputId, numberlist) {
		return "<textarea id='" + inputId + "' rows='7' style='width:350px;'></textarea>"
			+ "<a href='#' onclick='openListEditor(\"" + inputId + "\", " + numberlist + ", event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>"
			+ "<br/><span class='help-inline hide message'>非法的数据格式, 例: " + (numberlist ? "[\"3\", \"5.5\", \"7\"]" : "[\"hello\", \"world\"]") + "!</span>";
	}

	function generateMapComponent(inputId) {
		return "<textarea id='" + inputId + "' rows='7' style='width:350px;'></textarea>"
			+ "<a href='#' onclick='openMapEditor(\"" + inputId + "\", event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>"
			+ "<br/><span class='help-inline hide message'>非法的数据格式, 例: {\"url\":\"xx\", \"con\":{\"min\":\"3\",\"max\":\"6\"}}</span>";
	}

	function generateRefSimpleComponent(inputId) {
		return "<input type='text' id='" + inputId + "' style='width:350px;' readonly='readonly'>"
			+ "<a href='#' onclick='openSharedSelector(\"" + inputId + "\", event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>"
			+ "<span class='help-inline hide message'>必选!</span>";
	}

	function generateRefDBComponent(inputId) {
		return "<input type='text' id='" + inputId + "' style='width:350px;' readonly='readonly'>"
			+ "<a href='#' onclick='openDBSelector(\"" + inputId + "\", event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>"
			+ "<span class='help-inline hide message'>必选!</span><br/>"
			+ "连接数(初始/最小/最大，默认15/5/30): <br/>"
			+ "<input type='radio' id='cs1' name='db-conn' value='1/1/5' onclick='changeDbParam(\"" + inputId + "\", this);' style='margin-left:0px;margin-top:0px;'>"
			+ "<label class='help-inline' for='cs1'>1/1/5</label>"
			+ "<input type='radio' id='cs2' name='db-conn' value='5/3/15' onclick='changeDbParam(\"" + inputId + "\", this);' style='margin-left:10px;margin-top:0px;'>"
			+ "<label class='help-inline' for='cs2'>5/3/15</label>"
			+ "<input type='radio' id='cs3' name='db-conn' value='15/5/30' onclick='changeDbParam(\"" + inputId + "\", this);' style='margin-left:10px;margin-top:0px;'>"
			+ "<label class='help-inline' for='cs3'>15/5/30</label>"
			+ "<input type='radio' id='cs4' name='db-conn' value='15/10/50' onclick='changeDbParam(\"" + inputId + "\", this);' style='margin-left:10px;margin-top:0px;'>"
			+ "<label class='help-inline' for='cs4'>15/10/50</label>";
	}

	function validateConfigForm() {
		var checkPass = true;
		resetConfigFormValidation();
		if (!$("#config-key").val().startsWith(projectName + ".")) {
			setValidateError($("#config-key"));
			checkPass = false;
		}
		$("#config-key,#config-desc").each(function() {
			if ($(this).val().isBlank()) {
				setValidateError($(this));
				checkPass = false;
			}
		});
		var configType = parseInt($("#config-type-selector").val());
		var configVal = null;
		try {configVal = $("#config-value").val().trim()} catch(err) {};
		if (configType == Type_Number && !configVal.isNumber()) {
			setValidateError($("#config-value"));
			checkPass = false;
		} else if (configType == Type_List_Str) {
			if (!$.isJSonStrList(configVal)) {
				setValidateError($("#config-value"));
				checkPass = false;
			}
		} else if (configType == Type_List_Num) {
			if (!$.isJSonNumList(configVal)) {
				setValidateError($("#config-value"));
				checkPass = false;
			}
		} else if (configType == Type_Map) {
			if (!$.isJSonObj(configVal)) {
				setValidateError($("#config-value"));
				checkPass = false;
			}
		} else if (configType == Type_Ref_Shared || configType == Type_Ref_DB) {
			if (configVal.isBlank()) {
				setValidateError($("#config-value"));
				checkPass = false;
			}
		}
		var envselected = false;
		$(":checkbox[name='config-env']").each(function() {
			if ($(this).is(":checked")) {
				envselected = true;
				return false;
			}
		});
		if (!envselected) {
			setValidateError($(":checkbox[name='config-env']"));
			checkPass = false;
		}
		return checkPass;
	}

	function validateEditConfigForm() {
		var checkPass = true;
		resetConfigFormValidation();
		var configType = parseInt($("#edit-config-type-selector").val());
		var configVal = null;
		try {configVal = $("#edit-config-value").val().trim()} catch(err) {};
		if (configType == Type_Number && !configVal.isNumber()) {
			setValidateError($("#edit-config-value"));
			checkPass = false;
		} else if (configType == Type_List_Str) {
			if (!$.isJSonStrList(configVal)) {
				setValidateError($("#edit-config-value"));
				checkPass = false;
			}
		} else if (configType == Type_List_Num) {
			if (!$.isJSonNumList(configVal)) {
				setValidateError($("#edit-config-value"));
				checkPass = false;
			}
		} else if (configType == Type_Map) {
			if (!$.isJSonObj(configVal)) {
				setValidateError($("#edit-config-value"));
				checkPass = false;
			}
		} else if (configType == Type_Ref_Shared || configType == Type_Ref_DB) {
			if (configVal.isBlank()) {
				setValidateError($("#config-value"));
				checkPass = false;
			}
		}
		var envselected = false;
		$(":checkbox[name='edit-config-env']").each(function() {
			if ($(this).is(":checked")) {
				envselected = true;
				return false;
			}
		});
		if (!envselected) {
			setValidateError($(":checkbox[name='edit-config-env']"));
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

	function resetConfigForm(excepts) {
		resetConfigFormInput(excepts);
		resetConfigFormValidation();
		resetConfigAlerts();
	}

	function resetEditConfigForm() {
		$("#edit-config-modal").hideAlerts();
		$("#edit-trim-checkbox").attr("checked", true);
		resetConfigFormValidation();
		$("#edit-select-all-env,[name='edit-config-env']").attr("checked", false);
		$("#edit-save-btn,#edit-more-btn").attr("disabled", false);
	}

	function resetConfigAlerts() {
		$("#add-config-modal").hideAlerts();
	}

	function resetConfigFormInput(excepts) {
		var excepts_ = typeof excepts != "undefined" ? excepts : [];
		$("#config-type-selector").val($("#config-type-selector option:first").val()).change();
		$("#config-key").val($("#projectName").val() + ".");
		$("#config-desc,#config-value").val("");
		$("#trim-checkbox").attr("checked", true);
		if (!excepts_.contains("config-env")) {
			$(":checkbox[name='config-env']:enabled").attr("checked", false);
			$("#select-all-env").attr("checked", false);
		}
	}

	function resetConfigFormValidation() {
		$(".control-group").removeClass("error");
		$(".message").hide();
	}

	function getConfigKey($element_in_row) {
		return $element_in_row.parents(".config_row").find("[name='config_key']").val();
	}

	function getConfigType($element_in_row) {
		return $element_in_row.parents(".config_row").find("[name='config_type']").val();
	}

	function getConfigId($element_in_row) {
		return $element_in_row.parents(".config_row").find("[name='config_id']").val();
	}

	function bindConfigTableEvents() {
		$("[rel=tooltip]").tooltip({delay : {show : 800}});

		$("#display-all-btn").click(function() {
			display_all_btn = $(this).is(":checked");
			if (display_all_btn) {
				$(".config-btn-group .optional").removeClass("hide");
			} else {
				$(".config-btn-group .optional").addClass("hide");
			}
		});

		$(".clear-config-btn").click(function() {
			$clearAlert.dialog("open");
			$clearAlert.data("configId", getConfigId($(this)));
			return false;
		});

		$(".remove-config-btn").click(function() {
			$deleteAlert.dialog("open");
			$deleteAlert.data("configId", getConfigId($(this)));
			return false;
		});

		$(".test-connection-btn").click(function() {
			$.ajax("/config/testConnectionAjax.vhtml".prependcontext(), {
				data : $.param({
					"configId" : getConfigId($(this)),
					"envId" : $("#envId").val()
				}, true),
				dataType : "json",
				success : function(result) {
					$commonAlert.html(result.msg).dialog("open");
				}
			});
			return false;
		});
		
		$(".decode-password-btn").click(function() {
			$.ajax("/config/decodePasswordAjax.vhtml".prependcontext(), {
				data : $.param({
					"configId" : getConfigId($(this)),
					"envId" : $("#envId").val()
				}, true),
				dataType : "json",
				success : function(result) {
					$commonAlert.html(result.msg).dialog("open");
				}
			});
			return false;
		});
		
		$(".moveup-config-btn").click(function() {
			$.ajax("/config/moveUpConfigAjax.vhtml".prependcontext(), {
				data : $.param({
					"projectId" : $("#projectId").val(),
					"configId" : getConfigId($(this))
				}, true),
				dataType : "json",
				success : function(result) {
					if (result.code == Res_Code_Success) {
						reloadConfigListTable();
					}
				}
			});
			return false;
		});

		$(".movedown-config-btn").click(function() {
			$.ajax("/config/moveDownConfigAjax.vhtml".prependcontext(), {
				data : $.param({
					"projectId" : $("#projectId").val(),
					"configId" : getConfigId($(this))
				}, true),
				dataType : "json",
				success : function(result) {
					if (result.code == Res_Code_Success) {
						reloadConfigListTable();
					}
				}
			});
			return false;
		});

		$("#add-config-btn").click(function() {
			$("#add-config-modal").modal({
				backdrop : "static",
				keyboard : false
			});
			return false;
		});

		$(".edit-config-btn").click(function() {
			resetEditConfigForm();
			$("#edit-config-modal [name='config-id']").val(getConfigId($(this)));
			$("#edit-config-modal .config-key").text(getConfigKey($(this)));
			var config_type = getConfigType($(this));
			$("#edit-config-type-selector").val(config_type);
			$("#edit-config-value-container").html(generateValueComponent(parseInt(config_type), "edit-config-value"));
			$("#edit-config-modal").modal({
				backdrop : "static",
				keyboard : false
			});
			return false;
		});

		$(".edit-config-attr").click(function() {
			$("#edit-config-attr-modal [name='config-id']").val(getConfigId($(this)));
			$("#edit-config-attr-modal").modal({
				backdrop : "static",
				keyboard : false
			});
			return false;
		});

		$(".view-ref-link").click(function() {
			var configKey = getConfigKey($(this));
			$modal = $("#ref-list-modal");
			$.ajax("/config/configRefListAjax.vhtml".prependcontext(), {
				data : $.param({
					"config.key" : configKey
				}, true),
				dataType: "html",
				success : function(result) {
					$modal.find(".modal-body").html(result);
				}
			});
			$modal.modal({
				backdrop : "static"
			});
			return false;
		});
	}

});

function openMapEditor(inputId, event) {
	$config_map_editor.show({
		value : $("#" + inputId).val(),
		ok : function($editor) {
			var checkPass = true;
			var $errorInput = null;
			var trimInput = $editor.find(".trim_check").is(":checked");
			var map_result = "";
			if ($editor.find(".map-item").length > 0) {
				var map_obj = new Object();
				$editor.find(".map-item").each(function() {
					var key = $(this).find(".map-key-input").val();
					var value = $(this).find(".map-value-input").val();
					map_obj[key.trimIf(trimInput)] = value.trimIf(trimInput);
				});
				map_result = JSON.stringify(map_obj);
			}
			$("#" + inputId).val(map_result);
			$editor.modal("hide");
		}
	});
	event.preventDefault();
}

function openListEditor(inputId, numberlist, event) {
	$config_list_editor.show({
		value : $("#" + inputId).val(),
		title : (numberlist ? "List<Number>": "List<String>") + "编辑器",
		ok : function($editor) {
			var checkPass = true;
			var $errorInput = null;
			var trimInput = $editor.find(".trim_check").is(":checked");
			if (numberlist) {
				$editor.find(".list-item-input").each(function() {
					if (!$(this).val().trimIf(trimInput).isNumber()) {
						$(this).parent().addClass("error");
						checkPass = false;
						if ($errorInput == null) $errorInput = $(this);
					} else {
						$(this).parent().removeClass("error");
					}
				});
			}
			if (checkPass) {
				//回填
				var list_result = "";
				if ($editor.find(".list-item-input").length > 0) {
					var array = new Array();
					$editor.find(".list-item-input").each(function() {
						array.push($(this).val().trimIf(trimInput));
					});
					list_result = JSON.stringify(array);
				}
				$("#" + inputId).val(list_result);
				$editor.modal("hide");
			} else {
				$errorInput.select();
				$editor.find(".form-error").flashAlert("数字，必填!");
			}
		}
	});
	event.preventDefault();
}

function openSharedSelector(inputId, event) {
	$("#refshared-editor").data("backfill", inputId);
	$("#refshared-editor").modal({backdrop : "static", keyboard : false});
	event.preventDefault();
}

function openDBSelector(inputId, event) {
	$("#refdb-editor").data("backfill", inputId);
	$("#refdb-editor").modal({backdrop : "static", keyboard : false});
	event.preventDefault();
}

function changeDbParam(inputId, radio) {
	var refexpr = $("#" + inputId).val();
	if (!refexpr.isBlank()) {
		var paramstr = $(radio).val();
		var newRefExpr = refexpr.replaceAll("\\?.*}", "?" + generateDbParam(paramstr) + "}");
		$("#" + inputId).val(newRefExpr);
	}
}

function generateDbParam(paramstr) {
	var frags = paramstr.split("/");
	return "i=" + frags[0] + "&m=" + frags[1] + "&M=" + frags[2];
}





