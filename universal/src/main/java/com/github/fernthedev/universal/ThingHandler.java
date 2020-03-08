package com.github.fernthedev.universal;

import com.github.fernthedev.TickRunnable;
import com.github.fernthedev.exceptions.NewPlayerAddedException;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
class ThingHandler extends TickRunnable {


    @Getter
    protected static List<@NonNull GameObject> gameObjects = Collections.synchronizedList(new ArrayList<>());

    @Getter
    protected static Map<UUID, @NonNull GameObject> gameObjectMap = new HashMap<>();



    public void setGameObjects(List<GameObject> gameObjects) {

        ThingHandler.gameObjects = new Vector<>(gameObjects);
    }


    /**
     * This updates the player object for use of different instances.
     * @param universalPlayer The player instance to use.
     */
    public @Nullable EntityPlayer updatePlayerObject(UUID oldObject, EntityPlayer universalPlayer) {

        if (oldObject != null)
            removeEntityObject(gameObjectMap.get(oldObject));

        addEntityObject(universalPlayer);


        boolean isMainPlayer = oldObject != null && oldObject == universalPlayer.getUniqueId();

        if(isMainPlayer) {
            return universalPlayer;
        }

        return null;
    }

    public void addEntityObject(GameObject gameObject) {

        if(gameObject == null) throw new NullPointerException("Added a null pointer");

        if(!gameObjects.contains(gameObject))
            gameObjects.add(gameObject);






        if(gameObject instanceof EntityPlayer)
            try {
                throw new NewPlayerAddedException((EntityPlayer) gameObject);
            } catch (NewPlayerAddedException e) {
                // e.printStackTrace();
            }
        if(!gameObjectMap.containsValue(gameObject))
            gameObjectMap.put(gameObject.getUniqueId(), gameObject);



    }


    public void removeEntityObject(GameObject gameObject) {
        if(gameObject == null) throw new NullPointerException();

        gameObjects.remove(gameObject);
        gameObjectMap.remove(gameObject.getUniqueId(), gameObject);

        if(gameObjects.contains(gameObject)) {
            System.out.println("Failed to delete variable");
            throw new NullPointerException();
        }


    }


    public void tick() {
        List<GameObject> objects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());

        objects.parallelStream().forEach(tempObject -> {
            tempObject.tick();

            if(tempObject instanceof EntityPlayer)
                UniversalHandler.getThingHandler().collisionCheck((EntityPlayer) tempObject);
        });
    }

    public void collisionCheck(EntityPlayer universalPlayer) {

        List<GameObject> gameObjectsCheck = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());

        gameObjectsCheck.parallelStream().forEach(tempObject -> {
            if (tempObject.getEntityId() == EntityID.ENEMY || tempObject.getEntityId() == EntityID.ENEMY || tempObject.getEntityId() == EntityID.ENEMY) {
                if (universalPlayer.getBounds().intersects(tempObject.getBounds())) {
                    //COLLISION CODE
                    universalPlayer.setHealth(universalPlayer.getHealth() -2);
                }
            }


            if (tempObject.getEntityId() == EntityID.Coin) {
                if (universalPlayer.getBounds().intersects(tempObject.getBounds())) {
                    universalPlayer.setCoin(universalPlayer.getCoin() +1);
                    UniversalHandler.getThingHandler().removeEntityObject(tempObject);
                    System.out.println("COllision checking! COIN");
                    // this.handler.removeObject(tempObject);
                }
            }
        });
    }

    @Synchronized
    public synchronized List<GameObject> noTrailList(List<GameObject> oldgameObjects) {
        return new ArrayList<>(oldgameObjects).parallelStream().filter(Objects::nonNull).collect(Collectors.toList());

//        List<GameObject> noTrail = new ArrayList<>();
//
//        List<GameObject> gameObjects = new ArrayList<>(oldgameObjects);
//
//        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
//            GameObject tempObject = iterator.next();
//            if(tempObject != null && !(tempObject instanceof Trail)) noTrail.add(tempObject);
//        }
//
//        return noTrail;
    }

    @Synchronized
    public synchronized List<GameObject> noTrailList() {
        return noTrailList(getGameObjects());
//        List<GameObject> noTrail = new ArrayList<>();
//
//        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
//        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
//            GameObject tempObject = iterator.next();
//            if(tempObject != null && !(tempObject instanceof Trail)) noTrail.add(tempObject);
//        }
//
//        return noTrail;
    }
}
