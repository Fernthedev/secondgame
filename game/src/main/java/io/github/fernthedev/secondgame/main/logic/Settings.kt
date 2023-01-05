package io.github.fernthedev.secondgame.main.logic

import com.github.fernthedev.lightchat.client.ClientSettings
import com.github.fernthedev.universal.UniversalHandler

data class Settings(
    val name: String = "",
    val host: String = "127.0.0.1",
    val port: Int = UniversalHandler.MULTIPLAYER_PORT
) : ClientSettings()