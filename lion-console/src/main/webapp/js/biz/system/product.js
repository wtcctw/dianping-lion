var modalWindow;

$(document).ready(function() {
	bind();
});

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
    	var name, productLeader, teamId;
    	name = document.getElementById('input-product-name').value;
    	productLeader = document.getElementById('input-product-productLeaderName').value;
		if(!contain(productLeader,$("#input-product-productLeaderName").attr("data-source"))){
			$("#span-product-productLeaderName-error").addClass("lion_red");
			$("#span-product-productLeaderName-error").html("负责人必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
			return;
		}else{
			$("#span-product-productLeaderName-error").removeClass("lion_red");
			$("#span-product-productLeaderName-error").html("可输入名字或拼音提示");
		}
    	teamId = document.getElementById('input-product-teamName').value;
    	if (validateConfigForm()) {
	    	var clientdata = {
	//    			envLabel : encodeURI(encodeURI(envLabel)),
	    			name : name,
	    			productLeader : productLeader,
	    			teamId : teamId
	    	};
	//    	clientdata = encodeURIComponent(encodeURIComponent(clientdata));
	    	href = "/system/productAddSubmitAjax.vhtml";
			$.ajax( {
				type : "GET",
				contentType : "application/json;",
	//			contentType: "application/x-www-form-urlencoded; charset=utf-8",
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
	var id, name, productLeader, teamId;
	id = document.getElementById('input-product-id').value;
	name = document.getElementById('input-product-name').value;
	productLeader = document.getElementById('input-product-productLeaderName').value;
	if(!contain(productLeader,$("#input-product-productLeaderName").attr("data-source"))){
		$("#span-product-productLeaderName-error").addClass("lion_red");
		$("#span-product-productLeaderName-error").html("负责人必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
		return;
	}else{
		$("#span-product-productLeaderName-error").removeClass("lion_red");
		$("#span-product-productLeaderName-error").html("可输入名字或拼音提示");
	}
	teamId = document.getElementById('input-product-teamName').value;
	if (validateConfigForm()) {
		var clientdata = {
				id : id,
				name : name,
				productLeader : productLeader,
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

function contain(value,itemsStr){
	var itemsStr_ = itemsStr.substr(2,itemsStr.length-4);
	var items = itemsStr_.split("\",\"");
	for(var i=0;i<items.length;i++){
		if(value == items[i]){
			return true;
		}
	}
	return false;
}