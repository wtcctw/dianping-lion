function linkage(){
		$("option[team]").removeClass("hide");
		if($("#teamSelect").children('option:selected').val() != $("#productSelect").children('option:selected').attr("team")){
			$("#productSelect option").removeAttr("selected");
			$("#productSelect option:first").attr("selected","true");
		}
		
		if($("#teamSelect").children('option:selected').val() > 0){
			
			$("option[team]").addClass("hide");
			$("option[team="+$("#teamSelect").children('option:selected').val()+"]").removeClass("hide");
		}
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

$(document).ready(function(){
	
	$("#teamSelect").find("option").removeAttr("selected");
	var teamS = "option[value='"+$("#teamSelected").attr("value")+"']"
	$("#teamSelect").find(teamS).attr("selected","true");
	
	$("#productSelect").find("option").removeAttr("selected");
	var productS = "option[value='"+$("#productSelected").attr("value")+"']"
	$("#productSelect").find(productS).attr("selected","true");
	
	linkage();
	//联动select
	$("#teamSelect").change(linkage);
	//提交
	$("#query_btn").click(function(){
		var purl = "/system/projectList.vhtml?teamSelect="+$("#teamSelect").children('option:selected').val()+"&productSelect="+$("#productSelect").children('option:selected').val();
		purl = purl.prependcontext();
		$("#projectQuery").attr("action",purl);
		$("#projectQuery").submit();
	});
	$("#add_project_btn").click(function(){
		$("#techLeaderWarn").removeClass("lion_red");
		$("#techLeaderWarn").html("可输入名字或拼音提示");
		$("#operWarn").removeClass("lion_red");
		$("#operWarn").html("可输入名字或拼音提示");
		$("#add-project-modal").modal({
			backdrop : "static"
		});
	});
	$("#edit_project_btn").click(function(){
		$("#techLeaderWarnEdit").removeClass("lion_red");
		$("#techLeaderWarnEdit").html("可输入名字或拼音提示");
		$("#operWarnEdit").removeClass("lion_red");
		$("#operWarnEdit").html("可输入名字或拼音提示");
		$("#edit-project-modal").modal({
			backdrop : "static"
		});
	});
	$("#addProject").click(function(){
		
		var psa, pn, tl, op;
		psa = $("#productSelectAdd").children('option:selected').val();
		pn = $("#projectName").attr("value");
		tl = $("#techLeader").attr("value");
		if(!contain(tl,$("#techLeader").attr("data-source"))){
			$("#techLeaderWarn").addClass("lion_red");
			$("#techLeaderWarn").html("TechLeader必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
			return;
		}else{
			$("#techLeaderWarn").removeClass("lion_red");
			$("#techLeaderWarn").html("可输入名字或拼音提示");
		}
		op = $("#oper").attr("value");
		if(!contain(op,$("#oper").attr("data-source"))){
			$("#operWarn").addClass("lion_red");
			$("#operWarn").html("TechLeader必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
			return;
		}else{
			$("#operWarn").removeClass("lion_red");
			$("#operWarn").html("可输入名字或拼音提示");
		}
    	var clientdata = {
    			productId : psa,
    			projectName : pn,
    			techLeader : tl,
    			oper : op
    	};
    	href = "/system/projectAdd.vhtml";
		$.ajax( {
			type : "GET",
			contentType : "application/json",
			url : href.prependcontext(),
			data : clientdata,
			dataType : 'html',
			success : function(response) {
				$("#add-project-modal").modal({
					backdrop : "static"
				});
				location.reload();
			}
		});
	});
$("#editProject").click(function(){
		
		var psa, pn, tl, op;
		psa = $("#productSelectEdit").children('option:selected').val();
		pn = $("#projectNameEdit").attr("value");
		tl = $("#techLeaderEdit").attr("value");
		if(!contain(tl,$("#techLeaderEdit").attr("data-source"))){
			$("#techLeaderWarnEdit").addClass("lion_red");
			$("#techLeaderWarnEdit").html("TechLeader必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
			return;
		}else{
			$("#techLeaderWarnEdit").removeClass("lion_red");
			$("#techLeaderWarnEdit").html("可输入名字或拼音提示");
		}
		op = $("#operEdit").attr("value");
		if(!contain(op,$("#operEdit").attr("data-source"))){
			$("#operWarnEdit").addClass("lion_red");
			$("#operWarnEdit").html("TechLeader必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
			return;
		}else{
			$("#operWarnEdit").removeClass("lion_red");
			$("#operWarnEdit").html("可输入名字或拼音提示");
		}
    	var clientdata = {
    			productId : psa,
    			projectName : pn,
    			techLeader : tl,
    			oper : op
    	};
    	href = "/system/projectEdit.vhtml";
		$.ajax( {
			type : "GET",
			contentType : "application/json",
			url : href.prependcontext(),
			data : clientdata,
			dataType : 'html',
			success : function(response) {
				$("#add-project-modal").modal({
					backdrop : "static"
				});
				location.reload();
			}
		});
	});
});
	