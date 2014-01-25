/**
 * Created by zac on 2014-01-25.
 */


$("#nav-search").on("submit", function (e) {
    var what = $("#what").val();
    document.location = "/search/" + what;
    e.preventDefault();
})