package com.github.fernthedev.universal.entity

import com.github.fernthedev.CommonUtil
import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import java.awt.Color
import java.util.*
import kotlin.math.abs

class EntityPlayer
    (
    location: Location,
    val name: String,
    var health: Int = 100,
    var coin: Int = 0,
    velX: Float = 0F,
    velY: Float = 0F,
    uniqueId: UUID = UUID.randomUUID(),
    color: Color? = null,
) : GameObject(location, entityId = EntityID.PLAYER, velX = velX, velY = velY, uniqueId,   color = color ?: Color(UniversalHandler.RANDOM.nextInt(255),UniversalHandler.RANDOM.nextInt(255),UniversalHandler.RANDOM.nextInt(255)), 32F, 32F), Cloneable {


    companion object {
        @Transient
        const val MAX_VELOCITY = 5

        @JvmStatic
        fun isPlayerDifferent(oldPlayer: EntityPlayer, copyVel: EntityPlayer, velX: Float, velY: Float): Boolean {
            return abs(
                copyVel.location.x -
                        oldPlayer.location.x + velX
            ) > CommonUtil.PLAYER_COORD_DIF || abs(
                copyVel.location.y - oldPlayer.location.y + velY
            ) > CommonUtil.PLAYER_COORD_DIF || abs(
                copyVel.velX -
                        oldPlayer.velX
            ) > CommonUtil.PLAYER_VEL_DIF || abs(
                copyVel.velY -
                        oldPlayer.velY
            ) > CommonUtil.PLAYER_VEL_DIF
        }
    }

    override fun tick() {

    }
}