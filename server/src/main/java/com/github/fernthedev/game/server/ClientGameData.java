package com.github.fernthedev.game.server;

import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Data
public class ClientGameData {

    @NonNull
    private final ClientConnection clientConnection;


    @NonNull
    private EntityPlayer entityPlayer;

    @Getter
    @Setter
    private boolean changed;

    public void setEntityPlayer(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }


    /**
     * Used to cache the uuid of the object and last modified.
     * To avoid sending entities that are already on the client.
     */
//    private Map<UUID, Long> objectCacheTime = Collections.synchronizedMap(new HashMap<>());
    private Set<UUID> objectCacheList = Collections.synchronizedSet(new HashSet<>());

    @Deprecated
    private Set<UUID> objectsToAdd = Collections.synchronizedSet(new HashSet<>());

}
