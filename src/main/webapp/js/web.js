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
