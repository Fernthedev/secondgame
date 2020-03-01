package com.github.fernthedev.packets.ObjectUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.EntityPlayer;

public class SendObjectsList extends Packet {

    private String list;

    public boolean doUpdatePlayer() {
        return updatePlayer;
    }

    private boolean updatePlayer;

    private EntityPlayer mainPlayer;

    public EntityPlayer getMainPlayer() {
        return mainPlayer;
    }

    /*
    public SendObjectsList(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }*/

    public SendObjectsList(String list,boolean updatePlayer,EntityPlayer entityPlayer) {
        this.list = list;
        this.updatePlayer = updatePlayer;
        this.mainPlayer = entityPlayer;
    }

    public String getObjectList() {
        return list;
    }

}
