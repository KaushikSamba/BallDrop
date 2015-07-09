package com.kaushiksamba.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;


public class IntroScreen extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_intro_screen);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new introDisplayView(this));
    }

    class Letter extends GameObject
    {
        private char ch;
        private double accY;
        private int position;

        public Letter(int x, char ch, int position)
        {
            this.x = x;
            y=50;
            this.ch = ch;
            this.position=position;
        }

        public void draw(Canvas canvas)
        {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(55);
            canvas.drawText(Character.toString(ch),x,y,paint);
//            System.out.println("Letter " + ch + " drawn");
        }

        public void update()
        {
            accY += 0.1;
            dy = (int) accY;
            y+=dy*2;
            dy=0;
            switch(position)
            {
                case 0: x-=1;
                        break;
                case 1: x-=0.4;
                        break;
                case 2: x+=0.4;
                        break;
                case 3: x+=1;
                        break;
            }
        }
    }

    class introDisplayView extends SurfaceView implements SurfaceHolder.Callback
    {
        private introThread thread;
        private Background bg;
        private int HEIGHT = 384;
        private int WIDTH = 219;
        private ArrayList<Letter> letterList;
        private boolean mustUpdate = true;
        private long startTime;
        private boolean firstIteration = true;


        public introDisplayView(Context context)
        {
            super(context);
            getHolder().addCallback(this);
            letterList = new ArrayList<Letter>(Arrays.asList(new Letter(WIDTH/4 + 5,'B',0), new Letter(WIDTH/4+30,'A',1), new Letter(WIDTH/4+55,'L',2), new Letter(WIDTH/4+80,'L',3)));
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            if(!mustUpdate)
            {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    float yCoord = event.getY();
                    float height = getHeight();
                    float scaleFactorX = height/HEIGHT;
/*
                    System.out.println("1top: " + (height/5-40));
                    System.out.println("1bot: " + (height/5+20));
                    System.out.println("2top: " + (height/5+30));
                    System.out.println("2bot: " + (height/5+90));
                    System.out.println("3top: " + (height-80));
                    System.out.println("3bot: " + (height-15));
                    System.out.println("yCoord = " + yCoord);
*/
                    if(yCoord > (HEIGHT/5-40)*scaleFactorX && yCoord < (HEIGHT/5+20)*scaleFactorX)
                    {
                        //Play game
                        System.out.println("Play game");
                        Intent intent = new Intent(getContext(),StartingClass.class);
                        startActivity(intent);
                    }
                    else if(yCoord > (HEIGHT/5+30)*scaleFactorX && yCoord < (HEIGHT/5+90)*scaleFactorX)
                    {
                        //Instructions
                        System.out.println("Instructions");
                        Intent intent = new Intent(getContext(),Instructions.class);
                        startActivity(intent);
                    }
                    else if(yCoord > (HEIGHT-80)*scaleFactorX && yCoord < (HEIGHT - 15)*scaleFactorX)
                    {
                        //High Score
                        System.out.println("High score");
                        Intent intent = new Intent(getContext(),HighScore.class);
                        startActivity(intent);
                    }
                }
            }
            return super.onTouchEvent(event);
        }

        @Override
        public void draw(Canvas canvas)
        {
//            super.draw(canvas);
            float scaleFactorX = getWidth()/(float)WIDTH;
            float scaleFactorY = getHeight()/(float)HEIGHT;
            if(canvas!=null)
            {
                final int savedState = canvas.save();
                canvas.scale(scaleFactorX, scaleFactorY);

                //Drawing happens here
                    bg.draw(canvas);
                    for(Letter l : letterList)
                    {
                        l.draw(canvas);
                    }

                    if(!mustUpdate)
                    {
                        Paint paint = new Paint();
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(55);
                        float yCoord = letterList.get(0).getY()+45;
                        canvas.drawText("D", letterList.get(0).getX(), yCoord, paint);
                        canvas.drawText("R", letterList.get(1).getX(), yCoord, paint);
                        canvas.drawText("O", letterList.get(2).getX(), yCoord, paint);
                        canvas.drawText("P", letterList.get(3).getX(), yCoord, paint);
                        if(firstIteration)
                        {
                            startTime = System.nanoTime();
                            firstIteration = false;
                        }
                        if((System.nanoTime() - startTime)/1000000 > 1500)
                        {
//                            System.out.println("Width = " + getWidth());
//                            System.out.println("yCoord = " + yCoord);
                            canvas.drawRect(10, HEIGHT / 5 - 40, WIDTH - 10, HEIGHT / 5 + 20, paint);
                            canvas.drawRect(10, HEIGHT / 5 + 30, WIDTH - 10, HEIGHT / 5 + 90, paint);
                            canvas.drawRect(10, HEIGHT - 80, WIDTH - 10, HEIGHT - 15, paint);
                            paint.setTextSize(35);
                            paint.setColor(Color.WHITE);
                            canvas.drawText("PLAY GAME", 15, HEIGHT / 5 + 3, paint);
                            paint.setTextSize(28);
                            canvas.drawText("INSTRUCTIONS", 12, HEIGHT / 5 + 70, paint);
                            paint.setTextSize(34);
                            canvas.drawText("HIGH SCORE",11,HEIGHT-35,paint);
                        }
                    }
                canvas.restoreToCount(savedState);
            }
        }

        public void update()
        {
            //update details
            bg.update();
            for(int i=0;i<letterList.size();i++)
            {
                if(letterList.get(i).getY() <= HEIGHT*3/5)letterList.get(i).update();
                    else
                    {
                        mustUpdate = false;
                    }
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.introbg));
            thread = new introThread(this,getHolder());
            thread.setRunning(true);
            thread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
            boolean retry = true;

            while(retry)
            {
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
    }

    class introThread extends Thread
    {
        private introDisplayView view;
        private SurfaceHolder holder;
        private Canvas canvas;
        private boolean running;
        public introThread(introDisplayView view, SurfaceHolder holder)
        {
            super();
            this.view = view;
            this.holder = holder;
        }

        public void setRunning(boolean running)
        {
            this.running = running;
        }

        @Override
        public void run()
        {
//            super.run();
            int FPS = 30;
            long startTime;
            int optimalTime = 1000/FPS;
            int frameCount=0;

            while(running)
            {
                startTime = System.nanoTime();
                canvas = null;

                try
                {
                    canvas = this.holder.lockCanvas();
                    synchronized (holder) {
                        this.view.update();
                        this.view.draw(canvas);
                    }
                }
                catch (Exception e){e.printStackTrace();}
                finally
                {
                    if(canvas!=null)
                    {
                        try
                        {
                            this.holder.unlockCanvasAndPost(canvas);
                        }
                        catch (Exception e) {e.printStackTrace();}
                    }
                }

                long intervalTime = (System.nanoTime() - startTime)/100000;
                long waitTime = optimalTime - intervalTime;

                try
                {
                    if(waitTime>0) this.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                frameCount++;

                if(frameCount==30)
                {
                    frameCount=0;
                }

            }
        }
    }

}
