package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.*;

import java.awt.*;
import java.util.Random;

public class UniversalPlayer extends GameObject {
    protected Random r = new Random();

    protected Color color = Color.WHITE;
    protected int health = 100;
    protected int coin;

   protected UniversalPlayer() {
    }

    /**
     * This is used for keeping velocity while using different coordinates.
     * @param keepPlayer The player with the velocity to keep
     * @param newPlayer The player with the coordinates to keep
     */
    public UniversalPlayer(UniversalPlayer keepPlayer, UniversalPlayer newPlayer) {
        this.velX = keepPlayer.getVelX();
        this.velY = keepPlayer.getVelY();

            this.health = newPlayer.getHealth();

        this.coin = newPlayer.coin;
        this.id = ID.Player;

        this.x = newPlayer.getX();
        this.y = newPlayer.getY();
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public UniversalPlayer(GameObject gameObject) {
        super(gameObject);

        if(gameObject instanceof UniversalPlayer) {
            UniversalPlayer player = (UniversalPlayer) gameObject;
            this.health = player.health;
            this.color = player.color;


            this.velY = player.getVelY();
            this.velX = player.getVelX();
            this.health = player.getHealth();
            this.coin = player.getCoin();
        }
    }

    public UniversalPlayer(int x, int y, ID id,int objectID) {
        super(x, y, id,objectID);
    }

    public UniversalPlayer(int x, int y, ID id,Velocity velX,Velocity velY,int objectID) {
        super(x, y, id,objectID,velX,velY);
    }
/*
    @Deprecated
    public UniversalPlayer(NetPlayer netPlayer) {
        super((int)netPlayer.getX(),(int)netPlayer.getY(),netPlayer.id,netPlayer.getObjectID(),(int)netPlayer.getVelX(),(int)netPlayer.getVelY());
    }

    public UniversalPlayer(NetPlayer netPlayer, Color color) {
        super((int)netPlayer.getX(),(int)netPlayer.getY(),netPlayer.id,netPlayer.getObjectID(),(int)netPlayer.getVelX(),(int)netPlayer.getVelY());

        this.color = color;
    }*/

    public void tick() {
      //  System.out.println("Ticked");
        this.x = x + velX;
        this.y = y + velY;

        //System.out.println(x + " " + y + " " + velX + " " + velY + " oooooooooooooooooooooooooooold " + (x + velX) + " " + (y + velY));
        x = UniversalHandler.clamp(x,0,UniversalHandler.WIDTH - 37f);
        y = UniversalHandler.clamp(y,0,UniversalHandler.HEIGHT - 60f);
        UniversalHandler.getThingHandler().addEntityObject(new Trail(x,y,ID.Trail,Color.WHITE,32,32,0.05f, GameObject.entities));

        //System.out.println(x + " " + y + " " + velX + " " + velY + " newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww" + (x + velX) + " " + (y + velY) + "\n");

    }

    @Override
    public void render(Graphics g) {

    }



    public Color getColor() {
        return color;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,32,32);
    }
}
