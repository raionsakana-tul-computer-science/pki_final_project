// websocket

let webSocket;

if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
    showModalResponse("WebSocket aktualnie otwarty.");
} else {
    // heroku remote
    // webSocket = new WebSocket("wss://pki-project-patryk-janowski.herokuapp.com/app");

    // heroku local web
    webSocket = new WebSocket("ws://localhost:5000/app");
}

webSocket.onmessage = function (event) {
    var message = JSON.parse(event.data);

    if (message["type"] === "open") {
        prepareSelectList(message, "table-list")
    } else if (message["type"] === "error") {
        showModalResponse(message["data"]);
    } else {
        showModalResponse("Wystąpił błąd.");
    }
};


// Functions

function showModalResponse(text) {
    document.getElementById("insert-modal").innerHTML = text;

    $('#alert-modal').modal({
        show: true
    })
}

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
    window.location.href = "/view"
}
