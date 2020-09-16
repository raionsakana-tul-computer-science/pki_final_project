import lib.DatabaseTools;
import lib.TransformationHelper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

// https://www.baeldung.com/java-websockets
@ServerEndpoint(value = "/app")
public class ServerWebSocket {

    private final DatabaseTools databaseTools = new DatabaseTools();
    private final String getTablesNamesQuery = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Connection connection = this.databaseTools.getConnection();
        ResultSet resultSet = this.databaseTools.executeQuery(connection, this.getTablesNamesQuery);

        String response = TransformationHelper.getJsonResponse(resultSet, "table_name");
        this.databaseTools.closeConnection(connection);
        
        session.getBasicRemote().sendText(response);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection with client closed.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            System.out.println("Error occurred.");
            e.printStackTrace();
        }
    }
}
