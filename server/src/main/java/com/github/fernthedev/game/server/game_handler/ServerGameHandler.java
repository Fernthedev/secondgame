package com.github.fernthedev.game.server.game_handler;

import com.github.fernthedev.TickRunnable;
import com.github.fernthedev.fernutils.thread.ThreadUtils;
import com.github.fernthedev.game.server.GameServer;
import com.github.fernthedev.game.server.NewServerEntityRegistry;
import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.server.Server;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class ServerGameHandler extends TickRunnable {

    private Server server;

    @Getter
    private Spawn spawn;

//    @Getter
//    private static GameMechanics gameMechanics;

    @Getter
    @Setter
    private boolean started = false;

    @Getter
    private NewServerEntityRegistry entityHandler;

    private Thread entityThread;

    public ServerGameHandler(GameServer server, NewServerEntityRegistry entityHandler) {
        spawn = new Spawn(server);
        this.server = server.getServer();
//        gameMechanics = new GameMechanics();
        this.entityHandler = entityHandler;
    }

    @Override
    public void run() {

        entityThread = new Thread(entityHandler);
        entityThread.start();
        System.out.println(entityThread + " is from " + entityHandler);

//        mechanicThread = new Thread(gameMechanics);
//        mechanicThread.start();
//        System.out.println(mechanicThread + " is from " + gameMechanics);

        super.run();
    }


    public void tick() {

//        long before = System.nanoTime();
        spawn.tick();

        if (entityHandler.copyGameObjectsAsList().parallelStream().noneMatch(gameObject -> gameObject instanceof EntityPlayer)) {

            started = false;
            ThreadUtils.runForLoopAsync(new ArrayList<>(server.getPlayerHandler().getChannelMap().values()), connection -> {
                try {
                    connection.sendObject(new GameOverPacket()).sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connection.close();
                return null;
            });


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
