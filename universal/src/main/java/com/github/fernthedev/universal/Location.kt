package com.github.fernthedev.universal

import kotlin.math.abs

data class Location(
    var x: Float,
    var y: Float,
) : Cloneable {
    fun copyFrom(location: Location) {
        x = location.x
        y = location.y
    }
}

fun Location.approx(other: Location): Boolean {
    return abs(other.x - x) < 4 && abs(other.y - y) < 4
}