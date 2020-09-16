package lib;

import org.json.JSONArray;
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
    private static final String queryUpdate = "UPDATE \"%s\" SET %s WHERE %s;";

    private static final String[] numericTypes = new String[] { "bit", "serial", "int", "bool", "float", "double", "numeric" };
    private static final String[] stringTypes = new String[] { "char", "time", "date", "text" };

    public static String getQuery(JSONObject jsonObject, Map<String, String> columnTypes) {
        String key = jsonObject.getString(TYPE);

        switch (key) {
            case UPDATE:
                return getUpdateQuery(jsonObject, columnTypes);
            case DELETE:
                return getDeleteQuery(jsonObject, columnTypes);
            case ADD:
                return getAddQuery(jsonObject, columnTypes);
        }

        return "";
    }

    public static String getUpdateQuery(JSONObject jsonObject, Map<String, String> columnTypes) {
        StringBuilder conditionsStringBuilder = new StringBuilder();
        StringBuilder setStringBuilder = new StringBuilder();

        String table = jsonObject.getString(TABLE);
        JSONArray jsonArray = jsonObject.getJSONArray(DATA);

        JSONObject temp = jsonArray.getJSONObject(0);
        getQueryBody(temp, conditionsStringBuilder, columnTypes, " and ");

        temp = jsonArray.getJSONObject(1);
        getQueryBody(temp, setStringBuilder, columnTypes, ",");

        String conditions = cutLastAnd(conditionsStringBuilder.toString());
        String sets = cutString(setStringBuilder.toString());

        return String.format(queryUpdate, table, sets, conditions);
    }

    public static String getDeleteQuery(JSONObject jsonObject, Map<String, String> columnTypes) {
        StringBuilder stringBuilder = new StringBuilder();

        String table = jsonObject.getString(TABLE);
        JSONObject columns = jsonObject.getJSONObject(DATA);

        getQueryBody(columns, stringBuilder, columnTypes, " and ");
        String output = cutLastAnd(stringBuilder.toString());

        return String.format(queryDelete, table, output);
    }

    public static String getAddQuery(JSONObject jsonObject, Map<String, String> columnTypes) {
        StringBuilder columnsStringBuilder = new StringBuilder();
        StringBuilder valuesStringBuilder = new StringBuilder();

        String table = jsonObject.getString(TABLE);
        JSONObject columns = jsonObject.getJSONObject(DATA);

        prepareQueryData(columns, columnsStringBuilder, valuesStringBuilder, columnTypes);
        String columnsString = cutString(columnsStringBuilder.toString());
        String valuesString = cutString(valuesStringBuilder.toString());

        return String.format(queryInsert, table, columnsString, valuesString);
    }

    private static void prepareQueryData(JSONObject jsonObject, StringBuilder stringBuilder1,
                                         StringBuilder stringBuilder2, Map<String, String> columnTypes) {
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String key = it.next();
            String value = jsonObject.getString(key);
            value = adjustValue(key, value, columnTypes);

            stringBuilder1.append(key).append(",");
            stringBuilder2.append(value).append(",");
        }
    }

    private static void getQueryBody(JSONObject jsonObject, StringBuilder stringBuilder, Map<String, String> columnTypes, String connector) {
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String key = it.next();
            String value = jsonObject.getString(key);

            value = adjustValue(key, value, columnTypes);
            stringBuilder.append(key).append("=").append(value).append(connector);
        }
    }

    private static String cutString(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static String cutLastAnd(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 5);
        }
        return str;
    }

    private static String adjustValue(String columnName, String value, Map<String, String> columnTypes) {
        String type = columnTypes.get(columnName);

        if (checkType(type, stringTypes)) {
            return "'" + value + "'";
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
