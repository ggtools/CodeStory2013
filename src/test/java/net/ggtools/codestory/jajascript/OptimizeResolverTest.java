package net.ggtools.codestory.jajascript;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

import static net.ggtools.codestory.JsonEquals.jsonEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * User: Christophe Labouisse
 * Date: 19/01/13
 * Time: 10:00
 */
@Slf4j
public class OptimizeResolverTest {
    private OptimizeResolver resolver = new OptimizeResolver();

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void solveExample() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("ThreeFlights.json");
        assertThat(inputStream, notNullValue());
        String answer = resolver.solve(inputStream);
        assertThat(answer, jsonEquals("{ \"gain\": 18, \"path\": [\"MONAD42\", \"LEGACY01\"] }"));
    }

    @Test
    public void solveOneFlight() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("OneFlight.json");
        assertThat(inputStream, notNullValue());
        String answer = resolver.solve(inputStream);
        assertThat(answer, jsonEquals("{ \"gain\": 10, \"path\": [\"AF514\"] }"));
    }

    @Test
    public void computeManyFlights2() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("ManyFlights.json");
        assertThat(inputStream, notNullValue());
        Stopwatch stopwatch = new Stopwatch().start();
        Flight[] flights = resolver.getFlights(inputStream);
        Plan answer = resolver.computePlan2(flights);
        stopwatch.stop();
        log.info("Computed plan2 for {} flights in {}", flights.length, stopwatch);
        log.info("Plan is: {}", answer);
        assertThat(answer.getGain(), equalTo(88));
    }

    @Test
    public void computeMoreThan24Hours() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("MoreThan24Hours.json");
        assertThat(inputStream, notNullValue());
        Stopwatch stopwatch = new Stopwatch().start();
        Flight[] flights = resolver.getFlights(inputStream);
        Plan answer = resolver.computePlan2(flights);
        stopwatch.stop();
        log.info("Computed plan2 for {} flights in {}", flights.length, stopwatch);
        log.info("Plan is: {}", answer);
        assertThat(answer.getGain(), equalTo(102));
    }

    @Test
    public void random2() throws Exception {
        Random random = new Random(666);
        int count = 10000;
        Flight[] flights = new Flight[count];
        for (int i = 0; i < count; i++) {
            Flight flight = new Flight();
            flights[i] = flight;
            flight.setName("Flight-" + i);
            flight.setPrix(random.nextInt(50));
            flight.setDepart(random.nextInt(24));
            flight.setDuree(random.nextInt(24 - flight.getDepart()) + 1);
        }
        log.info("Flights: {}", Arrays.toString(flights));
        Stopwatch stopwatch = new Stopwatch().start();
        Plan answer = resolver.computePlan2(flights);
        stopwatch.stop();
        log.info("Computed plan for {} flights in {}", flights.length, stopwatch);
        log.info("Plan: {}", answer);
    }
}
