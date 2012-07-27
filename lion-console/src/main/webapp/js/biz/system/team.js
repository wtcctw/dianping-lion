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
//				$(location).attr("href", $(this).data("location"));
				$(this).dialog("close");
				$.ajax( {
					type : "GET",
					contentType : "application/json;",
					url : $(this).data("location"),
					dataType : 'html',
					success : function(response) {
						var temp = response.replace(/&quot;/g, '\"');
						if(temp.indexOf('table-team-list') != -1) {
							document.getElementById('table-team-list').innerHTML = temp;
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

function saveTeam() {
    	var name;
    	name = document.getElementById('input-team-name').value;
    	if (validateConfigForm()) {
	    	var clientdata = {
	    			name : name
	    	};
	    	href = "/system/teamAddSubmitAjax.vhtml";
			$.ajax( {
				type : "GET",
				contentType : "application/json;",
				url : href.prependcontext(),
				data : clientdata,
				dataType : 'html',
				success : function(response) {
					var temp = response.replace(/&quot;/g, '\"');
					document.getElementById('table-team-list').innerHTML = temp;
					modalWindow.modal('hide');
					/*var mc = document.getElementById("modalcontent");
					mc.parentNode.removeChild(mc);*/
					bind();
				}
			});
    	}
}

function updateTeam() {
	var id, name,ih;
	id = document.getElementById('input-team-id').value;
	name = document.getElementById('input-team-name').value;
	ih = document.getElementById('input-team-name').innerHTML;
	if (validateConfigForm()) {
		var clientdata = {
				id : id,
				name : name
		};
		href = "/system/teamEditSubmitAjax.vhtml";
		$.ajax( {
			type : "GET",
			contentType : "application/json",
			url : href.prependcontext(),
			data : clientdata,
			dataType : 'html',
			success : function(response) {
				var temp = response.replace(/&quot;/g, '\"');
				document.getElementById('table-team-list').innerHTML = temp;
				modalWindow.modal('hide');
				bind();
			}
		});
	}
}

function validateConfigForm() {
	var checkPass = true;
	resetConfigFormValidation();
	$("#input-team-name").each(function() {
		if ($(this).val().isBlank()) {
			setValidateError($(this),$("#span-team-name-error"),"必填");
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