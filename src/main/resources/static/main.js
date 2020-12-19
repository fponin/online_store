$(document).ready(function () {
    const tableBody = document.querySelector("#user-table > tbody");

    $(function () {
        $.ajax(
            {
                type: "GET",
                url: '/api/users/',
                dataType: "json",
                success: function (users) {
                    $.each(users, function (i, user) {

                        $(tableBody).append($('<tr id="userRowId' + user.id + '">')
                            .append($("<td>").append(user.id))
                            .append($("<td>").append(user.email))
                            .append($("<td>").append(user.authorities.map(role => role.authority).join(" ")))

                            .append($("<td>").append("<button class='btn btn-info updateBtn'>Edit</button>").click(function (event) {
                                $(".alert").alert('close');
                                $('.update-example #updateId').val(user.id);
                                $('.update-example #updateEmail').val(user.email);
                                $('.update-example #updateModal').modal('show');
                                $('#updateRoles option').each(function () {
                                    if (user.roles.map(role => role.name).includes($(this).val())) {
                                        $(this).prop('selected', true);
                                    } else {
                                        $(this).prop('selected', false);
                                    }
                                })
                            }))

                            .append($("<td>").append("<button class='btn btn-danger deleteBtn'>Delete</button>").click(function (event) {
                                $('.delete-example #deleteId').val(user.id);
                                $('.delete-example #deleteEmail').val(user.email);
                                $('.delete-example #deletePassword').val(user.password);
                                $('.delete-example #deleteModal').modal('show');
                            })));
                    })
                }
            });
    });


    $(function () {

        $('#addBtn').on('click', function () {
            $(".alert").alert('close');
            var email = $('#addEmail').val();
            var password = $('#addPassword').val();
            var roles = $('#addRoles').val();
            var user = {email, password, roles};
            $.ajax(
                {
                    type: 'POST',
                    url: '/api/users/',
                    dataType: "json",
                    data: JSON.stringify(user),
                    contentType: "application/json; charset=utf-8",
                    success: function (data) {

                        console.log(data);

                        $('#table-body').append($('<tr id="userRowId' + data.id + '">')
                            .append($("<td>").append(data.id))
                            .append($("<td>").append(data.email))
                            .append($("<td>").append(data.authorities.map(role => role.authority).join(" ")))

                            .append($("<td>").append("<button class='btn btn-info updateBtn '>Edit</button>").click(function (event) {
                                $('.update-example #updateId').val(data.id);
                                $('.update-example #updateEmail').val(data.email);
                                $('.update-example #updateModal').modal('show');
                            }))

                            .append($("<td>").append("<button class='btn btn-danger deleteBtn'>Delete</button>").click(function (event) {
                                $('.delete-example #deleteId').val(data.id);
                                $('.delete-example #deleteEmail').val(data.email);
                                $('.delete-example #deletePassword').val(data.password);
                                $('.delete-example #deleteModal').modal('show');
                            })));
                        $('.nav-tabs a[href="#nav-home"]').tab('show');
                    },
                    error: function (data) {
                        handleError(data, '#add-email-form-group');
                    }
                });
        });
    });

    $(function () {


        $('#updateSubmitBtn').on('click', function () {
            $(".alert").alert('close');
            var id = $('#updateId').val();
            var email = $('#updateEmail').val();
            var password = $('#updatePassword').val();
            var roles = $('#updateRoles').val();

            var user = {id, email, password, roles};

            $.ajax({
                type: 'PUT',
                url: '/api/users/',
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(user),
                success: function (data) {

                    $(function updateFunc() {
                        $('#editBtn').on('click', function () {
                            $(".alert").alert('close');
                            $('.update-example #updateId').val(data.id);
                            $('.update-example #updateEmail').val(data.email);
                            $('.update-example #updateModal').modal('show');
                        });
                    });

                    $(function deleteFunc() {
                        $('#deleteBtn').on('click', function () {
                            $('.delete-example #deleteId').val(data.id);
                            $('.delete-example #deleteEmail').val(data.email);
                            $('.delete-example #deletePassword').val(data.password);
                            $('.delete-example #deleteModal').modal('show');
                        });
                    });

                    var new_row = `
                        <tr id="userRowId${data.id}">
                            <td>${data.id} </td>
                            <td>${data.email} </td>
                            <td>${data.authorities.map(role => role.authority).join(" , ")} </td>
                            <td> <button class='btn btn-info' id='editBtn'>Edit</button></td>
                            <td> <button class='btn btn-danger' id='deleteBtn'>Delete</button></td>
                        </tr>`;

                    $('#userRowId' + id).replaceWith(new_row);
                    $('#updateModal').modal('hide');
                },
                error: function (data) {
                    handleError(data, '#update-email-form-group');
                }
            });
        });
    });

    $(function () {
        $('#deleteSubmitBtn').on('click', function () {
            var $deleteId = $('#deleteId').val();
            $.ajax(
                {
                    type: 'DELETE',
                    url: '/api/users/' + $deleteId,
                    contentType: "application/json; charset=utf-8",
                    success: function () {
                        $('#userRowId' + $deleteId).remove();
                    },
                    error: function (jqXhr, textStatus, errorThrown) {
                        console.log(errorThrown);
                    }
                });

        })
    });


    $(function () {
        $.ajax(
            {
                type: "GET",
                url: '/api/roles',
                dataType: "json",
                success: function (roles) {
                    for (let i = 0; i < roles.length; i++) {

                        $("#updateRoles").append('<option value=' + roles[i].authority + '>' + roles[i].authority + '</option>');
                        $("#deleteRoles").append('<option value=' + roles[i].authority + '>' + roles[i].authority + '</option>');
                        $("#addRoles").append('<option value=' + roles[i].authority + '>' + roles[i].authority + '</option>');
                    }
                },
                error: function (jqXhr, textStatus, errorThrown) {
                    console.log(errorThrown);
                }

            });
    });


});

function handleError(data, element) {
    $(element).append("<div class='alert alert-danger alert-dismissible fade show' role='alert'>" +
        "<strong>Error:</strong>&nbsp;" + data.responseJSON.message +
        "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
        "<span aria-hidden='true'>&times;</span>" +
        "</button>" +
        "</div>");
}
