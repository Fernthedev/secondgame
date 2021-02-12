package com.github.fernthedev.universal;


import lombok.*;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@ToString
public abstract class GameObject implements Serializable {

    @EqualsAndHashCode.Exclude
    private static final long serialVersionUID = 9102545992378004553L;

    @Getter
    protected float x, y;

    @Getter
    @Setter
    protected float prevX, prevY;

    @Getter
    @Setter
    public EntityID entityId;

    @Getter
    @Setter
    protected double velX = 0, velY = 0;

    @NonNull
    @Getter
    protected UUID uniqueId = UUID.randomUUID();

//    @Deprecated
//    public static int entities = 0;

    @Getter
    @Setter
    protected Color color;

    protected int width, height;

    @Getter
    protected boolean hasTrail = true;

    protected GameObject(float x, float y, int width, int height, EntityID entityId, UUID uniqueId, double velX, double velY, Color color) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;

        this.entityId = entityId;
        this.velY = velY;
        this.velX = velX;

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

    protected GameObject(GameObject gameObject) {
        this.x = gameObject.x;
        this.y = gameObject.y;
        this.prevX = gameObject.prevX;
        this.prevY = gameObject.prevY;
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
        this.prevX = x;
        this.prevY = y;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameObject)) return false;
        GameObject that = (GameObject) o;
        return Float.compare(that.x, x) == 0 && Float.compare(that.y, y) == 0 && Double.compare(that.velX, velX) == 0 && Double.compare(that.velY, velY) == 0 && width == that.width && height == that.height && hasTrail == that.hasTrail && entityId == that.entityId && uniqueId.equals(that.uniqueId) && color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, entityId.name(), velX, velY, uniqueId, color, width, height, hasTrail);
    }

    public void setX(float x) {
        this.prevX = this.x;
        this.x = x;
    }

    public void setY(float y) {
        this.prevY = this.y;
        this.y = y;
    }
}
