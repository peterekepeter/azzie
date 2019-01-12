package peter.azzie.event;

import java.time.LocalDateTime;
import java.util.List;

public class StartUserActivity extends EventBase {

    public static final String typename = "start";
    public final String activity;

    public StartUserActivity(LocalDateTime timestamp, String activity) {
        super(timestamp, typename);
        this.activity = activity;
    }

    @Override
    protected List<String> formatLine(List<String> line) {
        line = super.formatLine(line);
        line.add(activity);
        return line;
    }
}
