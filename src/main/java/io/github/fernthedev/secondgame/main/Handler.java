//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import io.github.fernthedev.secondgame.main.Game.STATE;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Handler {

    HUD hud;

    LinkedList<GameObject> object = new LinkedList<GameObject>();

    public void tick() {
        for(int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);

            tempObject.tick();
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i <  object.size();i++) {
            GameObject tempObject = object.get(i);
            try {
                TimeUnit.SECONDS.sleep((long) 0.3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tempObject.render(g);
        }

    }

    public void addObject(GameObject object) {
        this.object.add(object);
    }

    public void removeObject(GameObject object) {
        this.object.remove(object);
    }

    public void clearEnemies() {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            if (tempObject.id == ID.Player) {
                object.clear();
                if (Game.gameState != Game.STATE.End)
                    addObject(new Player((int) tempObject.getX(), (int) tempObject.getY(), ID.Player, this,hud));
            }
        }
    }

    public Handler(HUD hud) {
        this.hud = hud;
    }
}
