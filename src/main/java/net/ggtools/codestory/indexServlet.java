package net.ggtools.codestory;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * User: Christophe Labouisse
 * Date: 15/01/13
 * Time: 13:39
 */
public class indexServlet extends javax.servlet.http.HttpServlet {

    private List<Resolver> resolvers = new ArrayList<Resolver>();

    private OptimizeResolver optimizeResolver;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        resolvers.add(new QuestionResolver());
        resolvers.add(new ScalaskelResolver());
        resolvers.add(new CalculatorResolver());
        optimizeResolver = new OptimizeResolver();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String contents = new String(ByteStreams.toByteArray(inputStream));
        log(contents);
        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/jajascript/optimize")) {

        }
        /*
        88.190.22.96 - - [18/Jan/2013:09:40:26 +0000] "POST /enonce/2 HTTP/1.1" 200 25 "-" "curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3"
88.190.22.96 - - [18/Jan/2013:09:40:26 +0000] "GET /?q=As+tu+bien+recu+le+second+enonce(OUI/NON) HTTP/1.1" 200 6 "-" "curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3"
88.190.22.96 - - [18/Jan/2013:09:50:22 +0000] "GET /?q=As+tu+bien+recu+le+second+enonce(OUI/NON) HTTP/1.1" 200 5 "-" "curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3"
88.190.22.96 - - [18/Jan/2013:09:50:23 +0000] "POST /jajascript/optimize HTTP/1.1" 200 0 "-" "curl/7.22.0 (x86_64-pc-linux-gnu) libcurl/7.22.0 OpenSSL/1.0.1 zlib/1.2.3.4 libidn/1.23 librtmp/2.3"

         */
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        // Exclude some paths from processing.
        if (servletPath.startsWith("/favicon") || servletPath.startsWith("/_stax/")) {
            response.setStatus(404);
            return;
        }

        logRequest(request);
        String value = "Unknown request";
        try {
            for (Resolver resolver : resolvers) {
                log("Trying " + resolver);
                value = resolver.solve(request);
                if (value != null) {
                    break;
                }
            }
            response.getOutputStream().println(value);
        } catch (ResolverException e) {
            log("Cannot solve", e);
        }
    }

    private void logRequest(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder("Dispatch: '").append(request.getServletPath()).append("', params = {");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" = ").append(Arrays.toString(entry.getValue()));
        }
        stringBuilder.append("}");
        log(stringBuilder.toString());
    }
}
