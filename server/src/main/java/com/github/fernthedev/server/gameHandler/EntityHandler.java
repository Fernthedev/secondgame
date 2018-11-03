package com.github.fernthedev.server.gameHandler;

import com.github.fernthedev.exceptions.UnsupportedMethodForType;
import com.github.fernthedev.packets.ObjectUpdates.SendObjectsList;
import com.github.fernthedev.packets.PlayerUpdates.SetCurrentPlayer;
import com.github.fernthedev.server.ClientPlayer;
import com.github.fernthedev.server.Server;
import com.github.fernthedev.universal.*;
import com.github.fernthedev.universal.entity.Trail;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.netty.channel.Channel;

import java.util.*;

public class EntityHandler implements ThingHandler,Runnable {

    public static Vector<GameObject> gameObjects = new Vector<>();

    private static Map<Integer, GameObject> gameObjectMap = new HashMap<>();

    public static final Map<Channel, UniversalPlayer> playerMap = new HashMap<>();

    public static final Map<ClientPlayer,UniversalPlayer> playerClientMap = new HashMap<>();

    public Map<Integer, GameObject> getGameObjectMap() {
        return gameObjectMap;
    }

    public void setGameObjectMap(Map<Integer, GameObject> gameObjectMap) {
        EntityHandler.gameObjectMap = gameObjectMap;
    }

    @Override
    public List<GameObject> getGameObjects() {
        // System.out.println("Server objects " + gameObjects);
        return gameObjects;
    }

    @Override
    public void setGameObjects(List<GameObject> gameObjects) {
        EntityHandler.gameObjects = new Vector<>(gameObjects);
    }

    public synchronized void updatePlayerObject(Object clientPlayerE,UniversalPlayer universalPlayer) {

        if (!(clientPlayerE instanceof ClientPlayer)) try {
            throw new UnsupportedMethodForType(Object.class, ClientPlayer.class);
        } catch (UnsupportedMethodForType unsupportedMethodForType) {
            unsupportedMethodForType.printStackTrace();
        }
        else {

            ClientPlayer clientPlayer = (ClientPlayer) clientPlayerE;

           // HashMap<ClientPlayer, UniversalPlayer> playerUniversalPlayerHashMap = new HashMap<>(playerClientMap);

            UniversalPlayer gameObjectOld = playerClientMap.get(clientPlayer);

           // if (gameObjectOld == null)
          //      System.out.println("Is " + clientPlayer + " " + playerUniversalPlayerHashMap.containsKey(clientPlayer) + playerClientMap);

            if (universalPlayer == null) throw new NullPointerException();


            // System.out.println("\n" + noTraiLlist() + " old " + gameObjectOld);


            clientPlayer.setPlayerObject(universalPlayer);
            clientPlayer.sendObject(new SetCurrentPlayer(universalPlayer));

            if (gameObjectOld != null)
                removePlayerEntityObject(clientPlayer, gameObjectOld);

            addPlayerEntityObject(clientPlayer, universalPlayer);


            //System.out.println(universalPlayer + " old is " + gameObjectOld + " and players are" + noTraiLlist(gameObjects));


            onChanged();
        }
    }

    public void addPlayerEntityObject(ClientPlayer clientPlayer,UniversalPlayer gameObject) {

        if(gameObject == null) throw new NullPointerException();

        if(!gameObjects.contains(gameObject))
            gameObjects.add(gameObject);

        if(!gameObjectMap.containsValue(gameObject))
            gameObjectMap.put(gameObject.getObjectID(),gameObject);


        playerClientMap.put(clientPlayer, gameObject);
        onChanged();

        //HashMap<ClientPlayer,UniversalPlayer> playerUniversalPlayerHashMap = new HashMap<>(playerClientMap);



       // System.out.println("Added player " + gameObject + " to " + clientPlayer + " " + playerUniversalPlayerHashMap.containsKey(clientPlayer) + playerUniversalPlayerHashMap);
    }

    public void removePlayerEntityObject(ClientPlayer clientPlayer,GameObject gameObject) {

        if(gameObject == null) throw new NullPointerException();

        playerClientMap.remove(clientPlayer);
        removeEntityObject(gameObject);
    //    System.out.println("Removed player " + gameObject);
    }

    public void removePlayerEntityObject(ClientPlayer clientPlayer) {
        UniversalPlayer universalPlayer = playerClientMap.get(clientPlayer);
        playerClientMap.remove(clientPlayer);
        removeEntityObject(universalPlayer);


    }

    @Override
    public void addEntityObject(GameObject serverGameObject) {
        GameObject gameObject = ServerGameObject.getObjectType(serverGameObject);

        if(!gameObjects.contains(gameObject))
        gameObjects.add(gameObject);

        if(!gameObjectMap.containsValue(gameObject))
        gameObjectMap.put(serverGameObject.getObjectID(),gameObject);

        if(gameObject instanceof UniversalPlayer) {
            try {
                throw new UnsupportedMethodForType(gameObject.getClass(),GameObject.class);
            } catch (UnsupportedMethodForType unsupportedMethodForType) {
                unsupportedMethodForType.printStackTrace();
            }

            ClientPlayer clientPlayer = ClientPlayer.getPlayerFromObject(gameObject.getObjectID());
            if(clientPlayer != null)
            playerClientMap.put(clientPlayer, (UniversalPlayer) gameObject);
        }
    //    if(serverGameObject.id != ID.Trail)
     //   System.out.println(serverGameObject.id);

        /*if(gameObject.id == ID.Player) {
            addPlayerObject(new NetPlayer(new UniversalPlayer(gameObject)));
          //  if(netPlayer.getObjectID() == serverGameObject.getObjectID()) addPlayerObject(netPlayer);
        }*/
        onChanged();
        //Server.sendObjectToAllPlayers(new SendObjectsList(gameObjects));
    }

    public void removeEntityObject(GameObject serverGameObject) {

        gameObjects.remove(serverGameObject);
        gameObjectMap.remove(serverGameObject.getObjectID(), serverGameObject);

        if(serverGameObject instanceof UniversalPlayer)
        if(playerClientMap.containsValue(serverGameObject)) {
            System.out.println("Why isn't this deleted yet? " + serverGameObject);
            try {
                throw new UnsupportedMethodForType();
            } catch (UnsupportedMethodForType unsupportedMethodForType) {
                unsupportedMethodForType.printStackTrace();
            }
        }

        onChanged();

       // Server.sendObjectToAllPlayers(new SendObjectsList(gameObjects));
    }

    public void tick() {
        List<GameObject> objects = new ArrayList<>(gameObjects);



        //    if(Game.gameState != Game.STATE.Hosting) objects = ClientEntityHandler.gameObjects;
        //       else objects = EntityHandler.gameObjects;
        if (!objects.isEmpty())
            for (GameObject tempObject : objects) {
                tempObject.tick();
            }
    }

    private synchronized void onChanged() {
        String list = null;
        ArrayList<GameObject> gameObjectList = new ArrayList<>(gameObjects);
        try {
            list = UniversalHandler.gson.toJson(gameObjectList);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }

        Server.sendObjectToAllPlayers(new SendObjectsList(list));
      //  Server.sendObjectToAllPlayers(new SendObjectsList(gameObjects));
    }


    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(UniversalHandler.running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                //System.out.println(UniversalHandler.running);
                //updates++;
                delta--;
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
                //updates = 0;
            }
            try {Thread.sleep(UniversalHandler.tickWait);} catch(Exception e) {}
        }
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
}
