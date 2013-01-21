package net.ggtools.codestory;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import net.ggtools.codestory.jajascript.OptimizeResolver;

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
@Slf4j
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/jajascript/optimize")) {
            String answer = optimizeResolver.solve(inputStream);
            response.getOutputStream().println(answer);
        } else {
            String contents = new String(ByteStreams.toByteArray(inputStream));
            log(contents);
        }
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
        String answer = "Unknown request";
        try {
            for (Resolver resolver : resolvers) {
                log.info("Trying {}", resolver);
                answer = resolver.solve(request);
                if (answer != null) {
                    break;
                }
            }
            response.getOutputStream().println(answer);
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
        log.info(stringBuilder.toString());
    }
}
