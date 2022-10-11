package com.mygdx.game.persons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.GamePhysics;

public class Bullet {
    private final Body body;

    private final static float SPD = 120;
    private float time;

    public Bullet (GamePhysics gamePhysics, float x, float y, int dir){
        body = gamePhysics.addBullet(x,y);
        body.setBullet(true);
        body.setLinearVelocity(new Vector2(SPD*dir, 0)); // 0 для изменения полета по y

        time = 3;
    }
    public Body update(float dTime){
        time -= dTime;
        return (time <= 0)?body: null;
    }
}
