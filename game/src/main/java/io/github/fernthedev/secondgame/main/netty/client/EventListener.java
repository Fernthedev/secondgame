package io.github.fernthedev.secondgame.main.netty.client;


import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.packets.ObjectUpdates.SendGameObject;
import com.github.fernthedev.packets.ObjectUpdates.SendObjectsList;
import com.github.fernthedev.packets.ObjectUpdates.SetCoin;
import com.github.fernthedev.packets.PingPacket;
import com.github.fernthedev.packets.PlayerUpdates.GetToSendInfo;
import com.github.fernthedev.packets.PlayerUpdates.SendPlayerInfoPacket;
import com.github.fernthedev.packets.PlayerUpdates.SetCurrentPlayer;
import com.github.fernthedev.packets.PongPacket;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.MenuParticle;
import com.github.fernthedev.universal.entity.Trail;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.google.gson.reflect.TypeToken;
import io.github.fernthedev.secondgame.main.ClientObject;
import io.github.fernthedev.secondgame.main.Game;
import com.github.fernthedev.universal.entity.GsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EventListener {

    EventListener(Client client) {
    }

    public void recieved(Object p) {
        if(p instanceof PingPacket) {
            //System.out.println("Ponged!");
            PingPacket packet = (PingPacket) p;

            long time = (System.nanoTime() - packet.getTime() ) / 1000000;

            System.out.println("Ping: " + TimeUnit.MILLISECONDS.convert(time,TimeUnit.NANOSECONDS) + " ms");
            Client.getClientThread().sendObject(new PongPacket());
        } else if (p instanceof SendGameObject) {
            SendGameObject gameObject = (SendGameObject) p;

            Game.getHandler().addObject(gameObject.getGameObject());
        } else if (p instanceof SetCurrentPlayer) {
            SetCurrentPlayer playerPacket = (SetCurrentPlayer) p;

            //System.out.println("Setting new player data");

            UniversalHandler.mainPlayer = new EntityPlayer(playerPacket.getUniversalPlayer());

            UniversalHandler.getThingHandler().updatePlayerObject(null,UniversalHandler.mainPlayer);

           // GAME.getHandler().removeObject(GAME.mainPlayer);
          //  GAME.mainPlayer = new UniversalPlayer(playerPacket.getKeepPlayer(),GAME.getHandler(),GAME.getHud(),playerPacket.getKeepPlayer().getColor());

        } else if (p instanceof SendPlayerInfoPacket) {
            SendPlayerInfoPacket info = (SendPlayerInfoPacket) p;
            EntityPlayer universalPlayer = info.getPlayerObject();


            Game.getHandler().setPlayerInfo(universalPlayer);
            if(info.getPlayerObject().getObjectID() == UniversalHandler.mainPlayer.getObjectID()) {
                UniversalHandler.mainPlayer.setHealth(info.getPlayerObject().getHealth());
            }
        }else if (p instanceof SetCoin) {
            SetCoin coins = (SetCoin) p;

            System.out.println("Coin one up");

            Game.getHud().setCoin(coins.getCoins());
        } else if (p instanceof SendObjectsList) {
            SendObjectsList list = (SendObjectsList) p;

            System.out.println("Updating object list " + list.getObjectList() + "\n");


            List<GameObject> objectsAsInstanceFromPacket = new ArrayList<>();

            EntityPlayer universalPlayer;

            Type listType = new TypeToken<ArrayList<GsonObject>>(){}.getType();

            List<GsonObject> gameObjects = UniversalHandler.gson.fromJson(list.getObjectList(), listType);

            //System.out.println(list.getObjectList());

            List<GameObject> finalGameObjects;


            List<GameObject> newObjects = new ArrayList<>();

            universalPlayer = list.getMainPlayer();

            for(GsonObject gameObject : gameObjects) {
                GameObject checkedObject = ClientObject.getObjectType(gameObject);

                objectsAsInstanceFromPacket.add(checkedObject);
            }




            List<GameObject> currentGameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
            for(GameObject gameObject : currentGameObjects) {
                if(gameObject instanceof Trail) {
                    newObjects.add(gameObject);
                }
            }

            finalGameObjects = new ArrayList<>(objectsAsInstanceFromPacket);

            finalGameObjects.addAll(newObjects);








            System.out.println("Updating player bc server asked us to " + universalPlayer);
            UniversalHandler.getThingHandler().updatePlayerObject(null,universalPlayer);
            UniversalHandler.mainPlayer = universalPlayer;

            UniversalHandler.getThingHandler().setGameObjects(finalGameObjects);
        } else if (p instanceof GetToSendInfo) {
            GetToSendInfo info = (GetToSendInfo) p;

            EntityPlayer player = new EntityPlayer(info.getKeepPlayer(),info.getNewPlayer());

            UniversalHandler.getThingHandler().updatePlayerObject(null,player);
            UniversalHandler.mainPlayer = player;

            Game.sendPacket(new SendPlayerInfoPacket(new EntityPlayer(UniversalHandler.mainPlayer)));
        } else if (p instanceof GameOverPacket) {
            UniversalHandler.mainPlayer.setHealth(0);

            UniversalHandler.mainPlayer.setHealth(100);
            Game.gameState = Game.STATE.END;
            Game.getHandler().clearEnemies();

            Random r = new Random();

            int amount = r.nextInt(15);
            if (amount < 10) amount = 10;
            for (int i = 0; i < amount; i++) {
                Game.getHandler().addObject(new MenuParticle(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT), ID.MenuParticle, GameObject.entities));
            }

            Client.getClientThread().disconnect();
        }
    }

}
