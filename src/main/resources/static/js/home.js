var cardMask = false; //false为弹出层消失
var imgfile = null; //存储图片数据
/** 执行函数 */
lunbo();
getMyCard();
getCardToken();

/** 轮播图 */
function lunbo() {
    var $subscript = $(".subscript span");
    var $left = $(".container-left");
    var $photo = $(".photo");
    var index = 0;
    var flag = false;
    setInterval(function () {
        if (flag) return;
        if (index === 5) index = 0;
        $photo.css("marginLeft", -(index * 450));
        $subscript.css("background", "hsla(0,0%,13%,.2)");
        $(".subscript span:eq(" + index + ")").css("background", "red");
        index++;
    }, 3000);
    $left.hover(function () {
        flag = true
    }, function () {
        flag = false
    });
    $subscript.hover(function () {
        index = $(this).text() - 1;
        $photo.css("marginLeft", -(index * 450));
        $subscript.css("background", "hsla(0,0%,13%,.2)");
        $(".subscript span:eq(" + index + ")").css("background", "red")
    })
}

/** 任务卡片倒计时 */
function cardCountdown() {
    $('.time').each(function () {
        let time = $(this).attr("time");
        // '2020/1/23 0:0:0'
        $(this).countdown(time, function (event) {
            $(this).text(event.strftime('%D天 %H:%M:%S'));
        });
    });
};

/** 获取添加卡片的Token */
function getCardToken(){
    $.ajax({
       url: "http://localhost:9400/xzzj/bbs/card/getAddCardToken",
       type: "GET",
       datatype: "json",
       success: function (result) {
           if(result.code === 200){
               $("input[name='token']").val(result.data);
           }else {
               errtip_show(result.msg);
           }
       }
    });
}

/** 添加一个任务卡片 */
$(".card-plus").click(function (e) {
    if (!cardMask) {
        var $mask = $(".card-mask");
        var top = ($(window).height() - $mask.height()) / 2;
        var left = ($(window).width() - $mask.width()) / 2;
        var scrollTop = $(document).scrollTop();
        var scrollLeft = $(document).scrollLeft();
        $mask.removeClass("mask-hide");

        cardMask = !cardMask;
        $("body").addClass("overflow-hide");
        timeInit();
        getCardToken();
    }

    // return;
});
/** 删除一个任务卡片 */
$("body").on("click", ".card-del", function () {
    let $el = $(this).parents(".card");
    let id = $el.attr("id");
    console.log(id);
    if(id !== ''){
        $.ajax({
            url: "http://localhost:9400/xzzj/bbs/card/delCard",
            type: "POST",
            data: {"id": id},
            datatype: "json",
            success: function (result) {
                if(result.code === 200){
                    success_show(result.msg, 1500);
                    $el.remove();
                }else {
                    errtip_show(result.msg);
                }
            }
        });
    }

});

/** 将卡片信息装填到页面 */
function cardInit(id, time, title, msg, pic) {
    msg = msg === "" ? "这个人很懒，没有简介了..." : msg;
    var $card = $("<div class='card card-move' id='"+id+"'>" +
        "<div class='des'>" +
        "<h2 class='time' time='" + time + "'></h2>" +
        "<span class='time-des'>完成 [" + title + "] 的时间还有...</span>" +
        "<pre class='more-des'>" + msg + "</pre>" +
        "<div class='btm-des'><div title='删除卡片' class='btm-icon card-del'></div></div></div></div>");
    $card.css({"background": "url('" + pic + "') no-repeat", "background-size": "contain"});
    $(".card-plus").before($card);
    cardCountdown();
}



/** 检查表单项是否为空 */
function checkIsNull() {

    if ($("#cardForm input[name='title']").val() !== "" &&
        $("#cardForm input[name='checkUserName']").val() !== "") {
        return true
    }
    return false;
}

/** 时间初始化 */
function timeInit() {
    let date = new Date().Format("yyyy-MM-ddThh:mm");
    $("input[name='time']").val(date);
}

/** 时间blur */
$("input[name='time']").blur(function () {
    minTime();
});

/** 设置时间不得小于当前时间 */
function minTime() {
    let $el = $("#cardForm input[name='time']");
    let time = new Date(reTime($el.val()).replace("-", "/").replace("-", "/"));
    let date = new Date();
    // console.log("time: " + time);
    // console.log("date: " + date);
    let flag = time > date;
    // console.log("大小比对：" + (flag));
    if (!flag) {
        $el.val(date.Format("yyyy-MM-ddThh:mm"))
        errtip_show("设置时间不能小于等于当前时间");
        return false;
    }
    return true;
}

/** 时间格式转成字符串 */
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};


/** 图片预览 */
$("input[name='pic']").change(function (e) {
    imgfile = e.currentTarget.files[0];
    var objUrl = $(this)[0].files[0];
    //获得一个http格式的url路径:mozilla(firefox)||webkit or chrome
    var windowURL = window.URL || window.webkitURL;
    //createObjectURL创建一个指向该参数对象(图片)的URL
    var dataURL = windowURL.createObjectURL(objUrl);
    $(".label-up-pic").css({
        "background": "url(" + dataURL + ") no-repeat"
        , "background-size": "contain"
    }).text("");
    $(".close-pic").removeClass("hide");
});

/** 图片撤销 */
function clearPic() {
    $("input[name='pic']").val("");
    $(".label-up-pic").css({"background": "",}).text("上传图片");
    $(".close-pic").addClass("hide");
    imgfile = null;
}

$(".close-pic").click(function () {
    clearPic();
});
/** 监督人搜索 */
$(".checkUser-input").bind("input propertychange", function () {
    var $cdiv = $(".checkUser-div");
    if ($(this).val() === '') {
        $cdiv.addClass("hide");
        $(".checkUser-div").removeClass("high-height");

    } else {

        $(".checkUser-tip").removeClass("hide").text("搜索中...");

        $.ajax({
            url: "http://localhost:9400/xzzj/bbs/account/getUserByUserName",
            type: "POST",
            data: {"userName": $(this).val()},
            datatype: "json",
            success: function (result) {

                $(".checkUser-content").html("");
                if (result.code === 200) {
                    if (result.data.length > 1) {
                        $(".checkUser-div").addClass("high-height");
                    } else {
                        $(".checkUser-div").removeClass("high-height");
                    }
                    for (let i = 0; i < result.data.length; i++) {
                        var vo = result.data[i];
                        $(".checkUser-tip").addClass("hide");
                        var name = vo.nickName == null || vo.nickName === "" ? vo.userName : vo.nickName;
                        var $user = $("<div class='checkUser'><div class='click-cuser'>" +
                            "<img class='checkUser-p' src='" + vo.portrait + "'>" +
                            "<span class='checkUser-name' i='"+vo.id+"'>" + name + "</span></div>" +
                            "<span class='add-user'>+</span>" +
                            "</div>");
                        $(".checkUser-content").append($user);
                    }
                } else {
                    $(".checkUser-tip").removeClass("hide");
                    $(".checkUser-tip").text(result.msg);
                    $(".checkUser-div").removeClass("high-height");
                }
            }
        });
        $cdiv.removeClass("hide");
    }
});
/** 添加好友按钮 鼠标进入 */
$("body").on("mouseenter", ".add-user", function () {

    $(this).css({"background": "#1cc21c", "width": "64px"}).text("添加好友");
});
/** 添加好友按钮 鼠标离开 */
$("body").on("mouseleave", ".add-user", function () {

    $(this).css({"background": "#1AB11A", "width": "11px"}).text("+");
});
/** 添加好友按钮 鼠标按下 */
$("body").on("mousedown", ".add-user", function () {

    $(this).css({"background": "#137d13"});
});
/** 添加好友按钮 鼠标抬起 */
$("body").on("mouseup", ".add-user", function () {

    $(this).css({"background": "#1cc21c"});
});
/** 添加监督人 */
$("body").on("click", ".click-cuser", function () {
    let uname = $(this).find(".checkUser-name").text();
    let id = $(this).find(".checkUser-name").attr("i");
    $("input[name='checkUserName']").val(uname);
    $("input[name='checkUserId']").val(id);
    $(".checkUser-div").addClass("hide");
    $(".checkUser-div").removeClass("high-height");
});
/** 添加好友按钮 按下 */
$("body").on("click", ".add-user", function () {
    let id = $(this).parent().find(".checkUser-name").attr("i");
    let name = $(this).parent().find(".checkUser-name").text();

    success_show("已发送添加好友请求", 1500);
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/friend/addFriend",
        type: "POST",
        data: {"id": id, "nickName": name},
        datatype: "json",
        success: function (result) {
            if(result.code === 200){
                success_show(result.msg, 1500);
            }else {
                errtip_show(result.msg);
            }
        }
    });
});
/** 关闭任务卡片添加层 */
function closeMask() {
    var $mask = $(".card-mask");
    if (cardMask) {
        $mask.addClass("mask-hide");
        cardMask = !cardMask;

        setTimeout(function () {
            $("body").removeClass("overflow-hide");
        }, 310);
    }
    $("#cardForm input").each(function () {
        $(this).not(":submit").val("");
    });
    $(".checkUser-div").addClass("hide");
    $(".checkUser-div").removeClass("high-height");
    clearPic();
}

$(".close-mask").click(function () {
    closeMask();
});

/** 切换卡片页 */
var $cardPage = $(".on");
$(".card-tip-it").click(function () {
    $cardPage.removeClass("on");
    $cardPage = $(this);
   $(this).addClass("on");
});
/** 拦截添加任务层提交 */
$("#cardForm input[type='submit']").click(function (e) {
    let event = e || window.event;
    event.preventDefault(); // 兼容标准浏览器
    window.event.returnValue = false; // 兼容IE6~8

    if (!checkIsNull()) {
        errtip_show("标题和监督人不能为空");
        return;
    }
    //检查时间是否正确
    if (!minTime()) {
        return;
    }

    var fd = new FormData();
    fd.append("filePic", imgfile);
    fd.append("title", $("input[name='title']").val());
    fd.append("checkUserId", $("input[name='checkUserId']").val());
    fd.append("time", reTime($("input[name='time']").val()));
    fd.append("msg", $("textarea[name='msg']").val());
    fd.append("token", $("input[name='token']").val());

    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/card/addCard",
        type: "POST",
        data: fd,
        datatype: "json",
        processData: false,  // processData和contentType需设置为false
        contentType: false,
        async: true,
        success: function (result) {
            if (result.code === 200) {
                flag = true;
                success_show(result.msg, 3000);
                closeMask();
                window.location.replace("/")
            } else {
                flag = false;
                errtip_show(result.msg);
            }
        }
    });

    console.log("拦截成功");
});
/** 重新加载卡片，除新增卡片 */
function loadCardNotPlus() {
    
}
function reTime(obj) {

    return obj.replace(/T/g, " ");
    //document.getElementById('date1').value = obj.value.replace(/T/g, " ").replace(/\.[\x00-\xff]*/g, '');
}

/** 删除卡片 */

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
/** 点击用户组下个人中心链接 */
$(".user-space").parent("a").click(function () {
    window.location.replace("http://localhost:9400/xzzj/user_details");
});


/** 获取自己的卡片信息 */
function getMyCard() {
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/card/getMyCard",
        type: "GET",
        datatype: "json",
        success: function (result) {
            if (result.code === 200) {

                for (let i = 0; i < result.data.length; i++) {
                    let card = result.data[i];
                    cardInit(card.id, card.time, card.title, card.msg, card.pic);
                }

            } else {
                // errtip_show(result.msg);
            }
        }
    })
}
/** 点击通知文字展开与隐藏 */
$(".ct-msg").click(function () {
    if($(this).hasClass("text-hide")) {
        $(this).removeClass("text-hide");
    }else {
        $(this).addClass("text-hide");
    }
});
/** 引用login.js 的方法 */
/** 提示框 */
function errtip_show(text) {
    var $t_box = $("<div class='t-box t-box-flash'>" + text + "</div>");
    $("body").append($t_box);
    $t_box.animate({"z-index": 99}, 3000, function () {
        $t_box.remove()
    });
    $t_box.click(function () {
        $t_box.remove()
    })
}

function success_show(text, time) {
    var $t_box = $("<div class='t-box'>" + text + "</div>");
    $t_box.css("background", "#07a707");
    $("body").append($t_box);
    $t_box.animate({"z-index": 99}, time, function () {
        $t_box.remove()
    });
    $t_box.click(function () {
        $t_box.remove()
    })
}