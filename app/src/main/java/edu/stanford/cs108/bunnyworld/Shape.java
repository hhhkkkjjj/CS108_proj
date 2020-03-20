package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Shape {
    private float centerX, centerY, width, height, initWidth, initHeight;
    private ArrayList<Shape> visibleShapes;
    private ArrayList<Shape> invisibleShapes;
    private ArrayList<Shape> kill;


    private int shapeType;
    private String shapeName;  // shape shapeName : "shape1, shape2, ... "

    private String imageName;            // Name of the associated image
//    private script;
    private String text;
    private float textSize;
    private int textFont;
    private boolean textBold;
    private boolean textItalic;

    private String pageName;    // List of pages which contain the corresponding shape
    private int goToPage = -1;
    private int currPageNum;

    private boolean movable;             // True if the shape is movable
    private boolean visible;             // True if the shape is visible
    private boolean visibleInEditor;       // True if the shape is set to be invisible in editor
    private boolean useDefaultSize;              // True if the user choose to use default size of the image
    private boolean useImageScale;            // True if the user wants to resize the shape with the original proportion
    private boolean useInitDefaultSize;              // True if the user choose to use default size of the image
    private boolean useInitImageScale;            // True if the user wants to resize the shape with the original proportion
    private boolean isInInventory = false;
    private int drawOutline;


    private static int shapeNum = 0;  // count the number of shapes
    private int ID;

    private static final int RECTANGLE = 1;
    private static final int IMAGE = 2;
    public static final int TEXT = 3;
    private static BitmapDrawable carrotDrawable, evilbunnyDrawable, duckDrawable, carrot2Drawable, fireDrawable, mysticDrawable, doorDrawable;

    // Name of Sounds
    private static final int CARROT = 0;
    private static final int EVIL = 1;
    private static final int FIRE = 2;
    private static final int HOORAY = 3;
    private static final int MUNCH = 4;
    private static final int MUNCHING = 5;
    private static final int WOOF = 6;

    private static final float DEFAULT_TEXTSIZE = 50.0f;
    static final int DEFAULT_FONT = 0;
    static final int MONOSPACE = 1;
    static final int SANS_SERIF = 2;

    private static final ArrayList<String> imageList =
            new ArrayList<String>(Arrays.asList("carrot", "carrot2", "evilbunny", "duck", "mystic", "fire", "door"));
    private static final ArrayList<String> fontList = new ArrayList<String>(Arrays.asList(""));
    private static final Map<String, Float> proportionList = new HashMap<String, Float>(){
        {
            put("carrot", 0.840f);
            put("evilbunny", 0.933f);
            put("duck", 1.547f);
            put("mystic", 0.810f);
        }
    };



    private Paint grayFillPaint;
    private Paint opaquePaint;
    private Paint blueOutlinePaint;
    private Paint redOutlinePaint;
    private Paint textPaint;
    static Context context;

    private String myRule = "";
    private int onClickPage = - 1;
    private int onClickSound = - 1;
    private ArrayList<Integer> onClickHide = new ArrayList<>();
    private ArrayList<Integer> onClickShow = new ArrayList<>();

    private int onDropPage = - 1;
    private int onDropSound = - 1;
    private ArrayList<Integer> onDropHide = new ArrayList<>();
    private ArrayList<Integer> onDropShow = new ArrayList<>();

    private int onEnterPage = - 1;
    private int onEnterSound = - 1;
    private ArrayList<Integer> onEnterHide = new ArrayList<>();
    private ArrayList<Integer> onEnterShow = new ArrayList<>();
    private int dropId = - 1;
    private float scale;

    public Shape(float x, float y, float width, float height){
        this.centerX = x;
        this.centerY = y;
        this.width = width;
        this.height = height;

        visibleShapes = new ArrayList<>();
        invisibleShapes = new ArrayList<>();
        kill = new ArrayList<>();
        this.initHeight = height;
        this.initWidth = width;
        shapeType = RECTANGLE;
        shapeNum++;
        shapeName = "shape" + shapeNum;
        imageName = "";
        useImageScale = false;

        text = "";
        pageName = "";
        textSize = DEFAULT_TEXTSIZE;
        textFont = DEFAULT_FONT;
        textBold = false;
        textItalic = false;
        scale = (float)1.0;


        movable = true;
        visible = true;
        useDefaultSize = false;
        useImageScale = false;

        grayFillPaint = new Paint();
        grayFillPaint.setColor(Color.rgb(211, 211, 211));
        blueOutlinePaint = new Paint();
        blueOutlinePaint.setColor(Color.BLUE);
        blueOutlinePaint.setStyle(Paint.Style.STROKE);
        blueOutlinePaint.setStrokeWidth(20.0f);
        redOutlinePaint = new Paint();
        redOutlinePaint.setColor(Color.RED);
        redOutlinePaint.setStyle(Paint.Style.STROKE);
        redOutlinePaint.setStrokeWidth(20.0f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);

        ID = 0;
    }



    // Alternative constructor, more parameters
    // Parameters:
    // shape name, page name, type, x, y, width, height, ID
    // image name ("" if no image), text ("" if no text), rule,
    // movable, visible, use image bounds (true if you want to use the default size of the image)


    public Shape(float x, float y, float w, float h, int typ, String sName, String imName,
                 String rule, String txt, float tSize, String pName, int mov, int vis, int useImBounds,
                 int proScaling, int tFont, int tBold, int tItalic, int ID) {
        this.centerX = x;
        this.centerY = y;
        this.width = w;
        this.height = h;
        this.initHeight = height;
        this.initWidth = width;

        shapeNum++;
        shapeName = sName;
        pageName = pName;
        myRule = rule;
        updateRules(getArrayRules());
        //
        this.ID = ID;
        //

        //********************Use Integer to represent Boolean in Database**********************
        movable = mov != 0;

        visible = vis != 0;

        useImageScale = useImBounds != 0;

        useInitImageScale = proScaling != 0;

        textBold = tBold != 0;

        textItalic = tItalic != 0;
        //*************************************************************************************

        if (typ != RECTANGLE && typ != IMAGE && typ != TEXT) {
            shapeType = RECTANGLE;
        } else {
            shapeType = typ;
        }

        if (shapeType == IMAGE) {
            imageName = imName;
        } else {
            imageName = "";
        }

        if (shapeType == TEXT) {
            text = txt;
        } else {
            text = "";
        }

        textSize = tSize;
        textFont = tFont;

        grayFillPaint = new Paint();
        grayFillPaint.setColor(Color.rgb(211, 211, 211));
        blueOutlinePaint = new Paint();
        blueOutlinePaint.setColor(Color.BLUE);
        blueOutlinePaint.setStyle(Paint.Style.STROKE);
        blueOutlinePaint.setStrokeWidth(20.0f);
        redOutlinePaint = new Paint();
        redOutlinePaint.setColor(Color.RED);
        redOutlinePaint.setStyle(Paint.Style.STROKE);
        redOutlinePaint.setStrokeWidth(20.0f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
    }

    public void setScale( double scl){
        scale = (float)scl;

    }

    public void setParas(float x, float y, float w, float h, int pagenum, float textFontSize, boolean mov, boolean vis, boolean ita, boolean bol){
        centerX = x;
        centerY = y;
        initWidth = w;
        initHeight = h;
        currPageNum = pagenum;
        textSize = textFontSize;
        movable = mov;
        visible = vis;
        textBold = bol;
        textItalic = ita;
    }

    public void setPageName(String pname){
        pageName = pname;
    }



    public Shape copyShape(){
        Shape shapeCopy = new Shape(getCenterX(), getCenterY(), getWidth(), getHeight());
        shapeCopy.setID(getID());
        shapeCopy.setGoToPage(getGoToPageName());
        shapeCopy.setMovable(getMovable());
        shapeCopy.setVisible(getVisibility());
        shapeCopy.setCurrPageNum(getCurrPageNum());
        shapeCopy.setDrawOutline(getDrawOutline());
        shapeCopy.setShapeName(getShapeName());
        shapeCopy.setText(getText());
        shapeCopy.setImageName(getImageName());
        shapeCopy.setVisibleShapes(getVisibleShapes());
        shapeCopy.setInvisibleShapes(getInvisibleShapes());
        shapeCopy.setKill(getKill());

        return shapeCopy;
    }

    // get the ID of the shape

    public int getId(){
        return ID;
    }

    public static ArrayList<String> getSoundsList(){
        ArrayList<String> soundList = new ArrayList<>(Arrays.asList("carrot", "evil", "fire", "hooray", "munch", "munching", "woof"));
        return soundList;
    }

    // set shape parameters -------------------------------------
    public void setID(int idNum) {
        ID = idNum;
    }

    public void setGoToPage(int pageNum){goToPage = pageNum;}

    public void setMovable(boolean isMovable){
        movable = isMovable;
    }

    public void setVisible(boolean isVisible){
        visible = isVisible;
    }

    public void setCurrPageNum(int pageNum){
        currPageNum = pageNum;
    }

    public void setDrawOutline(int wantOutline){
        drawOutline = wantOutline;
    }


    // move the shape to a location x, y
    public boolean move(float x, float y) {
//        if (movable) {
            this.centerX = x;
            this.centerY = y;
            return true;
//        } else {
//            return false;
//        }
    }

    public void addRuleString(String s){
        String[] eachRule = s.split(",");
        for (String str: eachRule) {
            str.trim();
            String[] ruleContent = str.split(" ") ;
            ArrayList<Integer> indices = new ArrayList<>();
            for(String numStr: ruleContent){
                indices.add(Integer.parseInt(numStr));
            }
            addRule(indices);

        }

    }


    // determine if a given point is inside shape (include boundary)
    public boolean contains(float x, float y) {
        if (true) {
            if (Math.abs(this.centerX - x) <= width/2 && Math.abs(this.centerY - y) <= height/2) {
                return true;
            }
        }
        return false;
    }

    //  set the shapeName of the shape
    public void setShapeName(String newName){
        this.shapeName = newName;
    }

    public void setImageName(String imageName) {
        // If there's no such image, shape remains to be a rectangle
        if (imageList.contains(imageName)) {
            this.imageName = imageName;
            // If shape contains text, text takes precedence
            if (text.equals("")) {
                shapeType = IMAGE;
            } else {
                shapeType = TEXT;
            }
            // Refer to the image indicated by imageName
        }
    }

//    public String getImageName(){
//        return imageName;
//    }

    public void setVisibleShapes(ArrayList<Shape> l){
        visibleShapes = (ArrayList<Shape>)l.clone();
    }

    public void setInvisibleShapes(ArrayList<Shape> l){
        invisibleShapes = (ArrayList<Shape>)l.clone();
    }

    public void setKill(ArrayList<Shape> l){
        kill = (ArrayList<Shape>)l.clone();
    }

    public void setVisibleShapes(Shape shape){
        visibleShapes.add(shape);
    }

    public void setInvisibleShapes(Shape shape){
        invisibleShapes.add(shape);
    }

    public void setKill(Shape shape) {kill.add(shape);}

    public void setText(String txt){
        text = txt;
        shapeType = TEXT;
    };

    // get Shape parameters -----------------------------------------------------------------------
    // get the ID of the shape
    public int getID() {
        return ID;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getInitialWidth() {
        return initWidth;
    }

    public float getInitialHeight() {
        return initHeight;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }



    public int getType() {
        return shapeType;
    }

    public boolean getMovable() {
        return movable;
    }

    public int getDrawOutline(){
        return drawOutline;
    }

    public String getShapeName() {
        return shapeName;
    }

    public String getImageName() {
        return imageName;
    }
    public int getGoToPageName() {
        return goToPage;
    }

    public String getPageName() {
        return pageName;
    }

    public String getText() {
        return text;
    }

    public float getTextSize() {
        return textSize;
    }
    public boolean getVisibility() {
        return visible;
    }

    public int getCurrPageNum(){
        return currPageNum;
    }
    // change shape to visible
    public ArrayList<Shape> getVisibleShapes(){ return visibleShapes; }

    // change shapes to invisible
    public ArrayList<Shape> getInvisibleShapes(){ return invisibleShapes; }

    public ArrayList<Shape> getKill(){return kill;}

    public String getRule(){
        return myRule;
    }

    //-------------------Used for data saving-------------------------

    public int getMov() {
        if (movable) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getVis() {
        if (visible) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getUse() {
        if (useDefaultSize) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getPro() {
        if (useInitDefaultSize) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getTextBold() {
        if (textBold) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getTextItalic() {
        if (textItalic) {
            return 1;
        } else {
            return 0;
        }
    }


    public boolean getBold(){
        return textBold;
    }
    public boolean getItalic(){
        return textItalic;
    }


    public int getTextFont() {
        return textFont;
    }

    // draw shape -----------------------------------------------------------------------

    public void drawRec(Canvas canvas) {
        canvas.drawRect(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2, blueOutlinePaint);
    }

    public void draw(Canvas canvas, boolean isInInventory,boolean inEditor){
        draw( canvas,  isInInventory,inEditor,255);
    }

    public void draw(Canvas canvas, boolean isInInventory,int alpha){
        draw( canvas,  isInInventory,false,alpha);
    }

    public void draw(Canvas canvas, boolean isInInventory, boolean inEditor, int alpha){
//        if(visible){
//            if(shape!=null && replacement(shape))
//        }
        Paint partialOpaquePaint = new Paint();
        partialOpaquePaint.setAlpha(alpha);

        this.isInInventory = isInInventory;
        if(isInInventory){
            width = 100;
            height = 100;
        }else{
            width = initWidth;
            height = initHeight;
        }

        if (visible || inEditor){
            //System.out.println("visibletxt");
            if(shapeType == TEXT){
                //System.out.println("intxt");
                if (textBold && textItalic) {
                    if (textFont == DEFAULT_FONT) {
                        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
                        textPaint.setTypeface(typeface);
                    } else if (textFont == MONOSPACE) {
                        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD_ITALIC);
                        textPaint.setTypeface(typeface);
                    } else if (textFont == SANS_SERIF) {
                        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);
                        textPaint.setTypeface(typeface);
                    }
                } else if (textBold && !textItalic) {
                    if (textFont == DEFAULT_FONT) {
                        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                        textPaint.setTypeface(typeface);
                    } else if (textFont == MONOSPACE) {
                        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);
                        textPaint.setTypeface(typeface);
                    } else if (textFont == SANS_SERIF) {
                        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
                        textPaint.setTypeface(typeface);
                    }
                } else if (!textBold && textItalic) {
                    if (textFont == DEFAULT_FONT) {
                        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
                        textPaint.setTypeface(typeface);
                    } else if (textFont == MONOSPACE) {
                        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC);
                        textPaint.setTypeface(typeface);
                    } else if (textFont == SANS_SERIF) {
                        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC);
                        textPaint.setTypeface(font);
                    }
                } else {
                    System.out.println("inside");
                    if (textFont == DEFAULT_FONT) {
                        System.out.println("infont");
                        Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
                        System.out.println("stage");
                        textPaint.setTypeface(font);
                    } else if (textFont == MONOSPACE) {
                        Typeface font = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
                        textPaint.setTypeface(font);
                    } else if (textFont == SANS_SERIF) {
                        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
                        textPaint.setTypeface(font);
                    }
                }

                textPaint.setTextSize(textSize);
                textPaint.setAlpha(alpha);
                textPaint.setTextAlign(Paint.Align.CENTER);
                Rect bounds = new Rect();
                textPaint.getTextBounds(text,0, text.length(), bounds);
                width = bounds.width();
                height = bounds.height()*2;
                canvas.drawText(text, centerX, centerY, textPaint);
                if(drawOutline==2){
                    canvas.drawRect(centerX - width/2-15, centerY - height/2-20, centerX + width/2+15, centerY + height/2-10 , blueOutlinePaint);
                }else if(drawOutline==1){
                    canvas.drawRect(centerX - width/2-15, centerY - height/2-20, centerX + width/2+15, centerY + height/2-10 , redOutlinePaint);
                }



            }else if(shapeType == IMAGE){
                //System.out.println("image");
                if (imageList.contains(imageName)){
                    //System.out.println("inlist");
                    if(useImageScale && !isInInventory){
                        if(useDefaultSize){
                            scale = (float) 1.0;
                        }


//                if (shape != null && this.droppableBy(shape)) {
//                    drawRec(canvas);
                        
//                }
                        if (imageName.equals("carrot")) {
                            carrotDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot);
                            Bitmap carrotBitmap = carrotDrawable.getBitmap();
                            width = (float)carrotBitmap.getWidth();
                            height = (float)carrotBitmap.getHeight();
                            width = width*scale;
                            height = height*scale;
                            canvas.drawBitmap(carrotBitmap, centerX - width/2.0f, centerY - height/2.0f, partialOpaquePaint);
                        } else if (imageName.equals("evilbunny")) {
                            evilbunnyDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.evilbunny);
                            Bitmap evilbunnyBitmap = evilbunnyDrawable.getBitmap();
                            width = (float)evilbunnyBitmap.getWidth();
                            height = (float)evilbunnyBitmap.getHeight();
                            width = width*scale;
                            height = height*scale;
                            canvas.drawBitmap(evilbunnyBitmap, centerX - width/2.0f, centerY - height/2.0f, partialOpaquePaint);
                        } else if (imageName.equals("duck")) {
                            duckDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.duck);
                            Bitmap duckBitmap = duckDrawable.getBitmap();
                            width = (float)duckBitmap.getWidth();
                            height = (float)duckBitmap.getHeight();
                            width = width*scale;
                            height = height*scale;
                            canvas.drawBitmap(duckBitmap, centerX - width/2.0f, centerY - height/2.0f, partialOpaquePaint);
                        }
                    }else{
                        System.out.println("custom");
                        if (imageName.equals("carrot")) {
                            carrotDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot);
                            canvas.drawBitmap(carrotDrawable.getBitmap(), null, new RectF(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2), partialOpaquePaint);
                        } else if (imageName.equals("evilbunny")) {
                            evilbunnyDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.death);
                            canvas.drawBitmap(evilbunnyDrawable.getBitmap(), null, new RectF(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2), partialOpaquePaint);
                        }else if (imageName.equals("duck")) {
                            System.out.println("duck");
                            System.out.println(context.getResources());
                            duckDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.duck);

                            canvas.drawBitmap(duckDrawable.getBitmap(), null, new RectF(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2), partialOpaquePaint);
                            System.out.println("duck 3 line");
                        }else if (imageName.equals("carrot2")) {
                            carrot2Drawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot2);
                            canvas.drawBitmap(carrot2Drawable.getBitmap(), null, new RectF(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2), partialOpaquePaint);
                        }else if (imageName.equals("fire")) {
                            fireDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.fire);
                            canvas.drawBitmap(fireDrawable.getBitmap(), null, new RectF(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2), partialOpaquePaint);
                        }else if (imageName.equals("mystic")) {
                            mysticDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.mystic);
                            canvas.drawBitmap(mysticDrawable.getBitmap(), null, new RectF(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2), partialOpaquePaint);
                        }else if (imageName.equals("door")) {
                            doorDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.door);
                            canvas.drawBitmap(doorDrawable.getBitmap(), null, new RectF(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2), partialOpaquePaint);
                        }
                    }
                    if(drawOutline==2){
                        canvas.drawRect(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2, blueOutlinePaint);
                    }else if(drawOutline==1){
                        canvas.drawRect(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2, redOutlinePaint);
                    }
                }else{
                    canvas.drawRect(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2, grayFillPaint);
                    if(drawOutline==2){
                        canvas.drawRect(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2, blueOutlinePaint);
                    }else if(drawOutline==1){
                        canvas.drawRect(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2, redOutlinePaint);
                    }
                }

            }else{
                canvas.drawRect(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2, grayFillPaint);
            }

        }

        
    }

    public boolean droppableBy(Shape s) {
        if (visible && s.getVisibility()) {
            String shapeName = s.getShapeName();
//            ArrayList<String> dropAction = script.getActions(ONDROP, shapeName);
//            if (dropAction.size() != 0) {
//                return true;
//            }
            return true; //qie
        }
        return false;
    }

    // play sound
    public void playSound(int soundName) {
        if (!isInInventory) {
            MediaPlayer mp;
            if (soundName == CARROT ) {
                mp = MediaPlayer.create(context, R.raw.carrotcarrotcarrot);
                mp.start();
            } else if (soundName == EVIL) {
                mp = MediaPlayer.create(context, R.raw.evillaugh);
                mp.start();
            } else if (soundName == FIRE) {
                mp = MediaPlayer.create(context, R.raw.fire);
                mp.start();
            } else if (soundName == HOORAY) {
                mp = MediaPlayer.create(context, R.raw.hooray);
                mp.start();
            } else if (soundName == MUNCH) {
                mp = MediaPlayer.create(context, R.raw.munch);
                mp.start();
            } else if (soundName == MUNCHING) {
                mp = MediaPlayer.create(context, R.raw.munching);
                mp.start();
            } else if (soundName == WOOF) {
                mp = MediaPlayer.create(context, R.raw.woof);
                mp.start();
            }
        }
    }


    public void addRule(ArrayList<Integer> rule){

        int action1 = rule.get(0);
        int action2 = rule.get(1);
        int action3 = rule.get(2);
        if(myRule.equals("")){
            myRule += action1 + " " + action2 + " " + action3 + " " + rule.get(3);
        }else{
            myRule += "," + action1 + " " + action2 + " " + action3 + " "+ rule.get(3);
        }
        if(action1 == 0 ){
            switch (action2){
                case 0:
                    onClickPage = action3;
                    break;
                case 1:
                    onClickSound = action3;
                    break;
                case 2:
                    onClickHide.add(action3);
                    break;
                case 3:
                    onClickShow.add(action3);
                    break;
            }
        }else if (action1 == 1){
            dropId = rule.get(3);
//            System.err.println(dropId);
            switch (action2) {
                case 0:
                    onDropPage = action3;
                    break;
                case 1:
                    onDropSound = action3;
                    break;
                case 2:
                    onDropHide.add(action3);
                    break;
                case 3:
                    onDropShow.add(action3);
                    break;
            }

        }else if (action1 == 2){
                switch (action2){
                    case 0:
                        onEnterPage = action3;
                        break;
                    case 1:
                        onEnterSound = action3;
                        break;
                    case 2:
                        onEnterHide.add(action3);
                        break;
                    case 3:
                        onEnterShow.add(action3);
                        break;
            }

        }

    }




    public int onClickPage(){
        return onClickPage;
    }

    public void onClickSound(){
        playSound(onClickSound);
    }

    public ArrayList<Integer> onClickHide(){
        return onClickHide;
    }
    public ArrayList<Integer> onClickShow(){
        return onClickShow;
    }

    public int onDropPage(int shapeId){
        if(dropId == shapeId){
            return onDropPage;
        }
        return -1;
    }

    public void onDropSound(int shapeId){
        if(dropId == shapeId){
            playSound(onDropSound);
        }
    }

    public ArrayList<Integer> onDropHide(int shapeId){
        if(dropId == shapeId){
            return onDropHide;
        }
        ArrayList<Integer> res = new ArrayList<>();
        return res;
    }
    public ArrayList<Integer> onDropShow(int shapeId){
        if(dropId == shapeId){
            return onDropShow;
        }
        ArrayList<Integer> res = new ArrayList<>();
        return res;
    }

    public int onEnterPage(){
        return onEnterPage;
    }

    public void onEnterSound(){
        playSound(onEnterSound);
    }

    public ArrayList<Integer> onEnterHide(){
        return onEnterHide;
    }
    public ArrayList<Integer> onEnterShow(){
        return onEnterShow;
    }

    public void clearRule(){
        onClickPage = - 1;
        onClickSound = - 1;
        onClickHide.clear();
        onClickShow.clear();

        onDropPage = - 1;
        onDropSound = - 1;
        onDropHide.clear();
        onDropShow.clear();

        onEnterPage = - 1;
        onEnterSound = - 1;
        onEnterHide.clear();
        onEnterShow.clear();
        dropId = - 1;
        myRule = "";

    }
    public ArrayList<ArrayList<Integer>> getArrayRules(){

        //System.out.println("cheecj getrule");
        //System.out.println("this "+myRule);
        String[] arrRules = myRule.split(",");
        //System.out.println(arrRules[0]+"that");
        ArrayList<ArrayList<Integer>> myRulesArr = new ArrayList<>();
        if(myRule == ""){return myRulesArr;}
        //System.out.println("cheecj getrulehh");
       // System.out.println(arrRules[0]+"shit");
        for(String rule : arrRules){
            //System.out.println(rule+"boom");
            String[] actions = rule.split(" ");
            //System.out.println(actions[0]+"bbbbbboom");
            ArrayList<Integer> temp = new ArrayList<>();
            for(String action : actions){
                System.out.println(action+"zzzzbbbbbboom");
                if(!action.equals("")){
                    temp.add(Integer.parseInt(action));
                }

                //System.out.println("tiga");
            }
            myRulesArr.add(temp);
        }
        return myRulesArr;
    }
    public void updateRules(ArrayList<ArrayList<Integer>> newRules){
        clearRule();
        System.out.println("newRule");
        System.out.println(newRules.size());
        System.out.println(newRules.get(0).size());
        if(newRules.get(0).size() == 0){return;}
        for(ArrayList<Integer> rule : newRules){
            addRule(rule);
        }
    }
}
