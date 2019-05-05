package peter.azzie.io.file;

import java.io.File;

import peter.azzie.io.DataLayer;
import peter.azzie.io.DataStorage;

import static peter.azzie.AzzieLog.fail;
import static peter.azzie.AzzieLog.log;

public class FileDataLayer implements DataLayer {

    public FileDataLayer(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public DataStorage getStorage(String filePath) {
        String path = basePath + File.separator + filePath;
        log("FileDataLayer: using storage file", path);
        return new FileDataStorage(new java.io.File(path));
    }

    @Override
    public void setupDirectory(String directoryPath) {
        String path = basePath + File.separator + directoryPath;
        File directory = new File(path);
        if (directory.exists()) {
            if (directory.isDirectory()){
                log("FileDataLayer: using existing directory", path);
            } else {
                fail("FileDataLayer: invalid directory path, it's a file and it exists and I don't want to override it", path);
            }
        }
        else {
            log("FileDataLayer: creating new directory", path);
            if (!directory.mkdirs()){
                fail("FileDataLayer: failed to create directory: ", path);
            }
        }
    }

    @Override
    public String makePath(String... items) {
        return String.join(File.separator, items);
    }

    private String basePath;

}
