package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.Collections;
import java.util.List;

public class MyInputProcessor implements InputProcessor {

    private String outString = "";

    public String getOutString() {
        return outString;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!outString.contains(Input.Keys.toString(keycode))){
            outString += Input.Keys.toString(keycode);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (outString.contains(Input.Keys.toString(keycode))){
            String tmp = (outString.replace(Input.Keys.toString(keycode), ""));
            outString = tmp;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {// возвращает букву (не код) которую мы нажали (есть маленькие и большие)
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {// возвращает координату экрана которой мы коснулись (pointer - номер пальца, button - номер кнопки)
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {// когда кнопку опустили
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {// это если тащите мышкой или по экрану когда нажали
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {//просто когда водят мышкой отслеживает координаты
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {//
        return false;
    }
}
