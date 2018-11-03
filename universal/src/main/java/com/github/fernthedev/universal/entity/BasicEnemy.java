package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;

import java.awt.*;

public class BasicEnemy extends GameObject {



    public BasicEnemy(int x, int y, ID id, int objectID) {
        super(x, y, id,objectID);


        velX = 5;
        velY = 5;
    }

    public BasicEnemy(GameObject gameObject) {
        super(gameObject);
    }

    public void tick() {
        x += velX;
        y += velY;

        if (x <= 0 || x >= UniversalHandler.WIDTH - 16) velX *= -1;
        if (y <= 0 || y >= UniversalHandler.HEIGHT - 32) velY *= -1;

        UniversalHandler.getThingHandler().addEntityObject(new Trail(x, y, ID.Trail, Color.RED, 16, 16, 0.02f,GameObject.entities));

        //System.out.println("i was ticked");
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, 16, 16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 16, 16);
    }
}