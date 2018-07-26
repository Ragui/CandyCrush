package com.gu.ragui.candycrush;

public class candy {

    private int x, y; // position of the candy in the board
    private int type; // the candy type, from 0 to 5 (6 types)

    public candy(int i, int j, int t) {
        x = i;
        y = j;
        type = t;
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


