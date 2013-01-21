package net.ggtools.codestory.jajascript;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * User: Christophe Labouisse
 * Date: 21/01/13
 * Time: 07:05
 */
@ToString
@Getter
@EqualsAndHashCode(of = "name")
public class Flight {
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
