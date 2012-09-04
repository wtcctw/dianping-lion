//#clear-btn
var clearcache;
var $commonAlert;
$(function(){
	$commonAlert = $("<div class='alert-body'></div>")
		.dialog({
			autoOpen : false, 
			resizable : false,
			modal : true,
			title : "信息框",
			height : 140,
			buttons : {
				"确定" : function() {$(this).dialog("close");}
			}
		});
		
	clearcache = function(cache) {
		$.ajax("/system/clearCacheAjax.vhtml".prependcontext(), {
			data: $.param({
				"cache" : cache
			}, true),
			dataType: "json",
			success: function(result) {
				if (result.code == Res_Code_Success) {
					$commonAlert.html("清除成功.").dialog("open");
				} else if (result.code == Res_Code_Error) {
					$commonAlert.html(result.msg).dialog("open");
				}
			}
		});
		event.preventDefault();
	};
});