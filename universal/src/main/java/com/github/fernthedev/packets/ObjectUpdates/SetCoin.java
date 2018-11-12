package com.github.fernthedev.packets.ObjectUpdates;

import com.github.fernthedev.packets.Packet;

public class SetCoin extends Packet {

    private int coins;

    public int getCoins() {
        return coins;
    }

    public SetCoin(int coins) {
        this.coins = coins;
    }

}
