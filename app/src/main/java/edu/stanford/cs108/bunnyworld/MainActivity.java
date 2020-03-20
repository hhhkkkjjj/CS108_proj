package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static SQLiteDatabase db;
    public int newGameID = 0;
    public int newEditGameID = 0;
    static private String newEditWorldName = "";
    static private String newWorldName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase("WorldsDB",MODE_PRIVATE,null);
        initDB();
    }

    public void initDB(){

        Cursor tablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='worldList';",
                null);
        System.out.println("db here1");
        if(tablesCursor.getCount() == 0){
            System.err.println("db here");
            String setupStr = "CREATE TABLE worldList ("
                    + "world TEXT, startPage TEXT, _id INTEGER PRIMARY KEY AUTOINCREMENT);";
            db.execSQL(setupStr);
        }
    }

    public void startBunny(View v){
        CustomView.myWorld = new World(0);
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);

    }

    public void editGame(View v){


        AlertDialog.Builder builder_exist_games = new AlertDialog.Builder(MainActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);


        arrayAdapter.add("Create a new Game: ");

        for (String world_name : Database.getWorlds(db)){
            arrayAdapter.add(world_name);
        }

        builder_exist_games.setTitle("Select a game: ");
        builder_exist_games.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                newEditGameID = which;
                newEditWorldName = arrayAdapter.getItem(which);
            }
        });

        final Intent intent = new Intent(this, EditActivity.class);

        builder_exist_games.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (newEditGameID == 0){

                    AlertDialog.Builder builder_enter_name = new AlertDialog.Builder(MainActivity.this);
                    builder_enter_name.setTitle("Enter the new name of the page: \n(Leaving blank will give default name)");

                    final EditText input_rename = new EditText(MainActivity.this);
                    builder_enter_name.setView(input_rename);


                    builder_enter_name.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (input_rename.getText().toString().isEmpty()){
                                World.setWorldName("Default_Name");
                            } else{
                                World.setWorldName(input_rename.getText().toString());
                            }
                            EditCustomView.myWorld = new World(1);
                            startActivity(intent);
                        }
                    });

                    builder_enter_name.show();

//                        View view = findViewById(R.id.editCustomView);
//                        startActivity(intent);

                } else {
//                    EditCustomView view = findViewById(R.id.editCustomView);
                    EditCustomView.myWorld = new World(2);
                    System.out.println("new world number " + 2);

                    startActivity(intent);
                }
                newEditGameID = 0;

            }
        });

        builder_exist_games.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (newEditGameID != 0){
                    Database.deleteWorld(db, arrayAdapter.getItem(newEditGameID));
                }

                newEditGameID = 0;
            }
        });


        builder_exist_games.show();

    }

    public void newGame(View v){

        AlertDialog.Builder builder_exist_games = new AlertDialog.Builder(MainActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);

        for (String world_name : Database.getWorlds(db)){
            arrayAdapter.add(world_name);
        }

        if (!arrayAdapter.isEmpty()){
            newWorldName = arrayAdapter.getItem(0);
        }

        builder_exist_games.setTitle("Select a game: ");
        builder_exist_games.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                newWorldName = arrayAdapter.getItem(which);
            }
        });

        final Intent intent = new Intent(this, PlayActivity.class);

        builder_exist_games.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!arrayAdapter.isEmpty()) {
                    CustomView.myWorld = new World(3);
                    startActivity(intent);
                }

            }
        });

        builder_exist_games.show();

    }

//    public void delGame(View v){
//
//    }

    public static String getNewEditWorldName(){
        return newEditWorldName;
    }

    public static String getNewWorldName(){
        return newWorldName;
    }
}
