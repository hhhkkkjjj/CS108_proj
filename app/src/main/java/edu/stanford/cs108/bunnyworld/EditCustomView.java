package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EditCustomView extends View {
    private static final double DIVISION = 0.75;
    public static int currentPage;
    private int viewWidth;
    private int viewHeight;
    private Paint redOutline;
    private ArrayList<Shape> pageShapes;
    private ArrayList<Shape> inventShapes;
    public static World myWorld;
    private RectF backGroundRect;
    private RectF inventoryRect;
    private int numShape = 0;

    public EditCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        redOutline = new Paint();
        redOutline.setColor(Color.rgb(140,21,21));
        redOutline.setStyle(Paint.Style.STROKE);
        redOutline.setStrokeWidth(5.0f);
        if(myWorld==null)
        {
            myWorld = new World(1);
        }

        currentPage = 0;

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        backGroundRect = new RectF(0, 0, viewWidth, (float)DIVISION * viewHeight);
        inventoryRect = new RectF(0, (float)DIVISION * viewHeight, viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0.0f, (float) DIVISION * viewHeight, viewWidth, (float) DIVISION * viewHeight, redOutline);

        pageShapes = myWorld.getPageShapes(currentPage);
        inventShapes = myWorld.getInventShapes();

        for(int i = 0; i < pageShapes.size(); i++) {
//            System.err.println("Draw shape on page");
            pageShapes.get(i).draw(canvas, false,true);
        }

        for(int i = 0; i < inventShapes.size(); i++) {
//            System.err.println("Draw shape in bag");
            inventShapes.get(i).move((i+1)*150, (float)DIVISION * viewHeight+60);
            inventShapes.get(i).draw(canvas, true,true);
        }

        if(shapeInHand != null) {
//            System.err.println("Draw shape in hand");
            shapeInHand.draw(canvas, false,true);
        }

    }

    private long downTime;
    private long upTime;
    private float downX, downY, moveX, moveY, upX, upY;
    private static final long CLICK_TIMEOUT = 500;
    private static final float CLICK_DISTANCE = 10;
    private boolean validClick = false;
    private Shape shapeInHand = null;
    private boolean pageChange = false;
    private Shape shapeBelow = null;
    private boolean overlapFlag = false;
    public static Shape selectedShape = null;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pageChange = false;
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                downX = event.getX();
                downY = event.getY();

                if(downY < DIVISION * viewHeight) {
                    for(int i = pageShapes.size() - 1; i >= 0; i--) {
                        if(pageShapes.get(i).contains(downX, downY)) {
                            shapeInHand = pageShapes.get(i);
                            pageShapes.remove(i);
                            validClick = true;
                            break;
                        }
                    }
                    if (validClick==false)
                    {
                        if(selectedShape!=null)
                        {
                            selectedShape.setDrawOutline(0);
                        }
                        selectedShape=null;
                    }
                }
                else {
                    for(int i = inventShapes.size() - 1; i >= 0; i--) {
                        if(inventShapes.get(i).contains(downX, downY)) {
                            shapeInHand = inventShapes.get(i).copyShape();
                            shapeInHand.setShapeName("Shape" + numShape);
                            numShape++;
                            myWorld.addShape(shapeInHand);
                            int validID=0;
                            while(true)
                            {
                                if(myWorld.lookUPID(validID)!=null)
                                {
                                    validID++;
                                }
                                else
                                {
                                    break;
                                }
                            }
                            shapeInHand.setID(validID);

//                            inventShapes.remove(i);
                            validClick = true;
                            break;
                        }
                    }
                }
                if(shapeInHand!=null)
                {
                    shapeInHand.setDrawOutline(2);
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                System.err.println(validClick);
                if(validClick) {
                    moveX = event.getX();
                    moveY = event.getY();
                    if(moveX >= 0 && moveX <= viewWidth && moveY >= 0 && moveY <= viewHeight) {
                        shapeInHand.move(moveX, moveY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(validClick) {
                    shapeInHand.setDrawOutline(0);
                    upX = event.getX();
                    upY = event.getY();
                    upTime = System.currentTimeMillis();
                    if(upTime-downTime<=CLICK_TIMEOUT && Math.abs(downX - upX) < CLICK_DISTANCE && Math.abs(downY - upY) < CLICK_DISTANCE) {
                        if(selectedShape!=null)
                        {
                            selectedShape.setDrawOutline(0);
                        }
                        selectedShape = shapeInHand;
                        System.err.println(selectedShape.getID());
                        selectedShape.setDrawOutline(1);
                    }
                    else{
                    }

                    if(shapeInHand.getMovable() && upY > DIVISION * viewHeight) {
                        myWorld.rmShape(shapeInHand);
                    }
                    else {
                        shapeInHand.setPageName(myWorld.getPages().get(currentPage).getName());
                        pageShapes.add(shapeInHand);
                        if(selectedShape!=null)
                        {
                            selectedShape.setDrawOutline(0);
                        }
                        selectedShape = shapeInHand;
                        System.err.println(selectedShape.getID());
                        selectedShape.setDrawOutline(1);
                    }
                }

                validClick = false;
                shapeInHand = null;
                shapeBelow = null;
                break;
        }
        invalidate();
        return true;
    }

}
