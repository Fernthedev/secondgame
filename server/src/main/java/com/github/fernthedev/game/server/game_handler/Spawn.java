package com.github.fernthedev.game.server.game_handler;


import com.github.fernthedev.game.server.GameServer;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.packets.LevelUp;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Spawn {

    private int scoreKeep = 0;
    private final Random r = UniversalHandler.RANDOM;
    private int timer;
    private int nexttimer;

    private static int levels;

    private final GameServer server;

    public void tick() {

//        if(true) return;
        if (!server.getServerGameHandler().isStarted()) return;

//        System.out.println(server.getServerGameHandler().getEntityHandler().isClientDataEmpty() + " is empty");
        if ((server.getServerGameHandler().getEntityHandler().isClientDataEmpty())) {
            if (!server.getServerGameHandler().getEntityHandler().getGameObjects().isEmpty())
                server.getServerGameHandler().getEntityHandler().getGameObjects().clear();
            return;
        }
        scoreKeep++;
        //coinspawn = hud.getScore() + r.nextInt(512);
        //if (hud.getScore() == coinspawn) {
        ///      handler.addObject(new GameObject(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.Coin, handler));
        //  }
//        scoreKeep++;
        int coinspawn = scoreKeep + r.nextInt(512);

        if (scoreKeep == coinspawn && (!server.getServer().getPlayerHandler().getChannelMap().isEmpty())) {
            server.getServerGameHandler().getEntityHandler().addEntityObject(new Coin(r.nextInt(UniversalHandler.WIDTH - 50) + 1, r.nextInt(UniversalHandler.HEIGHT - 50) + 1, EntityID.COIN));
        }

        if (scoreKeep >= 250) {
//            System.out.println("Spawner tick 250");
            //hud.setLevel(hud.getLevel() + 1);
            timer++;
            scoreKeep = 0;
            StaticHandler.getCore().getLogger().info("RESETTING SCOREKEEP");

            if (!server.getServer().getPlayerHandler().getChannelMap().isEmpty()) {
                int mob = r.nextInt(4);
                if (mob == 0) mob++;

                // System.out.println(mob);
                if (mob == 1) {
                    server.getServerGameHandler().getEntityHandler().addEntityObject(
                            new BasicEnemy(r.nextInt(UniversalHandler.WIDTH - 50),
                                    r.nextInt(UniversalHandler.HEIGHT - 50),
                                    EntityID.ENEMY));

                    // handler.addObject(new BasicEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.BasicEnemey, handler));
                }

                if (mob == 2) {
                    server.getServerGameHandler().getEntityHandler().addEntityObject(
                            new FastEnemy(r.nextInt(UniversalHandler.WIDTH - 50),
                                    r.nextInt(UniversalHandler.HEIGHT - 50),
                                    EntityID.ENEMY));

                    //       handler.addObject(new FastEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.FastEnemy, handler));
                }

                if (mob == 3) {
                    server.getServerGameHandler().getEntityHandler().addEntityObject(
                            new SmartEnemy(r.nextInt(UniversalHandler.WIDTH - 50),
                                    r.nextInt(UniversalHandler.HEIGHT - 50),
                                    EntityID.ENEMY,
                                    new ArrayList<>(server.getServerGameHandler().getEntityHandler().getGameObjects().values())
                                            .parallelStream().filter(gameObject -> gameObject instanceof EntityPlayer)
                                            .findAny()
                                            .map(gameObject -> (EntityPlayer) gameObject)
                                            .get()));
                }

           /* if (mob == 3) {
                //Server.sendObjectToAllPlayers(new SendGameObject());
                ServerGameHandler.getEntityHandler().addEntityObject(new SmartEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.SmartEnemy, GameObject.entities));
           //     handler.addObject(new SmartEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.SmartEnemy, handler));
            }*/
            }
        }


        if (timer >= nexttimer) {
            nexttimer = r.nextInt(15) + 7;
            timer = 0;


//            Map<UUID, Pair<GameObject, Long>> players = new HashMap<>();
//
//            server.getServerGameHandler().getEntityHandler().getGameObjects().values()
//                    .parallelStream().filter(gameObject -> gameObject.getKey() instanceof EntityPlayer)
//                    .forEach(gameObject -> players.put(gameObject.getKey().getUniqueId(), new ImmutablePair<>(gameObject.getLeft(), gameObject.getRight())));

//            List<GameObject> gameObjects = new ArrayList<>(EntityHandler.getGameObjects());
//            for(GameObject gameObject : gameObjects) {
//                if(gameObject.entityId == EntityID.Player) players.add(gameObject);
//            }

//            gameObjects = new ArrayList<>(players);


            server.getServerGameHandler().getEntityHandler().removeRespawnAllPlayers();


            levels++;
            // Server.sendObjectToAllPlayers(new SendObjectsList(gameObjects));

            StaticHandler.getCore().getLogger().debug("New objects: {}",
                    server.getEntityRegistry().getGameObjects().values().parallelStream()
                            .filter(gameObjectLongPair -> !(gameObjectLongPair instanceof EntityPlayer))
                            .collect(Collectors.toList()));

            StaticHandler.getCore().getLogger().info("Doing levels " + new LevelUp(levels));
            server.getServer().sendObjectToAllPlayers(new LevelUp(levels));
            scoreKeep = 200;

        }
    }
}
