package peter.azzie;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static peter.azzie.AzzieLog.*;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private String fileBasePath;
    private Dataset dataset;

    private String makePath(String filePath)
    {
        return fileBasePath + File.separator + filePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_main);
        File directory = context.getFilesDir();

        log(File.separator);
        fileBasePath = directory.getAbsolutePath();
        dataset = new Dataset(makePath("test"));
    }
}
