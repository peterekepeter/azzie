package peter.azzie.io;

public interface DataLayer {
    DataStorage getStorage(String path);
    void setupDirectory(String directoryPath);
    String makePath(String... items);
}
