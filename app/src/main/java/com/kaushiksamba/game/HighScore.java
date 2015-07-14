package com.kaushiksamba.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


public class HighScore extends ActionBarActivity
{

    int[] array = new int[4];
    gridViewAdapter adapter;
    public class gridViewAdapter extends BaseAdapter
    {
        private Context context;
        private int[] array;
        private LayoutInflater inflater;

        public gridViewAdapter(Context context, int[] array)
        {
            this.context = context;
            this.array = array;
            inflater = (LayoutInflater) this.context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return array.length;
        }

        @Override
        public Object getItem(int position)
        {
            return array[position];
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view = inflater.inflate(R.layout.high_score_gridview_item,parent,false);
            String difficultyLevelText="";
            switch(position)
            {
                case 0: difficultyLevelText = "Easy";
                        break;
                case 1: difficultyLevelText = "Medium";
                        break;
                case 2: difficultyLevelText = "Hard";
                        break;
                case 3: difficultyLevelText = "Extreme";
                        break;
            }
            TextView difficultyLevel = (TextView) view.findViewById(R.id.difficulty_level_textView);
            TextView score = (TextView) view.findViewById(R.id.high_score_textView);
            difficultyLevel.setText(difficultyLevelText);
            score.setText(Integer.toString(array[position]));
            return view;
        }
    }


    private void getScores()
    {
        SharedPreferences preferences = getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
//        ScoreObject[] array = new ScoreObject[4];
//        array[0].setScore(preferences.getInt("High Score Easy",0));

        array[0] = preferences.getInt("High Score Easy",0);
        array[1] = preferences.getInt("High Score Medium",0);
        array[2] = preferences.getInt("High Score Hard",0);
        array[3] = preferences.getInt("High Score Extreme",0);
        System.out.println("Easy: " + array[0]);
        System.out.println("Medium: " + array[1]);
        System.out.println("Hard: " + array[2]);
        System.out.println("Extreme: " + array[3]);

//        array[1].score = preferences.getInt("High Score Medium",0);
//        array[2].score = preferences.getInt("High Score Hard",0);
//        array[3].score = preferences.getInt("High Score Extreme",0);
/*
        for(int i=0;i<4;i++)
        {
            switch(i)
            {
                case 0: array[i].difficulty = "Easy";
                        break;
                case 1: array[i].difficulty = "Medium";
                        break;
                case 2: array[i].difficulty = "Hard";
                        break;
                case 3: array[i].difficulty = "Extreme";
                        break;
            }
            array[i].score = preferences.getInt("High Score "+array[i].difficulty,0);
        }
*/
//       return array;
    }

    private void showScores()
    {
        GridView gridView = (GridView) findViewById(R.id.scores_gridView);
        adapter = new gridViewAdapter(this,array);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        getScores();
        showScores();

        Button button = (Button) findViewById(R.id.reset_high_score_button);
        final SharedPreferences preferences = getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(HighScore.this);
                builder.setMessage("Reset all high scores?")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("High Score Easy", 0);
                        editor.putInt("High Score Medium", 0);
                        editor.putInt("High Score Hard", 0);
                        editor.putInt("High Score Extreme", 0);
                        editor.apply();
                        for(int i=0;i<4;i++) array[i]=0;
                        adapter.notifyDataSetChanged();
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

/*
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
                builder.setMessage("Reset all high scores?")
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
*/
    }


}
