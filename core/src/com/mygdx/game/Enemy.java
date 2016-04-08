package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Enemy extends Actor 
{
    Texture texture;
    int posX;
    int posY;
    int rotation;
    int range;
    int startTime;

    public void draw(Batch batch, float alpha)
    {
        //batch.draw(texture,posX,posY);
        batch.draw(texture, this.getX(), this.getY(), texture.getWidth()/2, texture.getHeight()/2, texture.getWidth(), texture.getHeight(), 1f, 1f, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

    }
    
    public void setRange(int range)
    {
    	this.range = range;
    }
    
    public void setStartTime(int startTime)
    {
    	this.startTime = startTime;
    }
    
    public int getRange()
    {
    	return range;
    }
    
    public void setTexture(Texture texture)
    {
    	this.texture = texture;
    }
    
    public void setRotation(int rotation)
    {
    	this.rotation = rotation;
    }
}