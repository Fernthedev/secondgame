package io.github.fernthedev.secondgame.main.netty.client;


import com.github.fernthedev.packets.*;
import com.github.fernthedev.packets.ObjectUpdates.SendGameObject;
import com.github.fernthedev.packets.ObjectUpdates.SendObjectsList;
import com.github.fernthedev.packets.ObjectUpdates.SetCoin;
import com.github.fernthedev.packets.PlayerUpdates.*;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.MenuParticle;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import com.google.gson.reflect.TypeToken;
import io.github.fernthedev.secondgame.main.ClientObject;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.HUD;
import io.github.fernthedev.secondgame.main.entities.Player;
import io.github.fernthedev.secondgame.main.netty.client.netty.GsonObject;

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

            Game.mainPlayer = new Player(playerPacket.getUniversalPlayer(),Game.getHandler(),Game.getHud());

            UniversalHandler.getThingHandler().updatePlayerObject(null,Game.mainPlayer);

           // Game.getHandler().removeObject(Game.mainPlayer);
          //  Game.mainPlayer = new Player(playerPacket.getKeepPlayer(),Game.getHandler(),Game.getHud(),playerPacket.getKeepPlayer().getColor());

        } else if (p instanceof SendPlayerInfoPacket) {
            SendPlayerInfoPacket info = (SendPlayerInfoPacket) p;
            UniversalPlayer universalPlayer = info.getPlayerObject();


            Game.getHandler().setPlayerInfo(universalPlayer);
            if(info.getPlayerObject().getObjectID() == Game.mainPlayer.getObjectID()) {
                HUD.HEALTH = info.getPlayerObject().getHealth();
            }
        }else if (p instanceof SetCoin) {
            SetCoin coins = (SetCoin) p;

            System.out.println("Coin one up");

            Game.getHud().setCoin(coins.getCoins());
        } else if (p instanceof SendObjectsList) {
            SendObjectsList list = (SendObjectsList) p;

            List<GameObject> gameObjectsNew = new ArrayList<>();

            UniversalPlayer universalPlayer = null;

            Type listType = new TypeToken<ArrayList<GsonObject>>(){}.getType();

            List<GameObject> gameObjects = UniversalHandler.gson.fromJson(list.getObjectList(), listType);


            for(GameObject gameObject : gameObjects) {
                gameObjectsNew.add(ClientObject.getObjectType(gameObject));
                if(Game.mainPlayer != null)
                if(gameObject.getObjectID() == Game.mainPlayer.getObjectID() && gameObject instanceof UniversalPlayer) universalPlayer = (UniversalPlayer) gameObject;
               // System.out.println(ClientObject.getObjectType(gameObject));
            }

            UniversalHandler.getThingHandler().setGameObjects(gameObjectsNew);


            if(universalPlayer != null) {
                Player player = new Player(universalPlayer,Game.getHandler(),Game.getHud());

                UniversalHandler.getThingHandler().removeEntityObject(universalPlayer);
                UniversalHandler.getThingHandler().addEntityObject(player);

                UniversalHandler.getThingHandler().updatePlayerObject(null,universalPlayer);
                Game.mainPlayer = player;
            }
        } else if (p instanceof GetToSendInfo) {
            GetToSendInfo info = (GetToSendInfo) p;

            Player player = new Player(info.getKeepPlayer(),info.getNewPlayer(),Game.getHandler(),Game.getHud());

            UniversalHandler.getThingHandler().updatePlayerObject(null,player);
            Game.mainPlayer = player;

            Game.sendPacket(new SendPlayerInfoPacket(new UniversalPlayer(Game.mainPlayer)));
        } else if (p instanceof GameOverPacket) {
            Game.mainPlayer.setHealth(0);

            HUD.HEALTH = 100;
            Game.gameState = Game.STATE.End;
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
