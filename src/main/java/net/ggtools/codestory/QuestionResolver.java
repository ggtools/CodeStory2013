package net.ggtools.codestory;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Christophe Labouisse
 * Date: 16/01/13
 * Time: 08:31
 */
public class QuestionResolver implements Resolver {
    @Override
    public String solve(HttpServletRequest request) {
        String q = request.getParameter("q");
        QUESTION question = QUESTION.lookup(q);
        return question.getAnswer();
    }

    @Override
    public boolean match(HttpServletRequest request) {
        return "/".equals(request.getServletPath()) && request.getParameter("q") != null;
    }

    private enum QUESTION {

        ONE_PLUS_ONE("1 1", "2"),
        FIRST_MD("As tu bien recu le premier enonce(OUI/NON)", "OUI"),
        ALWAYS_YES("Est ce que tu reponds toujours oui(OUI/NON)", "NON"),
        MD_POST("Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)", "OUI"),
        HAPPY("Es tu heureux de participer(OUI/NON)", "OUI"),
        MAILING_LIST("Es tu abonne a la mailing list(OUI/NON)", "OUI"),
        EMAIL_ADDRESS("Quelle est ton adresse email", "consulting@labouisse.com"),
        UNKNOWN("", "Unknown question");
        private final String question;
        private final String answer;

        QUESTION(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public static QUESTION lookup(String question) {
            for (QUESTION current : QUESTION.values()) {
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
}
