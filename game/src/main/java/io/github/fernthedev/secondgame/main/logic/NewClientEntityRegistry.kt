package io.github.fernthedev.secondgame.main.logic

import com.github.fernthedev.GameMathUtil
import com.github.fernthedev.INewEntityRegistry
import com.github.fernthedev.exceptions.DebugException
import com.github.fernthedev.game.server.NewServerEntityRegistry
import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import com.github.fernthedev.universal.entity.*
import com.google.common.base.Stopwatch
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.entities.CoinRenderer
import io.github.fernthedev.secondgame.main.entities.MenuParticle
import io.github.fernthedev.secondgame.main.entities.PlayerRender
import io.github.fernthedev.secondgame.main.entities.Trail
import java.awt.Graphics2D
import java.util.*
import java.util.concurrent.TimeUnit

class NewClientEntityRegistry : INewEntityRegistry() {
    private val stopwatch = Stopwatch.createUnstarted()
    var serverEntityRegistry: NewServerEntityRegistry? = null

    override val gameObjects: Map<UUID, GameObject>
        get() = if (entityRegistryInUse === this) super.gameObjects else entityRegistryInUse.gameObjects

    private val entityRegistryInUse: INewEntityRegistry
        get() = if (Game.gameServer != null) serverEntityRegistry!! else this

    init {
        val defaultEntityRenderer = DefaultEntityRenderer()
        registerEntity(BasicEnemy::class.java, defaultEntityRenderer)
        registerEntity(FastEnemy::class.java, defaultEntityRenderer)
        registerEntity(SmartEnemy::class.java, defaultEntityRenderer)
        registerEntity(EntityPlayer::class.java, PlayerRender())
        registerEntity(MenuParticle::class.java, defaultEntityRenderer)
        registerEntity(Trail::class.java, Trail.Renderer())
        registerEntity(Coin::class.java, CoinRenderer())
    }

    private fun <T : GameObject> registerEntity(gameObject: Class<T>, renderer: IEntityRenderer<out GameObject>) {
        RENDER_REGISTRY[gameObject] = renderer
    }

    private fun <T : GameObject> getRenderer(clazz: Class<T>): IEntityRenderer<T> {
        require(RENDER_REGISTRY.containsKey(clazz)) { "The GameObject " + clazz.name + " does not have a renderer registered or assigned." }
        return RENDER_REGISTRY[clazz] as IEntityRenderer<T>
    }



    override fun collisionCheck(universalPlayer: EntityPlayer) {
        if (Game.client == null && Game.gameServer == null) super.collisionCheck(universalPlayer)
    }

    override fun clampAndTP(gameObject: GameObject) {
         if (Game.client == null && Game.gameServer == null || Game.mainPlayer != null && gameObject.uniqueId == Game.mainPlayer?.uniqueId) {
             super.clampAndTP(gameObject)
         }
    }

    override suspend fun tick() {
        super.tick()
        finishEntityUpdate()
        if (Game.client != null && !stopwatch.isRunning) stopwatch.start()
        if (Game.screen == null && Game.client != null && Game.client!!.isRegistered && Game.mainPlayer != null && stopwatch.elapsed(
                TimeUnit.MILLISECONDS
            ) >= 500
        ) {
            Game.client!!.sendObject(
                SendToServerPlayerInfoPacket(
                    Game.mainPlayer!!,
                    Game.staticEntityRegistry.objectsAndHashCode
                )
            )
            stopwatch.reset()
            Game.loggerImpl.info("Sent update packet " + Game.mainPlayer.toString())
        }
    }

    protected fun finishEntityUpdate() {}
    fun clearObjects() {
        DebugException("Cleared objects").printStackTrace()
        Game.loggerImpl.info("Clearing ")
        clearEntities()
    }

    fun startGame() {
        clearObjects()
        Game.mainPlayer = EntityPlayer(
            Location(UniversalHandler.WIDTH.toFloat() / 2f - 32f,
            UniversalHandler.HEIGHT.toFloat() / 2f - 32f),
            ""
        )
        Game.screen = null
        val r: Random = UniversalHandler.RANDOM
        val HEIGHT: Int = UniversalHandler.HEIGHT
        val WIDTH: Int = UniversalHandler.WIDTH
        addEntityObject(Game.mainPlayer!!)
        addEntityObject(BasicEnemy(Location(r.nextFloat(WIDTH - 50f), r.nextFloat(HEIGHT - 50f))))
        addEntityObject(Coin(Location(r.nextFloat(WIDTH - 50f), r.nextFloat(UniversalHandler.HEIGHT - 50f))))
    }

    fun resetLevel() {
        clearObjects()
        addEntityObject(Game.mainPlayer!!)
    }

    fun setPlayerInfo(player: EntityPlayer?) {
        if (player != null) {
            addEntityObject(player)
            Game.mainPlayer = player
        }
        // addObject(player);
    }

    private fun <T : GameObject> renderEntity(g: Graphics2D, `object`: T) {
        val prevLoc = previousLocations[`object`.uniqueId]!!

        val entityRenderer = getRenderer(`object`.javaClass)
        val drawX =
            GameMathUtil.lerp(prevLoc.x, `object`.location.x, Game.elapsedTime.toFloat())
        val drawY =
            GameMathUtil.lerp(prevLoc.y, `object`.location.y, Game.elapsedTime.toFloat())
        entityRenderer.render(g, `object`, drawX, drawY)
        if (`object`.hasTrail && `object` !is Trail) {
            try {
                entityRegistryInUse.addEntityObject(
                    Trail(
                        Location(drawX,
                        drawY),
                        color = `object`.color!!,
                        width = `object`.width,
                        height = `object`.height,
                        life = 0.04f
                    )
                )
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("GameObject: $`object`", e)
            }
        }
    }

    fun render(g: Graphics2D) {
        for (gameObject in gameObjects.values) {
            renderEntity(g, gameObject)
        }
    }

    val objectsAndHashCode: Map<UUID, Int>
        get() {
            return gameObjects
                .filter { (uuid, _) -> gameObjects[uuid] != null && gameObjects[uuid] !is Trail && gameObjects[uuid] !is MenuParticle }
                .map { (uuid, obj) ->
                    uuid to obj.hashCode()
                }
                .toMap()
        }

    companion object {
        private val RENDER_REGISTRY: MutableMap<Class<out GameObject>, IEntityRenderer<out GameObject>> =
            HashMap<Class<out GameObject>, IEntityRenderer<out GameObject>>()
    }
}