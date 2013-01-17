package net.ggtools.codestory;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static net.ggtools.codestory.ScalaskelResolver.COIN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * User: Christophe Labouisse
 * Date: 16/01/13
 * Time: 13:06
 */
@RunWith(MockitoJUnitRunner.class)
public class ScalaskelResolverTest {
    @Mock
    private HttpServletRequest request;
    private ScalaskelResolver resolver = new ScalaskelResolver();
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void solveBadRequest() throws Exception {
        Mockito.when(request.getServletPath()).thenReturn("/ga/bu/zo");
        String response = resolver.solve(request);
        assertThat(response, nullValue());
    }

    @Test
    public void solve1GD() throws Exception {
        Mockito.when(request.getServletPath()).thenReturn("/scalaskel/change/1");
        String response = resolver.solve(request);
        assertResponseOk(response, "[{\"foo\": 1}]");
    }

    @Test
    public void solve7GD() throws Exception {
        Mockito.when(request.getServletPath()).thenReturn("/scalaskel/change/7");
        String response = resolver.solve(request);
        assertResponseOk(response, "[ {\"foo\": 7}, {\"bar\": 1} ]");
    }

    @Test
    public void computeChange() throws Exception {
        for (int value = 0; value <= 100; value++) {
            List<Map<COIN, Integer>> list = resolver.computeChange(COIN.baz, value);
            for (Map<COIN, Integer> change : list) {
                int sum = 0;
                for (Map.Entry<COIN, Integer> entry : change.entrySet()) {
                    sum += entry.getValue() * entry.getKey().getValue();
                }
                assertThat("Change: " + change, sum, equalTo(value));
            }
        }
    }

    private void assertResponseOk(String response, String expected) throws IOException {
        assertThat(response, notNullValue());
        List refValue = mapper.readValue(expected, List.class);
        List respValue = mapper.readValue(response, List.class);
        assertThat(respValue, equalTo(refValue));
    }
}
