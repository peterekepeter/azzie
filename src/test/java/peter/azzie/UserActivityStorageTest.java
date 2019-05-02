package peter.azzie;

import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

import peter.azzie.event.EventBase;
import peter.azzie.io.memory.MemoryDataLayer;


public class UserActivityStorageTest {

    @Test public void canReadEmtpyLog() {
        assertThat(createStorage().readAll()).isEmpty();
    }

    @Test public void appendWorks() {
        EventBase event = EventBase.fromString("2019-05-02T14:22 start working");
        assertThatCode(() -> createStorage().append(event)).doesNotThrowAnyException();
    }

    @Test public void canAppendAndRead(){
        UserActivityStorage storage = createStorage();
        // write
        {
            storage.append(EventBase.fromString("2019-05-02T14:22 start working"));
            storage.append(EventBase.fromString("2019-05-02T15:44 start resting"));
        }
        // assert
        ArrayList<EventBase> events = storage.readAll();
        assertThat(events.size()).isEqualTo(2);
        for(EventBase event : events) {
            assertThat(event.isStartUserActivity()).isTrue();
            assertThat(event.toStartUserActivity().activity).matches("working|resting");
        }
    }

    private UserActivityStorage createStorage(){
        return new UserActivityStorage(new MemoryDataLayer().getStorage("/activity.log"));
    }
}