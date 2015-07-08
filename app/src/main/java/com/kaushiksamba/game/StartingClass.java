package com.kaushiksamba.game;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


public class StartingClass extends Activity implements Runnable
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_starting_class);

//Setting up the environment

        //Removing title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new Gamepanel(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
//This is to call the game loop
        Thread thread = new Thread(this);
        thread.start();
    }

    //This is the game loop
    @Override
    public void run()
    {
        while(true)
        {
//          redraw();
            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
