var modalWindow;

$(document).ready(function() {
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
});

function saveEnv() {
    	var envName, envLabel, ips, seq;
    	envName = document.getElementById('input-env-name').value;
    	envLabel = document.getElementById('input-env-label').value;
    	envIps = document.getElementById('input-env-ips').value;
    	seq = document.getElementById('input-env-seq').value;
    	var clientdata = {
    			envName : envName,
    			envLabel : envLabel,
    			envIps : envIps,
    			seq : seq
    	};
    	href = "/system/addEnvSubmitAjax.vhtml";
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
		}
	});
}