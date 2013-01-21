package net.ggtools.codestory.jajascript;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * User: Christophe Labouisse
 * Date: 21/01/13
 * Time: 07:06
 */
@Getter
@ToString
@AllArgsConstructor
public class Plan {
    private final int gain;

    private final List<Flight> path;
}
