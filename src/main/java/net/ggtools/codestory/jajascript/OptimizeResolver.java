package net.ggtools.codestory.jajascript;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.common.io.ByteStreams;
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
    public static final int MAX_SLOTS = 24;

    private static final Plan EMPTY_PLAN = new Plan(0, Collections.<Flight>emptyList());

    private ObjectMapper mapper = new ObjectMapper();

    public String solve(InputStream jsonInputStream) {
        try {
            Flight[] flights = getFlights(jsonInputStream);
            Stopwatch stopwatch = new Stopwatch().start();
            Plan plan = computePlan2(flights);
            stopwatch.stop(); // optional
            log.info("Computed plan for {} flights in {}", flights.length, stopwatch);
            String answer = plan2Json(plan);
            log.info("Answer: {}", answer);
            return answer;
        } catch (IOException e) {
            log.error("Cannot parse input stream", e);
        }
        return null;

    }

    Plan computePlan2(Flight[] flights) {
        // TODO make the sumber of slots dynamic
        // Create slots plus an extra one.
        Slot[] slots = new Slot[MAX_SLOTS + 1];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot(i);
        }

        for (Flight flight : flights) {
            slots[flight.getDepart()].getFlights().add(flight);
        }

        int maxGain = -1;
        Slot selectedSlot = null;
        for (int i = MAX_SLOTS - 1; i >= 0; i--) {
            Slot currentSlot = slots[i];
            Slot selectedNextSlot = slots[i + 1];
            int gain = selectedNextSlot.getGain();

            Flight selectedFlight = null;
            for (Flight flight : currentSlot.getFlights()) {
                Slot nextSlot = slots[flight.getEnd()];
                int currentGain = flight.getPrix() + nextSlot.getGain();
                if (currentGain > gain) {
                    selectedFlight = flight;
                    gain = currentGain;
                    selectedNextSlot = nextSlot;
                }
            }

            currentSlot.setGain(gain);
            if (selectedFlight != null) currentSlot.getPath().add(selectedFlight);
            currentSlot.getPath().addAll(selectedNextSlot.getPath());

            if (currentSlot.getGain() > maxGain) {
                maxGain = currentSlot.getGain();
                selectedSlot = currentSlot;
            }
        }

        return new Plan(maxGain, selectedSlot.getPath());
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

}
