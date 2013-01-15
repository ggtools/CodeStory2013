package net.ggtools.codestory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cele
 * Date: 15/01/13
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public enum Questions {

    UNKNOWN(""),
    EMAIL_ADDRESS("Quelle est ton adresse email");

    private String question;

    Questions(String question) {
        this.question = question;
    }

    public static Questions lookup(String question) {
        for (Questions current : Questions.values()) {
            if (current.question.equals(question)) {
                return current;
            }
        }
        return UNKNOWN;
    }
}
