package io.github.fernthedev.secondgame.main

import java.awt.Canvas
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import kotlin.system.exitProcess

internal class Window(width: Int, height: Int, title: String?, game: Game) : Canvas() {
    private val game: Game

    init {
        val frame = JFrame(title)
        frame.preferredSize = Dimension(width, height)
        frame.maximumSize = Dimension(width, height)
        frame.minimumSize = Dimension(width, height)
        this.game = game
        val loader = BufferedImageLoader()
        val image = loader.loadImage("/icon.png")
        frame.iconImage = image
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                close()
                // close sockets, etc
            }
        })
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.isResizable = false
        frame.setLocationRelativeTo(null)
        frame.add(game)
        frame.isVisible = true


        //frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        game.start()
    }

    private fun close() {
        game.loggerImpl.info("Closing")
        game.stop()
        exitProcess(0)
    }

    companion object {
        private const val serialVersionUID = -8639908922292785986L
    }
}