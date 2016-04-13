package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AvailableTower extends Actor 
{
    Texture texture;
    int rotation;
    Tower tower;

    public void draw(Batch batch, float alpha)
    {
    	batch.setColor(1,1,1,1);
        //batch.draw(texture,posX,posY);
    	batch.draw(texture, this.getX(), this.getY(), texture.getWidth()/2, texture.getHeight()/2, texture.getWidth(), texture.getHeight(), 1f, 1f, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

    }
    
    public void setTower(Tower tower)
    {
    	this.tower = tower;
    }
    
    public Tower getTower()
    {
    	return tower;
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