package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

public class GamePhysics {
    public final MyContactListener contactListener;
    public static final float PPM = 100;
    public final World world;
    private final Box2DDebugRenderer dDebugRenderer;



    public GamePhysics() {
        world = new World(new Vector2(0, -9.81f), true);
        this.dDebugRenderer = new Box2DDebugRenderer();

        contactListener = new MyContactListener();
        world.setContactListener(contactListener);
    }

    public void  removeBody(Body body){
        world.destroyBody(body); // метод для удаления тел при пересечении
    }

    public void destroyBody(Body body){world.destroyBody(body);}

    public Array<Body> getBodys(String name) {
        Array<Body> tmp = new Array<>();
        world.getBodies(tmp); // возвращает список всех тел которые находятся в MyMap.tmx
        Iterator<Body> it = tmp.iterator();
        while (it.hasNext()){// выбираем которые нам не нужны и удаляем
            Body body = it.next();
            if (!body.getUserData().equals("coins")) it.remove();
        }
        return tmp;
    }

    public Body addObject(RectangleMapObject object) {
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        if (type.equals("StaticBody")) def.type = BodyDef.BodyType.StaticBody;
        if (type.equals("DynamicBody")) def.type = BodyDef.BodyType.DynamicBody;

        def.position.set((rect.x + rect.width/2)/PPM, (rect.y + rect.height/2)/PPM);
        def.gravityScale = (float) object.getProperties().get("gravityScale");

        polygonShape.setAsBox(rect.width/2/PPM, rect.height/2/PPM);


        fdef.shape = polygonShape;
        fdef.friction = (float) object.getProperties().get("friction");
        fdef.density = 1;
        fdef.restitution = (float) object.getProperties().get("restitution");

        String name = "";
        if (object.getName() != null) name = object.getName();
        Body body;
        body = world.createBody(def);
        body.setUserData(name);
        body.createFixture(fdef).setUserData(name);

        if (name.equals("Гера")){
            polygonShape.setAsBox(rect.width/3/PPM, rect.height/10/PPM, new Vector2(0,-rect.width/2), 0);
            body.createFixture(fdef).setUserData("legs");
            body.getFixtureList().get(1).setSensor(true);
        }

        polygonShape.dispose();
        return body;
    }

    public void debugDraw (OrthographicCamera camera){
        dDebugRenderer.render(world, camera.combined);
    }

    public void step(){world.step(1/60f, 3, 3);}//  первая цифра - частота пересчета физики, вторая - количество скоростей для просчета

    public void dispose (){
        world.dispose();
        dDebugRenderer.dispose();
    }
}
