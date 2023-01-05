package com.github.fernthedev.game.server

import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.universal.entity.EntityPlayer
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class ClientGameData(
    val clientConnection: ClientConnection,
    val uuid: UUID,
    var entityPlayer: EntityPlayer,
    var clientSidePlayerHashCode: Int = 0,
    var forcedUpdate: Boolean = true
){


    /**
     * Used to cache the uuid of the object and last modified.
     * To avoid sending entities that are already on the client.
     *
     * UUID:HashCode
     */
    val objectCacheList: MutableMap<UUID, Int> = ConcurrentHashMap()
}