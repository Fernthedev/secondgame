package com.github.fernthedev.game.server.game_handler;

import com.github.fernthedev.core.StaticHandler;
import com.github.fernthedev.exceptions.UnsupportedMethodForType;
import com.github.fernthedev.game.server.ClientGameData;
import com.github.fernthedev.packets.object_updates.SendObjectsList;
import com.github.fernthedev.server.ClientConnection;
import com.github.fernthedev.server.PlayerHandler;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ThingHandler;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.github.fernthedev.universal.entity.NewGsonGameObject;
import io.netty.channel.Channel;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class EntityHandler extends ThingHandler {

    private static Map<ClientConnection, ClientGameData> clientGameDataMap = new HashMap<>();

    private boolean toChange = false;

    @Override
    public void setGameObjects(List<GameObject> gameObjects) {
        EntityHandler.gameObjects = new Vector<>(gameObjects);
    }

    public static ClientGameData getClientData(ClientConnection connection) {
        return clientGameDataMap.getOrDefault(connection, null);
    }

    public static void addClientData(ClientConnection connection, ClientGameData clientGameData) {
        clientGameDataMap.put(connection, clientGameData);
    }

    public static void removeClientData(ClientConnection connection) {
        clientGameDataMap.remove(connection);
    }

    @Synchronized
    public synchronized void updatePlayerObject(ClientConnection clientPlayerE, EntityPlayer universalPlayer) {
        if (clientPlayerE == null) try {
            throw new UnsupportedMethodForType(Object.class, ClientConnection.class);
        } catch (UnsupportedMethodForType unsupportedMethodForType) {
            unsupportedMethodForType.printStackTrace();
        }else {

            if (universalPlayer == null) throw new NullPointerException();

            toChange = true;


            GameObject gameObjectOld = gameObjectMap.get(universalPlayer.getUniqueId());

            if(gameObjectOld != null)
                removeEntityObject(gameObjectOld);

            addPlayerEntityObject(clientPlayerE, universalPlayer);

            ClientGameData clientGameData = clientGameDataMap.get(clientPlayerE);
            clientGameData.setEntityPlayer(universalPlayer);


            //clientPlayer.sendObject(new SetCurrentPlayer(universalPlayer));


            StaticHandler.getCore().getLogger().debug("Attempting to update info " + universalPlayer);

            clientGameData.setChanged(true);
        }
    }

    @Synchronized
    public synchronized void addPlayerEntityObject(ClientConnection clientPlayer, EntityPlayer gameObject) {

        if(gameObject == null) throw new NullPointerException();

        if(!gameObjects.contains(gameObject))
            gameObjects.add(gameObject);

        if(!gameObjectMap.containsValue(gameObject))
            gameObjectMap.put(gameObject.getUniqueId(), gameObject);

        toChange = true;
        clientGameDataMap.get(clientPlayer).setChanged(true);
    }

    public synchronized void removePlayerEntityObject(GameObject gameObject) {

        if(gameObject == null) throw new NullPointerException();


        removeEntityObject(gameObject);

        //    System.out.println("Removed player " + gameObject);
    }


    @Synchronized
    @Override
    public synchronized void addEntityObject(GameObject gameObject) {

        super.addEntityObject(gameObject);

        toChange = true;

    }

    public void removeEntityObject(GameObject serverGameObject) {
        super.removeEntityObject(serverGameObject);


        if(serverGameObject instanceof EntityPlayer)
            toChange = true;
    }

    public void tick() {
        super.tick();


        onChanged();
    }

    @Synchronized
    private synchronized void updatePlayerInfo(ClientConnection clientPlayer) {
        toChange = false;

        ClientGameData clientGameData = clientGameDataMap.get(clientPlayer);
        clientPlayer.sendObject(new SendObjectsList(
                noTrailList(gameObjects)
                        .parallelStream()
                        .map(NewGsonGameObject::new)
                        .collect(Collectors.toList()),
                clientGameData.isChanged(), clientGameData.getEntityPlayer()));
        clientGameData.setChanged(false);
    }

    @Synchronized
    private void onChanged() {
        if (toChange) {
            toChange = false;


            List<GameObject> objects = new ArrayList<>(gameObjects);

            Map<Channel, ClientConnection> channelClientConnectionHashMap = new HashMap<>(PlayerHandler.getChannelMap());

            for(GameObject gameObject : objects) {
                if(gameObject.entityId == EntityID.Player) {
                    boolean doesHavePlayer = false;
                    for(ClientConnection clientPlayer : channelClientConnectionHashMap.values()) {
                        ClientGameData clientGameData = clientGameDataMap.get(clientPlayer);
                        if(clientGameData.getEntityPlayer() == gameObject) {
                            doesHavePlayer = true;
                            break;
                        }
                    }

                    if(!doesHavePlayer) {
                        gameObjects.remove(gameObject);
                    }
                }
            }

            for (ClientConnection clientPlayer : channelClientConnectionHashMap.values()) {

                updatePlayerInfo(clientPlayer);
            }


            //  Server.sendObjectToAllPlayers(new SendObjectsList(gameObjects));
        }
    }








    public synchronized List<GameObject> noTrailList(List<GameObject> oldgameObjects) {
        List<GameObject> noTrail = new ArrayList<>();

        List<GameObject> gameObjects = new ArrayList<>(oldgameObjects);

        for (GameObject tempObject : gameObjects) {
            if (tempObject != null) noTrail.add(tempObject);
        }

        return noTrail;
    }

    public synchronized List<GameObject> noTrailList() {
        List<GameObject> noTrail = new ArrayList<>();

        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
        for (GameObject tempObject : gameObjects) {
            if (tempObject != null) noTrail.add(tempObject);
        }

        return noTrail;
    }
}
