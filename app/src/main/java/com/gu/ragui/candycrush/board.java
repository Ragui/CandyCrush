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
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class board extends SurfaceView implements SurfaceHolder.Callback {

    private candy candies[][] = new candy[9][9];
    private Rect rects[][] = new Rect[9][9];
    private Bitmap bitmap[][] = new Bitmap[9][9];
    private Random rand = new Random();
    private int candyType = 6, x = 0, y = 0;
    private Canvas can ;
    private GestureDetector gd;
    private boolean down = false, move = false, back = true, isY = false, upLeft = false;
    private double x1, x2, y1, y2;
    private String candy_Type = "";
    private int s;
    private TranslateAnimation anim;
    private int inX = 0, inY = 0, outX = 0, outY = 0;
    int m1 = 0, m2 = 0;


    public board(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        this.setWillNotDraw(false);
    }

    public board(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
        setFocusable(true);
        this.setWillNotDraw(false);
    }

    public board(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        this.setWillNotDraw(false);
    }

    private void drawCandy(int I, int J, Canvas c){
        int moveType = candies[I][J].getMove();

        if(moveType == 1){   //move right
            m1 += x/3;

            rects[I][J].set(x*candies[I][J].getX() + m1, y*(J+2), x*candies[I][J].getX() + m1 + x,y*(J+3) );
            c.drawBitmap( bitmap[I][J], null, rects[I][J], null);
            if(m1 == x) {
               m1 = 0;

               /*if(!candies[I][J].getMoved() && possibleMove()){

               } else */if(candies[I][J].getMoved()){
                    candies[I][J].setMoved(false);
                    candies[I][J].setRunning(false);
                    candies[I][J].changePosition(I, J);
                    candies[I][J].setMove(0);
                    rects[I][J].set(I*x , (J+2)*y,(I+1)*x,(J+3)*y);
                }else{
                    candies[I][J].setMoved(true);
                    candies[I][J].setMove(2);
                    candies[I][J].changePosition(I+1, J);
                }
            }

        }
        else if(moveType == 2){ //move left
            m2 += x/3;
            rects[I][J].set(x*candies[I][J].getX() - m2, y*(J+2), x*candies[I][J].getX() - m2 + x,y*(J+3) );
            c.drawBitmap( bitmap[I][J], null, rects[I][J], null);
            if(m2 == x) {
                m2 = 0;

                if(candies[I][J].getMoved()){
                    candies[I][J].setMoved(false);
                    candies[I][J].setRunning(false);
                    candies[I][J].changePosition(I, J);
                    candies[I][J].setMove(0);
                    rects[I][J].set(I*x , (J+2)*y,(I+1)*x,(J+3)*y);
                }else{
                    candies[I][J].setMoved(true);
                    candies[I][J].setMove(1);
                    candies[I][J].changePosition(I-1, J);
                }
            }
        }
        else if(moveType == 3){ //move up
            m1 += y/3;

            rects[I][J].set(x*I , y*(candies[I][J].getY()+2) - m1, x*I + x,y*(candies[I][J].getY()+3) - m1);
            c.drawBitmap( bitmap[I][J], null, rects[I][J], null);
            if(m1 == y/3 * 3) {
                m1 = 0;

                if(candies[I][J].getMoved()){
                    candies[I][J].setMoved(false);
                    candies[I][J].setRunning(false);
                    candies[I][J].changePosition(I, J);
                    candies[I][J].setMove(0);
                    rects[I][J].set(I*x , (J+2)*y,(I+1)*x,(J+3)*y);
                }else{
                    candies[I][J].setMoved(true);
                    candies[I][J].setMove(4);
                    candies[I][J].changePosition(I, J-1);
                }
            }
        }
        else if(moveType == 4){ //move down
            m2 += y/3;

            rects[I][J].set(x*I , y*(candies[I][J].getY()+2) + m2, x*I + x,y*(candies[I][J].getY()+3) + m2);
            c.drawBitmap( bitmap[I][J], null, rects[I][J], null);
            if(m2 == y/3 * 3) {
                m2 = 0;

                if(candies[I][J].getMoved()){
                    candies[I][J].setMoved(false);
                    candies[I][J].setRunning(false);
                    candies[I][J].changePosition(I, J);
                    candies[I][J].setMove(0);
                    rects[I][J].set(I*x , (J+2)*y,(I+1)*x,(J+3)*y);
                }else{
                    candies[I][J].setMoved(true);
                    candies[I][J].setMove(3);
                    candies[I][J].changePosition(I, J+1);
                }
            }
        }
        else if(moveType == 0){
            c.drawBitmap( bitmap[I][J], null, rects[I][J], null);
        }
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        can = c;
        c.drawColor(Color.GRAY);

        for(int j = 0; j < 9 ; j++){
            for(int i = 0; i < 9; i++){
                drawCandy(i, j, c);
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
                candies[i][j] = new candy(i,j,rand.nextInt(2), bitmap[i][j], this);
                rects[i][j] = new Rect();
                rects[i][j].set(i*x , (j+2)*y,(i+1)*x,(j+3)*y);

                candy_Type = String.format("ic_food%d", candies[i][j].getType());
                s  = getResources().getIdentifier(candy_Type,
                        "mipmap", "com.gu.ragui.candycrush" );
                bitmap[i][j] = BitmapFactory.decodeResource(getResources(), s);
            }
        }

        getHolder().unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format , int width , int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        int action = motionEvent.getActionMasked();

        if(action == MotionEvent.ACTION_DOWN){
            this.setWillNotDraw(true);
            down = true;
            x1 = motionEvent.getX();
            y1 = motionEvent.getY();

            inX = (int)(x1/x);
            inY = (int)(y1/y) - 2;
        }

        if(action == MotionEvent.ACTION_MOVE && down){
            move = true;
        }

        if(action == MotionEvent.ACTION_UP && move){
            down = move = false;
            x2 = motionEvent.getX();
            y2 = motionEvent.getY();

            outX = (int)(x2/x);
            outY = (int)(y2/y) - 2;

            if(outX >= inX+1){ //righ
                candies[inX][inY].setMove(1);
                candies[inX][inY].setRunning(true);
                candies[inX][inY].start();

                candies[inX+1][inY].setMove(2);
                candies[inX+1][inY].setRunning(true);
                candies[inX+1][inY].start();
            }else if(outX <= inX-1){ //left
                candies[inX][inY].setMove(2);
                candies[inX][inY].setRunning(true);
                candies[inX][inY].start();

                candies[inX-1][inY].setMove(1);
                candies[inX-1][inY].setRunning(true);
                candies[inX-1][inY].start();
            }else if(outY >= inY+1){    //down
                candies[inX][inY].setMove(4);
                candies[inX][inY].setRunning(true);
                candies[inX][inY].start();

                candies[inX][inY+1].setMove(3);
                candies[inX][inY+1].setRunning(true);
                candies[inX][inY+1].start();
            }else if(outY <= inY-1){    //up
                candies[inX][inY].setMove(3);
                candies[inX][inY].setRunning(true);
                candies[inX][inY].start();

                candies[inX][inY-1].setMove(4);
                candies[inX][inY-1].setRunning(true);
                candies[inX][inY-1].start();
            }

        }

        return true;
    }

    private boolean possibleMove(int X1, int Y1, int X2, int Y2) {
        // 1- change/destroy rectangles
        // 2- change/destroy candies
        // returns true if the move is possible, and false otherwise


        if(X1 == X2+1){ //left

        }
        else if(X2 == X1+1){   //right

            if(     (Y1 > 0 && candies[X1][Y1].getType() == candies[X2][Y1-1].getType()) ||
                    (Y1 <= 7 && candies[X1][Y1].getType() == candies[X2][Y1+1].getType()) ){

                if(Y1 > 1 && candies[X1][Y1].getType() == candies[X2][Y1-1].getType() &&
                        candies[X1][Y1].getType() == candies[X2][Y1-2].getType()){
                    rects[X2][Y1-1].setEmpty();
                    rects[X2][Y1-2].setEmpty();
                }
                if(Y1 <= 6 && candies[X1][Y1].getType() == candies[X2][Y1+1].getType() &&
                        candies[X1][Y1].getType() == candies[X2][Y1+2].getType()){
                    rects[X2][Y1+1].setEmpty();
                    rects[X2][Y1+2].setEmpty();
                }
                if(candies[X1][Y1].getType() == candies[X2][Y1-1].getType() &&
                        candies[X1][Y1].getType() == candies[X2][Y1+1].getType()){
                    rects[X2][Y1+1].setEmpty();
                    rects[X2][Y1-1].setEmpty();
                }
                if(X2 <= 6 && candies[X1][Y1].getType() == candies[X2+1][Y1].getType()
                        && candies[X1][Y1].getType() == candies[X2+2][Y1].getType()){
                    rects[X2+1][Y1].setEmpty();
                    rects[X2+2][Y1].setEmpty();
                }

            }
            else if(X2 <= 6 && candies[X1][Y1].getType() == candies[X2+1][Y1].getType()
                   && candies[X1][Y1].getType() == candies[X2+2][Y1].getType()){
               swap(X1, Y1, X2, Y2);
               rects[X2+1][Y1].setEmpty();
               rects[X2+2][Y1].setEmpty();
           }
           else{
               return false;
           }
        }
        else if(Y1 == Y2+1) { //up

        }
        else{ //down

        }

        invalidate();
        return true;
    }

    private void swap(int X1, int Y1, int X2, int Y2) {

    }

}
