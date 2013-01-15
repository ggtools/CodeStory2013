package net.ggtools.codestory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Christophe Labouisse
 * Date: 15/01/13
 * Time: 14:43
 */
public enum Questions {

    UNKNOWN("", "Unknown question"),
    FIRST_MD("As tu bien recu le premier enonce(OUI/NON)", "OUI"),
    ALWAYS_YES("Est ce que tu reponds toujours oui(OUI/NON)", "NON"),
    MD_POST("Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)", "OUI"),
    HAPPY("Es tu heureux de participer(OUI/NON)", "OUI"),
    MAILING_LIST("Es tu abonne a la mailing list(OUI/NON)", "OUI"),
    EMAIL_ADDRESS("Quelle est ton adresse email", "consulting@labouisse.com");

    private final String question;
    private final String answer;

    Questions(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public static Questions lookup(String question) {
        for (Questions current : Questions.values()) {
            if (current.question.equals(question)) {
                return current;
            }
        }
        return UNKNOWN;
    }

    public String getAnswer() {
        return answer;
    }
}
