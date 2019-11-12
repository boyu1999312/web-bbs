

/** 执行函数 */
lunbo();
daojishi();
getUserInfo();

/** 轮播图 */
function lunbo() {
    var $subscript = $(".subscript span");
    var $left = $(".container-left");
    var $photo = $(".photo");
    var index = 0;
    var flag = false;
    setInterval(function () {
        if(flag) return;
        if(index === 5) index = 0;
        $photo.css("marginLeft", -(index * 450));
        $subscript.css("background","hsla(0,0%,13%,.2)");
        $(".subscript span:eq("+index+")").css("background","red");
        index++;
    },3000);
    $left.hover(function () {
        flag = true
    },function () {
        flag = false
    });
    $subscript.hover(function () {
        index = $(this).text() - 1;
        $photo.css("marginLeft", -(index * 450));
        $subscript.css("background","hsla(0,0%,13%,.2)");
        $(".subscript span:eq("+index+")").css("background","red")
    })
}
/** 任务卡片倒计时 */
function daojishi() {
    $('.time').countdown('2020/1/23 0:0:0', function(event) {
        $(this).html(event.strftime('%D天 %H:%M:%S'));
    });
}


/** 点击用户组下退出链接 */
$(".user-exit").parent("a").click(function () {
    $.ajax({
        url: "http://119.3.170.239/xzzj/bbs/account/logout",
        type: "POST",
        datatype: "json",
        success: function (result) {
            if(result.code === 200){
                window.location.replace("http://119.3.170.239");
            }else {
                console.log(result.msg);
            }
        }
    })
});
/** 点击用户组下个人中心链接 */
$(".user-space").parent("a").click(function () {
    window.location.replace("http://119.3.170.239/xzzj/user_details");
});
function getUserInfo() {
    $.ajax({
        url: "http://119.3.170.239/xzzj/bbs/account/getUserInfo",
        type: "POST",
        datatype: "json",
        success: function (result) {
            if(result.code === 200){
                var userInfo = result.data;
                console.log(userInfo);
                $(".user-group").find("img").attr("src", userInfo.portrait);
            }else {
                console.log(result.msg)
            }
        }
    })
}