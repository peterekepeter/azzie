package peter.azzie;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static peter.azzie.LineParser.*;

public class LineParserUnitTest {
    @Test
    public void safeStringOnSimpleCaseReturnsSame() { encode("abc","abc");}
    @Test
    public void havingSpaceIntroducesQuotes() { encode("\"a b\"", "a b");}
    @Test
    public void newlinesAreEscaped() { encode("\\n", "\n");}
    @Test
    public void backslashesAreEscaped() { encode("\\\\", "\\");}
    @Test
    public void doubleQuotesAreEscaped() { encode("\"he said \"\"hi!\"\"\"", "he said \"hi!\"");}
    @Test
    public void lineEncodeWorks() { encode("start project",  "start", "project"); }
    @Test
    public void eachFieldEscaped() { encode("start \"my project\"", "start", "my project"); }
    @Test
    public void decodingWorks() { decode("start project", "start", "project"); }
    @Test
    public void decodingWithEscapedContent() { decode("start \"my project\"", "start", "my project"); }
    @Test
    public void decodingWithDoubleQutes() { decode("st \"says \"\"hi\"\"\"", "st", "says \"hi\""); }
    @Test
    public void basicCase() { stable("2019-01-12T17:46", "start", "sleep"); }
    @Test
    public void needsEscaping() { stable("2019-01-12T17:46", "start", "work meeting"); }
    @Test
    public void needsMoreEscaping() { stable("define", "x", "Hello\nJohn!", "good stuff"); }

    private void stable(String... input){
        arrayEquals(input, decodeLine(encodeLine(input)));
    }

    private void decode(String input, String... expected){
        String[] decoded = decodeLine(input);
        arrayEquals(expected, decoded);
    }

    private void arrayEquals(String[] expected, String[] actual){
        assertEquals("it should have same length", expected.length, actual.length);
        for (int i=0; i<expected.length; i++){
            assertEquals("element mismatch at position" + i, expected[i], actual[i]);
        }
    }

    private void encode(String expected, String... input){
        assertEquals(expected, encodeLine(input));
    }

    private void encode(String expected, String input)
    {
        assertEquals(expected, encodeString(input));
    }

}
