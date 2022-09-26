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
    private final HashMap<Actions, MyAtlasAnimation> manAssetss;
    private final Music music;
    private Sound sound;
    private MyInputProcessor myInputProcessor;
    private float x,y;
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
        manAssetss.put(Actions.RUN, new MyAtlasAnimation("atlas/men.atlas","run",5, false, "beg-po-trotuiarty.mp3"));
        manAssetss.put(Actions.JUMP, new MyAtlasAnimation("atlas/men.atlas","jamp",3, false, "beg-po-trotuiarty.mp3"));
        manAssetss.put(Actions.UP, new MyAtlasAnimation("atlas/men.atlas","up",1, false, "beg-po-trotuiarty.mp3"));
        manAssetss.put(Actions.SIT, new MyAtlasAnimation("atlas/men.atlas","sit",3, false, "beg-po-trotuiarty.mp3"));
        actions = Actions.STAND;

        music = Gdx.audio.newMusic(Gdx.files.internal("George Thorogood - Bad to the Bone_(newmp3.org).mp3"));
        music.setPan(0,0.025f);
        music.setLooping(true);
        music.play();

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
        }

        manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
        if (!manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x < -0.6f) {manAssetss.get(actions).draw().flip(true, false);}
        if (manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x > 0.6f) {manAssetss.get(actions).draw().flip(true, false);}

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
