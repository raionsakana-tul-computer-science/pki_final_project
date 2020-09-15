import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTools {

    private final String address = "ec2-54-247-78-30.eu-west-1.compute.amazonaws.com";
    private final String port = "5432";
    private final String database = "d5pbnnu30s3fef";

    private final String user = "iurlafzjuzogpe";
    private final String password = "a920ea57b9a90ced49482d9127bf8b7d71c372e4b621fbf91a7dad1b58b2f556";

    private String URI;

    public DatabaseTools() {
        this.URI = String.format("jdbc:postgresql://%s:%s/%s", this.address, this.port, this.database);
    }

    public Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(this.URI, this.user, this.password);
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

        return connection;
    }

    public ResultSet executeQuery(Connection connection, String query) {
        ResultSet resultSet = null;

        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

        return resultSet;
    }

    public String getJsonResponse(ResultSet resultSet, String column_name) {
        StringBuilder response = new StringBuilder();

        try {
            while (resultSet.next()) {
                response.append(String.format("\"%s\",", resultSet.getString(column_name)));
            }
        } catch (SQLException e) {
            return String.format("{\"type\": \"error\", \"data\":\"%s\"}", e);
        }

        return String.format("{\"type\": \"open\", \"data\": [%s]}", subLastComma(response.toString()));
    }

    private String subLastComma(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
