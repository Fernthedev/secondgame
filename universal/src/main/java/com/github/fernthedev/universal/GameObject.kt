package com.github.fernthedev.universal

import java.awt.Color
import java.awt.Rectangle
import java.io.Serializable
import java.util.*


abstract class GameObject(
    val location: Location,
    var entityId: EntityID?,

    var velX: Float = 0.0F,
    var velY: Float = 0.0F,

    val uniqueId: UUID = UUID.randomUUID(),


    var color: Color,

    var width: Float = 0F, var height: Float = 0F
) : Serializable, Cloneable {
    var hasTrail = true
        protected set

    init {
        require(width != 0F && height != 0F) { "HEIGHT OR WIDTH ARE 0" }
    }

    abstract fun tick()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameObject) return false

        if (location != other.location) return false
        if (entityId != other.entityId) return false
        if (velX != other.velX) return false
        if (velY != other.velY) return false
        if (uniqueId != other.uniqueId) return false
        if (color != other.color) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (hasTrail != other.hasTrail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = location.hashCode()
        result = 31 * result + (entityId?.hashCode() ?: 0)
        result = 31 * result + velX.hashCode()
        result = 31 * result + velY.hashCode()
        result = 31 * result + uniqueId.hashCode()
        result = 31 * result + (color?.hashCode() ?: 0)
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + hasTrail.hashCode()
        return result
    }


    open val bounds: Rectangle
        get() = Rectangle(location.x.toInt(), location.y.toInt(), width.toInt(), height.toInt())



    companion object {
        private val serialVersionUID = 9102545992378004553L
    }
}