package peter.azzie.io.memory;

import java.util.ArrayList;

import peter.azzie.io.DataStorage;

class MemoryDataStorage implements DataStorage {

    ArrayList<String> lines = new ArrayList<>();

    @Override
    public Iterable<String> readAllLines() {
        return lines;
    }

    @Override
    public void rewriteAllLines(Iterable<String> dataLines) {
        ArrayList<String> newList = new ArrayList<>();
        for (String line : dataLines){
            newList.add(line);
        }
        lines = newList;
    }

    @Override
    public void appendLines(Iterable<String> appendLines) {
        for (String line : appendLines){
            lines.add(line);
        }
    }
}
