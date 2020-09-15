package lib;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// https://examples.javacodegeeks.com/core-java/sql/java-jdbc-postgresql-connection-example/
// https://mvnrepository.com/artifact/org.postgresql/postgresql/42.2.5
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

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Disconnection failure.");
            e.printStackTrace();
        }
    }

}
