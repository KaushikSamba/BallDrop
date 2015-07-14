package com.kaushiksamba.game;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class DifficultySelection extends Activity
        //ActionBarActivity
{
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_difficulty_selection);
//        ListView listView = (ListView) findViewById(R.id.difficultyList);
//        String[] array = new String[]{"Easy","Medium","Hard","Extreme"};

            Button easy = (Button) findViewById(R.id.difficulty_easy);
            Button medium = (Button) findViewById(R.id.difficulty_medium);
            Button hard = (Button) findViewById(R.id.difficulty_hard);
            Button extreme = (Button) findViewById(R.id.difficulty_extreme);

            easy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    openGame(0);
                }
            });

            medium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGame(1);
                }
            });

            hard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGame(2);
                }
            });

            extreme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //openGame(3);
                    Toast.makeText(getApplicationContext(),"Extreme disabled, kanna",Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void openGame(int difficulty)
        {
//            Intent intent = new Intent(this,StartingClass.class);
//            intent.putExtra("Difficulty",difficulty);
//            System.out.println(difficulty);
//            startActivity(intent);
            setResult(difficulty);
            finish();
        }
}
