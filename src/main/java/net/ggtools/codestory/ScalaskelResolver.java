package net.ggtools.codestory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * User: Christophe Labouisse
 * Date: 16/01/13
 * Time: 13:01
 */
public class ScalaskelResolver implements Resolver {

    @Override
    public String solve(HttpServletRequest request) {
        String path = request.getServletPath();
        int value = Integer.valueOf(path.substring(path.lastIndexOf('/') + 1));
        // List<Map<COIN, Integer>> results = computeChange(COIN.Baz.ordinal(), value);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean match(HttpServletRequest request) {
        return request.getServletPath().startsWith("/scalaskel/change/");
    }

    private enum COIN {
        //Foo vaut 1 cent, le Bar vaut 7 cents, le Qix vaut 11 cents et le Baz vaut 21 cents.
        Foo(1), Bar(7), Qix(11), Baz(21);
        private final int value;

        private COIN(int value) {
            this.value = value;
        }
    }
}
