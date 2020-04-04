let menuAjaxUrl = "/api/menu/";
let modalData;

$(document).ready(function () {
    initContext({
        ajaxUrl: menuAjaxUrl,
        datatableApi: $('#datatable').DataTable({
            "select": true,
            "paging": false,
            "info": true,
            "async": false,
            "sAjaxSource": menuAjaxUrl + 'byDate',
            "sAjaxDataProp": "",
            "columns": [
                {"data": "restaurant"},
                {"data": "menu"},
                {"data": "date"},
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

function filter() {
    $.ajax({
        url: menuAjaxUrl + 'byDate',
        type: 'GET',
        async: false,
        data: {
            start: $("#startDate").val(),
            end: $("#endDate").val(),
            restaurant: $("#restaurantSelectId").val()
        },
        success: function (data) {
            $('#datatable').DataTable().clear().rows.add(data).draw();
        }
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(context.ajaxUrl, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

$('#addItemId').on('click', function () {
    $('#modalDataTable').DataTable().row.add({"name": '', "price": '0'}).draw();
});

function edit(obj) {
    $('#modalDataTable').DataTable().clear().draw()
    $('#modalForm')[0].reset();

    let currentObj = $('#datatable').DataTable()
        .row(obj.closest('tr'))
        .data();

    let menuId = currentObj.id;

    $(".modal-body #menu").val(currentObj.menu);
    $(".modal-body #menuId").val(menuId);
    $(".modal-body #restaurant").val(currentObj.restaurant);
    $(".modal-body #restaurant").attr('readonly', true);
    $(".modal-body #date").val(currentObj.date);

    modalData = $('#modalDataTable').DataTable({
        "paging": false,
        "searching": false,
        "destroy": true,
        "sAjaxSource": "/api/item/menu/" + menuId,
        "sAjaxDataProp": "",
        "columns": [
            {
                "data": "name",
                "render": function (data, type, row) {
                    return '<input name="name" type=\'text\' value="' + row.name + '">';
                }
            },
            {
                "data": "price",
                "render": function (data, type, row) {
                    return '<input name="price" type=\'number\' value="' + row.price + '">';
                }
            },
            {
                "data": null,
                "render": function (data, type, row) {
                    return '<button type="button" id="' + row.id + '" onclick="removeRow(this)">' +
                        '<span class="fa fa-remove"></span></button>'
                }
            }
        ],
        "order": [[0, "asc"]]
    });

    $('#modalDataTable').DataTable().on('change', 'input', function () {
        let cell = $(this).closest('td');
        $(this).attr('value', $(this).val());
        modalData.cell($(cell)).invalidate('dom').draw();
    });

    $('#mainForm').modal();
};

function remove(obj) {
    $.ajax({
        type: 'DELETE',
        url: menuAjaxUrl + $(obj).attr('id')
    }).done(function () {
        updateTable();
    });
};

function removeRow(obj) {
    modalData.row(obj.closest('tr')).remove().draw();
};

function create() {
    $('#modalDataTable').DataTable().clear().draw()
    $('#modalForm')[0].reset();
    $(".modal-body #menuId").val(null);
    $(".modal-body #restaurant").attr('readonly', false);
    $('#mainForm').modal();
};

$('#saveId').on('click', function () {
    let menu = $("#menu").val();
    let menuId = $("#menuId").val();
    let date = $("#date").val();

    if (menuId != null && menuId !== "") {
        $.ajax({
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            url: menuAjaxUrl + menuId,
            async: false,
            data: JSON.stringify({
                name: menu,
                date: date,
                items: JSON.parse(JSON.stringify(getItems()))
            })
        }).done(function () {
            updateTable();
        });
    } else {
        $.ajax({
            type: 'POST',
            url: menuAjaxUrl,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            async: false,
            data: JSON.stringify({
                name: menu,
                date: date,
                restaurant: $(".modal-body #restaurant").val(),
                items: JSON.parse(JSON.stringify(getItems()))
            })
        }).done(function () {
            updateTable();
        });
    }
});

function getItems() {
    let items = [];
    $('#modalDataTable tr:gt(0)').each(function () {
        let itemId = $(this).find('button[name$="remove"]').attr('id');
        let name = $(this).find('input[name$="name"]').val();
        let price = $(this).find('input[name$="price"]').val();
        if (!isEmpty(name, price)) {
            items.push({
                id: itemId,
                name: name,
                price: price
            });
        }
    });
    return items;
}
