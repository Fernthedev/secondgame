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
    protected Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Long>> gameObjects = Collections.synchronizedMap(new HashMap<>());

    private List<UUID> updatedPlayers = Collections.synchronizedList(new ArrayList<>());



    public void addEntityObject(GameObject gameObject) {
        gameObjects.put(gameObject.getUniqueId(), new ImmutablePair<>(gameObject, System.nanoTime()));
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

            if(tempObject instanceof EntityPlayer)
                collisionCheck((EntityPlayer) tempObject);
        }

        onEntityUpdate();

        if(!updatedPlayers.isEmpty())
        new ArrayList<>(updatedPlayers).parallelStream().forEach(uuid -> {
            playerUpdate((EntityPlayer) getGameObjects().get(uuid).getKey());
            updatedPlayers.remove(uuid);
        });
    }

    protected void clampAndTP(GameObject gameObject) {

        gameObject.setX(UniversalHandler.clamp(gameObject.getX(), 0, (float) UniversalHandler.WIDTH - (float) gameObject.getWidth()));
        gameObject.setY(UniversalHandler.clamp(gameObject.getY(), 0, (float) UniversalHandler.HEIGHT - (float) gameObject.getHeight()*2));

    }

    protected void addToUpdateList(EntityPlayer entityPlayer) {
        if (!updatedPlayers.contains(entityPlayer.getUniqueId())) updatedPlayers.add(entityPlayer.getUniqueId());
    }

    public Map<UUID, Pair<GameObject, Long>> copyGameObjectsAsMap() {
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
