package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GamePhysics;
import com.mygdx.game.MyAtlasAnimation;
import com.mygdx.game.MyInputProcessor;
import com.mygdx.game.enums.Actions;
import java.util.HashMap;

public class GameScreen implements Screen {
    Game game;

    private final SpriteBatch batch;
    //private MyAtlasAnimation run, stand, jump, up, sit, tmpA;
    private final HashMap<Actions, MyAtlasAnimation> manAssetss;
    private final Music music;
    private Sound sound;
    private MyInputProcessor myInputProcessor;
    private float x,y;
    //private int dir = 0, step = 1;
    private OrthographicCamera camera;
    private GamePhysics gamePhysics;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Actions actions;
    private int[] front, tL;

    public GameScreen(Game game) {
        this.game = game;

        map = new TmxMapLoader().load("map/MyMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        front = new int[1];
        front[0] = map.getLayers().getIndex("Слой тайлов 2");
        tL = new int[1];
        tL[0] = map.getLayers().getIndex("Слой тайлов 1");

        gamePhysics = new GamePhysics();

//        BodyDef def = new BodyDef();
//        FixtureDef fdef = new FixtureDef();
//        PolygonShape shape = new PolygonShape();


        //def.position.set(0, 0); //позиция тела, можно выбирать



//        shape.setAsBox(250, 5);
//        fdef.shape = shape;
//        fdef.density = 1;// масса тела
//        fdef.friction = 0;// тело будет скользить
//        fdef.restitution = 1; // тело будет работать как батут



//        MapLayer env = map.getLayers().get("evn");
//        Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
//        for (int i = 0; i < rect.size; i++) {
//            float x = rect.get(i).getRectangle().x;
//            float y = rect.get(i).getRectangle().y;
//            float w = rect.get(i).getRectangle().width/2;
//            float h = rect.get(i).getRectangle().height/2;
//            def.position.set(x,y);
//            shape.setAsBox(w,h);
//            gamePhysics.world.createBody(def).createFixture(fdef).setUserData("Kubiki");
//        }

//        def.gravityScale = 100; // гравитация равна 1
//        def.type = BodyDef.BodyType.StaticBody; // статическое тело через которое нельзя пройти и нельзя сдвинуть
//        env = map.getLayers().get("dinam");
//        rect = env.getObjects().getByType(RectangleMapObject.class);
//        for (int i = 0; i < rect.size; i++) {
//            float x = rect.get(i).getRectangle().x;
//            float y = rect.get(i).getRectangle().y;
//            float w = rect.get(i).getRectangle().width/2;
//            float h = rect.get(i).getRectangle().height/2;
//            def.position.set(x,y);
//            shape.setAsBox(w,h);
//            gamePhysics.world.createBody(def).createFixture(fdef).setUserData("Kubiki");
//        }
//
//        env = map.getLayers().get("Гера");
//        RectangleMapObject hero = (RectangleMapObject)env.getObjects().get("Гера");
//        float x = hero.getRectangle().x;
//        float y = hero.getRectangle().y;
//        float w = hero.getRectangle().width/2;
//        float h = hero.getRectangle().height/2;
//        def.position.set(x,y);
//        shape.setAsBox(w,h);

//        gamePhysics.world.createBody(def).createFixture(fdef).setUserData("Palka");

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

//        def.position.set(MathUtils.random(-120, 120), MathUtils.random(120, 120));
//        shape.setAsBox(10, 10);
//        fdef.shape = shape;
//        fdef.density = 1;
//        fdef.friction = 0;
//        fdef.restitution = 1;
//        body = gamePhysics.world.createBody(def);
//        body.createFixture(fdef).setUserData("telo");
//
//
//
//        shape.dispose();

        Array<RectangleMapObject> objects = map.getLayers().get("evn").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("dinam").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            gamePhysics.addObject(objects.get(i));
        }
        body = gamePhysics.addObject((RectangleMapObject) map.getLayers().get("Гера").getObjects().get("Гера"));
        body.setFixedRotation(true);

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        batch = new SpriteBatch();

        manAssetss = new HashMap<>();
        manAssetss.put(Actions.STAND, new MyAtlasAnimation("atlas/men.atlas","stand",2, false, "beg-po-trotuiarty.mp3"));
        //stand =new MyAtlasAnimation("atlas/men.atlas","stand",2, false, "beg-po-trotuiarty.mp3");
        manAssetss.put(Actions.RUN, new MyAtlasAnimation("atlas/men.atlas","run",5, false, "beg-po-trotuiarty.mp3"));
        //run =new MyAtlasAnimation("atlas/men.atlas","run",20, false, "beg-po-trotuiarty.mp3");
        manAssetss.put(Actions.JUMP, new MyAtlasAnimation("atlas/men.atlas","jamp",3, false, "beg-po-trotuiarty.mp3"));
        //jump =new MyAtlasAnimation("atlas/men.atlas","jump",3, false, "beg-po-trotuiarty.mp3");
        manAssetss.put(Actions.UP, new MyAtlasAnimation("atlas/men.atlas","up",1, false, "beg-po-trotuiarty.mp3"));
//        up =new MyAtlasAnimation("atlas/men.atlas","up",1, false,"beg-po-trotuiarty.mp3");
        manAssetss.put(Actions.SIT, new MyAtlasAnimation("atlas/men.atlas","sit",3, false, "beg-po-trotuiarty.mp3"));
        //sit =new MyAtlasAnimation("atlas/men.atlas","sit",1, false, "beg-po-trotuiarty.mp3");
        actions = Actions.STAND;

        music = Gdx.audio.newMusic(Gdx.files.internal("George Thorogood - Bad to the Bone_(newmp3.org).mp3"));
        music.setPan(0,0.025f);
        music.setLooping(true);
        music.play();

        //sound = Gdx.audio.newSound(Gdx.files.internal("beg-po-trotuiarty.mp3"));
        //sound.play(); // надо выводить на клик

        //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.position.x = body.getPosition().x * gamePhysics.PPM;
        camera.position.y = body.getPosition().y * gamePhysics.PPM;
        camera.zoom = 0.7f;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(tL);

//        tmpA = stand;
//        dir = 0;
//
//        if (myInputProcessor.getOutString().contains("A")){
//            dir = -1;
//            tmpA = run;
//        }
//        if (myInputProcessor.getOutString().contains("D")){
//            dir = 1;
//            tmpA = run;
//        }
//        if (myInputProcessor.getOutString().contains("W")){
//            tmpA = up;
//        }
//        if (myInputProcessor.getOutString().contains("S")){
//            tmpA = sit;
//        }
//        if (myInputProcessor.getOutString().contains("Space")) {
//            body.applyForceToCenter(new Vector2(0, 1000f), true);
//        }

//        if (dir == -1) x -= step;
//        if (dir == 1) x += step;

        manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
        body.applyForceToCenter(myInputProcessor.getVector(), true);

        if (body.getLinearVelocity().len() < 0.1f){
            actions = Actions.STAND;
        }
        if (Math.abs(body.getLinearVelocity().x) > 0.1f) {
            actions = Actions.RUN;
        }
        if (Math.abs(body.getLinearVelocity().y) > 0.1f) {
            actions = Actions.JUMP;
        }
        if (myInputProcessor.isSitting()) {
            actions = Actions.SIT;
        }
        if (myInputProcessor.isUp()){
            actions = Actions.UP;
//        }else if (body.getLinearVelocity().y == 0f){
//            actions = Actions.UP;
        }


        manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
        if (!manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x < -0.6f) {manAssetss.get(actions).draw().flip(true, false);}
        if (manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x > 0.6f) {manAssetss.get(actions).draw().flip(true, false);}



//        TextureRegion tmp = tmpA.draw();
//        if (!tmpA.draw().isFlipX() & dir == -1){ tmpA.draw().flip(true,false);}
//        if (tmpA.draw().isFlipX() & dir == 1) {tmpA.draw().flip(true,false);}



//		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//			sound.stop();
//			sound.play(1,1,0);
//		}


        float x = body.getPosition().x * gamePhysics.PPM -2.5f/camera.zoom;
        float y = body.getPosition().y * gamePhysics.PPM -2.5f/camera.zoom;


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(manAssetss.get(actions).draw(), x, y,25/camera.zoom, 20/camera.zoom);
        batch.end();

        mapRenderer.render(front);

        Gdx.graphics.setTitle(String.valueOf(body.getLinearVelocity()));
        gamePhysics.step();

        gamePhysics.debugDraw(camera);


    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        music.dispose();
        sound.dispose();
        mapRenderer.dispose();
        map.dispose();
    }
}
