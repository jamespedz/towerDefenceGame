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
	BackgroundTile[][] grid;
	int gridXSize = 60;
	int gridYSize = 30;
	
	@Override
	public void create () 
	{
		shapeRenderer = new ShapeRenderer();
		
		stage = new Stage();
		stage2 = new Stage();
		
		camera = new OrthographicCamera(64*30, 64*60);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	
		createMap();
		createTowers();
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
	
	public void createMap()
	{
		grid = new BackgroundTile[gridXSize][gridYSize]; //Grid for background

		for(int i = 0; i < gridXSize; i++)
		{
			for(int j = 0; j < gridYSize; j++)
			{
				BackgroundTile actor = new BackgroundTile();
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
	}
	
	public void createTowers()
	{
		tower = new Tower();
		tower.setX(((60*64)/2)-64);
		tower.setY(((30*64)/2)-64);
		tower.setWidth(128);
		tower.setHeight(128);
		tower.setRange(350);
		tower.setTarget(Target.FARTHEST);
		tower.setTexture(new Texture("Kills_skull_3_128x128.png"));
		towers.add(tower);
		
		tower2 = new Tower();
		tower2.setX(((40*64)/2)-64);
		tower2.setY(((30*64)/2)-64);
		tower2.setWidth(128);
		tower2.setHeight(128);
		tower2.setRange(500);
		tower2.setTarget(Target.CLOSEST);
		tower2.setTexture(new Texture("Kills_skull_3_128x128.png"));
		towers.add(tower2);

		
		for (Tower currentTower : towers)
		{
			stage2.addActor(currentTower); 
		}	
	}
	
	public void createEnemies()
	{
		
		
		int startDelay = 250;
		Vector2 startPosition = new Vector2(((20*64)/2)-64, ((40*64)/2)-64);
		
		for(int i = 0; i < 50; i++)
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
			if(tempDistance < distance&&tempDistance<tower.getRange())
			{
				closestEnemy = enemy;
				distance = tempDistance;
			}
		}
		return closestEnemy;
	}
	
	public Enemy farthestEnemyInRange(Tower tower)
	{
		Float distance = 0f;
		Enemy farthestEnemy = null;
		System.out.println("-------------------");
		for (Enemy enemy : enemies)
		{
			float tempDistance = distanceBetweenPoints(tower.getX(), tower.getY(), enemy.getX(), enemy.getY());
			if(tempDistance > distance&&tempDistance < tower.getRange())
			{
				System.out.println(tempDistance + " is less than " + tower.getRange());
				farthestEnemy = enemy;
				distance = tempDistance;
			}
		}
		
		if(farthestEnemy!=null)
		{
			System.out.println("This: " + distance + " should match");
			System.out.println("This: " + distanceBetweenPoints(tower.getX(), tower.getY(), farthestEnemy.getX(), farthestEnemy.getY()));
		}
		return farthestEnemy;
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
			Enemy currentEnemy = null;
			
			if(currentTower.getTarget().equals(Target.CLOSEST))
			{
				currentEnemy = closestEnemy(currentTower);
			}
			else if(currentTower.getTarget().equals(Target.FARTHEST))
			{
				currentEnemy = farthestEnemyInRange(currentTower);
			}
			
			if(currentEnemy!=null)
			{
				double enemyX = currentEnemy.getX();
				double enemyY = currentEnemy.getY();
				float towerX = currentTower.getX()+64;
				float towerY = currentTower.getY()+64;  
				
				float dist = (float) Math.sqrt(Math.pow(enemyX - towerX, 2) + Math.pow(enemyY - towerY, 2) );
				
				
				if(currentEnemy!=null)
				{
					double theta = 180.0 / Math.PI * Math.atan2(towerX - enemyX, enemyY - towerY);
					currentTower.setRotation((int)theta);
				}
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
