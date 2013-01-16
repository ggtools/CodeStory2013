package net.ggtools.codestory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: Christophe Labouisse
 * Date: 16/01/13
 * Time: 13:01
 */
public class ScalaskelResolver implements Resolver {

    private ObjectMapper mapper = new ObjectMapper();

    private static void addToChange(COIN currentCoin, int value, Map<COIN, Integer> changeMap) {
        if (value != 0) {
            changeMap.put(currentCoin, value);
        }
    }

    @Override
    public String solve(HttpServletRequest request) throws ResolverException {
        String path = request.getServletPath();
        int value = Integer.valueOf(path.substring(path.lastIndexOf('/') + 1));
        List<Map<COIN, Integer>> results = computeChange(COIN.baz, value);
        try {
            return mapper.writeValueAsString(results);
        } catch (IOException e) {
            throw new ResolverException("Cannot convert results to JSON", e);
        }
    }

    protected List<Map<COIN, Integer>> computeChange(COIN currentCoin, int value) {
        if (currentCoin.getValue() == 1) {
            Map<COIN, Integer> changeMap = Maps.newHashMapWithExpectedSize(COIN.values().length);
            addToChange(currentCoin, value, changeMap);
            return Lists.newArrayList(changeMap);
        }
        int maxCoins = value / currentCoin.getValue();
        List<Map<COIN, Integer>> results = Lists.newArrayList();
        for (int i = 0; i <= maxCoins; i++) {
            List<Map<COIN, Integer>> subChanges = computeChange(COIN.values()[currentCoin.ordinal() - 1], value - i * currentCoin.getValue());
            for (Map<COIN, Integer> change : subChanges) {
                addToChange(currentCoin, i, change);
                results.add(change);
            }
        }
        return results;
    }

    @Override
    public boolean match(HttpServletRequest request) {
        return request.getServletPath().startsWith("/scalaskel/change/");
    }

    protected enum COIN {
        //Foo vaut 1 cent, le Bar vaut 7 cents, le Qix vaut 11 cents et le Baz vaut 21 cents.
        foo(1), bar(7), qix(11), baz(21);
        private final int value;

        private COIN(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
