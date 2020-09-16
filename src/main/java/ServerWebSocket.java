import lib.DatabaseTools;
import lib.QueryHelper;
import lib.TransformationHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.util.PSQLException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

// https://www.baeldung.com/java-websockets
@ServerEndpoint(value = "/app")
public class ServerWebSocket {

    private final DatabaseTools databaseTools = new DatabaseTools();
    private final String getTablesNamesQuery = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
    private final String getColumnTypesQuery = "SELECT * FROM public.";
    private final String correct = "Operacja udana.";

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Connection connection = this.databaseTools.getConnection();
        ResultSet resultSet = this.databaseTools.executeQuery(connection, this.getTablesNamesQuery);

        String response = TransformationHelper.getJsonResponse(resultSet, "table_name");
        this.databaseTools.closeConnection(connection);
        
        session.getBasicRemote().sendText(response);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(message);
            Connection connection = this.databaseTools.getConnection();

            Map<String, String> columnTypes = TransformationHelper.getColumnTypes(this.databaseTools.executeQuery(
                    connection,
                    getQuery(jsonObject.getString(QueryHelper.TABLE))
            ));

            String query = QueryHelper.getQuery(jsonObject, columnTypes);
            System.out.println(query);

            this.databaseTools.executeUpdate(connection,query);
            this.databaseTools.closeConnection(connection);

            session.getBasicRemote().sendText(String.format(TransformationHelper.response, "success", correct));
        } catch (JSONException | SQLException e) {
            e.printStackTrace();
            String out = String.format(
                    TransformationHelper.errorJson,
                    e.toString().replaceAll("\"", "'")
            ).replaceAll("\n", "\\n");

            System.out.println(out);
            session.getBasicRemote().sendText(out);
        }
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

    private String getQuery(String parameter) {
        return this.getColumnTypesQuery + parameter + ";";
    }

}
