var produceSelectArray = new Array();
function linkage(){
	var pse = $("#productSelect option");
	
	pse.each(function(){
		$(this).remove();
	});
	var teamS = $("#teamSelect").children('option:selected').val();
	for(var psai in produceSelectArray){
		if(produceSelectArray.hasOwnProperty(psai)){
			var psa = produceSelectArray[psai];
			if(teamS == 0 || psa[1] == 0 || psa[0] == teamS){
				$("<option team='"+psa[0]+"' value='"+psa[1]+"'>"+psa[2]+"</option>").appendTo($("#productSelect"));
			}
		}
		
	}
	$("#productSelect").find("option").removeAttr("selected");
	var productS = "option[value='"+$("#productSelected").attr("value")+"']"
	$("#productSelect").find(productS).attr("selected","true");
	
		if($("#teamSelect").children('option:selected').val() != $("#productSelect").children('option:selected').attr("team")){
			$("#productSelect option").removeAttr("selected");
			$("#productSelect option:first").attr("selected","true");
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

function editOpen(projectId,projectName,productId,manager,techLeader,oper){
	$("#managerWarnEdit").removeClass("lion_red");
	$("#managerWarnEdit").html("可输入名字或域账号提示");
	$("#techLeaderWarnEdit").removeClass("lion_red");
	$("#techLeaderWarnEdit").html("可输入名字或域账号提示");
	$("#operWarnEdit").removeClass("lion_red");
	$("#operWarnEdit").html("可输入名字或域账号提示");

	var productS = "option[value='"+productId+"']"
	$("#productSelectEdit").find(productS).attr("selected","true");
	$("#projectNameEdit").attr("value",projectName);
	$("#managerEdit").attr("value",manager);
	$("#techLeaderEdit").attr("value",techLeader);
	$("#operEdit").attr("value",oper);
	$("#projectIdEdit").attr("value",projectId);
	$("#edit-project-modal").modal({
		backdrop : "static"
	});

}

$(document).ready(function(){
	$("[rel=tooltip]").tooltip({delay : {show : 800}});
	var v = 0;
	$("#productSelect option").each(function(){
		var opattr = new Array();
		opattr[0]=$(this).attr("team");
		opattr[1]=$(this).attr("value");
		opattr[2]=$(this).html();
		produceSelectArray[v++] = opattr;
	});
	
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
		return false;
	});
	$("#add_project_btn").click(function(){
		$("#managerWarn").removeClass("lion_red");
		$("#managerWarn").html("可输入名字或域账号提示");
		$("#techLeaderWarn").removeClass("lion_red");
		$("#techLeaderWarn").html("可输入名字或域账号提示");
		$("#operWarn").removeClass("lion_red");
		$("#operWarn").html("可输入名字或域账号提示");
		$("#add-project-modal").modal({
			backdrop : "static"
		});
		return false;
	});
	
	$("#addProject").click(function(){
		var psa, pn, pm, tl, op;
		psa = $("#productSelectAdd").children('option:selected').val();
		pn = $("#projectName").attr("value");
		pm = $("#manager").attr("value");
		if(!contain(pm,$("#manager").attr("data-source"))){
			$("#managerWarn").addClass("lion_red");
			$("#managerWarn").html("PM必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
			return;
		}else{
			$("#managerWarn").removeClass("lion_red");
			$("#managerWarn").html("可输入名字或域账号提示");
		}
		tl = $("#techLeader").attr("value");
		if(!contain(tl,$("#techLeader").attr("data-source"))){
			$("#techLeaderWarn").addClass("lion_red");
			$("#techLeaderWarn").html("TechLeader必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
			return;
		}else{
			$("#techLeaderWarn").removeClass("lion_red");
			$("#techLeaderWarn").html("可输入名字或域账号提示");
		}
		op = $("#oper").attr("value");
		if(!contain(op,$("#oper").attr("data-source"))){
			$("#operWarn").addClass("lion_red");
			$("#operWarn").html("业务运维必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
			return;
		}else{
			$("#operWarn").removeClass("lion_red");
			$("#operWarn").html("可输入名字或域账号提示");
		}
    	var clientdata = {
    			productId : psa,
    			projectName : pn,
    			manager : pm,
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
		return false;
	});
$("#editProject").click(function(){
	var projectId, psa, pn, pm, tl, op;
	psa = $("#productSelectEdit").children('option:selected').val();
	pn = $("#projectNameEdit").attr("value");
	projectId = $("#projectIdEdit").attr("value");
	pm = $("#managerEdit").attr("value");
	if(!contain(pm,$("#managerEdit").attr("data-source"))){
		$("#managerWarnEdit").addClass("lion_red");
		$("#managerWarnEdit").html("PM必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
		return;
	}else{
		$("#managerWarnEdit").removeClass("lion_red");
		$("#managerWarnEdit").html("可输入名字或域账号提示");
	}
	tl = $("#techLeaderEdit").attr("value");
	if(!contain(tl,$("#techLeaderEdit").attr("data-source"))){
		$("#techLeaderWarnEdit").addClass("lion_red");
		$("#techLeaderWarnEdit").html("TechLeader必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
		return;
	}else{
		$("#techLeaderWarnEdit").removeClass("lion_red");
		$("#techLeaderWarnEdit").html("可输入名字或域账号提示");
	}
	op = $("#operEdit").attr("value");
	if(!contain(op,$("#operEdit").attr("data-source"))){
		$("#operWarnEdit").addClass("lion_red");
		$("#operWarnEdit").html("业务运维必须是下拉框中的成员，如果不存在请让其先用域帐号登录系统");
		return;
	}else{
		$("#operWarnEdit").removeClass("lion_red");
		$("#operWarnEdit").html("可输入名字或域账号提示");
	}
	var clientdata = {
			projectId : projectId,
			productId : psa,
			projectName : pn,
			manager : pm,
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
			$("#edit-project-modal").modal({
				backdrop : "static"
			});
			location.reload();
		}
	});
	return false;
	});

var $deleteAlert = $("<div>确认删除该项目? [<font color='green'>不可恢复</font>]</div>")
.dialog({
	autoOpen : false,
	resizable : false,
	modal : true,
	title : "提示框",
	height : 140,
	buttons : {
		"是" : function() {
			$.ajax( {
				type : "GET",
				contentType : "application/json",
				url : $(this).data("location").prependcontext(),
				dataType : 'html',
				success : function(response) {
					location.reload();
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
});
	