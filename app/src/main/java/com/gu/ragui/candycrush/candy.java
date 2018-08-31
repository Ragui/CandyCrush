package com.gu.ragui.candycrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.graphics.Canvas;


public class candy extends Thread{

    private board b;
    private int x, y; // position of the candy in the board
    private int type; // the candy type, from 0 to 5 (6 types)
    private int s;
    private String candy_Type;
    private int move; //0 -> not moving, 1 -> right, 2 -> left, 3 -> up, 4 -> down
    private boolean running, moved;


    public candy(int i, int j, int t, Bitmap b, board bView) {
        x = i;
        y = j;
        type = t;
        move = 0;

        candy_Type = String.format("ic_food%d", t);
       /* s  = getResources().getIdentifier(candy_Type,
                "mipmap", "com.gu.ragui.candycrush" );
        b = BitmapFactory.decodeResource(getResources(), s);*/

       this.b = bView;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getType(){
        return type;
    }

    public int getMove(){
        return move;
    }

    public void changeType(int t){
        type = t;
    }

    public void changePosition(int i, int j){
        x = i;
        y = j;
    }

    public void setRunning (boolean r){
        running = r;
    }

    public void setMoved (boolean m){
        moved = m;
    }

    public boolean getMoved(){
        return  moved;
    }

    public void setMove(int m){
        move = m;
    }

    protected void finalize() throws Throwable{

    }

    @Override
    public void run() {
        while (running) {
            Canvas c = null;
            try {
                c = b.getHolder().lockCanvas();
                synchronized (b.getHolder()) {
                    b.onDraw(c);
                }
            } finally {
                if (c != null) {
                    b.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }
}


