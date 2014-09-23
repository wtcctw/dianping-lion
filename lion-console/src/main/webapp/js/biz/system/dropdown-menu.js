$(function () {

    $(".dropdown").click(function () {
        var offset = $(this).offset().left;
        var winWidth = $(window).width();

        var dropdownMenu = $('.dropdown-menu');
        dropdownMenu.removeClass('pull-right');
        dropdownMenu.removeClass('pull-left');

        if (offset * 2 < winWidth) {
            dropdownMenu.addClass("pull-left");
        } else {
            dropdownMenu.addClass("pull-right");
        }
    });
})