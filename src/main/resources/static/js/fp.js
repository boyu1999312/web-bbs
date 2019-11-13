/** 为空提示 */
function tip_show($val, $tip){
    if($val.val() == null || $val.val() === ""){
        $tip.css("z-index", 1);
        $tip.css("opacity", 1);
        return true
    }
}

/** 提示框 */
function errtip_show(text){
    var $t_box = $("<div class='t-box t-box-flash'>"+text+"</div>");
    $("body").append($t_box);
    $t_box.animate({"z-index":99},3000,function () {$t_box.remove()});
    $t_box.click(function () {$t_box.remove()})
}

function success_show(text){
    var $t_box = $("<div class='t-box'>"+text+"</div>");
    $t_box.css("background","#07a707");
    $("body").append($t_box);
    $t_box.animate({"z-index":99},3000,function () {$t_box.remove()});
    $t_box.click(function () {$t_box.remove()})
}




/** input交互 */
$("input").blur(function () {
    tip_show($(this), $(this).prev())

});

$(".error-tip").click(function () {
    $(this).css("z-index", -1);
    $(this).css("opacity", 0);
    $(this).next().focus()
});
$("input").focus(function () {
    $(this).prev().css("z-index", -1);
    $(this).prev().css("opacity", 0);
});

/** 密码显示与隐藏 */
$(".eyes").click(function () {
    $(this).toggleClass("pwd-open");
    var $prev = $(this).prev();
    var type = $prev.attr("type") === "text" ? "password" : "text";
    $prev.attr("type", type);
});
/** 注册ajax */
function submit(url){
    $.ajax({
        url: url,
        type: "POST",
        data:
            {
                "pwd": $("input[name='pwd']").val(),
                "updatedTime": $("input[name='updatedTime']").val(),
                "head": $("input[name='head']").val(),
                "left": $("input[name='left']").val()
            },
        datatype: "json",
        async: false,
        success: function (result) {
            if (result.code === 200) {
                success_show(result.msg);
                setTimeout(function () {
                    window.location.replace("http://localhost:9400/xzzj/login");
                }, 3000)
            } else {
                errtip_show(result.msg);
            }
        }
    });
}

/** 拦截表单 */
$('#login-form input[type="submit"]').click(function (e) {
    let event = e || window.event;
    event.preventDefault(); // 兼容标准浏览器
    window.event.returnValue = false; // 兼容IE6~8
    var reg = /[\u4e00-\u9fa5]+/;
    var pwdReg = /^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[a-zA-Z0-9]+$/;
    $("input[name='updatedTime']").val(new Date());

    if(!pwdReg.test($("input[name='pwd']").val()) |
        $("input[name='pwd']").val().length < 10){
        errtip_show("请输入至少 10 位的英文数字混合密码");
        return
    }
    if($("input[name='pwd']").val() !==
        $("input[name='rePwd']").val()){
        errtip_show("重复密码与密码不一致");
        return
    }

    submit("http://localhost:9400/xzzj/bbs/account/updPassword");
});