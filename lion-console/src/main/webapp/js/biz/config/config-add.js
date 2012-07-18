var Type_String = 5;
var Type_Number = 10;
var Type_Bool = 15;
var Type_List = 20;
var Type_Map = 25;
var Type_Ref = 30;

$(function(){
	
	$("#type").change(function() {
		var type = parseInt($(this).val());
		renderValueComponent(generateValueComponent(type));
	});
	
	function renderValueComponent(html) {
		$("#valueContainer").html(html);
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
		return "<textarea name='value' rows='5' style='width:750px;'></textarea>";
	}
	
	function generateNumberComponent() {
		return "<input type='text' name='value' class='input-medium'>";
	}
	
	function generateBoolComponent() {
		return "<input type='radio' name='value' id='a1' checked='checked'><label for='a1'>是</label>"
			+ "<input type='radio' name='value' id='a2'><label for='a2'>否</label>";
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