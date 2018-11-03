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
    }

    public Trail(GameObject gameObject) {
        super(gameObject);
        if(gameObject instanceof Trail) {
            Trail trail = (Trail) gameObject;
            this.life = trail.life;
            this.alpha = trail.alpha;
            this.width = trail.width;
            this.height = trail.height;
            this.color = trail.color;
        }
    }

    public Trail(float x, float y, ID id, Color color, int width, int height, float life, int objectID) {
        super(x, y, id,objectID);

        this.color = color;
        this.height = height;
        this.width = width;
        this.life = life;
    }

    public void tick() {
        //System.out.println("I am now being trailed for.");
        if(alpha > life) {
            alpha-= (life - 0.0001f);
        }else{
            UniversalHandler.getThingHandler().removeEntityObject(this);
        }
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;



        g2d.setComposite(makeTransparent(alpha));

        g.setColor(color);
        g.fillRect((int)x,(int)y,width,height);

        g2d.setComposite(makeTransparent(1));
    }

    private AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;

        if((alpha <= 0.0F) && (alpha >= 1.0F)) {
            alpha = 1.0F - 0.0001f;

        }

        try {
            return (AlphaComposite.getInstance(type, alpha));
        } catch (IllegalArgumentException e) {
            System.out.println(type + " " + alpha);
            System.out.println((alpha >= 0.0F) + " " + (alpha <= 1.0F));
            e.printStackTrace();
        }
        return null;
    }

    public Rectangle getBounds() {
        return null;
    }
}
