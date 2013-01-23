package net.ggtools.codestory;

import groovy.lang.GroovyShell;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * User: Christophe Labouisse
 * Date: 17/01/13
 * Time: 11:43
 */
public class CalculatorResolver implements Resolver {

    @Override
    public String solve(HttpServletRequest request) throws ResolverException {
        String q = request.getParameter("q");

        if (q == null) return null;

        String expr = q.replace(',', '.').replaceAll(" ", "+");
        try {
            GroovyShell shell = new GroovyShell();
            Object evalResult = shell.evaluate(expr);
            Object result;
            if (evalResult instanceof BigDecimal) {
                BigDecimal bigDecimal = (BigDecimal) evalResult;
                result = bigDecimal.stripTrailingZeros();
            } else {
                result = evalResult;
            }

            return result.toString().replace('.', ',');
        } catch (Exception e) {
            return null;
        }
    }
}
