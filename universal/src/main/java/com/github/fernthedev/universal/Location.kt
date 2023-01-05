package com.github.fernthedev.universal

data class Location(
    var x: Float,
    var y: Float,
) : Cloneable {
    fun copyFrom(location: Location) {
        x = location.x
        y = location.y
    }
}