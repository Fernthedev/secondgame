//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package io.github.fernthedev.secondgame.main

import com.github.fernthedev.CommonUtil
import com.github.fernthedev.IGame
import com.github.fernthedev.INewEntityRegistry
import com.github.fernthedev.config.common.Config
import com.github.fernthedev.config.gson.GsonConfig
import com.github.fernthedev.game.server.GameServer
import com.github.fernthedev.lightchat.client.Client
import com.github.fernthedev.lightchat.client.event.ServerDisconnectEvent
import com.github.fernthedev.lightchat.core.PacketJsonRegistry
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.server.event.ServerStartupEvent
import com.github.fernthedev.universal.UniversalHandler
import com.github.fernthedev.universal.entity.EntityPlayer
import io.github.fernthedev.secondgame.main.inputs.joystick.JoystickHandler
import io.github.fernthedev.secondgame.main.inputs.keyboard.KeyInput
import io.github.fernthedev.secondgame.main.logic.NewClientEntityRegistry
import io.github.fernthedev.secondgame.main.logic.Settings
import io.github.fernthedev.secondgame.main.netty.client.ClientPacketHandler
import io.github.fernthedev.secondgame.main.ui.MouseHandler
import io.github.fernthedev.secondgame.main.ui.api.Screen
import io.github.fernthedev.secondgame.main.ui.screens.EndScreen
import io.github.fernthedev.secondgame.main.ui.screens.MainMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.lwjgl.Version
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferStrategy
import java.io.File
import java.text.NumberFormat
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.exitProcess

class Game : Canvas(), Runnable, IGame {
    @Transient
    private var thread: Thread? = null
    private var running = true

    @Transient
    private val spawnner: Spawn

    //    private final Menu menu;
    @Transient
    private val keyInput: KeyInput
    private val window: Window

    @Transient
    private val testJoyStick: JoystickHandler

    /**
     * DIFFICULTY
     * 0 = NORMAL
     * 1 = HARD
     */
    var dif = 0

    /**
     * MAIN
     */
    init {
        loggerImpl.info("Loaded icon")
        UniversalHandler.iGame = this
        val settings = Settings()
        gameSettings = GsonConfig(settings, File("./config_settings.json"))
        gameSettings.load()
        gameSettings.save()
        hud = HUD()

//        handler = new Handler(hud);
        staticEntityRegistry = NewClientEntityRegistry()
        testJoyStick = JoystickHandler()
        screen = MainMenu()
        //        menu = new io.github.fernthedev.secondgame.main.ui.Menu(this,handler,hud);
        loggerImpl.info("LWJGL Version " + Version.getVersion() + " is working.")
        keyInput = KeyInput(this)
        this.addKeyListener(keyInput)
        this.addMouseListener(MouseHandler())
        window = Window(UniversalHandler.WIDTH, UniversalHandler.HEIGHT, "A NEW GAME", this)
        `fern$` = r.nextInt(100)
        loggerImpl.info("{}", `fern$`)
        spawnner = Spawn(hud, this)
    }

    /**
     * RUNNABLE METHOD
     */
    override fun run() {
        this.requestFocus()

        try {
            var lastTime = System.nanoTime()
            val amountOfTicks = 60.0
            val ns = 1000000000 / amountOfTicks
            elapsedTime = 0.0
            var timer = System.currentTimeMillis()
            var frames = 0
            runBlocking {
                while (running) {
                    val now = System.nanoTime()
                    elapsedTime += (now - lastTime) / ns
                    lastTime = now
                    while (elapsedTime >= 1) {
                        tick()

                        elapsedTime--
                    }
                    if (running) render()
                    frames++
                    if (System.currentTimeMillis() - timer > 1000) {
                        timer += 1000
                        loggerImpl.info("FPS: " + NumberFormat.getNumberInstance(Locale.US).format(frames.toLong()))
                        frames = 0
                        //updates = 0;
                    }
                    try {
                        delay(UniversalHandler.TICK_WAIT.toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        Thread.currentThread().interrupt()
                    }
                }
            }
        } catch (e: Throwable) {
            Game.loggerImpl.error("Main thread crashed")
            e.printStackTrace()
            exitProcess(-1)
        }
    }

    /**
     * TICK METHOD
     */
    private suspend fun tick() {
        try {
            if (screen != null) {
                staticEntityRegistry.tick()
                return
            }

            keyInput.tick()
            testJoyStick.tick()

            if (paused) {
                return
            }

            hud.tick()
            staticEntityRegistry.tick()

            if (gameServer == null && client == null) {
                spawnner.tick()
                if (mainPlayer != null && mainPlayer!!.health <= 0) {
                    mainPlayer!!.health = 100
                    screen = EndScreen()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * RENDERING OBJECTS AND GAME
     */
    private fun render() {
        if (bufferStrategy == null) {
            createBufferStrategy(3)
            return
        }
        val bs: BufferStrategy = this.bufferStrategy
        val g: Graphics2D = bs.drawGraphics as Graphics2D
        g.color = Color.black
        g.fillRect(0, 0, UniversalHandler.WIDTH, UniversalHandler.HEIGHT)
        staticEntityRegistry.render(g)
        if (paused) {
            g.color = Color.WHITE
            g.drawString("Paused", 100, 100)
        }

        if (screen != null) {
            screen!!.render(g)
        } else {
            hud.render(g)
        }

        g.dispose()
        bs.show()
    }

    /**
     * Threads
     */
    @Synchronized
    fun start() {
        thread = Thread(this, "MainGameThread")
        thread!!.start()
    }

    /**
     * Stopping game threads
     */
    @Synchronized
    fun stop() {
        try {
            UniversalHandler.running = false
            running = false
            thread!!.join()
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        //    public static final int WIDTHHH = 640, HEIGHTTTT =   WIDTHHH / 12 * 9;
        private const val serialVersionUID = -7376944666695581278L


        val loggerImpl: Logger by lazy { LoggerFactory.getLogger(Game::class.java.name) }

        //    private static transient Handler handler;
        @Transient
        private val r: Random = UniversalHandler.RANDOM

        @Transient
        lateinit var hud: HUD
            private set


        var screen: Screen? = null
            set(value) {
                if (field != null) screen?.parentScreen = field
                field = value
            }

        lateinit var gameSettings: Config<out Settings>


        var mainPlayer: EntityPlayer? = null


        var client: Client? = null
        var gameServer: GameServer? = null

        lateinit var staticEntityRegistry: NewClientEntityRegistry
        var `fern$`: Int = 0

        /**
         * STATE OF PAUSED
         */
        var paused = false
        var elapsedTime = 0.0
            private set

        /**
         * CREATING A BOX/LIMIT FOR A VARIABLE
         * @param var Variable being affected
         * @param min The minimum value
         * @param max The Max Value
         * @return Returns the max or min if either var is greater than either, if not returns var
         */
        fun clamp(`var`: Float, min: Float, max: Float): Float {
            return if (`var` >= max) max else if (`var` <= min) min else `var`
        }

        fun clamp(`var`: Int, min: Int, max: Int): Int {
            return if (`var` >= max) max else if (`var` <= min) min else `var`
        }

        /**
         * STARTING METHOD
         */
        @JvmStatic
        fun main(args: Array<String>) {
            if (Arrays.stream(args).parallel()
                    .anyMatch { s: String -> s == "-debug" }
            ) {
                StaticHandler.setDebug(true)
            }
            Game()
        }

        fun setupJoiningMultiplayer() {
            staticEntityRegistry.clearObjects()
            val client =
                Client(gameSettings.configData.host, gameSettings.configData.port)
            client.clientSettingsManager = gameSettings
            client.maxPacketId = CommonUtil.MAX_PACKET_IDS
            Companion.client = client
            screen = null
            CommonUtil.registerNetworking()
            PacketJsonRegistry.registerDefaultPackets()


            val clientPacketHandler = ClientPacketHandler()

            client.eventHandler.add(ServerDisconnectEvent::class.java) {
                clientPacketHandler.onDisconnect(it)
            }
            client.addPacketHandler(clientPacketHandler)

            var name = client.name

            val configName: String = gameSettings.configData.name

            if (configName.trim { it <= ' ' }.isNotEmpty()) name = configName

            StaticHandler.core.logger.debug("Using uuid name: {}", name)

            client.name = name
            mainPlayer = null
            Dispatchers.Default.dispatch(EmptyCoroutineContext) {
                runBlocking {
                    try {
                        client.connect()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        Thread.currentThread().interrupt()
                    }
                }
            }
            val thread = Thread {

            }
            thread.start()
        }

        fun setupHostingMultiplayer() {
            staticEntityRegistry.startGame()

            val server = GameServer(
                arrayOf(),
                UniversalHandler.MULTIPLAYER_PORT,
                true /* TODO: Remove */
            )
            staticEntityRegistry.serverEntityRegistry = server.entityHandler

            gameServer = server
            server.server.eventHandler.add(ServerStartupEvent::class.java) {
                staticEntityRegistry.addEntityObject(mainPlayer!!)

            }
        }
    }

    override fun getEntityRegistry(): INewEntityRegistry {
        return staticEntityRegistry
    }

    override fun getLoggerImpl(): Logger {
        return Companion.loggerImpl
    }
}