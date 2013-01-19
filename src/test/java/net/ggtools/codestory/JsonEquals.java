package net.ggtools.codestory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;
import java.util.List;

/**
 * User: Christophe Labouisse
 * Date: 19/01/13
 * Time: 11:29
 */
public class JsonEquals extends TypeSafeMatcher<String> {
    private final String reference;
    private ObjectMapper mapper = new ObjectMapper();

    public JsonEquals(String reference) {
        this.reference = reference;
    }

    @Override
    protected boolean matchesSafely(String other) {
        if (reference == null && other == null) return true;
        if (other == null) return false;
        try {
            List refValue = mapper.readValue(reference, List.class);
            List respValue = mapper.readValue(other, List.class);
            return refValue.equals(respValue);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("not JSon equal to ").appendValue(reference);
    }

    @Factory
    public static <T> Matcher<String> jsonEquals(String reference) {
        return new JsonEquals(reference);
    }
}
