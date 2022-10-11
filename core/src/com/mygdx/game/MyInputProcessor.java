package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.persons.Hero;


public class MyInputProcessor implements InputProcessor {
    private boolean sitting = false;
    public boolean isSitting() {
        return sitting;
    }
    private boolean up = false;
    public boolean isUp() {
        return up;
    }

    private Vector2 outForce;


    public MyInputProcessor() {
        outForce = new Vector2();
    }

    public Vector2 getVector(){return outForce;}

    @Override
    public boolean keyDown(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey){
            case "A": outForce.add(-0.0012F, 0);break;
            case "D": outForce.add(0.0012F, 0);break;
            case "SPACE": if (MyContactListener.cnt>0) outForce.add(0, 0.012F);break;
            case "S": Hero.isSitting = true; break;
            case  "W" : Hero.isUp = true; break;
            case  "L" : Hero.isFire = true;break;

        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey){
            case "A": outForce.set(0, outForce.y);break;
            case "D": outForce.set(0, outForce.y);break;
            case "S": Hero.isSitting = false; break;
            case "SPACE": outForce.set(outForce.x, 0);break;
            case  "W" : Hero.isUp = false; break;
            case  "L" : Hero.isFire = false;break;

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
