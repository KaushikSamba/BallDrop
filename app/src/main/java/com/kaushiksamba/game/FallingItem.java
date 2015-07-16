package com.kaushiksamba.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

//import com.kaushiksamba.game.Gamepanel;

public class FallingItem extends GameObject
{
    private Bitmap bmp;
    private int colour;
    //    private int score;
    private double accY;
    //    private boolean playing;
//    private Animation animation;
//    private long startTime;
    private int level=0;
//    private boolean up;

/*
    public void levelUp()
    {
        level++;
    }
*/

    public FallingItem(Bitmap res, int colour, int level, float x)
    {
        y = 0;
        dy = 0;
//        score = 0;
//        height = h;
//        width = w;
//        x = width/2;
        this.x = x;
        this.colour = colour;
        bmp = res;
        this.level = level;
//        startTime = System.nanoTime();
    }

    public int getColour()
    {
        return colour;
    }

    public void update()
    {
//        long elapsed = (System.nanoTime() - startTime)/1000000;
        accY +=0.1 + (level*0.1);
        dy = (int) accY;
        if(dy>14) dy = 14;
        y+=dy*2;
        dy=0;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bmp,x,y,null);
    }

    public void resetAccY()
    {
        accY = 0;
    }
}
