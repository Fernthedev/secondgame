package com.github.fernthedev.universal.entity

import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import java.awt.Color
import java.util.*


class Coin(location: Location, uniqueId: UUID = UUID.randomUUID()) :
    GameObject(
        location,
        width = 16f,
        height = 16f,
        entityId = EntityID.COIN,
        color = Color.ORANGE,
        velX = 0F,
        velY = 0F,
        uniqueId = uniqueId
    ) {
    init {
        hasTrail = false
    }

    override fun tick() {
    }
}