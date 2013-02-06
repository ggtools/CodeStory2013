package net.ggtools.codestory.jajascript;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Christophe Labouisse
 * Date: 18/01/13
 * Time: 14:04
 */
@Slf4j
public class OptimizeResolver {
    private ObjectMapper mapper = new ObjectMapper();

    public String solve(InputStream jsonInputStream) {
        try {
            Flight[] flights = getFlights(jsonInputStream);
            Stopwatch stopwatch = new Stopwatch().start();
            Plan plan = computePlan2(flights);
            stopwatch.stop(); // optional
            log.info("Computed plan for {} flights in {}", flights.length, stopwatch);
            String answer = plan2Json(plan);
            if (plan.getPath().size() <= 20) {
                log.info("Answer: {}", answer);
            } else {
                log.info("Answer: gain: {}, path: {} flights", plan.getGain(), plan.getPath().size());
            }
            return answer;
        } catch (IOException e) {
            log.error("Cannot parse input stream", e);
        }
        return null;
    }

    Plan computePlan2(Flight[] flights) {
        PlanCalculator calculator = new PlanCalculator(flights);
        return calculator.computePlan();
    }

    Flight[] getFlights(InputStream jsonInputStream) throws IOException {
//        String contents = new String(ByteStreams.toByteArray(jsonInputStream));
//        log.info("Processing '{}'", contents);
        return mapper.readValue(jsonInputStream, Flight[].class);
    }

    // FIXME remove this dirty hack use Jackson to do it.
    private String plan2Json(Plan plan) throws JsonProcessingException {
        if (plan == null) return null;
        // Create a map for result.
        List<String> path = new ArrayList<>();
        for (Flight flight : plan.getPath()) {
            path.add(flight.getName());
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("gain", plan.getGain());
        result.put("path", path);
        return mapper.writeValueAsString(result);
    }

}
