package com.github.fernthedev.game.server

import com.github.fernthedev.lightchat.core.encryption.PacketTransporter
import com.github.fernthedev.lightchat.server.ClientConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun ClientConnection.sendObjectIODeferred(transporter: PacketTransporter) = coroutineScope {
    return@coroutineScope async(Dispatchers.IO) {
        return@async this@sendObjectIODeferred.sendObject(transporter)
    }
}

suspend fun ClientConnection.sendObjectIO(transporter: PacketTransporter) = coroutineScope {
    return@coroutineScope launch(Dispatchers.IO) {
        this@sendObjectIO.sendObject(transporter)
    }
}