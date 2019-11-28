var codeCache = "XZZJ_CODECACHE_" + randomCoding();
var forgetCodeCache = "FORGET_CODECACHE_" + randomCoding();
var forget = false;     //false非忘记密码页
var register = false;   //false为登录页，true为注册页
var reUserName = false; //false为用户名重复
var reCode = false;     //false为验证码不正确
var reEmail = false;    //false为邮箱重复

/** 生成认证码 */
function randomCoding(){
    //创建26个字母数组
    var arr = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
    var idvalue ='';
    var n = 4;
    for(var i=0;i<n;i++){
        idvalue+=arr[Math.floor(Math.random()*26)];
    }
    return idvalue;
}

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

function success_show(text, time){
    var $t_box = $("<div class='t-box'>"+text+"</div>");
    $t_box.css("background","#07a707");
    $("body").append($t_box);
    $t_box.animate({"z-index":99},time,function () {$t_box.remove()});
    $t_box.click(function () {$t_box.remove()})
}
/** 侧边提示框 */
function right_tip($el, text, err){
    backgroundColor = err ? "#07a707" : "red";
    $el.text(text);
    $el.css({"z-index":1,"opacity":1,"background":backgroundColor});

}

/** 校验电子邮箱 */
function email_check($el, $tip){
    var email = $el.val();
    if(email === "" || email == null) {
        right_tip($tip, "邮箱不可用", false);
        return true;
    }
    var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
    if(!reg.test(email)){
        right_tip($tip, "邮箱不可用", false);
        errtip_show("电子邮箱格式不正确");
        return true
    }
    return false
}


/** input交互 */
$("input").blur(function () {
    var $val = $(this);
    tip_show($val, $val.prev())

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

/** 切换登录注册页 */

$(".tp-register").click(function () {
    register = true;
    $(this).css("color", "#1a1a1a");
    $(".tp-login").css("color", "#4C4C4C");
    $(".bot-div").css("left", "63px");


    $(".lbtn").css("top","525px").val("注册");
    $(".box").css("height","613px");
    $(".once").css({"z-index":-1,"opacity":0});
    $(".d-pwd").css({"z-index":1,"opacity":1});
    $("title").text("小猪之家|注册");
    $(".notice").css({"display":"block"})
        .text("如果找不到邮件请在垃圾箱里找一下，验证码时效为 5 分钟");
});
$(".tp-login").click(function () {

    register = false;
    $(".lbtn").css("top","262px").val("登录");
    $(".box").css("height","350px");


    $(this).css("color", "#1a1a1a");
    $(".tp-register").css("color", "#4C4C4C");
    $(".bot-div").css("left", "0");
    $(".once").css({"z-index":1,"opacity":1});
    $(".d-pwd").css({"z-index":-1,"opacity":0});
    $("title").text("小猪之家|登录");
    $(".notice").css({"display":"none"})
        .text("");
});
$(".forget-btn").click(function () {
    forget = true;
    $(".once").css({"z-index":-1,"opacity":0});
    $(".d-forget").css({"z-index":1,"opacity":1});
    $(".lbtn").css("z-index","2").val("修改密码");
    $(".per").css({"z-index":-1,"opacity":0});
    $("title").text("小猪之家|忘记密码");

});
$(".d-forget .tp-tl").click(function () {
    forget = false;
    $(".once").css({"z-index":1,"opacity":1});
    $(".d-forget").css({"z-index":-1,"opacity":0});
    $(".lbtn").css("z-index","1").val("登录");
    $(".per").css({"z-index":1,"opacity":1});
    $("title").text("小猪之家|登录");
});

/** 检查用户名、邮箱和验证码 */

function checkUserName($el){
    var reg = /[\u4e00-\u9fa5]+/;
    if($el.val() === "" || $el.val() == null) {
        right_tip($(".checkAcc"), "用户名不可用", false);
        return false;
    }
    if(reg.test($el.val()) |
        $el.val().length < 5){
        errtip_show("请输入至少 5 位，非中文的用户名");
        right_tip($(".checkAcc"), "用户名不可用", false);
        return false
    }
    return true
}
function checkReUserName(flag){
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/checkUserName",
        type: "POST",
        data: {"userName": $("input[name='acc']").val()},
        datatype: "json",
        async: flag,
        success: function (result) {
            $(".checkAcc").text(result.msg);
            if (result.code === 200) {
                reUserName = true;
                $(".checkAcc").css({"z-index":1,"opacity":1, "background": "#07a707"})
            } else {
                reUserName = false;
                $(".checkAcc").css({"z-index":1,"opacity":1, "background": "red"})
            }
        }
    });
}
$("input[name='acc']").blur(function () {
    if(checkUserName($("input[name='acc']"))){
        checkReUserName(true)
    }
});
function checkReEmail(flag, url, $email, $tip){
    $.ajax({
        url: url,
        type: "POST",
        data: {"email": $email.val()},
        datatype: "json",
        async: flag,
        success: function (result) {
            $tip.text(result.msg);
            if(result.code === 200){
                reEmail = true;
                $tip.css({"z-index":1,"opacity":1, "background": "#07a707"})
            }else {
                reEmail = false;
                $tip.css({"z-index":1,"opacity":1, "background": "red"})
            }
        }
    });
}
$("input[name='email']").blur(function () {
    if(!email_check($("input[name='email']"),$(".checkEmail"))){
        var url = "http://localhost:9400/xzzj/bbs/account/checkEmail";
        checkReEmail(true, url, $(this), $(".checkEmail"));
    }
});
$("input[name='forgetEmail']").blur(function () {
    if(!email_check($("input[name='forgetEmail']"),$(".checkForgetEmail"))){
        var url = "http://localhost:9400/xzzj/bbs/account/emailExists";
        checkReEmail(true, url, $(this), $(".checkForgetEmail"));
    }
});
$("input[name='code']").blur(function () {
    checkCode(true, codeCache, $("input[name='code']"),
        $("input[name='email']"), $(".checkCode"))
});
function checkCode(flag, codeCache, $inputCode, $inputEmail, $checkTip){
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/checkCode",
        type: "POST",
        data: {"codeCache": codeCache,
            "code": $inputCode.val(),
            "email": $inputEmail.val()},
        datatype: "json",
        async: flag,
        success: function (result) {
            $checkTip.text(result.msg);
            if (result.code === 200) {
                reCode = true;
                $checkTip.css({"z-index":1,"opacity":1, "background": "#07a707"});
                $inputEmail.attr("readonly", "readonly")
            } else {
                reCode = false;
                $checkTip.css({"z-index":1,"opacity":1, "background": "red"})
            }
        }
    });
}

/** 验证码按钮倒计时 */
function countDown($el){
    var count = 60;
    var timer = null;
    $el.text(count+" s");
    $el.attr("disabled",true);
    $el.css("cursor","no-drop");
    var timer = setInterval(function () {
        if(--count > 0){
            $el.text(+count+" s")
        }else {
            $el.text("重新获取验证码");
            $el.attr("disabled",false);
            $el.css("cursor","pointer");
            clearInterval(timer)
        }
    },1000)
}

/** 获取验证码按钮被按下 */
$(".get-code").click(function () {
    if(tip_show($("input[name='email']"), $(".reg-email")) ||
        email_check($("input[name='email']"), $(".checkEmail"))){return}
    var url = "http://localhost:9400/xzzj/bbs/account/checkEmail";
    checkReEmail(false, url, $("input[name='email']"), $(".checkEmail"));
    if(!reEmail){return}

    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/getCode",
        type: "GET",
        data: {"codeCache": codeCache,
            "email": $("input[name='email']").val(),
            "acc": $("input[name='acc']").val()},
        datatype: "json",
        success: function (result) {
            if (result.code === 200) {
                console.log(result.data);
                $("input[name='token']").val(result.data);
            } else {
                $(".checkCode").text(result.msg);
            }
        }
    });
    $(".checkCode").text("邮箱验证码已发送");
    $(".checkCode").css({"z-index":1,"opacity":1, "background": "#07a707"});

    countDown($(".get-code"));

});

/** 忘记密码页 获取验证码被按下 */
$(".get-forget-code").click(function () {
    if(tip_show($("input[name='forgetEmail']"), $(".d-forget .in")) ||
        email_check($("input[name='forgetEmail']"), $(".checkForgetEmail"))){return}
    var url = "http://localhost:9400/xzzj/bbs/account/emailExists";
    checkReEmail(false, url, $("input[name='forgetEmail']"), $(".checkForgetEmail"));
    if(!reEmail){return}

    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/getCode",
        type: "GET",
        data: {"codeCache": forgetCodeCache,
            "email": $("input[name='forgetEmail']").val(),
            "acc": $("input[name='acc']")},
        datatype: "json",
        success: function (result) {
            if (result.code === 200) {
                console.log(result.data)
            } else {
                $(".checkForgetCode").text(result.msg);
            }
        }
    });
    $(".checkForgetCode").text("邮箱验证码已发送");
    $(".checkForgetCode").css({"z-index":1,"opacity":1, "background": "#07a707"});

    countDown($(".get-forget-code"));
});
/** 忘记密码页 检查验证码 */
$("input[name='forgetCode']").blur(function () {
    checkCode(true, forgetCodeCache, $("input[name='forgetCode']"),
        $("input[name='forgetEmail']"), $(".checkForgetCode"))
});
/** 注册ajax */
function submit(url){
    var flag = false;
    $.ajax({
        url: url,
        type: "POST",
        data:
            {
                "accEmail": $("input[name='accEmail']").val(),
                "lPwd": $("input[name='lPwd']").val(),
                "acc": $("input[name='acc']").val(),
                "email": $("input[name='email']").val(),
                "code": $("input[name='code']").val(),
                "pwd": $("input[name='pwd']").val(),
                "gender": $("input[name='gender']:checked").val(),
                "codeCache": codeCache,
                "token": $("input[name='token']").val()
            },
        datatype: "json",
        async: false,
        success: function (result) {
            if (result.code === 200) {
                flag = true;
            } else {
                errtip_show(result.msg);
                flag = false;
            }
        }
    });
    return flag
}

/** 拦截表单 */
$('#login-form input[type="submit"]').click(function (e) {
    let event = e || window.event;
    event.preventDefault(); // 兼容标准浏览器
    window.event.returnValue = false; // 兼容IE6~8
    var reg = /[\u4e00-\u9fa5]+/;
    var pwdReg = /^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[a-zA-Z0-9]+$/;

    if(forget){
        var flag = false;
        $.ajax({
            url: "http://localhost:9400/xzzj/bbs/account/forgetPassword",
            type: "POST",
            data:
                {
                    "email": $("input[name='forgetEmail']").val(),
                    "codeCache": forgetCodeCache,
                    "code":$("input[name='forgetCode']").val()
                },
            datatype: "json",
            async: false,
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
        if(flag){
            setTimeout(function () {
                window.location.replace("http://localhost:9400/xzzj/login");
            }, 3000)
        }
        return
    }

    if(register){
        /** 注册流程 */
        /** 检查输入是否合法 */
        if(tip_show($("input[name='acc']"),$(".reg-acc")) |
            tip_show($("input[name='email']"), $(".reg-email")) |
            tip_show($("input[name='pwd']"), $(".pwd")) |
            tip_show($("input[name='rePwd']"), $(".repwd")) |
            email_check($("input[name='email']"),$(".checkEmail"))){
            return
        }else {

            if(!$("input[type='radio']").is(':checked')){
                errtip_show("请选择性别");
                return
            }
            if(!checkUserName($("input[name='acc']"))){
                return
            }
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
        }
        $("input[name='codeCache']").val(codeCache);

        //重新验证是否填写正确
        var url = "http://localhost:9400/xzzj/bbs/account/checkEmail";
        checkReEmail(true, url, $("input[name='email']"), $(".checkEmail"));
        checkReUserName(false);
        checkCode(true, codeCache, $("input[name='code']"),
            $("input[name='email']"), $(".checkCode"));

        if(reUserName && reCode && reEmail){
            success_show("正在注册，请稍等", 5000);
            if(submit("http://localhost:9400/xzzj/bbs/account/register")){
                $(".t-box").remove();
                success_show("注册成功，一秒后跳转",1500);
                setTimeout(function () {
                    window.location.replace("http://localhost:9400/");
                }, 1500)
            }
        }

    }else {
        /** 登录流程 */
        /** 检查输入是否合法 */
        if(tip_show($(".ac-in"),$(".in")) |
            tip_show($(".ac-code"), $(".code"))){
            return
        }else if(reg.test($("input[name='accEmail']").val())){
            errtip_show("请不要使用中文字符哦~");
            return
        }
        if(submit("http://localhost:9400/xzzj/bbs/account/login")) {
            window.location.replace("http://localhost:9400");
        }
    }
});