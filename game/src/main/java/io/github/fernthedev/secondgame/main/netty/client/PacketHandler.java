package io.github.fernthedev.secondgame.main.netty.client;

import com.github.fernthedev.client.api.IPacketHandler;
import com.github.fernthedev.client.event.ServerConnectFinishEvent;
import com.github.fernthedev.client.event.ServerDisconnectEvent;
import com.github.fernthedev.core.api.event.api.EventHandler;
import com.github.fernthedev.core.api.event.api.Listener;
import com.github.fernthedev.core.packets.Packet;
import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.packets.LevelUp;
import com.github.fernthedev.packets.object_updates.*;
import com.github.fernthedev.packets.player_updates.SendPlayerInfoPacket;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.github.fernthedev.universal.entity.NewGsonGameObject;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.ui.screens.EndScreen;
import io.github.fernthedev.secondgame.main.ui.screens.MainMenu;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PacketHandler implements IPacketHandler, Listener {
    @Override
    public void handlePacket(Packet p) {
        if (p instanceof SendGameObject) {
            SendGameObject gameObject = (SendGameObject) p;

            Game.getStaticEntityRegistry().addEntityObject(gameObject.getGameObject());
        } else if (p instanceof SendPlayerInfoPacket) {
            SendPlayerInfoPacket info = (SendPlayerInfoPacket) p;
            EntityPlayer universalPlayer = info.getPlayerObject();


            Game.setMainPlayer(universalPlayer);

            Game.getStaticEntityRegistry().addEntityObject(Game.getMainPlayer());

            Game.getMainPlayer().setHealth(universalPlayer.getHealth());

            Game.getClient().sendObject(new SendPlayerInfoPacket(Game.getMainPlayer()));

        } else if (p instanceof SetCoin) {
            SetCoin coins = (SetCoin) p;

            System.out.println("Coin one up");

            Game.getHud().setCoin(coins.getCoins());
        } else if (p instanceof SendObjectsList) {
            SendObjectsList list = (SendObjectsList) p;

            System.out.println("Updating object list " + list.getObjectList() + "\n");


            //            Type listType = new TypeToken<ArrayList<GsonObject>>(){}.getType();

            Map<UUID, NewGsonGameObject> gameObjects = list.getObjectList();

            //System.out.println(list.getObjectList());

//            List<GameObject> finalGameObjects;


//            List<GameObject> newObjects = new ArrayList<>();

            EntityPlayer universalPlayer = list.getMainPlayer();

//            for(GameObject gameObject : gameObjects) {
//                GameObject checkedObject = ClientObject.getObjectType(gameObject);
//
//                objectsAsInstanceFromPacket.add(checkedObject);
//            }
//
//            List<UUID> trailList = Game.getStaticEntityRegistry().getGameObjects().values()
//                    .parallelStream().filter(gameObjectLongPair -> gameObjectLongPair.getKey() instanceof Trail)
//                    .map(gameObjectLongPair -> gameObjectLongPair.getKey().getUniqueId())
//                    .collect(Collectors.toList());
//
//            Map<UUID, Pair<GameObject, Long>> entityMap = new HashMap<>();
//
//            trailList.parallelStream().forEach(uuid -> entityMap.put(uuid, Game.getStaticEntityRegistry().getGameObjects().get(uuid)));
//
//            gameObjects.forEach((uuid, newGsonGameObject) -> {
//                try {
//                    entityMap.put(uuid, new ImmutablePair<>(newGsonGameObject.toGameObject(), System.nanoTime()));
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            Game.getStaticEntityRegistry().getGameObjects().clear();
//            Game.getStaticEntityRegistry().getGameObjects().putAll(entityMap);


          gameObjects.forEach((uuid, newGsonGameObject) -> {
                try {

                    if (newGsonGameObject == null) {
                        Game.getStaticEntityRegistry().getGameObjects().remove(uuid);
                    } else {
                        GameObject object = newGsonGameObject.toGameObject();
                        if (object == null) Game.getStaticEntityRegistry().getGameObjects().remove(uuid);
                        else Game.getStaticEntityRegistry().addEntityObject(object);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
             }
       });

//            List<GameObject> objectsAsInstanceFromPacket = gameObjects.parallelStream().map(newGsonGameObject -> {
//                try {
//                    return newGsonGameObject.toGameObject();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }).collect(Collectors.toList());




//            finalGameObjects = new ArrayList<>(Game.getNewClientEntityRegistry().getGameObjects().values());
//            finalGameObjects.retainAll(finalGameObjects.parallelStream()
//                    .filter(gameObject -> gameObject instanceof Trail)
//                    .collect(Collectors.toList()));
//            finalGameObjects.addAll(objectsAsInstanceFromPacket);



//            List<GameObject> currentGameObjects = new ArrayList<>(Game.getNewClientEntityRegistry().getGameObjects().values());
//            for(GameObject gameObject : currentGameObjects) {
//                if(gameObject instanceof Trail) {
//                    newObjects.add(gameObject);
//                }
//            }
//
//            finalGameObjects = new ArrayList<>(objectsAsInstanceFromPacket);
//
//            finalGameObjects.addAll(newObjects);




            System.out.println("Updating player because server asked us to " + universalPlayer);
            Game.getStaticEntityRegistry().addEntityObject(universalPlayer);
            Game.setMainPlayer(universalPlayer);

//            Map<UUID, GameObject> objectMap = new HashMap<>();
//            finalGameObjects.forEach(gameObject -> objectMap.put(gameObject.getUniqueId(), gameObject));

//            Game.getNewClientEntityRegistry().getGameObjects().clear();
//            Game.getNewClientEntityRegistry().getGameObjects().putAll(objectMap);

//            UniversalHandler.getThingHandler().setGameObjects(finalGameObjects);
        } else if (p instanceof AddObjectPacket) {
            AddObjectPacket packet = (AddObjectPacket) p;
            try {
                GameObject object = packet.getNewGsonGameObject().toGameObject();
                Game.getStaticEntityRegistry().addEntityObject(object);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if(p instanceof RemoveObjectPacket) {
            RemoveObjectPacket packet = (RemoveObjectPacket) p;
            Game.getStaticEntityRegistry().getGameObjects().remove(packet.getUuid());
        } if (p instanceof GameOverPacket) {
            Objects.requireNonNull(Game.getClient()).disconnect();

            Game.getMainPlayer().setHealth(100);
            Game.setScreen(new EndScreen());

        } else if (p instanceof LevelUp) {
            Game.getHud().setLevel(((LevelUp) p).getLevel());
        }
    }



    @EventHandler
    public void onConnect(ServerConnectFinishEvent e) {

    }

    @EventHandler
    public void onDisconnect(ServerDisconnectEvent e) {
        if (Game.getClient() != null) {
            resetGame();
            Game.setClient(null);
        }
    }

    private void resetGame() {

        Game.setScreen(new MainMenu());
        if (Game.getMainPlayer() != null) Game.getMainPlayer().setHealth(100);
//        Random r = UniversalHandler.RANDOM;
//        int amount = r.nextInt(15);
//        if (amount < 10) amount = 10;
//        for (int i = 0; i < amount; i++) {
//
//            Game.getHandler().addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), EntityID.MenuParticle));
//        }
    }


}
