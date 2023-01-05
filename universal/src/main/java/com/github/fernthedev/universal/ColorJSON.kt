package com.github.fernthedev.universal

import java.awt.Color

data class ColorJSON(val r: Int,
    val g: Int,
    val b: Int,
    val a: Int) {
    constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha)

    fun toColor(): Color = Color(r, g, b, a)
}
