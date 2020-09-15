import lib.DatabaseTools;
import lib.TransformationHelper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
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
        session.getBasicRemote().sendText(TransformationHelper.getJsonResponse(resultSet, "table_name"));
    }

//    @OnMessage
//    public void onMessage(Session session) throws IOException {
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
