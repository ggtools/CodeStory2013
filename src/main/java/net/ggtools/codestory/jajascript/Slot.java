package net.ggtools.codestory.jajascript;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Christophe Labouisse
 * Date: 21/01/13
 * Time: 07:05
 */
@Getter
@Setter
class Slot {
    private List<Flight> path = Collections.emptyList();

    private final List<Flight> flights = new ArrayList<>();

    private int gain;
}
