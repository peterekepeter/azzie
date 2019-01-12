package peter.azzie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {

    public LocalDateTime timestamp;
    public String typename;

    private String rawString = null;

    protected String formatPartialString(){
        return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + typename;
    }

    protected String setRawString(String value){
        return rawString = value;
    }

    @Override
    public String toString() {
        if (rawString == null) { setRawString(formatPartialString()); }
        return rawString;
    }

    public static void fromString(String data){

    }
}
