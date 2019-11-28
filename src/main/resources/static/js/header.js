var friendNotice = []; //好友通知
var invalidFN = [];     //失效的好友通知
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
                let $ct = $(".ct-ctt-des");
                if(result.code === 200){
                    let vo = result.data;
                    window.localStorage.setItem("ct",vo.length);
                    flushRedPoint();
                    friendNotice = vo;
                    for (let i = 0; i < vo.length; i++) {
                        let $div = getdefaultNotice(vo[i]);
                        $ct.append($div);
                    }
                }else {
                    $ct.append("<div class='c-div-tip'>空空如也</div>");
                }
            }
        });
    }
    if(invalidFN.length === 0){
        $.ajax({
            url: "http://localhost:9400/xzzj/bbs/account/friend/getMyInvalidFriendNotice",
            type: "GET",
            datatype: "json",
            success: function (result) {
                let $ct = $(".ct-ctt-inv");
                if(result.code === 200){
                    let vo = result.data;
                    invalidFN = vo;
                    for (let i = 0; i < vo.length; i++) {
                        let $div = getdefaultNotice(vo[i]);
                        $ct.append($div);
                    }
                }else {
                    $ct.append("<div class='c-div-tip'>空空如也</div>");
                }
            }
        })
    }
});
/** 同意或拒绝好友申请 */
$("body").on("click", ".ct-btn-ok", function () {
    clickCtbtn($(this), true);
});
$("body").on("click", ".ct-btn-ero", function () {
    clickCtbtn($(this), false);
});
function clickCtbtn($el, parm){
    let $f = $el.parents(".c-ct");
    let id = $f.attr("i");

    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/friend/answer",
        type: "POST",
        data: {"id": id,"res": parm},
        datatype: "json",
        success: function (result) {
            if(result.code === 200){
                $el.parent().remove();
                let rm = parm ? "ct-rm-ok" : "ct-rm-ero";
                let rs = parm ? "已同意" : "已拒绝";
                $f.append("<div class='ct-result-msg'><div class='ct-rm "+
                    rm+"'></div><span class='ct-rs'>"+rs+"</span></div>");
            }else {
                errtip_show(result.msg);
            }
        }
    });
}
/** 消除小红点 */
$(".h-div").each(function () {
   $(this).click(function () {
       $(this).find(".h-div-tip").text('');
   })
});
/** 刷新小红点 */
function flushRedPoint(){
    let ctNum = window.localStorage.getItem("ct");
    $(".h-ct").text(ctNum);
}
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
/** 点击通知文字展开与隐藏 */
$(".ct-msg").click(function () {
    if($(this).hasClass("text-hide")) {
        $(this).removeClass("text-hide");
        $(this).parents(".c-ct").addClass("c-ct-open");
    }else {
        $(this).addClass("text-hide");
        $(this).parents(".c-ct").removeClass("c-ct-open");
    }
});
function clickMsg($el){
    if($el.hasClass("text-hide")) {
        $el.removeClass("text-hide");
        $el.parents(".c-ct").addClass("c-ct-open");
    }else {
        $el.addClass("text-hide");
        $el.parents(".c-ct").removeClass("c-ct-open");
    }
}
/** 点击用户组下个人中心链接 */
$(".user-space").parent("a").click(function () {
    window.location.replace("http://localhost:9400/xzzj/user_details");
});
/** 展开与关闭消息栏 */
$(".ch-text").click(function () {
   let $next = $(this).next();
   if($next.hasClass("hide-notice")){

       $(this).addClass("ch-text-blue");
       $(this).text($(this).text().slice(0,4));
       $next.removeClass("hide-notice");
   }else {

       $next.addClass("hide-notice");
       $(this).removeClass("ch-text-blue");
       $(this).text($(this).text()+ " ("+ ($next.children().length || 0) +")");
   }

});

//判断:当前元素是否是被筛选元素的子元素或者本身
jQuery.fn.isChildAndSelfOf = function(b){
    return (this.closest(b).length > 0);
};
/** 获取默认通知 */
function getdefaultNotice(vo) {
    let $div;

    vo.msg = vo.msg === '' || vo.msg === undefined ? '' : "<span class='ct-msg text-hide ct-msg-lang'>附加消息："+vo.msg+"</span>";
    let rmHtml = getResultBox(vo.result, vo.isOriginator);
    if(vo.isOriginator){
        $div = $("<div class='c-ct' i='"+vo.id+"'>" +
            "<span class='ct-title'>您对&nbsp;<a class='ct-a' href='#'>"+vo.otherNickname+"</a>"+
            "&nbsp;发送了好友请求</span>" +
            "<span class='ct-time' title='发送于："+vo.time+"'>发送于："+vo.time+"</span>" +
            vo.msg + rmHtml +"</div>");
        let $title = $div.find(".ct-title");
        $title.attr("title", $title.text());
    }else {
        if(vo.result)
            $div = $("<div class='c-ct' i='"+vo.id+"'>" +
                "<span class='ct-title'><a class='ct-a' href='#'>"+vo.otherNickname+"</a>"+
                "&nbsp;对您发出好友申请</span>" +
                "<span class='ct-time' title='发送于："+vo.time+"'>发送于："+vo.time+"</span>" +
                vo.msg + "</span>" + rmHtml + "</div>");

        let $title = $div.find(".ct-title");
        $title.attr("title", $title.text());
    }

    return $div;
}
/** 创建消息元素 */
function getResultBox(result, isOriginator){

    let rss = ["已同意", "已通过", "被拒绝", "已拒绝", "已过期", "等待中"];
    let rms = ["ct-rm-ok", "ct-rm-ero", "ct-rm-invalid", "ct-rm-load"];

    let rm,rs;

    if(result === 1){
        if(!isOriginator){
            return "<div class='ct-btn-div'><button class='ct-btn-ok'>同意</button><button class='ct-btn-ero'>拒绝</button></div>"
        }
        rm = rms[3];rs = rss[5];
    }else if(result === 2){
        if(isOriginator){
            rm = rms[0];rs = rss[0];
        }
        rm = rms[0];rs = rss[1];
    }else if(result === 3){
        if(isOriginator){
            rm = rms[1];rs = rss[2];
        }
        rm = rms[1];rs = rss[3];
    }else {
        rm = rms[2];rs = rss[4];
    }
    return "<div class='ct-result-msg'><div class='ct-rm "+rm+"'></div><span class='ct-rs'>"+rs+"</span></div>";
}
/** 绑定消息展开与关闭 */
$("body").on("click", ".ct-msg", function () {
    if($(this).hasClass("text-hide")) {
        $(this).removeClass("text-hide");
        $(this).parents(".c-ct").addClass("c-ct-open");
    }else {
        $(this).addClass("text-hide");
        $(this).parents(".c-ct").removeClass("c-ct-open");
    }
});
/** 绑定好友同意拒绝 */
$("body").on("click", ".ct-btn-ok", function () {
   let id = $(this).parents(".c-ct").attr("i");

   $.ajax({
       url: "http://localhost:9400/xzzj/bbs/account/friend/accept",
       type: "POST",
       data: {"id":id},
       datatype: "json",
       success: function (result) {
           if(result.code === 200){

           }else {
               errtip_show(result.msg);
           }
       }

   });
});