package io.github.fernthedev.secondgame.main.ui.api

import java.awt.Color
import java.awt.Font


data class ScreenFont(
    val font: Font,
    val color: Color
) {

    val size: Int
        get() = font.size
}