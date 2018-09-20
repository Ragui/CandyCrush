package com.gu.ragui.candycrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.TranslateAnimation;
import java.util.Random;

public class board extends SurfaceView implements SurfaceHolder.Callback {

    private candy candies[][] = new candy[9][9];
    private candy gameLoop;
    private Rect rects[][] = new Rect[9][9];
    private Bitmap bitmap[][] = new Bitmap[9][9];
    private Random rand = new Random();
    private int x = 0, y = 0;
    private boolean down = false, move = false, back = true, isY = false, upLeft = false;
    private double x1, x2, y1, y2;
    private String candy_Type = "";
    private int s;
    private TranslateAnimation anim;
    private int inX = 0, inY = 0, outX = 0, outY = 0;
    private int m1 = 0, m2 = 0;
    private boolean initial = true;
    private int droppedCandies = 0;
    private int howMany[] = new int[9];
    private boolean update = false;

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

               if( !(candies[I][J].getMoved()) && possibleMove( I, J, I+1, J)  ){
                   rects[I][J].set(I * x, (J + 2) * y, (I + 1) * x, (J + 3) * y);
                   rects[I + 1][J].set((I + 1) * x, (J + 2) * y, (I + 2) * x, (J + 3) * y);

                   if(possibleMove( I+1, J, I, J)){
                       rects[I][J].setEmpty();
                   }

                   //swap candies
                   candy temp_candy = new candy(candies[I][J]);
                   candies[I][J].swapCandy(candies[I + 1][J]);
                   candies[I + 1][J].swapCandy(temp_candy);

                   candies[I][J].changePosition(I,J);
                   candies[I+1][J].changePosition(I+1,J);

                   Bitmap temp_map = Bitmap.createBitmap(bitmap[I][J]);
                   bitmap[I][J] = Bitmap.createBitmap(bitmap[I + 1][J]);
                   bitmap[I + 1][J] = Bitmap.createBitmap(temp_map);

                   c.drawBitmap(bitmap[I][J], null, rects[I][J], null);

                   rects[I + 1][J].setEmpty();
                   c.drawBitmap(bitmap[I + 1][J], null, rects[I + 1][J], null);

                   candies[I][J].setMove(0);
                   candies[I + 1][J].setMove(0);
                   candies[I][J].setMoved(false);
                   candies[I+1][J].setMoved(false);

                   gameLoop.setRunning(false);

                   m2 = 0;
                   update = true;

               } else if(candies[I][J].getMoved()){
                    candies[I][J].setMoved(false);
                    gameLoop.setRunning(false);
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

                if( !(candies[I][J].getMoved()) && possibleMove( I, J, I-1, J)  ){
                    rects[I][J].set(I * x, (J + 2) * y, (I + 1) * x, (J + 3) * y);
                    rects[I - 1][J].set((I - 1) * x, (J + 2) * y, I * x, (J + 3) * y);

                    //swap candies
                    candy temp_candy = new candy(candies[I][J]);
                    candies[I][J].swapCandy(candies[I - 1][J]);
                    candies[I - 1][J].swapCandy(temp_candy);

                    candies[I][J].changePosition(I,J);
                    candies[I-1][J].changePosition(I-1,J);

                    Bitmap temp_map = Bitmap.createBitmap(bitmap[I][J]);
                    bitmap[I][J] = Bitmap.createBitmap(bitmap[I - 1][J]);
                    bitmap[I - 1][J] = Bitmap.createBitmap(temp_map);

                    c.drawBitmap(bitmap[I][J], null, rects[I][J], null);

                    rects[I - 1][J].setEmpty();
                    c.drawBitmap(bitmap[I - 1][J], null, rects[I - 1][J], null);

                    candies[I][J].setMove(0);
                    candies[I - 1][J].setMove(0);
                    candies[I][J].setMoved(false);

                    candies[I-1][J].setMoved(false);
                    gameLoop.setRunning(false);

                    update = true;

                }else if(candies[I][J].getMoved()){
                    candies[I][J].setMoved(false);
                    gameLoop.setRunning(false);
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
                if( !(candies[I][J].getMoved()) && possibleMove( I, J, I, J-1)  ){
                    rects[I][J].set(I * x, (J + 2) * y, (I + 1) * x, (J + 3) * y);
                    rects[I][J-1].set((I) * x, (J + 1) * y, (I + 1) * x, (J + 2) * y);

                    if(possibleMove( I, J-1, I, J)){
                        rects[I][J].setEmpty();
                    }

                    //swap candies
                    candy temp_candy = new candy(candies[I][J]);
                    candies[I][J].swapCandy(candies[I][J-1]);
                    candies[I][J-1].swapCandy(temp_candy);

                    candies[I][J].changePosition(I,J);
                    candies[I][J-1].changePosition(I,J-1);

                    Bitmap temp_map = Bitmap.createBitmap(bitmap[I][J]);
                    bitmap[I][J] = Bitmap.createBitmap(bitmap[I][J-1]);
                    bitmap[I][J-1] = Bitmap.createBitmap(temp_map);

                    c.drawBitmap(bitmap[I][J], null, rects[I][J], null);

                    rects[I][J-1].setEmpty();
                    c.drawBitmap(bitmap[I][J-1], null, rects[I][J-1], null);

                    candies[I][J].setMove(0);
                    candies[I][J-1].setMove(0);
                    candies[I][J].setMoved(false);

                    candies[I][J-1].setMoved(false);
                    gameLoop.setRunning(false);
                    m2 = 0;
                    update = true;

                }else if(candies[I][J].getMoved()){
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

                if( !(candies[I][J].getMoved()) && possibleMove( I, J, I, J+1)  ){
                    rects[I][J].set(I * x, (J + 2) * y, (I + 1) * x, (J + 3) * y);
                    rects[I][J+1].set((I) * x, (J + 3) * y, (I + 1) * x, (J + 4) * y);

                    //swap candies
                    candy temp_candy = new candy(candies[I][J]);
                    candies[I][J].swapCandy(candies[I][J+1]);
                    candies[I][J+1].swapCandy(temp_candy);

                    candies[I][J].changePosition(I,J);
                    candies[I][J+1].changePosition(I,J+1);

                    Bitmap temp_map = Bitmap.createBitmap(bitmap[I][J]);
                    bitmap[I][J] = Bitmap.createBitmap(bitmap[I][J+1]);
                    bitmap[I][J+1] = Bitmap.createBitmap(temp_map);

                    c.drawBitmap(bitmap[I][J], null, rects[I][J], null);

                    rects[I][J+1].setEmpty();
                    c.drawBitmap(bitmap[I][J+1], null, rects[I][J+1], null);

                    candies[I][J].setMove(0);
                    candies[I][J].setMoved(false);

                    candies[I][J+1].setMove(0);
                    candies[I][J+1].setMoved(false);
                    gameLoop.setRunning(false);
                    update = true;

                }else if(candies[I][J].getMoved()){
                    candies[I][J].setMoved(false);
                    gameLoop.setRunning(false);
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
        else if(moveType > 4){ //drop candy
          int z = candies[I][J].addMoveStep(y/3);

            rects[I][J].set(x*I , y*(candies[I][J].getY()+2) + z, x*I + x,y*(candies[I][J].getY()+3) + z);
            c.drawBitmap( bitmap[I][J], null, rects[I][J], null);
            if(z == (y/3 * 3 * (moveType - 4)) ) {
                droppedCandies--;
                if(droppedCandies == 0){
                    gameLoop.setRunning(false);
                    droppedCandies = -1;
                }
                candies[I][J].resetMoveStep();

                //swap candies
                candies[I][J+(moveType-4)].swapCandy(candies[I][J]);
                candies[I][J+(moveType-4)].changePosition(I,J+(moveType-4));
                bitmap[I][J+(moveType-4)] = Bitmap.createBitmap(bitmap[I][J]);
                candies[I][J].setMove(0);

                candies[I][J+(moveType-4)].setMove(0);
                rects[I][J+(moveType-4)].set(x*I, (J+(moveType-4)+2 ) * y, (I+1)*x, (J+(moveType-4)+3 ) * y);
                c.drawBitmap(bitmap[I][J+(moveType-4)], null, rects[I][J+(moveType-4)], null);
            }
        }

    }

    private void dropNewCandies(int col, int count, Canvas c){
        //drop "count" candies on the "col"th column
            int i = 0;
            if(count != 0){
                droppedCandies++;
            }
            rects[col][i].set(col*x , (i+2)*y,(col+1)*x,(i+3)*y);
            candies[col][i].changeType(rand.nextInt(4));
            candies[col][i].setMove(4+ ((count == 0) ? -4 : count));
            candy_Type = String.format("ic_can%d", candies[col][i].getType());
            s  = getResources().getIdentifier(candy_Type,
                    "mipmap", "com.gu.ragui.candycrush" );
            bitmap[col][i] = BitmapFactory.decodeResource(getResources(), s);

    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        c.drawColor(Color.GRAY);

        for(int i = 0; i < 9 ; i++){
            for(int j = 8; j >= 0; j--){
                drawCandy(i, j, c);
            }
        }

        if(update){
            updateBoard();
            update = false;
        }

        if(droppedCandies == -1) {
            droppedCandies = 0;
            for (int i = 0; i < 9; i++) {
                if (howMany[i] > 0) {
                    dropNewCandies(i, howMany[i] - 1, c);
                    howMany[i]--;
                }
            }
            gameLoop.setRunning(true);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Canvas c = getHolder().lockCanvas();
        x  = c.getWidth()/9;
        y  = c.getHeight()/12;

        //allocate candies
        for(int j = 0; j < 9 ; j++){
            for(int i = 0; i < 9; i++){
                candies[i][j] = new candy(i,j,rand.nextInt(4), bitmap[i][j], this);
                rects[i][j] = new Rect();
                rects[i][j].set(i*x , (j+2)*y,(i+1)*x,(j+3)*y);

                candy_Type = String.format("ic_can%d", candies[i][j].getType());
                s  = getResources().getIdentifier(candy_Type,
                        "mipmap", "com.gu.ragui.candycrush" );
                bitmap[i][j] = BitmapFactory.decodeResource(getResources(), s);
            }
        }

        gameLoop = new candy(0,0,0, bitmap[0][0], this);
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

            if(outX >= inX+1 && inX < 8){ //righ
                candies[inX][inY].setMove(1);
                candies[inX+1][inY].setMove(2);
            }else if(outX <= inX-1 && inX > 0){ //left
                candies[inX][inY].setMove(2);
                candies[inX-1][inY].setMove(1);
            }else if(outY >= inY+1 && inY < 8){    //down
                candies[inX][inY].setMove(4);
                candies[inX][inY+1].setMove(3);
            }else if(outY <= inY-1 && inY > 0){    //up
                candies[inX][inY].setMove(3);
                candies[inX][inY-1].setMove(4);
            }
            if(initial){
                this.setWillNotDraw(true);
                initial = false;
            }

            gameLoop.setRunning(true);
            if( !(gameLoop.isAlive())) {
                gameLoop.start();
            }
        }

        return true;
    }

    private boolean possibleMove(int X1, int Y1, int X2, int Y2) {
        // 1- change/destroy rectangles
        // 2- change/destroy candies
        // returns true if the move is possible, and false otherwise
        boolean yes = false;


        if(X1 == X2+1){ //left
            if(     (Y1 > 0 && candies[X1][Y1].getType() == candies[X2][Y1-1].getType()) ||
                    (Y1 <= 7 && candies[X1][Y1].getType() == candies[X2][Y1+1].getType()) ){

                if(Y1 > 1 && candies[X1][Y1].getType() == candies[X2][Y1-1].getType() &&
                        candies[X1][Y1].getType() == candies[X2][Y1-2].getType()){

                   // candies[X2][Y1-1].setMove(-1);
                   // candies[X2][Y1-2].setMove(-1);

                    rects[X2][Y1-1].setEmpty();
                    rects[X2][Y1-2].setEmpty();
                    yes = true;
                }
                if(Y1 <= 6 && candies[X1][Y1].getType() == candies[X2][Y1+1].getType() &&
                        candies[X1][Y1].getType() == candies[X2][Y1+2].getType()){
                    rects[X2][Y1+1].setEmpty();
                    rects[X2][Y1+2].setEmpty();
                    yes = true;

                }
                if( (Y1 > 0 && candies[X1][Y1].getType() == candies[X2][Y1-1].getType()) &&
                        (Y1 <= 7 && candies[X1][Y1].getType() == candies[X2][Y1+1].getType()) ){
                    rects[X2][Y1+1].setEmpty();
                    rects[X2][Y1-1].setEmpty();
                    yes = true;

                }
                if(X2 > 1 && candies[X1][Y1].getType() == candies[X2-1][Y1].getType()
                        && candies[X1][Y1].getType() == candies[X2-2][Y1].getType()){
                    rects[X2-1][Y1].setEmpty();
                    rects[X2-2][Y1].setEmpty();
                    yes = true;
                }
                return yes;

            }
            else if(X2 > 1 && candies[X1][Y1].getType() == candies[X2-1][Y1].getType()
                    && candies[X1][Y1].getType() == candies[X2-2][Y1].getType()){
                rects[X2-1][Y1].setEmpty();
                rects[X2-2][Y1].setEmpty();
            }
            else{
                return false;
            }
        }
        else if(X2 == X1+1){   //right

            if(     (Y1 > 0 && candies[X1][Y1].getType() == candies[X2][Y1-1].getType()) ||
                    (Y1 <= 7 && candies[X1][Y1].getType() == candies[X2][Y1+1].getType()) ){

                if(Y1 > 1 && candies[X1][Y1].getType() == candies[X2][Y1-1].getType() &&
                        candies[X1][Y1].getType() == candies[X2][Y1-2].getType()){
                    rects[X2][Y1-1].setEmpty();
                    rects[X2][Y1-2].setEmpty();
                    yes = true;
                }
                if(Y1 <= 6 && candies[X1][Y1].getType() == candies[X2][Y1+1].getType() &&
                        candies[X1][Y1].getType() == candies[X2][Y1+2].getType()){
                    rects[X2][Y1+1].setEmpty();
                    rects[X2][Y1+2].setEmpty();
                    yes = true;

                }
                if((Y1 > 0 && candies[X1][Y1].getType() == candies[X2][Y1-1].getType()) &&
                        (Y1 <= 7 && candies[X1][Y1].getType() == candies[X2][Y1+1].getType()) ){
                    rects[X2][Y1+1].setEmpty();
                    rects[X2][Y1-1].setEmpty();
                    yes = true;

                }
                if(X2 <= 6 && candies[X1][Y1].getType() == candies[X2+1][Y1].getType()
                        && candies[X1][Y1].getType() == candies[X2+2][Y1].getType()){
                    rects[X2+1][Y1].setEmpty();
                    rects[X2+2][Y1].setEmpty();
                    yes = true;

                }
                return yes;

            }
            else if(X2 <= 6 && candies[X1][Y1].getType() == candies[X2+1][Y1].getType()
                   && candies[X1][Y1].getType() == candies[X2+2][Y1].getType()){
               rects[X2+1][Y1].setEmpty();
               rects[X2+2][Y1].setEmpty();
           }
           else{
               return false;
           }
        }
        else if(Y1 == Y2+1) { //up

            if(     (X1 > 0 && candies[X1][Y1].getType() == candies[X2-1][Y2].getType()) ||
                    (X1 <= 7 && candies[X1][Y1].getType() == candies[X2+1][Y2].getType()) ){

                if(X2 > 1 && candies[X1][Y1].getType() == candies[X2-1][Y2].getType() &&
                        candies[X1][Y1].getType() == candies[X2-2][Y2].getType()){
                    rects[X2-1][Y2].setEmpty();
                    rects[X2-2][Y2].setEmpty();
                    yes = true;
                }
                if(X2 <= 6 && candies[X1][Y1].getType() == candies[X2+1][Y2].getType() &&
                        candies[X1][Y1].getType() == candies[X2+2][Y2].getType()){
                    rects[X2+1][Y2].setEmpty();
                    rects[X2+2][Y2].setEmpty();
                    yes = true;

                }
                if((X1 > 0 && candies[X1][Y1].getType() == candies[X2-1][Y2].getType()) &&
                        (X1 <= 7 && candies[X1][Y1].getType() == candies[X2+1][Y2].getType()) ){
                    rects[X2+1][Y2].setEmpty();
                    rects[X2-1][Y2].setEmpty();
                    yes = true;

                }
                if(Y2 > 1 && candies[X1][Y1].getType() == candies[X2][Y2-1].getType()
                        && candies[X1][Y1].getType() == candies[X2][Y2-2].getType()){
                    rects[X2][Y2-1].setEmpty();
                    rects[X2][Y2-2].setEmpty();
                    yes = true;
                }
                return yes;

            }
            if(Y2 > 1 && candies[X1][Y1].getType() == candies[X2][Y2-1].getType()
                    && candies[X1][Y1].getType() == candies[X2][Y2-2].getType()){
                rects[X2][Y2-1].setEmpty();
                rects[X2][Y2-2].setEmpty();
            }
            else{
                return false;
            }
        }
        else{ //down

            if(     (X1 > 0 && candies[X1][Y1].getType() == candies[X2-1][Y2].getType()) ||
                    (X1 <= 7 && candies[X1][Y1].getType() == candies[X2+1][Y2].getType()) ){

                if(X2 > 1 && candies[X1][Y1].getType() == candies[X2-1][Y2].getType() &&
                        candies[X1][Y1].getType() == candies[X2-2][Y2].getType()){
                    rects[X2-1][Y2].setEmpty();
                    rects[X2-2][Y2].setEmpty();
                    yes = true;
                }
                if(X2 <= 6 && candies[X1][Y1].getType() == candies[X2+1][Y2].getType() &&
                        candies[X1][Y1].getType() == candies[X2+2][Y2].getType()){
                    rects[X2+1][Y2].setEmpty();
                    rects[X2+2][Y2].setEmpty();
                    yes = true;

                }
                if((X1 > 0 && candies[X1][Y1].getType() == candies[X2-1][Y2].getType()) &&
                        (X1 <= 7 && candies[X1][Y1].getType() == candies[X2+1][Y2].getType()) ){
                    rects[X2+1][Y2].setEmpty();
                    rects[X2-1][Y2].setEmpty();
                    yes = true;

                }
                if(Y2 <= 6 && candies[X1][Y1].getType() == candies[X2][Y2+1].getType()
                        && candies[X1][Y1].getType() == candies[X2][Y2+2].getType()){
                    rects[X2][Y2+1].setEmpty();
                    rects[X2][Y2+2].setEmpty();
                    yes = true;
                }
                return yes;

            }
            if(Y2 <= 6 && candies[X1][Y1].getType() == candies[X2][Y2+1].getType()
                    && candies[X1][Y1].getType() == candies[X2][Y2+2].getType()){
                rects[X2][Y2+1].setEmpty();
                rects[X2][Y2+2].setEmpty();
            }
            else{
                return false;
            }
        }

        return true;
    }

    private void updateBoard(){
        int count = 0, start = 0;
        droppedCandies = 0;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(rects[i][j].isEmpty()){
                    if(count == 0){
                        start = j;
                    }
                    count++;
                }
            }
            if(count != 0){
                dropCandyAt(count, start, i);
            }
            howMany[i] = count;
            count = start = 0;

        }
        if(droppedCandies > 0){
            gameLoop.setRunning(true);
        }
    }

    private void dropCandyAt(int count, int start, int i) {
        if(start > 0) {
            for (int j = start - 1; j >= 0; j--) {
                candies[i][j].setMove(4 + count);
                droppedCandies++;
            }
        }
    }
}
