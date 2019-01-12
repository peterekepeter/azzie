package peter.azzie;

import android.util.EventLog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventBaseUnitTest {

    @Test
    public void canConvertBackAndForth() { forceRebuild("2019-01-12T18:23:00 undo");}
    @Test
    public void maintainsParsedForm() { rebuild("2019-01-12T18:23:00 custom event type");}

    private void rebuild(String input){
        EventBase event = EventBase.fromString(input);
        assertEquals(input, event.toString());
    }

    private void forceRebuild(String input){
        EventBase event = EventBase.fromString(input);
        event.setRawString(null);
        assertEquals(input, event.toString());
    }
}
