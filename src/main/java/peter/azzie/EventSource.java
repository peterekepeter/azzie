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
import java.util.List;

import peter.azzie.event.EventBase;

import static peter.azzie.AzzieLog.fail;
import static peter.azzie.AzzieLog.log;

public class EventSource {

    private final String filename;

    public EventSource(String filename){
        this.filename = filename;
    }

    public ArrayList<EventBase> readAll(){
        File file = new File(filename);
        ArrayList<EventBase> data = new ArrayList<>();
        if (!file.exists()){
            log("file for event source not found (this is okay, no events yet)", filename);
            return data;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            log("contents of", filename, "are:");
            while ((line = br.readLine()) != null) {
                // process the line.
                log(line);
                data.add(EventBase.fromString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("File read failiure at ", filename);
        }
        return data;
    }

    public void append(EventBase... events){
        try(FileWriter fw = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(fw);)
        {
            for (EventBase event : events){
                out.write(event.toString());
                out.write('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("File writing failed!");
        }
    }
}
