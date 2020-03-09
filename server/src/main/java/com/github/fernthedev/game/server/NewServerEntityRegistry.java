package com.github.fernthedev.game.server;

import com.github.fernthedev.CommonUtil;
import com.github.fernthedev.GameMathUtil;
import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.core.ColorCode;
import com.github.fernthedev.core.StaticHandler;
import com.github.fernthedev.packets.object_updates.AddObjectPacket;
import com.github.fernthedev.packets.object_updates.RemoveObjectPacket;
import com.github.fernthedev.packets.object_updates.SendObjectsList;
import com.github.fernthedev.packets.object_updates.SetCoin;
import com.github.fernthedev.packets.player_updates.SendPlayerInfoPacket;
import com.github.fernthedev.server.ClientConnection;
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

    public boolean isClientDataEmpty() {
        return clientGameDataMap.isEmpty();
    }

    public ClientGameData getClientData(ClientConnection connection) {
        return clientGameDataMap.getOrDefault(connection, null);
    }

    //    @Synchronized(value = "modifyPlayerListLock")
    public void addClientData(ClientConnection connection, @NonNull ClientGameData clientGameData) {
        clientGameData.setChanged(true);
        clientGameDataMap.put(connection, clientGameData);
        addEntityObject(clientGameData.getEntityPlayer());

        toChange = true;
    }

    //    @Synchronized(value = "modifyPlayerListLock")
    public void removeClientData(ClientConnection connection) {
        clientGameDataMap.remove(connection);
    }


    //    @Synchronized(value = "modifyPlayerListLock")
    public void updatePlayerObject(@NonNull ClientConnection clientPlayerE, EntityPlayer universalPlayer) {
        if (universalPlayer == null) throw new NullPointerException();

        toChange = true;

        if (!getGameObjects().containsKey(universalPlayer.getUniqueId())) {
            StaticHandler.getCore().getLogger().debug(ColorCode.RED + "Player updating but is removed from game");
            return;
        }

//        addPlayerEntityObject(clientPlayerE, universalPlayer);
        getClientData(clientPlayerE).setEntityPlayer(universalPlayer);
        addEntityObject(universalPlayer);



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
                clientGameData.getObjectCacheList().add(gameObject.getUniqueId());
                connection.sendObject(new AddObjectPacket(new NewGsonGameObject(gameObject)));
            }
        });
//        );


        toChange = true;
    }

    @Override
//    @Synchronized(value = "modifyEntityListLock")
    public void removeEntityObject(GameObject gameObject) {
        super.removeEntityObject(gameObject);

        StaticHandler.getCore().getLogger().debug("Removed {}", gameObject.entityId);

//        ThreadUtils.runAsync(new )
        clientGameDataMap.forEach((connection, clientGameData) -> {
            clientGameData.getObjectsToAdd().remove(gameObject.getUniqueId());
            connection.sendObject(new RemoveObjectPacket(gameObject.getUniqueId()));

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
//                        connection.sendObject(new GameOverPacket());
//                        connection.close();
                        removeEntityObject(clientGameData.getEntityPlayer());
//                        removeClientData(connection);
                    }
                });
    }

    @Override
    protected String clampAndTP(GameObject gameObject) {
        String result = super.clampAndTP(gameObject);
        if (result != null)
            StaticHandler.getCore().getLogger().debug("{} {} {}", result, gameObject.getEntityId(), gameObject);

        return result;
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

        if (clientGameData == null) throw new NullPointerException("ClientGameData is null ");



        Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Integer>> cachedObjectMap = copyGameObjectsAsMap();


        /* //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Update existing or removed entities
//        clientGameData.getObjectCacheTime().entrySet().parallelStream()
        clientGameData.getObjectCacheList().parallelStream()
                .filter(uuidLongEntry ->
                        !cachedObjectMap.containsKey(uuidLongEntry) ||
                                (
                                        cachedObjectMap.containsKey(uuidLongEntry) &&
                                                cachedObjectMap.get(uuidLongEntry).getValue() != cachedObjectMap.get(uuidLongEntry).hashCode()
//                                                (uuidLongEntry.getValue() < getGameObjects().get(uuidLongEntry.getKey()).getValue() ||
//                                                        uuidLongEntry.hashCode() != getGameObjects().get(uuidLongEntry.getKey()).getKey().hashCode()
//                                                )
                                ))
                .forEach(uuidLongEntry -> {

                    // Update entity if it still exists
                    if (cachedObjectMap.containsKey(uuidLongEntry) && //(
                            cachedObjectMap.get(uuidLongEntry).getValue() != cachedObjectMap.get(uuidLongEntry).hashCode()
//                            uuidLongEntry.getValue() < getGameObjects().get(uuidLongEntry.getKey()).getValue()) ||
//                            uuidLongEntry.getValue().hashCode() != getGameObjects().get(uuidLongEntry.getKey()).getKey().hashCode()
                    ) {
                        newGameObjects.put(uuidLongEntry, new NewGsonGameObject(cachedObjectMap.get(uuidLongEntry).getLeft()));
//                        clientGameData.getObjectCacheTime().put(uuidLongEntry, System.nanoTime());
                    }

                    // Remove from list if entity was removed
                    if (!cachedObjectMap.containsKey(uuidLongEntry)) {
                        newGameObjects.put(uuidLongEntry, NewGsonGameObject.nullObject());
                        clientGameData.getObjectCacheList().remove(uuidLongEntry);
                    }
                });


        //        try {

        cachedObjectMap.keySet().parallelStream().filter(uuid -> !clientGameData.getObjectCacheList().contains(uuid))
                .forEach(uuid -> newGameObjects.put(uuid, new NewGsonGameObject(cachedObjectMap.get(uuid).getKey())));

        // Add objects that should have been added before

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


        */ ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        } catch (Exception e) {
//            new RuntimeException("Exception here", e);
//            System.exit(0);
//        }

//        Map<UUID, NewGsonGameObject> nonCachedObjects = new HashMap<>();
//
//        copyGameObjectsAsList().parallelStream().forEach(gameObject -> {
//            nonCachedObjects.put(gameObject.getUniqueId(), new NewGsonGameObject(gameObject));
//        });

        cachedObjectMap.forEach((uuid, gameObjectIntegerPair) -> {

            isClientDataEmpty();

            boolean changed = clientGameData.getObjectCacheList().add(uuid) ||
                    gameObjectIntegerPair.getRight() != gameObjectIntegerPair.getKey().hashCode();

//            StaticHandler.getCore().getLogger().debug("Updating {} and {}", gameObjectIntegerPair.getRight(), gameObjectIntegerPair.getKey().hashCode());


            if (changed && uuid == clientGameData.getEntityPlayer().getUniqueId())
                changed = isPlayerDifferent(clientGameData.getEntityPlayer(), (EntityPlayer) gameObjectIntegerPair.getLeft(), 0 ,0)
                        || clientGameData.isChanged();

            if (changed)
            {


                newGameObjects.put(uuid, new NewGsonGameObject(gameObjectIntegerPair.getKey()));
//                StaticHandler.getCore().getLogger().debug("Updating object hash code is different or not existing in list");

            }
        });

        new HashSet<>(clientGameData.getObjectCacheList()).forEach(uuid -> {
            if (!cachedObjectMap.containsKey(uuid)) {
//                StaticHandler.getCore().getLogger().debug("Removing objects");
                newGameObjects.put(uuid, NewGsonGameObject.nullObject());
                clientGameData.getObjectCacheList().remove(uuid);
            }
        });

//        boolean isChanged = cachedObjectMap.containsKey(clientGameData.getEntityPlayer().getUniqueId()) &&
//                clientGameData.getEntityPlayer().hashCode() != cachedObjectMap.get(clientGameData.getEntityPlayer().getUniqueId()).getRight();
//
//        if (isChanged && !newGameObjects.containsKey(clientGameData.getEntityPlayer().getUniqueId()))
//            new DebugException().printStackTrace();

        SendObjectsList sendObjectsList = new SendObjectsList(
                newGameObjects, clientGameData.isChanged(), clientGameData.getEntityPlayer());



//        System.out.println("Sending object list " + UniversalHandler.gson.toJson(sendObjectsList));

        if (!newGameObjects.isEmpty())
            clientPlayer.sendObject(sendObjectsList);

        clientGameData.setChanged(false);
    }


    public void onEntityUpdate() {
        onEntityUpdate(toChange);
    }

    public void onEntityUpdate(boolean override) {
//        if (toChange || override) {
//            toChange = false;
        boolean oldChange = toChange;
            StopWatch stopWatch = StopWatch.createStarted();
            List<ClientConnection> clientConnections = new ArrayList<>(clientGameDataMap.keySet());

//            TaskInfoForLoop<ClientConnection> taskInfoList = ThreadUtils.runForLoopAsync(clientConnections, connection -> {
            clientConnections.parallelStream().forEach(this::updatePlayerInfo);

//                return null;
//            });

//            taskInfoList.runThreads();
//            taskInfoList.awaitFinish(2);
            stopWatch.stop();
            if (oldChange)
                StaticHandler.getCore().getLogger().debug("Updating players took {}", stopWatch.getTime(TimeUnit.MILLISECONDS));
            toChange = false;
//        }
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

    public void removeRespawnAllPlayers() {
        Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Integer>> oldMap = new HashMap<>(server.getServerGameHandler().getEntityHandler().getGameObjects());

        oldMap.keySet().parallelStream()
                .filter(uuid -> !(oldMap.get(uuid).getKey() instanceof EntityPlayer))
                .forEach(uuid -> removeEntityObject(oldMap.get(uuid).getKey()));

        clientGameDataMap.forEach((connection, clientGameData) -> {
            clientGameData.getEntityPlayer().setHealth(100);
            addEntityObject(clientGameData.getEntityPlayer());
        });

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

    public void handleClientRespond(ClientConnection clientPlayer, SendPlayerInfoPacket infoPacket) {
        EntityPlayer oldPlayer = server.getServerGameHandler().getEntityHandler().getClientData(clientPlayer).getEntityPlayer();

        EntityPlayer packetPlayer = infoPacket.getPlayerObject();

        double velXClamp = GameMathUtil.clamp(packetPlayer.getVelX(), -EntityPlayer.MAX_VELOCITY, EntityPlayer.MAX_VELOCITY);
        double velYClamp = GameMathUtil.clamp(packetPlayer.getVelY(), -EntityPlayer.MAX_VELOCITY, EntityPlayer.MAX_VELOCITY);

        EntityPlayer copyVel = new EntityPlayer(
                oldPlayer.getX() + (float) velXClamp,
                oldPlayer.getY() + (float) velYClamp,
                oldPlayer.getUniqueId(),
                velXClamp,
                velYClamp,
                oldPlayer.getColor(),
                oldPlayer.getHealth(),
                oldPlayer.getCoin()
        );



        if (
                isPlayerDifferent(oldPlayer, copyVel, velXClamp, velYClamp)
        ) {
            clientGameDataMap.get(clientPlayer).setChanged(true);
            StaticHandler.getCore().getLogger().debug("Client player is changed");
        }


        updatePlayerObject(clientPlayer, copyVel);
    }

    private static boolean isPlayerDifferent(EntityPlayer oldPlayer, EntityPlayer copyVel, double velX, double velY) {
        return GameMathUtil.absDif(copyVel.getX(), oldPlayer.getX() + (float) velX) > CommonUtil.PLAYER_COORD_DIF ||
                GameMathUtil.absDif(copyVel.getY(), oldPlayer.getY() + (float) velY) > CommonUtil.PLAYER_COORD_DIF ||
                GameMathUtil.absDif(copyVel.getVelX(), oldPlayer.getVelX()) > CommonUtil.PLAYER_VEL_DIF ||
                GameMathUtil.absDif(copyVel.getVelY(), oldPlayer.getVelY()) > CommonUtil.PLAYER_VEL_DIF;
    }
}
