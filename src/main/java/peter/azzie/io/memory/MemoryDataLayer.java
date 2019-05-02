package peter.azzie.io.memory;

import java.util.HashMap;

import peter.azzie.io.DataLayer;
import peter.azzie.io.DataStorage;

public class MemoryDataLayer implements DataLayer {

    HashMap<String, DataStorage> collections = new HashMap<>();

    @Override
    public DataStorage getStorage(String path) {
        if (!collections.containsKey(path)) {
            collections.put(path, makeEmtpyStorage());
        }
        return collections.get(path);
    }

    private DataStorage makeEmtpyStorage() {
        return new MemoryDataStorage();
    }
}
