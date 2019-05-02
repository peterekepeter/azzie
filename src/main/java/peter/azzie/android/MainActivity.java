package peter.azzie.android;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import peter.azzie.Azzie;
import peter.azzie.R;
import peter.azzie.io.DataLayer;
import peter.azzie.io.file.FileDataLayer;

import static peter.azzie.AzzieLog.log;

public class MainActivity extends AppCompatActivity {

    private Azzie azzie;
    private DataLayer dataLayer;

    private Handler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File internalDirectory = getApplicationContext().getFilesDir();

        dataLayer = new FileDataLayer(internalDirectory.getAbsolutePath());
        azzie = new Azzie(dataLayer);

        setContentView(R.layout.activity_main);

        selectDataset(azzie.getDatasets()[0]);

        // setup timer
        timerHandler = new Handler();
        final Runnable updater = new Runnable() {
            @Override
            public void run() {
                timerHandler.postDelayed(this, 1000);
                updateCurrentActivityView();
            }
        };
        timerHandler.postDelayed(updater, 1000);
    }

    public void selectDataset(String which){
        azzie.selectDataset(which);
        TextView datasetField = ((TextView)findViewById(R.id.datasetField));
        datasetField.setText(azzie.getDatasetName());
        updateCurrentActivityView();
    }

    public void updateCurrentActivityView(){
        Azzie.CurrentActivityInfo current = azzie.getCurrentActivity();
        ((TextView)findViewById(R.id.currentActivityField)).setText(current.activity);
        ((TextView)findViewById(R.id.currentActivityTime)).setText(current.timeExpression);
    }

    public void onClickDatasetField(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Dataset");
        String[] datasets = azzie.getDatasets();
        builder.setItems(datasets, (dialog, which) -> {
            selectDataset(datasets[which]);
        });
        builder.create().show();
    }

    private class DialogChoiceHandler implements DialogInterface.OnClickListener {

        public Button positiveButton;
        public String[] items;
        public String selectedItem = null;

        public DialogChoiceHandler(String[] items) {
            this.items = items;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            selectedItem = items[which];
            log(which, selectedItem);
            positiveButton.setEnabled(true);
        }
    }

    public void onClickActivityField(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start");

        String[] testItems = azzie.getSuggestedActivities();
        DialogChoiceHandler handler = new DialogChoiceHandler(testItems);
        builder.setSingleChoiceItems(testItems, -1, handler);
        builder.setNegativeButton("New Custom Activity", (dialog, which) -> {
            onSetCustomActivity();
            log(which);
        });
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            azzie.setCurrentActivity(handler.selectedItem);
            updateCurrentActivityView();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setEnabled(false);
        handler.positiveButton = positiveButton;
    }

    private void onSetCustomActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name of custom activity?");

        final EditText input = new EditText(this);
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Create And Set", (dialog, which) -> {
            String text = input.getText().toString();
            azzie.setCurrentActivity(text);
            log("received custom activity input", text);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> log("cancelled dialog"));

        builder.show();
    }
}
