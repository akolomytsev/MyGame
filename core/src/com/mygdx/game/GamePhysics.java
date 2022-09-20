package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GamePhysics {
    final World world;
    private final Box2DDebugRenderer dDebugRenderer;

    public GamePhysics() {
        world = new World(new Vector2(0, -9.81f), true);
        this.dDebugRenderer = new Box2DDebugRenderer();
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
