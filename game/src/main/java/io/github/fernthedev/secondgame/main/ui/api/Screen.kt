package io.github.fernthedev.secondgame.main.ui.api

import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.entities.MenuParticle
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.util.*

abstract class Screen(private val title: String) {

    var parentScreen: Screen? = null

    private var yButtonIncrement = resetY()

    val DEFAULT_BACK_BUTTON by lazy { ScreenButton("Back", null, null) { returnToParentScreen() } }

    fun incrementY(increment: Int): Int {
        yButtonIncrement += increment + buttonSpacing
        return yButtonIncrement
    }

    fun resetY(): Int {
        return buttonYStart() - buttonSpacing.also { yButtonIncrement = it }
    }


    protected var textFont = ScreenFont(Font("arial", Font.BOLD, 50), Color.WHITE)


    var buttonList: MutableList<ScreenButton> = ArrayList()
    fun addButton(button: ScreenButton) {
        buttonList.add(button)
    }

    protected abstract fun draw(g: Graphics2D)
    fun buttonX(screenButton: ScreenButton): Int {
        return xCenter - screenButton.buttonSize.width / 2
    }

    fun buttonYStart(): Int {
        return buttonYStart
    }

    fun render(g: Graphics2D) {
        buttonList.clear()
        resetY()
        draw(g)
        g.font = textFont.font
        g.color = textFont.color

        g.drawString(
            title,
            UniversalHandler.WIDTH / 2 - textFont.size / 2 * title.length,
            (UniversalHandler.HEIGHT - UniversalHandler.HEIGHT * 0.85).toInt()
        )
        if (StaticHandler.isDebug()) {
            g.drawRect(
                UniversalHandler.WIDTH / 2 - textFont.size / 2 * title.length,
                (UniversalHandler.HEIGHT - (UniversalHandler.HEIGHT - UniversalHandler.HEIGHT * 0.2)).toInt(),
                15,
                15
            )
        }
        for (button in buttonList) {
            button.parentScreen = this
            val x = buttonX(button)
            val y: Int = yButtonIncrement
            if (button.render(g, Location(x.toFloat(), y.toFloat()))) {
                incrementY(button.buttonSize.height)
            }
        }
    }

    open fun returnToParentScreen() {
        Game.screen = parentScreen
    }

    companion object {
        private const val buttonYStart: Int = (UniversalHandler.HEIGHT - UniversalHandler.HEIGHT * 0.7).toInt()
        private const val xCenter: Int = UniversalHandler.WIDTH / 2
        @JvmStatic
        protected fun addMenuParticles() {
            val r: Random = UniversalHandler.RANDOM
            Game.staticEntityRegistry.clearObjects()
            val WIDTH: Int = UniversalHandler.WIDTH
            val HEIGHT: Int = UniversalHandler.HEIGHT
            var amount = r.nextInt(15)
            if (amount < 10) amount = 10
            for (i in 0 until amount) {
                Game.staticEntityRegistry.addEntityObject(
                    MenuParticle(
                        Location(
                        r.nextFloat(WIDTH - 40f) + 20f,
                        r.nextFloat(HEIGHT - 40f) + 20f,
                        )
                    )
                )
            }
        }

        const val buttonSpacing = 10
        var setParentScreenOnSet = true
    }
}