package com.kaushiksamba.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class HighScore extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        final TextView textView = (TextView) findViewById(R.id.high_score_text);
        SharedPreferences preferences = getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        int highscore = preferences.getInt("High Score",0);
        textView.setText(Integer.toString(highscore));

        Button button = (Button) findViewById(R.id.reset_high_score_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(HighScore.this);
                builder.setMessage("Reset high score?")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //reset process
                                System.out.println("Reset");
                                SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile",MODE_PRIVATE).edit();
                                editor.putInt("High Score",0);
                                editor.apply();
                                textView.setText("0");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Do nothing
                                System.out.println("Nuffink");
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


}
