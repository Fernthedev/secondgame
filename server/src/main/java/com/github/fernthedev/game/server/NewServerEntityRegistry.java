package com.github.fernthedev.game.server;

import com.github.fernthedev.CommonUtil;
import com.github.fernthedev.GameMathUtil;
import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.lightchat.core.ColorCode;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.packets.object_updates.AddObjectPacket;
import com.github.fernthedev.packets.object_updates.RemoveObjectPacket;
import com.github.fernthedev.packets.object_updates.SendObjectsList;
import com.github.fernthedev.packets.object_updates.SetCoin;
import com.github.fernthedev.packets.player_updates.SendPlayerInfoPacket;
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

@RequiredArgsConstructor
public class NewServerEntityRegistry extends INewEntityRegistry {

    @Setter
    private GameServer server;

    private final Map<ClientConnection, ClientGameData> clientGameDataMap = Collections.synchronizedMap(new HashMap<>());

    private boolean toChange = false;

    public boolean isClientDataEmpty() {
        return clientGameDataMap.isEmpty();
    }

    public ClientGameData getClientData(ClientConnection connection) {
        return clientGameDataMap.getOrDefault(connection, null);
    }

    public void addClientData(ClientConnection connection, @NonNull ClientGameData clientGameData) {
        clientGameData.setChanged(true);
        clientGameDataMap.put(connection, clientGameData);
        addEntityObject(clientGameData.getEntityPlayer());

        toChange = true;
    }

    public void removeClientData(ClientConnection connection) {
        clientGameDataMap.remove(connection);
    }


    public void updatePlayerObject(@NonNull ClientConnection clientPlayerE, EntityPlayer universalPlayer) {
        if (universalPlayer == null) throw new NullPointerException();

        toChange = true;

        if (!getGameObjects().containsKey(universalPlayer.getUniqueId())) {
            StaticHandler.getCore().getLogger().debug(ColorCode.RED + "Player updating but is removed from game");
            return;
        }

        getClientData(clientPlayerE).setEntityPlayer(universalPlayer);
        addEntityObject(universalPlayer);

        StaticHandler.getCore().getLogger().debug("Attempting to update info {}", universalPlayer);
    }


    @Override
    public void addEntityObject(GameObject gameObject) {
        super.addEntityObject(gameObject);

        clientGameDataMap.forEach((connection, clientGameData) -> {

            if (!gameObject.getUniqueId().equals(clientGameData.getEntityPlayer().getUniqueId())) {
                clientGameData.getObjectCacheList().add(gameObject.getUniqueId());
                connection.sendObject(new AddObjectPacket(new NewGsonGameObject(gameObject)));
            }
        });


        toChange = true;
    }

    @Override
    public void removeEntityObject(GameObject gameObject) {
        super.removeEntityObject(gameObject);

        StaticHandler.getCore().getLogger().debug("Removed {}", gameObject.entityId);

        clientGameDataMap.forEach((connection, clientGameData) -> connection.sendObject(new RemoveObjectPacket(gameObject.getUniqueId())));

        toChange = true;
    }

    @Override
    public void collisionCheck(EntityPlayer universalPlayer) {
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
                        removeEntityObject(clientGameData.getEntityPlayer());
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
        updatePlayerInfo(connection);
    }

    //    @Synchronized("modifyEntityListLock")
    private void updatePlayerInfo(ClientConnection clientPlayer) {
        toChange = false;

        Map<UUID, NewGsonGameObject> newGameObjects = new HashMap<>();
        ClientGameData clientGameData = getClientData(clientPlayer);

        if (clientGameData == null) throw new NullPointerException("ClientGameData is null ");


        Map<@NonNull UUID, @NonNull Pair<@NonNull GameObject, Integer>> cachedObjectMap = copyGameObjectsAsMap();

        cachedObjectMap.forEach((uuid, gameObjectIntegerPair) -> {

            boolean changed = clientGameData.getObjectCacheList().add(uuid) ||
                    gameObjectIntegerPair.getRight() != gameObjectIntegerPair.getKey().hashCode();


            if (changed && uuid == clientGameData.getEntityPlayer().getUniqueId())
                changed = isPlayerDifferent(clientGameData.getEntityPlayer(), (EntityPlayer) gameObjectIntegerPair.getLeft(), 0 ,0)
                        || clientGameData.isChanged();

            if (changed)
            {
                newGameObjects.put(uuid, new NewGsonGameObject(gameObjectIntegerPair.getKey()));
            }
        });

        new HashSet<>(clientGameData.getObjectCacheList()).forEach(uuid -> {
            if (!cachedObjectMap.containsKey(uuid)) {
                newGameObjects.put(uuid, NewGsonGameObject.nullObject());
                clientGameData.getObjectCacheList().remove(uuid);
            }
        });

        SendObjectsList sendObjectsList = new SendObjectsList(
                newGameObjects, clientGameData.isChanged(), clientGameData.getEntityPlayer());



        if (!newGameObjects.isEmpty())
            clientPlayer.sendObject(sendObjectsList);

        clientGameData.setChanged(false);
    }

    public void onEntityUpdate() {
        boolean oldChange = toChange;
        StopWatch stopWatch = StopWatch.createStarted();
        List<ClientConnection> clientConnections = new ArrayList<>(clientGameDataMap.keySet());


        clientConnections.parallelStream().forEach(this::updatePlayerInfo);


        stopWatch.stop();
        if (oldChange)
            StaticHandler.getCore().getLogger().debug("Updating players took {}", stopWatch.getTime(TimeUnit.MILLISECONDS));
        toChange = false;

    }

    /**
     * Individually handle every updated player
     *
     * @param entityPlayer
     */
    @Override
    protected void playerUpdate(EntityPlayer entityPlayer) {

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

        server.getServerGameHandler().getEntityHandler().onEntityUpdate();
    }

    public void handleClientRespond(ClientConnection clientPlayer, SendPlayerInfoPacket infoPacket) {
        EntityPlayer oldPlayer = server.getServerGameHandler().getEntityHandler().getClientData(clientPlayer).getEntityPlayer();

        EntityPlayer packetPlayer = infoPacket.getPlayerObject();

        double velXClamp = GameMathUtil.clamp(packetPlayer.getVelX(), -EntityPlayer.MAX_VELOCITY, EntityPlayer.MAX_VELOCITY);
        double velYClamp = GameMathUtil.clamp(packetPlayer.getVelY(), -EntityPlayer.MAX_VELOCITY, EntityPlayer.MAX_VELOCITY);

        EntityPlayer copyNewPlayer = new EntityPlayer(
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
                isPlayerDifferent(oldPlayer, copyNewPlayer, velXClamp, velYClamp)
        ) {
            clientGameDataMap.get(clientPlayer).setChanged(true);
            StaticHandler.getCore().getLogger().debug("Client player is changed");
        }


        updatePlayerObject(clientPlayer, copyNewPlayer);
    }

    private static boolean isPlayerDifferent(EntityPlayer oldPlayer, EntityPlayer copyVel, double velX, double velY) {
        return GameMathUtil.absDif(copyVel.getX(), oldPlayer.getX() + (float) velX) > CommonUtil.PLAYER_COORD_DIF ||
                GameMathUtil.absDif(copyVel.getY(), oldPlayer.getY() + (float) velY) > CommonUtil.PLAYER_COORD_DIF ||
                GameMathUtil.absDif(copyVel.getVelX(), oldPlayer.getVelX()) > CommonUtil.PLAYER_VEL_DIF ||
                GameMathUtil.absDif(copyVel.getVelY(), oldPlayer.getVelY()) > CommonUtil.PLAYER_VEL_DIF;
    }
}
