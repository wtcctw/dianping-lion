$(function(){
	$("[rel=tooltip]").tooltip();
	$(".icon-intro").popover();
	
	var $clearAlert = $("<div>确认清除该配置项值? [<font color='green'>不可恢复</font>]</div>")
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
	
	$(".clearLink").click(function() {
		$clearAlert.dialog("open");
		$clearAlert.data("location", $(this).attr("href"));
		return false;
	});
	
	$(".deleteLink").click(function() {
		$deleteAlert.dialog("open");
		$deleteAlert.data("location", $(this).attr("href"));
		return false;
	});

});