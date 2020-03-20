package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static edu.stanford.cs108.bunnyworld.Shape.context;

public class PlayActivity extends AppCompatActivity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        context = this;

        db = openOrCreateDatabase("WorldsDB", MODE_PRIVATE, null);

        mp = MediaPlayer.create(context, R.raw.bgm);
        mp.setVolume((float)0.1,(float)0.1);
        mp.setLooping(true);
        mp.start();
    }

    private MediaPlayer mp;

    @Override
    protected void onPause() {
        mp.stop();
        mp.release();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Change Background");
        return true;
    }

    private int backgroundIdx = 0;

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case 1:

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PlayActivity.this, android.R.layout.select_dialog_singlechoice);
                List<String> backgrounds = new ArrayList<>(Arrays.asList("Sky", "Forest", "Ice", "Desert"));
                for (String s : backgrounds) {
                    arrayAdapter.add(s);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
                builder.setTitle("Choose Background");
                builder.setCancelable(true);

                backgroundIdx = 0;
                builder.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        backgroundIdx = i;
                    }
                });

                builder.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.err.println(backgroundIdx);
                        CustomView view = findViewById(R.id.customView);
                        view.changeBackground(backgroundIdx);
                        view.invalidate();

                    }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
