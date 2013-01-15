package net.ggtools.codestory;

import java.io.IOException;

/**
 * User: Christophe Labouisse
 * Date: 15/01/13
 * Time: 13:39
 */
public class indexServlet extends javax.servlet.http.HttpServlet {
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String q = request.getParameter("q");
        Questions question = Questions.lookup(q);
        getServletContext().log("Question: " + q + " -> " + question);
        switch (question) {
            case EMAIL_ADDRESS:
                response.getOutputStream().println("consulting@labouisse.com");
                break;

            default:
                getServletContext().log("Unknown question: '" + q + "'");
                response.getOutputStream().println("Unknown question " + q);
                break;
        }
    }
}
