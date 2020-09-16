// Functions

// https://getbootstrap.com/docs/4.0/components/modal/
function showModalResponse(text) {
    document.getElementById("insert-modal").innerHTML = text;

    $('#alert-modal').modal({
        show: true
    })
}

// https://mdbootstrap.com/docs/jquery/forms/select/
function prepareSelectList(json, id) {
    var obj, content = "", size = json["data"].length, i;

    for (i = 0; i < size - 1; i++) {
        obj = json["data"][i];
        content += "<option value=\"" + obj + "\">" + obj + "</option>";
    }

    obj = json["data"][size - 1]
    content += "<option value=\"" + obj + "\" selected>" + obj + "</option>";

    document.getElementById(id).innerHTML = content;
}

function viewTable() {
    var e = document.getElementById("table-list");
    var value = e.options[e.selectedIndex].value;
    window.location.href = "/view?table_name=" + value;
}

function backToHomePage() {
    window.location.href = "/"
}