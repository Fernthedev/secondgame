package io.github.fernthedev.secondgame.main.ui.api

import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.universal.Location
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

class ScreenButton {
    private var string: String
    var parentScreen: Screen? = null


    val onClick: Runnable

    var defaultButtonWidth = 200
    var defaultButtonHeight = 64
    var defaultButtonWidthSpace = 30


    var buttonFont = ScreenFont(Font("arial", Font.BOLD, 30), Color.WHITE)
    var buttonSize: Size

    private var location: Location?
    lateinit var renderedLocation: Location

    constructor(string: String, buttonSize: Size? = null, location: Location? = null, onClick: Runnable) {
        this.string = string
        this.onClick = onClick
        this.buttonSize = buttonSize ?: sizeDependingLength
        this.location = location
    }

    constructor(
        string: String,
        buttonFont: ScreenFont,
        buttonSize: Size?,
        location: Location?,
        onClick: Runnable,
    ) {
        this.string = string
        this.onClick = onClick
        this.buttonFont = buttonFont
        this.buttonSize = buttonSize ?: sizeDependingLength
        this.location = location
    }

    /**
     *
     * @param g
     * @param loc default location
     * @return true if using the location defined in parameter
     */
    fun render(g: Graphics2D, loc: Location): Boolean {
        var returnStatus = false

        var buttonDrawLocation = location

        if (buttonDrawLocation == null) {
            buttonDrawLocation = loc
            returnStatus = true
        }


        renderedLocation = buttonDrawLocation
        // 70
        val stringX =
            (buttonDrawLocation.x + buttonSize.width / 2.0f - buttonFont.size * string.length * 0.26f).toInt() //((((string.length() / buttonFont.getSize())) + (buttonSize.getWidth())) + buttonSize.getWidth()/2); //- ((buttonSize.getWidth() / 2) ) ; //( (width / 2) - (size + (string.length() * 2) - string.length() / 2));
        val stringY: Int =
            (buttonDrawLocation.y + (buttonSize.height - (buttonFont.size - buttonSize.height / 8f))).toInt()
        g.font = buttonFont.font
        g.color = buttonFont.color
        g.drawRect(buttonDrawLocation.x.toInt(), buttonDrawLocation.y.toInt(), buttonSize.width, buttonSize.height)
        g.drawString(string, stringX, stringY)
        if (StaticHandler.isDebug()) g.drawRect(stringX, stringY, 15, 15)

        return returnStatus
    }

    protected val sizeDependingLength: Size
        get() {
            var width = defaultButtonWidth
            if (string.length * (buttonFont.size / 2) > (width - defaultButtonWidthSpace * 2) / 2) {
                width =
                    string.length * buttonFont.size / 2 + width / 2 //(string.length() + defaultButtonWidthSpace + (width/2)) * 2;
            }
            return Size(width, defaultButtonHeight)
        }
}