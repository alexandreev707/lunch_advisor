var context;

function initContext(ctx) {
    context = ctx;
}

function updateTable() {
    $.get(context.ajaxUrl, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function isEmpty() {
    for (let i = 0; i < arguments.length; i++) {
        if (arguments[i] === null || typeof arguments[i] === 'undefined') return true;
    }
}

function renderEditBtn(date, type, row) {
    if (type === 'display') {
        return '<button type="button" onclick="edit(this)"><span class="fa fa-pencil"></span></button>';
    }
}

function renderDeleteBtn(date, type, row) {
    if (type === 'display') {
        return '<button type="button" id="' + row.id + '" onclick="remove(this)"><span class="fa fa-remove"></span></button>';
    }
}

function isGreaterThanEqual(dateString) {
    let currentDate = new Date().setHours(0, 0, 0, 0);
    let date = new Date(dateString).setHours(0, 0, 0, 0);
    return date.valueOf() >= currentDate.valueOf();
}