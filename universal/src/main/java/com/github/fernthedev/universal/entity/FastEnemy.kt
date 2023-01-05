package com.github.fernthedev.universal.entity

import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import java.awt.Color


class FastEnemy(location: Location) :
    GameObject(location, EntityID.ENEMY, 5F, 7F, width = 16f, height = 16f, color = Color.CYAN) {

    override fun tick() {}
}