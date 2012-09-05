$(function(){
	$("#login-modal").on("hidden", function() {
		$("input[type='text'],input[type='password']").val("");
		$("#login-msg").html("");
		resetLoginFormValidation();
	});
	
	$("#login-modal").on("shown", function() {
		$("#login-name").focus();
	});
	
	$("#login_link").click(function() {
		$("#login-modal").modal({
			backdrop : "static", 
			keyboard : true
		});
		return false;
	});	
	
	var formSubmitHandler = function(event) {
		$("#login-msg").html("");
		if (validateLoginForm()) {
			$.ajax("/loginAjax.vhtml".prependcontext(), {
				data : $.param({
					"loginName" : $("#login-name").val().trim(),
					"passwd" : $("#login-passwd").val()
				}, true),
				dataType : "json",
				success : function(result) {
					if (result.code == Res_Code_Success) {
						$("#login-msg").html("<font color='green'>登陆成功, 载入中...</font>");
						window.location.reload();
					} else if (result.code == Res_Code_Error) {
						$("#login-msg").html("<font color='#c09853'>" + result.msg + "</font>");
					}
				}
			});
		}
		return false;
	};
	
	$("input[type='text'],input[type='password']").keypress(function(e) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		if (keyCode == 13) {
			formSubmitHandler(e);
		}
	});
	
	$("#login-btn").click(formSubmitHandler);
	
	function validateLoginForm() {
		var checkPass = true;
		var firstErrorEle;
		resetLoginFormValidation();
		$("#login-name, #login-passwd").each(function() {
			if ($(this).val().isBlank()) {
				setValidateError($(this));
				if (firstErrorEle == null) {
					firstErrorEle = $(this);
				}
				checkPass = false;
			}
		});
		if (!checkPass && firstErrorEle != null) {
			firstErrorEle.focus();
		}
		return checkPass;
	}
	
	function resetLoginFormValidation() {
		$(".control-group").removeClass("error");
		$(".message").hide();
	}
	
	function setValidateError($element) {
		$element.parents(".control-group").addClass("error");
		$element.next(".message").show();
	}
});