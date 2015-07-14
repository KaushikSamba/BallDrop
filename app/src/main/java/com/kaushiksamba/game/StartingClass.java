package com.kaushiksamba.game;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


public class StartingClass extends Activity
        //implements Runnable
{

    int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_starting_class);

        //Difficulty setting menu
        Intent intent = new Intent(this,DifficultySelection.class);
        startActivityForResult(intent, REQUEST_CODE);

/*
//Setting up the environment

        //Removing title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new Gamepanel(this));
*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    //    super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE)
        {
            int difficulty = resultCode;
            System.out.println(difficulty);
            //Setting up the environment

            //Removing title
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            //Full screen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(new Gamepanel(this,difficulty));
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartingClass.this);
        builder.setMessage("Do you wish to leave?")
                .setPositiveButton("Back to Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
                .setNegativeButton("Stay here", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
//        super.onBackPressed();
    }
}
