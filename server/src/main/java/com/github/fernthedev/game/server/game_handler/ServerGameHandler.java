package com.github.fernthedev.game.server.game_handler;

import com.github.fernthedev.TickRunnable;
import com.github.fernthedev.fernutils.thread.ThreadUtils;
import com.github.fernthedev.game.server.GameServer;
import com.github.fernthedev.game.server.NewServerEntityRegistry;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.server.Server;
import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class ServerGameHandler extends TickRunnable {

    private final Server server;

    @Getter
    private final Spawn spawn;

    @Getter
    private final PlayerPollUpdateThread playerPollUpdateThread;

//    @Getter
//    private static GameMechanics gameMechanics;

    @Getter
    @Setter
    private boolean started = false;

    @Getter
    private final NewServerEntityRegistry entityHandler;

    public ServerGameHandler(GameServer server, NewServerEntityRegistry entityHandler) {
        spawn = new Spawn(server);
        this.server = server.getServer();
//        gameMechanics = new GameMechanics();
        this.entityHandler = entityHandler;
        playerPollUpdateThread = new PlayerPollUpdateThread(server);
    }

    @Override
    public void run() {

        playerPollUpdateThread.start();

        Thread entityThread = new Thread(entityHandler);
        entityThread.start();
        StaticHandler.getCore().getLogger().debug(entityThread + " is from " + entityHandler);

//        mechanicThread = new Thread(gameMechanics);
//        mechanicThread.start();
//        System.out.println(mechanicThread + " is from " + gameMechanics);

        super.run();
    }


    public void tick() {

//        long before = System.nanoTime();
        spawn.tick();

        if (started && entityHandler.copyGameObjectsAsList().parallelStream().noneMatch(gameObject -> gameObject instanceof EntityPlayer)) {

            started = false;
            if (!server.getPlayerHandler().getChannelMap().isEmpty()) {
                ThreadUtils.runForLoopAsync(new ArrayList<>(server.getPlayerHandler().getChannelMap().values()), connection -> {
                    try {
                        connection.sendObject(new GameOverPacket()).sync();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                    connection.close();
                }).runThreads(server.getExecutorService());

                entityHandler.getGameObjects().clear();
            }

        }
        //  System.out.println( (System.nanoTime() - before) / 1000000 + "Thing1");

        //before = System.nanoTime();
        //    gameMechanics.tick();
        // System.out.println( (System.nanoTime() - before) / 1000000 + "Thing2");


        //  before = System.nanoTime();
        //entityHandler.tick();
        // System.out.println( (System.nanoTime() - before) / 1000000 + "Thing3");

//        before = System.nanoTime();


        //System.out.println( (System.nanoTime() - before) / 1000000 + "Thing4");

    }
}
