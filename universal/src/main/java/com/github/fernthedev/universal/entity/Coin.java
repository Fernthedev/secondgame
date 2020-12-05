package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.EntityID;
import lombok.EqualsAndHashCode;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
public class Coin extends GameObject {

    public Coin(int x, int y, EntityID entityId) {
        super(x, y, 16, 16, entityId, Color.ORANGE);
        this.velX = 0.0F;
        this.velY = 0.0F;
    }

    protected Coin() {
        super();
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
    }
}