package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Tower extends Actor 
{
    Texture texture;
    int rotation;
    int range;
    Target target;
    
    public Tower(float x, float y, int range, Target target, Texture texture)
    {
    	this.setPosition(x, y);
    	this.range = range;
    	this.target = target;
    	this.texture = texture;
    	
    }
    
    public Tower(int range, int height, int width, Texture texture)
    {
    	this.range = range;
    	this.texture = texture;
    	this.setHeight(height);
    	this.setWidth(width);
    }

    public void draw(Batch batch, float alpha)
    {
    	batch.setColor(1,1,1,1);
        //batch.draw(texture,posX,posY);
        batch.draw(texture, this.getX(), this.getY(), texture.getWidth()/2, texture.getHeight()/2, texture.getWidth(), texture.getHeight(), 1f, 1f, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

    }
    
    public void setTarget(Target target)
    {
    	this.target = target;
    }
    
    public Target getTarget()
    {
    	return target;
    }
    
    public void setRange(int range)
    {
    	this.range = range;
    }
    
    public int getRange()
    {
    	return range;
    }
    
    public void setTexture(Texture texture)
    {
    	this.texture = texture;
    }
    
    public Texture getTexture()
    {
    	return texture;
    }
    
    public void setRotation(int rotation)
    {
    	this.rotation = rotation;
    }
}