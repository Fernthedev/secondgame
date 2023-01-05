package com.github.fernthedev

import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import com.github.fernthedev.universal.entity.EntityPlayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.measureTimeMillis

abstract class INewEntityRegistry : TickRunnable {
//    private val tickThreadGroup: Array<Thread>
//    init {
//        val size = max(Runtime.getRuntime().availableProcessors() / 2, 1)
//        tickThreadGroup = Array(size, i -> {
//            Thread()
//        })
//
//    }

    private val _gameObjects: MutableMap<UUID, GameObject> = ConcurrentHashMap()
    private val _previousLocations: MutableMap<UUID, Location> = ConcurrentHashMap()

    open val gameObjects: Map<UUID, GameObject>
        get() = _gameObjects
    val previousLocations: Map<UUID, Location>
        get() = _previousLocations

    fun clearEntities() {
        _gameObjects.clear()
        _previousLocations.clear()
    }

    fun addEntityObject(gameObject: GameObject) {
//        if (gameObjects.containsKey(gameObject.uniqueId)) {
//            throw IllegalArgumentException("GameObject ${gameObject.uniqueId} already exists")
//        }

        // For interpolation
        _gameObjects[gameObject.uniqueId] = gameObject
        _previousLocations[gameObject.uniqueId] = gameObject.location.copy()
    }

    fun removeEntityObject(gameObject: UUID) {
        _gameObjects.remove(gameObject)
        _previousLocations.remove(gameObject)
    }

    fun removeEntityObject(gameObject: GameObject) {
        removeEntityObject(gameObject.uniqueId)
    }

    open fun collisionCheck(universalPlayer: EntityPlayer) {
        for ((_, tempObject) in gameObjects) {
            if (!universalPlayer.bounds.intersects(tempObject.bounds)) continue

            if (tempObject.entityId == EntityID.ENEMY) {
                //COLLISION CODE
                universalPlayer.health = universalPlayer.health - 2
            }

            if (tempObject.entityId == EntityID.COIN) {
                universalPlayer.coin = universalPlayer.coin + 1
                removeEntityObject(tempObject)
            }
        }
    }

    override suspend fun tick() = coroutineScope {
        val ms = measureTimeMillis {

            val jobs = gameObjects.map { (_, obj) ->
                val job = launch {
                    obj.tick()
                    clampAndTP(obj)
                    if (obj is EntityPlayer) collisionCheck(obj)
                }

                job
            }.toList()

            jobs.forEach { job ->
                job.join()
            }

        }

        if (ms > 10) {
            println("Tick took $ms")
        }
    }

    protected open fun clampAndTP(gameObject: GameObject) {
        val location = gameObject.location
        location.x += gameObject.velX
        location.y += gameObject.velY

        val maxX: Float = UniversalHandler.WIDTH.toFloat() - gameObject.width - gameObject.width / 2f
        val maxY: Float = UniversalHandler.HEIGHT.toFloat() - gameObject.height * 2f - gameObject.width / 4f

        val newX: Float = GameMathUtil.clamp(gameObject.location.x, 0F, maxX)
        val newY: Float = GameMathUtil.clamp(gameObject.location.y, 0F, maxY)

        if (gameObject is EntityPlayer) {
            if (newX != gameObject.location.x) {
                gameObject.velX = 0F
            }
            if (newY != gameObject.location.y) {
                gameObject.velY = 0F
            }
        } else {
            if (location.x <= 0 || location.x >= maxX) gameObject.velX *= -1f
            if (location.y <= 0 || location.y >= maxY) gameObject.velY *= -1f
        }

        val prevLoc = previousLocations[gameObject.uniqueId]
        if (prevLoc != null) {
            prevLoc.x = gameObject.location.x
            prevLoc.y = gameObject.location.y
        }

        gameObject.location.x = newX
        gameObject.location.y = newY
    }
}