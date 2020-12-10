package com.github.fernthedev.universal.entity;

import com.github.fernthedev.CommonUtil;
import com.github.fernthedev.GameMathUtil;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class EntityPlayer extends GameObject {
    protected static Random r = UniversalHandler.RANDOM;

    @NonNull
    @Getter
    protected String name;

    @Getter
    @Setter
    protected int health = 100;

    @Getter
    @Setter
    protected int coin;

    public static final transient int MAX_VELOCITY = 5;

    public EntityPlayer(float x, float y, String name) {
        super(x, y, 32, 32, EntityID.PLAYER, new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
        this.name = name;
    }

    public EntityPlayer(float x, float y, UUID uniqueId, double velX, double velY, Color color, int health, int coin, String name) {
        super(x, y, 32, 32, EntityID.PLAYER, uniqueId, velX, velY, color);
        this.health = health;
        this.coin = coin;
        this.name = name;
    }

    protected EntityPlayer() { }

    /////////////////////////////////////////////////////////////////////////////////////


    public EntityPlayer(EntityPlayer universalPlayer) {
        super(universalPlayer);
        this.color = universalPlayer.getColor();
        this.health = universalPlayer.getHealth();
        this.coin = universalPlayer.getCoin();
    }

    public static boolean isPlayerDifferent(EntityPlayer oldPlayer, EntityPlayer copyVel, double velX, double velY) {
        return GameMathUtil.absDif(copyVel.getX(), oldPlayer.getX() + (float) velX) > CommonUtil.PLAYER_COORD_DIF ||
                GameMathUtil.absDif(copyVel.getY(), oldPlayer.getY() + (float) velY) > CommonUtil.PLAYER_COORD_DIF ||
                GameMathUtil.absDif(copyVel.getVelX(), oldPlayer.getVelX()) > CommonUtil.PLAYER_VEL_DIF ||
                GameMathUtil.absDif(copyVel.getVelY(), oldPlayer.getVelY()) > CommonUtil.PLAYER_VEL_DIF;
    }

    public void tick() {
        this.x = (float) (x + velX);
        this.y = (float) (y + velY);

        // System.out.println(x + " " + y + " " + velX + " " + velY + " oooooooooooooooooooooooooooold " + (x + velX) + " " + (y + velY));
//        x = UniversalHandler.clamp(x, 0, UniversalHandler.WIDTH - 37f);
//        y = UniversalHandler.clamp(y, 0, UniversalHandler.HEIGHT - 60f);


        /*
        x += velX;
        y += velY;
        x = Game.clamp(x,0,Game.WIDTH - 37f);
        y = Game.clamp(y,0,Game.HEIGHT - 60f);
        if(Game.gameState == Game.STATE.Game) {
            handler.addObject(new Trail(x, y, EntityID.Trail, Color.WHITE, 32, 32, 0.05f, GameObject.entities));
            collision();

        }*/

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityPlayer)) return false;
        if (!super.equals(o)) return false;
        EntityPlayer that = (EntityPlayer) o;
        return health == that.health && coin == that.coin && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, health, coin);
    }

//    @Deprecated
//    private void collision() {
//        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
//        for (GameObject tempObject : gameObjects) {
//            if (tempObject.getEntityId() == EntityID.ENEMY || tempObject.getEntityId() == EntityID.ENEMY || tempObject.getEntityId() == EntityID.ENEMY) {
//                if (getBounds().intersects(tempObject.getBounds())) {
//                    //COLLISION CODE
//                    health -= 2;
//                }
//            }
//
//            /*
//            if (tempObject.getEntityId() == EntityID.Coin) {
//                if (getBounds().intersects(tempObject.getBounds())) {
//                    Game.getHud().plusCoin();
//                    UniversalHandler.getThingHandler().removeEntityObject(tempObject);
//                    System.out.println("COllision checking! COIN");
//                    // this.handler.removeObject(tempObject);
//                }
//            }*/
//
//
//        }
//    }

}
