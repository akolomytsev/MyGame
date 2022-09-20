package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private MyAtlasAnimation run, stand, jump, up, sit, tmpA;
	private Music music;
	private Sound sound;
	private MyInputProcessor myInputProcessor;
	private float x,y;
	private int dir = 0, step = 1;
	private OrthographicCamera camera;
	private Rectangle rectangle, window;
	private GamePhysics gamePhysics;
	private Body body;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	@Override
	public void create () {

		map = new TmxMapLoader().load("map/MyMap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		gamePhysics = new GamePhysics();

		BodyDef def = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();


		//def.position.set(0, 0); //позиция тела, можно выбирать



		shape.setAsBox(250, 5);
		fdef.shape = shape;
		fdef.density = 1;// масса тела
		fdef.friction = 0;// тело будет скользить
		fdef.restitution = 1; // тело будет работать как батут

		MapLayer env = map.getLayers().get("evn");
		Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width/2;
			float h = rect.get(i).getRectangle().height/2;
			def.position.set(x,y);
			shape.setAsBox(w,h);
			gamePhysics.world.createBody(def).createFixture(fdef).setUserData("Kubiki");
		}

		def.gravityScale = 100; // гравитация равна 1
		def.type = BodyDef.BodyType.StaticBody; // статическое тело через которое нельзя пройти и нельзя сдвинуть
		env = map.getLayers().get("dinam");
		rect = env.getObjects().getByType(RectangleMapObject.class);
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width/2;
			float h = rect.get(i).getRectangle().height/2;
			def.position.set(x,y);
			shape.setAsBox(w,h);
			gamePhysics.world.createBody(def).createFixture(fdef).setUserData("Kubiki");
		}

		env = map.getLayers().get("Гера");
		RectangleMapObject hero = (RectangleMapObject)env.getObjects().get("Гера");
		float x = hero.getRectangle().x;
		float y = hero.getRectangle().y;
		float w = hero.getRectangle().width/2;
		float h = hero.getRectangle().height/2;
		def.position.set(x,y);
		shape.setAsBox(w,h);

		gamePhysics.world.createBody(def).createFixture(fdef).setUserData("Palka");

//		def.type = BodyDef.BodyType.DynamicBody;
//		for (int i = 0; i < 100; i++) {
//			def.position.set(MathUtils.random(-120, 120), MathUtils.random(120, 320));
//			shape.setAsBox(5, 5);
//			fdef.shape = shape;
//			fdef.density = 1;
//			fdef.friction = 0;
//			fdef.restitution = 1;
//			gamePhysics.world.createBody(def).createFixture(fdef).setUserData("Kubiki");
//
//		}

		def.position.set(MathUtils.random(-120, 120), MathUtils.random(120, 120));
		shape.setAsBox(10, 10);
		fdef.shape = shape;
		fdef.density = 1;
		fdef.friction = 0;
		fdef.restitution = 1;
		body = gamePhysics.world.createBody(def);
		body.createFixture(fdef).setUserData("telo");

		

		shape.dispose();

		myInputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(myInputProcessor);
		batch = new SpriteBatch();
		stand =new MyAtlasAnimation("atlas/men.atlas","stand",2, false, "beg-po-trotuiarty.mp3");
		run =new MyAtlasAnimation("atlas/men.atlas","run",20, false, "beg-po-trotuiarty.mp3");
		jump =new MyAtlasAnimation("atlas/men.atlas","jump",3, false, "beg-po-trotuiarty.mp3");
		up =new MyAtlasAnimation("atlas/men.atlas","up",1, false,"beg-po-trotuiarty.mp3");
		sit =new MyAtlasAnimation("atlas/men.atlas","sit",1, false, "beg-po-trotuiarty.mp3");
		tmpA = stand;

		music = Gdx.audio.newMusic(Gdx.files.internal("George Thorogood - Bad to the Bone_(newmp3.org).mp3"));

		music.setPan(0,0.025f);
		music.setLooping(true);
		music.play();

		//sound = Gdx.audio.newSound(Gdx.files.internal("beg-po-trotuiarty.mp3"));
		//sound.play(); // надо выводить на клик

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void resize(int width, int height) {
	camera.viewportHeight = height;
	camera.viewportWidth = width;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.position.x = body.getPosition().x;
		camera.position.y = body.getPosition().y;
		camera.zoom = 1f;
		camera.update();

		mapRenderer.setView(camera);
		mapRenderer.render();

		tmpA = stand;
		dir = 0;

		if (myInputProcessor.getOutString().contains("A")){
			dir = -1;
			tmpA = run;
		}
		if (myInputProcessor.getOutString().contains("D")){
			dir = 1;
			tmpA = run;
		}
		if (myInputProcessor.getOutString().contains("W")){
			tmpA = up;
		}
		if (myInputProcessor.getOutString().contains("S")){
			tmpA = sit;
		}
		if (myInputProcessor.getOutString().contains("Space")) {
			body.applyForceToCenter(new Vector2(0, 1000f), true);
		}

		if (dir == -1) x -= step;
		if (dir == 1) x += step;

		tmpA.setTime(Gdx.graphics.getDeltaTime());

		TextureRegion tmp = tmpA.draw();
		if (!tmpA.draw().isFlipX() & dir == -1){ tmpA.draw().flip(true,false);}
		if (tmpA.draw().isFlipX() & dir == 1) {tmpA.draw().flip(true,false);}



//		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//			sound.stop();
//			sound.play(1,1,0);
//		}


		float x = body.getPosition().x -10f/camera.zoom;
		float y = body.getPosition().y -10f/camera.zoom;


		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(tmpA.draw(), x, y, 20/camera.zoom, 20/camera.zoom);
		batch.end();

		gamePhysics.step();

		gamePhysics.debugDraw(camera);


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		tmpA.dispose();
		music.dispose();
		sound.dispose();
		stand.dispose();
		jump.dispose();
		run.dispose();
		mapRenderer.dispose();
		map.dispose();
	}
}
