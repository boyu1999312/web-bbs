/** 函数执行 */
getUserInfo();


function getUserInfo() {
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/getUserInfo",
        type: "POST",
        datatype: "json",
        success: function (result) {
            if(result.code === 200){
                var userInfo = result.data;
                console.log(userInfo);
                $(".user-group").find("img").attr("src", userInfo.portrait);
                $(".div-pic").css({"background":"url('"+userInfo.portrait+"') no-repeat",
                            "background-size":"cover"});
            }else {
                console.log(result.msg)
            }
        }
    })
}

/** 更换头像 */
$("input[name='pic']").change(function (e) {

    if(!isPic($(this))){
        return;
    }

    let filePic = e.currentTarget.files[0];
    var objUrl = $(this)[0].files[0];
    //获得一个http格式的url路径:mozilla(firefox)||webkit or chrome
    var windowURL = window.URL || window.webkitURL;
    //createObjectURL创建一个指向该参数对象(图片)的URL
    var dataURL = windowURL.createObjectURL(objUrl);

    info_show("正在更换头像");
    var fd = new FormData();
    fd.append("filePic", filePic);
    $.ajax({
        url: "http://localhost:9400/xzzj/bbs/account/setPic",
        type: "POST",
        data: fd,
        datatype: "json",
        processData: false,  // processData和contentType需设置为false
        contentType: false,
        success: function (result) {
            if(result.code === 200){
                success_show(result.msg, 1500);
            }else {
                errtip_show(result.msg);
            }
        }
    });

    $(".div-pic").css({"background":"url('"+dataURL+"') no-repeat",
        "background-size": "cover"});

});


/** 判断是否为图片 */
function isPic($el){
    let path = $el.val();
    console.log(path);
    if (path.length === 0) {
        // errtip_show("请上传图片");
        return false;
    } else {
        var extStart = path.lastIndexOf('.'),
            ext = path.substring(extStart, path.length).toUpperCase();
        console.log(ext);
        if (ext !== '.PNG' && ext !== '.JPG' && ext !== '.JPEG' && ext !== '.GIF') {
            errtip_show("请选择正确格式的图片");
            return false;
        }
    }
    return true;
}
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
function info_show(text) {
    var $t_box = $("<div class='t-box'>" + text + "</div>");
    $t_box.css("background", "#eec500");
    $("body").append($t_box);
    $t_box.animate({"z-index": 99}, 1500, function () {
        $t_box.remove()
    });
    $t_box.click(function () {
        $t_box.remove()
    })
}