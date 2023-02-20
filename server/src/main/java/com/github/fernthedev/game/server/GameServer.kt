package com.github.fernthedev.game.server

import com.github.fernthedev.CommonUtil
import com.github.fernthedev.IGame
import com.github.fernthedev.Ticker
import com.github.fernthedev.config.gson.GsonConfig
import com.github.fernthedev.fernutils.console.ArgumentArrayUtils.parseArguments
import com.github.fernthedev.game.server.game_handler.GameNetworkProcessingHandler
import com.github.fernthedev.game.server.game_handler.PlayerUpdateHandler
import com.github.fernthedev.game.server.game_handler.ServerGameHandler
import com.github.fernthedev.game.server.game_handler.Spawn
import com.github.fernthedev.lightchat.core.ColorCode
import com.github.fernthedev.lightchat.core.PacketJsonRegistry
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.server.SenderInterface
import com.github.fernthedev.lightchat.server.Server
import com.github.fernthedev.lightchat.server.event.PlayerDisconnectEvent
import com.github.fernthedev.lightchat.server.event.PlayerJoinEvent
import com.github.fernthedev.lightchat.server.event.ServerShutdownEvent
import com.github.fernthedev.lightchat.server.event.ServerStartupEvent
import com.github.fernthedev.lightchat.server.settings.ServerSettings
import com.github.fernthedev.lightchat.server.terminal.ServerTerminal
import com.github.fernthedev.lightchat.server.terminal.ServerTerminalSettings
import com.github.fernthedev.lightchat.server.terminal.command.Command
import com.github.fernthedev.universal.UniversalHandler
import io.netty.channel.ChannelPipeline
import org.slf4j.Logger
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import kotlin.system.exitProcess

class GameServer constructor(
    args: Array<String>,
    defaultPort: Int,
    terminal: Boolean
) : IGame {

    val entityHandler: NewServerEntityRegistry = NewServerEntityRegistry(this)
    val processHandler: GameNetworkProcessingHandler = GameNetworkProcessingHandler(this)
    val playerUpdateHandler: PlayerUpdateHandler = PlayerUpdateHandler(this)
    val spawn: Spawn = Spawn(this)
    val serverGameHandler: ServerGameHandler = ServerGameHandler(this, entityHandler, spawn, playerUpdateHandler)

    lateinit var serverThread: Thread

    val logger: Logger by lazy { StaticHandler.core.logger }


    init {
        UniversalHandler.iGame = this

        val port = AtomicInteger(defaultPort)

        parseArguments(args)
            .handle("-port") { queue: Queue<String> ->
                try {
                    port.set(queue.remove().toInt())
                    if (port.get() <= 0) {
                        logger.error("-port cannot be less than 0")
                        port.set(-1)
                    } else logger.info("Using port {}", port)
                } catch (e: NumberFormatException) {
                    logger.error("-port is not a number")
                    port.set(-1)
                }
            }
            .handle("-debug") { StaticHandler.setDebug(true) }
            .apply()

        val serverSettings = ServerSettings()
        ServerTerminal.init(
            args,
            ServerTerminalSettings.builder()
                .port(port.get())
                .allowChangePassword(false)
                .allowTermPackets(false)
                .launchConsoleInCMDWhenNone(terminal)
                .serverSettings(GsonConfig<ServerSettings>(serverSettings, File("settings.json")))
                .build()
        )
        ServerTerminal.server.eventHandler.add(ServerShutdownEvent::class.java) {
            ServerTerminal.server.logger.info(ColorCode.RED.toString() + "Goodbye!")
            exitProcess(0)
        }

        ServerTerminal.server.maxPacketId = CommonUtil.MAX_PACKET_IDS
        ServerTerminal.server.addPacketHandler(ServerPacketHandler(this))
        ServerTerminal.server.eventHandler.add(ServerStartupEvent::class.java) { event ->

            UniversalHandler.running = true
            ServerTerminal.server.logger.info("Running game startup code")

            CommonUtil.registerNetworking()

            serverThread = ServerTerminal.server.serverThread!!


            ServerTerminal.server.channelHandlers = Consumer { channel: ChannelPipeline ->
                channel.addLast(processHandler)
            }

            ServerTerminal.server.eventHandler.add(PlayerJoinEvent::class.java) {
                processHandler.playerJoin(it)
            }
            ServerTerminal.server.eventHandler.add(PlayerDisconnectEvent::class.java) {
                processHandler.onLeave(it)
            }

            val thread = Thread(Ticker(serverGameHandler))
            thread.start()


            ServerTerminal.registerCommand(object : Command("start") {
                override fun onCommand(sender: SenderInterface, args: Array<String>) {
                    ServerTerminal.server.authenticationManager.authenticate(sender)
                        .thenAccept { aBoolean: Boolean ->
                            if (aBoolean) {
                                serverGameHandler.started = true
                            }
                        }
                }
            })
            ServerTerminal.registerCommand(object : Command("stop") {
                override fun onCommand(sender: SenderInterface, args: Array<String>) {
                    ServerTerminal.server.authenticationManager.authenticate(sender)
                        .thenAccept { aBoolean: Boolean ->
                            if (aBoolean) {
                                serverGameHandler.started = false
                                entityRegistry.removeRespawnAllPlayers()
                            }
                        }
                }
            })

        }

        ServerTerminal.server.eventHandler.add(ServerShutdownEvent::class.java) {
            UniversalHandler.running = false
        }

        PacketJsonRegistry.registerDefaultPackets()
        ServerTerminal.startBind()
    }

    val server: Server
        get() = ServerTerminal.server

    override fun getEntityRegistry(): NewServerEntityRegistry {
        return serverGameHandler.entityHandler
    }

    override fun getLoggerImpl(): Logger {
        return ServerTerminal.server.logger
    }
}

