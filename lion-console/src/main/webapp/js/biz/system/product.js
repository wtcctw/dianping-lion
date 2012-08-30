var modalWindow;

$(document).ready(function() {
	bind();
});

function bindLeaderSelector() {
	$("#input-product-productLeaderName").autocomplete("/system/getUsersAjax.vhtml".prependcontext(), {
		minChars : 1,
		width : 230,
		max : 15,
		delay : 300,
		matchContains : false,
		hidden : "input-product-productLeaderId",
		formatItem: function(data, i, n, value) {
			return data.loginName + " -- " + data.name;
		},
		formatResult : function(data, value) {
			return data.loginName + "/" + data.name;
		},
		formatHidden : function(data , value) {
			return data.id;
		}
	});
}

function bind() {
	$("[rel=tooltip]").tooltip({delay : {show : 800}});
	$('[data-toggle="modal"]').click(function(e) {
		e.preventDefault();
		var href = $(this).attr('href');
		$.ajax( {
			type : "GET",
			contentType : "application/json",
			url : href,
			dataType : 'html',
			success : function(result) {
				var temp = result.replace(/&quot;/g, '\"');
				modalWindow = $(temp);
				modalWindow.on("hidden", function() {
					$(this).remove();
				});
				modalWindow.on("shown", function() {
					bindLeaderSelector();
				});
				modalWindow.modal({
					backdrop : "static"
				});
			}
		});
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
				$(this).dialog("close");
				$.ajax( {
					type : "GET",
					contentType : "application/json;",
					url : $(this).data("location"),
					dataType : 'html',
					success : function(response) {
						var temp = response.replace(/&quot;/g, '\"');
						if(temp.indexOf('table-product-list') != -1) {
							document.getElementById('table-product-list').innerHTML = temp;
							bind();
						} else {
							var $deleteAlert2 = $("<div> [<font color='red'>"+response+"</font>]</div>")
							.dialog({
									autoOpen : false,
									resizable : false,
									modal : true,
									title : "提示框",
									height : 140,
									buttons : {
									"确定" : function() {$(this).dialog("close");}
								}
							});
							$deleteAlert2.dialog("open");
						}
					}
				});
			},
			"否" : function() {$(this).dialog("close");}
		}
	});
	$(".deletelink").click(function() {
		$deleteAlert.dialog("open");
		$deleteAlert.data("location", $(this).attr("href"));
		return false;
	});
}

function saveProduct() {
    	var name, productLeaderId, teamId;
    	name = document.getElementById('input-product-name').value;
    	productLeaderId = $("#input-product-productLeaderId").val();
    	teamId = document.getElementById('input-product-teamName').value;
    	if (validateConfigForm()) {
	    	var clientdata = {
	    			name : name,
	    			productLeaderId : productLeaderId,
	    			teamId : teamId
	    	};
	    	href = "/system/productAddSubmitAjax.vhtml";
		$.ajax( {
			type : "GET",
			contentType : "application/json;",
			url : href.prependcontext(),
			data : clientdata,
			dataType : 'html',
			success : function(response) {
				var temp = response.replace(/&quot;/g, '\"');
				document.getElementById('table-product-list').innerHTML = temp;
				modalWindow.modal('hide');
				bind();
			}
		});
    	}
}

function updateProduct() {
	var id, name, productLeaderId, teamId;
	id = document.getElementById('input-product-id').value;
	name = document.getElementById('input-product-name').value;
	productLeaderId = $("#input-product-productLeaderId").val();
	teamId = document.getElementById('input-product-teamName').value;
	if (validateConfigForm()) {
		var clientdata = {
				id : id,
				name : name,
				productLeaderId : productLeaderId,
				teamId : teamId
		};
		href = "/system/productEditSubmitAjax.vhtml";
		$.ajax( {
			type : "GET",
			contentType : "application/json",
			url : href.prependcontext(),
			data : clientdata,
			dataType : 'html',
			success : function(response) {
				var temp = response.replace(/&quot;/g, '\"');
				document.getElementById('table-product-list').innerHTML = temp;
				modalWindow.modal('hide');
				bind();
			}
		});
	}
}

function validateConfigForm() {
	var checkPass = true;
	resetConfigFormValidation();
	$("#input-product-name").each(function() {
		if ($(this).val().isBlank()) {
			setValidateError($(this),$("#span-product-name-error"),"必填");
			checkPass = false;
		}
	});
	return checkPass;
}

function setValidateError($element,$error,message) {
	$element.parents(".control-group").addClass("error");
	$error.html(message).show();
}

function resetConfigFormValidation() {
	$(".control-group").removeClass("error");
	$(".message").hide();
}

