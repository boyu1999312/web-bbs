var cardMask = false; //false为弹出层消失
var imgfile; //存储图片数据
/** 执行函数 */
lunbo();
getUserInfo();

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
function daojishi() {
    $('.time').each(function () {
        $(this).countdown('2020/1/23 0:0:0', function (event) {
            $(this).html(event.strftime('%D天 %H:%M:%S'));
        });
    });
};
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
    }

    return;
    var $card = $("<div class='card'>" +
        "<div class='des'>" +
        "<h2 class='time'>19:24:12</h2>" +
        "<span class='time-des'>据与小阿姨的爱情纪念日还有...</span>" +
        "<pre class='more-des'>哎呀<br/>亲亲这只香小猪吧~<br/>嘻嘻！</pre>" +
        "<div class='btm-des'></div>" +
        "</div>" +
        "</div>");
    $(this).before($card);
    daojishi();
});

/** 关闭任务卡片添加层 */
$(".close-mask").click(function () {
    var $mask = $(".card-mask");
    if (cardMask) {
        $mask.addClass("mask-hide");
        cardMask = !cardMask;
        setTimeout(function () {
            $("body").removeClass("overflow-hide");
        }, 310);
    }
});

/** 检查表单项是否为空 */
function checkIsNull() {
    if ($("#cardForm input[name='cardTitle']").val().length !== 0 ||
        $("#cardForm input[name='cardTime']").val() !== null ||
        $("#cardForm input[name='cardMsg']").val().length !== 0) {
        return false
    }
    return true;
}

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
});
/** 监督人搜索 */
$(".checkUser-input").bind("input propertychange", function () {
    var $cdiv = $(".checkUser-div");
    if ($(this).val() === '') {
        $cdiv.addClass("hide");

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
                        var $user = $("<div class='checkUser'>" +
                            "<img class='checkUser-p' src='" + vo.portrait + "'>" +
                            "<span class='checkUser-name'>" + name + "</span>" +
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
$("body").on("click", ".checkUser", function () {
    var uname = $(".checkUser-name").text();
    $("input[name='checkUserName']").val(uname);
    $(".checkUser-div").addClass("hide");
});

/** 拦截添加任务层提交 */
$("#cardForm input[type='submit']").click(function (e) {
    let event = e || window.event;
    event.preventDefault(); // 兼容标准浏览器
    window.event.returnValue = false; // 兼容IE6~8

    // if(!checkIsNull()){
    //     alert("内容不能为空");
    // }
    var fd = new FormData();
    fd.append("pic", imgfile);
    fd.append("title", $("input[name='title']").val());
    fd.append("checkUserName", $("input[name='checkUserName']").val());
    fd.append("time", reTime($("input[name='time']").val()));
    fd.append("msg", $("input[name='msg']").val());
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
                success_show(result.msg, 3000)
            } else {
                flag = false;
                errtip_show(result.msg)
            }
        }
    });

    console.log("拦截成功");
});

function reTime(obj) {

    return obj.replace(/T/g, " ");
    //document.getElementById('date1').value = obj.value.replace(/T/g, " ").replace(/\.[\x00-\xff]*/g, '');
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
/** 点击用户组下个人中心链接 */
$(".user-space").parent("a").click(function () {
    window.location.replace("http://localhost:9400/xzzj/user_details");
});

function getUserInfo() {
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/getUserInfo",
        type: "POST",
        datatype: "json",
        success: function (result) {
            if (result.code === 200) {
                var userInfo = result.data;
                console.log(userInfo);
                $(".user-group").find("img").attr("src", userInfo.portrait);
            } else {
                console.log(result.msg)
            }
        }
    })
}


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