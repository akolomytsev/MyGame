package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import java.awt.*;

public class MenuScreen implements Screen {
    Game game;
    private Music music;
    Texture fon, sign;
    SpriteBatch batch;
    int x, y;
    Rectangle rectangle;

    public MenuScreen(Game game) {
        this.game = game;
        fon = new Texture("fon.png");
        sign = new Texture("play2.png");
        x = Gdx.graphics.getWidth() / 2 - sign.getWidth() / 2;
        y = Gdx.graphics.getHeight() / 2 - sign.getHeight() / 2;
        rectangle = new Rectangle(x, y, sign.getWidth(), sign.getHeight());
        batch = new SpriteBatch();

        music = Gdx.audio.newMusic(Gdx.files.internal("George Thorogood - Bad to the Bone_(newmp3.org).mp3"));
        music.setPan(0, 0.005f);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(fon, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // отрисовка фона
        batch.draw(sign, x, y);
        batch.end();

        if (Gdx.input.isTouched()) {
            if (rectangle.contains(Gdx.input.getX(), Gdx.input.getY())) {
                dispose();
                game.setScreen(new GameScreen(game));
            }
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
        this.sign.dispose();
        this.fon.dispose();
        this.batch.dispose();
        music.dispose();
    }
}
