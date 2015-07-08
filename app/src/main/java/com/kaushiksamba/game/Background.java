package com.kaushiksamba.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by kaushiksamba on 07-07-2015.
 */
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

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image,0,0,null);
    }
}
