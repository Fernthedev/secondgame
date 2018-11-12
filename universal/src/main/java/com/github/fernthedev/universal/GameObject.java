package com.github.fernthedev.universal;


import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class GameObject implements Serializable {


    private static final long serialVersionUID = 9102545992378004553L;

    protected float x, y;
    public ID id;
    protected float velX = 0, velY = 0;

    public int objectID = entityUp();

    public static int entities = 0;

    protected GameObject(float x, float y, ID id, int objectID,Velocity velX,Velocity velY) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.velY = velY.toInt();
        this.velX = velX.toInt();

        this.objectID = objectID;
        entityUp();
    }

    protected GameObject(float x,float y,ID id,int objectID) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.velY = 0;
        this.velX = 0;

        this.objectID = objectID;
        entityUp();
    }

    @Deprecated
    protected GameObject() {
    }

    private static int entityUp() {
        List<GameObject> gameObjectList = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());


        for(int i = 0;i < gameObjectList.size();i++) {

            if(gameObjectList.get(i) == null) {
                entities = i;

                break;
            }
        }

        if(entities < gameObjectList.size()) entities = gameObjectList.size() + 1;

        return entities;
    }

    protected GameObject(GameObject gameObject) {
        this.x = gameObject.x;
        this.y = gameObject.y;
        this.id = gameObject.id;
        this.objectID = gameObject.getObjectID();
        this.velX = gameObject.velX;
        this.velY = gameObject.velY;
    }

    public GameObject(float x,float y, ID id) {
        this.x =x;
        this.y = y;
        this.id = id;


        this.objectID =  entities;

        entityUp();
    }

    public int getObjectID() {
        return objectID;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }


      @Override
    public String toString() {
          return x + "X " + y + "Y " + velX + "velX " + velY + " velY" + id + "ID " + objectID + "ObjectID " + entities;
    }
}
