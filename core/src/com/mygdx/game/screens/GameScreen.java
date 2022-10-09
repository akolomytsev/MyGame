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

import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GamePhysics;
import com.mygdx.game.LabelHP;
import com.mygdx.game.MyContactListener;
import com.mygdx.game.MyInputProcessor;
import com.mygdx.game.enums.Actions;
import com.mygdx.game.persons.Bullet;
import com.mygdx.game.persons.Hero;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GameScreen implements Screen {
    Game game;

    private final SpriteBatch batch;
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
    private AnimationMan coinAnm;
    public static List<Body> bodyToDelete;
    public static List<Bullet> bullets;

    private int coins, bulletsCnt;


    private final LabelHP font;

    public GameScreen(Game game) {

        bulletsCnt = 100;
        coins = 0;
        font = new LabelHP(15);// новый экземпляр лоя надписи

        bodyToDelete = new ArrayList<>();

        bullets = new ArrayList<>();

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
        objects.clear();
        objects.addAll(map.getLayers().get("lava").getObjects().getByType(RectangleMapObject.class));
        objects.addAll(map.getLayers().get("spikes").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            gamePhysics.addDMGObject(objects.get(i));
        }

        body = gamePhysics.addObject((RectangleMapObject) map.getLayers().get("Гера").getObjects().get("Гера"));
        body.setFixedRotation(true); // защита от вращения
        hero = new Hero(body);

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        batch = new SpriteBatch();

        music = Gdx.audio.newMusic(Gdx.files.internal("George Thorogood - Bad to the Bone_(newmp3.org).mp3"));
        music.setPan(0,0.001f);
        music.setLooping(true);
        music.play();

        camera = new OrthographicCamera();
        camera.zoom = 0.2f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.position.x = body.getPosition().x * gamePhysics.PPM;
        camera.position.y = body.getPosition().y * gamePhysics.PPM;
        //camera.zoom = 0.35f;
        camera.update();



        mapRenderer.setView(camera);
        mapRenderer.render(tL); // отрисовываем то что будет на слое за персонажем

        hero.setTime(delta); //пересчет времени анимации
        Vector2 vector = myInputProcessor.getVector();
        Body body1 = hero.setFPS(body.getLinearVelocity(), true);
        if (body1 != null && bulletsCnt > 0){
            bulletsCnt--;
            bullets.add(new Bullet(gamePhysics, body1.getPosition().x, body1.getPosition().y, hero.getDir()));
            vector.set(0, 0);
        } else if (body1 != null){
            vector.set(0, 0);
            hero.setState(Actions.STAND);
        }
        if (MyContactListener.cnt < 1) {
            vector.set(vector.x, 0);
        }

        body.applyForceToCenter(vector, true);

        ArrayList<Bullet> bTmp = new ArrayList<>();
        for (Bullet b: bullets){
            Body tB = b.update(delta);
            if (tB != null){
                bodyToDelete.add(tB);
                bTmp.add(b);};
        }
        bullets.removeAll(bTmp);

        body.applyForceToCenter(myInputProcessor.getVector(), true);// тело принимает силу в центр из getVector

        hero.setFPS(body.getLinearVelocity(), true);// получает текущую линейную скорость тела

//        if(myInputProcessor.isSitting()){
//            hero.setState(Actions.SIT);
//        } else if(myInputProcessor.isUp()){
//            hero.setState(Actions.UP);
//        } else {hero.setState(Actions.STAND);}

        Rectangle tmp = hero.getRect(camera, hero.getFrame());
        ((PolygonShape)body.getFixtureList().get(0).getShape()).setAsBox(tmp.width/2, tmp.height/2);// Высчитываем размер нашего полигона в данное кремя (всех полигонов из атласа)
        ((PolygonShape)body.getFixtureList().get(1).getShape()).setAsBox(tmp.width/3, tmp.height/10, new Vector2(0,-tmp.height/2), 0);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "hp: "+ String.valueOf(hero.getHit(0)), tmp.x, tmp.y + tmp.height * GamePhysics.PPM);
        batch.draw(hero.getFrame(), tmp.x, tmp.y,tmp.width*GamePhysics.PPM, tmp.height * GamePhysics.PPM );

        Array<Body> bodies = gamePhysics.getBodys("coins"); //получили тела со всех слоев физ мира с данным именем
        coinAnm.setTime(delta);
        TextureRegion tr = coinAnm.draw();
        float dScale = 6;
        for (Body bd: bodies){

            float cx = bd.getPosition().x * GamePhysics.PPM - tr.getRegionWidth() / 2 / dScale;
            float cy = bd.getPosition().y * GamePhysics.PPM - tr.getRegionHeight() / 2 / dScale;
            float cW = tr.getRegionWidth() / GamePhysics.PPM / dScale;
            float cH = tr.getRegionHeight() / GamePhysics.PPM / dScale;
            ((PolygonShape)bd.getFixtureList().get(0).getShape()).setAsBox(cW/2, cH/2);
            batch.draw(tr, cx, cy,cW * GamePhysics.PPM, cH * GamePhysics.PPM );

        }


        batch.end();
        //Gdx.graphics.setTitle(String.valueOf(body.getLinearVelocity())); //координаты
        mapRenderer.render(front);


        for (Body bd: bodyToDelete){
            if (bd.getUserData() != null && bd.getUserData().equals("coins")){ coins++;}
            if (bd.getUserData() != null && bd.getUserData().equals("coins"))


            gamePhysics.destroyBody(bd);// удаляем сами тела из физики
        }
        bodyToDelete.clear(); //зачем чистим

        gamePhysics.step();

        gamePhysics.debugDraw(camera);


        if (MyContactListener.isDamage) {
            if (hero.getHit(1) < 1){
                dispose();
                game.setScreen(new GameOverScreen(game));
            }
        }


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
        this.hero.dispose();
        this.font.dispose();
        this.gamePhysics.dispose();
        this.coinAnm.dispose();
    }
}
