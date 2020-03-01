package com.github.fernthedev.server.gameHandler;

import com.github.fernthedev.exceptions.UnsupportedMethodForType;
import com.github.fernthedev.packets.ObjectUpdates.SendObjectsList;
import com.github.fernthedev.server.ClientPlayer;
import com.github.fernthedev.server.Server;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.ThingHandler;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.github.fernthedev.universal.entity.Trail;
import io.netty.channel.Channel;

import java.util.*;

public class EntityHandler extends ThingHandler implements Runnable {
    public Map<Integer, GameObject> getGameObjectMap() {
        return gameObjectMap;
    }


    private boolean toChange = false;

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

    public synchronized void updatePlayerObject(Object clientPlayerE, EntityPlayer universalPlayer) {
        if (!(clientPlayerE instanceof ClientPlayer)) try {
            throw new UnsupportedMethodForType(Object.class, ClientPlayer.class);
        } catch (UnsupportedMethodForType unsupportedMethodForType) {
            unsupportedMethodForType.printStackTrace();
        }else {

            if (universalPlayer == null) throw new NullPointerException();

            toChange = true;


            GameObject gameObjectOld = gameObjectMap.get(universalPlayer.getObjectID());

            if(gameObjectOld != null)
                removeEntityObject(gameObjectOld);

            addPlayerEntityObject((ClientPlayer) clientPlayerE,universalPlayer);

            ClientPlayer clientPlayer = (ClientPlayer) clientPlayerE;






            clientPlayer.setPlayerObject(universalPlayer);
            //clientPlayer.sendObject(new SetCurrentPlayer(universalPlayer));


            System.out.println("Attempting to update info " + universalPlayer);

            clientPlayer.isChanged = true;
        }
    }

    public synchronized void addPlayerEntityObject(ClientPlayer clientPlayer, EntityPlayer gameObject) {

        if(gameObject == null) throw new NullPointerException();

        if(!gameObjects.contains(gameObject))
            gameObjects.add(gameObject);

        if(!gameObjectMap.containsValue(gameObject))
            gameObjectMap.put(gameObject.getObjectID(),gameObject);

        toChange = true;
        clientPlayer.isChanged = true;

        //HashMap<ClientPlayer,UniversalPlayer> playerUniversalPlayerHashMap = new HashMap<>(playerClientMap);



        // System.out.println("Added player " + gameObject + " to " + clientPlayer + " " + playerUniversalPlayerHashMap.containsKey(clientPlayer) + playerUniversalPlayerHashMap);
    }

    public synchronized void removePlayerEntityObject(GameObject gameObject) {

        if(gameObject == null) throw new NullPointerException();


        removeEntityObject(gameObject);

        //    System.out.println("Removed player " + gameObject);
    }


    @Override
    public synchronized void addEntityObject(GameObject gameObject) {

        super.addEntityObject(gameObject);

        if(gameObject instanceof Trail) return;

        /*if(gameObject instanceof EntityPlayer ) {
            try {
                throw new UnsupportedMethodForType(gameObject.getClass(),GameObject.class);
            } catch (UnsupportedMethodForType unsupportedMethodForType) {
                unsupportedMethodForType.printStackTrace();
            }

            ClientPlayer clientPlayer = ClientPlayer.getPlayerFromObject(gameObject.getObjectID());
            if(clientPlayer != null)
            playerClientMap.put(clientPlayer, (EntityPlayer) gameObject);
        }*/

        toChange = true;

    }

    public void removeEntityObject(GameObject serverGameObject) {
        super.removeEntityObject(serverGameObject);


        if(serverGameObject instanceof EntityPlayer)
        /*if(playerClientMap.containsValue(serverGameObject)) {
            System.out.println("Why isn't this deleted yet? " + serverGameObject);

            for(ClientPlayer clientPlayer : playerClientMap.keySet()) {
                if(playerClientMap.get(clientPlayer) == serverGameObject) {
                    System.out.println(clientPlayer + " is responsible " + playerClientMap);
                    break;
                }
            }

        }*/
            toChange = true;

        // Server.sendObjectToAllPlayers(new SendObjectsList(gameObjects));
    }

    public void tick() {
        super.tick();


        onChanged();
    }

    private synchronized void updatePlayerInfo(ClientPlayer clientPlayer) {
        toChange = false;

        String list = null;
        ArrayList<GameObject> gameObjectList = new ArrayList<>(noTraiLlist(gameObjects));

        // if (!clientPlayer.isChanged)
        //       gameObjectList.remove(clientPlayer.getPlayerObject());

        try {
            list = UniversalHandler.gson.toJson(gameObjectList);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        clientPlayer.sendObject(new SendObjectsList(list,clientPlayer.isChanged,clientPlayer.getPlayerObject()));
        clientPlayer.isChanged = false;
    }

    private synchronized void onChanged() {
        if (toChange) {
            toChange = false;


            List<GameObject> objects = new ArrayList<>(gameObjects);

            Map<Channel,ClientPlayer> channelClientPlayerHashMap = new HashMap<>(Server.socketList);

            for(GameObject gameObject :objects) {
                if(gameObject.id == ID.Player) {
                    boolean doesHavePlayer = false;
                    for(ClientPlayer clientPlayer : channelClientPlayerHashMap.values()) {
                        if(clientPlayer.getPlayerObject() == gameObject) {
                            doesHavePlayer = true;
                            break;
                        }
                    }

                    if(!doesHavePlayer) {
                        gameObjects.remove(gameObject);
                    }
                }
            }

            for (ClientPlayer clientPlayer : channelClientPlayerHashMap.values()) {

                updatePlayerInfo(clientPlayer);
            }


            //  Server.sendObjectToAllPlayers(new SendObjectsList(gameObjects));
        }
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
