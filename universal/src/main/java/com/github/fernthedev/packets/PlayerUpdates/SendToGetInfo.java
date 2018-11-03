package com.github.fernthedev.packets.PlayerUpdates;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.universal.entity.UniversalPlayer;

public class SendToGetInfo extends Packet {

    private UniversalPlayer keepPlayer;

    /**
     * This is used for ping pong player info. The request's answer is this.
     * @param universalPlayer The new player
     */
    public SendToGetInfo(UniversalPlayer universalPlayer) {
        this.keepPlayer = universalPlayer;
    }

    public UniversalPlayer getKeepPlayer() {
        return keepPlayer;
    }
}
