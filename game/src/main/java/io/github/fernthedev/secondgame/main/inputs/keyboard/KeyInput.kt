//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package io.github.fernthedev.secondgame.main.inputs.keyboard

import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket
import com.github.fernthedev.universal.UniversalHandler
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.inputs.InputHandler
import io.github.fernthedev.secondgame.main.inputs.InputType
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class KeyInput    //        glfwPollEvents();

/*GLFW.glfwInit();
   GLFW.glfwPollEvents();

   if (glfwJoystickPresent(GLFW_JOYSTICK_1)) {
       Game.getLogger().info("connected controller " + glfwGetJoystickName(GLFW_JOYSTICK_1));
   }*/(private val game: Game) : KeyAdapter() {
    private val keyButtonMap: MutableMap<Int, KeyButton> = HashMap()
    private var toUpdate = false
    override fun keyPressed(e: KeyEvent) {
        InputHandler.inputType = InputType.KEYBOARD
        //Game.getLogger().info("Some key pressed ");
        val key = e.keyCode
        registerKey(key, true)
        if (key == KeyEvent.VK_P) {
            if (Game.screen == null) {
                Game.paused = !Game.paused
            }
        }
        if (key == KeyEvent.VK_ESCAPE) game.stop()
    }

    override fun keyReleased(e: KeyEvent) {
        val key = e.keyCode
        registerKey(key, false)
    }

    private fun registerKey(key: Int): KeyButton {
        var button = keyButtonMap[key]
        if (button == null) {
            button = KeyButton(key, false)
            keyButtonMap[key] = button
        }
        return button
    }

    private fun registerKey(key: Int, held: Boolean): KeyButton {
        var button = keyButtonMap[key]
        if (button == null) {
            button = KeyButton(key, held)
            keyButtonMap[key] = button
        } else {
            button.held = held
        }
        return button
    }

    fun tick() {

        // Game.getLogger().info("Thing! " + gameObjectList.size());
        if (Game.mainPlayer != null && InputHandler.inputType == InputType.KEYBOARD) {
            var velX = 0
            var velY = 0
            val velMultiplier = UniversalHandler.PLAYER_VEL_MULTIPLIER
            if (registerKey(KeyEvent.VK_W).held) {
                velY += -velMultiplier
            }
            if (registerKey(KeyEvent.VK_S).held) {
                velY += velMultiplier
            }
            if (registerKey(KeyEvent.VK_D).held) {
                velX += velMultiplier
            }
            if (registerKey(KeyEvent.VK_A).held) {
                velX += -velMultiplier
            }
            if (Game.mainPlayer?.velX != velX.toFloat() || Game.mainPlayer?.velY != velY.toFloat()) toUpdate = true

            //VERTICAL
//            if (!keyDown[0] && !keyDown[1] && Game.mainPlayer.getVelY() != 0 && InputHandler.inputType == InputType.KEYBOARD) {
//                Game.mainPlayer.setVelY(0);
//                toUpdate = true;
//            }
//
//            //HORIZONTAL
//            if (!keyDown[2] && !keyDown[3] && Game.mainPlayer.getVelX() != 0 && InputHandler.inputType == InputType.KEYBOARD) {
//                Game.mainPlayer.setVelX(0);
//                toUpdate = true;
//            }
            if (toUpdate) {
                Game.mainPlayer!!.velX = (velX.toFloat())
                Game.mainPlayer!!.velY = (velY.toFloat())
                Game.staticEntityRegistry.setPlayerInfo(Game.mainPlayer)
                update()
            }

        }
    }

    private fun update() {
        toUpdate = false
        if (Game.screen == null && Game.client?.isRegistered == true && Game.mainPlayer != null) {
            Game.client!!.sendObject(
                SendToServerPlayerInfoPacket(
                    Game.mainPlayer!!,
                    Game.staticEntityRegistry.objectsAndHashCode
                )
            )
            Game.loggerImpl.debug("Updated player")
        }
    }
}