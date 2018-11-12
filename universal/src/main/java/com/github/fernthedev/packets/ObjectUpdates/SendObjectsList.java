package com.github.fernthedev.packets.ObjectUpdates;

import com.github.fernthedev.packets.Packet;

public class SendObjectsList extends Packet {

    private String list;

    public boolean doUpdatePlayer() {
        return updatePlayer;
    }

    private boolean updatePlayer;
/*
    public SendObjectsList(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }*/

    public SendObjectsList(String list,boolean updatePlayer) {
        this.list = list;
        this.updatePlayer = updatePlayer;
    }

    public String getObjectList() {
        return list;
    }

}
