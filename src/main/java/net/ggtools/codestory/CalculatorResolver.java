package net.ggtools.codestory;

import groovy.lang.GroovyShell;

import javax.servlet.http.HttpServletRequest;

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
            Object evaluate = shell.evaluate(expr);
            return evaluate.toString().replaceAll("\\.0+$", "").replace('.', ',');
        } catch (Exception e) {
            return null;
        }
    }
}
