package net.ggtools.codestory.jajascript;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Christophe Labouisse
 * Date: 21/01/13
 * Time: 07:05
 */
@RequiredArgsConstructor
@Getter
@Setter
class Slot {
    private final int start;

    private int gain;

    private final List<Flight> path = new ArrayList<>(OptimizeResolver.MAX_SLOTS);

    private final List<Flight> flights = new ArrayList<>();
}
