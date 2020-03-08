package io.github.fernthedev.secondgame.main.deprecated.netty.client;


import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.packets.object_updates.SendGameObject;
import com.github.fernthedev.packets.object_updates.SendObjectsList;
import com.github.fernthedev.packets.object_updates.SetCoin;
import com.github.fernthedev.packets.player_updates.GetSelfPlayerInfo;
import com.github.fernthedev.packets.player_updates.SendPlayerInfoPacket;
import com.github.fernthedev.packets.player_updates.SetCurrentPlayer;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.EntityID;
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

@Deprecated
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
        }
    }

}
