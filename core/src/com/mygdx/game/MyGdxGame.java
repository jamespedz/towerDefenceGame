package com.mygdx.game;

import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img;
	Stage stage, stage2, stage3;
	private OrthographicCamera camera;
	Tower tower, tower2, tower3;
	ShapeRenderer shapeRenderer;
	Vector<Tower> towers = new Vector<Tower>();
	Vector<Enemy> enemies = new Vector<Enemy>();
	Enemy enemy1, enemy2;
	long startTime;
	BackgroundTile[][] grid;
	BackgroundTile[] topBar;
	int gridXSize = 20;
	int gridYSize = 14;
	TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    Tower highlighted, rightTowerPanel, leftTowerPanel;
    Boolean leftTowerPanelState, rightTowerPanelState;
    float uiMovementSpeed;
	
	@Override
	public void create () 
	{
		Gdx.input.setInputProcessor(this);
		uiMovementSpeed = 0.1f;
		shapeRenderer = new ShapeRenderer();
		
		stage = new Stage();
		stage2 = new Stage();
		stage3 = new Stage();
		
		camera = new OrthographicCamera(64*gridYSize+1, 64*gridXSize);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	
		createMap();
		createUI();
		createTowers();
		createEnemies();
		
		startTime = System.currentTimeMillis();
		
	}
	
	public SequenceAction createPath()
	{
		SequenceAction path = new SequenceAction();
		
		MoveToAction moveAction1 = new MoveToAction();
		moveAction1.setPosition(((10*64)), ((11*64)));
		moveAction1.setDuration(2);
		
		MoveToAction moveAction2 = new MoveToAction();
		moveAction2.setPosition(((10*64)), ((6*64)));
		moveAction2.setDuration(2);
		
		MoveToAction moveAction3 = new MoveToAction();
		moveAction3.setPosition(((8*64)), ((6*64)));
		moveAction3.setDuration(2);
		
		MoveToAction moveAction4 = new MoveToAction();
		moveAction4.setPosition(((8*64)), ((2*64)));
		moveAction4.setDuration(2);
        
        
        path.addAction(moveAction1);
        path.addAction(moveAction2);
        path.addAction(moveAction3);
        
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
				
				actor.setTexture(new Texture("Tiles/tile_01.png"));

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
	
	public void createUI()
	{
		leftTowerPanelState = false;
		rightTowerPanelState = false;
		
		highlighted = new Tower();
		highlighted.setX(0);
		highlighted.setY(0);
		highlighted.setTexture(new Texture("Tiles/tile_70.png"));
		highlighted.setWidth(64);
		highlighted.setHeight(64);

		stage3.addActor(highlighted);
		
		rightTowerPanel = new Tower();
		rightTowerPanel.setX(17*64);
		rightTowerPanel.setY(15*64);
		rightTowerPanel.setTexture(new Texture("towerPanel.png"));
		rightTowerPanel.setWidth(3*64);
		rightTowerPanel.setHeight(14*64);

		stage3.addActor(rightTowerPanel);
		
		leftTowerPanel = new Tower();
		leftTowerPanel.setX(0*64);
		leftTowerPanel.setY(15*64);
		leftTowerPanel.setTexture(new Texture("towerPanel.png"));
		leftTowerPanel.setWidth(3*64);
		leftTowerPanel.setHeight(14*64);

		stage3.addActor(leftTowerPanel);
		
		topBar = new BackgroundTile[gridXSize];
		for(int i = 0; i < gridXSize; i++)
		{
			BackgroundTile actor = new BackgroundTile();
			
			actor.setTexture(new Texture("Tiles/tile_203.png"));

			actor.setX(i*64);
			actor.setY(14*64);
			topBar[i] = actor;
		}
		
		for(int k = 0; k < gridXSize; k++)
		{
			
			stage.addActor(topBar[k]);

		}
		
		
		
	}
	
	public void createTowers()
	{
		tower = new Tower();
		tower.setX(3*64);
		tower.setY(3*64);
		tower.setWidth(64);
		tower.setHeight(64);
		tower.setRange(100);
		tower.setTarget(Target.FARTHEST);
		tower.setTexture(new Texture("Kills_skull_3_64x64.png"));
		towers.add(tower);
		
		tower2 = new Tower();
		tower2.setX(8*64);
		tower2.setY(3*64);
		tower2.setWidth(64);
		tower2.setHeight(64);
		tower2.setRange(150);
		tower2.setTarget(Target.CLOSEST);
		tower2.setTexture(new Texture("Kills_skull_3_64x64.png"));
		towers.add(tower2);

		
		for (Tower currentTower : towers)
		{
			stage2.addActor(currentTower); 
		}	
	}
	
	public void createEnemies()
	{
		
		
		int startDelay = 250;
		Vector2 startPosition = new Vector2(((0*64)), ((11*64)));
		
		for(int i = 0; i < 50; i++)
		{
			SequenceAction pathToFollow = createPath();
			Enemy enemyToAdd = new Enemy();
			enemyToAdd.setPosition(startPosition.x, startPosition.y);
			enemyToAdd.setWidth(64);
			enemyToAdd.setHeight(64);
			enemyToAdd.setTexture(new Texture("Kills_skull_4_64x64.png"));
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
		//System.out.println("-------------------");
		for (Enemy enemy : enemies)
		{
			float tempDistance = distanceBetweenPoints(tower.getX(), tower.getY(), enemy.getX(), enemy.getY());
			if(tempDistance > distance&&tempDistance < tower.getRange())
			{
				//System.out.println(tempDistance + " is less than " + tower.getRange());
				farthestEnemy = enemy;
				distance = tempDistance;
			}
		}
		
		if(farthestEnemy!=null)
		{
			//System.out.println("This: " + distance + " should match");
			//System.out.println("This: " + distanceBetweenPoints(tower.getX(), tower.getY(), farthestEnemy.getX(), farthestEnemy.getY()));
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
        
        //tiledMapRenderer.setView(camera);
        //tiledMapRenderer.render();
        
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
        
        stage3.getViewport().setCamera(camera);
        stage3.act(Gdx.graphics.getDeltaTime());
        stage3.draw();
	}
	
	public Vector2 tileClickedOn(float x, float y)
	{
		for(int k = 0; k < gridXSize; k++)
		{
			for(int l = 0; l < gridYSize; l++)
			{
				if((k*64)<x&&((k*64)+64)>x&&(l*64)<y&&((l*64)+64)>y)
				{
					return new Vector2(k,l);
				}
			}
		}
		
		return null;
	}
	
	public void update()
	{
		camera.viewportHeight = (64*15);
		//System.out.println((float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight());
		camera.viewportWidth = ((float)(64*15)*((float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight()));
		camera.position.set(new Vector3((float)(gridXSize*64)/2,((float)(gridYSize+1)*64)/2,0));
		camera.update();
		
		if(Gdx.input.isButtonPressed((Input.Buttons.LEFT)))
		{
			//System.out.println("Left tower is at: " + leftTowerPanel.getX()/64 + ", " + leftTowerPanel.getY()/64);
			//System.out.println("Right tower is at: " + rightTowerPanel.getX()/64 + ", " + rightTowerPanel.getY()/64);
			
			if(leftTowerPanel.getX()==0&&leftTowerPanel.getY()==15*64&&rightTowerPanel.getX()==17*64&&rightTowerPanel.getY()==15*64)
			{
				//System.out.println("Both panels are out of view");
				
				Vector3 temp = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		        Vector2 tempPos = tileClickedOn(temp.x, temp.y);
		        //System.out.println(tempPos.x + ", " + tempPos.y);
		        if(tempPos!=null)
		        {//&&tempPos.x>=0&&tempPos.x<20)
			        if(tempPos.x>=0&&tempPos.x<10)
		        	{
			        	highlighted.setPosition(tempPos.x*64, tempPos.y*64);
			        	
			        	MoveToAction moveToAction = new MoveToAction();
						moveToAction.setPosition(17*64,0);
						moveToAction.setDuration(uiMovementSpeed);
						rightTowerPanel.addAction(moveToAction);
			        }
			        
			        else if(tempPos.x>=10&&tempPos.x<20)
			        {
			        	highlighted.setPosition(tempPos.x*64, tempPos.y*64);
			        	
			        	MoveToAction moveToAction = new MoveToAction();
						moveToAction.setPosition(0,0);
						moveToAction.setDuration(uiMovementSpeed);
						leftTowerPanel.addAction(moveToAction);
			        }
		        }				
			}
			
			else if(leftTowerPanel.getX()==0&&leftTowerPanel.getY()==0)
			{
				//System.out.println("Left Panel is down");
				MoveToAction moveToAction = new MoveToAction();
				moveToAction.setPosition(0,15*64);
				moveToAction.setDuration(uiMovementSpeed);
				leftTowerPanel.addAction(moveToAction);
			}
			
			else if(rightTowerPanel.getX()==17*64&&rightTowerPanel.getY()==0)
			{
				//System.out.println("Right tower is down.");
				MoveToAction moveToAction = new MoveToAction();
				moveToAction.setPosition(17*64,15*64);
				moveToAction.setDuration(uiMovementSpeed);
				rightTowerPanel.addAction(moveToAction);
			}
	    }
		
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button == Buttons.LEFT){
           System.out.println("Left mouse released");
        }
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
