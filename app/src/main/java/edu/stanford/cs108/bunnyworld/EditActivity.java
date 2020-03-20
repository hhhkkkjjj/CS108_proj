package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static edu.stanford.cs108.bunnyworld.EditCustomView.*;


public class EditActivity extends AppCompatActivity {

    private String new_page_name = "";
    private int jump_to_page = 0;
    private int delete_page = 0;
    private int delete_script = 0;
    private ArrayList<ArrayList<Integer>> allRules;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);
        db = openOrCreateDatabase("WorldsDB", MODE_PRIVATE, null);
        Shape.context = this;
        setTitle("Current Page: " + myWorld.getPages().get(currentPage).getName());
    }

    public void onShape(View view){
        PopupMenu shapePopUp = new PopupMenu(this, view);

        shapePopUp.getMenuInflater().inflate(R.menu.popup_shape, shapePopUp.getMenu());
        shapePopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        EditActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();

                if(selectedShape == null){
                    AlertDialog.Builder dialog_not_select = new AlertDialog.Builder(EditActivity.this);
                    dialog_not_select.setTitle("Shape not selected!");
                    dialog_not_select.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialogDelete = dialog_not_select.create();
                    dialogDelete.show();
                }else{
                    switch (item.getItemId()){
                        case R.id.setPropoerty:
                            AlertDialog.Builder builder_property = new AlertDialog.Builder(EditActivity.this);
                            builder_property.setTitle("Shape properties: ");


                            LayoutInflater inflater = LayoutInflater.from(EditActivity.this);
                            View viewEditProperty = inflater.inflate(R.layout.editproperty, null);
                            builder_property.setView(viewEditProperty);
                            float cur_x = selectedShape.getCenterX();
                            float cur_y = selectedShape.getCenterY();
                            float cur_width = selectedShape.getWidth();
                            float cur_height = selectedShape.getHeight();
                            String cur_shapeName = selectedShape.getShapeName();
                            float cur_fontSize = selectedShape.getTextSize();
                            boolean cur_move = selectedShape.getMovable();
                            boolean cur_visible = selectedShape.getVisibility();

                            final EditText new_x = viewEditProperty.findViewById(R.id.x);
                            final EditText new_y = viewEditProperty.findViewById(R.id.y);
                            final EditText new_width = viewEditProperty.findViewById(R.id.width);
                            final EditText new_height = viewEditProperty.findViewById(R.id.height);
                            final EditText new_shapeName = viewEditProperty.findViewById(R.id.shapeName);
                            final EditText new_fontSize = viewEditProperty.findViewById(R.id.fontSize);
                            final Switch new_move = viewEditProperty.findViewById(R.id.isMovable);
                            final Switch new_visiable = viewEditProperty.findViewById(R.id.isVisible);
                            final Switch new_bold = viewEditProperty.findViewById(R.id.isBold);
                            final Switch new_italic = viewEditProperty.findViewById(R.id.isItalic);



                            new_x.setText(Float.toString(cur_x));
                            new_y.setText(Float.toString(cur_y));
                            new_width.setText(Float.toString(cur_width));
                            new_height.setText(Float.toString(cur_height));
                            new_shapeName.setText(cur_shapeName);
                            new_fontSize.setText(Float.toString(cur_fontSize));
                            new_move.setChecked(selectedShape.getMovable());
                            new_visiable.setChecked(selectedShape.getVisibility());
                            new_bold.setChecked(selectedShape.getBold());
                            new_italic.setChecked(selectedShape.getItalic());


                            builder_property.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    float newX = Float.valueOf(new_x.getText().toString());
                                    float newY = Float.valueOf(new_y.getText().toString());
                                    float newWidth = Float.valueOf(new_width.getText().toString());
                                    float newHeight = Float.valueOf(new_height.getText().toString());
                                    String newShapeName = new_shapeName.getText().toString();
                                    float newFontSize = Float.valueOf(new_fontSize.getText().toString());
                                    View view = findViewById(R.id.editCustomView);
                                    selectedShape.setParas(newX, newY, newWidth, newHeight,
                                            selectedShape.getCurrPageNum(), newFontSize, new_move.isChecked(), new_visiable.isChecked(), new_italic.isChecked(), new_bold.isChecked());
                                    selectedShape.setShapeName(newShapeName);
                                    view.invalidate();
                                }
                            });

                            builder_property.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    View view = findViewById(R.id.editCustomView);
                                    view.invalidate();
                                }
                            });

                            AlertDialog dialog = builder_property.create();
                            dialog.show();
                            break;

                        case R.id.shapeDelete:
                            myWorld.rmShape(selectedShape);
                            myWorld.getPages().get(currentPage).removeShape(selectedShape);
                            selectedShape = null;
                            View view = findViewById(R.id.editCustomView);
                            view.invalidate();
                            break;

                        case R.id.editText:
                            Toast.makeText(
                                    EditActivity.this,
                                    "You Clicked : " + item.getTitle(),
                                    Toast.LENGTH_SHORT
                            ).show();
                            if(selectedShape.getType() != Shape.TEXT){
                                AlertDialog.Builder dialog_cannot_edit = new AlertDialog.Builder(EditActivity.this);
                                dialog_cannot_edit.setTitle("Please select a text shape!");
                                dialog_cannot_edit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog dialogDelete = dialog_cannot_edit.create();
                                dialogDelete.show();
                            }else{
                                AlertDialog.Builder builder_rename = new AlertDialog.Builder(EditActivity.this);
                                builder_rename.setTitle("Enter the new text: \n(Leaving blank will give default text)");

                                final EditText input_rename = new EditText(EditActivity.this);


                                builder_rename.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String new_text = input_rename.getText().toString();
                                        if(new_text.equals("")){
                                            new_text = "Text";
                                        }
                                        selectedShape.setText(new_text);
                                        View view = findViewById(R.id.editCustomView);
                                        view.invalidate();
                                    }
                                });

                                builder_rename.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder_rename.setView(input_rename);

                                builder_rename.show();
                            }

                            break;
                    }
                }




                return true;
            }
        });


        shapePopUp.show();

    }

    public void onPage(final View view){
        PopupMenu PagePopUp = new PopupMenu(this, view);

        PagePopUp.getMenuInflater().inflate(R.menu.popup_page, PagePopUp.getMenu());

        PagePopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        EditActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();


                switch (item.getItemId()){
                    case R.id.createPage:
                        Toast.makeText(
                                EditActivity.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                        builder.setTitle("Enter the name of the page: \n(Leaving blank will give default name)");

                        final EditText input = new EditText(EditActivity.this);


                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new_page_name = input.getText().toString();
                                if(new_page_name.equals(""))
                                    new_page_name = "page" + (myWorld.getPages().size()+1);
                                while(true){
                                    boolean same_name = false;
                                    for(int i = 0; i < myWorld.getPages().size(); ++i){
                                        System.out.println("index:" + i + " " + myWorld.getPages().get(i).getName());
                                        if(new_page_name.equals(myWorld.getPages().get(i).getName())){
                                            new_page_name += "*";
                                            same_name = true;
                                        }
                                    }
                                    if(!same_name){ break; }
                                }
                                myWorld.addPage(new Page(new_page_name));
                                View view = findViewById(R.id.editCustomView);
                                currentPage = myWorld.getPages().size()-1;
                                setTitle("Current Page: " + myWorld.getPages().get(currentPage).getName());
                                view.invalidate();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.setView(input);
                        builder.show();
                        break;

                    case R.id.jumpToPage:
                        Toast.makeText(
                                EditActivity.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();

                        AlertDialog.Builder builder_jump = new AlertDialog.Builder(EditActivity.this);

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);

                        for (Page p : myWorld.getPages()){
                            arrayAdapter.add(p.getName());
                        }

                        builder_jump.setTitle("Select a page");

                        builder_jump.setSingleChoiceItems(arrayAdapter, currentPage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                jump_to_page = which;
                            }
                        });

                        builder_jump.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                View view = findViewById(R.id.editCustomView);
                                currentPage = jump_to_page;
                                setTitle("Current Page: " + myWorld.getPages().get(currentPage).getName());
                                view.invalidate();
                            }
                        });

                        builder_jump.show();
                        break;

                    case R.id.deletePage:
                        AlertDialog.Builder builder_delete = new AlertDialog.Builder(EditActivity.this);

                        final ArrayAdapter<String> arrayAdapter_delete = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);

                        for (Page p : myWorld.getPages())
                            arrayAdapter_delete.add(p.getName());

                        builder_delete.setTitle("Select a page");

                        builder_delete.setSingleChoiceItems(arrayAdapter_delete, delete_page, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditActivity.this, "Position: " + which + " Value: " +
                                        arrayAdapter_delete.getItem(which), Toast.LENGTH_LONG).show();
                                delete_page = which;
                            }
                        });

                        builder_delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                View view = findViewById(R.id.editCustomView);
                                if(delete_page == 0){
                                    AlertDialog.Builder dialog_cannot_delete = new AlertDialog.Builder(EditActivity.this);
                                    dialog_cannot_delete.setTitle("Deleting the starting page is not allowed!");
                                    dialog_cannot_delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog dialogDelete = dialog_cannot_delete.create();
                                    dialogDelete.show();
                                }else{
                                    if(delete_page <= currentPage){
                                        currentPage -= 1;
                                    }
                                    System.out.println("To be delete:" + delete_page);
                                    myWorld.rmPage(delete_page);
                                    delete_page = currentPage;
                                    setTitle("Current Page: " + myWorld.getPages().get(currentPage).getName());
                                    view.invalidate();
                                }

                            }
                        });

                        builder_delete.show();
                        break;

                    case R.id.renamePage:
                        Toast.makeText(
                                EditActivity.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        if(currentPage == 0){
                            AlertDialog.Builder dialog_cannot_delete = new AlertDialog.Builder(EditActivity.this);
                            dialog_cannot_delete.setTitle("Renaming the starting page is not allowed!");
                            dialog_cannot_delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialogDelete = dialog_cannot_delete.create();
                            dialogDelete.show();
                        }else{
                            AlertDialog.Builder builder_rename = new AlertDialog.Builder(EditActivity.this);
                            builder_rename.setTitle("Enter the new name of the page: \n(Leaving blank will give default name)");

                            final EditText input_rename = new EditText(EditActivity.this);


                            builder_rename.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new_page_name = input_rename.getText().toString();
                                    if(new_page_name.equals("")){
                                        new_page_name = "page" + (myWorld.getPages().size()+1);
                                    }
                                    while(true){
                                        boolean same_name = false;
                                        for(int i = 0; i < myWorld.getPages().size(); ++i){
                                            System.out.println("index:" + i + " " + myWorld.getPages().get(i).getName());
                                            if(new_page_name.equals(myWorld.getPages().get(i).getName())){
                                                new_page_name += "*";
                                                same_name = true;
                                            }
                                        }
                                        if(!same_name){ break; }
                                    }
                                    Page curPage = myWorld.getPages().get(currentPage);
                                    curPage.setName(new_page_name);
                                    View view = findViewById(R.id.editCustomView);
                                    setTitle("Current Page: " + myWorld.getPages().get(currentPage).getName());
                                    view.invalidate();
                                }
                            });

                            builder_rename.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder_rename.setView(input_rename);

                            builder_rename.show();
                        }

                        break;
                }

                return true;
            }
        });


        PagePopUp.show();


    }

    public void onScript(final View view){
        PopupMenu scriptPopUp = new PopupMenu(this, view);

        scriptPopUp.getMenuInflater().inflate(R.menu.popup_script, scriptPopUp.getMenu());

        scriptPopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        EditActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();

                switch (item.getItemId()){
                    case R.id.createScript:
//                        Toast.makeText(
//                                EditActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                        if(selectedShape==null){
                            AlertDialog.Builder dialog_not_select = new AlertDialog.Builder(EditActivity.this);
                            dialog_not_select.setTitle("Please select or create a shape!");
                            dialog_not_select.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = dialog_not_select.create();
                            dialog.show();
                        }else{
                            create(view);
                        }
                        break;

                    case R.id.editScript:
                        if(selectedShape==null){
                            AlertDialog.Builder dialog_not_select = new AlertDialog.Builder(EditActivity.this);
                            dialog_not_select.setTitle("Please select or create a shape!");
                            dialog_not_select.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = dialog_not_select.create();
                            dialog.show();
                        }else{
                            AlertDialog.Builder builder0 = new AlertDialog.Builder(EditActivity.this);

                            final ArrayAdapter<String> arrayAdapter0 = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                            allRules = selectedShape.getArrayRules();
                            if(allRules.size() == 0){
                                AlertDialog.Builder dialog_no_script = new AlertDialog.Builder(EditActivity.this);
                                dialog_no_script.setTitle("This shape does not have script!");
                                dialog_no_script.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog dialogDelete = dialog_no_script.create();
                                dialogDelete.show();
                            }else{
                                for(int i = 0; i < allRules.size(); ++i){
                                    arrayAdapter0.add(displayRule(allRules.get(i)));
                                }

                                builder0.setTitle("Edit a rule");
                                final int checked = 0;
                                builder0.setSingleChoiceItems(arrayAdapter0, checked, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter0.getItem(which), Toast.LENGTH_LONG).show();
                                        delete_script = which;
                                    }
                                });

                                builder0.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(allRules.size()<=1){
                                            selectedShape.clearRule();
                                        }else{
                                            allRules.remove(delete_script);
                                            selectedShape.updateRules(allRules);
                                        }
                                        create(view);

                                    }
                                });
                                builder0.show();
                            }
                        }
                        break;

                    case R.id.deleteScript:
                        if(selectedShape==null){
                            AlertDialog.Builder dialog_not_select = new AlertDialog.Builder(EditActivity.this);
                            dialog_not_select.setTitle("Please select or create a shape!");
                            dialog_not_select.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = dialog_not_select.create();
                            dialog.show();
                        }else{
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(EditActivity.this);

                            final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                            allRules = selectedShape.getArrayRules();
                            if(allRules.size() == 0){
                                AlertDialog.Builder dialog_no_script = new AlertDialog.Builder(EditActivity.this);
                                dialog_no_script.setTitle("This shape does not have script!");
                                dialog_no_script.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog dialogDelete = dialog_no_script.create();
                                dialogDelete.show();
                            }else{
                                for(int i = 0; i < allRules.size(); ++i){
                                    arrayAdapter1.add(displayRule(allRules.get(i)));
                                }

                                builder1.setTitle("Delete a rule");
                                final int checked = 0;
                                builder1.setSingleChoiceItems(arrayAdapter1, checked, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter1.getItem(which), Toast.LENGTH_LONG).show();
                                        delete_script = which;
                                    }
                                });

                                builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(allRules.size()<=1){
                                            selectedShape.clearRule();
                                        }else{
                                            allRules.remove(delete_script);
                                            selectedShape.updateRules(allRules);
                                        }

                                    }
                                });
                                builder1.show();
                            }
                        }
                        break;

                }

                return true;
            }
        });


        scriptPopUp.show();
    }

    public void onSave(View view){
        System.err.println(myWorld.getWorldName()+ "get name");
        Database.saveWorld(db, myWorld.getWorldName(), "page1", myWorld.getPages());
//        ArrayList<Shape> shapeList = Database.getWorldShapes(db, "wordList");
//        System.out.println(shapeList);
        AlertDialog.Builder dialog_save = new AlertDialog.Builder(EditActivity.this);
        dialog_save.setTitle("Successfully save a world!");
        dialog_save.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = dialog_save.create();
        dialog.show();
    }

    public void create(View view){
        final ArrayList<Integer> rule = new ArrayList<>();
        rule.add(0); rule.add(0); rule.add(0); rule.add(0);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditActivity.this);

        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);

        arrayAdapter1.add("On Click");
        arrayAdapter1.add("On Drop");
        arrayAdapter1.add("On Enter");
        builder1.setTitle("Select a trigger");
        final int checked = 0;
        builder1.setSingleChoiceItems(arrayAdapter1, checked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter1.getItem(which), Toast.LENGTH_LONG).show();
                rule.set(0, which);
            }
        });

        builder1.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlertDialog.Builder builder2 = new AlertDialog.Builder(EditActivity.this);

                final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);

                arrayAdapter2.add("Goto");
                arrayAdapter2.add("Play");
                arrayAdapter2.add("Hide");
                arrayAdapter2.add("Show");
                builder2.setTitle("Select a trigger");
                final int checked = 0;
                builder2.setSingleChoiceItems(arrayAdapter2, checked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter2.getItem(which), Toast.LENGTH_LONG).show();
                        rule.set(1, which);
                    }
                });

                builder2.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(EditActivity.this);

                        final ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);

                        switch(rule.get(1)){
                            case 0: //goto
                                for(int i = 0; i < myWorld.getPages().size(); ++i){
                                    arrayAdapter3.add(myWorld.getPages().get(i).getName());
                                }
                                break;
                            case 1: // play
                                for(int i = 0; i < Shape.getSoundsList().size(); ++i){
                                    arrayAdapter3.add(Shape.getSoundsList().get(i));
                                }
                                break;
                            case 2:
                            case 3:
                                for(int i = 0; i < myWorld.getShapes().size(); ++i){
                                    arrayAdapter3.add(myWorld.getShapes().get(i).getShapeName());
                                }
                                break;
                        }
                        builder3.setTitle("Select a trigger");
                        final int checked = 0;
                        builder3.setSingleChoiceItems(arrayAdapter3, checked, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter3.getItem(which), Toast.LENGTH_LONG).show();
                                rule.set(2, which);
                            }
                        });

                        builder3.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(rule.get(0) == 1) {
                                    AlertDialog.Builder builder4 = new AlertDialog.Builder(EditActivity.this);

                                    final ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);

                                    for(int i = 0; i < myWorld.getShapes().size(); ++i){
                                        arrayAdapter4.add(myWorld.getShapes().get(i).getShapeName());
                                    }
                                    builder4.setTitle("Select a trigger");
                                    final int checked = 0;
                                    builder4.setSingleChoiceItems(arrayAdapter4, checked, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter2.getItem(which), Toast.LENGTH_LONG).show();
                                            rule.set(3, which);

                                        }
                                    });

                                    builder4.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            selectedShape.addRule(rule);
                                        }
                                    });

                                    builder4.show();
                                }else{
                                    selectedShape.addRule(rule);
                                }
                            }
                        });

                        builder3.show();
                    }
                });

                builder2.show();
            }
        });

        builder1.show();
    }

    public String displayRule(ArrayList<Integer> rule){
        String s = "";
        String action1 = "";
        String action2 = "";
        String target1 = "";
        String target2 = "";
        switch (rule.get(0)){
            case 0:
                action1 = "On Click ";
                break;
            case 1:
                action1 = "On Drop ";
                target2 = myWorld.getShapes().get(rule.get(3)).getShapeName();
                break;
            case 2:
                action1 = "On Enter ";
                break;
        }
        switch (rule.get(1)){
            case 0:
                action2 = "Go to ";
                target1 = myWorld.getPages().get(rule.get(2)).getName();
                break;
            case 1:
                action2 = "Play ";
                target1 = Shape.getSoundsList().get(rule.get(2));
                break;
            case 2:
                action2 = "Hide ";
                target1 = myWorld.getShapes().get(rule.get(2)).getShapeName();
                break;
            case 3:
                action2 = "Show ";
                target1 = myWorld.getShapes().get(rule.get(2)).getShapeName();
                break;
        }
        s = action1 + action2 + target1 + " " + target2;
        return s;
    }
}


