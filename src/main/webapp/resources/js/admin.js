let adminAjaxUrl =  ctx + "/api/restaurant/";

$(document).ready(function () {
    initContext({
        ajaxUrl: adminAjaxUrl,
        datatableApi: $('#datatable').DataTable({
            "select": true,
            "sAjaxSource": adminAjaxUrl,
            "sAjaxDataProp": "",
            "aoColumns": [
                {"data": "name"},
                {"data": "address"},
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

    $(".modal-body #restaurant").val(currentObj.name);
    $(".modal-body #address").val(currentObj.address);
    $(".modal-body #restaurantId").val(currentObj.id);
    $('#mainForm').modal();
};

$('#createId').on('click', function () {
    $('#mainForm').modal();
});

function remove(obj) {
    $.ajax({
        type: 'DELETE',
        url: adminAjaxUrl + $(obj).attr('id')
    }).done(function () {
        updateTable();
    });
};

$('#saveId').on('click', function () {
    let id = $("#restaurantId").val();
    let name = $("#restaurant").val();
    let address = $("#address").val();

    if (id != null && id !== "") {
        $.ajax({
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            url: adminAjaxUrl + id,
            data: JSON.stringify({
                name: name,
                address: address
            })
        }).done(function () {
            updateTable();
        });
    } else {
        $.ajax({
            type: 'POST',
            url: adminAjaxUrl,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                name: name,
                address: address
            })
        }).done(function () {
            updateTable();
        });
    }
});