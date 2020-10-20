// websocket

let webSocket;
var tableBackup;

if (document.getElementById('table-reload') !== null) {
    tableBackup = document.getElementById('table-reload').innerHTML;
}

if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
    showModalResponse("WebSocket aktualnie otwarty.");
} else {
    // heroku remote
    webSocket = new WebSocket("wss://pki-project-patryk-janowski.herokuapp.com/app");

    // heroku local web
    // webSocket = new WebSocket("ws://localhost:5000/app");
}

webSocket.onmessage = function (event) {
    var message = JSON.parse(event.data);

    switch (message["type"]) {
        case "open":
            prepareSelectList(message, "table-list")
            break;
        case "error":
            showModalResponse(message["data"]);
            document.getElementById("table-reload").innerHTML = tableBackup;
            break;
        case "success":
            showModalResponse(message["data"]);
            tableBackup = document.getElementById('table-reload').innerHTML;
            break;
        default:
            showModalResponse("Wystąpił błąd.");
            break;
    }
};

webSocket.onopen = function (event) {
    console.log("Połączono z serwerem.");
};

webSocket.onclose = function (event) {
    console.log("Rozłączono z serwerem.");
};

webSocket.onerror = function (event) {
    console.error("Błąd w połączeniu z serwerem.");
};