function post() {
    var questionId = $("#question_id").val();
    var content = $(".comment").val();
    var category = {"parentId": questionId, "type": 1,"content": content};
    var jsonData = JSON.stringify(category);

    $.ajax({
        type: "post",
        url: "/comment",
        data: jsonData,
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (result) {
            console.log(result);
        }
    });
}