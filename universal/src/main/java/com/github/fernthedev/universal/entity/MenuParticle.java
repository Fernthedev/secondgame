package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;

import java.awt.*;
import java.util.Random;

public class MenuParticle extends GameObject {


    private Random r = new Random();

    private Color col;

    private int dir = 0;


    public MenuParticle(int x, int y, ID id, int objectID) {
        super(x, y, id,objectID);



        dir = r.nextInt(2);

        velX =(r.nextInt(7 - -7) + -7);
        velY =(r.nextInt(7 - -7) + -7);

        if(velX == 0) velX = 1;
        if(velY == 0) velY = 1;

        col = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));
    }

    public MenuParticle(GameObject gameObject) {
        super(gameObject);
        if(gameObject instanceof MenuParticle) {
            MenuParticle menuParticle = (MenuParticle) gameObject;
            this.col = menuParticle.col;
            this.dir = menuParticle.dir;
            this.r = menuParticle.r;
        }
    }



    public void tick() {
        x +=velX;
        y +=velY;

        if(x <= 0 || x >= UniversalHandler.WIDTH - 16)  velX *= -1;
        if(y <= 0 || y >= UniversalHandler.HEIGHT - 32)  velY *= -1;

        UniversalHandler.getThingHandler().addEntityObject(new Trail(x,y,ID.Trail,col,16,16,0.05f, GameObject.entities));
    }

    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int)x,(int)y,16,16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,16,16);
    }
}
