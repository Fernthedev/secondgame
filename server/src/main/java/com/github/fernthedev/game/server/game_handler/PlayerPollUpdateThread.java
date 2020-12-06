package com.github.fernthedev.game.server.game_handler;

import com.github.fernthedev.game.server.ClientGameData;
import com.github.fernthedev.game.server.GameServer;
import com.github.fernthedev.game.server.NewServerEntityRegistry;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.packets.object_updates.SendObjectsList;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.github.fernthedev.universal.entity.NewGsonGameObject;
import lombok.Getter;
import org.apache.commons.lang3.time.StopWatch;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This thread watches for updates
 * on the server and sends player updates
 *
 */
public class PlayerPollUpdateThread extends Thread {
    private final GameServer gameServer;

    @Getter
    private final AtomicBoolean sendUpdate = new AtomicBoolean(false);

    public PlayerPollUpdateThread(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (gameServer.getServer().isRunning()) {

            if (sendUpdate.get()) {
                sendUpdate.set(false);

                update();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private void update() {
        NewServerEntityRegistry entityHandler = gameServer.getServerGameHandler().getEntityHandler();

        int oldHashCode = entityHandler.getGameObjects().hashCode();

        StopWatch stopWatch = StopWatch.createStarted();
        List<ClientConnection> clientConnections = new ArrayList<>(entityHandler.getClientGameDataMap().keySet());

        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();

        for (ClientConnection clientConnection : clientConnections) {
            completableFutures.add(CompletableFuture.runAsync(() -> updatePlayerInfo(clientConnection), gameServer.getServer().getExecutorService()));
        }

        try {
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).thenRun(() -> {

                stopWatch.stop();
                if (oldHashCode != entityHandler.getGameObjects().hashCode())
                    StaticHandler.getCore().getLogger().debug("Updating players took {}", stopWatch.getTime(TimeUnit.MILLISECONDS));


            }).get(30, TimeUnit.SECONDS);


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            gameServer.getServer().getLogger().error("An update took more than 30 seconds to complete", e);
        }
    }


    private void updatePlayerInfo(ClientConnection clientPlayer) {
        Map<UUID, NewGsonGameObject> newGameObjects = new HashMap<>();
        ClientGameData clientGameData = gameServer.getEntityRegistry().getClientData(clientPlayer);

        if (clientGameData == null) throw new NullPointerException("ClientGameData is null ");


        Map<UUID, GameObject> cachedObjectMap = gameServer.getEntityRegistry().copyGameObjectsAsMap();

        cachedObjectMap.forEach((uuid, gameObject) -> {

            boolean changed = !clientGameData.getObjectCacheList().containsKey(uuid) || (
                    clientGameData.getObjectCacheList().get(uuid) != null &&
                            clientGameData.getObjectCacheList().get(uuid) != gameObject.hashCode()
            );


            if (changed && uuid == clientGameData.getEntityPlayer().getUniqueId()) {
                changed = clientGameData.getClientSidePlayerHashCode() != clientGameData.getEntityPlayer().hashCode() ||
                        EntityPlayer.isPlayerDifferent(clientGameData.getEntityPlayer(), (EntityPlayer) gameObject, 0, 0);
            }

            if (changed)
            {
                newGameObjects.put(uuid, new NewGsonGameObject(gameObject));
            }
        });

        try {
            new HashMap<>(clientGameData.getObjectCacheList()).forEach((uuid, hashCode) -> {
                if (!cachedObjectMap.containsKey(uuid)) {
                    newGameObjects.put(uuid, NewGsonGameObject.nullObject());
                }
            });
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
            return;
        }

        SendObjectsList sendObjectsList = new SendObjectsList(
                newGameObjects, clientGameData.getEntityPlayer());

//      TODO:  if (newGameObjects.size() > 0)
//            StaticHandler.getCore().getLogger().info("Changed objects: {}", newGameObjects.values().parallelStream().map(NewGsonGameObject::getClazz ).collect(Collectors.toList()));

        if (!newGameObjects.isEmpty() || clientGameData.getClientSidePlayerHashCode() != clientGameData.getEntityPlayer().hashCode())
            clientPlayer.sendObject(sendObjectsList);

        clientGameData.setClientSidePlayerHashCode(clientGameData.getEntityPlayer().hashCode());
    }
}
