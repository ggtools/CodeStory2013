package net.ggtools.codestory.jajascript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * User: Christophe Labouisse
 * Date: 06/02/13
 * Time: 02:46
 */
public class PlanCalculator {

    private final Slot[] slots;

    private final Collection<Flight> flights;

    public PlanCalculator(Flight[] flights) {
        this(Arrays.asList(flights));
    }

    public PlanCalculator(Collection<Flight> flights) {
        this.flights = flights;
        int maxIndex = 0;
        for (Flight flight : flights) {
            if (flight.getEnd() > maxIndex) {
                maxIndex = flight.getEnd();
            }
        }

        slots = new Slot[maxIndex + 1];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot();
        }

        for (Flight flight : flights) {
            int wantedIndex = flight.getDepart();
            slots[flight.getDepart()].getFlights().add(flight);
        }
    }

    public Plan computePlan() {
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
            if (selectedFlight != null) currentSlot.setSelectedFlight(selectedFlight);
            currentSlot.setNextSlot(selectedNextSlot);

            if (currentSlot.getGain() > maxGain) {
                maxGain = currentSlot.getGain();
                selectedSlot = currentSlot;
            }
        }

        assert selectedSlot != null;
        // Compute the actual path length to minimize memory consumption.
        int pathLength = 0;
        Slot currentSlot = selectedSlot;
        while (currentSlot != null) {
            if (currentSlot.getSelectedFlight() != null) {
                pathLength++;
            }
            currentSlot = currentSlot.getNextSlot();
        }
        List<Flight> path = new ArrayList<>(pathLength);
        currentSlot = selectedSlot;
        while (currentSlot != null) {
            if (currentSlot.getSelectedFlight() != null) {
                path.add(currentSlot.getSelectedFlight());
            }
            currentSlot = currentSlot.getNextSlot();
        }

        return new Plan(maxGain, path);
    }
}
