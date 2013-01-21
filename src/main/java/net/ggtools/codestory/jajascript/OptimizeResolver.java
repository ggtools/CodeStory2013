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
            log.info("Answer: {}", answer);
            return answer;
        } catch (IOException e) {
            log.error("Cannot parse input stream", e);
        }
        return null;
    }

    Plan computePlan2(Flight[] flights) {
        List<Slot> slotList = new ArrayList<>();

        for (Flight flight : flights) {
            int wantedIndex = flight.getDepart();
            int maxIndex = flight.getEnd();
            while (slotList.size() <= maxIndex) {
                slotList.add(new Slot());
            }
            slotList.get(flight.getDepart()).getFlights().add(flight);
        }

        Slot[] slots = slotList.toArray(new Slot[slotList.size()]);

        int maxGain = -1;
        Slot selectedSlot = null;
        for (int i = slots.length - 2; i >= 0; i--) {
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
            List<Flight> path = new ArrayList<>(selectedFlight == null ? selectedNextSlot.getPath().size() : selectedNextSlot.getPath().size() + 1);
            if (selectedFlight != null) path.add(selectedFlight);
            path.addAll(selectedNextSlot.getPath());
            currentSlot.setPath(path);

            if (currentSlot.getGain() > maxGain) {
                maxGain = currentSlot.getGain();
                selectedSlot = currentSlot;
            }
        }

        assert selectedSlot != null;
        return new Plan(maxGain, selectedSlot.getPath());
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
