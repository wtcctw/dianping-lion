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

function saveJob() {
    	var jobName, switcher, failMail;
    	jobName = document.getElementById('input-job-name').value;
//    	switcher = document.getElementsByName('input-job-switcher').value;
    	switcher = $("input[name='input-job-switcher'][checked]").val();
    	failMail = document.getElementById('input-job-failMail').value;
    	if (validateConfigForm()) {
	    	var clientdata = {
	    			jobName : jobName,
	//    			envLabel : encodeURI(encodeURI(envLabel)),
	    			switcher : switcher,
	    			failMail : failMail
	    	};
	//    	clientdata = encodeURIComponent(encodeURIComponent(clientdata));
	    	href = "/system/addJobSubmitAjax.vhtml";
			$.ajax( {
				type : "GET",
				contentType : "application/json;",
	//			contentType: "application/x-www-form-urlencoded; charset=utf-8",
				url : href.prependcontext(),
				data : clientdata,
				dataType : 'html',
				success : function(response) {
					var temp = response.replace(/&quot;/g, '\"');
					document.getElementById('table-job-list').innerHTML = temp;
					modalWindow.modal('hide');
					bind();
				}
			});
    	}
}

function updateJob() {
	var id, jobName, switcher, failMail;
	id = document.getElementById('input-job-id').value;
	jobName = document.getElementById('input-job-name').value;
//	switcher = document.getElementsByName('input-job-switcher').value;
	switcher = $("input[name='input-job-switcher'][checked]").val();
	failMail = document.getElementById('input-job-failMail').value;
	if (validateConfigForm()) {
		var clientdata = {
				jobId : id,
    			jobName : jobName,
    			switcher : switcher,
    			failMail : failMail
		};
		href = "/system/updateJobSubmitAjax.vhtml";
		$.ajax( {
			type : "GET",
			contentType : "application/json",
			url : href.prependcontext(),
			data : clientdata,
			dataType : 'html',
			success : function(response) {
				var temp = response.replace(/&quot;/g, '\"');
				document.getElementById('table-job-list').innerHTML = temp;
				modalWindow.modal('hide');
				bind();
			}
		});
	}
}

function validateConfigForm() {
	var checkPass = true;
	resetConfigFormValidation();
	$("#input-job-name").each(function() {
		if ($(this).val().isBlank()) {
			setValidateError($(this),$("#span-job-name-error"),"必填");
			checkPass = false;
		}
	});
	if ($("#input-job-failMail").val().isBlank()) {
		setValidateError($("#input-job-failMail"),$("#span-job-failMail-error"),"必填");
		checkPass = false;
	}
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