//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import io.github.fernthedev.secondgame.main.Game.STATE;
import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
    LinkedList<GameObject> object = new LinkedList();
    HUD hud;

    public void tick() {
        for(int i = 0; i < this.object.size(); ++i) {
            GameObject tempObject = (GameObject)this.object.get(i);
            tempObject.tick();
        }

    }

    public void render(Graphics g) {
        for(int i = 0; i < this.object.size(); ++i) {
            GameObject tempObject = (GameObject)this.object.get(i);
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
        for(int i = 0; i < this.object.size(); ++i) {
            GameObject tempObject = (GameObject)this.object.get(i);
            if (tempObject.id == ID.Player) {
                this.object.clear();
                if (Game.gameState != STATE.End) {
                    this.addObject(new Player((int)tempObject.getX(), (int)tempObject.getY(), ID.Player, this, this.hud));
                }
            }
        }

    }

    public Handler(HUD hud) {
        this.hud = hud;
    }
}
