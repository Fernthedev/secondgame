package com.github.fernthedev.universal.entity

import com.github.fernthedev.universal.GameObject

class GsonObject(go: GameObject) : GameObject(
    location = go.location.copy(),
    velX = go.velX,
    velY = go.velY,
    height = go.height,
    width = go.width,
    color = go.color,
    entityId = go.entityId,
    uniqueId = go.uniqueId
) {

    val alpha = 1f
    var health = 100
    var coin = 0
    var life = 0f

    override fun tick() {}
}