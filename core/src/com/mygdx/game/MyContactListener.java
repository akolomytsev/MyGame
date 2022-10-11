package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

import com.mygdx.game.screens.GameScreen;

public class MyContactListener implements ContactListener {

    public static int cnt = 0;
    public static boolean isDamage;

    @Override
    public void beginContact(Contact contact) { //во время начала контакта (обработка при начале пересечения)
        Fixture a = contact.getFixtureA(); // первое произвольное тело
        Fixture b = contact.getFixtureB(); // второе произвольное тело

        if (a.getUserData().equals("bullet") && b.getUserData().equals("stone")){
            GameScreen.bodyToDelete.add(a.getBody());

        }
        if (b.getUserData().equals("bullet") && a.getUserData().equals("stone")){
            GameScreen.bodyToDelete.add(b.getBody());
        }

        if (a.getUserData().equals("Гера") && b.getUserData().equals("coins")){ // если тело не равно телу с именем Гера и равно coins
            //Gdx.graphics.setTitle(String.valueOf(++cnt)); // простой счетчик с выводом в шапку приложения
            GameScreen.bodyToDelete.add(b.getBody());

        }
        if (b.getUserData().equals("Гера") && a.getUserData().equals("coins")){ // то же самое второй раз что бы точно одна из фикстур сработала
            //Gdx.graphics.setTitle(String.valueOf(++cnt)); //
            // удаление тела надо делать в физике, но никак не здесь. Из-за использования разной памяти для физики и Box2D
            GameScreen.bodyToDelete.add(a.getBody());
        }

        if (a.getUserData().equals("legs") && b.getUserData().equals("stone")){ //
            //Hero.canJump = true;
            cnt++;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("stone")){ //
            //Hero.canJump = true;
            cnt++;
        }
        if (a.getUserData().equals("legs") && b.getUserData().equals("damage")){ //
            isDamage = true;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("damage")){ //
            isDamage = true;

        }
    }

    @Override
    public void endContact(Contact contact) { // когда контакт прекращается
        Fixture a = contact.getFixtureA(); // первое произвольное тело
        Fixture b = contact.getFixtureB(); // второе произвольное тело

//        if (a.getUserData().equals("Гера") && b.getUserData().equals("coins")){ // если тело не равно телу с именем Гера и равно coins
//            //Gdx.graphics.setTitle(String.valueOf(--cnt));
//
//        }
//        if (b.getUserData().equals("Гера") && a.getUserData().equals("coins")){ // то же самое второй раз что бы точно одна из фикстур сработала
//            //Gdx.graphics.setTitle(String.valueOf(--cnt));
//        }
        if (a.getUserData().equals("legs") && b.getUserData().equals("stone")){ //
            //Hero.canJump = false;
            cnt--;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("stone")){ //
            //Hero.canJump = false;
            cnt--;
        }
        if (a.getUserData().equals("legs") && b.getUserData().equals("damage")){ //
            isDamage = false;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("damage")){ //
            isDamage = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { // когда физика считает что при следующем пересчете физики тела пересекутся (типа заранее)

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { // когда физика считает что при следующем пересчете физики тела разъединятся

    }
}
