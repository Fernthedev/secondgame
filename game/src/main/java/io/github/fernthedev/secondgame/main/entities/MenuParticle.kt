package io.github.fernthedev.secondgame.main.entities

import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import java.awt.Color

class MenuParticle(
    location: Location
) : GameObject(location = location, height = 16f, width = 16f, color = Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)), entityId = EntityID.MENU_PARTICLE), Cloneable {
    private var dir = 0

    init {
        dir = r.nextInt(2)
        velX = (r.nextInt(7 - -7) + -7).toFloat()
        velY = (r.nextInt(7 - -7) + -7).toFloat()
        if (velX == 0f) velX = 1f
        if (velY == 0f) velY = 1f
    }



    override fun tick() {
        if (velX == 0f) velX = (r.nextInt(7 - -7) + -7).toFloat()
        if (velY == 0f) velY = (r.nextInt(7 - -7) + -7).toFloat()
    }

    companion object {
        private val r = UniversalHandler.RANDOM
    }
}