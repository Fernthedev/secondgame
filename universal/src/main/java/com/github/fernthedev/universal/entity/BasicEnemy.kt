package com.github.fernthedev.universal.entity

import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import java.awt.Color
import java.util.UUID


class BasicEnemy(
    location: Location,
    width: Float = 16F,
    height: Float = 16F,
    entityId: EntityID = EntityID.ENEMY,
    color: Color = Color.RED,
    velX: Float = 5f,
    velY: Float = 5f,
    uniqueID: UUID = UUID.randomUUID()
) : GameObject(
    location,
    width = width,
    height = height,
    entityId = entityId,
    color = color,
    velX = velX,
    velY = velY,
    uniqueId = uniqueID
) {


    override fun tick() {}
}