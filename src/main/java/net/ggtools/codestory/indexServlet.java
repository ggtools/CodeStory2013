package net.ggtools.codestory;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Christophe Labouisse
 * Date: 15/01/13
 * Time: 13:39
 */
public class indexServlet extends javax.servlet.http.HttpServlet {

    private List<Resolver> resolvers = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        resolvers.add(new QuestionResolver());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream inputStream = req.getInputStream();
        byte[] bytes = ByteStreams.toByteArray(inputStream);
        getServletContext().log(new String(bytes));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().log("Dispatch: '" + request.getServletPath() + "' " + request.getParameterMap() + "'");
        String value = "Unknown request";
        for (Resolver resolver : resolvers) {
            if (resolver.match(request)) {
                value = resolver.resolve(request);
                break;
            }
        }
        response.getOutputStream().println(value);
    }
}
