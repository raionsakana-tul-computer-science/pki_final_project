package lib;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransformationHelper {

    private static final String error = "Wystapil blad w polaczeniu z baza danych, za utrudnienia przepraszamy.";

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

    public static String getTable(ResultSet resultSet) {
        StringBuilder response = new StringBuilder();
        List<String> list = new ArrayList<>();

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            response.append("<thead>\n<tr>\n");

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                response.append(String.format("<th scope=\"col\">%s</th>\n", columnName));
                list.add(columnName);
            }
            response.append("</tr>\n</thead>\n<tbody>\n");

            while (resultSet.next()) {
                response.append("<tr>\n");

                for (String column : list) {
                    response.append(String.format("<td>%s</td>\n", resultSet.getString(column)));
                }

                response.append("</tr>\n");
            }

            response.append("</tbody>\n");
        } catch (SQLException e) {
            return getErrorMessage();
        }

        return response.toString();
    }

    private static String subLastComma(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static String getErrorMessage() {
        return String.format("<div class=\"alert alert-danger\" role=\"alert\">%s</div>", error);
    }
}
