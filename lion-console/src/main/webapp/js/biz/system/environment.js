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

function saveEnv() {
    	var envName, envLabel, ips, seq;
    	envName = document.getElementById('input-env-name').value;
    	envLabel = document.getElementById('input-env-label').value;
    	envIps = document.getElementById('input-env-ips').value;
    	seq = document.getElementById('input-env-seq').value;
    	var clientdata = {
    			envName : envName,
//    			envLabel : encodeURI(encodeURI(envLabel)),
    			envLabel : envLabel,
    			envIps : envIps,
    			seq : seq
    	};
//    	clientdata = encodeURIComponent(encodeURIComponent(clientdata));
    	href = "/system/addEnvSubmitAjax.vhtml";
		$.ajax( {
			type : "GET",
			contentType : "application/json;",
//			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			url : href.prependcontext(),
			data : clientdata,
			dataType : 'html',
			success : function(response) {
				var temp = response.replace(/&quot;/g, '\"');
				document.getElementById('table-env-list').innerHTML = temp;
				modalWindow.modal('hide');
				bind();
			}
		});
}

function updateEnv() {
	var envId, envName, envLabel, ips, seq;
	envId = document.getElementById('input-env-id').value;
	envName = document.getElementById('input-env-name').value;
	envLabel = document.getElementById('input-env-label').value;
	envIps = document.getElementById('input-env-ips').value;
	seq = document.getElementById('input-env-seq').value;
	var clientdata = {
			envId : envId,
			envName : envName,
			envLabel : envLabel,
			envIps : envIps,
			seq : seq
	};
	href = "/system/updateEnvSubmitAjax.vhtml";
	$.ajax( {
		type : "GET",
		contentType : "application/json",
		url : href.prependcontext(),
		data : clientdata,
		dataType : 'html',
		success : function(response) {
			var temp = response.replace(/&quot;/g, '\"');
			document.getElementById('table-env-list').innerHTML = temp;
			modalWindow.modal('hide');
			bind();
		}
	});
}