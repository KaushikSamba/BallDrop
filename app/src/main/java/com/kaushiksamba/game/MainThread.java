package com.kaushiksamba.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread
{
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private Gamepanel gamepanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, Gamepanel gamepanel)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamepanel = gamepanel;
    }

    public void setRunning(boolean x)
    {
        running = x;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/FPS;

        while(running)
        {
            startTime = System.nanoTime();
            canvas = null;

            try
            {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    this.gamepanel.update();
                    this.gamepanel.draw(canvas);
                }
            }
            catch (Exception e){e.printStackTrace();}
            finally
            {
                if(canvas!=null)
                {
                    try
                    {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) { e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;

            try {
                if(waitTime>0) this.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if(frameCount==FPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount=0;
                totalTime=0;
//                System.out.println(averageFPS);
            }
        }

    }
}
