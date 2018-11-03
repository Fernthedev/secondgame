package com.github.fernthedev.server.gameHandler;

import com.github.fernthedev.server.Server;
import com.github.fernthedev.universal.UniversalHandler;

public class ServerGameHandler implements Runnable {

    private final Server server;
    private static Spawn spawn;
    private static GameMechanics gameMechanics;
    private static EntityHandler entityHandler;

    private static Thread entityThread,mechanicThread;

    static int score = 0;

    public ServerGameHandler(Server server,EntityHandler entityHandler) {
        this.server = server;
        spawn = new Spawn();
        gameMechanics = new GameMechanics();
        ServerGameHandler.entityHandler = entityHandler;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        entityThread = new Thread(entityHandler);
        entityThread.start();
        System.out.println(entityThread + " is from " + entityHandler);
        UniversalHandler.threads.add(entityThread);

        mechanicThread = new Thread(gameMechanics);
        mechanicThread.start();
        System.out.println(mechanicThread + " is from " + gameMechanics);
        UniversalHandler.threads.add(mechanicThread);

        System.out.println(UniversalHandler.threads.size() + " threads started for game handling");

        while(UniversalHandler.running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                //System.out.println(UniversalHandler.running);
                //updates++;
                delta--;
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
                //updates = 0;
            }

            try {Thread.sleep(UniversalHandler.tickWait);} catch(Exception e) {}
        }


    }


    private void tick() {
        long before = System.nanoTime();
        spawn.tick();
      //  System.out.println( (System.nanoTime() - before) / 1000000 + "Thing1");

        //before = System.nanoTime();
    //    gameMechanics.tick();
       // System.out.println( (System.nanoTime() - before) / 1000000 + "Thing2");

        score++;

      //  before = System.nanoTime();
        //entityHandler.tick();
       // System.out.println( (System.nanoTime() - before) / 1000000 + "Thing3");

        before = System.nanoTime();


        //System.out.println( (System.nanoTime() - before) / 1000000 + "Thing4");

    }

    public Server getServer() {
        return server;
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public static EntityHandler getEntityHandler() {
        return entityHandler;
    }

    public GameMechanics getGameMechanics() {
        return gameMechanics;
    }
}
