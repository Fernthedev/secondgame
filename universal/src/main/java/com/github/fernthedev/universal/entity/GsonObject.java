package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;

import java.awt.*;

public class GsonObject extends GameObject {
    private float alpha = 1;

    private Color color;


    protected int health = 100;
    protected int coin;

    private int width,height;
    private float life;

    public int getHealth() {
        return health;
    }

    public int getCoin() {
        return coin;
    }

    public float getAlpha() {
        return alpha;
    }

    public Color getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getLife() {
        return life;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public Rectangle getBounds() {
        return null;
    }
}
