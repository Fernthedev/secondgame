package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;

import java.awt.*;

public class UniversalCoin extends GameObject {

    public UniversalCoin(int x, int y, ID id, int objectID) {
        super(x, y, id,objectID);
        this.velX = 0.0F;
        this.velY = 0.0F;
    }

    public UniversalCoin(GameObject gameObject) {
        super(gameObject);
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
    }

    @Override
    public void render(Graphics g) {

    }

    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, 16, 16);
    }
}
