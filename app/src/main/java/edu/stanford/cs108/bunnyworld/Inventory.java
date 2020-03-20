package edu.stanford.cs108.bunnyworld;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Shape> shapes;

    Inventory(){
        shapes = new ArrayList<Shape>();
    }

    public ArrayList<Shape> getInventory(){
        return shapes;
    }

    public void addShape(Shape shape){
        shapes.add(shape);
    }

    public void delShape(Shape shape){
        shapes.remove(shape);
    }
}
