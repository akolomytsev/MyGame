package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnimation {
    private final Texture img;
    private final Animation<TextureRegion> anm;
    private float time;

    public MyAnimation(String name,int row, int col, float fsp, Animation.PlayMode playMode){
        img = new Texture(name);
        TextureRegion reg1 = new TextureRegion(img);
        TextureRegion[][] regions = reg1.split(img.getWidth()/col, img.getHeight()/row);
        TextureRegion[] tmp = new TextureRegion[regions.length * regions[0].length];
        int cnt = 0;
        for (TextureRegion[] region : regions) {
            for (int j = 0; j < regions[0].length; j++) {
                tmp[cnt++] = region[j];
            }

        }

        anm = new Animation<TextureRegion>(1/fsp, tmp);
        anm.setPlayMode(playMode);
    }

    public TextureRegion draw(){
        return anm.getKeyFrame(time);
    }

    public void setTime (float dT){
        time +=dT;
    }

    public void dispose(){
        this.img.dispose();
    }

}
