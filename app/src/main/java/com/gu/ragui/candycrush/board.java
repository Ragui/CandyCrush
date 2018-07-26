package com.gu.ragui.candycrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;

public class board extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap mybitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    private candy candies[][] = new candy[9][9];
    private Random rand = new Random();
    private int candyType = 6;

    public board(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

    }

    public board(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
        setFocusable(true);

    }

    public board(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    private void drawBoard(Canvas c){
        c.drawColor(Color.BLUE);
        int x  = c.getWidth()/9;
        int y  = c.getHeight()/12;
        for(int j = 0; j < 9 ; j++){
            for(int i = 0; i < 9; i++){
                candies[i][j] = new candy(i,j,rand.nextInt(6));
                drawCandy(candies[i][j],x,y,c);
            }
        }

    }

    private void drawCandy(candy cand, int width, int height, Canvas c){
        Rect x = new Rect();
        x.set(cand.getX()*width , (cand.getY()+2)*height,(cand.getX()+1)*width,(cand.getY()+3)*height);
        c.drawBitmap(mybitmap, null, x, null);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Canvas c = getHolder().lockCanvas();
        drawBoard(c);
        getHolder().unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format , int width , int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

}
