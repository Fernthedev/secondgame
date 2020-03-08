package com.github.fernthedev.game.server;

import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.core.StaticHandler;
import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.packets.object_updates.AddObjectPacket;
import com.github.fernthedev.packets.object_updates.RemoveObjectPacket;
import com.github.fernthedev.packets.object_updates.SendObjectsList;
import com.github.fernthedev.packets.object_updates.SetCoin;
import com.github.fernthedev.packets.player_updates.SendPlayerInfoPacket;
import com.github.fernthedev.server.ClientConnection;
import com.github.fernthedev.server.PlayerHandler;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.github.fernthedev.universal.entity.NewGsonGameObject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NewServerEntityRegistry extends INewEntityRegistry {

    @Setter
    private GameServer server;

    private Map<ClientConnection, ClientGameData> clientGameDataMap = Collections.synchronizedMap(new HashMap<>());

    private boolean toChange = false;

    private final Object modifyEntityListLock = new Object();
    private final Object modifyPlayerListLock = new Object();

    public boolean isClientDataEmpty() {
        return clientGameDataMap.isEmpty();
    }

    public ClientGameData getClientData(ClientConnection connection) {
        return clientGameDataMap.getOrDefault(connection, null);
    }

    //    @Synchronized(value = "modifyPlayerListLock")
    public void addClientData(ClientConnection connection, ClientGameData clientGameData) {
        clientGameDataMap.put(connection, clientGameData);
        addEntityObject(clientGameData.getEntityPlayer());

        toChange = true;
        clientGameData.setChanged(true);
    }

    //    @Synchronized(value = "modifyPlayerListLock")
    public void removeClientData(ClientConnection connection) {
        clientGameDataMap.remove(connection);
    }


    //    @Synchronized(value = "modifyPlayerListLock")
    public void updatePlayerObject(@NonNull ClientConnection clientPlayerE, EntityPlayer universalPlayer) {
        if (universalPlayer == null) throw new NullPointerException();

        toChange = true;

//        addPlayerEntityObject(clientPlayerE, universalPlayer);

        addEntityObject(universalPlayer);
        clientGameDataMap.get(clientPlayerE).setChanged(true);
        getClientData(clientPlayerE).setEntityPlayer(universalPlayer);


        //clientPlayer.sendObject(new SetCurrentPlayer(universalPlayer));


        StaticHandler.getCore().getLogger().debug("Attempting to update info {}", universalPlayer);
    }


//    @Synchronized(value = "modifyPlayerListLock")


    //    @Synchronized(value = "modifyEntityListLock")
    @Override
    public void addEntityObject(GameObject gameObject) {
        super.addEntityObject(gameObject);

//        ThreadUtils.runAsync(() ->
        clientGameDataMap.forEach((connection, clientGameData) -> {
            if (!gameObject.getUniqueId().equals(clientGameData.getEntityPlayer().getUniqueId())) {
//                clientGameData.getObjectsToAdd().add(gameObject.getUniqueId());
                connection.sendObject(new AddObjectPacket(new NewGsonGameObject(gameObject)));
                clientGameData.setChanged(true);
            }
        });
//        );


        toChange = true;
    }

    @Override
//    @Synchronized(value = "modifyEntityListLock")
    public void removeEntityObject(GameObject gameObject) {
        super.removeEntityObject(gameObject);

//        ThreadUtils.runAsync(new )
        clientGameDataMap.forEach((connection, clientGameData) -> {
//            clientGameData.getObjectsToAdd().remove(gameObject.getUniqueId());
            connection.sendObject(new RemoveObjectPacket(gameObject.getUniqueId()));
            clientGameData.setChanged(true);
        });

        toChange = true;
    }

    @Override
    public void collisionCheck(EntityPlayer universalPlayer) {

//        List<GameObject> objects = new ArrayList<>(EntityHandler.getGameObjects());
//
//
//        PlayerHandler.getChannelMap().values()
//
//        for (ClientConnection clientPlayer : PlayerHandler.getChannelMap().values()) {
//            for (GameObject tempObject : objects) {
//
//                if (playerObject.getBounds().intersects(tempObject.getBounds())) {
//                    if (tempObject.getEntityId() == EntityID.ENEMY) {
//
//                        //COLLISION CODE
//                        playerObject.setHealth(playerObject.getHealth() - 2);
//
//                        ServerGameHandler.getEntityHandler().updatePlayerObject(clientPlayer, playerObject);
//
//                    }
//
//                    if (tempObject.getEntityId() == EntityID.Coin) {
//
//                        ServerGameHandler.getEntityHandler().removeEntityObject(tempObject);
//
//                        clientPlayer.sendObject(new SetCoin(playerObject.getCoin() + 1));
//                    }
//                }
//            }
//
//            if (playerObject.getHealth() <= 0) {
//                clientPlayer.sendObject(new GameOverPacket());
//                clientPlayer.close();
//            }
//        }

        List<GameObject> gameObjectsCheck = copyGameObjectsAsList();

        new ArrayList<>(clientGameDataMap.keySet()).parallelStream()
                .filter(connection -> clientGameDataMap.get(connection).getEntityPlayer().getUniqueId() == universalPlayer.getUniqueId())
                .forEach(connection -> {
                    ClientGameData clientGameData = getClientData(connection);

                    gameObjectsCheck.parallelStream()
                            .filter(gameObject -> gameObject.getBounds().intersects(universalPlayer.getBounds()))
                            .forEach(tempObject -> {

                                if (tempObject.getEntityId() == EntityID.ENEMY) {
                                    //COLLISION CODE
                                    universalPlayer.setHealth(universalPlayer.getHealth() - 2);
                                    connection.sendObject(new SendPlayerInfoPacket(universalPlayer));
                                    clientGameData.setChanged(true);


                                }


                                if (tempObject.getEntityId() == EntityID.Coin) {
                                    universalPlayer.setCoin(universalPlayer.getCoin() + 1);
                                    connection.sendObject(new SetCoin(universalPlayer.getCoin()));
                                    removeEntityObject(tempObject);
                                    System.out.println("Collision checking! COIN");
                                    // this.handler.removeObject(tempObject);
                                }


                            });

                    if (universalPlayer.getHealth() <= 0) {
                        connection.sendObject(new GameOverPacket());
                        connection.close();
                        removeEntityObject(getClientData(connection).getEntityPlayer());
                        removeClientData(connection);
                    }
                });
    }

    /**
     * Use at init
     *
     * @param connection
     */
    public void forceUpdate(ClientConnection connection) {
        getClientData(connection).getObjectsToAdd().addAll(gameObjects.keySet());
        updatePlayerInfo(connection);
    }

    //    @Synchronized("modifyEntityListLock")
    private void updatePlayerInfo(ClientConnection clientPlayer) {
        toChange = false;

        Map<UUID, NewGsonGameObject> newGameObjects = new HashMap<>();
        ClientGameData clientGameData = getClientData(clientPlayer);

        // Update existing or removed entities
        clientGameData.getObjectCacheTime().entrySet().parallelStream()
                .filter(uuidLongEntry ->
                        !getGameObjects().containsKey(uuidLongEntry.getKey()) ||
                                (
                                        getGameObjects().containsKey(uuidLongEntry.getKey()) &&
                                                (uuidLongEntry.getValue() < getGameObjects().get(uuidLongEntry.getKey()).getValue() ||
                                                        uuidLongEntry.getKey().hashCode() != getGameObjects().get(uuidLongEntry.getKey()).getKey().hashCode()
                                                )
                                ))
                .forEach(uuidLongEntry -> {

                    // Update entity if it still exists
                    if (getGameObjects().containsKey(uuidLongEntry.getKey()) && (
                            uuidLongEntry.getValue() < getGameObjects().get(uuidLongEntry.getKey()).getValue()) ||
                            uuidLongEntry.getKey().hashCode() != getGameObjects().get(uuidLongEntry.getKey()).getKey().hashCode()
                    ) {
                        newGameObjects.put(uuidLongEntry.getKey(), new NewGsonGameObject(getGameObjects().get(uuidLongEntry.getKey()).getLeft()));
                        clientGameData.getObjectCacheTime().put(uuidLongEntry.getKey(), System.nanoTime());
                    }

                    // Remove from list if entity was removed
                    if (!getGameObjects().containsKey(uuidLongEntry.getKey())) {
                        newGameObjects.put(uuidLongEntry.getKey(), NewGsonGameObject.nullObject());
                        clientGameData.getObjectCacheTime().remove(uuidLongEntry.getKey());
                    }
                });


        //        try {


        Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Long>> cachedObjectMap = new HashMap<>(getGameObjects());
        new ArrayList<>(clientGameData.getObjectsToAdd()).parallelStream()
                .forEach(uuid -> {
                    try {
                        if (cachedObjectMap.containsKey(uuid))
                            newGameObjects.put(uuid, new NewGsonGameObject(cachedObjectMap.get(uuid).getKey()));

                        clientGameData.getObjectsToAdd().remove(uuid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
//        } catch (Exception e) {
//            new RuntimeException("Exception here", e);
//            System.exit(0);
//        }

//        Map<UUID, NewGsonGameObject> nonCachedObjects = new HashMap<>();
//
//        copyGameObjectsAsList().parallelStream().forEach(gameObject -> {
//            nonCachedObjects.put(gameObject.getUniqueId(), new NewGsonGameObject(gameObject));
//        });


        SendObjectsList sendObjectsList = new SendObjectsList(
                newGameObjects,
                clientGameData.isChanged(), clientGameData.getEntityPlayer());

//        System.out.println("Sending object list " + UniversalHandler.gson.toJson(sendObjectsList));

        clientPlayer.sendObject(sendObjectsList);
        clientGameData.setChanged(false);
    }


    public void onEntityUpdate() {
        onEntityUpdate(toChange);
    }

    public void onEntityUpdate(boolean override) {
        if (toChange || override) {
            toChange = false;

            StopWatch stopWatch = StopWatch.createStarted();
            List<ClientConnection> clientConnections = new ArrayList<>(PlayerHandler.getChannelMap().values());

//            TaskInfoForLoop<ClientConnection> taskInfoList = ThreadUtils.runForLoopAsync(clientConnections, connection -> {
            clientConnections.parallelStream().forEach(this::updatePlayerInfo);

//                return null;
//            });

//            taskInfoList.runThreads();
//            taskInfoList.awaitFinish(2);
            stopWatch.stop();
            StaticHandler.getCore().getLogger().debug("Updating players took {}", stopWatch.getTime(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * Individually handle every updated player
     *
     * @param entityPlayer
     */
    @Override
    protected void playerUpdate(EntityPlayer entityPlayer) {

    }


    public synchronized List<GameObject> noTrailList(List<GameObject> oldgameObjects) {
        return new ArrayList<>(oldgameObjects).parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void removeAllButPlayers() {
        Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Long>> oldMap = new HashMap<>(server.getServerGameHandler().getEntityHandler().getGameObjects());

        oldMap.keySet().parallelStream()
                .filter(uuid -> !(oldMap.get(uuid).getKey() instanceof EntityPlayer))
                .forEach(uuid -> removeEntityObject(oldMap.get(uuid).getKey()));

        toChange = true;

//        List<UUID> uuids = oldMap.keySet().parallelStream().filter(uuid -> !(oldMap.get(uuid).getKey() instanceof EntityPlayer)).collect(Collectors.toList());
//
//        Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Long>> newMap = new HashMap<>();
//        uuids.parallelStream().forEach(uuid -> oldMap.put(uuid, oldMap.get(uuid)));
//        oldMap.clear();
//
//        toChange = true;
//        server.getServerGameHandler().getEntityHandler().getGameObjects().clear();
//        server.getServerGameHandler().getEntityHandler().getGameObjects().putAll(newMap);
//
        server.getServerGameHandler().getEntityHandler().onEntityUpdate(true);
    }
}
