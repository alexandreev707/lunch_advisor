$("#loginId").click(function (event) {
    $('#loginModal').modal('show');
});

$('#registerId').on('click', function () {
    $('#registrationModalId').modal();
});

$('#saveId').on('click', function () {
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        url:  ctx + "api/user/register/",
        data: JSON.stringify({
            name: $("input[name='name']").val(),
            email: $("input[name='email']").val(),
            password: $("#pwd").val(),
        })
    });
});
