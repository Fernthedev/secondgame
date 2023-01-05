package com.github.fernthedev.game.server

import com.github.fernthedev.universal.UniversalHandler

fun main(args: Array<String>) {
    GameServer(args, UniversalHandler.MULTIPLAYER_PORT, true)
}