package peter.azzie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import peter.azzie.event.EventBase;
import peter.azzie.event.StartUserActivity;

import static peter.azzie.AzzieLog.*;

public class Dataset {

    private final File directory;
    private final EventSource mainEventSource;

    public Dataset(String directoryPath) {
        directory = setupDatasetDirectory(directoryPath);
        String mainEventSourceFile = directoryPath + File.separator + "events.txt";
        log("main event source is ", mainEventSourceFile);
        mainEventSource = new EventSource(mainEventSourceFile);
    }

    private static File setupDatasetDirectory(String directoryPath){
        File directory = new File(directoryPath);
        if (directory.exists()) {
            if (directory.isDirectory()){
                log("using existing dataset directory", directory);
            } else {
                fail("invalid directory path, it's a file and it exists and I don't want to override it", directoryPath);
            }
        }
        else {
            log("using new dataset directory", directory);
            if (!directory.mkdirs()){
                fail("failed to create directory: ", directoryPath);
            }
        }
        if (directory.isDirectory() == false) { fail("expecting", directory, "to be a directory"); }
        return directory;
    }

    public ArrayList<EventBase> readExistingActivities(){
        return mainEventSource.readAll();
    }

    public void writeNewActivity(StartUserActivity activity){
        mainEventSource.append(activity);
    }

}
