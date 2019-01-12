package peter.azzie.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import peter.azzie.LineParser;

import static peter.azzie.AzzieLog.fail;
import static peter.azzie.AzzieLog.log;

public class EventBase {

    public final LocalDateTime timestamp;
    public final String typename;

    private String rawString = null;

    public EventBase(LocalDateTime timestamp, String typename){
        this.typename = typename;
        this.timestamp = timestamp;
    }

    protected List<String> formatLine(List<String> line){
        if (line == null) { line = new ArrayList<String>(); }
        line.add(timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        line.add(typename);
        return line;
    }

    protected String setRawString(String value){
        return rawString = value;
    }

    public String unsafeSetRawString(String value){
        return rawString = value;
    }

    @Override
    public String toString() {
        if (rawString == null) {
            List<String> formatted = formatLine(null);
            rawString = LineParser.encodeLine(formatted.toArray(new String[formatted.size()]));
        }
        return rawString;
    }

    public static EventBase fromString(String data){
        String[] fields = LineParser.decodeLine(data);
        if (fields.length < 2){
            fail("corrupted data line", data);
        }
        LocalDateTime when = LocalDateTime.parse(fields[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String type = fields[1];
        EventBase event = null;
        switch (type){
            case StartUserActivity.typename:
                if (fields.length <= 2) { fail("corrupted start event", data); }
                event = new StartUserActivity(when, fields[2]);
                break;
            default:
                log("undefined event type", type);
                event = new EventBase(when, type);
                break;
        }
        event.rawString = data;
        return event;
    }

    public boolean isStartUserActivity() { return typename.equals(StartUserActivity.typename); }

    public StartUserActivity toStartUserActivity() { return (StartUserActivity) this; }

}
