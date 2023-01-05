package com.github.fernthedev.universal.entity

import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import java.awt.Color


class BasicEnemy(location: Location) :
    GameObject(location, width = 16F, height = 16F, entityId = EntityID.ENEMY, color = Color.RED) {
    init {
        velX = 5f
        velY = 5f
    }

    override fun tick() {}
}