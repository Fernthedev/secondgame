package io.github.fernthedev.secondgame.main.netty.client;

import com.github.fernthedev.CommonUtil;
import com.github.fernthedev.lightchat.client.api.IPacketHandler;
import com.github.fernthedev.lightchat.client.event.ServerConnectFinishEvent;
import com.github.fernthedev.lightchat.client.event.ServerDisconnectEvent;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.core.api.event.api.EventHandler;
import com.github.fernthedev.lightchat.core.api.event.api.Listener;
import com.github.fernthedev.lightchat.core.packets.Packet;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class PacketHandler implements IPacketHandler, Listener {

    @Override
    public void handlePacket(Packet p, int packetId) {
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

            StaticHandler.getCore().getLogger().debug("Updating object list " + list.getObjectList() + "\n");


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

            AtomicBoolean doUpdate = new AtomicBoolean(true);

            gameObjects.forEach((uuid, newGsonGameObject) -> {
                try {

                    if (newGsonGameObject == null) {
                        Game.getStaticEntityRegistry().getGameObjects().remove(uuid);
                    } else {
                        GameObject object = newGsonGameObject.toGameObject();
                        if (object == null) Game.getStaticEntityRegistry().getGameObjects().remove(uuid);
                        else {
                            assert Game.getClient() != null;

                            boolean updateEntity = true;

                            boolean isPlayer = Game.getMainPlayer() != null && object.getUniqueId() == Game.getMainPlayer().getUniqueId();

                            if (isPlayer &&
                                    Game.getClient().getPacketId(p.getClass()).getLeft() - 3 > packetId // If the packet received is 3 packets old
                                    && System.currentTimeMillis() - Game.getClient().getPacketId(p.getClass()).getRight() > 900
                            ) updateEntity = false;

                            if (isPlayer && (
                                            Math.abs(universalPlayer.getX() - Game.getMainPlayer().getX()) > CommonUtil.PLAYER_COORD_DIF ||
                                                    Math.abs(universalPlayer.getY() - Game.getMainPlayer().getY()) > CommonUtil.PLAYER_COORD_DIF
                            )) updateEntity = false;

                            if (updateEntity) {
                                Game.getStaticEntityRegistry().addEntityObject(object);
                            }

                            if (isPlayer) doUpdate.set(updateEntity);
//                            if (!Game.getStaticEntityRegistry().getGameObjects().containsKey(object.getUniqueId()))

                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });

            if (list.isChanged())
                Game.getStaticEntityRegistry().addEntityObject(universalPlayer);


            if (doUpdate.get())
                Game.setMainPlayer(universalPlayer);

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


            StaticHandler.getCore().getLogger().debug("Updating player because server asked us to " + universalPlayer);

//            Game.getStaticEntityRegistry().addEntityObject(universalPlayer);


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
        } else if (p instanceof RemoveObjectPacket) {
            RemoveObjectPacket packet = (RemoveObjectPacket) p;
            Game.getStaticEntityRegistry().getGameObjects().remove(packet.getUuid());
        }
        if (p instanceof GameOverPacket) {
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
