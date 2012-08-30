var produceSelectArray = new Array();
var $deleteMemberAlert;
var deleteMember;

function linkage() {
	var pse = $("#productSelect option");

	pse.each(function() {
				$(this).remove();
			});
	var teamS = $("#teamSelect").children('option:selected').val();
	for (var psai in produceSelectArray) {
		if (produceSelectArray.hasOwnProperty(psai)) {
			var psa = produceSelectArray[psai];
			if (teamS == 0 || psa[1] == 0 || psa[0] == teamS) {
				$("<option team='" + psa[0] + "' value='" + psa[1] + "'>"
						+ psa[2] + "</option>").appendTo($("#productSelect"));
			}
		}

	}
	$("#productSelect").find("option").removeAttr("selected");
	var productS = "option[value='" + $("#productSelected").attr("value")
			+ "']"
	$("#productSelect").find(productS).attr("selected", "true");

	if ($("#teamSelect").children('option:selected').val() != $("#productSelect")
			.children('option:selected').attr("team")) {
		$("#productSelect option").removeAttr("selected");
		$("#productSelect option:first").attr("selected", "true");
	}

}

function contain(value, itemsStr) {
	var itemsStr_ = itemsStr.substr(2, itemsStr.length - 4);
	var items = itemsStr_.split("\",\"");
	for (var i = 0; i < items.length; i++) {
		if (value == items[i]) {
			return true;
		}
	}
	return false;
}

function editOpen(projectId, projectName, productId, manager, techLeader, oper) {
	var productS = "option[value='" + productId + "']"
	$("#productSelectEdit").find(productS).attr("selected", "true");
	$("#projectNameEdit").attr("value", projectName);
	$("#projectIdEdit").attr("value", projectId);
	$("#edit-project-modal").modal({
		backdrop : "static"
	});

}

$(document).ready(function() {
	$("[rel=tooltip]").tooltip({
		delay : {show : 800}
	});
	var v = 0;
	$("#productSelect option").each(function() {
				var opattr = new Array();
				opattr[0] = $(this).attr("team");
				opattr[1] = $(this).attr("value");
				opattr[2] = $(this).html();
				produceSelectArray[v++] = opattr;
			});

	$("#teamSelect").find("option").removeAttr("selected");
	var teamS = "option[value='" + $("#teamSelected").attr("value") + "']"
	$("#teamSelect").find(teamS).attr("selected", "true");

	$("#productSelect").find("option").removeAttr("selected");
	var productS = "option[value='" + $("#productSelected").attr("value")
			+ "']"
	$("#productSelect").find(productS).attr("selected", "true");

	linkage();
	// 联动select
	$("#teamSelect").change(linkage);
	$("#add_project_btn").click(function() {
		$("#add-project-modal").modal({
			backdrop : "static"
		});
		return false;
	});

	$("#addProject").click(function() {
		var psa = $("#productSelectAdd").children('option:selected').val();
		var pn = $("#projectName").attr("value");
		var clientdata = {
			productId : psa,
			projectName : pn,
		};
		href = "/system/projectAdd.vhtml";
		$.ajax({
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
	$("#editProject").click(function() {
		var psa = $("#productSelectEdit").children('option:selected').val();
		var pn = $("#projectNameEdit").attr("value");
		var projectId = $("#projectIdEdit").attr("value");
		var clientdata = {
			projectId : projectId,
			productId : psa,
			projectName : pn,
		};

		href = "/system/projectEdit.vhtml";
		$.ajax({
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
						$.ajax({
							type : "GET",
							contentType : "application/json",
							url : $(this).data("location").prependcontext(),
							dataType : 'html',
							success : function(response) {
								location.reload();
							}
						});
					},
					"否" : function() {
						$(this).dialog("close");
					}
				}
			});
	$(".deletelink").click(function() {
		$deleteAlert.dialog("open");
		$deleteAlert.data("location", $(this).attr("href"));
		return false;
	});
			
	$(".edit-member-btn").click(function() {
		$("#member_projectId").val(getProjectId($(this)));
		loadprojectmembers();
		$("#edit-project-member-modal").modal({
			backdrop : "static", 
			keyboard : true
		});
		return false;
	});
	
	$("#edit-project-member-modal").on("hidden", function() {
		$("#member-container").html("");
		$("#owner-link").click();
		$("#user-search,#user-search-id").val("");
	});
	
	function getProjectId($element_in_row) {
		return $element_in_row.parents(".project_row").find("[name='projectId']").val();
	}
	
	function getMemberType() {
		return $("#edit-project-member-modal .nav-tabs li.active a").attr("href").substr(1);
	}
	
	$("#user-search").autocomplete("/system/getUsersAjax.vhtml".prependcontext(), {
		minChars : 1,
		width : 230,
		max : 15,
		delay : 300,
		matchContains : false,
		hidden : "user-search-id",
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
	
	$("#add-member").click(function() {
		var userId = $("#user-search-id").val();
		if (!userId.isEmpty()) {
			var projectId = $("#member_projectId").val();
			$.ajax("/system/addMemeberAjax.vhtml".prependcontext(), {
				data : $.param({
					"projectId" : projectId,
					"memberType" : getMemberType(),
					"userId" : userId
				}, true),
				dataType : "json",
				success : function(result) {
					loadprojectmembers();
				}
			});
		}
		return false;
	});
	
	function loadprojectmembers() {
		$("#member-container").html("<img src=" + "/img/indicator.gif".prependcontext() + "></img>");
		$.ajax("/system/getMembersAjax.vhtml".prependcontext(), {
			data : $.param({
				"projectId" : $("#member_projectId").val()
			}, true),
			dataType : "html",
			success : function(result) {
				$("#member-container").html(result);
				$("#edit-project-member-modal .tab-pane").removeClass("active");
				$("#edit-project-member-modal #" + getMemberType()).addClass("active");
			}
		});
	}
	
	$deleteMemberAlert = $("<div>确认删除?</div>")
		.dialog({
			autoOpen : false,
			resizable : false,
			modal : true,
			title : "提示框",
			height : 140,
			zIndex : 1060,
			buttons : {
				"是" : function() {
					$(this).dialog("close");
					$.ajax("/system/deleteMemeberAjax.vhtml".prependcontext(), {
						data : $.param({
							"projectId" : $(this).data("projectId"),
							"memberType" : $(this).data("memberType"),
							"userId" : $(this).data("userId")
						}, true),
						dataType : "json",
						success : function(result) {
							loadprojectmembers();
						}
					});
				},
				"否" : function() {$(this).dialog("close");}
			}
		});
		
		deleteMember = function(projectId, memberType, userId) {
			$deleteMemberAlert.dialog("open");
			$deleteMemberAlert.data("projectId", projectId);
			$deleteMemberAlert.data("memberType", memberType);
			$deleteMemberAlert.data("userId", userId);
			event.preventDefault();
		}
});




