package net.ggtools.codestory;

import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
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

    @Before
    public void setUp() throws Exception {

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

    private void assertResponseOk(String response, String expected) throws IOException {
        assertThat(response, notNullValue());
        List refValue = mapper.readValue(expected, List.class);
        List respValue = mapper.readValue(response, List.class);
        assertThat(respValue, CoreMatchers.equalTo(refValue));
    }
}
