package com.mygdx.game;

import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Stage stage, stage2;
	private OrthographicCamera camera;
	Tower tower, tower2, tower3;
	ShapeRenderer shapeRenderer;
	Vector<Tower> towers = new Vector<Tower>();
	Vector<Enemy> enemies = new Vector<Enemy>();
	Enemy enemy1, enemy2;
	long startTime;
	
	@Override
	public void create () 
	{
		shapeRenderer = new ShapeRenderer();
		
		stage = new Stage();
		stage2 = new Stage();
		
		camera = new OrthographicCamera(64*30, 64*60);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		
		int gridXSize = 60;
		int gridYSize = 30;
		MyActor[][] grid = new MyActor[gridXSize][gridYSize]; //Grid for background
		
		
		
		for(int i = 0; i < gridXSize; i++)
		{
			for(int j = 0; j < gridYSize; j++)
			{
				MyActor actor = new MyActor();
				if(i>=10&&i<=50)
				{
					actor.setTexture(new Texture("Kills_skull_2_64x64.png"));
				}
				else
				{
					actor.setTexture(new Texture("Kills_skull_64x64.png"));
				}
				actor.setX(i*64);
				actor.setY(j*64);
				grid[i][j] = actor;
			}
		}
		
		for(int k = 0; k < gridXSize; k++)
		{
			for(int l = 0; l < gridYSize; l++)
			{
				stage.addActor(grid[k][l]);
			}
		}
		
		tower = new Tower();
		tower.setX(((60*64)/2)-64);
		tower.setY(((30*64)/2)-64);
		tower.setWidth(128);
		tower.setHeight(128);
		tower.setRange(350);
		tower.setTexture(new Texture("Kills_skull_3_128x128.png"));
		towers.add(tower);
		
		tower2 = new Tower();
		tower2.setX(((20*64))-64);
		tower2.setY(((30*64)/2)-64);
		tower2.setWidth(128);
		tower2.setHeight(128);
		tower2.setRange(600);
		tower2.setTexture(new Texture("Kills_skull_3_128x128.png"));
		towers.add(tower2);
		
		tower3 = new Tower();
		tower3.setX(((35*64))-64);
		tower3.setY(((10*64)/2)-64);
		tower3.setWidth(128);
		tower3.setHeight(128);
		tower3.setRange(400);
		tower3.setTexture(new Texture("Kills_skull_3_128x128.png"));
		towers.add(tower3);
		
		for (Tower currentTower : towers)
		{
			stage2.addActor(currentTower); 
		}	
		
		createEnemies();
		
		startTime = System.currentTimeMillis();
		
	}
	
	public SequenceAction createPath()
	{
		SequenceAction path = new SequenceAction();
		
		MoveToAction moveAction1 = new MoveToAction();
		moveAction1.setPosition(((33*64))-64, ((20*64))-64);
		moveAction1.setDuration(10);
        
        MoveToAction moveAction2 = new MoveToAction();
        moveAction2.setPosition(((33*64))-64, ((0*64))-64);
        moveAction2.setDuration(10);
        
        path.addAction(moveAction1);
        path.addAction(moveAction2);
        
        return path;
	}
	
	
	public void createEnemies()
	{
		
		
		int startDelay = 500;
		Vector2 startPosition = new Vector2(((20*64)/2)-64, ((40*64)/2)-64);
		
		for(int i = 0; i < 10; i++)
		{
			SequenceAction pathToFollow = createPath();
			Enemy enemyToAdd = new Enemy();
			enemyToAdd.setPosition(startPosition.x, startPosition.y);
			enemyToAdd.setWidth(128);
			enemyToAdd.setHeight(128);
			enemyToAdd.setTexture(new Texture("Kills_skull_4_128x128.png"));
			enemyToAdd.setStartTime(i*startDelay);
			enemyToAdd.addAction(pathToFollow);
			
			enemies.add(enemyToAdd);
		}
	}
	
	public class MyActor extends Actor 
	{
        Texture texture;
        int posX;
        int posY;
        int rotation;

        public void draw(Batch batch, float alpha)
        {
            //batch.draw(texture,posX,posY);
            batch.draw(texture, posX, posY, texture.getWidth()/2, texture.getHeight()/2, texture.getWidth(), texture.getHeight(), 1f, 1f, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

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
	
	public float distanceBetweenPoints(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) );
	}
	
	public Enemy closestEnemy(Tower tower)
	{
		float distance = 10000;
		Enemy closestEnemy = null;
		
		for (Enemy enemy : enemies)
		{
			float tempDistance = distanceBetweenPoints(tower.getX(), tower.getY(), enemy.getX(), enemy.getY());
			if(tempDistance < distance)
			{
				closestEnemy = enemy;
				distance = tempDistance;
			}
		}
		return closestEnemy;
	}

	@Override
	public void render() 
	{
		update();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.getViewport().setCamera(camera);
		
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        camera.update();
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 0.5f);
        //camera.unproject(new vector3(500, 500, 0)
        for (Tower currentTower : towers)
		{
        	shapeRenderer.circle(currentTower.getX()+(currentTower.texture.getWidth()/2), currentTower.getY()+(currentTower.texture.getHeight()/2), currentTower.getRange());
		}
        //shapeRenderer.circle(tower.posX+(tower.texture.getWidth()/2), tower.posY+(tower.texture.getHeight()/2), tower.getRange());
        //shapeRenderer.circle(tower2.posX+(tower2.texture.getWidth()/2), tower2.posY+(tower2.texture.getHeight()/2), tower2.getRange());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
		
		stage2.getViewport().setCamera(camera);
        stage2.act(Gdx.graphics.getDeltaTime());
        System.out.println(Gdx.graphics.getDeltaTime());
        stage2.draw();
	}
	
	public void update()
	{
		camera.viewportHeight = (64*30);
		camera.viewportWidth = ((64*30)*Gdx.graphics.getWidth()/Gdx.graphics.getHeight());
		camera.position.set(new Vector3((60*64)/2,(64*30)/2,0));
		camera.update();
		
		for (Tower currentTower : towers)
		{
			Enemy closestEnemy = closestEnemy(currentTower);

			double enemyX = closestEnemy.getX();
			double enemyY = closestEnemy.getY();
			float towerX = currentTower.getX()+64;
			float towerY = currentTower.getY()+64;  
			
			float dist = (float) Math.sqrt(Math.pow(enemyX - towerX, 2) + Math.pow(enemyY - towerY, 2) );
			
			if(dist < currentTower.getRange())
			{
				double theta = 180.0 / Math.PI * Math.atan2(towerX - enemyX, enemyY - towerY);
				currentTower.setRotation((int)theta);
			}
		}
		
		long currentTime = System.currentTimeMillis();
		for (Enemy currentEnemy : enemies)
		{
			if(currentTime - startTime > currentEnemy.startTime)
			{
				stage2.addActor(currentEnemy);
			}
		}
	}
}
