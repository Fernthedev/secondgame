package com.github.fernthedev;

import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public abstract class INewEntityRegistry extends TickRunnable {

    @Getter
    protected Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Integer>> gameObjects = Collections.synchronizedMap(new HashMap<>());

    private List<UUID> updatedPlayers = Collections.synchronizedList(new ArrayList<>());



    public void addEntityObject(@NonNull GameObject gameObject) {
        if (!gameObjects.containsKey(gameObject.getUniqueId()))
            gameObjects.put(gameObject.getUniqueId(), new ImmutablePair<>(gameObject, gameObject.hashCode()));
        else
            gameObjects.put(gameObject.getUniqueId(), new ImmutablePair<>(
                    gameObject,
                    gameObjects.get(gameObject.getUniqueId()).getRight()
            ));
    }

    public void removeEntityObject(GameObject gameObject) {
        gameObjects.remove(gameObject.getUniqueId());
    }

    public void collisionCheck(EntityPlayer universalPlayer) {

        List<GameObject> gameObjectsCheck = copyGameObjectsAsList();

        for (GameObject tempObject : gameObjectsCheck) {
            if (universalPlayer.getBounds().intersects(tempObject.getBounds())) {
                if (tempObject.getEntityId() == EntityID.ENEMY) {
                    //COLLISION CODE
                    universalPlayer.setHealth(universalPlayer.getHealth() - 2);
                    addToUpdateList(universalPlayer);
                }


                if (tempObject.getEntityId() == EntityID.Coin) {
                    universalPlayer.setCoin(universalPlayer.getCoin() + 1);
                    removeEntityObject(tempObject);
                    addToUpdateList(universalPlayer);
                    System.out.println("COllision checking! COIN");
                    // this.handler.removeObject(tempObject);
                }
            }
        }
    }

    public void tick() {
        List<GameObject> objects = copyGameObjectsAsList();

        for (GameObject tempObject : objects) {
            tempObject.tick();

            clampAndTP(tempObject);

            if (tempObject instanceof EntityPlayer)
                collisionCheck((EntityPlayer) tempObject);
        }

        onEntityUpdate();

        if (!updatedPlayers.isEmpty())
            new ArrayList<>(updatedPlayers).parallelStream().forEach(uuid -> {
                playerUpdate((EntityPlayer) getGameObjects().get(uuid).getKey());
                updatedPlayers.remove(uuid);
            });


        new HashMap<>(gameObjects).forEach((uuid, gameObjectIntegerPair) -> registerUpdatedObjectTime(gameObjectIntegerPair.getKey()));
    }

    /**
     * Registers the object last modified time
     */
    protected void registerUpdatedObjectTime(GameObject gameObject) {
        getGameObjects().put(gameObject.getUniqueId(), new ImmutablePair<>(gameObject, gameObject.hashCode()));
    }

    protected String clampAndTP(GameObject gameObject) {

        String result = "";

        float maxX = (float) UniversalHandler.WIDTH - (float) gameObject.getWidth() - gameObject.getWidth()/2f;
        float maxY = (float) UniversalHandler.HEIGHT - (float) gameObject.getHeight()*2f - gameObject.getWidth()/4f;

        float newX = GameMathUtil.clamp(gameObject.getX(), 0, maxX);
        float newY = GameMathUtil.clamp(gameObject.getY(), 0, maxY);

        if (newX != gameObject.getX() && gameObject instanceof EntityPlayer) {
            gameObject.setVelX(0);
            result += "X was clamped " + newX;
        }

        if (newY != gameObject.getY() && gameObject instanceof EntityPlayer) {
            result += " Y was clamped " + newY;
            gameObject.setVelY(0);
        }

        if (result.isEmpty())
            result = null;
        else {
            gameObject.setX(newX);
            gameObject.setY(newY);
        }

        return result;
    }

    protected void addToUpdateList(EntityPlayer entityPlayer) {
        if (!updatedPlayers.contains(entityPlayer.getUniqueId())) updatedPlayers.add(entityPlayer.getUniqueId());
    }

    public Map<UUID, Pair<GameObject, Integer>> copyGameObjectsAsMap() {
        return new HashMap<>(getGameObjects());
    }

    public List<GameObject> copyGameObjectsAsList() {
        return new ArrayList<>(getGameObjects().values())
                .parallelStream()
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }

    protected abstract void onEntityUpdate();


    /**
     * Individually handle every updated player
     * @param entityPlayer
     */
    protected abstract void playerUpdate(EntityPlayer entityPlayer);
}
