package net.ggtools.codestory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Christophe Labouisse
 * Date: 16/01/13
 * Time: 13:01
 */
public class ScalaskelResolver implements Resolver {

    //Foo vaut 1 cent, le Bar vaut 7 cents, le Qix vaut 11 cents et le Baz vaut 21 cents.
    private static Coin[] coins = {new Coin("Baz", 21), new Coin("Qix", 11), new Coin("Bar", 7), new Coin("Foo", 1)};

    @Override
    public String solve(HttpServletRequest request) {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean match(HttpServletRequest request) {
        return request.getServletPath().startsWith("/scalaskel/change/");
    }

    @Getter
    @NoArgsConstructor
    @ToString
    @AllArgsConstructor
    private static class Coin {
        private String name;
        private int value;
    }
}
