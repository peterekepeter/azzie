package peter.azzie;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import peter.azzie.event.EventBase;
import peter.azzie.event.StartUserActivity;

import static peter.azzie.AzzieLog.log;

public class Controller {

    private static Controller instance = null;

    public static Controller getInstance(){
        if (instance == null){
            instance = new Controller();
        }
        return instance;
    }

    private String makePath(String filePath)
    {
        return internalDirectory + File.separator + filePath;
    }

    String internalDirectory;
    Dataset dataset;
    String datasetName;
    ArrayList<EventBase> eventList;

    String[] getDatasets(){
        return new String[] { "test", "live" };
    }

    public void setInternalDirectory(String absolutePath) {
        internalDirectory = absolutePath;
    }

    public void selectDataset(String which) {
        datasetName = which;
        String datasetPath = internalDirectory + File.separator + which;
        log("using dataset", which);
        dataset = new Dataset(datasetPath);
        // need to invalidate internal state
        eventList = null;
    }

    public String getDatasetName() {
        return datasetName;
    }

    private ArrayList<EventBase> getOrReadEvents(){
        if (eventList == null){
            eventList = dataset.readExistingActivities();
        }
        return eventList;
    }

    public String[] getSuggestedActivities() {
        if (eventList == null || eventList.size() == 0){
            return new String[] { "relaxing", "working", "sleeping", "commuting" };
        }
        StartUserActivity last = findLastActivity();
        StartUserActivity previous = null;
        HashMap<String, Integer> score = new HashMap<String, Integer>();
        for (EventBase event : eventList){
            if (event.isStartUserActivity()){
                StartUserActivity current = event.toStartUserActivity();
                if (current.activity.equals(last.activity)){
                    continue;
                }
                int scoreModifier = -1;
                if (previous != null && previous.activity.equals(last.activity)){
                    scoreModifier -= 2;
                }
                if (score.containsKey(current.activity)){
                    score.put(current.activity, score.get(current.activity) + scoreModifier);
                } else {
                    score.put(current.activity, scoreModifier);
                }
                previous = current;
            }
        }
        Set<String> keySet = score.keySet();
        String[] keys = score.keySet().toArray(new String[keySet.size()]);
        Arrays.sort(keys, Comparator.comparing(score::get));
        return keys;
    }

    private StartUserActivity findLastActivity(){
        ArrayList<EventBase> events = getOrReadEvents();
        for (int i=events.size()-1; i>=0; i--){
            EventBase event = events.get(i);
            if (event.isStartUserActivity()){
                StartUserActivity userActivity = event.toStartUserActivity();
                return userActivity;
            }
        }
        return null;
    }

    public class CurrentActivityInfo
    {
        String activity;
        String timeExpression;
        StartUserActivity event;

        public CurrentActivityInfo(String activity, String timeExpression, StartUserActivity event) {
            this.activity = activity;
            this.timeExpression = timeExpression;
            this.event = event;
        }
    }

    private static String humanReadableTimeDiff(LocalDateTime from, LocalDateTime to){
        long seconds = ChronoUnit.SECONDS.between(from, to);
        if (seconds <= 0) return "now";
        long minutes = seconds / 60;
        seconds -= minutes * 60;
        long hours = minutes / 60;
        minutes -= hours * 60;
        StringBuilder builder = new StringBuilder();
        if (hours > 0){
            builder.append(hours).append(" hour");
            if (hours != 1){ builder.append("s"); }
        }
        if (hours > 0 && minutes > 0) { builder.append(" and "); }
        if (minutes > 0 && hours < 24){
            builder.append(minutes).append(" minute");
            if (minutes != 1){ builder.append("s"); }
        }
        if (minutes > 0 && seconds > 0) { builder.append(" and "); }
        if (seconds > 0 && minutes < 60){
            builder.append(seconds).append(" second");
            if (seconds != 1) { builder.append("s"); }
        }
        return builder.toString();
    }

    public CurrentActivityInfo getCurrentActivity(){
        StartUserActivity event = findLastActivity();
        String timeExpression = "since forever";
        if (event == null){
            return new CurrentActivityInfo("not doing anything", timeExpression, event);
        }
        timeExpression = "for " + humanReadableTimeDiff(event.timestamp, now());
        return new CurrentActivityInfo(event.activity, timeExpression, event);
    }

    private LocalDateTime now(){
        return LocalDateTime.now();
    }

    public void setCurrentActivity(String activity){
        StartUserActivity last = findLastActivity();
        if (last != null && last.activity.equals(activity)){
            log("new activity is same as old one, no change, no write", activity);
            return;
        }
        StartUserActivity event = new StartUserActivity(now(), activity);
        if (eventList != null){
            eventList.add(event);
        }
        dataset.writeNewActivity(event);
    }
}
