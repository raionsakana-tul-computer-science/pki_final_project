package lib;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransformationHelper {

    public static String getJsonResponse(ResultSet resultSet, String column_name) {
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

    private static String subLastComma(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
