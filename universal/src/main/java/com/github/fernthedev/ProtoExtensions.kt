package com.github.fernthedev

import com.github.fernthedev.packets.proto.*
import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.entity.BasicEnemy
import com.github.fernthedev.universal.entity.Coin
import com.github.fernthedev.universal.entity.EntityPlayer
import com.google.protobuf.ByteString
import java.awt.Color
import java.nio.ByteBuffer
import java.util.*
import com.github.fernthedev.packets.proto.Packets.GameObject as ProtoGameObject

fun convertUUIDToBytes(uuid: UUID): ByteArray {
    val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(uuid.mostSignificantBits)
    bb.putLong(uuid.leastSignificantBits)
    return bb.array()
}

fun convertBytesToUUID(bytes: ByteArray): UUID {
    val byteBuffer: ByteBuffer = ByteBuffer.wrap(bytes)
    val high: Long = byteBuffer.long
    val low: Long = byteBuffer.long
    return UUID(high, low)
}

fun UUID.toProto(): Packets.UUID {
    return uUID {
        value = ByteString.copyFrom(convertUUIDToBytes(this@toProto))
    }
}

fun Packets.UUID.toUUID(): UUID {
    return UUID.nameUUIDFromBytes(this.value.toByteArray())
}

fun Location.toProto(): Packets.Location {
    return location {
        x = this@toProto.x
        y = this@toProto.y
    }
}

fun Color.toProto(): Packets.Color {
    return color {
        r = red
        g = green
        b = blue
        a = alpha
    }
}

fun Packets.Location.toLocation(): Location {
    return Location(
        x = x,
        y = y
    )
}

fun Packets.Color.toColor(): Color {
    return Color(r, g, b, a)
}

fun ProtoGameObject.toGameObject(): GameObject {

    return when (this.entityId) {
        Packets.EntityID.PLAYER -> EntityPlayer(
            location = location.toLocation(),
            name = name,
            health = health,
            coin = coin,
            velX = velX,
            velY = velY,
            color = color.toColor(),
            uniqueId = uniqueId.toUUID()
        )

        Packets.EntityID.ENEMY -> BasicEnemy(
            location = location.toLocation(),
            velX = velX,
            velY = velY,
            color = color.toColor(),
            width = width,
            height = height,
            uniqueID = uniqueId.toUUID()
        )

        Packets.EntityID.COIN -> Coin(
            location = location.toLocation(),
            uniqueId = uniqueId.toUUID()
        )

        else -> TODO()
    }
}

fun GameObject.toProto(): Packets.GameObject {

    return gameObject {
        location = this@toProto.location.toProto()
        color = this@toProto.color.toProto()
        velX = this@toProto.velX
        velY = this@toProto.velY
        entityId = when (this@toProto.entityId) {
            EntityID.PLAYER -> Packets.EntityID.PLAYER
            EntityID.ENEMY -> Packets.EntityID.ENEMY
            EntityID.COIN -> Packets.EntityID.COIN

            else -> TODO()
        }
        uniqueId =
            this@toProto.uniqueId.toProto()

        height = this@toProto.height
        width = this@toProto.width
        hasTrail = this@toProto.hasTrail

        if (this@toProto is EntityPlayer) {
            health = this@toProto.health
            coin = this@toProto.coin
            name = this@toProto.name
        }

    }
}

fun Packets.GameObjectNullable?.toGameObject(): GameObject? {
    if (this == null) {
        return null
    }

    return this.o.toGameObject()
}

fun GameObject?.toProto(): Packets.GameObjectNullable {
    if (this == null) {
        return Packets.GameObjectNullable.getDefaultInstance()
    }

    return gameObjectNullable {
        this.o = this@toProto.toProto()
    }
}


fun <T> Map<String, T>.toUUIDMap(): Map<UUID, T> {
    return this.mapKeys {
        UUID.fromString(it.key)
    }
}

fun <T> Map<UUID, T>.toUUIDStringMap(): Map<String, T> {
    return this.mapKeys {
        it.key.toString()
    }
}