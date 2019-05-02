package peter.azzie;

import android.util.EventLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import peter.azzie.event.EventBase;
import peter.azzie.io.DataStorage;

public class UserActivityStorage {

    private final DataStorage storage;

    public UserActivityStorage(DataStorage storage){
        this.storage = storage;
    }

    public ArrayList<EventBase> readAll(){
        ArrayList<EventBase> data = new ArrayList<>();
        for (String line : storage.readAllLines()){
            data.add(EventBase.fromString(line));
        }
        return data;
    }

    public void append(EventBase... events){
        Collection<String> lines = Arrays.stream(events)
                .map(eventBase -> eventBase.toString())
                .collect(Collectors.toList());
        storage.appendLines(lines);
    }
}
