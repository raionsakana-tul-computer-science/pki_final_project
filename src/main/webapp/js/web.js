// websocket

let webSocket;
var tableBackup = document.getElementById('table-reload').innerHTML;

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
        document.getElementById("table-reload").innerHTML = tableBackup;
    } else if (message["type"] === "success") {
        showModalResponse(message["data"]);
        tableBackup = document.getElementById('table-reload').innerHTML;
    }   else {
        showModalResponse("Wystąpił błąd.");
    }
};
