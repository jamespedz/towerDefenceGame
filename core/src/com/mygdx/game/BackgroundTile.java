package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundTile extends Actor 
{
    Texture texture;
    int posX;
    int posY;
    int rotation;

    public void draw(Batch batch, float alpha)
    {
    	batch.setColor(1,1,1,1);
        //batch.draw(texture,posX,posY);
    	batch.draw(texture, this.getX(), this.getY(), texture.getWidth()/2, texture.getHeight()/2, texture.getWidth(), texture.getHeight(), 1f, 1f, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

    }
    
    public void setTexture(Texture texture)
    {
    	this.texture = texture;
    }
    
    public void setX(int posX)
    {
    	this.posX = posX;
    }
    
    public void setY(int posY)
    {
    	this.posY = posY;
    }
    
    public void setRotation(int rotation)
    {
    	this.rotation = rotation;
    }
}