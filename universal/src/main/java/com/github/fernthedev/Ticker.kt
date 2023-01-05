package com.github.fernthedev

import com.github.fernthedev.universal.UniversalHandler
import kotlinx.coroutines.runBlocking

open class Ticker(private val tick: suspend () -> Unit): Runnable {
    constructor(t: Runnable) : this({ t.run() })
    constructor(t: TickRunnable) : this(suspend { t.tick() })

    override fun run() {
        runBlocking {
            var lastTime = System.nanoTime()
            val amountOfTicks = 60.0
            val ns = 1000000000 / amountOfTicks
            var delta = 0.0
            while (UniversalHandler.running) {
                val now = System.nanoTime()
                delta += (now - lastTime) / ns
                lastTime = now
                while (delta >= 1) {
                    tick()
                    delta--
                }
                try {
                    Thread.sleep(UniversalHandler.TICK_WAIT.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    Thread.currentThread().interrupt()
                }
            }
        }
    }
}