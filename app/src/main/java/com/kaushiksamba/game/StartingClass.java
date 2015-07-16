package com.kaushiksamba.game;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class StartingClass extends Activity
        //implements Runnable
{

    int REQUEST_CODE = 1;
    boolean pause = false;
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
    //resultCode returns the difficulty setting
    //    super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE)
        {
//            int difficulty = resultCode;
//            System.out.println(difficulty);
            //Setting up the environment

            //Removing title
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            //Full screen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(new Gamepanel(this,resultCode));
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartingClass.this);
        pause = true;
        builder.setMessage("Do you wish to leave?")
                .setPositiveButton("Back to Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        })
                .setNegativeButton("Stay here", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                 //       pause = false;
                    }
                })
                .create().show();
//        super.onBackPressed();
    }

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
        private boolean isExtreme = false;
        private boolean switched = false;
        private boolean switching = false;
        private long switchStartTime = 0;
        private int nextSwitchScore;
        //    private boolean ready = false;
//        private long startPauseTimer = 0;
//        private boolean pause = false;
//        private boolean shouldPause = false;
        private boolean shouldUpdate = true;
        private int textMoveUp=0;  //For the "Switch" message movement
//        private long startPauseContinueTime=0;
        private boolean isHighScore = false;
        private int createBallDelayTime;
        private double scoreDivisionFraction;
        private int difficulty;         //Easy, Medium, Hard, and Extreme Modes.
                                        //Easy - 0; Medium - 1; Hard - 2; Extreme - 3;
        private String difficulty_name = "";


        private void setNextSwitchScore()
        {
            if(score==0) nextSwitchScore=50 + rand.nextInt(5)*10;
                else nextSwitchScore = score + 30 + rand.nextInt(6)*10;
        }
        private void setBallDelayTime()
        {
            //Easy starts at 700 seconds delay; Medium - 650; Hard - 600; Extreme starts at 700 but enables switching.
            switch (difficulty)
            {
                case 0:     createBallDelayTime = 700;
                            scoreDivisionFraction = 4;
                            break;
                case 1:     createBallDelayTime = 650;
                            scoreDivisionFraction = 3.5;
                            break;
                case 2:     createBallDelayTime = 600;
                            scoreDivisionFraction = 3;
                            break;
                case 3:     createBallDelayTime = 650;
                            scoreDivisionFraction = 3.7;
                            isExtreme = true;
                            setNextSwitchScore();
                            break;
            }
        }
        public Gamepanel(Context context, int difficulty)
        {
            super(context);
            score = 0;
            this.difficulty = difficulty;
            setBallDelayTime();

            //Add callbacks to surfaceholder to "intercept" events
            getHolder().addCallback(this);

            //Make Gamepanel focusable
            setFocusable(true);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.woodbg));

//        item = new FallingItem(BitmapFactory.decodeResource(getResources(),R.drawable.redball),25,25);

            itemsList = new ArrayList<FallingItem>();
            itemStartTime = System.nanoTime();

            //Start the game loop
            thread = new MainThread(getHolder(),StartingClass.Gamepanel.this);
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

        private void removeBlueBall()
        {
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
                    isPlaying = false;
                }
            }
        }

        private void removeRedBall()
        {
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
                    isPlaying = false;
                }
            }
        }
        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            if(event.getAction()==MotionEvent.ACTION_DOWN && isPlaying && !pause)
            {
//                Toast.makeText(getContext(),Float.toString(getHeight()),Toast.LENGTH_SHORT).show();
                float xClick = event.getX();
                float left = getWidth()/3;
                float right = getWidth()*2/3;
                if(!switched)
                {
                    if (xClick < left)
                    {
                        removeBlueBall();
                    }
                    else if (xClick > right && event.getY() < getHeight() / 7.4)
                    {
                        pause = true;
//                    Toast.makeText(getContext(),"PAUSED",Toast.LENGTH_SHORT).show();
                    }
                    else if (xClick > right)
                    {
                        removeRedBall();
                    }
                }
                else        //Applicable only for extreme mode where the buttons and, hence, the button clicks change the ball removal.
                {
                    if (xClick < left)
                    {
                        removeRedBall();
                    }
                    else if (xClick > right && event.getY() < getHeight() / 7.4)
                    {
                        pause = true;
                    }
                    else if(xClick > right)
                    {
                        removeBlueBall();
                    }
                }
                System.out.println("Score: " + score);
                if(score == nextSwitchScore && isExtreme)
                {
                    pause = true;
                    switched=!switched;
                    switching = true;
                }
            }

            else if(pause && !switching)
            {
                pause = false;
//                if(switching) switching = false;
            }
//            else if(!isPlaying && !pause)
            else if(!isPlaying)         //Resetting the game
            {
                float yClick = event.getY();
//            System.out.println(getHeight()/4-50);
//            System.out.println(getHeight()/4+30);
//            System.out.println(yClick);
                final float scaleFactorY = getHeight()/(float)HEIGHT;
                if(yClick > (HEIGHT/4-50)*scaleFactorY && yClick < (HEIGHT/4+30)*scaleFactorY)
                {
                    score = 0;
                    nextSwitchScore=0;
                    if(isExtreme)
                    {
                        setNextSwitchScore();
                    }
                    switched = false;
                    isHighScore = false;
                    shouldUpdate = true;
                    isPlaying = true;
                }
                else if(yClick > (HEIGHT/2 - 30)*scaleFactorY && yClick < (HEIGHT/2+50)*scaleFactorY)
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey, I'm playing Ball Drop. It's an awesome game, so go ahead and download it! I scored " + Integer.toString(score) + " points on " + difficulty_name + "difficulty. Download it at #URL");
                    intent.setType("text/plain");
                    getContext().startActivity(Intent.createChooser(intent, "Share score with.."));
                }
            }

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
            if(isPlaying && !pause)
            {
                bg.update();

                //        item.update();

                long itemElapsed = (System.nanoTime() - itemStartTime) / 1000000;
//            if(itemElapsed > 650 - score/4 && !pause)
                if(itemElapsed > createBallDelayTime - score/scoreDivisionFraction)
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
                for(int i = 0; i < itemsList.size(); i++)
                {
                    itemsList.get(i).update();
                    if (itemsList.get(i).getY() > HEIGHT)     //If a ball falls off the screen
                    {
                        itemsList.clear();
                        isPlaying = false;                      //Player loses
                        System.out.println("Fell off screen");
                        break;
                    }
                }
            }
            else
            {
                if(switching)
                {
                    if(switchStartTime==0) switchStartTime = System.nanoTime();
                    if((System.nanoTime()-switchStartTime + score)/1000000 > 1200)
                    {
                        pause = false;
//                        level--;
                        switching = false;
                        textMoveUp=0;
                        switchStartTime = 0;
                        setNextSwitchScore();
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
                if(!pause)
                {
                    for (FallingItem fi : itemsList)
                    {
                        fi.draw(canvas);
                    }
                    if(isPlaying) drawIntro(canvas);
                }
                else
                {
                    if(isPlaying && !switching) bg.setToBlack(canvas,HEIGHT,WIDTH);
                    if(switching)
                    {
                        drawIntro(canvas);
                        drawSwitchingText(canvas);
                        textMoveUp+=3;
                    }
                }
                drawText(canvas);
                canvas.restoreToCount(savedstate);
            }
        }

        private void drawSwitchingText(Canvas canvas)
        {
            Paint paint1 = new Paint();
            paint1.setColor(Color.RED);
            paint1.setTextSize(55);
            canvas.drawText("SWITCH",WIDTH/7,HEIGHT/9*4 - textMoveUp,paint1);
            canvas.drawText("BITCH",WIDTH/3, HEIGHT/9*5 - textMoveUp,paint1);
        }
        private void drawIntro(Canvas canvas)
        {
            Paint paint = new Paint();

            if(!switched)
            {
                //Red circle on the right
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.redclickball), 2 * WIDTH / 3, HEIGHT / 5 * 3, paint);
                //Blue circle on the left
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueclickball), 6, HEIGHT / 5 * 3, paint);
            }
            else
            {
                //Blue circle on the right
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blueclickball), 2 * WIDTH / 3, HEIGHT / 5 * 3, paint);
                //Red circle on the left
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.redclickball), 6, HEIGHT / 5 * 3, paint);
            }
            //To draw the pause icon on the top right of the screen
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.pauseicon), WIDTH/5*4, 10, paint);
        }

        public void drawText(Canvas canvas)
        {
            //To draw the score on the screen
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(60);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            if(score==0) canvas.drawText("0", WIDTH / 2 - 20, HEIGHT - 20, paint);
            else
            {
                String scoreString = Integer.toString(score);
                canvas.drawText(scoreString, WIDTH / 2 - 30 * (scoreString.length() - 1), HEIGHT - 20, paint);
            }
            if(!isPlaying)
//        if(!isPlaying && !pause)
            {
                Paint paint1 = new Paint();
                paint1.setColor(Color.BLACK);
                canvas.drawRect(WIDTH/(float)21.3, HEIGHT / 4 - 50, WIDTH - 15, HEIGHT / 4 + 30, paint1);
                paint1.setTextSize(35);
                paint1.setColor(Color.WHITE);
                paint1.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText("PRESS TO RETRY", 25, HEIGHT / 4, paint1);

                paint1.setColor(Color.LTGRAY);
                paint1.setTextSize(45);
//                canvas.drawText("YOU LOST",35,HEIGHT/2,paint1);
                canvas.drawText("YOU SCORED",WIDTH/(float)14,HEIGHT/3*2+60,paint1);
/*
            paint1.setTextSize(20);
            canvas.drawText("PRESS RIGHT FOR RED BALLS", 25, HEIGHT / 3 * 2, paint1);
            canvas.drawText("PRESS LEFT FOR BLUE BALLS", 25, HEIGHT / 3 * 2 + 30, paint1);
            canvas.drawText("DON'T LET THE BALLS FALL!", 25, HEIGHT / 3 * 2 + 60, paint1);
*/
                paint1.setColor(Color.BLACK);
//                canvas.drawRect(WIDTH/(float)21.3, HEIGHT / 3 * 2 - 20, WIDTH - 15, HEIGHT / 3 * 2 + 60, paint1);
                canvas.drawRect(WIDTH/(float)21.3,HEIGHT/2-30,WIDTH-15,HEIGHT/2+50,paint1);
                paint1.setColor(Color.WHITE);
                paint1.setTextSize(35);
//                canvas.drawText("SHARE SCORE", WIDTH / 6, HEIGHT / 3 * 2 + 35, paint1);
                canvas.drawText("SHARE SCORE",WIDTH/6,HEIGHT/2+25,paint1);

                if(isHighScore)
                {
                    canvas.save();
                    canvas.rotate(-56, 35, HEIGHT);
                    paint1.setTextSize(60);
                    paint1.setColor(Color.GREEN);
                    canvas.drawText("NEW HIGH SCORE!", 35, HEIGHT, paint1);
                    canvas.restore();
                }
//            newGame();
                if(shouldUpdate)
                {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                    switch(difficulty)
                    {
                        case 0: difficulty_name = "Easy";
                            break;
                        case 1: difficulty_name = "Medium";
                            break;
                        case 2: difficulty_name = "Hard";
                            break;
                        case 3: difficulty_name = "Extreme";
                            break;
                    }
                    int highscore = sharedPreferences.getInt("High Score "+difficulty_name, 0);
                    if(score>highscore)
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("High Score "+difficulty_name,score);
                        editor.apply();
                        isHighScore = true;
                    }
//                isPlaying = true;
                    itemsList.clear();
                    level=0;
                    shouldUpdate = false;
                }
            }
        }
    }
}
