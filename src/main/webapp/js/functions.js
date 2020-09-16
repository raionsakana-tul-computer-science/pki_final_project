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
    window.location.href = "/";
}

function onClose() {
    webSocket.close();
}

function prepareJson(names, input) {
    var text = "{";

    for (var i = 0; i < names.children.length - 1; i++) {
        text += "\"" + names.children[i].textContent + "\": \"" + input[i].textContent + "\", ";
    }

    text = text.substring(0, text.length - 2);
    return text + "}";
}

function prepareJsonFromPlaceholder(names, input) {
    var text = "{";

    for (var i = 0; i < names.children.length - 1; i++) {
        text += "\"" + names.children[i].textContent + "\": \"" + input[i].placeholder + "\", ";
    }

    text = text.substring(0, text.length - 2);
    return text + "}";
}

function prepareRequest(json, type, table) {
    return "{\"type\": \"" + type + "\", \"table\": \"" + table + "\", \"data\": " + json + "}";
}

function prepareList(old, new_) {
    return "[" + old + ", " + new_ + "]";
}