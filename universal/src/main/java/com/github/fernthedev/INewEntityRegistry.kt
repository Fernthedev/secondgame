package com.github.fernthedev;

import com.github.fernthedev.fernutils.thread.ThreadUtils;
import com.github.fernthedev.fernutils.thread.multiple.TaskInfoList;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public abstract class INewEntityRegistry extends TickRunnable {

    @Getter
    protected Map<@NonNull UUID, @NonNull GameObject> gameObjects = Collections.synchronizedMap(new HashMap<>());

    public void addEntityObject(@NonNull GameObject gameObject) {
        // For interpolation
        float prevX = gameObject.getPrevX();
        float prevY = gameObject.getPrevY();

        if (gameObjects.containsKey(gameObject.getUniqueId()) && gameObjects.get(gameObject.getUniqueId()).hashCode() != gameObject.hashCode()) {
            prevX = gameObject.getX();
            prevY = gameObject.getY();
        }

        gameObject.setPrevX(prevX);
        gameObject.setPrevY(prevY);

        gameObjects.put(gameObject.getUniqueId(), gameObject);
    }


    public void removeEntityObject(UUID gameObject) {
        gameObjects.remove(gameObject);
    }

    public void removeEntityObject(GameObject gameObject) {
        removeEntityObject(gameObject.getUniqueId());
    }

    public void collisionCheck(EntityPlayer universalPlayer) {
        List<GameObject> gameObjectsCheck = copyGameObjectsAsList();

        for (GameObject tempObject : gameObjectsCheck) {
            if (universalPlayer.getBounds().intersects(tempObject.getBounds())) {
                if (tempObject.getEntityId() == EntityID.ENEMY) {
                    //COLLISION CODE
                    universalPlayer.setHealth(universalPlayer.getHealth() - 2);
                }


                if (tempObject.getEntityId() == EntityID.COIN) {
                    universalPlayer.setCoin(universalPlayer.getCoin() + 1);
                    removeEntityObject(tempObject);
                    UniversalHandler.getIGame().getLoggerImpl().info("COllision checking! COIN");
                    // this.handler.removeObject(tempObject);
                }
            }
        }
    }

    public void tick() {
        List<GameObject> objects = copyGameObjectsAsList();


        TaskInfoList taskInfoList = ThreadUtils.runForLoopAsync(objects, tempObject -> {
            tempObject.tick();

            clampAndTP(tempObject);

            if (tempObject instanceof EntityPlayer)
                collisionCheck((EntityPlayer) tempObject);
        });

        Stopwatch stopwatch = Stopwatch.createStarted();
        taskInfoList.runThreads(getExecutorService());

        // Wait for all objects to finish ticking
        taskInfoList.awaitFinish(1);
        stopwatch.stop();
    }



    protected String clampAndTP(GameObject gameObject) {

        String result = "";

        float maxX = (float) UniversalHandler.WIDTH - (float) gameObject.getWidth() - gameObject.getWidth()/2f;
        float maxY = (float) UniversalHandler.HEIGHT - (float) gameObject.getHeight()*2f - gameObject.getWidth()/4f;

        float newX = GameMathUtil.clamp(gameObject.getX(), 0, maxX);
        float newY = GameMathUtil.clamp(gameObject.getY(), 0, maxY);

        if (newX != gameObject.getX() && gameObject instanceof EntityPlayer) {
            gameObject.setVelX(0);
            gameObject.setX(newX);
            result += "X was clamped " + newX;
        }

        if (newY != gameObject.getY() && gameObject instanceof EntityPlayer) {
            result += " Y was clamped " + newY;
            gameObject.setY(newY);
            gameObject.setVelY(0);
        }

        if (result.isEmpty())
            result = null;

        return result;
    }


    public Map<UUID, GameObject> copyGameObjectsAsMap() {
        return new HashMap<>(getGameObjects());
    }

    public List<GameObject> copyGameObjectsAsList() {
        return new ArrayList<>(getGameObjects().values())
                .parallelStream()
                .collect(Collectors.toList());
    }

    public Map<UUID, Integer> getObjectsAndHashCode() {
        return getObjectsAndHashCode(gameObjects.keySet());
    }

    public Map<UUID, Integer> getObjectsAndHashCode(Set<UUID> uuids) {
        Map<UUID, Integer> map = new HashMap<>();

        copyGameObjectsAsMap().forEach((uuid, gameObjectIntegerPair) -> {
            if(uuids.contains(uuid))
                map.put(uuid, gameObjectIntegerPair.hashCode());
        });

        return map;
    }




    protected abstract ExecutorService getExecutorService();
}
