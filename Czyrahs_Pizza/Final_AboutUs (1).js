$(document).ready(function() {
    $(".lightbox-trigger").click(function() {
        let imgSrc = $(this).attr("data-image");
        $("#lightbox-img").attr("src", imgSrc);
        $("#lightbox").fadeIn();
    });

    $("#close-lightbox").click(function() {
        $("#lightbox").fadeOut();
    });
});
