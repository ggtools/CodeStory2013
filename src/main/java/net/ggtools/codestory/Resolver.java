package net.ggtools.codestory;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Christophe Labouisse
 * Date: 16/01/13
 * Time: 08:45
 */
public interface Resolver {
    String resolve(HttpServletRequest request);

    boolean match(HttpServletRequest request);
}
