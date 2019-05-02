package peter.azzie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import peter.azzie.io.DataStorage;
import peter.azzie.io.Settings;

import static peter.azzie.AzzieLog.log;

public class FileSettings implements Settings {

    DataStorage storage;

    FileSettings(DataStorage storage){
        this.storage = storage;
    }

    boolean didRead = false;
    Map<String, String> data;

    @Override
    public String getString(String key) {
        makeSureDataWasRead();
        return data.getOrDefault(key, null);
    }

    @Override
    public void setString(String key, String value) {
        makeSureDataWasRead();
        data.put(key, value);
        writeData();
    }

    private void makeSureDataWasRead(){
        if (!didRead){
            readDataFromFile();
            didRead = true;
        }
    }

    private void readDataFromFile(){
        HashMap<String, String> data = new HashMap<>();
        for (String line : storage.readAllLines()){
            String[] values = LineParser.decodeLine(line);
            if (values.length != 2){
                log("FileSettings found corrupted data line", line);
                continue;
            }
            data.put(values[0], values[1]);
        }
        this.data = data;
    }

    private void writeData(){
        ArrayList<String> values = new ArrayList<>();
        for(Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String encoded = LineParser.encodeLine(key, value);
            values.add(encoded);
        }
        storage.rewriteAllLines(values);
    }

}
