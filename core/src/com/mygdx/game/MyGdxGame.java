package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img, coinImg;
	private MyAnimation animation;
	private Music stomp;
	private OrthographicCamera camera;
	int x;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		//coinImg = new Texture("sprite-animation4.png");

//		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		camera.position.set(new Vector3(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,0));
		animation =new MyAnimation("sprite-animation4.png",5,6,60, Animation.PlayMode.LOOP);


		stomp = Gdx.audio.newMusic(Gdx.files.internal("beg-po-trotuiarty.mp3"));
		stomp.setLooping(true);
		stomp.play();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		animation.setTime(Gdx.graphics.getDeltaTime());

//		camera.position.x = camera.position.x -1;
//
//		camera.update();
//		batch.setProjectionMatrix(camera.combined);

		float x = Gdx.input.getX() - animation.draw().getRegionWidth()/2;
		float y = Gdx.graphics.getHeight() - animation.draw().getRegionHeight()/2 - Gdx.input.getY();
		batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		batch.draw(img,
//				0,
//				0,
//				img.getWidth()/2,
//				img.getHeight()/2,
//				img.getWidth(),
//				img.getHeight(),
//				0,
//				0,
//				0,
//				0,
//				0,
//				img.getWidth(),
//				img.getHeight(),
//				false,
//				false);
		batch.draw(animation.draw(), x, y);
		batch.end();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		//coinImg.dispose();
		animation.dispose();
		stomp.dispose();
	}
}
