package com.github.fernthedev.game.server;

import com.github.fernthedev.GameMathUtil;
import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.lightchat.core.ColorCode;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.packets.object_updates.SetCoin;
import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class NewServerEntityRegistry extends INewEntityRegistry {

    @Setter
    private GameServer server;

    @Getter
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

        removeEntityObject(connection.getUuid());
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
    public void tick() {
        super.tick();

        if (!clientGameDataMap.isEmpty())
            finishEntityUpdate();
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
                                    StaticHandler.getCore().getLogger().debug("Collision checking! COIN");
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

    @Override
    protected ExecutorService getExecutorService() {
        return server.getServer().getExecutorService();
    }

    public void finishEntityUpdate() {
        server.getServerGameHandler().getPlayerPollUpdateThread().getSendUpdate().set(true);
    }

    public void removeRespawnAllPlayers() {
        gameObjects.clear();
        clientGameDataMap.forEach((connection, clientGameData) -> {
            clientGameData.getEntityPlayer().setHealth(100);
            addEntityObject(clientGameData.getEntityPlayer());
        });

        server.getServerGameHandler().getEntityHandler().finishEntityUpdate();
    }

    public void handleClientRespond(ClientConnection clientPlayer, SendToServerPlayerInfoPacket infoPacket) {
        ClientGameData clientData = server.getServerGameHandler().getEntityHandler().getClientData(clientPlayer);

        if (clientData == null) return;

        EntityPlayer oldPlayer = clientData.getEntityPlayer();

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
                oldPlayer.getCoin(),
                oldPlayer.getName()
        );

        ClientGameData clientGameData = clientGameDataMap.get(clientPlayer);
        clientGameData.setClientSidePlayerHashCode(infoPacket.getPlayerObject().hashCode());
        clientGameData.getObjectCacheList().clear();
        clientGameData.getObjectCacheList().putAll(infoPacket.getEntitiesHashCodeMap());

        if (
                EntityPlayer.isPlayerDifferent(oldPlayer, copyNewPlayer, velXClamp, velYClamp)
        ) {

            StaticHandler.getCore().getLogger().debug("Client player is changed");
        }


        updatePlayerObject(clientPlayer, copyNewPlayer);
    }

}
