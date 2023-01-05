package io.github.fernthedev.secondgame.main

import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket
import org.lwjgl.glfw.GLFW
import java.nio.ByteBuffer

@Deprecated("")
class JoystickThing(private val game: Game) {
    private val joystick = BooleanArray(4)
    private var ifJoystick = false

    init {
        GLFW.glfwInit()
        GLFW.glfwPollEvents()
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1)) {
            Game.loggerImpl.info("1 player connected controller " + GLFW.glfwGetJoystickName(GLFW.GLFW_JOYSTICK_1))
        }
    }

    fun tick() {
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1)) {
            var toUpdate = false
            val buttons = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1)
            if (Game.mainPlayer != null && buttons != null) {
                val buttonsList: MutableList<Byte> = ArrayList()
                val buttonsArray = getByteArrayFromByteBuffer(buttons)
                for (byteE in buttonsArray) {
                    buttonsList.add(byteE)
                }
                Game.loggerImpl.info(buttonsList.toString())

                //UP
                if (buttons[10] != 0.toByte() && !joystick[0]) {
                    ifJoystick = true
                    joystick[0] = true
                    Game.mainPlayer!!.velY = -5f
                    toUpdate = true
                } else {
                    if (ifJoystick) {
                        joystick[0] = false
                    }
                }

                //RIGHT
                if (buttons[11] != 0.toByte() && !joystick[2]) {
                    ifJoystick = true
                    joystick[2] = true
                    Game.mainPlayer!!.velX = 5f
                    toUpdate = true
                } else {
                    if (ifJoystick) {
                        joystick[2] = false
                    }
                }

                //DOWN
                if (buttons[12] != 0.toByte() && !joystick[1]) {
                    ifJoystick = true
                    joystick[1] = true
                    Game.mainPlayer!!.velY = 5f
                    toUpdate = true
                } else {
                    if (ifJoystick) {
                        joystick[1] = false
                    }
                }

                //LEFT
                if (buttons[13] != 0.toByte() && !joystick[3]) {
                    ifJoystick = true
                    joystick[3] = true
                    Game.mainPlayer!!.velX = -5f
                    toUpdate = true
                } else {
                    if (ifJoystick) {
                        joystick[3] = false
                    }
                }
                //TODO Add joystick support
            }
            if (toUpdate) update()
        }
    }

    private fun update() {
        if (Game.screen == null) {
            Game.staticEntityRegistry.addEntityObject(Game.mainPlayer!!)
            Game.loggerImpl.info("Updated " + Game.mainPlayer)
        }
        if (Game.screen == null && Game.client != null) {
            Game.client!!.sendObject(
                SendToServerPlayerInfoPacket(
                    Game.mainPlayer!!,
                    Game.staticEntityRegistry.objectsAndHashCode
                )
            )
        }
    }

    companion object {
        private fun getByteArrayFromByteBuffer(byteBuffer: ByteBuffer): ByteArray {
            val bytesArray = ByteArray(byteBuffer.remaining())
            byteBuffer[bytesArray, 0, bytesArray.size]
            return bytesArray
        }
    }
}