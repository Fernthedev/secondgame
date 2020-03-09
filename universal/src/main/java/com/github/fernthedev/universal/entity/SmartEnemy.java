package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

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
        x += velX;
        y += velY;



        GameObject player = null;
        Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Integer>> objects;
        try {
             objects = new HashMap<>(UniversalHandler.getIGame().getEntityRegistry().getGameObjects());
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
            return;
        }

        if (!objects.containsKey(playerUUID)) {
            Optional<@NonNull Pair<@NonNull GameObject, Integer>> option = objects
                    .values().parallelStream()
                    .filter(gameObjectLongPair -> gameObjectLongPair.getKey() instanceof EntityPlayer)
                    .findAny();

            if (option.isPresent()) player = option.get().getKey();
        }
        else player = UniversalHandler.getIGame().getEntityRegistry().getGameObjects().get(playerUUID).getKey();


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



}
