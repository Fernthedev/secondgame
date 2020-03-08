package io.github.fernthedev.secondgame.main.entities;

import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.logic.IEntityRenderer;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Trail extends GameObject {

    @Getter
    private float alpha = 1;

    @Getter
    private float life;

    {
        hasTrail = false;
    }

    //life = 0.01 - 0.1

    public Trail(GameObject gameObject, int width, int height, float life,Color color) {
        this(gameObject);
        this.width = width;
        this.height = height;
        this.life = life;
        this.color = color;

        checkTrail(this);
    }

    public Trail(GameObject gameObject) {
        super(gameObject);
        if(gameObject instanceof Trail) {
            Trail trail = (Trail) gameObject;
            this.life = trail.life;

            this.width = trail.width;
            this.height = trail.height;
            this.color = trail.color;

            checkTrail(this);
//            if(height == 0 || width == 0 || color == null || life <= 0.0 || alpha >= 1.0001) {
//                try {
//                    throw new IllegalArgumentException(height + " " + width + " " + color + " " + life + " " + alpha);
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    }

    public Trail(float x, float y, EntityID entityId, Color color, int width, int height, float life) {
        super(x, y, width, height, entityId, color);

        this.life = life;


        checkTrail(this);
//        if(height == 0 || width == 0 || color == null || life <= 0.0 || alpha >= 1.0001) {
//            try {
//                throw new IllegalArgumentException(height + " " + width + " " + color + " " + life + " " + alpha);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            }
//        }

        if(life <= 0) new IllegalArgumentException().printStackTrace();
    }

    public void tick() {
        //System.out.println("I am now being trailed for.");
//        float thing = (life - 0.0001f);
        //System.out.println(thing + " is the new alpha " + life);


        checkTrail(this);
//        if(height == 0 || width == 0 || color == null || life <= 0.0 || alpha >= 1.0001) {
//            try {
//                throw new IllegalArgumentException(height + " " + width + " " + color + " " + life + " " + alpha);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            }
//        }

        if(alpha > life) {
            alpha-= (life - 0.0001f);
        } else {
            Game.getStaticEntityRegistry().removeEntityObject(this);
//            UniversalHandler.getThingHandler().removeEntityObject(this);
        }
    }


    private static AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;

        try {
            return (AlphaComposite.getInstance(type, alpha));
        } catch (IllegalArgumentException e) {
            System.out.println((alpha >= 0.0F) + " " + (alpha <= 1.0F) + " " + alpha);
            e.printStackTrace();
        }
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

        return x + "X " + y + "Y " + velX + "velX " + velY + " velY" + entityId + "EntityID " + uniqueId + "ObjectID " + color + "col " + life + "life " + alpha + "alpha ";
    }

    public static class Renderer implements IEntityRenderer<Trail> {
        @Override
        public void render(Graphics g, Trail gameObject) {
            Graphics2D g2d = (Graphics2D) g;

//            checkTrail(gameObject);

            g2d.setComposite(makeTransparent(gameObject.getAlpha()));

            g.setColor(gameObject.getColor());
            g.fillRect((int)gameObject.getX(),(int)gameObject.getY(), gameObject.getWidth(),gameObject.getHeight());

            g2d.setComposite(makeTransparent(1));
        }
    }

    private static void checkTrail(Trail gameObject) throws IllegalArgumentException {

        List<String> values = new ArrayList<>();

        if (gameObject.getHeight() == 0) values.add("height");
        if (gameObject.getWidth() == 0) values.add("width");
        if (gameObject.getLife() <= 0.0) values.add("life " + gameObject.getLife());
        if (gameObject.getAlpha() >= 1.0001) values.add("alpha " + gameObject.getAlpha());
        if (gameObject.getColor() == null) values.add("color");


        if (!values.isEmpty()) throw new IllegalArgumentException("These values are incorrect: " + values);
    }


}
