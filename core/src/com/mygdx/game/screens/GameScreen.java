package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GamePhysics;
import com.mygdx.game.MyContactListener;
import com.mygdx.game.MyInputProcessor;
import com.mygdx.game.enums.Actions;
import com.mygdx.game.persons.Hero;

import java.util.ArrayList;
import java.util.List;


public class GameScreen implements Screen {
    Game game;

    private final SpriteBatch batch;
    //private final HashMap<Actions, MyAtlasAnimation> manAssetss;
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
    private final Hero hero;
    private final AnimationMan coinAnm;
    public static List<Body> bodyToDelete;

    public GameScreen(Game game) {
        bodyToDelete = new ArrayList<>();
        coinAnm = new AnimationMan("Full Coinss.png", 1, 8, 12f, Animation.PlayMode.LOOP);
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
        body.setFixedRotation(true); // защита от вращения
        hero = new Hero(body);

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        batch = new SpriteBatch();

//        manAssetss = new HashMap<>();
//        manAssetss.put(Actions.STAND, new MyAtlasAnimation("atlas/men.atlas","stand",2, false, "beg-po-trotuiarty.mp3"));
//        manAssetss.put(Actions.RUN, new MyAtlasAnimation("atlas/men.atlas","run",5, false, "beg-po-trotuiarty.mp3"));
//        manAssetss.put(Actions.JUMP, new MyAtlasAnimation("atlas/men.atlas","jamp",3, false, "beg-po-trotuiarty.mp3"));
//        manAssetss.put(Actions.UP, new MyAtlasAnimation("atlas/men.atlas","up",1, false, "beg-po-trotuiarty.mp3"));
//        manAssetss.put(Actions.SIT, new MyAtlasAnimation("atlas/men.atlas","sit",3, false, "beg-po-trotuiarty.mp3"));
//        actions = Actions.STAND;

        music = Gdx.audio.newMusic(Gdx.files.internal("George Thorogood - Bad to the Bone_(newmp3.org).mp3"));
        music.setPan(0,0.025f);
        music.setLooping(true);
        music.play();

        camera = new OrthographicCamera();
        camera.zoom = 0.35f;
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
        mapRenderer.render(tL); // отрисовываем то что будет на слое за персонажем

        //manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
        hero.setTime(delta); //пересчет времени анимации
        Vector2 vector = myInputProcessor.getVector();
        if (MyContactListener.cnt < 1) vector.set(vector.x, 0);
        body.applyForceToCenter(vector, true);
        body.applyForceToCenter(myInputProcessor.getVector(), true);// тело принимает силу в центр из getVector

        hero.setFPS(body.getLinearVelocity(), true);// получает текущую линейную скорость тела
//
//        if (body.getLinearVelocity().len() < 0.1f){
//            actions = Actions.STAND;
//        }
//        if (Math.abs(body.getLinearVelocity().x) > 0.1f) {
//            actions = Actions.RUN;
//        }
//        if (Math.abs(body.getLinearVelocity().y) > 0.1f) {
//            actions = Actions.JUMP;
//        }
//        if (myInputProcessor.isSitting()) {
//            actions = Actions.SIT;
//        }
//        if (myInputProcessor.isUp()){
//            actions = Actions.UP;
//        }
//
//        manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
//        if (!manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x < -0.6f) {manAssetss.get(actions).draw().flip(true, false);}
//        if (manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x > 0.6f) {manAssetss.get(actions).draw().flip(true, false);}
//
//        float x = body.getPosition().x * gamePhysics.PPM -2.5f/camera.zoom;
//        float y = body.getPosition().y * gamePhysics.PPM -2.5f/camera.zoom;
        Rectangle tmp = hero.getRect(camera, hero.getFrame());
        ((PolygonShape)body.getFixtureList().get(0).getShape()).setAsBox(tmp.width/2, tmp.height/2);// Высчитываем размер нашего полигона в данное кремя (всех полигонов из атласа)
        ((PolygonShape)body.getFixtureList().get(1).getShape()).setAsBox(tmp.width/3, tmp.height/10, new Vector2(0,-tmp.height/2), 0);
//        ((PolygonShape)body.getFixtureList().get(1).getShape()).setAsBox(
//                tmp.width/3,
//                tmp.height/10,
//                new Vector2(0, -tmp.height/2),0);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(hero.getFrame(), tmp.x, tmp.y,tmp.width*GamePhysics.PPM, tmp.height * GamePhysics.PPM );

        Array<Body> bodies = gamePhysics.getBodys("coins"); //получили тела со всех слоев физ мира с данным именем
        coinAnm.setTime(delta);
        TextureRegion tr = coinAnm.draw();
        float dScale = 6;
        for (Body bd: bodies){
            //tmp = hero.getRect(camera, manAnm.draw());
            float cx = bd.getPosition().x * GamePhysics.PPM - tr.getRegionWidth() / 2 / dScale;
            float cy = bd.getPosition().y * GamePhysics.PPM - tr.getRegionHeight() / 2 / dScale;
            float cW = tr.getRegionWidth() / GamePhysics.PPM / dScale;
            float cH = tr.getRegionHeight() / GamePhysics.PPM / dScale;
            ((PolygonShape)bd.getFixtureList().get(0).getShape()).setAsBox(cW/2, cH/2);
            batch.draw(tr, cx, cy,cW * GamePhysics.PPM, cH * GamePhysics.PPM );

        }
         // отрисовываем то что будет перед персонажем (перекрывать персонажа)

        batch.end();
        //Gdx.graphics.setTitle(String.valueOf(body.getLinearVelocity()));
        mapRenderer.render(front);


        for (Body bd: bodyToDelete){
            gamePhysics.destroyBody(bd);// удаляем сами тела из физики
        }
        bodyToDelete.clear(); //зачем чистим

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
