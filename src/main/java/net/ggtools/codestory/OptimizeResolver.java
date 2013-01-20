package net.ggtools.codestory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.common.io.ByteStreams;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * User: Christophe Labouisse
 * Date: 18/01/13
 * Time: 14:04
 */
@Slf4j
public class OptimizeResolver {
    private static final Plan EMPTY_PLAN = new Plan(0, Collections.<Flight>emptyList());

    private ObjectMapper mapper = new ObjectMapper();

    public String solve(InputStream jsonInputStream) {
        try {
            Flight[] flights = getFlights(jsonInputStream);
            Stopwatch stopwatch = new Stopwatch().start();
            Plan plan = computePlan(flights);
            stopwatch.stop(); // optional
            log.info("Computed plan for {} flights in {}", flights.length, stopwatch);
            return plan2Json(plan);
        } catch (IOException e) {
            log.error("Cannot parse input stream", e);
        }
        return null;

    }

    Plan computePlan(Flight[] flights) {
        Plan plan = null;
        for (int i = 0; i < flights.length; i++) {
            Flight current = flights[i];
            List<Flight> selectedFlights = new ArrayList<>(flights.length - i);
            for (int j = i + 1; j < flights.length; j++) {
                Flight candidate = flights[j];
                if (current.isCompatibleWith(candidate)) {
                    selectedFlights.add(candidate);
                }
            }

            Plan currentPlan;
            int gain;
            if (selectedFlights.isEmpty()) {
                gain = current.getPrix();
                currentPlan = new Plan(gain, Arrays.asList(current));
            } else {
                Flight[] subArray = selectedFlights.toArray(new Flight[selectedFlights.size()]);
                currentPlan = computePlan(subArray);
                gain = current.getPrix() + currentPlan.getGain();
                List<Flight> path = new ArrayList<>(currentPlan.getPath().size() + 1);
                path.add(current);
                path.addAll(currentPlan.getPath());
                currentPlan = new Plan(gain, path);
            }

            if (plan == null || plan.getGain() < gain) {
                plan = currentPlan;
            }
        }
        return plan;
    }

    Flight[] getFlights(InputStream jsonInputStream) throws IOException {
        String contents = new String(ByteStreams.toByteArray(jsonInputStream));
        log.info("Processing '{}'", contents);
        return mapper.readValue(contents, Flight[].class);
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

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Plan {
        private final int gain;

        private final List<Flight> path;
    }

    @ToString
    @Getter
    @EqualsAndHashCode(of = "name")
    public static class Flight {
        @JsonProperty("VOL")
        @Setter(AccessLevel.PACKAGE)
        private String name;

        private int depart;

        private int duree;

        @JsonProperty("PRIX")
        @Setter(AccessLevel.PACKAGE)
        private int prix;

        private transient int end = -1;

        public boolean isCompatibleWith(Flight other) {
            return (other.depart >= end) || (other.end <= depart);
        }

        @JsonProperty("DEPART")
        void setDepart(int depart) {
            this.depart = depart;
            computeEnd();
        }

        @JsonProperty("DUREE")
        void setDuree(int duree) {
            this.duree = duree;
            computeEnd();
        }

        private void computeEnd() {end = this.depart + this.duree;}
    }
}
