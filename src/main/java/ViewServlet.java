import lib.DatabaseTools;
import lib.TransformationHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/view")
public class ViewServlet extends HttpServlet {

    private final DatabaseTools databaseTools = new DatabaseTools();
    private final String query = "SELECT * FROM public.";
    private final String tableName = "table_name";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parameter = request.getParameter(this.tableName);

        Connection connection = databaseTools.getConnection();
        String out = TransformationHelper.getTable(databaseTools.executeQuery(connection, getQuery(parameter)));

        request.setAttribute(this.tableName, parameter);
        request.setAttribute("table", out);
        request.getRequestDispatcher("view.jsp").forward(request, response);
    }

    private String getQuery(String parameter) {
        return this.query + parameter + ";";
    }

}
