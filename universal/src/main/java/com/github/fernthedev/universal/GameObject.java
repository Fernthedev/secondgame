package com.github.fernthedev.universal;


import lombok.*;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@ToString
@EqualsAndHashCode()
public abstract class GameObject implements Serializable {


    private static final long serialVersionUID = 9102545992378004553L;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    protected float x, y;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    public EntityID entityId;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    protected double velX = 0, velY = 0;

    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    protected UUID uniqueId = UUID.randomUUID();

//    @Deprecated
//    public static int entities = 0;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    protected Color color;

    @EqualsAndHashCode.Include
    protected int width, height;

    @Getter
    @EqualsAndHashCode.Include
    protected boolean hasTrail = true;



    protected GameObject(float x, float y, int width, int height, EntityID entityId, UUID uniqueId, Velocity velX, Velocity velY, Color color) {
        this.x = x;
        this.y = y;
        this.entityId = entityId;
        this.velY = velY.toInt();
        this.velX = velX.toInt();

        this.uniqueId = uniqueId;
        this.color = color;

        if (width == 0 || height == 0) throw new IllegalArgumentException("HEIGHT OR WIDTH ARE 0");

        this.width = width;
        this.height = height;

//        entityUp();
    }

    protected GameObject(float x, float y, int width, int height, EntityID entityId, UUID uniqueId, Color color) {
        this.x = x;
        this.y = y;
        this.entityId = entityId;
        this.velY = 0;
        this.velX = 0;

        this.uniqueId = uniqueId;
        this.color = color;

        this.width = width;
        this.height = height;

//        entityUp();
    }

    @Deprecated
    protected GameObject() { }

//    private static int getNextAvailableID() {
//        int nextAvailableID = 0;
//        while (UniversalHandler.getThingHandler().getGameObjectMap().containsKey(nextAvailableID)) {
//            nextAvailableID++;
//        }
//        return nextAvailableID;
//    }

//    /**
//     * @deprecated Replaced with {@link #getNextAvailableID()} since it grabs the next available ID
//     * @return entity size + 1
//     */
//    @Deprecated
//    private static int entityUp() {
//        return entities = UniversalHandler.getThingHandler().getGameObjects().size();
////        List<GameObject> gameObjectList = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
////
////
////        for(int i = 0;i < gameObjectList.size();i++) {
////
////            if(gameObjectList.get(i) == null) {
////                entities = i;
////
////                break;
////            }
////        }
////
////        if(entities < gameObjectList.size()) entities = gameObjectList.size() + 1;
////
////        return entities;
//    }

    protected GameObject(GameObject gameObject) {
        this.x = gameObject.x;
        this.y = gameObject.y;
        this.entityId = gameObject.entityId;
        this.uniqueId = gameObject.getUniqueId();
        this.velX = gameObject.velX;
        this.velY = gameObject.velY;

        this.width = gameObject.getWidth();
        this.height = gameObject.getHeight();

        this.color = gameObject.getColor();
    }

    public GameObject(float x,float y, int width, int height, EntityID entityId, Color color) {
        this.x =x;
        this.y = y;
        this.entityId = entityId;

        this.width = width;
        this.height = height;

//        this.objectID = entities;
        this.color = color;

//        entityUp();
    }

    public abstract void tick();


    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }


//    @Override
//    public String toString() {
//          return x + "X " + y + "Y " + velX + "velX " + velY + " velY" + entityId + "EntityID " + objectID + "ObjectID " + entities;
//    }
}
