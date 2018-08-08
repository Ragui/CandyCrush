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
    private boolean down = false, move = false, back = true, isY = false;
    private double x1, x2, y1, y2;
    private String candy_Type = "";
    private int s;
    private TranslateAnimation anim;
    private int inX = 0, inY = 0, outX = 0, outY = 0;


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

    private void drawCandy(int I, int J, Canvas c){
        //Toast.makeText (getContext(), "drawCandy", Toast.LENGTH_LONG).show();
        c.drawBitmap( bitmap[I][J], null, rects[I][J], null);
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
                candies[i][j] = new candy(i,j,rand.nextInt(2), bitmap[i][j]);
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

            inX = (int)(x1/x);
            inY = (int)(y1/y) - 2;
        }

        if(action == MotionEvent.ACTION_MOVE && down){
            int tempX = (int)(motionEvent.getX());
            int tempY = (int)(motionEvent.getY());

            if(!move){
                if(abs(tempX-x1) > abs(tempY-y1)){
                    isY = false;
                }
                else{
                    isY = true;
                }
            }

           if( tempX < (int)x1 && inX != 0 && !isY){ // moving left
                if(tempX < ((inX-1)*(x) + (x/2)) ){

                    if(possibleMove(inX, inY, tempX/x, tempY/y)){

                    }
                    else{
                        rects[inX][inY].set( inX*x , (inY+2)*y,
                                (inX+1)*x , (inY+3)*y);
                        rects[inX-1][inY].set( (inX-1)*x , (inY+2)*y,
                                inX*x , (inY+3)*y);
                    }

                    back = false;
                }
                else{
                    int z = (inX*x) + x/2 - tempX;
                    rects[inX][inY].set(tempX - (x/2) , (inY+2)*y,
                            tempX + (x/2), (inY+3)*y);
                    rects[inX-1][inY].set( (inX-1)*x + z, (inY+2)*y,
                            inX*x + z, (inY+3)*y );
                }
            }
            else if(tempX > (int)x1 && inX != 8 && !isY){ //moving right
                if(tempX > ((inX+1)*(x) + (x/2)) ){
                    if(possibleMove(inX, inY, tempX/x, tempY/y)){

                    }
                    else {
                        rects[inX][inY].set(inX * x, (inY + 2) * y,
                                (inX + 1) * x, (inY + 3) * y);
                        rects[inX+1][inY].set( (inX+1)*x , (inY+2)*y,
                                (inX+2)*x , (inY+3)*y);
                    }
                    back = false;
                }
                else{
                    int z = tempX - (inX*x) - x/2;
                    rects[inX][inY].set(tempX - (x/2) , (inY+2)*y,
                            (x/2) +  tempX, (inY+3)*y);
                    rects[inX+1][inY].set( (inX+1)*x - z, (inY+2)*y,
                            (inX+2)*x - z, (inY+3)*y );
                }
            }
            else if(tempY <  (int)y1 && inY != 0){ // moving up
                if(tempY < ( (inY+2-1)*(y) + (y/2) )  ){
                    if(possibleMove(inX, inY, tempX/x, tempY/y)){

                    }
                    else {
                        rects[inX][inY].set(inX * x, (inY + 2) * y,
                                (inX + 1) * x, (inY + 3) * y);
                        rects[inX][inY-1].set( (inX)*x , (inY+2-1)*y,
                                (inX+1)*x , (inY+3-1)*y);
                    }
                    back = false;
                }
                else{
                    int z = ((inY+2)*y) + y/2 - tempY;
                    rects[inX][inY].set( inX*x , tempY - (y/2),
                            (inX+1)*x, tempY + (y/2));
                    rects[inX][inY-1].set( (inX)*x, (inY+2-1)*y + z,
                            (inX+1)*x, (inY+3-1)*y +z);
                }
            }
            else if(tempY > (int)y1 && inY != 8){ // moving down
               if(tempY > ((inY+1+2)*(y) + (y/2)) ){

                   if(possibleMove(inX, inY, tempX/x, tempY/y)){

                   }
                   else {
                       rects[inX][inY].set(inX * x, (inY + 2) * y,
                               (inX + 1) * x, (inY + 3) * y);
                       rects[inX][inY+1].set( (inX)*x , (inY+2+1)*y,
                               (inX+1)*x , (inY+3+1)*y);
                   }

                   back = false;
               }
               else{
                   int z = ((inY+2)*y) + y/2 - tempY;
                   rects[inX][inY].set( inX*x , tempY - (y/2),
                           (inX+1)*x, tempY + (y/2));
                   rects[inX][inY+1].set( (inX)*x, (inY+2+1)*y + z,
                           (inX+1)*x, (inY+3+1)*y +z);

               }
           }

            invalidate();
            move = true;
        }

        if(action == MotionEvent.ACTION_UP && move){
            down = move = false;
            x2 = motionEvent.getX();
            y2 = motionEvent.getY();

            outX = (int)(x2/x);
            outY = (int)(y2/y) - 2;

            if (back){
                //Toast.makeText(getContext(), "UP", Toast.LENGTH_LONG).show();
                rects[inX][inY].set( inX*x , (inY+2)*y,
                        (inX+1)*x , (inY+3)*y);
                if(isY){
                    if(inY > 0){
                        rects[inX][inY-1].set( (inX)*x , (inY+2-1)*y,
                                (inX+1)*x , (inY+3-1)*y);
                    }
                    if(inY < 8){
                        rects[inX][inY+1].set( (inX)*x , (inY+2+1)*y,
                                (inX+1)*x , (inY+3+1)*y);
                    }
                }
                else{
                    if(inX > 0){
                        rects[inX-1][inY].set( (inX-1)*x , (inY+2)*y,
                                (inX)*x , (inY+3)*y);
                    }
                    if(inX < 8){
                        rects[inX+1][inY].set( (inX+1)*x , (inY+2)*y,
                                (inX+2)*x , (inY+3)*y);
                    }
                }

                invalidate();
            }
            back = true;
            isY = false;
        }

        return true;
    }

    private boolean possibleMove(int X1, int Y1, int X2, int Y2){
        // 1- change/destroy rectangles
        // 2- change/destroy candies
        // returns true if the move is possible, and false otherwise
        return false;
    }

}
