package peter.azzie;

import org.junit.Test;

import java.time.LocalDateTime;

import peter.azzie.event.EventBase;
import peter.azzie.event.StartUserActivity;

import static org.junit.Assert.assertEquals;

public class EventBaseUnitTest {

    @Test
    public void canConvertBackAndForth() {
        String input = "2019-01-12T18:23:00 undo";
        EventBase event = EventBase.fromString(input);
        event.unsafeSetRawString(null);
        assertEquals(input, event.toString());
    }
    @Test
    public void maintainsParsedForm() {
        String input = "2019-01-12T18:23:00 custom event type";
        EventBase event = EventBase.fromString(input);
        assertEquals(input, event.toString());
    }
    @Test
    public void canCreateStartUserActivity(){
        LocalDateTime now = LocalDateTime.now();
        StartUserActivity activity = new StartUserActivity(now, "working");
        StartUserActivity event = (StartUserActivity) EventBase.fromString(activity.toString());
        assertEquals(now, event.timestamp);
        assertEquals(activity.typename, event.typename);
        assertEquals(activity.activity, event.activity);
    }
    @Test
    public void canParseStartUserActivity(){
        String input = "2019-01-12T19:19:00 start eating";
        StartUserActivity event = (StartUserActivity) EventBase.fromString(input);
        assertEquals("eating", event.activity);
        assertEquals(input, event.toString());
    }
}
