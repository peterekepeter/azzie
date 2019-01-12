package peter.azzie;

import java.io.File;
import static peter.azzie.AzzieLog.*;

public class Dataset {

    private final File directory;

    public Dataset(String directoryPath) {
        directory = setupDatasetDirectory(directoryPath);
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

}
