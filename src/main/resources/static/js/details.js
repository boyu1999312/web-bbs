/** 函数执行 */

/** 点击用户头像后 */
$(".user-portrait img").click(function (e) {
    var text = ["你点我干嘛？", "你是不是脑壳进水了？", "我看你今天是吃了枪药。",
                "你个呆头呆脑滴！", "你再点？", "你再点看我不打你屁屁！"];
    var rand = parseInt(Math.random() * (5 - 0 + 1) + 0);
    var x = e.pageX;
    var y = e.pageY;
    var $ab = $("<h1></h1>").text(text[rand]);
    $ab.css({
       "position": "absolute",
        "top": x - 30,
        "left": y - 100,
        "background": "white",
    });
    $("body").append($ab);
    $ab.animate({"top":y-150, "opacity":0},1000,function () {$ab.remove();})
});