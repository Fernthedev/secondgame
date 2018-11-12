package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;

import java.awt.*;

public class Trail extends GameObject {

    private float alpha = 1;

    private Color color;

    private int width,height;
    private float life;

    //life = 0.01 - 0.1

    public Trail(GameObject gameObject, int width, int heigh, float life,Color color) {
        super(gameObject);
        this.width = width;
        this.height = heigh;
        this.life = life;
        this.color = color;

        if(height == 0 || width == 0 || color == null || life <= 0.0 || alpha >= 1.0001) {
            try {
                throw new IllegalArgumentException(height + " " + width + " " + color + " " + life + " " + alpha);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public Trail(GameObject gameObject) {
        super(gameObject);
        if(gameObject instanceof Trail) {
            Trail trail = (Trail) gameObject;
            this.life = trail.life;

            this.width = trail.width;
            this.height = trail.height;
            this.color = trail.color;

            if(height == 0 || width == 0 || color == null || life <= 0.0 || alpha >= 1.0001) {
                try {
                    throw new IllegalArgumentException(height + " " + width + " " + color + " " + life + " " + alpha);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public Trail(float x, float y, ID id, Color color, int width, int height, float life, int objectID) {
        super(x, y, id,objectID);

        this.color = color;
        this.height = height;
        this.width = width;
        this.life = life;

        if(height == 0 || width == 0 || color == null || life <= 0.0 || alpha >= 1.0001) {
            try {
                throw new IllegalArgumentException(height + " " + width + " " + color + " " + life + " " + alpha);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        if(life <= 0) new IllegalArgumentException().printStackTrace();
    }

    public void tick() {
        //System.out.println("I am now being trailed for.");
        float thing = (life - 0.0001f);
        //System.out.println(thing + " is the new alpha " + life);

        if(height == 0 || width == 0 || color == null || life <= 0.0 || alpha >= 1.0001) {
            try {
                throw new IllegalArgumentException(height + " " + width + " " + color + " " + life + " " + alpha);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        if(alpha > life) {
            alpha-= (life - 0.0001f);
        }else{
            UniversalHandler.getThingHandler().removeEntityObject(this);
        }
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if(height == 0 || width == 0 || color == null || life <= 0.0 || alpha >= 1.0001) {
            try {
                throw new IllegalArgumentException(height + " " + width + " " + color + " " + life + " " + alpha + " " + id);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        g2d.setComposite(makeTransparent(alpha));

        g.setColor(color);
        g.fillRect((int)x,(int)y,width,height);

        g2d.setComposite(makeTransparent(1));
    }

    private AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;

        try {
            return (AlphaComposite.getInstance(type, alpha));
        } catch (IllegalArgumentException e) {
            System.out.println((alpha >= 0.0F) + " " + (alpha <= 1.0F) + " " + alpha);
            e.printStackTrace();
        }
        return null;
    }

    public Rectangle getBounds() {
        return null;
    }

    @Override
    public String toString() {
        /*
        try {
            throw new DebugException("Summoned a trail");
        } catch (DebugException e) {
            e.printStackTrace();
        }*/

        return x + "X " + y + "Y " + velX + "velX " + velY + " velY" + id + "ID " + objectID + "ObjectID " + color + "col " + life + "life " + alpha + "alpha " + entities;
    }
}
