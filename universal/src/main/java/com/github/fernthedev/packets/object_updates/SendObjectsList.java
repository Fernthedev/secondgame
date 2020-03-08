package com.github.fernthedev.packets.object_updates;

import com.github.fernthedev.core.packets.Packet;
import com.github.fernthedev.core.packets.PacketInfo;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.github.fernthedev.universal.entity.NewGsonGameObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@PacketInfo(name = "SEND_OBJECT_LIST")
public class SendObjectsList extends Packet implements Serializable {

    @Getter
    private Map<UUID, NewGsonGameObject> objectList;

    @Getter
    private boolean updatePlayer;

    @Getter
    private EntityPlayer mainPlayer;


    /*
    public SendObjectsList(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }*/

}
