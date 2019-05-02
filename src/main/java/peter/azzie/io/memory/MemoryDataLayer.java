package peter.azzie.io.memory;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import peter.azzie.io.DataLayer;
import peter.azzie.io.DataStorage;

public class MemoryDataLayer implements DataLayer {

    Set<String> existingDirectories;

    public MemoryDataLayer() {
        existingDirectories = new HashSet<String>();
        existingDirectories.add(""); // root
    }

    private void validateParentDir(String path){
        int lastIndex = path.lastIndexOf('/');
        if (lastIndex != -1){
            String dir = path.substring(0, lastIndex);
            if (!existingDirectories.contains(dir)){
                throw new RuntimeException("directory does not exist");
            }
        }
    }
    HashMap<String, DataStorage> collections = new HashMap<>();

    @Override
    public DataStorage getStorage(String path) {
        validateParentDir(path);
        if (!collections.containsKey(path)) {
            collections.put(path, makeEmtpyStorage());
        }
        return collections.get(path);
    }

    @Override
    public void setupDirectory(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        existingDirectories.add(path);
    }

    private DataStorage makeEmtpyStorage() {
        return new MemoryDataStorage();
    }

    @Override
    public String makePath(String... items) {
        return String.join("/", items);
    }
}
