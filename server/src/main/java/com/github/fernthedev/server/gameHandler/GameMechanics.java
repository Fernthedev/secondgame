package com.github.fernthedev.server.gameHandler;

import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.packets.ObjectUpdates.SetCoin;
import com.github.fernthedev.server.ClientPlayer;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.UniversalPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameMechanics implements Runnable {

    private Rectangle getBounds(int x, int y) {
        return new Rectangle(x, y,32,32);
    }

    public void tick() {

        ArrayList<GameObject> objects = new ArrayList<>(EntityHandler.gameObjects);

        for(GameObject gameObject : objects) {
            if(gameObject instanceof UniversalPlayer) {
                collisionCheck((UniversalPlayer) gameObject);

            }
        }

        /*for(NetPlayer gameObject : EntityHandler.playerList) {
            ClientPlayer clientPlayer = ClientPlayer.getPlayerFromObject(gameObject.getObjectID());
            if(clientPlayer != null)
            collisionCheck(clientPlayer.getPlayerObject());
        }*/
    }

    private synchronized void collisionCheck(UniversalPlayer playerObject) {

        HashMap<ClientPlayer, UniversalPlayer> playerUniversalPlayerHashMap = new HashMap<>(EntityHandler.playerClientMap);
        List<GameObject> objects = new ArrayList<>(EntityHandler.gameObjects);

        for (ClientPlayer clientPlayer : playerUniversalPlayerHashMap.keySet()) {
            for (GameObject tempObject : objects) {


                if (tempObject.getId() == ID.BasicEnemey || tempObject.getId() == ID.FastEnemy || tempObject.getId() == ID.SmartEnemy) {
                    if (playerObject.getBounds().intersects(tempObject.getBounds())) {
                        //COLLISION CODE
                        playerObject.setHealth(playerObject.getHealth() - 2);

                        ServerGameHandler.getEntityHandler().updatePlayerObject(clientPlayer, playerObject);
                    }
                }

                if (tempObject.getId() == ID.Coin) {
                    if (playerObject.getBounds().intersects(tempObject.getBounds())) {

                        ServerGameHandler.getEntityHandler().removeEntityObject(tempObject);

                        if (clientPlayer != null)
                            clientPlayer.sendObject(new SetCoin(playerObject.getCoin() + 1));
                    }
                }

                if(playerObject.getHealth() <= 0 && clientPlayer != null) {
                    clientPlayer.sendObject(new GameOverPacket());
                    clientPlayer.close();
                }
            }
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
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
}
