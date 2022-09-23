package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class MyInputProcessor implements InputProcessor {

//    private String outString = "";
//
//    public String getOutString() {
//        return outString;
//    }
    private Vector2 outForce;


    public MyInputProcessor() {
        outForce = new Vector2();
    }

    public Vector2 getVector(){return outForce;}

    @Override
    public boolean keyDown(int keycode) {
//        if (!outString.contains(Input.Keys.toString(keycode))){
//            outString += Input.Keys.toString(keycode);
//        }
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey){
            case "A": outForce.add(-1000.5F, 0);break;
            case "D": outForce.add(1000.5F, 0);break;
            case "W": outForce.add(0, -1000.5F);break;
            case "S": outForce.add(0, 10000.5F);break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
//        if (outString.contains(Input.Keys.toString(keycode))){
//            String tmp = (outString.replace(Input.Keys.toString(keycode), ""));
//            outString = tmp;
//        }
        String inKey = Input.Keys.toString(keycode).toUpperCase();

        switch (inKey){
            case "A": outForce.set(0, outForce.y);break;
            case "D": outForce.set(0, outForce.y);break;
            case "W": outForce.set(outForce.x, 0);break;
            case "S": outForce.set(outForce.x, 0);break;
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
