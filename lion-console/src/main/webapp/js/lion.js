function displayLogContent() {
	var content, project, opType, user, env, from, to;
	content = document.getElementById('log-textarea').value;
	project = document.getElementById('log-project').value;
	opType = document.getElementById('log-opType').value;
	user = document.getElementById('log-user').value;
	env = document.getElementById('log-env').value;
	from = document.getElementById('input-time-start-log').value;
	to = document.getElementById('input-time-end-log').value;
	var clientdata = {
			content : content,
			project : project,
			opType : opType,
			user : user,
			env : env,
			from : from,
			to : to
	};
	var myjson = JSON.encode(clientdata);
	var req = new Request( {
//		url : "/system/opLogList.vhtml?content="+content+"&project="+project+"&opType="+opType+"&user="+user+"&env="+env
		url : "/system/opLogListAjax.vhtml",
		data : 'clientdata=' + encodeURIComponent(myjson),
		onSuccess : function(response) {
		var temp = response.replace(/&quot;/g, '\"');
		document.getElementById('table-oplog-list').innerHTML = response;
		//decoratePage();
		},
		onFailure : function(response) {
			this.options.result = response;
		}
	});	
	req.send();
	
}

function appendcontext() {
		
}
}