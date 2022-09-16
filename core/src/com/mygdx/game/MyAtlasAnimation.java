package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAtlasAnimation {
    private final TextureAtlas atlas;
    private final Animation<TextureAtlas.AtlasRegion> anm;
    private float time;

    public MyAtlasAnimation(String atlas, String name, float fsp, Animation.PlayMode playMode){
        time = 0;
        this.atlas = new TextureAtlas(atlas);
        anm = new Animation<>(1/fsp, this.atlas.findRegions(name));
        anm.setPlayMode(playMode);
    }

    public TextureRegion draw(){
        return anm.getKeyFrame(time);
    }

    public void setTime (float dT){
        time +=dT;
    }

    public void dispose(){
        this.atlas.dispose();
    }
}
