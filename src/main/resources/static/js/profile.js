$(document).ready(function() {
    $(document).delegate("#buttonChangeMail", "click", changeMail);
    $(document).delegate("#new_password", "keyup", checkPassword);
    $("#submitNewPassword").attr('disabled', true);
});

function changeMail() {
    var formData = $('#formChangeMail').serialize();
    $.ajax({
        url: '/customer/changemail',
        type: 'POST',
        data: formData,
        success: function (res) {
            toastr.success(res.body, {timeOut: 5000});
            document.location.href = "/customer";
        },
        error: function (res){
            if (res.status == 400) {
                toastr.error(res.body, {timeOut: 5000});
            } else {
                console.log(res.status);
            }
            document.location.href = "/customer";
        }
    });
}

function close() {
    $('#openNewMailModal').hide();
    $(".modal-backdrop.show").hide();
}

function successChangePass() {
    toastr.success('Пароль успешно изменен!', {timeOut: 5000})

}

function checkPassword(){
    regularExpression = new RegExp("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{8,}");
    newPassword = document.getElementById("new_password").value;
    if(regularExpression.test(newPassword)) {
        $("#submitNewPassword").attr('disabled', false);
    }
}