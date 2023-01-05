package com.github.fernthedev

import kotlin.math.max


object GameMathUtil {
    @JvmStatic
    fun clamp(v: Float, min: Float, max: Float): Float {
        return when {
            v < max -> max(v, min)
            else -> max
        }
    }

    @JvmStatic
    fun lerp(start: Float, end: Float, time: Float): Float {
        return start + (end - start) * time
    }

}