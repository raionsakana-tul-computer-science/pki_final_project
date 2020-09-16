package lib;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class QueryHelper {

    private static final String TYPE = "type";
    public static final String TABLE = "table";
    private static final String DATA = "data";

    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String ADD = "add";

    private static final String queryInsert = "INSERT INTO \"%s\" (%s) VALUES (%s);";
    private static final String queryDelete = "DELETE FROM \"%s\" WHERE %s;";
    private static final String queryUpdate = "UPDATE TABLE \"%s\" SET %s WHERE %s;";

    private static final String[] numericTypes = new String[] { "bit", "serial", "int", "bool", "float", "double", "numeric" };
    private static final String[] stringTypes = new String[] { "char", "time", "date", "text" };

    public static String getQuery(JSONObject jsonObject, Map<String, String> columnTypes) {
        String key = jsonObject.getString(TYPE);

        if (key.equals(UPDATE)) {
            return getUpdateQuery(jsonObject, columnTypes);
        } else if (key.equals(DELETE)) {
            return getDeleteQuery(jsonObject, columnTypes);
        } else if (key.equals(ADD)) {
            return getAddQuery(jsonObject, columnTypes);
        }

        return "";
    }

    public static String getUpdateQuery(JSONObject jsonObject, Map<String, String> columnTypes) {
        return "";
    }

    public static String getDeleteQuery(JSONObject jsonObject, Map<String, String> columnTypes) {
        return "";
    }

    public static String getAddQuery(JSONObject jsonObject, Map<String, String> columnTypes) {
        StringBuilder columnsStringBuilder = new StringBuilder();
        StringBuilder valuesStringBuilder = new StringBuilder();

        String table = jsonObject.getString(TABLE);
        JSONObject columns = jsonObject.getJSONObject(DATA);

        for (Iterator<String> it = columns.keys(); it.hasNext(); ) {
            String key = it.next();
            String value = columns.getString(key);
            value = adjustValue(key, value, columnTypes);

            columnsStringBuilder.append(key).append(",");
            valuesStringBuilder.append(value).append(",");
        }

        String columnsString = cutString(columnsStringBuilder.toString());
        String valuesString = cutString(valuesStringBuilder.toString());

        return String.format(queryInsert, table, columnsString, valuesString);
    }

    private static String cutString(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static String adjustValue(String columnName, String value, Map<String, String> columnTypes) {
        String type = columnTypes.get(columnName);

        if (checkType(type, stringTypes)) {
            return "\'" + value + "\'";
        }

        if (checkType(type, numericTypes)) {
            return value;
        }

        return "";
    }

    private static boolean checkType(String type, String[] types) {
        boolean mark = false;

        for (String t : types) {
            if (type.contains(t)) {
                mark = true;
                break;
            }
        }

        return mark;
    }

}
