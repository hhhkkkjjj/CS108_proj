package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomView extends View {
    private static final double DIVISION = 0.75;
    private int currentPage;
    private int viewWidth;
    private int viewHeight;
    private Paint redOutline;
    private ArrayList<Shape> pageShapes;
    private ArrayList<Shape> inventShapes;
    public static World myWorld;
    private RectF backGroundRect;
    private RectF inventoryRect;
    private int alpha;


    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        alpha=255;
        redOutline = new Paint();
        redOutline.setColor(Color.rgb(140,21,21));
        redOutline.setStyle(Paint.Style.STROKE);
        redOutline.setStrokeWidth(5.0f);
        if(myWorld==null)
        {
            myWorld = new World(0);
        }
        currentPage = 0;
        pageBg = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_green);


        timer = new CountDownTimer(20000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(alpha+5<255)
                {
                    alpha+=5;
                    invalidate();
//                    System.err.println(alpha);
                }
            }
            @Override
            public void onFinish() {
                timer.start();
            }
        }.start();

    }

    private CountDownTimer timer;




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        backGroundRect = new RectF(0, 0, viewWidth, (float)DIVISION * viewHeight);
        inventoryRect = new RectF(0, (float)DIVISION * viewHeight, viewWidth, viewHeight);
    }

    public void changeBackground(int bgIdx){
        switch (bgIdx) {
            case 0:
                pageBg = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_blue);
                break;
            case 1:
                pageBg = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_green);
                break;
            case 2:
                pageBg = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_white);
                break;
            case 3:
                pageBg = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_yellow);
                break;
        }

        return;
    }

    private BitmapDrawable pageBg;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint partialOpaquePaint = new Paint();
        partialOpaquePaint.setAlpha(180);
        canvas.drawBitmap(pageBg.getBitmap(), null, backGroundRect, partialOpaquePaint);
        BitmapDrawable invBg = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_brown);
        canvas.drawBitmap(invBg.getBitmap(), null, inventoryRect, partialOpaquePaint);

        canvas.drawLine(0.0f, (float) DIVISION * viewHeight, viewWidth, (float) DIVISION * viewHeight, redOutline);

        pageShapes = myWorld.getPageShapes(currentPage);
        inventShapes = myWorld.getInventShapes();

        if(pageChange) {

            pageChange = false;
            alpha=0;
            for(int j = 0; j < pageShapes.size(); j++) {
                Shape temp = pageShapes.get(j);
                if(!temp.getVisibility()){
                    continue;
                }
                if(temp.onEnterPage() != -1) {
                    System.err.println("On Enter ChangePage");
                    currentPage = temp.onEnterPage();
                    pageChange = true;
                }

                // Change visibility
                ArrayList<Integer> inv;
                inv = temp.onEnterHide();
                for(int i = 0; i < inv.size(); i++)
                {
                    System.err.println("On Enter Hide item");
                    myWorld.lookUPID(inv.get(i)).setVisible(false);
                }
                inv = temp.onEnterShow();
                for(int i = 0; i < inv.size(); i++)
                {
                    System.err.println("On Enter Show item");
                    myWorld.lookUPID(inv.get(i)).setVisible(true);
                }
                temp.onEnterSound();
            }
        }

        for(int i = 0; i < pageShapes.size(); i++) {
//            System.out.println("Draw shape on page");
            pageShapes.get(i).draw(canvas, false,alpha);
        }

        for(int i = 0; i < inventShapes.size(); i++) {
//            System.out.println("Draw shape in bag");
            inventShapes.get(i).move((i+1)*150, (float)DIVISION * viewHeight+60);
            inventShapes.get(i).draw(canvas, true, 255);
        }
        if(shapeInHand != null) {
            shapeInHand.draw(canvas, false,255);
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
                        if(pageShapes.get(i).contains(downX, downY) && pageShapes.get(i).getVisibility()) {
                            shapeInHand = pageShapes.get(i);
                            pageShapes.remove(i);
                            validClick = true;
                            break;
                        }
                    }
                }
                else {
                    for(int i = inventShapes.size() - 1; i >= 0; i--) {
                        if(inventShapes.get(i).contains(downX, downY) && inventShapes.get(i).getVisibility()) {
                            shapeInHand = inventShapes.get(i);
                            inventShapes.remove(i);
                            validClick = true;
                            break;
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
//                System.err.println(validClick);
                if(validClick && shapeInHand.getMovable()) {
                    shapeInHand.setDrawOutline(2);
                    moveX = event.getX();
                    moveY = event.getY();
                    if(moveX >= 0 && moveX <= viewWidth && moveY >= 0 && moveY <= viewHeight) {
                        shapeInHand.move(moveX, moveY);
                    }

                    for(int i = pageShapes.size() - 1; i >= 0; i--) {
                        if(pageShapes.get(i).contains(moveX, moveY) && pageShapes.get(i).getVisibility()) {
                            shapeBelow = pageShapes.get(i);
//                            System.err.println("Detect overlap");
                            if(shapeInHand.onDropPage(pageShapes.get(i).getID()) != -1 || shapeInHand.onDropHide(pageShapes.get(i).getID()).size()>0|| shapeInHand.onDropShow(pageShapes.get(i).getID()).size()>0) {
                                overlapFlag = true;
                                shapeBelow.setDrawOutline(1);
                            }
                        }
                    }

                }
                if(!overlapFlag)
                {
                    if(shapeBelow != null)
                    {
                        shapeBelow.setDrawOutline(0);
                        shapeBelow = null;
                    }
                }
                overlapFlag = false;
                break;
            case MotionEvent.ACTION_UP:
                if(validClick) {
                    shapeInHand.setDrawOutline(0);
                    upX = event.getX();
                    upY = event.getY();
                    upTime = System.currentTimeMillis();
                    if(upTime-downTime<=CLICK_TIMEOUT && Math.abs(downX - upX) < CLICK_DISTANCE && Math.abs(downY - upY) < CLICK_DISTANCE) {
                        // Change Page
                        if(shapeInHand.onClickPage() != -1) {
                            System.err.println("On Click ChangePage");
                            currentPage = shapeInHand.onClickPage();
                            pageChange = true;
                        }

                        // Change visibility
                        ArrayList<Integer> inv;
                        inv = shapeInHand.onClickHide();
                        for(int i = 0; i < inv.size(); i++)
                        {
                            System.err.println("On Click Hide item");
                            myWorld.lookUPID(inv.get(i)).setVisible(false);
                        }
                        inv = shapeInHand.onClickShow();
                        for(int i = 0; i < inv.size(); i++)
                        {
                            System.err.println("On Click Show item");
                            myWorld.lookUPID(inv.get(i)).setVisible(true);
                        }
                        shapeInHand.onClickSound();
                    }

                    else{

                    }

                    if(shapeInHand.getMovable() && upY > DIVISION * viewHeight) {
                        inventShapes.add(shapeInHand);

                    }
                    else {

//                        shapeInHand.playSound("evil-laugh");
                        // Kill each other
                        for(int i = pageShapes.size() - 1; i >= 0; i--) {
                            if(pageShapes.get(i).contains(upX, upY) && pageShapes.get(i).getVisibility()) {
                                System.err.println("Overlapped");
                                if(shapeInHand.onDropPage(pageShapes.get(i).getID()) != -1) {
                                    System.err.println("On Drop ChangePage");
                                    currentPage = shapeInHand.onDropPage(pageShapes.get(i).getID());
                                    pageChange = true;
                                }

                                // Change visibility
                                ArrayList<Integer> inv;
                                inv = shapeInHand.onDropHide(pageShapes.get(i).getID());
//                                System.err.println(pageShapes.get(i).getID());
//                                System.err.println(shapeInHand.getID());
//                                System.err.println(inv.size());
                                for(int ii = 0; ii < inv.size(); ii++)
                                {
                                    System.err.println("On Drop Hide item");
                                    myWorld.lookUPID(inv.get(ii)).setVisible(false);
                                }
                                inv = shapeInHand.onDropShow(pageShapes.get(i).getID());
                                for(int ii = 0; ii < inv.size(); ii++)
                                {
                                    System.err.println("On Drop Show item");
                                    myWorld.lookUPID(inv.get(ii)).setVisible(true);
                                }
                                shapeInHand.onDropSound(pageShapes.get(i).getID());
                            }
                        }
                        pageShapes.add(shapeInHand);
                    }


                }

                if(shapeBelow!=null)
                {
                    shapeBelow.setDrawOutline(0);
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
