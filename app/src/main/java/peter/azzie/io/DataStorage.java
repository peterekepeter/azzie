package peter.azzie.io;

import java.util.Iterator;

public interface DataStorage {
    Iterable<String> readAllLines();
    void rewriteAllLines(Iterable<String> dataLines);
    void appendLines(Iterable<String> appendLines);
}
