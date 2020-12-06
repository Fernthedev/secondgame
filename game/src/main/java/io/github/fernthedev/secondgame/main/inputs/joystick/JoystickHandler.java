package io.github.fernthedev.secondgame.main.inputs.joystick;

import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.inputs.InputHandler;
import io.github.fernthedev.secondgame.main.inputs.InputType;


public class JoystickHandler {

    private Game game;
    private JInputJoystick joystick1;



    int oldHorizontal = 0,oldVertical = 0;

    public static boolean[] getConnectedController() {
        return connectedController;
    }

    private static boolean connectedController[] = new boolean[4];

    public JoystickHandler(Game game) {

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
//            /* Remember to poll each one */
//            controller.poll();
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
//                StringBuffer buffer = new StringBuffer(controller
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
//                System.out.println(buffer.toString());
//            }
//        }
    }

    public void tick() {
        return;
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
//                    System.out.println("Connected player one to " + joystick1.getControllerName());
//                }
//
//                if (connectedController[0] && !joystick1.isControllerConnected()) {
//                    connectedController[0] = false;
//                    System.out.println("Disconnected player one to " + joystick1.getControllerName());
//                }
//
//                // Left controller joystick
//                int xValuePercentageLeftJoystick = joystick1.getXAxisPercentage();
//                int yValuePercentageLeftJoystick = joystick1.getYAxisPercentage();
//
//// Right controller joystick
//                int xValuePercentageRightJoystick, yValuePercentageRightJoystick;
//
//// stick type controller
//                if (joystick1.getControllerType() == Controller.Type.STICK) {
//                    // Right controller joystick
//                    xValuePercentageRightJoystick = joystick1.getZAxisPercentage();
//                    yValuePercentageRightJoystick = joystick1.getZRotationPercentage();
//                }
//// gamepad type controller
//                else {
//                    boolean toUpdate = false;
//                    // Right controller joystick
//                    xValuePercentageRightJoystick = joystick1.getXRotationPercentage();
//                    yValuePercentageRightJoystick = joystick1.getYRotationPercentage();
//
//
//
//                    double horizontal = 0;
//                    double vertical = 0;
//
//
//                    if (!(xValuePercentageLeftJoystick >= 40 && xValuePercentageLeftJoystick <= 60)) {
//
//
//
//                        // If Z Axis exists.
//                        if (joystick1.componentExists(Component.Identifier.Axis.Z)) {
//                            int zAxisValuePercentage = joystick1.getZAxisPercentage();
//                        }
//
//
//                        if (xValuePercentageLeftJoystick >= 50 && xValuePercentageLeftJoystick <= 100) {
//                            System.out.println(1 + " assuming right");
//                            horizontal = (((double) xValuePercentageLeftJoystick / 100.0) * 5.0);
//
//
//                            if(oldHorizontal != horizontal) {
//                                System.out.println("Updating 5 horizontal");
//                                oldHorizontal = (int) horizontal;
//                                toUpdate = true;
//                            }
//                        }
//
//                        if (xValuePercentageLeftJoystick <= 50) {
//                            System.out.println(2 + " assuming left");
//                            horizontal = (((double) xValuePercentageLeftJoystick / 100.0) * 5.0) * -1;
//
//                            if(oldHorizontal != horizontal) {
//                                System.out.println("Updating -5 horizontal");
//                                oldHorizontal = (int) horizontal;
//                                toUpdate = true;
//                            }
//                        }
//                    }
//
//                    if (!(yValuePercentageLeftJoystick >= 40 && yValuePercentageLeftJoystick <= 60)) {
//
//                        if (yValuePercentageLeftJoystick >= 50 && yValuePercentageLeftJoystick <= 100) {
//                            System.out.println(3 + " assuming down " + yValuePercentageLeftJoystick);
//                            vertical = (((double) yValuePercentageLeftJoystick / 100.0) * 5.0);
//
//                            if(oldVertical != vertical) {
//                                System.out.println("Updating 5 vertical");
//                                oldVertical = (int) vertical;
//                                toUpdate = true;
//                            }
//                        }
//                        if (yValuePercentageLeftJoystick <= 50) {
//                            //System.out.println(4 + " assuming up" + yValuePercentageLeftJoystick);
//
//                             vertical = (((double) yValuePercentageLeftJoystick / 100.0) * 5.0) * -1;
//
//                            if(oldVertical != vertical) {
//                                System.out.println("Updating -5 vertical");
//                                oldVertical = (int) vertical;
//                                toUpdate = true;
//                            }
//                        }
//                    }
//
//                    if(yValuePercentageLeftJoystick >= 40 && yValuePercentageLeftJoystick <= 60) {
//                        if(oldVertical != vertical) {
//                            oldHorizontal = (int) horizontal;
//                            toUpdate = true;
//                        }
//                    }
//
//                    if(xValuePercentageLeftJoystick >= 40 && xValuePercentageLeftJoystick <= 60) {
//                        if(oldHorizontal != horizontal) {
//                            oldHorizontal = (int) horizontal;
//                            toUpdate = true;
//                        }
//                    }
//
//                    if(Game.getScreen() == null) {
//
//
//
//                        if(toUpdate) {
//                            updatePlayer(vertical,horizontal);
//                            update();
//                        }
//                    }
//                }
//            }else{
//                InputHandler.inputType = InputType.KEYBOARD;
//            }
//        }
    }

    public void updatePlayer(double vertical,double horizontal) {
        InputHandler.inputType = InputType.JOYSTICK;

        System.out.println("Updating player");

        Game.getMainPlayer().setVelY(vertical);
        Game.getMainPlayer().setVelX(horizontal);
    }

    public void update() {


        if(Game.getScreen() == null) {
            Game.getStaticEntityRegistry().addEntityObject(Game.getMainPlayer());
            System.out.println("Updated " + Game.getMainPlayer());
        }



        if(Game.getScreen() == null && Game.getClient() != null) {
            Game.getClient().sendObject(new SendToServerPlayerInfoPacket(Game.getMainPlayer(), Game.getStaticEntityRegistry().getObjectsAndHashCode()));
        }
    }
}


