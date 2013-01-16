package net.ggtools.codestory;

import java.io.IOException;

/**
 * User: Christophe Labouisse
 * Date: 17/01/13
 * Time: 00:27
 */
public class ResolverException extends Exception {

    public ResolverException(String s, IOException e) {
        super(s, e);
    }
}
