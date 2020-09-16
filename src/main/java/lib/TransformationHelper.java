package lib;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformationHelper {

    private static final String error = "Wystapil blad w polaczeniu z baza danych, za utrudnienia przepraszamy.";
    private static final String errorHtml = "<div class=\"alert alert-danger\" role=\"alert\">%s</div>";
    public static final String errorJson = "{\"type\": \"error\", \"data\":\"%s\"}";
    public static final String response = "{\"type\": \"%s\", \"data\":\"%s\"}";

    private static final String answerJson = "{\"type\": \"open\", \"data\": [%s]}";
    private static final String formatList = "\"%s\",";

    private static final String startHeader = "<thead>\n<tr id=\"thead_tr\">\n";
    private static final String HeaderToBody = "</tr>\n</thead>\n<tbody>\n";
    private static final String stopBody = "</tbody>\n";

    private static final String rowHeader = "<th scope=\"col\">%s</th>\n";
    private static final String row = "<td>%s</td>\n";
    private static final String tableName = "<input style=\"display: none;\" id=\"table-name\" placeholder=\"%s\"></input>\n";

    private static final String startRow = "<tr>\n";
    private static final String stopRow = "</tr>\n";

    private static final String addButton = "<button type=\"button\" class=\"btn btn-warning addbtn\">Add</button>\n";
    private static final String editButton = "<button type=\"button\" class=\"btn btn-success editbtn\">Edit</button>\n";
    private static final String removeButton = "<button type=\"button\" class=\"btn btn-danger dltbtn\"><i class=\"far fa-trash-alt\"></i></button>\n";

    public static String getJsonResponse(ResultSet resultSet, String columnName) {
        StringBuilder response = new StringBuilder();

        try {
            prepareListForJson(response, resultSet, columnName);
        } catch (SQLException e) {
            return String.format(errorJson, e);
        }

        return String.format(answerJson, subLastComma(response.toString()));
    }

    public static String getTable(ResultSet resultSet) {
        StringBuilder response = new StringBuilder();
        List<String> list = new ArrayList<>();

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            prepareTable(response, resultSet, resultSetMetaData, list);
        } catch (SQLException e) {
            return getErrorMessage();
        }

        return response.toString();
    }

    public static Map<String, String> getColumnTypes(ResultSet resultSet) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            prepareTableMap(resultSetMetaData, map);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static void prepareTableMap(ResultSetMetaData resultSetMetaData, Map<String, String> map) throws SQLException {
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            String columnName = resultSetMetaData.getColumnName(i);
            String columnType = resultSetMetaData.getColumnTypeName(i);

            map.put(columnName, columnType);
        }
    }

    private static void prepareTable(StringBuilder response, ResultSet resultSet, ResultSetMetaData resultSetMetaData, List<String> list) throws SQLException {
        response.append(startHeader);
        response.append(String.format(tableName, resultSetMetaData.getTableName(1)));

        prepareHeader(resultSetMetaData, response, list);
        response.append(HeaderToBody);

        prepareRows(resultSet, response, list);
        response.append(stopBody);
    }

    private static void prepareHeader(ResultSetMetaData resultSetMetaData, StringBuilder response, List<String> list) throws SQLException {
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            String columnName = resultSetMetaData.getColumnName(i);
            response.append(String.format(rowHeader, columnName));
            list.add(columnName);
        }

        response.append(String.format(rowHeader, "Opcje"));
    }

    private static void prepareRows(ResultSet resultSet, StringBuilder response, List<String> list) throws SQLException {
        while (resultSet.next()) {
            response.append(startRow);

            for (String column : list) {
                response.append(String.format(row, resultSet.getString(column)));
            }

            response.append(String.format(row, getButtons()));
            response.append(stopRow);
        }
    }

    private static void prepareListForJson(StringBuilder response, ResultSet resultSet, String columnName) throws SQLException {
        while (resultSet.next()) {
            response.append(String.format(formatList, resultSet.getString(columnName)));
        }
    }

    // https://stackoverflow.com/questions/7438612/how-to-remove-the-last-character-from-a-string
    private static String subLastComma(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    // https://getbootstrap.com/docs/4.0/components/alerts/
    private static String getErrorMessage() {
        return String.format(errorHtml, error);
    }

    private static String getButtons() {
        return addButton + editButton + removeButton;
    }
}
