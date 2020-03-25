function post() {
    var questionId = $("#question_id").val();
    var content = $(".comment").val();
    comment2Target(questionId,1,content);
}

function comment2Target(targetId,type,content) {
    if (!content) {
        alert("不能回复空内容哦.");
        return;
    }

    var category = {"parentId": targetId, "type": type,"content": content};
    var jsonData = JSON.stringify(category);

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

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    console.log(commentId + "   " + content);
    comment2Target(commentId,2,content);
}


function collapseComments(e) {
    var id = e.getAttribute("data-id");
    Date.prototype.format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "H+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };

    $.getJSON("/comment/"+id,function (data) {
        var listInfo="";
        $.each(data.data,function (){
            var now_date = new Date(this.gmtCreate).format("yyyy-MM-dd");
            listInfo += "<div class=\"media media-comment\" id='media-comment'>\n" +
                "             <div class=\"media-left\" >\n" +
                "                  <a href=\"#\">\n" +
                "                       <img class=\"media-object img-rounded\" src=" + this.user.avatarUrl + ">\n" +
                "                                        </a>\n" +
                "                                    </div>\n" +
                "                                    <div class=\"media-body\">\n" +
                "                                        <h5 class=\"media-heading\">\n" +
                "                                            <span>" + this.user.name +"</span>\n" +
                "                                        </h5>\n" +
                "                                        <span>" + this.content +"</span>\n" +
                "                                    </div>\n" + "<span class=\"text-desc pull-right\">"+ now_date + "</span>" +
                "                                </div>";
        });
        $("#comment-" + id)[0].innerHTML = listInfo;
    })
}

function tagVerify() {
    var tag = $("#tag").val();
    var description = $("#description").val();
    var title = $("#title").val();
    var category = {"title":title, "description": description,"tag": tag};
    //将JSON对象序列化为字符串.
    var jsonData = JSON.stringify(category);

    $.ajax({
        type: "post",
        url: "/publish/verify",
        data: jsonData,
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            if (result.error) {
                alert(result.error);
            }
        }
    });
}

function showSelectTag() {
    $("#tag-div").show();
    $(".tag-name:first").addClass("active");
    $(".tag-info:first").addClass("active");
}

function selectTag(e) {
    var val = e.getAttribute("data-id");
    var tag = $("#tag").val();
    if (tag.indexOf(val) === -1) {
        if (tag) {
            $("#tag").val(tag + "," + val);
        } else {
            $("#tag").val(val);
        }
    } else {
        alert("请勿重复添加.")
    }
}