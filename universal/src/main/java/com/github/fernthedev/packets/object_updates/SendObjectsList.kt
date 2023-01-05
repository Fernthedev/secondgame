package com.github.fernthedev.packets.object_updates;


import com.github.fernthedev.lightchat.core.packets.Packet;
import com.github.fernthedev.lightchat.core.packets.PacketInfo;
import com.github.fernthedev.universal.entity.EntityPlayer;
import com.github.fernthedev.universal.entity.NewGsonGameObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@PacketInfo(name = "SEND_OBJECT_LIST")
public class SendObjectsList extends Packet implements Serializable {

    @Getter
    @NonNull
    private final Map<UUID, NewGsonGameObject> objectList;

    @Getter
    @NonNull
    private final EntityPlayer mainPlayer;


    /*
    public SendObjectsList(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }*/

}
