package peter.azzie;

import java.io.File;
import java.util.ArrayList;

import peter.azzie.event.EventBase;
import peter.azzie.event.StartUserActivity;
import peter.azzie.io.DataLayer;

import static peter.azzie.AzzieLog.*;

public class Dataset {

    private final UserActivityStorage mainUserActivityStorage;

    public Dataset(DataLayer dataLayer, String directoryPath) {
        dataLayer.setupDirectory(directoryPath);
        String mainEventSourceFile = directoryPath + File.separator + "events.txt";
        log("main event source is ", mainEventSourceFile);
        mainUserActivityStorage = new UserActivityStorage(dataLayer.getStorage(mainEventSourceFile));
    }

    public ArrayList<EventBase> readExistingActivities(){
        return mainUserActivityStorage.readAll();
    }

    public void writeNewActivity(StartUserActivity activity){
        mainUserActivityStorage.append(activity);
    }

}
