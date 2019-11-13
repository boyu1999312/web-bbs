var cardMask = false; //false为弹出层消失

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
        $mask.css(
            {
                "z-index": 5, "opacity": 1,
                "top": top, "left": left
            });

        cardMask = !cardMask;
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
        $mask.css({"z-index": -1, "opacity": 0});
        cardMask = !cardMask;
    }
});
function checkIsNull(){
    if($("#cardForm input[name='cardTitle']").val().length !== 0 ||
        $("#cardForm input[name='cardTime']").val() !== null  ||
        $("#cardForm input[name='cardMsg']").val().length !== 0 ){
        return false
    }
    return true;
}
/** 拦截添加任务层提交 */
$("#cardForm input[type='submit']").click(function (e) {
    let event = e || window.event;
    event.preventDefault(); // 兼容标准浏览器
    window.event.returnValue = false; // 兼容IE6~8

    // if(!checkIsNull()){
    //     alert("内容不能为空");
    // }
    $.ajax({
       url: "http://localhost:9400/xzzj/bbs/account/card/add",
        type: "POST",
        data:
            {
                "cardTitle": $("input[name='cardTitle']").val(),
                "cardTime": $("input[name='cardTime']").val(),
                "cardMsg": $("input[name='cardMsg']").val(),
                "cardPic": $("input[name='cardPic']").val()
            },
        datatype: "json",
        async: true,
        success: function (result) {
            if(result.code === 200){
                flag = true;
                success_show(result.msg, 3000)
            } else{
                flag = false;
                errtip_show(result.msg)
            }
        }
    });

    console.log("拦截成功");
});
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
function errtip_show(text){
    var $t_box = $("<div class='t-box t-box-flash'>"+text+"</div>");
    $("body").append($t_box);
    $t_box.animate({"z-index":99},3000,function () {$t_box.remove()});
    $t_box.click(function () {$t_box.remove()})
}

function success_show(text, time){
    var $t_box = $("<div class='t-box'>"+text+"</div>");
    $t_box.css("background","#07a707");
    $("body").append($t_box);
    $t_box.animate({"z-index":99},time,function () {$t_box.remove()});
    $t_box.click(function () {$t_box.remove()})
}