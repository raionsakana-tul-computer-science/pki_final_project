let webSocket;

// if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
//     // writeResponse("WebSocket is already opened.");
// } else {
//     // heroku remote
//     // webSocket = new WebSocket("wss://pki-projekt-koncowy.herokuapp.com/echo");
//
//     // heroku local web
//     webSocket = new WebSocket("ws://localhost:5000/app");
//
//     // installij run
//     // webSocket = new WebSocket("ws://localhost:5000/pki_projekt_koncowy_war/echo");
// }

webSocket = new WebSocket("ws://localhost:5000/app");
