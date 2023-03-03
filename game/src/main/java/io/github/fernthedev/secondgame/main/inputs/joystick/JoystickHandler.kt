package io.github.fernthedev.secondgame.main.inputs.joystick

import com.github.fernthedev.universal.entity.NewGsonGameObject
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.inputs.InputHandler
import io.github.fernthedev.secondgame.main.inputs.InputType

class JoystickHandler {
    private val joystick1: JInputJoystick? = null
    private val xDeadzone = 20
    private val yDeadzone = 20
    var oldHorizontal = 0
    var oldVertical = 0
    fun tick() {
//
//        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
//
//
//        for (Controller controller : controllers) {
//            /* Remember to poll each one */
//            controller.poll();
//
//            /* Get the controllers event queue */
//
//
//        }
//
//
//        if (joystick1 != null && joystick1.getController() != null) {
//            joystick1.pollController();
//
//            if (joystick1.isControllerConnected()) {
//
//                if (!connectedController[0] && joystick1.isControllerConnected()) {
//                    connectedController[0] = true;
//                    Game.getStaticLogger().info("Connected player one to " + joystick1.getControllerName());
//                }
//
//                if (connectedController[0] && !joystick1.isControllerConnected()) {
//                    connectedController[0] = false;
//                    Game.getStaticLogger().info("Disconnected player one to " + joystick1.getControllerName());
//                }
//
//                // Left controller joystick
//                float xValueLeftJoystick = joystick1.getXAxisValue();
//                float yValueLeftJoystick = joystick1.getYAxisValue();
//
//// Right controller joystick
//
//// stick type controller
//                if (joystick1.getControllerType() == Controller.Type.GAMEPAD) {
//                    // Right controller joystick
//
//
//                    double horizontal = 0;
//                    double vertical = 0;
//
//                    int playerVelMultiplier = UniversalHandler.PLAYER_VEL_MULTIPLIER;
//
//                    if (xValueLeftJoystick <= -xDeadzone || xValueLeftJoystick >= xDeadzone) {
//
//
//                        if (xValueLeftJoystick <= -xDeadzone) {
//                            Game.getStaticLogger().info(1 + " assuming right");
//                            horizontal += xDeadzone * playerVelMultiplier;
//
//
//                            if (oldHorizontal != horizontal) {
//                                Game.getStaticLogger().info("Updating 5 horizontal");
//                                oldHorizontal = (int) horizontal;
//                            }
//                        }
//
//                        if (xValueLeftJoystick >= xDeadzone) {
//                            Game.getStaticLogger().info(2 + " assuming left");
//                            horizontal += (xValueLeftJoystick * playerVelMultiplier);
//
//                            if (oldHorizontal != horizontal) {
//                                Game.getStaticLogger().info("Updating -5 horizontal");
//                                oldHorizontal = (int) horizontal;
//                            }
//                        }
//                    }
//
//                    if (yValueLeftJoystick <= -yDeadzone || yValueLeftJoystick >= yDeadzone) {
//
//                        if (yValueLeftJoystick <= -yDeadzone) {
//                            Game.getStaticLogger().info(3 + " assuming down " + yValueLeftJoystick);
//                            vertical += yValueLeftJoystick * playerVelMultiplier;
//
//                            if (oldVertical != vertical) {
//                                Game.getStaticLogger().info("Updating 5 vertical");
//                                oldVertical = (int) vertical;
//                            }
//                        }
//                        if (yValueLeftJoystick >= yDeadzone) {
//                            //Game.getLogger().info(4 + " assuming up" + yValuePercentageLeftJoystick);
//
//                            vertical += (yValueLeftJoystick * playerVelMultiplier);
//
//                            if (oldVertical != vertical) {
//                                Game.getStaticLogger().info("Updating -5 vertical");
//                                oldVertical = (int) vertical;
//                            }
//                        }
//                    }
//
//
//                    if (Game.getScreen() == null && (Game.Game.mainPlayer.getVelX() != horizontal || Game.Game.mainPlayer.getVelY() != vertical)) {
//                        updatePlayer(vertical, horizontal);
//                    }
//
//
//                }
//// gamepad type controller
//                // Right controller joystick
//
//
//            } else {
//                InputHandler.inputType = InputType.KEYBOARD;
//            }
//        }
    }

    fun updatePlayer(vertical: Float, horizontal: Float) {
        InputHandler.inputType = InputType.JOYSTICK
        Game.loggerImpl.info("Updating player")
        Game.mainPlayer?.velY = (vertical)
        Game.mainPlayer?.velX = (horizontal)
        if (Game.screen == null && Game.client != null) {
            Game.updateWorld()
        }
    }

    companion object {
        val connectedController = BooleanArray(4)
    }
}