import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

@ServerEndpoint(value = "/app")
public class ServerWebSocket {

    private final DatabaseTools databaseTools = new DatabaseTools();
    private final String getTablesNamesQuery = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
    private Connection connection;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.connection = databaseTools.getConnection();
        ResultSet resultSet = databaseTools.executeQuery(this.connection, this.getTablesNamesQuery);
        String response = databaseTools.getJsonResponse(resultSet, "table_name");

        System.out.println(response);
        session.getBasicRemote().sendText(response);
    }

//    @OnMessage
//    public void onMessage(Session session, Message message) throws IOException {
//        // Handle new messages
//    }
//
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        // WebSocket connection closes
//    }
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        // Do error handling here
//    }
}
