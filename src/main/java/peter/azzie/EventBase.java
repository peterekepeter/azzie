package peter.azzie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static peter.azzie.AzzieLog.fail;
import static peter.azzie.AzzieLog.log;

public class EventBase {

    public LocalDateTime timestamp;
    public String typename;

    private String rawString = null;

    protected EventBase(){

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
        LocalDateTime timestamp = LocalDateTime.parse(fields[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String type = fields[1];
        switch (type){
            default:{
                log("undefined event type", type);
                EventBase event = new EventBase();
                event.rawString = data;
                event.typename = type;
                event.timestamp = timestamp;
                return event;
            }
        }
    }
}
