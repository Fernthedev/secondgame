package com.github.fernthedev.server.gameHandler;

import com.github.fernthedev.packets.LevelUp;
import com.github.fernthedev.server.Server;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.BasicEnemy;
import com.github.fernthedev.universal.entity.FastEnemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Spawn {

    private int scoreKeep = 0;
    private final Random r = new Random();
    private int timer;
    private int nexttimer;

    private static int levels;

    public void tick() {
     //   scoreKeep++;
        //coinspawn = hud.getScore() + r.nextInt(512);
       //if (hud.getScore() == coinspawn) {
      ///      handler.addObject(new GameObject(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), ID.Coin, handler));
      //  }
        scoreKeep++;
        int coinspawn = ServerGameHandler.score + r.nextInt(512);
        if (ServerGameHandler.score == coinspawn && (!EntityHandler.playerMap.isEmpty())) {
            ServerGameHandler.getEntityHandler().addEntityObject(ServerGameObject.getObjectType(new ServerGameObject(r.nextInt(Server.WIDTH - 50), r.nextInt(Server.HEIGHT - 50), ID.Coin, GameObject.entities)));
        }

        if (scoreKeep >= 250) {
            //hud.setLevel(hud.getLevel() + 1);
            timer++;
            scoreKeep = 0;

            if(!EntityHandler.playerMap.isEmpty()) {
                int mob = r.nextInt(4);
                if (mob == 0) mob++;

               // System.out.println(mob);
                if (mob == 1) {
                    ServerGameHandler.getEntityHandler().addEntityObject(new BasicEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), ID.BasicEnemey, GameObject.entities));

                    // handler.addObject(new BasicEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), ID.BasicEnemey, handler));
                }

                if (mob == 2) {
                    ServerGameHandler.getEntityHandler().addEntityObject(new FastEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), ID.FastEnemy, GameObject.entities));

                    //       handler.addObject(new FastEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), ID.FastEnemy, handler));
                }

           /* if (mob == 3) {
                //Server.sendObjectToAllPlayers(new SendGameObject());
                ServerGameHandler.getEntityHandler().addEntityObject(new SmartEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), ID.SmartEnemy, GameObject.entities));
           //     handler.addObject(new SmartEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), ID.SmartEnemy, handler));
            }*/
            }else{
                EntityHandler.gameObjects.clear();
                EntityHandler.playerClientMap.clear();
                EntityHandler.playerMap.clear();
            }
        }


        if (timer >= nexttimer) {
            nexttimer = r.nextInt(15) + 7;
            timer = 0;

            List<GameObject> players = new ArrayList<>();

            List<GameObject> gameObjects = new ArrayList<>(EntityHandler.gameObjects);
            for(GameObject gameObject : gameObjects) {
                if(gameObject.id == ID.Player) players.add(gameObject);
            }

            gameObjects = new ArrayList<>(players);

            EntityHandler.gameObjects = new Vector<>(gameObjects);

            levels++;
           // Server.sendObjectToAllPlayers(new SendObjectsList(gameObjects));
            Server.sendObjectToAllPlayers(new LevelUp(levels));
            scoreKeep = 250;

        }
    }



}
