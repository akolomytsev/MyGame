package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.LabelHP;

public class VictoryScreen implements Screen {
    Game game;
    Texture fon;
    SpriteBatch batch;
    LabelHP label;

    private Music music;


    public VictoryScreen(Game game) {
        this.game = game;
        fon = new Texture("fon.png");
        batch = new SpriteBatch();
        label = new LabelHP(45);

        music = Gdx.audio.newMusic(Gdx.files.internal("rock-n-roll-racing-gonki-pod-rok-n-roll-igrovaya-pristavka-hamy4-sega-dendy_(mp3IQ.net).mp3"));
        music.setPan(0, 0.05f);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(fon, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        label.draw(batch, "Victory", 0, Gdx.graphics.getHeight() / 2);

        batch.end();

        if (Gdx.input.isTouched()) {
            dispose();
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

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
        this.fon.dispose();
        this.batch.dispose();
        this.label.dispose();
        music.dispose();
    }
}
