package com.mygdx.game.persons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.GamePhysics;
import com.mygdx.game.enums.Actions;

import java.util.HashMap;

public class Hero {
    HashMap<Actions, Animation<TextureRegion>> manAssetss; //
    private final Sound sound;
    private final float FPS = 1/5f;// Основная скорость картинки персонажа вынесена константой
    private float time;// время
    public static boolean canJump, isFire, isUp, isSitting;//
    private Animation<TextureRegion> baseAnm;//
    private boolean loop;//
    private TextureAtlas atlas;// текстурный атлас героя
    private Body body;//
    private Dir dir;//
    private static float dScale = 6f;// принудительный масштаб атласа героя

    private float hitPoints, live;
    public enum Dir{A, D}//
//    private Sound sound;


public Hero(Body body){// конструктор описывающий физическое тело в игровом физическом мире
        hitPoints = live = 100;
        this.body = body;

        manAssetss = new HashMap<>();//
        atlas = new TextureAtlas("atlas/men.atlas");//
        sound = Gdx.audio.newSound(Gdx.files.internal("cokot-kablukov-po-trotuaru.mp3"));

        manAssetss.put(Actions.STAND, new Animation<TextureRegion>(FPS, atlas.findRegions("stand")));//
        manAssetss.put(Actions.RUN, new Animation<TextureRegion>(FPS, atlas.findRegions("run")));//
        manAssetss.put(Actions.JUMP, new Animation<TextureRegion>(FPS, atlas.findRegions("jamp")));//
        manAssetss.put(Actions.UP, new Animation<TextureRegion>(FPS, atlas.findRegions("up")));//
        manAssetss.put(Actions.SIT, new Animation<TextureRegion>(FPS, atlas.findRegions("sit")));//
        manAssetss.put(Actions.SHOOT, new Animation<TextureRegion>(FPS, atlas.findRegions("fire")));//
        baseAnm = manAssetss.get(Actions.STAND);//
        loop = true;//
        dir = Dir.D;//

}

    public float getHit (float damage){
        hitPoints -= damage;
        return hitPoints;
    }

    public int getDir(){
        return (dir == Dir.D)?1:-1;
    }

    public boolean isCanJump() {return canJump;}//
    public static void setCanJump(boolean isJump) {canJump = isJump;}//
    public void setDir(Dir dir){this.dir = dir;}// установка направления героя
    public void setLoop(boolean loop) {this.loop = loop;}// кусочек анимации
    public Body setFPS(Vector2 vector, boolean onGround) {// класс увеличения скорости картинки относительно его физической скорости (передаются вектор скорости и стоит ли герой на земле)
        if (vector.x > 0.01f) setDir(Dir.D);//
        if (vector.x < -0.01f) setDir(Dir.A);//
        float tmp = (float) (Math.sqrt(vector.x*vector.x + vector.y*vector.y))*10;// вычисляем вектор средней скорости
        setState(Actions.STAND);//
        if (isFire){
            setState(Actions.SHOOT);
            return body;
            //onGround = false;
        }
        if (Math.abs(vector.x) > 0.25f && Math.abs(vector.y) < 10 && onGround) {// увеличиваем скорость отображения картинки
            setState(Actions.RUN);//
            //sound.play();
            baseAnm.setFrameDuration(1/tmp);//
            return null;
        }
        if (Math.abs(vector.y) > 0.001F /*&& canJump*/) {// если герой прыгает, то FPS устанавливается по умолчанию
            setState(Actions.JUMP);// переключаемся в прыжок
//            baseAnm.setFrameDuration(FPS);//
//            return null;
        }
        if(isSitting){
            setState(Actions.SIT);
        } else if(isUp){
            setState(Actions.UP);
        }
        return null;
    }

    public float setTime(float deltaTime) {//
        time += deltaTime;
        return time;
    }

    public void setState(Actions state){//
        baseAnm = manAssetss.get(state);
        switch (state){
            case STAND: loop = true; baseAnm.setFrameDuration(FPS);break;// зацикленное изображение когда стоит
            case SHOOT: loop = true; baseAnm.setFrameDuration(FPS);break;
            case JUMP: loop = false; break; // анимация замирает на последнем кадре анимации
            default: loop = true;
        }
    }

    public TextureRegion getFrame() {// выбор направление картинки
        if (time > baseAnm.getAnimationDuration() && loop) time = 0;
        if (time > baseAnm.getAnimationDuration()) time = 0;
        TextureRegion tr = baseAnm.getKeyFrame(time);
        if (!tr.isFlipX() && dir == Dir.A) tr.flip(true, false);
        if (tr.isFlipX() && dir == Dir.D) tr.flip(true, false);
        return tr;
    }

    public Rectangle getRect(OrthographicCamera camera, TextureRegion region) {// получаем размер конкретного кадра из атласа
        TextureRegion tr = baseAnm.getKeyFrame(time);
        //TextureRegion tr = region;
        float cx = body.getPosition().x * GamePhysics.PPM - tr.getRegionWidth() / 2 / dScale;
        float cy = body.getPosition().y * GamePhysics.PPM - tr.getRegionHeight() / 2 / dScale;
        float cW = tr.getRegionWidth() / GamePhysics.PPM / dScale;
        float cH = tr.getRegionHeight() / GamePhysics.PPM / dScale;
        return new Rectangle(cx , cy, cW, cH);
    }

    public void dispose(){
        atlas.dispose();
        this.manAssetss.clear();
    }



}
