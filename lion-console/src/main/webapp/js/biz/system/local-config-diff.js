$(function(){

    $("#config-diff-form").submit(function(event) {
        if (validateDiffConfigForm()) {
            return;
        } else {
            $("#config-diff-warning").text("比较环境不能相同").css({"color":"red"}).show();
            setTimeout(function(){$("#config-diff-warning").hide()}, 1000);
            event.preventDefault();
        }
    });
})

function validateDiffConfigForm() {
    var checkPass = true;
    var diffConfigEnv1 = $("#diff-config-env1").val();
    var diffConfigEnv2 = $("#diff-config-env2").val();

    if (diffConfigEnv1 != diffConfigEnv2) {
        checkPass = true;
    } else {
        checkPass = false;
    }
    return checkPass;
}