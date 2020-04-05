let userAjaxUrl = ctx + "/api/user/";

$(document).ready(function () {
    initContext({
        ajaxUrl: userAjaxUrl + "menu",
        datatableApi: $('#datatable').DataTable({
            "sAjaxSource": userAjaxUrl + "menu",
            "sAjaxDataProp": "",
            "aoColumns": [
                {"data": "restaurant"},
                {"data": "menu"},
                {"data": "date"},
                {
                    "data": "isVote",
                    "render": function (data, type, row) {
                        if (isGreaterThanEqual(row.date)) {
                            return data ? '<input type="checkbox" class="editor-active" checked>'
                                : '<input type="checkbox" class="editor-active" unchecked>';
                        } else {
                            return data && new Date() >= new Date(row.date) ? '<input type="checkbox" class="editor-active" checked disabled>'
                                : '<input type="checkbox" class="editor-active" unchecked disabled>';
                        }

                    },
                    "className": "dt-body-center"
                }
            ],
            "order": [[0, "asc"]]
        })
    });
});

$('#datatable').on('change', 'tr', function () {
    let obj = context.datatableApi.row(this).data();
    let node = context.datatableApi.row(this).node();
    $.ajax({
        type: 'GET',
        async: false,
        url: ctx + "/api/review/menu/" + obj.id
    }).done(function (data) {
        update(data, node);
    }).fail(function (data) {
        if (data.status === 404)
            createItem(obj.id, node);
    });
});

$('#datatable').on('click', 'tr', function () {
    let obj = context.datatableApi.row(this).data();

    let menuId;
    if (obj !== null && typeof obj !== 'undefined') {
        menuId = obj.id;
    } else {
        menuId = "";
    }

    $('#modalDataTable').DataTable({
        "paging": false,
        "searching": false,
        "destroy": true,
        "sAjaxSource": ctx + "/api/item/menu/" + menuId,
        "sAjaxDataProp": "",
        "columns": [
            {"data": "name"},
            {"data": "price"}
        ],
        "order": [[0, "asc"]]
    });

    $('#mainForm').modal();
});

function filter() {
    $.ajax({
        url: context.ajaxUrl,
        type: 'GET',
        async: false,
        data: {
            startDate: $("#startDate").val(),
            endDate: $("#endDate").val(),
        },
        success: function (data) {
            $('#datatable').DataTable().clear().rows.add(data).draw();
        }
    });
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(context.ajaxUrl, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function update(review, node) {
    $.ajax({
        type: 'PUT',
        async: false,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        url: '/api/review/' + review.id,
        data: JSON.stringify({
            menuId: review.menuId,
            isVote: $(node).find('input').prop('checked')
        })
    }).done(function () {
        updateTable();
    });
}

function createItem(menuId, node) {
    $.ajax({
        type: 'POST',
        async: false,
        url: '/api/review',
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            menuId: menuId,
            isVote: $(node).find('input').prop('checked')
        })
    }).done(function () {
        updateTable();
    });
}
