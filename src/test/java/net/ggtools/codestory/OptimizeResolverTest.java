package net.ggtools.codestory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static net.ggtools.codestory.JsonEquals.jsonEquals;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * User: Christophe Labouisse
 * Date: 19/01/13
 * Time: 10:00
 */
public class OptimizeResolverTest {
    private OptimizeResolver resolver = new OptimizeResolver();
    private InputStream threeFlightsStream;
    private InputStream oneFlightStream;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        threeFlightsStream = getClass().getResourceAsStream("ThreeFlights.json");
        oneFlightStream = getClass().getResourceAsStream("OneFlight.json");
    }

    @Test
    public void solveExample() throws Exception {
        assertThat(threeFlightsStream, notNullValue());
        String answer = resolver.solve(threeFlightsStream);
        assertThat(answer, jsonEquals("{ \"gain\": 18, \"path\": [\"MONAD42\", \"LEGACY01\"] }"));
    }

    @Test
    public void solveOneFlight() throws Exception {
        assertThat(oneFlightStream, notNullValue());
        String answer = resolver.solve(oneFlightStream);
        assertThat(answer, jsonEquals("{ \"gain\": 10, \"path\": [\"AF514\"] }"));
    }
}
