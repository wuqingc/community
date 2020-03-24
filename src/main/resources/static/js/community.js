function post() {
    var questionId = $("#question_id").val();
    var content = $(".comment").val();
    var category = {"parentId": questionId, "type": 1,"content": content};
    var jsonData = JSON.stringify(category);

    if (!content) {
        alert("不能回复空内容哦.");
        return;
    }

    $.ajax({
        type: "post",
        url: "/comment",
        data: jsonData,
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            if (result.code === 200){
                alert("评论成功.");
                window.location.reload();
            } else {
                if (result.code === 2003) {
                    var accept = confirm(result.message);
                    if (accept) {
                        window.open("https://github.com/login/oauth/authorize?client_id=382e5710bc068c24c967&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closeable",true);
                    }
                }
                if (result.code === 2007) {
                    alert(result.message);
                }
            }
        }
    });
}