$(document).ready(function () {
    $(document).delegate("#buttonChangeMail", "click", changeMail);
    $(document).delegate("#new_password", "keyup", checkPassword);
    $(document).delegate("#submitNewPassword", "click", changePass);
    $("#submitNewPassword").attr('disabled', true);
});

function changeMail() {

    var formData = $('#formChangeMail').serialize();
    $.ajax({
        url: '/customer/changemail',
        type: 'POST',
        data: formData,
        success: function (res) {
            toastr.success(res, {timeOut: 5000});
            close();
            $('#email_input').val($('#new_mail').val());
            $('#new_mail').val('');
        },
        error: function (res) {
            if (res.status === 400) {
                if (res.responseText === 'duplicatedEmailError') {
                    $(".messages-after-submit").text('Ошибка: Электронный адрес уже зарегистрирован');
                    toastr.error("Ошибка: Электронный адрес уже зарегистрирован", {timeOut: 5000});
                }
                if (res.responseText === 'notValidEmailError') {
                    $(".messages-after-submit").text('Ошибка: Не верно указана электронная почта');
                    toastr.error("Ошибка: Не верно указана электронная почта", {timeOut: 5000});
                }
            } else {
                console.log(res.status);
            }
        }
    });
    return false;
}

function changePass() {
    var formData = $('#formChangePass').serialize();
    console.log(formData);
    $.ajax({
        url: '/customer/change-password',
        type: 'POST',
        data: formData,
        success: function (res) {
            toastr.success("Пароль успешно изменен", {timeOut: 5000});
            close();
        },
        error: function (res) {
            console.log(res);
            if (res.responseText === "error_old_pass") {
                toastr.error("Текущий пароль не совпадает. Введите заново пароль.", {timeOut: 5000});
                $('#old_password').val('').focus();
            }
            if (res.responseText === "error_valid") {
                toastr.error("Ваш пароль должен состоять из 8-20 символов, содержать буквы и цифры и не должны содержать пробелов и смайлики.", {timeOut: 5000});
                $('#new_password').val('').focus();
            }
            if (res.responseText === "error_pass_len") {
                toastr.error("Ваш пароль должен состоять минимум из 8 символов.", {timeOut: 5000});
                $('#new_password').val('').focus();
            }
        }
    });
    return false;
}

function close() {
    $('#openNewMailModal').hide();
    $(".modal-backdrop.show").hide();
}

function successChangePass() {
    toastr.success('Пароль успешно изменен!', {timeOut: 5000})

}

function checkPassword() {
    regularExpression = new RegExp("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{8,}");
    newPassword = document.getElementById("new_password").value;
    if (regularExpression.test(newPassword)) {
        $("#submitNewPassword").attr('disabled', false);
    }
}