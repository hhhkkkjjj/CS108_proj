package edu.stanford.cs108.bunnyworld;

import java.util.ArrayList;

public class Page {
    private String name;
    private ArrayList<Shape> shapes;

    Page (String _name){
        name = _name;
        shapes = new ArrayList<Shape>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name){this.name = name;}

    public ArrayList<Shape> getShapes(){
        return shapes;
    }

    public void clearPage(){
        shapes.clear();
    }

    public void addShape(Shape s) {
        if (!shapes.contains(s)){
            shapes.add(s);
        }
    }

    public void removeShape(Shape s){
        shapes.remove(s);
    }


}
