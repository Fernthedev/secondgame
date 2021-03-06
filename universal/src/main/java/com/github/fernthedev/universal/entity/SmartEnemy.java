package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;

import java.awt.*;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
public class SmartEnemy extends GameObject {


    @Setter
    private UUID playerUUID;

    public SmartEnemy(float x, float y, EntityID entityId, EntityPlayer player) {
        super(x, y, 16, 16, entityId, Color.GREEN);
        this.playerUUID = player.getUniqueId();

//        for(int i = 0; i < UniversalHandler.getThingHandler().getGameObjects().size(); i++) {
//            GameObject tempObject = UniversalHandler.getThingHandler().getGameObjects().get(i);
//            if(tempObject.entityId == EntityID.Player) player = tempObject;
//        }

    }

    protected SmartEnemy() {
        super();
    }

    public void tick() {
        setX((float) (x + velX));
        setY((float) (y + velY));



        GameObject player = null;
        Map<@NonNull UUID, @NonNull GameObject> objects = UniversalHandler.getIGame().getEntityRegistry().getGameObjects();

        if (!objects.containsKey(playerUUID)) {
            Optional<@NonNull GameObject> option = objects
                    .values().parallelStream()
                    .filter(gameObjectLongPair -> gameObjectLongPair instanceof EntityPlayer)
                    .findAny();

            if (option.isPresent()) player = option.get();
        }
        else player = UniversalHandler.getIGame().getEntityRegistry().getGameObjects().get(playerUUID);


        if (player == null) {
            velX = 0;
            velY = 0;
            return;
        }

        playerUUID = player.getUniqueId();

//        @NonNull GameObject player = playerPair.getKey();

        float diffX = x - player.getX() - (float) player.getWidth();
        float diffY = y - player.getY() - (float) player.getHeight();
        float distance = (float) Math.sqrt((x - player.getX()) * (x - player.getX()) + (y - player.getY()) * (y - player.getY()));

        velX = ((-1 / distance) * diffX);
        velY = ((-1 / distance) * diffY);

//        if (velX != 0) velX += 0.2 * multiplyOpposite(velX);
//        if (velY != 0) velY += 0.2 * multiplyOpposite(velY);

        //if(x <= 0 || x >= GAME.WIDTH - 16)  velX *= -1;
        //if(y <= 0 || y >= GAME.HEIGHT - 32)  velY *= -1;

//        UniversalHandler.getThingHandler().addEntityObject(new Trail(x,y, EntityID.Trail,Color.green,16,16,0.02f, GameObject.entities));

    }


    public int multiplyOpposite(double val) {
        if (val > 0) return -1;
        return 1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmartEnemy)) return false;
        if (!super.equals(o)) return false;
        SmartEnemy that = (SmartEnemy) o;
        return playerUUID.equals(that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), playerUUID);
    }
}
