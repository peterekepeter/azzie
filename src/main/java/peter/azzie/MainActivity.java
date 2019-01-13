package peter.azzie;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static peter.azzie.AzzieLog.log;

public class MainActivity extends AppCompatActivity {

    private Controller controller;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = Controller.getInstance();

        setContentView(R.layout.activity_main);
        File internalDirectory = getApplicationContext().getFilesDir();
        controller.setInternalDirectory(internalDirectory.getAbsolutePath());

        selectDataset(controller.getDatasets()[0]);

        // setup timer
        handler = new Handler();
        final Runnable updater = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                updateCurrentActivityView();
            }
        };
        handler.postDelayed(updater, 1000);
    }

    public void selectDataset(String which){
        controller.selectDataset(which);
        TextView datasetField = ((TextView)findViewById(R.id.datasetField));
        datasetField.setText(controller.getDatasetName());
        updateCurrentActivityView();
    }

    public void updateCurrentActivityView(){
        Controller.CurrentActivityInfo current = controller.getCurrentActivity();
        ((TextView)findViewById(R.id.currentActivityField)).setText(current.activity);
        ((TextView)findViewById(R.id.currentActivityTime)).setText(current.timeExpression);
    }

    public void onClickDatasetField(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Dataset");
        String[] datasets = controller.getDatasets();
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

        String[] testItems = controller.getSuggestedActivities();
        DialogChoiceHandler handler = new DialogChoiceHandler(testItems);
        builder.setSingleChoiceItems(testItems, -1, handler);
        builder.setNegativeButton("New Custom Activity", (dialog, which) -> {
            onSetCustomActivity();
            log(which);
        });
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            controller.setCurrentActivity(handler.selectedItem);
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
            controller.setCurrentActivity(text);
            log("received custom activity input", text);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> log("cancelled dialog"));

        builder.show();
    }
}
