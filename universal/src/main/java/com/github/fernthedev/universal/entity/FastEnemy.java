package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;

import java.awt.*;

public class FastEnemy extends GameObject {




    public FastEnemy(int x, int y, ID id, int objectID) {
        super(x, y, id,objectID);



        velX =2;
        velY = 9;
    }

    public FastEnemy(GameObject gameObject) {
        super(gameObject);
    }

    public void tick() {
        x +=velX;
        y +=velY;

        if(x <= 0 || x >= UniversalHandler.WIDTH - 16)  velX *= -1;
        if(y <= 0 || y >= UniversalHandler.HEIGHT - 32)  velY *= -1;

        UniversalHandler.getThingHandler().addEntityObject(new Trail(x,y,ID.Trail,Color.CYAN,16,16,0.02f, GameObject.entities));

    }

    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int)x,(int)y,16,16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,16,16);
    }
}
