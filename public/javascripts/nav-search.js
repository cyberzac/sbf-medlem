/**
 * Created by zac on 2014-01-25.
 */


$("#nav-search").blur(function (e) {
    var what = $("#what").val();
    document.location = "/search/" + what;
    //e.preventDefault();
});

$("#what").blur(function (e) {
    var what = $("#what").val();
    document.location = "/search/" + what;
    //e.preventDefault();
});