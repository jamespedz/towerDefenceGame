package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img;
	Stage stage;//, stage2, stage3;
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
    BackgroundTile highlighted;//, leftTowerPanel;
    List<BackgroundTile> availableTowers = new ArrayList<BackgroundTile>();
    //Boolean leftTowerPanelState, rightTowerPanelState;
    float uiMovementSpeed;
    InputMultiplexer im;
    Boolean tempScroll = false;
    ScrollPane testScroll;
    
    Group backgroundGroup, enemiesGroup, towersGroup, UIGroup;
    
    //Vector2 rightPanelHiddenPosition, leftPanelHiddenPosition, rightPanelShownPosition, leftPanelShownPosition;
	
	@Override
	public void create () 
	{
		backgroundGroup = new Group();
		enemiesGroup = new Group();
		towersGroup = new Group();
		UIGroup = new Group();
		
		uiMovementSpeed = 0.1f;
		shapeRenderer = new ShapeRenderer();
		
		stage = new Stage();
		
		camera = new OrthographicCamera(64*gridYSize+1, 64*gridXSize);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	
		createMap();
		createEnemies();
		createTowers();
		createUI();
		addLayers();
		
		//Gdx.input.setInputProcessor(this);
		//Gdx.input.setInputProcessor(stage3);
		
		im = new InputMultiplexer();
		im.addProcessor(this);
		im.addProcessor(stage);
		
		Gdx.input.setInputProcessor(im);
		
		startTime = System.currentTimeMillis();
		
	}
	
	public void addLayers()
	{
		stage.addActor(backgroundGroup);
		stage.addActor(enemiesGroup);
		stage.addActor(towersGroup);
		stage.addActor(UIGroup);
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
				actor.setPosition(i*64, j*64);
				grid[i][j] = actor;
			}
		}
		
		for(int k = 0; k < gridXSize; k++)
		{
			for(int l = 0; l < gridYSize; l++)
			{
				backgroundGroup.addActor((grid[k][l]));
			}
		}
	}
	
	public void createUI()
	{
		Table availableTowerScrollPaneTable = new Table();
	
		highlighted = new BackgroundTile();
		highlighted.setPosition(0-64, 0-64);
		highlighted.setTexture(new Texture("Tiles/tile_70.png"));
		highlighted.setWidth(64);
		highlighted.setHeight(64);

		UIGroup.addActor(highlighted);
		
		//leftTowerPanel = new BackgroundTile();
		//leftTowerPanel.setPosition(0*64, 15*64);
		//leftTowerPanel.setTexture(new Texture("towerPanel.png"));
		//leftTowerPanel.setWidth(5*64);
		//leftTowerPanel.setHeight(14*64);

		//stage3.addActor(leftTowerPanel);
		
		for(int i = 0; i < 12; i++)
		{
			BackgroundTile availableTower = new BackgroundTile();
			//availableTower1.setPosition((1*64)-32, ((13+13)*64)+32);
			availableTower.setTexture(new Texture("Kills_skull_3_128x128.png"));
			availableTower.setWidth(1*128);
			availableTower.setHeight(1*128);
			
			availableTowers.add(availableTower);
		}
		
		//System.out.println(availableTowers.size());
		
		for(int j = 0; j < availableTowers.size(); j++)
		{
			if(j % 2 == 0 && j > 1)
			{
				availableTowerScrollPaneTable.row();
			}
			//availableTowerScrollPaneTable.row();
			availableTowerScrollPaneTable.add(availableTowers.get(j)).expandX().padTop(48);
		}
		
		availableTowerScrollPaneTable.setDebug(true);
		
		availableTowerScrollPaneTable.top();
		//availableTowerScrollPaneTable.setPosition(0, 0);
		availableTowerScrollPaneTable.setWidth(3*64);
		availableTowerScrollPaneTable.setHeight(16*64);
		
		testScroll = new ScrollPane(availableTowerScrollPaneTable);
		testScroll.setPosition(0, 0*64);
		testScroll.setWidth(5*64);
		testScroll.setHeight(14*64);
		testScroll.setScrollingDisabled(true, false);
		testScroll.setSmoothScrolling(false);
		
		UIGroup.addActor(testScroll);

		topBar = new BackgroundTile[gridXSize];
		for(int i = 0; i < gridXSize; i++)
		{
			BackgroundTile actor = new BackgroundTile();
			
			actor.setTexture(new Texture("Tiles/tile_203.png"));

			actor.setPosition(i*64, 14*64);
			
			topBar[i] = actor;
		}
		
		for(int k = 0; k < gridXSize; k++)
		{
			
			UIGroup.addActor(topBar[k]);

		}	
	}
	
	public void createTowers()
	{
		/*
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
		*/	
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
		System.out.println("This is the render method");
		
		update();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.getViewport().setCamera(camera);
        stage.act(Gdx.graphics.getDeltaTime());
        
        //draw range circle round towers
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 0.5f);
        for (Tower currentTower : towers)
		{
        	shapeRenderer.circle(currentTower.getX()+(currentTower.texture.getWidth()/2), currentTower.getY()+(currentTower.texture.getHeight()/2), currentTower.getRange());
		}
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        stage.draw();
        camera.update();
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
		System.out.println("This is the update method");
		camera.viewportHeight = (64*15);
		//System.out.println((float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight());
		camera.viewportWidth = ((float)(64*15)*((float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight()));
		camera.position.set(new Vector3((float)(gridXSize*64)/2,((float)(gridYSize+1)*64)/2,0));
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
				enemiesGroup.addActor(currentEnemy);
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
		if(button == Buttons.LEFT)
		{
			if(tempScroll==true)
			{
				System.out.println("Left mouse release - Mouse scrolled");
				tempScroll=false;
			}
			
			else
			{
				System.out.println("Left mouse released - Mouse NOT scrolled");
			}
        }
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) 
	{
		tempScroll=true;
		System.out.println("Scrolled");
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
