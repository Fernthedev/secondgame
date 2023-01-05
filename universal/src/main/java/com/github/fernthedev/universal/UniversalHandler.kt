package com.github.fernthedev.universal

import com.github.fernthedev.IGame
import com.google.gson.Gson
import java.util.*

object UniversalHandler {
    const val WIDTH = 640
    const val HEIGHT = WIDTH / 12 * 9
    const val TICK_WAIT = 5
    const val MULTIPLAYER_PORT = 3000
    const val PLAYER_VEL_MULTIPLIER = 5

    lateinit var iGame: IGame


    @JvmStatic
    var running = true
    val gson: Gson = Gson()


    val RANDOM = Random()
}