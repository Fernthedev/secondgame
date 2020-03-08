package com.github.fernthedev.game.server;

import com.github.fernthedev.server.ClientConnection;
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
    private EntityPlayer entityPlayer;

    @Getter
    private boolean changed;

    public void setEntityPlayer(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
        lastModified = System.nanoTime();
        changed = true;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
        lastModified = System.nanoTime();
    }

    @Setter
    @Getter
    private long lastModified = System.nanoTime();


    /**
     * Used to cache the uuid of the object and last modified.
     * To avoid sending entities that are already on the client.
     */
    private Map<UUID, Long> objectCacheTime = Collections.synchronizedMap(new HashMap<>());

    private Set<UUID> objectsToAdd = Collections.synchronizedSet(new HashSet<>());

}
