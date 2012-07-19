var Type_String = 5;
var Type_Number = 10;
var Type_Bool = 15;
var Type_List = 20;
var Type_Map = 25;

$(function(){
	$("[rel=tooltip]").tooltip();
	$(".icon-intro").popover();
	
	var $clearAlert = $("<div>确认清除该配置项值? [<font color='green'>不可恢复</font>]</div>")
			.dialog({
				autoOpen : false,
				resizable : false,
				modal : true,
				title : "提示框",
				height : 140,
				buttons : {
					"是" : function() {
						$(location).attr("href", $(this).data("location"));
					},
					"否" : function() {$(this).dialog("close");}
				}
			});
			
	var $deleteAlert = $("<div>确认删除该配置项? [<font color='green'>不可恢复</font>]</div>")
			.dialog({
				autoOpen : false,
				resizable : false,
				modal : true,
				title : "提示框",
				height : 140,
				buttons : {
					"是" : function() {
						$(location).attr("href", $(this).data("location"));
					},
					"否" : function() {$(this).dialog("close");}
				}
			});
	
	$(".clearLink").click(function() {
		$clearAlert.dialog("open");
		$clearAlert.data("location", $(this).attr("href"));
		return false;
	});
	
	$(".deleteLink").click(function() {
		$deleteAlert.dialog("open");
		$deleteAlert.data("location", $(this).attr("href"));
		return false;
	});
	
	$("#add-config-btn").click(function() {
		$("#add-config-modal").modal({
			backdrop : "static"
		});
		return false;
	});
	
	$("#add-config-modal").on("hidden", function() {
		resetConfigForm();
	});
	
	$("#save-btn").click(function() {
		if (validateConfigForm()) {
			var envs = new Array();
			$(":checkbox[name='config-env']:checked").each(function() {envs.push($(this).val());});
			$.ajax("/config/createConfig.vhtml".prependcontext(), {
				data: $.param({
					"config.key" : $("#config-key").val().trim(),
					"config.desc" : $("#config-desc").val().trim(),
					"config.type" : $("#config-type-selector").val(),
					"config.projectId" : $("#projectId").val(),
					"trim" : $("#trim-checkbox").is(":checked"),
					"environments" : envs,
					"value" : $("#config-value").length > 0 ? $("#config-value").val() : $(":radio[name='config-value']:checked").val()
				}, true),
				dataType: "json",
				success: function(result) {
					if (result.code == Res_Code_Success) {
						
					} else if (result.code == Res_Code_Error) {
						
					} else if (result.code == Res_Code_Warn) {
						
					}
				}
			});
		}
		return false;
	});
	
	$("#config-type-selector").change(function() {
		var type = parseInt($(this).val());
		clearValidateError($("#config-value"));
		renderValueComponent(generateValueComponent(type));
	});
	
	$("#ccc").click(function() {
		$("#modal2").modal({
			backdrop : "static"
		});
	});
	
	function renderValueComponent(html) {
		$("#config-value-container").html(html);
	}
	
	function generateValueComponent(type) {
		switch (type) {
			case Type_String : return generateStringComponent();
			case Type_Number : return generateNumberComponent();
			case Type_Bool : return generateBoolComponent();
			case Type_List : return generateListComponent();
			case Type_Map : return generateMapComponent();
		}
	}
	
	function generateStringComponent() {
		return "<textarea id='config-value' rows='7' style='width:350px;'></textarea>";
	}
	
	function generateNumberComponent() {
		return "<input type='text' id='config-value' class='input-medium'>"
			+ "<span class='help-inline hide message'>数字,必填!</span>";
	}
	
	function generateBoolComponent() {
		return "<input type='radio' name='config-value' id='config-value-yes' value='true' checked='checked'"
			+ "><label for='config-value-yes' class='help-inline'>是</label>"
			+ "<input type='radio' name='config-value' id='config-value-no' value='false'"
			+ "><label for='config-value-no' class='help-inline'>否</label>";
	}
	
	function generateListComponent() {
		return "<textarea id='config-value' rows='7' style='width:350px;' readonly='readonly'></textarea>"
			+ "<i class='icon-edit' style='vertical-align:bottom;'></i>";
	}
	
	function generateMapComponent() {
		return "<textarea id='config-value' rows='7' style='width:350px;' readonly='readonly'></textarea>"
			+ "<i class='icon-edit' style='vertical-align:bottom;'></i>";
	}
	
	function validateConfigForm() {
		var checkPass = true;
		resetConfigFormValidation();
		$("#config-key,#config-desc").each(function() {
			if ($(this).val().isBlank()) {
				setValidateError($(this));
				checkPass = false;
			}
		});
		if (parseInt($("#config-type-selector").val()) == Type_Number
				&& !$("#config-value").val().isNumber()) {
			setValidateError($("#config-value"));
			checkPass = false;
		}
		return checkPass;
	}
	
	function setValidateError($element) {
		$element.parents(".control-group").addClass("error");
		$element.next(".message").show();
	}
	
	function clearValidateError($element) {
		$element.parents(".control-group").removeClass("error");
		$element.next(".message").hide();
	}
	
	function resetConfigForm() {
		resetConfigFormInput();
		resetConfigFormValidation();
	}
	
	function resetConfigFormInput() {
		$("#config-type-selector").val($("#config-type-selector option:first").val()).change();
		$("#config-key,#config-desc,#config-value").val("");
		$("#trim-checkbox").attr("checked", true);
		$(":checkbox[name='config-env']:enabled").attr("checked", false);
	}
	
	function resetConfigFormValidation() {
		$(".control-group").removeClass("error");
		$(".message").hide();
	}

});