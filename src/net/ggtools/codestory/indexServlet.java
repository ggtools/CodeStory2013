package net.ggtools.codestory;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Christophe Labouisse
 * Date: 15/01/13
 * Time: 13:39
 */
public class indexServlet extends javax.servlet.http.HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream inputStream = req.getInputStream();
        byte[] bytes = ByteStreams.toByteArray(inputStream);
        getServletContext().log(new String(bytes));
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String q = request.getParameter("q");
        if (q != null) {
            Questions question = Questions.lookup(q);
            getServletContext().log("Question: " + q + " -> " + question);
            switch (question) {
                case UNKNOWN:
                    getServletContext().log("Unknown question: '" + q + "'");
                    response.getOutputStream().println("Unknown question " + q);
                    break;

                default:
                    response.getOutputStream().println(question.getAnswer());
                    break;
            }
        }
    }
}
