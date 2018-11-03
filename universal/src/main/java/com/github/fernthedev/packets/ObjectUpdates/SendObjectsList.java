package com.github.fernthedev.packets.ObjectUpdates;

import com.github.fernthedev.packets.Packet;

public class SendObjectsList extends Packet {

    private String list;

/*
    public SendObjectsList(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }*/

    public SendObjectsList(String list) {
        this.list = list;
    }

    public String getObjectList() {
        return list;
    }

}
