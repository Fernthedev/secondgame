package com.github.fernthedev.game.server;

import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.*;

import java.util.*;

@RequiredArgsConstructor
@Getter
@Data
public class ClientGameData {

    @NonNull
    private final ClientConnection clientConnection;

    @NonNull
    private UUID uuid;

    @NonNull
    private EntityPlayer entityPlayer;

    @Getter
    @Setter
    private int clientSidePlayerHashCode;

    public void setEntityPlayer(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }


    /**
     * Used to cache the uuid of the object and last modified.
     * To avoid sending entities that are already on the client.
     *
     * UUID:HashCode
     */
    private Map<UUID, Integer> objectCacheList = Collections.synchronizedMap(new HashMap<>());

}
