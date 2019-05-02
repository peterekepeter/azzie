package peter.azzie.io.file;

import peter.azzie.io.DataLayer;
import peter.azzie.io.DataStorage;

public class FileDataLayer implements DataLayer {

    @Override
    public DataStorage getStorage(String path) {
        return new FileDataStorage(new java.io.File(path));
    }
}
