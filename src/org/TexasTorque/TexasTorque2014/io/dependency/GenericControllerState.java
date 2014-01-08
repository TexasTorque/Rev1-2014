package org.TexasTorque.TexasTorque2014.io.dependency;

import org.TexasTorque.TorqueLib.util.GenericController;

public class GenericControllerState {

    private boolean isLogitechController;
    private double[] rawAxis;
    private boolean[] buttons;
    //-----Joystick indicies-----
    private static int LEFT_X = 0;
    private static int LEFT_Y = 1;
    private static int RIGHT_X = 2;
    private static int RIGHT_Y = 3;
    //-----Button Indicies-----
    private static int START = 0;
    private static int BACK = 1;
    private static int BOTTOM_ACTION = 2;
    private static int LEFT_ACTION = 3;
    private static int RIGHT_ACTION = 4;
    private static int TOP_ACTION = 5;
    private static int LEFT_DPAD = 6;
    private static int RIGHT_DPAD = 7;
    private static int RIGHT_TRIGGER = 8;
    private static int LEFT_TRIGGER = 9;
    private static int RIGHT_BUMPER = 10;
    private static int LEFT_BUMPER = 11;
    private static int RIGHT_STICK = 12;
    private static int LEFT_STICK = 13;

    public GenericControllerState(GenericController controller) {
        isLogitechController = controller.isLogitech();
        rawAxis = new double[4];
        buttons = new boolean[14];
        //-----Joysticks-----
        rawAxis[LEFT_X] = controller.getLeftXAxis();
        rawAxis[LEFT_Y] = controller.getLeftYAxis();
        rawAxis[RIGHT_X] = controller.getRightXAxis();
        rawAxis[RIGHT_Y] = controller.getRightYAxis();
        //-----Buttons (Trigger and D-Pad Inclusive)
        buttons[START] = controller.getRightCenterButton();
        buttons[BACK] = controller.getLeftCenterButton();
        buttons[BOTTOM_ACTION] = controller.getBottomActionButton();
        buttons[LEFT_ACTION] = controller.getLeftActionButton();
        buttons[RIGHT_ACTION] = controller.getRightActionButton();
        buttons[TOP_ACTION] = controller.getTopActionButton();
        buttons[LEFT_DPAD] = controller.getLeftDPAD();
        buttons[RIGHT_DPAD] = controller.getRightDPAD();
        buttons[RIGHT_TRIGGER] = controller.getBottomRightBumper();
        buttons[LEFT_TRIGGER] = controller.getBottomLeftBumper();
        buttons[RIGHT_BUMPER] = controller.getTopRightBumper();
        buttons[LEFT_BUMPER] = controller.getTopLeftBumper();
        buttons[RIGHT_STICK] = controller.getRightStickClick();
        buttons[LEFT_STICK] = controller.getLeftStickClick();
    }

    public synchronized boolean getLogitech() {
        return isLogitechController;
    }

    public synchronized double getLeftYAxis() {
        return rawAxis[1];
    }

    public synchronized double getLeftXAxis() {
        return rawAxis[0];
    }

    public synchronized double getRightYAxis() {
        return rawAxis[3];
    }

    public synchronized double getRightXAxis() {
        return rawAxis[2];
    }

    public synchronized boolean getLeftDPAD() {
        return buttons[LEFT_DPAD];
    }

    public synchronized boolean getRightDPAD() {
        return buttons[RIGHT_DPAD];
    }

    public synchronized boolean getLeftStickClick() {
        return buttons[LEFT_STICK];
    }

    public synchronized boolean getRightStickClick() {
        return buttons[RIGHT_STICK];
    }

    public synchronized boolean getTopLeftBumper() {
        return buttons[LEFT_BUMPER];
    }

    public synchronized boolean getTopRightBumper() {
        return buttons[RIGHT_BUMPER];
    }

    public synchronized boolean getBottomLeftBumper() {
        return buttons[LEFT_TRIGGER];
    }

    public synchronized boolean getBottomRightBumper() {
        return buttons[RIGHT_TRIGGER];
    }

    public synchronized boolean getLeftCenterButton() {
        return buttons[BACK];
    }

    public synchronized boolean getRightCenterButton() {
        return buttons[START];
    }

    public synchronized boolean getLeftActionButton() {
        return buttons[LEFT_ACTION];
    }

    public synchronized boolean getTopActionButton() {
        return buttons[TOP_ACTION];
    }

    public synchronized boolean getRightActionButton() {
        return buttons[RIGHT_ACTION];
    }

    public synchronized boolean getBottomActionButton() {
        return buttons[BOTTOM_ACTION];
    }
}
