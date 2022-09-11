package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, coinImg;

	MyAnimation animation;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		coinImg = new Texture("sprite-animation4.png");
		animation =new MyAnimation("1612576368_183-p-chastitsi-na-zelenom-fone-234.png",6,5,15, Animation.PlayMode.LOOP);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		animation.setTime(Gdx.graphics.getDeltaTime());

		float x = Gdx.input.getX() - animation.draw().getRegionWidth()/2;
		float y = Gdx.graphics.getHeight() - animation.draw().getRegionHeight()/2 - Gdx.input.getY();
		batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(animation.draw(), x, y);


		//batch.draw(coinImg, x, y);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		animation.dispose();
	}
}
