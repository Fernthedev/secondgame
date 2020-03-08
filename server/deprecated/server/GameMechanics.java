package com.github.fernthedev.game.server.game_handler;

import com.github.fernthedev.TickRunnable;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.Synchronized;

import java.util.ArrayList;

@Deprecated
public class GameMechanics extends TickRunnable {

    public void tick() {

        ArrayList<GameObject> objects = new ArrayList<>(EntityHandler.getGameObjects());

        for (GameObject gameObject : objects) {
            if (gameObject instanceof EntityPlayer) {
                collisionCheck((EntityPlayer) gameObject);

            }
        }

        /*for(NetPlayer gameObject : EntityHandler.playerList) {
            ClientPlayer clientPlayer = ClientPlayer.getPlayerFromObject(gameObject.getObjectID());
            if(clientPlayer != null)
            collisionCheck(clientPlayer.getPlayerObject());
        }*/
    }

    @Synchronized
    private synchronized void collisionCheck(EntityPlayer playerObject) {

    }
}
