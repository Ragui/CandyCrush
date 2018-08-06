package com.gu.ragui.candycrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;

import static java.lang.Math.abs;

public class board extends SurfaceView implements SurfaceHolder.Callback {

    private candy candies[][] = new candy[9][9];
    private Rect rects[][] = new Rect[9][9];
    private Random rand = new Random();
    private int candyType = 6, x = 0, y = 0;
    private Canvas can ;
    private GestureDetector gd;
    private boolean down = false, move = false;
    private double x1, x2, y1, y2;
    private String candy_Type = "";
    private int s;

    public board(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        this.setWillNotDraw(false);
       // invalidate();
    }

    public board(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
        setFocusable(true);
        this.setWillNotDraw(false);
        //invalidate();
    }

    public board(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        this.setWillNotDraw(false);
       // invalidate();
    }

    private void drawCandy(candy cand, Canvas c){
        //Toast.makeText (getContext(), "drawCandy", Toast.LENGTH_LONG).show();
        candy_Type = String.format("ic_food%d", cand.getType());
        s  = getResources().getIdentifier(candy_Type,
                "mipmap", "com.gu.ragui.candycrush" );
        c.drawBitmap(BitmapFactory.decodeResource(getResources(), s), null,
                rects[cand.getX()][cand.getY()], null);
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        c.drawColor(Color.GRAY);

         for(int j = 0; j < 9 ; j++){
            for(int i = 0; i < 9; i++){
                drawCandy(candies[i][j],c);
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder){
        //Toast.makeText (getContext(), "SURFACE CREATED", Toast.LENGTH_LONG).show();
        Canvas c = getHolder().lockCanvas();
        x  = c.getWidth()/9;
        y  = c.getHeight()/12;


        //allocate candies
        for(int j = 0; j < 9 ; j++){
            for(int i = 0; i < 9; i++){
                candies[i][j] = new candy(i,j,rand.nextInt(2));
                rects[i][j] = new Rect();
                rects[i][j].set(i*x , (j+2)*y,(i+1)*x,(j+3)*y);
            }
        }

        getHolder().unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format , int width , int height){
        Canvas c = getHolder().lockCanvas();
        getHolder().unlockCanvasAndPost(c);
        invalidate();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        int action = motionEvent.getActionMasked();

        if(action == MotionEvent.ACTION_DOWN){
            down = true;
            x1 = motionEvent.getX();
            y1 = motionEvent.getY();
        }

        if(action == MotionEvent.ACTION_MOVE && down){
            move = true;
        }

        if(action == MotionEvent.ACTION_UP && move){
            down = move = false;
            x2 = motionEvent.getX();
            y2 = motionEvent.getY();

            int inX = (int)(x1/x);
            int inY = (int)(y1/y) - 2;

            int outX = (int)(x2/x);
            int outY = (int)(y2/y) - 2;

            // Toast.makeText(getContext(), "" + inX + " " + inY + " " + outX + " " + outY, Toast.LENGTH_LONG).show();
            if (!  ( (inX == outX && inY == outY) || abs(inX-outX) > 1 ||  abs(inY-outY) > 1
                    || inY < 0 || outY < 0 || outY > 8 || inY > 8 || (abs(inX-outX) == 1 &&  abs(inY-outY) == 1) )  ){
                candies[inX][inY].changeType(candies[inX][inY].getType() == 0 ? 1 : 0 );
                invalidate();
                Toast.makeText(getContext(), "" + inX + " " + inY + " " + outX + " " + outY, Toast.LENGTH_LONG).show();
            }
        }

        return true;
    }

}
