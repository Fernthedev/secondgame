package com.github.fernthedev.packets.ObjectUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.GameObject;

public class SendGameObject extends Packet {

    private final GameObject gameObject;

    public SendGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
