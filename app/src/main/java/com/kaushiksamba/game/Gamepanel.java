package com.kaushiksamba.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/*
List of things to do now.
    1. Enable click to remove ball. [DONE]
    2. Score increments.
    3. Randomize red and blue ball generation.
    4. Ball to bottom = death.
    5. Score-level-speed relationships.
 */

public class Gamepanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 320;
    public static final int HEIGHT = 480;
    private int score;
    private int level = 0;
    private MainThread thread;
    private Background bg;
//    private FallingItem item;
    private ArrayList<FallingItem> itemsList;
    private long itemStartTime;
    private Random rand = new Random();
    private boolean isPlaying = true;
    private boolean ready = false;

    public Gamepanel(Context context)
    {
        super(context);
        score = 0;
        //Add callbacks to surfaceholder to "intercept" events
        getHolder().addCallback(this);

        //Make Gamepanel focusable
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.woodbg));

//        item = new FallingItem(BitmapFactory.decodeResource(getResources(),R.drawable.redball),25,25);

        itemsList = new ArrayList<FallingItem>();
        itemStartTime = System.nanoTime();

        //Start the game loop
        thread = new MainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
//        finishTheActivity();

        boolean retry = true;
        int counter = 0;
        while(retry && counter <1000)
        {
            counter++;
            try
            {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN && isPlaying)
        {
            float xClick = event.getX();
            float left = getWidth()/3;
            float right = getWidth()*2/3;
            if(xClick < left)
            {

                //Toast.makeText(getContext(),"Left clicked",Toast.LENGTH_SHORT).show();
                //Removing blue ball
                if(itemsList.size()>0)
                {
                    if(itemsList.get(0).getColour()==1)
                    {
                        itemsList.remove(0);
                        score+=10;
                        if(score%40==0) level++;
                    }
                    else
                    {
//                        Toast.makeText(getContext(),"YOU LOSE",Toast.LENGTH_SHORT).show();
                        isPlaying = false;
                    }
                }

            }
            else if(xClick > right)
            {
              //  Toast.makeText(getContext(),"Right clicked",Toast.LENGTH_SHORT).show();
                //Removing red ball
                if(itemsList.size()>0)
                {
                    if(itemsList.get(0).getColour()==0)
                    {
                        itemsList.remove(0);    //Removing the ball
                        score+=10;              //Updating the score
                        if(score%40==0)
                        {
                            level++;
                        }
                    }
                    else
                    {
//                        Toast.makeText(getContext(),"YOU LOSE",Toast.LENGTH_SHORT).show();
                        isPlaying = false;
                    }
                }

            }
            System.out.println("Score: " + score);
        }

        else if(!isPlaying)
        {
            isPlaying = true;
            score = 0;
            level = 0;
            itemsList.clear();
            ready = true;
        }

/*
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            item.resetAccY();
            return true;
        }
*/
        return super.onTouchEvent(event);
    }

    private int randomItemColour()      // 0 - RED ; 1 - BLUE
    {
        return rand.nextInt(2);
    }

    private int randomXCoordinate()
    {
        int rXC = WIDTH/4;
        switch(rand.nextInt(3))
        {
            case 0: break;
            case 1: rXC = 2 * rXC;
                    break;
            case 2: rXC = 3 * rXC;
                    break;
        }
        return rXC;
    }
    public void update()
    {
        if(isPlaying)
        {
            bg.update();

            //        item.update();

            long itemElapsed = (System.nanoTime() - itemStartTime) / 1000000;
            if(itemElapsed > 650 - score/4)
//            if (itemElapsed > 2000)
            {
                int colour = randomItemColour();
//                int colour = 0;
                if (colour == 0)
                {
                    itemsList.add(new FallingItem(BitmapFactory.decodeResource(getResources(), R.drawable.redball), colour, level, randomXCoordinate()));
                }
                    else
                    {
                        itemsList.add(new FallingItem(BitmapFactory.decodeResource(getResources(), R.drawable.blueball), colour, level, randomXCoordinate()));
                    }
                itemStartTime = System.nanoTime();
            }

            for (int i = 0; i < itemsList.size(); i++) {
                itemsList.get(i).update();
                if (itemsList.get(i).getY() > HEIGHT)     //If a ball falls off the screen
                {
                    itemsList.remove(i);
                    isPlaying = false;                      //Player loses
                    System.out.println("Fell off screen");
                    break;
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(float)WIDTH;
        final float scaleFactorY = getHeight()/(float) HEIGHT;
        if(canvas!=null)
        {
            final int savedstate = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);

//            item.draw(canvas);
            for(FallingItem fi : itemsList)
            {
                fi.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedstate);
        }
    }

    public void drawText(Canvas canvas)
    {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText("SCORE: " + score, WIDTH / 4, HEIGHT - 20, paint);

        if(!isPlaying)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(35);
            paint1.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText("PRESS TO RETRY", 25, HEIGHT / 4, paint1);

            paint1.setTextSize(55);
            canvas.drawText("YOU LOST",35,HEIGHT/2,paint1);
//            System.out.println("Final score: " + score);
//            canvas.drawText("YOU SCORED: " + score, WIDTH / 10, HEIGHT - 20, paint1);
            paint1.setTextSize(20);
            canvas.drawText("PRESS RIGHT FOR RED BALLS", 25, HEIGHT / 3 * 2, paint1);
            canvas.drawText("PRESS LEFT FOR BLUE BALLS", 25, HEIGHT / 3 * 2 + 30, paint1);
            canvas.drawText("DON'T LET THE BALLS FALL!", 25, HEIGHT / 3 * 2 + 60, paint1);

//            newGame();
            itemsList.clear();
        }
    }
}
