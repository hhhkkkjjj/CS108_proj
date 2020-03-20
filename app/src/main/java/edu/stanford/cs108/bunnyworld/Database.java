package edu.stanford.cs108.bunnyworld;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.*;

public class Database {
    protected static ArrayList<String> getWorlds(SQLiteDatabase db){
        ArrayList<String> wordList = new ArrayList<>();
        //System.out.println("ghghghghgd");
        Cursor cursor = db.rawQuery("SELECT * FROM worldList;", null);
        //System.out.println("ghghghghg");
        //System.out.println(cursor.getCount());
        while(cursor.moveToNext()){
            String str = cursor.getString(0);
            if(str.length()!=0 && Character.isDigit(str.charAt(0))){
                str = "\""+str+"\"";
            }
            wordList.add(str);
        }
        return wordList;
    }

    protected static String getStartPage(SQLiteDatabase db, String name) {
        if(Character.isDigit(name.charAt(0))){
            name = "\""+name+"\"";
        }
        if (!getWorlds(db).contains(name)) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT startPage FROM worldList WHERE world = '" + name + "';",null);
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        } else {
            return null;
        }
    }

    protected static ArrayList<Shape> getWorldShapes(SQLiteDatabase db, String name){


        if(Character.isDigit(name.charAt(0))){
            name = "\""+name+"\"";
        }
        if(!getWorlds(db).contains(name)){
            return null;
        }
        ArrayList<Shape> shapeList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + name + ";",null);
        while (cursor.moveToNext()) {
            System.out.println("output");
            System.out.println(cursor.getString(10));
            System.out.println(cursor.getFloat(3));


            Shape sp = new Shape(cursor.getFloat(0), cursor.getFloat(1),
                    cursor.getFloat(2), cursor.getFloat(3), cursor.getInt(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getFloat(9), cursor.getString(10),
                    cursor.getInt(11), cursor.getInt(12), cursor.getInt(13),
                    cursor.getInt(14), cursor.getInt(15), cursor.getInt(16),
                    cursor.getInt(17), cursor.getInt(18));
            //System.out.println("succ");
            shapeList.add(new Shape(cursor.getFloat(0), cursor.getFloat(1),
                    cursor.getFloat(2), cursor.getFloat(3), cursor.getInt(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getFloat(9), cursor.getString(10),
                    cursor.getInt(11), cursor.getInt(12), cursor.getInt(13),
                    cursor.getInt(14), cursor.getInt(15), cursor.getInt(16),
                    cursor.getInt(17), cursor.getInt(18)));
        }

        return shapeList;
    }

    protected static void saveWorld(SQLiteDatabase db, String name, String startPage, List<Page> pageList) {
        if (getWorlds(db).contains(name)) {
            db.execSQL("UPDATE worldList SET startPage = '" + startPage + "' WHERE world = '" + name + "';");
            db.execSQL("DROP TABLE IF EXISTS " + name + ";");
        } else {
            db.execSQL("INSERT INTO worldList VALUES ('" + name + "', '" + startPage + "', NULL);");
        }
        if(Character.isDigit(name.charAt(0))){
            name = "\""+name+"\"";
        }

        System.out.println(name);

        System.out.println(name.getClass());
        db.execSQL("CREATE TABLE " + name + " (x FLOAT, y FLOAT, w FLOAT, h FLOAT, typ INTEGER, " +
                "sName TEXT, imName TEXT, scr TEXT, txt TEXT, tSize FLOAT, pName TEXT, mov INTEGER, " +
                "vis INTEGER, use INTEGER, pro INTEGER, tFont INTEGER, tBold INTEGER, tItalic INTEGER, ID INTEGER, _id INTEGER PRIMARY KEY AUTOINCREMENT);");
        System.out.println("ppppppppp");

        for (Page p : pageList) {
            for (Shape s : p.getShapes()) {
               System.out.println("take");
//

                System.out.println(s.getPageName()+"here");
//                System.out.println("take");
//                System.out.println(s.getInitialWidth());
//                System.out.println("take");
//                System.out.println(s.getHeight());
//                System.out.println("take");
//                System.out.println(s.getWidth());

                float x = s.getCenterX();
                float y = s.getCenterY();
                float h = s.getHeight();
                float w = s.getWidth();


                db.execSQL("INSERT INTO " + name + " VALUES (" + x + ", " + y + ", "
                        + w + ", " + h + ", " + s.getType() + ", '" + s.getShapeName()
                        + "', '" + s.getImageName() + "', '" + s.getRule() + "', '" + s.getText()
                        + "', " + s.getTextSize() + ", '" + s.getPageName() + "', " + s.getMov()
                        + ", " + s.getVis() + ", " + s.getUse() + ", " + s.getPro() + ", " + s.getTextFont()
                        + ", " + s.getTextBold() + ", " + s.getTextItalic() + ", " + s.getID() + ", NULL);");
            }
        }
    }

    protected static void deleteWorld(SQLiteDatabase db, String name) {

        if(name.length()!=0 && Character.isDigit(name.charAt(0))){
            name = "\""+name+"\"";
        }

        //System.out.println("check");
        //System.out.println(name);
        if (getWorlds(db).contains(name)) {
            //System.out.println(name+"geyit");
            if(name.length()!=0 && name.charAt(0)=='"'){
                name = name.substring(1,name.length()-1);
            }
            //System.out.println(name+" fgfg");
            db.execSQL("DELETE FROM worldList WHERE world = '" + name + "';");
            if(name.length()!=0 && Character.isDigit(name.charAt(0))){
                name = "\""+name+"\"";
            }

            db.execSQL("DROP TABLE IF EXISTS " + name + " ;");
        }else{
            System.out.println("Not found");
        }
    }

    protected static void emptyDB(SQLiteDatabase db) {
        for (String str : getWorlds(db)) {
            if(Character.isDigit(str.charAt(0))){
                str = "\""+str+"\"";
            }
            deleteWorld(db, str);
        }
    }



}
