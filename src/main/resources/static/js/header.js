var friendNotice = []; //好友通知

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

    if(friendNotice.length === 0){
        $.ajax({
            url: "http://localhost:9400/xzzj/bbs/account/friend/getMyFriendNotice",
            type: "GET",
            datatype: "json",
            success: function (result) {
                console.log(result);
                let $hct = $(".h-ct");
                let $ct = $(".ct");
                $ct.html('');
                if(result.code === 200){
                    let vo = result.data;
                    friendNotice = vo;
                    $hct.text(vo.length);
                    for (let i = 0; i < vo.length; i++) {
                        let $div = getdefaultNotice(vo[i]);
                        $ct.append($div);
                    }
                }else {
                    $ct.append("<div class='c-div-tip'>空空如也</div>");
                    $hct.text('');
                }
            }
        });
    }
});
/** 当点下消息下的子菜单后 */
$(".h-div").each(function () {
   $(this).click(function () {
       $(this).find(".h-div-tip").text('');
   })
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
var $hDiv = $(".h-div:first-child");
$(".h-div").click(function () {
    let i = -($(this).attr("i") - 1)*400;
    $hDiv.removeClass("h-div-active");
    $hDiv = $(this);
    $(this).addClass("h-div-active");
    $(".c-div").css({"overflow-y":"hidden"});
    setTimeout(function () {
        $(".c-div").css({"overflow-y":"auto"});
    },200);
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
/** 获取默认通知 */
function getdefaultNotice(vo) {
    let $div;
    if(vo.isOriginator){
        $div = $("<div class='c-ct ct-ok' i='"+vo.id+"'>" +
            "<span>&nbsp;"+vo.myNickname+"&nbsp;向&nbsp;"
            +vo.otherNickname+"&nbsp;发起好友请求&nbsp;时间："+vo.time+"</span></div>");
    }else {
        if(vo.result)
        $div = $("<div class='c-ct ct-ero' i='"+vo.id+"'>" +
            "<span>&nbsp;"+vo.myNickname+"&nbsp;向&nbsp;"
            +vo.otherNickname+"&nbsp;发起好友请求&nbsp;时间："+vo.time+"</span></div>");
    }
    return $div;
}