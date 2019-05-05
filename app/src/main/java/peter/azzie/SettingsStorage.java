package peter.azzie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import peter.azzie.io.DataStorage;

import static peter.azzie.AzzieLog.log;

public class SettingsStorage {

    DataStorage storage;
    Map<String, String> data;
    boolean didRead = false;
    boolean didChange = false;

    SettingsStorage(DataStorage storage){
        this.storage = storage;
    }

    public String getString(String key) {
        makeSureDataWasRead();
        return data.getOrDefault(key, null);
    }

    public void setString(String key, String value) {
        String previousValue = getString(key);
        if (previousValue == value || previousValue != null && previousValue.equals(value)){
            return;
        };
        didChange = true;
        data.put(key, value);
    }

    public boolean hasChanges(){
        return didChange;
    }

    public void saveChanges(){
        if (hasChanges()){
            writeData();
        }
    }

    private void makeSureDataWasRead(){
        if (!didRead){
            readDataFromFile();
        }
    }

    private void readDataFromFile(){
        HashMap<String, String> data = new HashMap<>();
        for (String line : storage.readAllLines()){
            String[] values = LineParser.decodeLine(line);
            if (values.length != 2){
                log("SettingsStorage found corrupted data line", line);
                continue;
            }
            data.put(values[0], values[1]);
        }
        this.data = data;
        didRead = true;
        didChange = false;
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
        didChange = false;
    }

}
