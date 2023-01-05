//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package io.github.fernthedev.secondgame.main

import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

class BufferedImageLoader {
    private var image: BufferedImage? = null
    fun loadImage(path: String?): BufferedImage? {
        try {
            image = ImageIO.read(this.javaClass.getResource(path))
        } catch (var3: IOException) {
            var3.printStackTrace()
        }
        return image
    }
}