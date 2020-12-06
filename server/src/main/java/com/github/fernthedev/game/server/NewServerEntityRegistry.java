package com.github.fernthedev.game.server;

import com.github.fernthedev.CommonUtil;
import com.github.fernthedev.GameMathUtil;
import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.lightchat.core.ColorCode;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.packets.object_updates.SendObjectsList;
import com.github.fernthedev.packets.object_updates.SetCoin;
import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.github.fernthedev.universal.entity.NewGsonGameObject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.StopWatch;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NewServerEntityRegistry extends INewEntityRegistry {

    @Setter
    private GameServer server;

    private final Map<ClientConnection, ClientGameData> clientGameDataMap = Collections.synchronizedMap(new HashMap<>());


    public boolean isClientDataEmpty() {
        return clientGameDataMap.isEmpty();
    }

    public ClientGameData getClientData(ClientConnection connection) {
        return clientGameDataMap.getOrDefault(connection, null);
    }

    public void addClientData(ClientConnection connection, @NonNull ClientGameData clientGameData, int entityHashCode) {
        clientGameData.setClientSidePlayerHashCode(entityHashCode);
        clientGameDataMap.put(connection, clientGameData);
        addEntityObject(clientGameData.getEntityPlayer());
    }

    public void removeClientData(ClientConnection connection) {
        clientGameDataMap.remove(connection);
    }


    public void updatePlayerObject(@NonNull ClientConnection clientPlayerE, EntityPlayer universalPlayer) {
        if (universalPlayer == null) throw new NullPointerException();

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
    }

    @Override
    public void removeEntityObject(GameObject gameObject) {
        super.removeEntityObject(gameObject);

        StaticHandler.getCore().getLogger().debug("Removed {}", gameObject.entityId);
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
                                    connection.sendObject(new SendToServerPlayerInfoPacket(universalPlayer, null));


                                }


                                if (tempObject.getEntityId() == EntityID.COIN) {
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
        Map<UUID, NewGsonGameObject> newGameObjects = new HashMap<>();
        ClientGameData clientGameData = getClientData(clientPlayer);

        if (clientGameData == null) throw new NullPointerException("ClientGameData is null ");


        Map<UUID, GameObject> cachedObjectMap = copyGameObjectsAsMap();

        cachedObjectMap.forEach((uuid, gameObject) -> {

            boolean changed = !clientGameData.getObjectCacheList().containsKey(uuid) || (
                        clientGameData.getObjectCacheList().get(uuid) != null &&
                            clientGameData.getObjectCacheList().get(uuid) != gameObject.hashCode()
                    );


            if (changed && uuid == clientGameData.getEntityPlayer().getUniqueId()) {
                changed = clientGameData.getClientSidePlayerHashCode() != clientGameData.getEntityPlayer().hashCode() ||
                        isPlayerDifferent(clientGameData.getEntityPlayer(), (EntityPlayer) gameObject, 0, 0);
            }

            if (changed)
            {
                newGameObjects.put(uuid, new NewGsonGameObject(gameObject));
            }
        });

        new HashMap<>(clientGameData.getObjectCacheList()).forEach((uuid, hashCode) -> {
            if (!cachedObjectMap.containsKey(uuid)) {
                newGameObjects.put(uuid, NewGsonGameObject.nullObject());
            }
        });

        SendObjectsList sendObjectsList = new SendObjectsList(
                newGameObjects, clientGameData.getEntityPlayer());

        if (newGameObjects.size() > 0)
            StaticHandler.getCore().getLogger().info("Changed objects: {}", newGameObjects.values().parallelStream().map(NewGsonGameObject::getClazz ).collect(Collectors.toList()));

        if (!newGameObjects.isEmpty() || clientGameData.getClientSidePlayerHashCode() != clientGameData.getEntityPlayer().hashCode())
            clientPlayer.sendObject(sendObjectsList);

        clientGameData.setClientSidePlayerHashCode(clientGameData.getEntityPlayer().hashCode());
    }

    public void onEntityUpdate() {
        int oldHashCode = gameObjects.hashCode();
        StopWatch stopWatch = StopWatch.createStarted();
        List<ClientConnection> clientConnections = new ArrayList<>(clientGameDataMap.keySet());


        clientConnections.parallelStream().forEach(this::updatePlayerInfo);


        stopWatch.stop();
        if (oldHashCode != gameObjects.hashCode())
            StaticHandler.getCore().getLogger().debug("Updating players took {}", stopWatch.getTime(TimeUnit.MILLISECONDS));
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
        Map<@NonNull UUID, @NonNull GameObject> oldMap = new HashMap<>(server.getServerGameHandler().getEntityHandler().getGameObjects());

        oldMap.keySet().parallelStream()
                .filter(uuid -> !(oldMap.get(uuid) instanceof EntityPlayer))
                .forEach(uuid -> removeEntityObject(oldMap.get(uuid)));

        clientGameDataMap.forEach((connection, clientGameData) -> {
            clientGameData.getEntityPlayer().setHealth(100);
            addEntityObject(clientGameData.getEntityPlayer());
        });

        server.getServerGameHandler().getEntityHandler().onEntityUpdate();
    }

    public void handleClientRespond(ClientConnection clientPlayer, SendToServerPlayerInfoPacket infoPacket) {
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

        ClientGameData clientGameData = clientGameDataMap.get(clientPlayer);
        clientGameData.setClientSidePlayerHashCode(infoPacket.getPlayerObject().hashCode());
        clientGameData.getObjectCacheList().clear();
        clientGameData.getObjectCacheList().putAll(infoPacket.getEntitiesHashCodeMap());

        if (
                isPlayerDifferent(oldPlayer, copyNewPlayer, velXClamp, velYClamp)
        ) {

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
