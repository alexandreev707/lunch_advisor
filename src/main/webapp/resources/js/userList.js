let userListAjaxUrl = "/api/user/";

$(document).ready(function () {
    initContext({
        ajaxUrl: userListAjaxUrl,
        datatableApi: $('#datatable').DataTable({
            "sAjaxSource": userListAjaxUrl,
            "sAjaxDataProp": "",
            "aoColumns": [
                {"data": "name"},
                {"data": "email"},
                {"data": "enabled"},
                {
                    "render": renderEditBtn,
                    "defaultContent": "",
                    "orderable": false
                },
                {
                    "render": renderDeleteBtn,
                    "defaultContent": "",
                    "orderable": false
                }
            ],
            "order": [[0, "asc"]]
        })
    });
});

function edit(obj) {
    let currentObj = $('#datatable').DataTable()
        .row(obj.closest('tr')).data();

    let userId = currentObj.id;

    $(".modal-body #userId").val(userId);
    $(".modal-body #user").val(currentObj.name);
    $(".modal-body #email").val(currentObj.email);
    if (currentObj.roles) {
        $(".modal-body #roleListId").val(currentObj.roles.toString());
    } else {
        $(".modal-body #roleListId").val('');
    }
    if (currentObj.enabled)
        $(".modal-body #enabled").prop('checked', true);

    $('#modalDataTable').DataTable({
        "paging": false,
        "searching": false,
        "destroy": true,
        "sAjaxSource": "/api/review/user/" + userId,
        "sAjaxDataProp": "",
        "columns": [
            {
                "data": "menu"
            },
            {
                "data": "dateTime"
            },
            {
                "data": "state"
            }
        ],
        "order": [[0, "asc"]]
    });

    $('#mainForm').modal();
};

$('#createId').on('click', function () {
    $('#mainForm').modal();
});

function changeRoles() {
    let roles = $('#roleListId').val();
    let selectedRoles = $('#rolesSelectId').val();

    if (roles && selectedRoles && !roles.toLowerCase().includes(selectedRoles.toLowerCase()))
        $(".modal-body #roleListId").val(selectedRoles);
}

function getRoles() {
    let roles = [];
    $('#roleListId li').each(function () {
        roles.push({role: $(this).text()});
    });
    return roles;
}

function remove(obj) {
    $.ajax({
        type: 'DELETE',
        url: userListAjaxUrl + $(obj).attr('id')
    }).done(function () {
        updateTable();
    });
};

$('#saveId').on('click', function () {
    let userId = $("#userId").val();
    let name = $("#user").val();
    let email = $("#email").val();
    let enabled = $('#enabled').prop("checked");
    let roles = $("#roleListId").val().split(',');

    if (userId != null && userId !== "") {
        $.ajax({
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            url: userListAjaxUrl + userId,
            data: JSON.stringify({
                id: userId,
                name: name,
                email: email,
                enabled: enabled,
                roles: roles
            })
        }).done(function () {
            updateTable();
        });
    } else {
        $.ajax({
            type: 'POST',
            url: userListAjaxUrl,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                name: name,
                email: email,
                enabled: enabled,
                roles: roles
            })
        }).done(function () {
            updateTable();
        });
    }
});
