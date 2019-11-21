getUserInfo();
/** 获取自己的用户信息 */
function getUserInfo() {
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/getUserInfo",
        type: "GET",
        datatype: "json",
        success: function (result) {
            if (result.code === 200) {
                var userInfo = result.data;
                $(".user-group").find("img").attr("src", userInfo.portrait);
            } else {
                console.log(result.msg)
            }
        }
    })
}
/** 点击用户组下退出链接 */
$(".user-exit").parent("a").click(function () {
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/logout",
        type: "POST",
        datatype: "json",
        success: function (result) {
            if (result.code === 200) {
                window.location.replace("http://localhost:9400");
            } else {
                console.log(result.msg);
            }
        }
    })
});
/** 点击用户组下消息链接 */
$(".user-msg").parent("a").click(function () {
    $(".msg-box").removeClass("msg-box-hide");
});
/** 当点击消息框外后关闭消息框 */
$(document).click(function (e) {
    var e = e || window.event;
    $el = $(e.target);
    if(!$el.isChildAndSelfOf(".msga")){
        $(".msg-box").addClass("msg-box-hide");

    }
});
/** 点击消息框的三个标签 */
$(".h-div").click(function () {
    let i = -($(this).attr("i") - 1)*500;
    console.log(i);
    $(".msg-content").css({"margin-left":i+"px"})
});
/** 点击用户组下个人中心链接 */
$(".user-space").parent("a").click(function () {
    window.location.replace("http://localhost:9400/xzzj/user_details");
});

//判断:当前元素是否是被筛选元素的子元素或者本身
jQuery.fn.isChildAndSelfOf = function(b){
    return (this.closest(b).length > 0);
};