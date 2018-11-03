package io.github.fernthedev.secondgame.main.netty.client;

import com.github.fernthedev.exceptions.NewPlayerAddedException;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.ThingHandler;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.Trail;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.github.fernthedev.secondgame.main.ClientObject;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.Handler;
import io.github.fernthedev.secondgame.main.entities.Coin;
import io.github.fernthedev.secondgame.main.entities.Player;

import java.util.*;

public class ClientEntityHandler implements ThingHandler {

    public static Vector<GameObject> gameObjects = new Vector<>();

    public static Map<Integer,GameObject> gameObjectMap = new HashMap<>();

    private static List<Coin> coinList = new ArrayList<>();

    @Override
    public List<GameObject> getGameObjects() {
        //System.out.println("Client objects");
        return gameObjects;
    }

    public Map<Integer, GameObject> getGameObjectMap() {
        return gameObjectMap;
    }

    public void setGameObjectMap(Map<Integer, GameObject> gameObjectMap) {
        ClientEntityHandler.gameObjectMap = gameObjectMap;
    }


    @Override
    public void setGameObjects(List<GameObject> gameObjects) {

        ClientEntityHandler.gameObjects = new Vector<>(gameObjects);
    }


    /**
     * This updates the player object for use of different instances.
     * @param clientPlayerE This is null
     * @param universalPlayer The player instance to use.
     */
    @Override
    public void updatePlayerObject(Object clientPlayerE, UniversalPlayer universalPlayer) {


        GameObject gameObjectOld = gameObjectMap.get(universalPlayer.getObjectID());


        // System.out.println("\n" + noTraiLlist() + " old " + gameObjectOld);

       if(gameObjectOld != null)
        removeEntityObject(gameObjectOld);

        addEntityObject(universalPlayer);


        if(universalPlayer == Game.mainPlayer || gameObjectOld == Game.mainPlayer || Game.mainPlayer.getObjectID() == universalPlayer.getObjectID()) {
            Game.mainPlayer = (Player) universalPlayer;
        }

      /*  if(Game.gameState == Game.STATE.InServer) {
            Game.sendPacket(new SendPlayerInfoPacket(new UniversalPlayer(Game.mainPlayer)));
        }*/


    }

    @Override
    public void addEntityObject(GameObject serverGameObject) {
        GameObject gameObject = ClientObject.getObjectType(serverGameObject);



        //if(!(gameObject instanceof Trail)) System.out.println(serverGameObject);

        if(gameObject == null) throw new NullPointerException("Added a null pointer");

        if(!gameObjects.contains(gameObject))
        gameObjects.add(gameObject);

        if(serverGameObject instanceof Coin && !coinList.contains(serverGameObject))
            coinList.add((Coin) serverGameObject);

        if(serverGameObject instanceof Player)
            try {
                throw new NewPlayerAddedException((UniversalPlayer) serverGameObject);
            } catch (NewPlayerAddedException e) {
               // e.printStackTrace();
            }
        if(!gameObjectMap.containsValue(gameObject))
        gameObjectMap.put(serverGameObject.getObjectID(),gameObject);



    }


    @Deprecated
    public void addEntityObject(ClientObject serverGameObject) {

        GameObject gameObject = ClientObject.getObjectType(serverGameObject);

        if(gameObject == null) throw new NullPointerException("Added a null pointer");

        gameObjects.add(gameObject);
        gameObjectMap.put(serverGameObject.getObjectID(),gameObject);
        //   if(serverGameObject.id != ID.Trail)
        //   System.out.println(serverGameObject.id);

        if(gameObject.id == ID.Player) {
            addEntityObject((new UniversalPlayer(gameObject)));
            //  if(netPlayer.getObjectID() == serverGameObject.getObjectID()) addPlayerObject(netPlayer);
        }


    }

    public void removeEntityObject(GameObject gameObject) {

       // GameObject gameObject = ClientObject.getObjectType(serverGameObject);



     //   if(!(gameObject instanceof Trail)) System.out.println(serverGameObject);

  //      if(gameObject == null) throw new NullPointerException("Added a null pointer");

        if(gameObject instanceof Coin) System.out.println("Removing the coin.");



        gameObjects.remove(gameObject);
        gameObjectMap.remove(gameObject.getObjectID(), gameObject);

        if(gameObject instanceof Coin) {
            coinList.remove(gameObject);
            System.out.println(coinList + "\n" + noTraiLlist());

        }

        if(gameObjects.contains(gameObject)) {
            System.out.println("Failed to delete variable");
            throw new NullPointerException();
        }
        //    if(serverGameObject.id != ID.Trail)
        //   System.out.println(serverGameObject.id);

     /*   if(!(serverGameObject instanceof Trail)) {
            System.out.println(noTraiLlist(gameObjects) + "\n is the list and removed " + gameObject);

            System.out.println(gameObjects.contains(gameObject));
        }*/

    }

    public synchronized List<GameObject> noTraiLlist(List<GameObject> oldgameObjects) {
        List<GameObject> noTrail = new ArrayList<>();

        List<GameObject> gameObjects = new ArrayList<>(oldgameObjects);

        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject tempObject = iterator.next();
            if(tempObject != null && !(tempObject instanceof Trail)) noTrail.add(tempObject);
        }

        return noTrail;
    }

    public synchronized List<GameObject> noTraiLlist() {
        List<GameObject> noTrail = new ArrayList<>();

        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject tempObject = iterator.next();
            if(tempObject != null && !(tempObject instanceof Trail)) noTrail.add(tempObject);
        }

        return noTrail;
    }

    private Handler getHandler() {
        return Game.getHandler();
    }
}
