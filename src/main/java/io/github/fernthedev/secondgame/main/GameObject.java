//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected ID id;
    protected float velX;
    protected float velY;

    public GameObject(float x, float y, ID id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();

    public abstract void render(Graphics var1);

    public abstract Rectangle getBounds();

    public void setX(int x) {
        this.x = (float)x;
    }

    public float getVelX() {
        return this.velX;
    }

    public void setVelX(int velX) {
        this.velX = (float)velX;
    }

    public float getVelY() {
        return this.velY;
    }

    public void setVelY(int velY) {
        this.velY = (float)velY;
    }

    public ID getId() {
        return this.id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = (float)y;
    }
}
