package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.packets.PlayerUpdates.SendPlayerInfoPacket;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import org.lwjgl.glfw.GLFW;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

@Deprecated
public class JoystickThing {

    private final boolean[] joystick = new boolean[4];

    private boolean ifJoystick;

    private Game game;

    public JoystickThing(Handler handler, Game game) {
        this.game = game;
        GLFW.glfwInit();
        GLFW.glfwPollEvents();

        if (glfwJoystickPresent(GLFW_JOYSTICK_1)) {
            System.out.println("1 player connected controller " + glfwGetJoystickName(GLFW_JOYSTICK_1));
        }
    }

    public void tick() {
        if (glfwJoystickPresent(GLFW_JOYSTICK_1)) {
            boolean toUpdate = false;
            ByteBuffer buttons = GLFW.glfwGetJoystickButtons(GLFW_JOYSTICK_1);
            if (Game.mainPlayer != null && buttons != null) {


                List<Byte> buttonsList = new ArrayList<>();


                byte[] buttonsArray = getByteArrayFromByteBuffer(buttons);

                for (byte byteE : buttonsArray) {
                    buttonsList.add(byteE);
                }

                System.out.println(buttonsList);

                //UP
                if (buttons.get(10) != (byte) 0 && !joystick[0]) {
                    ifJoystick = true;
                    joystick[0] = true;
                    Game.mainPlayer.setVelY(-5);
                    toUpdate = true;

                } else {
                    if (ifJoystick) {
                        joystick[0] = false;
                    }
                }

                //RIGHT
                if (buttons.get(11) != (byte) 0 && !joystick[2]) {
                    ifJoystick = true;
                    joystick[2] = true;
                    Game.mainPlayer.setVelX(5);
                    toUpdate = true;
                } else {
                    if (ifJoystick) {
                        joystick[2] = false;
                    }
                }

                //DOWN
                if (buttons.get(12) != (byte) 0 && !joystick[1]) {
                    ifJoystick = true;
                    joystick[1] = true;
                    Game.mainPlayer.setVelY(5);
                    toUpdate = true;
                } else {
                    if (ifJoystick) {
                        joystick[1] = false;
                    }
                }

                //LEFT
                if (buttons.get(13) != (byte) 0 && !joystick[3]) {
                    ifJoystick = true;
                    joystick[3] = true;
                    Game.mainPlayer.setVelX(-5);
                    toUpdate = true;
                } else {
                    if (ifJoystick) {
                        joystick[3] = false;
                    }
                }
                //TODO Add joystick support

            }
            if(toUpdate) update();
        }
    }

    private void update() {
        if(Game.gameState == Game.STATE.Game || Game.gameState == Game.STATE.InServer || Game.gameState == Game.STATE.Hosting) {
            UniversalHandler.getThingHandler().updatePlayerObject(Game.getServerClientObject(),Game.mainPlayer);
            System.out.println("Updated " + Game.mainPlayer);
        }

        if(Game.gameState == Game.STATE.InServer) {
            Game.sendPacket(new SendPlayerInfoPacket(new UniversalPlayer(Game.mainPlayer)));
        }
    }

    private static byte[] getByteArrayFromByteBuffer(ByteBuffer byteBuffer) {
        byte[] bytesArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytesArray, 0, bytesArray.length);
        return bytesArray;
    }
}
