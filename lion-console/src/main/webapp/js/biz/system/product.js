var modalWindow;

$(document).ready(function() {
	bind();
});

function bind() {
	$('[data-toggle="modal"]').click(function(e) {
		e.preventDefault();
		var href = $(this).attr('href');
		$.ajax( {
			type : "GET",
			contentType : "application/json",
			url : href.prependcontext(),
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
				$(location).attr("href", $(this).data("location"));
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
    	productLeaderId = document.getElementById('input-product-productLeaderName').value;
    	teamId = document.getElementById('input-product-teamName').value;
    	if (validateConfigForm()) {
	    	var clientdata = {
	//    			envLabel : encodeURI(encodeURI(envLabel)),
	    			name : name,
	    			productLeaderId : productLeaderId,
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
	var id, name, productLeaderId, teamId;
	id = document.getElementById('input-product-id').value;
	name = document.getElementById('input-product-name').value;
	productLeaderId = document.getElementById('input-product-productLeaderName').value;
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