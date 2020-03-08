package com.github.fernthedev.game.server.game_handler;

import com.github.fernthedev.TickRunnable;
import com.github.fernthedev.game.server.GameServer;
import com.github.fernthedev.game.server.NewServerEntityRegistry;
import lombok.Getter;

public class ServerGameHandler extends TickRunnable {

    @Getter
    private Spawn spawn;

//    @Getter
//    private static GameMechanics gameMechanics;

    @Getter
    private NewServerEntityRegistry entityHandler;

    private Thread entityThread;

    public ServerGameHandler(GameServer server, NewServerEntityRegistry entityHandler) {
        spawn = new Spawn(server);
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
