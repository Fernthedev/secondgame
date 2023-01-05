package io.github.fernthedev.secondgame.main.inputs.joystick;

import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.inputs.InputHandler;
import io.github.fernthedev.secondgame.main.inputs.InputType;


public class JoystickHandler {

    private JInputJoystick joystick1;

    private final int xDeadzone = 20;
    private final int yDeadzone = 20;


    int oldHorizontal = 0,oldVertical = 0;

    public static boolean[] getConnectedController() {
        return connectedController;
    }

    private static boolean connectedController[] = new boolean[4];

    public JoystickHandler() {

//        return;
//        /* Create an event object for the underlying plugin to populate */
//        Event event = new Event();
//
//        /* Get the available controllers */
//        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
//
//        joystick1 = new JInputJoystick(Controller.Type.STICK, Controller.Type.GAMEPAD, 0);
//
//        for (Controller controller : controllers) {
//            System.out.println("H");
//            /* Remember to poll each one */
//            controller.poll();
//            System.out.println("T");
//
//            /* Get the controllers event queue */
//            EventQueue queue = controllers[0].getEventQueue();
//
//            /* For each object in the queue */
//            while (queue.getNextEvent(event)) {
//                /* Get event component */
//                Component comp = event.getComponent();
//
//                /* Process event (your awesome code) */
//
//                /*
//                 * Create a strug buffer and put in it, the controller name,
//                 * the time stamp of the event, the name of the component
//                 * that changed and the new value.
//                 *
//                 * Note that the timestamp is a relative thing, not
//                 * absolute, we can tell what order events happened in
//                 * across controllers this way. We can not use it to tell
//                 * exactly *when* an event happened just the order.
//                 */
//                StringBuilder buffer = new StringBuilder(controller
//                        .getName());
//                buffer.append(" at ");
//                buffer.append(event.getNanos()).append(", ");
//                buffer.append(comp.getName()).append(" changed to ");
//                float value = event.getValue();
//
//                /*
//                 * Check the type of the component and display an
//                 * appropriate value
//                 */
//                if (comp.isAnalog()) {
//                    buffer.append(value);
//                } else {
//                    if (value == 1.0f) {
//                        buffer.append("On");
//                    } else {
//                        buffer.append("Off");
//                    }
//                }
//
//
//                Game.getStaticLogger().info(buffer.toString());
//            }
//        }
    }

    public void tick() {
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
//                    if (Game.getScreen() == null && (Game.getMainPlayer().getVelX() != horizontal || Game.getMainPlayer().getVelY() != vertical)) {
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

    public void updatePlayer(double vertical,double horizontal) {
        InputHandler.inputType = InputType.JOYSTICK;

        Game.getLogger().info("Updating player");

        Game.getMainPlayer().setVelY(vertical);
        Game.getMainPlayer().setVelX(horizontal);

        if(Game.getScreen() == null && Game.getClient() != null) {
            Game.getClient().sendObject(new SendToServerPlayerInfoPacket(Game.getMainPlayer(), Game.getStaticEntityRegistry().getObjectsAndHashCode()));
        }
    }


}


