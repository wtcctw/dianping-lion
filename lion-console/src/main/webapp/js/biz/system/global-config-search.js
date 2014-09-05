var ALL_SELECT_ID = "-1";

$(function () {

    $("#global-config-search-form").submit(function () {
        if (validateGlobalConfigSearchForm()) {
            return;
        } else {
            var originalKeyColor = $("#key").css("border-color");
            var originalValueColor = $("#value").css("border-color");
            $("#key").css({"border-color":"red"});
            $("#value").css({"border-color":"red"});
            setTimeout(function(){$("#key").css({"border-color":originalKeyColor});}, 1000);
            setTimeout(function(){$("#value").css({"border-color":originalValueColor});}, 1000);
            event.preventDefault();
        }
    })
})

function validateGlobalConfigSearchForm() {
    var checkPass = true;
    var globalConfigSearchKey = $("#key").val();
    var globalConfigSearchValue = $("#value").val();

    if (isBlank(globalConfigSearchKey) && isBlank(globalConfigSearchValue)) {
        checkPass = false;
    } else {
        checkPass = true;
    }

    return checkPass;
}

function isBlank(value) {
    var tmpValue = value.replace(/\s/gi,"");

    if (value.length == 0 || tmpValue == "") {
        return true;
    } else {
        return false;
    }
}
