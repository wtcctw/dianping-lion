var Type_String = 5;
var Type_Number = 10;
var Type_Bool = 15;
var Type_List = 20;
var Type_Map = 25;
var Type_Ref = 30;

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
	
	$("#add-config-btn").click(function() {
		$("#add-config-modal").modal({
			backdrop : "static"
		});
	});
	
	$("#config-type-selector").change(function() {
		var type = parseInt($(this).val());
		renderValueComponent(generateValueComponent(type));
	});
	
	$("#ccc").click(function() {
		$("#modal2").modal({
			backdrop : "static"
		});
	});
	
	function renderValueComponent(html) {
		$("#config-value-container").html(html);
	}
	
	function generateValueComponent(type) {
		switch (type) {
			case Type_String : return generateStringComponent();
			case Type_Number : return generateNumberComponent();
			case Type_Bool : return generateBoolComponent();
			case Type_List : return generateListComponent();
			case Type_Map : return generateMapComponent();
			case Type_Ref : return generateRefComponent();
		}
	}
	
	function generateStringComponent() {
		return "<textarea name='value' rows='5' style='width:350px;'></textarea>";
	}
	
	function generateNumberComponent() {
		return "<input type='text' name='value' class='input-medium'>";
	}
	
	function generateBoolComponent() {
		return "<input type='radio' name='value' id='a1' checked='checked'><label for='a1' class='help-inline'>是</label>"
			+ "<input type='radio' name='value' id='a2'><label for='a2' class='help-inline'>否</label>";
	}
	
	function generateListComponent() {
		return "";
	}
	
	function generateMapComponent() {
		return "";
	}
	
	function generateRefComponent() {
		return "";
	}

});