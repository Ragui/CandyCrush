package com.gu.ragui.candycrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

public class candy {

    Bitmap bitmap;
    private int x, y; // position of the candy in the board
    private int type; // the candy type, from 0 to 5 (6 types)
    private int s;
    private String candy_Type;

    public candy(int i, int j, int t, Bitmap b) {
        x = i;
        y = j;
        type = t;

        candy_Type = String.format("ic_food%d", t);
       /* s  = getResources().getIdentifier(candy_Type,
                "mipmap", "com.gu.ragui.candycrush" );
        b = BitmapFactory.decodeResource(getResources(), s);*/
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

    public void changeType(int t){
        type = t;
    }

    public void changePosition(int i, int j){
        x = i;
        y = j;
    }
}


