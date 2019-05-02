package peter.azzie.io.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import peter.azzie.event.EventBase;
import peter.azzie.io.DataStorage;

import static peter.azzie.AzzieLog.fail;

public class FileDataStorage implements DataStorage {

    private File file;

    public FileDataStorage(File file) {
        this.file = file;
    }

    @Override
    public Iterable<String> readAllLines() {
        return () -> startReading();
    }

    private Iterator<String> startReading() {
        if (!file.exists()){
            return new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return false;
                }
                @Override
                public String next() {
                    return null;
                }
            };
        }
        return new Iterator<String>() {

            boolean didReadNext;
            String next;

            @Override
            public boolean hasNext() {
                if (!didReadNext) {
                    didReadNext = true;
                    next = read();
                }
                return next != null;
            }

            @Override
            public String next() {
                if (didReadNext){
                    didReadNext = false;
                    return next;
                }
                next = read();
                return next;
            }

            private BufferedReader br;

            private String read(){
                try {
                    return br.readLine();
                } catch (IOException e) {
                    fail("File read failiure at ", file.getName());
                }
                return null;
            }
        };
    }

    @Override
    public void rewriteAllLines(Iterable<String> dataLines) {
        write(dataLines, false);
    }

    @Override
    public void appendLines(Iterable<String> dataLines) {
        write(dataLines, true);
    }

    private void write(Iterable<String> data, boolean append)
    {
        try(FileWriter fw = new FileWriter(file, append);
            BufferedWriter out = new BufferedWriter(fw);)
        {
            for (String line : data){
                out.write(line);
                out.write('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("File writing failed!");
        }
    }

}
