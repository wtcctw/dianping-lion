var ALL_SELECT_ID = "-1";

$(function () {

    $("#global-config-diff-form").ready(function () {
        if (isTeamAllSelect()) {
            initProductSelector();
            initProjectSelector();
        } else if (isProductAllSelect()) {
            initProjectSelector();
        }
    })

    $("#business-team").change(function () {

        initProductSelector();
        initProjectSelector();

        if (!isTeamAllSelect()){
            $.ajax({
                type: 'GET',
                url: '/system/selectProductAjax.vhtml',
                data: {
                    'criteria.teamId': $("#business-team").val()
                },
                datatype: "json",
                success: function (data) {
                    $("#business-product").empty();
                    activeElement($("#business-product"));

                    data = JSON.parse(data);
                    $.each(JSON.parse(data.result), function (i, value) {
                        $("#business-product")
                            .append($("<option></option>")
                                .attr("value", value.id)
                                .text(value.name));
                    });
                }
            });
        }
    });

    $("#business-product").change(function () {
        initProjectSelector();

        if (!isProductAllSelect()){
            $.ajax({
                type: 'GET',
                url: '/system/selectProjectAjax.vhtml',
                data: {
                    'criteria.productId': $("#business-product").val()
                },
                datatype: "json",
                success: function (data) {
                    $("#business-project").empty();
                    activeElement($("#business-project"));

                    data = JSON.parse(data);
                    $.each(JSON.parse(data.result), function (i, value) {
                        $("#business-project")
                            .append($("<option></option>")
                                .attr("value", value.id)
                                .text(value.name));
                    });
                }
            });
        }
    });

    $("global-config-diff-form").submit(function () {

    });

    $("#global-config-diff-form").submit(function(event) {
        if (validateGlobalConfigDiffForm()) {
            return;
        } else {
            var originalColor = $("#right-env-id").css("border-color");
            $("#right-env-id").css({"border-color":"red"});
            setTimeout(function(){$("#right-env-id").css({"border-color":originalColor});}, 1000);
            event.preventDefault();
        }
    });
})

function validateGlobalConfigDiffForm() {
    var checkPass = true;
    var lglobalConfigDiff = $("#left-env-id").val();
    var rglobalConfigDiff = $("#right-env-id").val();

    if (lglobalConfigDiff != rglobalConfigDiff) {
        checkPass = true;
    } else {
        checkPass = false;
    }
    return checkPass;
}

function activeElement ($element) {
    $element.attr("disabled", false);
}

function disableElement ($element) {
    $element.attr("disabled", true);
}

function isTeamAllSelect () {
    if ($("#business-team").val() === ALL_SELECT_ID) {
        return true;
    }
    return false;
}

function isProductAllSelect () {
    if ($("#business-product").val() === ALL_SELECT_ID) {
        return true;
    }
    return false;
}

function initProductSelector () {
    $("#business-product").find('option:selected').removeAttr("selected");
    $("#business-product").find('option[value="-1"]').attr("selected", true);
    $("#business-product").val("-1");
    disableElement($("#business-product"));
}

function initProjectSelector () {
    $("#business-project").find('option:selected').removeAttr("selected");
    $("#business-project").find('option[value="-1"]').attr("selected", true);
    $("#business-project").val("-1");
    disableElement($("#business-project"));
}
