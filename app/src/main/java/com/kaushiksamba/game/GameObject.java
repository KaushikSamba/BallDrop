package com.kaushiksamba.game;

import android.graphics.Rect;


public abstract class GameObject
{
    protected float x,y;
    protected int dx, dy;
    protected int width;
    protected int height;

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

/*
    public Rect getRectangle()
    {
        return new Rect(x,y,x+width,y+height);
    }
*/
}
