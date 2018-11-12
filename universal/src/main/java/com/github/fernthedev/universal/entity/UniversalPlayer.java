package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniversalPlayer extends GameObject {
    protected Random r = new Random();

    protected Color color;
    protected int health = 100;
    protected int coin;


    /**
     * This is used for keeping velocity while using different coordinates.
     * @param keepPlayer The player with the velocity to keep
     * @param newPlayer The player with the coordinates to keep
     */
    public UniversalPlayer(UniversalPlayer keepPlayer, UniversalPlayer newPlayer) {
        this.velX = keepPlayer.getVelX();
        this.velY = keepPlayer.getVelY();

        this.health = newPlayer.getHealth();
        this.color = newPlayer.getColor();

        this.coin = newPlayer.coin;
        this.objectID = newPlayer.getObjectID();
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

    public UniversalPlayer(GsonObject gameObject) {
        super(gameObject);


            this.health = gameObject.getHealth();
            this.color = gameObject.getColor();
            this.coin = gameObject.getCoin();

            this.velY = gameObject.getVelY();
            this.velX = gameObject.getVelX();
            this.health = gameObject.getHealth();

    }

    public UniversalPlayer(int x, int y, ID id,int objectID) {
        super(x, y, id,objectID);
        this.color = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));
    }

    public UniversalPlayer(int x, int y, ID id,Velocity velX,Velocity velY,int objectID) {
        super(x, y, id,objectID,velX,velY);

        this.color = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));
    }

    /////////////////////////////////////////////////////////////////////////////////////


    public UniversalPlayer(UniversalPlayer universalPlayer) {
        super(universalPlayer);
        this.color = universalPlayer.getColor();
        this.health = universalPlayer.getHealth();
        this.coin = universalPlayer.getCoin();
    }

    public void tick() {
        UniversalHandler.getThingHandler().addEntityObject(new Trail(x,y,ID.Trail,color,32,32,0.05f, GameObject.entities));
            this.x = x + velX;
            this.y = y + velY;

           // System.out.println(x + " " + y + " " + velX + " " + velY + " oooooooooooooooooooooooooooold " + (x + velX) + " " + (y + velY));
            x = UniversalHandler.clamp(x,0,UniversalHandler.WIDTH - 37f);
            y = UniversalHandler.clamp(y,0,UniversalHandler.HEIGHT - 60f);


        /*
        x += velX;
        y += velY;
        x = Game.clamp(x,0,Game.WIDTH - 37f);
        y = Game.clamp(y,0,Game.HEIGHT - 60f);
        if(Game.gameState == Game.STATE.Game) {
            handler.addObject(new Trail(x, y, ID.Trail, Color.WHITE, 32, 32, 0.05f, GameObject.entities));
            collision();

        }*/

    }

    @Deprecated
    private void collision() {
        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
        for (GameObject tempObject : gameObjects) {
            if (tempObject.getId() == ID.BasicEnemey || tempObject.getId() == ID.FastEnemy || tempObject.getId() == ID.SmartEnemy) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    //COLLISION CODE
                    health -= 2;
                }
            }

            /*
            if (tempObject.getId() == ID.Coin) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    Game.getHud().plusCoin();
                    UniversalHandler.getThingHandler().removeEntityObject(tempObject);
                    System.out.println("COllision checking! COIN");
                    // this.handler.removeObject(tempObject);
                }
            }*/


        }
    }


    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, 32, 32);
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
