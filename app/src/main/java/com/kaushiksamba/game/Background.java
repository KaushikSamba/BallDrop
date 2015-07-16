package com.kaushiksamba.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Background
{
    Bitmap image;

    public Background(Bitmap bmp)
    {
        image = bmp;
    }

    public void update()
    {

    }

    public void setToBlack(Canvas canvas, int height, int width)
    {
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setTextSize(25);
        paint.setColor(Color.WHITE);
        canvas.drawText("CLICK TO RESUME",width/(float)6,height/2,paint);
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image,0,0,null);
    }
}
