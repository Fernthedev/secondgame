package com.github.fernthedev.universal.entity

import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import java.awt.Color
import java.util.*
import kotlin.math.sqrt


class SmartEnemy(location: Location, var playerUUID: UUID? = null) : GameObject(location, width = 16F, height = 16F, entityId = EntityID.ENEMY, color = Color.GREEN) {


    override fun tick() {
        var player: GameObject? = null
        val objects: Map<UUID, GameObject> = UniversalHandler.iGame.entityRegistry.gameObjects
        if (!objects.containsKey(playerUUID)) {
            val option: Optional<GameObject> = objects
                .values.parallelStream()
                .filter { gameObjectLongPair -> gameObjectLongPair is EntityPlayer }
                .findAny()
            if (option.isPresent) player = option.get()
        } else {
            player = UniversalHandler.iGame.entityRegistry.gameObjects[playerUUID]
        }

        if (player == null) {
            velX = 0f
            velY = 0f
            return
        }
        playerUUID = player.uniqueId

//        @NonNull GameObject player = playerPair.getKey();
        val diffX: Float = location.x - player.location.x - player.width
        val diffY: Float = location.y - player.location.y - player.height
        val distance =
            sqrt((location.x - player.location.x) * (location.x - player.location.x) + (location.y - player.location.y) * (location.y - player.location.y))
        velX = -1 / distance * diffX
        velY = -1 / distance * diffY
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SmartEnemy) return false
        if (!super.equals(other)) return false

        if (playerUUID != other.playerUUID) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (playerUUID?.hashCode() ?: 0)
        return result
    }


}