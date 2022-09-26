package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class MyInputProcessor implements InputProcessor {

    //private String outString = "";
    private boolean sitting = false;
    public boolean isSitting() {
        return sitting;
    }
    private boolean up = false;
    public boolean isUp() {
        return up;
    }


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
            case "A": outForce.add(-1F, 0);break;
            case "D": outForce.add(1F, 0);break;
            case "SPACE": outForce.add(0, 2.5F);break;
            case "S": sitting = true; break;
//            case "S": outForce.add(0, 0);break;
            case  "W" : up = true; break;
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
            case "S": sitting = false; break;
//            case "S": outForce.set(outForce.x, 0);break;
            case "SPACE": outForce.set(outForce.x, 0);break;
            case  "W" : up = false; break;
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
