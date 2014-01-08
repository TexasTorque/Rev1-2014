package org.TexasTorque.TexasTorque2014.io.dependency;

import org.TexasTorque.TexasTorque2014.io.DriverInput;

public class DriverInputState {

    public GenericControllerState driveControllerState;
    private GenericControllerState operatorControllerState;
    private double autonDelay;
    private int autonMode;
    private boolean inOverrideState;

    public DriverInputState(DriverInput input) {
        driveControllerState = new GenericControllerState(input.getDriverController());
        operatorControllerState = new GenericControllerState(input.getOperatorController());
        autonDelay = input.getAutonomousDelay();
        autonMode = input.getAutonomousMode();
        inOverrideState = false;
    }

    public void update(DriverInput input) {
        driveControllerState = new GenericControllerState(input.getDriverController());
        operatorControllerState = new GenericControllerState(input.getOperatorController());
        autonDelay = input.getAutonomousDelay();
        autonMode = input.getAutonomousMode();
        inOverrideState = false;
    }

    public synchronized void setAutonomousDelay(double autonDelay) {
        this.autonDelay = autonDelay;
    }

    public synchronized void setAutonomousMode(int autonMode) {
        this.autonMode = autonMode;
    }

    public synchronized void setOverrideState(boolean overrides) {
        this.inOverrideState = overrides;
    }

    public synchronized double getAutonomousDelay() {
        return autonDelay;
    }

    public synchronized int getAutonomousMode() {
        return autonMode;
    }

    public synchronized boolean resetSensors() {
        return operatorControllerState.getBottomActionButton();
    }

//---------- Drivebase ----------
    public synchronized double getThrottle() {
        return -1 * driveControllerState.getLeftYAxis();
    }

    public synchronized double getTurn() {
        return driveControllerState.getRightXAxis();
    }
    
    public synchronized double getRotation()
    {
        return operatorControllerState.getLeftXAxis();
    }

    public synchronized boolean shiftHighGear() {
        return driveControllerState.getTopLeftBumper();
    }

    public synchronized boolean hasInput() {
        return (Math.abs(getThrottle()) > .07 || Math.abs(getTurn()) > .07);
    }

//---------- Manipulator ----------    
    public synchronized boolean restoreToDefault() {
        return operatorControllerState.getBottomLeftBumper();
    }

//---------- Overrides ----------
    public synchronized boolean overrideState() {
        if (operatorControllerState.getLeftCenterButton()) {
            inOverrideState = true;
        } else if (operatorControllerState.getRightCenterButton()) {
            inOverrideState = false;
        }

        return inOverrideState;
    }
}
