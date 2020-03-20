package edu.stanford.cs108.bunnyworld;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class World {
    private ArrayList<Page> pages;
    private Inventory bag;
    static SQLiteDatabase db;
    private ArrayList<Shape> allShapes;
    static String worldName;

    World(int bunny){
        if(bunny == 0){
            pages = new ArrayList<>();
            allShapes = new ArrayList<>();

            for(int i = 0; i < 5; i++){
                Page p = new Page(Integer.toString(i));
                pages.add(p);
            }


            Shape door0 = new Shape(200.f, 500.f, 100.f, 100.f);
            Shape door1 = new Shape(800.f, 500.f, 100.f, 100.f);
            Shape door2 = new Shape(1400.f, 500.f, 100.f, 100.f);
            door0.addRule(new ArrayList<Integer>(Arrays.asList(0, 0, 1, -1)));
            door1.addRule(new ArrayList<Integer>(Arrays.asList(0, 0, 2, -1)));
            door2.addRule(new ArrayList<Integer>(Arrays.asList(0, 0, 3, -1)));
            door0.setID(0);
            door1.setID(1);
            door2.setID(2);
            door0.setImageName("door");
            door1.setImageName("door");
            door2.setImageName("door");
            allShapes.add(door0); // #0
            allShapes.add(door1); // #1
            allShapes.add(door2); // #2
            door1.setVisible(false);
//        door0.setGoToPage(1);
//            door0.setVisibleShapes(door1);
            door0.setMovable(false);
            door1.setMovable(false);
            door2.setMovable(false);
//        door1.setGoToPage(2);
//        door2.setGoToPage(3);
            pages.get(0).addShape(door0);
            pages.get(0).addShape(door1);
            pages.get(0).addShape(door2);

            Shape text_1 = new Shape(800.f, 100.f, 300.f, 100.f);
            text_1.setText("Bunny World!");
            text_1.setMovable(false);
            pages.get(0).addShape(text_1);


            Shape text_2 = new Shape(800.f, 300.f, 300.f, 100.f);
            text_2.setText("You are in a maze of twisty little passages, all alike");
            text_2.setMovable(false);
            pages.get(0).addShape(text_2);


            Shape text1 = new Shape(800.f, 250.f, 200.f, 200.f);
            text1.setImageName("");

            door0 = new Shape(200.f, 500.f, 100.f, 100.f);
//        door0.setGoToPage(0);
            door0.addRule(new ArrayList<Integer>(Arrays.asList(0, 0, 0, -1)));
            door0.addRule(new ArrayList<Integer>(Arrays.asList(0, 3, 1, -1)));
            door0.setID(3);
            door0.setMovable(false);
            door0.setImageName("door");
            allShapes.add(door0); // #3
            Shape mystic = new Shape(800.f, 300.f, 200.f, 200.f);
            mystic.setID(4);
            allShapes.add(mystic); // #4
            mystic.setImageName("mystic");
            mystic.setCurrPageNum(1);
            mystic.addRule(new ArrayList<Integer>(Arrays.asList(0, 2, 6, -1)));
            mystic.addRule(new ArrayList<Integer>(Arrays.asList(0, 1, 0, -1)));
            mystic.setMovable(false);
            pages.get(1).addShape(door0);
            pages.get(1).addShape(mystic);

            Shape text_3 = new Shape(900.f, 500.f, 400.f, 100.f);
            text_3.setText("Mystic Bunny Rub my tummy for a big surprise!");
            text_3.setMovable(false);
            pages.get(1).addShape(text_3);


            door0 = new Shape(700.f, 500.f, 100.f, 100.f);
//        door0.setGoToPage(1);
            door0.addRule(new ArrayList<Integer>(Arrays.asList(2, 1, 2, -1)));
            door0.addRule(new ArrayList<Integer>(Arrays.asList(0, 0, 1, -1)));
            door0.setID(5);
            door0.setMovable(false);
            door0.setImageName("door");
            allShapes.add(door0); // #5
            Shape carrot = new Shape(1000.f, 500.f, 150.f, 150.f);
            carrot.setImageName("carrot");

            carrot.setID(6);
            allShapes.add(carrot); // #6
            Shape fire = new Shape(500.f, 200.f, 400.f, 400.f);
            fire.addRule(new ArrayList<Integer>(Arrays.asList(2, 1, 2, -1)));
            fire.setMovable(false);
            fire.setID(7);
            allShapes.add(fire); // #7

            fire.setImageName("fire");
            fire.setCurrPageNum(2);
            pages.get(2).addShape(door0);
            pages.get(2).addShape(carrot);
            pages.get(2).addShape(fire);

            Shape text_4 = new Shape(300.f, 500.f, 400.f, 100.f);
            text_4.setText("Eek! Fire-room. Run away!");
            text_4.setMovable(false);
            pages.get(2).addShape(text_4);


            door0 = new Shape(1000.f, 400.f, 100.f, 100.f);
            door0.setGoToPage(4);
            door0.setMovable(false);
            door0.setImageName("door");
            door0.addRule(new ArrayList<Integer>(Arrays.asList(0, 0, 4, -1)));

            door0.setVisible(false);
            door0.setID(8);
            allShapes.add(door0); // #8
            Shape evil = new Shape(700.f, 300.f, 300.f, 300.f);

            carrot.addRule(new ArrayList<Integer>(Arrays.asList(1, 2, 6, 9)));
            carrot.addRule(new ArrayList<Integer>(Arrays.asList(1, 2, 9, 9)));
            carrot.addRule(new ArrayList<Integer>(Arrays.asList(1, 3, 8, 9)));
            carrot.addRule(new ArrayList<Integer>(Arrays.asList(1, 1, 0, 9)));
            evil.addRule(new ArrayList<Integer>(Arrays.asList(2, 1, 1, -1)));
            evil.addRule(new ArrayList<Integer>(Arrays.asList(0, 1, 2, -1)));

            evil.setMovable(false);
            evil.setImageName("evilbunny");
            evil.setCurrPageNum(3);
//        evil.setKill(carrot);
//        carrot.setKill(evil);
            evil.setID(9);
            allShapes.add(evil); // #9
            pages.get(3).addShape(door0);
            pages.get(3).addShape(evil);

            Shape text_5 = new Shape(900.f, 500.f, 400.f, 100.f);
            text_5.setText("You must appease the Bunny of Death!");
            text_5.setMovable(false);
            pages.get(3).addShape(text_5);


            Shape carrot1 = new Shape(200.f, 300.f, 150.f, 150.f);
            carrot1.setImageName("carrot");
            carrot1.setCurrPageNum(1);
            carrot1.setID(10);
            carrot1.addRule(new ArrayList<Integer>(Arrays.asList(2, 1, 3, -1)));
            allShapes.add(carrot1); // #10
            Shape carrot2 = new Shape(800.f, 300.f, 150.f, 150.f);
            carrot2.setImageName("carrot");
            carrot2.setCurrPageNum(1);
            carrot2.setID(11);
            allShapes.add(carrot2); // #11
            Shape carrot3 = new Shape(1400.f, 300.f, 150.f, 150.f);
            carrot3.setImageName("carrot");
            carrot3.setCurrPageNum(1);
            carrot3.setID(12);
            allShapes.add(carrot3); // #12
            pages.get(4).addShape(carrot1);
            pages.get(4).addShape(carrot2);
            pages.get(4).addShape(carrot3);

            Shape text_6 = new Shape(300.f, 500.f, 400.f, 100.f);
            text_6.setText("You Win! YaY!");
            text_6.setMovable(false);
            pages.get(4).addShape(text_6);


            bag = new Inventory();

        } else if (bunny == 1){
            pages = new ArrayList<>();
            allShapes = new ArrayList<>();
            pages.add(new Page("page1"));
            bag = new Inventory();
            Shape mystic = new Shape(0.f, 0.f, 150.f, 150.f);
            mystic.setImageName("mystic");
            mystic.setID(0);
            bag.addShape(mystic);
//            allShapes.add(mystic);
            Shape carrot = new Shape(0.f, 0.f, 150.f, 150.f);
            carrot.setImageName("carrot");
            carrot.setID(1);
//            allShapes.add(carrot);
            bag.addShape(carrot);
            Shape carrot2 = new Shape(0.f, 0.f, 150.f, 150.f);
            carrot2.setImageName("carrot2");
            carrot2.setID(2);
//            allShapes.add(carrot2);
            bag.addShape(carrot2);
            Shape door = new Shape(0.f, 0.f, 150.f, 150.f);
            door.setImageName("door");
            door.setID(3);
//            allShapes.add(door);
            bag.addShape(door);
            Shape evilbunny = new Shape(0.f, 0.f, 150.f, 150.f);
            evilbunny.setImageName("evilbunny");
            evilbunny.setID(4);
//            allShapes.add(evilbunny);
            bag.addShape(evilbunny);
            Shape fire = new Shape(0.f, 0.f, 150.f, 150.f);
            fire.setImageName("fire");
            fire.setID(5);
//            allShapes.add(fire);
            bag.addShape(fire);
            Shape duck = new Shape(0.f, 0.f, 150.f, 150.f);
            duck.setImageName("duck");
            duck.setID(6);
//            allShapes.add(duck);
            bag.addShape(duck);
            Shape text = new Shape(0.f, 0.f, 150.f, 150.f);
            text.setText("Text");
            text.setID(7);
//            allShapes.add(text);
            bag.addShape(text);
        } else if (bunny == 2){

            ArrayList<Shape> shapes = Database.getWorldShapes(MainActivity.db, MainActivity.getNewEditWorldName());

            allShapes = new ArrayList<>();
            pages = new ArrayList<>();
            bag = new Inventory();
            Hashtable<String, ArrayList<Shape>> dicShapes = new Hashtable<>();

            for (Shape s : shapes){
                if (dicShapes.containsKey(s.getPageName())){
                    dicShapes.get(s.getPageName()).add(s);
                } else {
                    dicShapes.put(s.getPageName(), new ArrayList<Shape>());
                    dicShapes.get(s.getPageName()).add(s);
                }
                allShapes.add(s);
            }

            for (String _pages : dicShapes.keySet()){
                Page p = new Page(_pages);

                for (Shape s : dicShapes.get(_pages)){
                    p.addShape(s);
                }

                pages.add(p);
            }

            worldName = MainActivity.getNewEditWorldName();

            Shape mystic = new Shape(0.f, 0.f, 150.f, 150.f);
            mystic.setImageName("mystic");
            mystic.setID(0);
            bag.addShape(mystic);
//            allShapes.add(mystic);
            Shape carrot = new Shape(0.f, 0.f, 150.f, 150.f);
            carrot.setImageName("carrot");
            carrot.setID(1);
//            allShapes.add(carrot);
            bag.addShape(carrot);
            Shape carrot2 = new Shape(0.f, 0.f, 150.f, 150.f);
            carrot2.setImageName("carrot2");
            carrot2.setID(2);
//            allShapes.add(carrot2);
            bag.addShape(carrot2);
            Shape door = new Shape(0.f, 0.f, 150.f, 150.f);
            door.setImageName("door");
            door.setID(3);
//            allShapes.add(door);
            bag.addShape(door);
            Shape evilbunny = new Shape(0.f, 0.f, 150.f, 150.f);
            evilbunny.setImageName("evilbunny");
            evilbunny.setID(4);
//            allShapes.add(evilbunny);
            bag.addShape(evilbunny);
            Shape fire = new Shape(0.f, 0.f, 150.f, 150.f);
            fire.setImageName("fire");
            fire.setID(5);
//            allShapes.add(fire);
            bag.addShape(fire);
            Shape duck = new Shape(0.f, 0.f, 150.f, 150.f);
            duck.setImageName("duck");
            duck.setID(6);
//            allShapes.add(duck);
            bag.addShape(duck);
            Shape text = new Shape(0.f, 0.f, 150.f, 150.f);
            text.setText("Text");
            text.setID(7);
//            allShapes.add(text);
            bag.addShape(text);

        } else {
            ArrayList<Shape> shapes = Database.getWorldShapes(MainActivity.db, MainActivity.getNewWorldName());

            allShapes = new ArrayList<>();
            pages = new ArrayList<>();
            bag = new Inventory();
            Hashtable<String, ArrayList<Shape>> dicShapes = new Hashtable<>();

            for (Shape s : shapes){
                if (dicShapes.containsKey(s.getPageName())){
                    dicShapes.get(s.getPageName()).add(s);
                } else {
                    dicShapes.put(s.getPageName(), new ArrayList<Shape>());
                    dicShapes.get(s.getPageName()).add(s);
                }
                allShapes.add(s);
            }

            for (String _pages : dicShapes.keySet()){
                Page p = new Page(_pages);

                for (Shape s : dicShapes.get(_pages)){
                    p.addShape(s);
                }

                pages.add(p);
            }

            worldName = MainActivity.getNewEditWorldName();
        }

    }

    public ArrayList<Page> getPages(){
        return pages;
    }

    public ArrayList<Shape> getShapes(){
        return allShapes;
    }

    public ArrayList<Shape> getPageShapes(int i){
        return pages.get(i).getShapes();
    }


    public ArrayList<Shape> getInventShapes(){
        return bag.getInventory();
    }

    public String getWorldName(){
        return worldName;
    }


    public Shape lookUPID(int idx) {
        for(int i = 0; i < allShapes.size(); ++i){
            Shape s = allShapes.get(i);
            if(s.getID() == idx){
                return s;
            }
        }
        return null;
    }

    public void addShape(Shape sp){
        allShapes.add(sp);
    }

    public void addPage(Page pg){
        pages.add(pg);
    }

    public void rmShape(Shape sp){
        int n = allShapes.size();
        for(int i = 0; i < n; ++i){
            if(sp.getID() == allShapes.get(i).getID()){
                allShapes.remove(i);
                return;
            }
        }
    }

    public void rmShapeInPage(Page pg, Shape sp){
        pg.removeShape(sp);
    }

    public void rmPage(int i){
        if(i >= 0 && i < pages.size()){
            pages.remove(i);
        }
    }


    static List<String> getDrawables() {
        List<String> drawables = new ArrayList<>(Arrays.asList("duck", "carrot", "evilbunny", "mystic", "fire"));
        return drawables;
    }

    static void setWorldName(String name){
        worldName = name;
    }


}
