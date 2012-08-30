var jump2Page;
var viewdetail;

$(document).ready(function() {
	jump2Page = function(pageNo) {
		$("#searchForm [name='paginater.pageNumber']").val(pageNo);
		$("#searchForm").submit();
		event.preventDefault();
	};
	$("#log-search-fromdate,#log-search-todate").click(function() {
		var options = {dateFmt : "yyyy-MM-dd HH:mm:ss"};
		if ($(this).attr("id") == "log-search-fromdate") {
			options.maxDate = "#F{$dp.$D('log-search-todate')}";
		} else {
			options.minDate = "#F{$dp.$D('log-search-fromdate')}";
		}
		WdatePicker(options);
	});
	$("#log-opType").change(function() {
		var projectRelated = $(this).val().substringAfterLast("|");
		if ("true" == projectRelated) {
			$("#log-project,#log-env").attr("disabled", false);
		} else {
			$("#log-project,#log-env").val("");
			$("#log-project,#log-env").attr("disabled", true);
		}
	});
	$("#op-user-loginname").autocomplete("/system/getUsersAjax.vhtml".prependcontext(), {
		minChars : 1,
		width : 230,
		max : 15,
		delay : 300,
		matchContains : false,
		hidden : "op-user-id",
		formatItem: function(data, i, n, value) {
			return data.loginName + " -- " + data.name;
		},
		formatResult : function(data, value) {
			return data.loginName;
		},
		formatHidden : function(data , value) {
			return data.id;
		}
	});
	$("#big-log-modal,#middle-log-modal").on("hidden", function() {
		$(this).find(".modal-body").html("");
	});
	viewdetail = function(modal, logid, keys) {
		$modal = $("#" + modal + "-log-modal");
		$.ajax("/system/viewKeysAjax.vhtml".prependcontext(), {
			data : $.param({
				"logid" : logid,
				"keys" : keys
			}, true),
			dataType : "html",
			success : function(result) {
				$modal.find(".modal-body").html(result);
			}
		});
		$modal.modal({keyboard : true});
		event.preventDefault();
	}
});   