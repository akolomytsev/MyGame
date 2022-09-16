package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private MyAtlasAnimation run, stand, jump, up, sit, tmpA;
	private Music music;
	private Sound sound;
	private MyInputProcessor myInputProcessor;
	private float x,y;
	private int dir = 0, step = 1;

	
	@Override
	public void create () {
		myInputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(myInputProcessor);
		batch = new SpriteBatch();
		stand =new MyAtlasAnimation("atlas/men.atlas","stand",2, Animation.PlayMode.LOOP);
		run =new MyAtlasAnimation("atlas/men.atlas","run",20, Animation.PlayMode.LOOP);
		jump =new MyAtlasAnimation("atlas/men.atlas","jump",3, Animation.PlayMode.LOOP);
		up =new MyAtlasAnimation("atlas/men.atlas","up",1, Animation.PlayMode.LOOP);
		sit =new MyAtlasAnimation("atlas/men.atlas","sit",1, Animation.PlayMode.LOOP);
		tmpA = stand;

		music = Gdx.audio.newMusic(Gdx.files.internal("George Thorogood - Bad to the Bone_(newmp3.org).mp3"));

		music.setPan(0,0.025f);
		music.setLooping(true);
		music.play();

		sound = Gdx.audio.newSound(Gdx.files.internal("beg-po-trotuiarty.mp3"));
		//sound.play(); // надо выводить на клик
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		tmpA = stand;
		dir = 0;

		if (myInputProcessor.getOutString().contains("A")){
			dir = -1;
			tmpA = run;
		}
		if (myInputProcessor.getOutString().contains("D")){
			dir = 1;
			tmpA = run;
		}
		if (myInputProcessor.getOutString().contains("W")){
			tmpA = up;
		}
		if (myInputProcessor.getOutString().contains("S")){
			tmpA = sit;
		}
		if (myInputProcessor.getOutString().contains("Space")) {
			tmpA = jump;
		}

		if (dir == -1) x -= step;
		if (dir == 1) x += step;

		TextureRegion tmp = tmpA.draw();
		if (!tmpA.draw().isFlipX() & dir == -1){ tmpA.draw().flip(true,false);}
		if (tmpA.draw().isFlipX() & dir == 1) {tmpA.draw().flip(true,false);}

		tmpA.setTime(Gdx.graphics.getDeltaTime());

		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			sound.stop();
			sound.play(1,1,0);
		}


		batch.begin();
		batch.draw(tmpA.draw(), x, y);
		batch.end();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		tmpA.dispose();
		music.dispose();
		sound.dispose();
		stand.dispose();
		jump.dispose();
		run.dispose();
	}
}
