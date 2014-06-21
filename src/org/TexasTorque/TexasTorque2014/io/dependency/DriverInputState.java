package org.TexasTorque.TexasTorque2014.io.dependency;

import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.DriverInput;
import org.TexasTorque.TorqueLib.util.TorqueToggle;

public class DriverInputState {

    public GenericControllerState driveControllerState;
    private GenericControllerState operatorControllerState;
    private double autonDelay;
    private int autonMode;
    private boolean inOverrideState;
    private boolean catapultStopAngle;

    private Hashtable autonomousData = new Hashtable();

    private TorqueToggle driveBaseMode;

    public DriverInputState() {
        driveBaseMode = new TorqueToggle();
        inOverrideState = false;
    }

    public void updateState(DriverInput input) {
        driveControllerState = new GenericControllerState(input.getDriverController());
        operatorControllerState = new GenericControllerState(input.getOperatorController());
        autonDelay = input.getAutonomousDelay();
        autonMode = input.getAutonomousMode();
        driveBaseMode.calc(driveControllerState.getLeftStickClick());

        autonomousData.clear();
    }

    public void updateAutonData(Hashtable table) {
        inOverrideState = false;
        autonomousData = table;

        autonomousData.put("CatapultAngle", Boolean.FALSE);
    }

    public synchronized boolean isAuton() {
        return !autonomousData.isEmpty();
    }

    public synchronized boolean getAutonBool(String key, boolean def) {
        if (autonomousData.containsKey(key)) {
            return ((Boolean) autonomousData.get(key)).booleanValue();
        }
        return def;
    }

    public synchronized double getAutonDouble(String key, double def) {
        if (autonomousData.containsKey(key)) {
            return ((Double) autonomousData.get(key)).doubleValue();
        }
        return def;
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
    public synchronized double getYAxis() {
        return driveControllerState.getLeftYAxis();
    }

    public synchronized double getRotation() {
        double axis = driveControllerState.getRightXAxis();
        // ^ (5/2) fit
        axis = Math.sqrt(Math.abs(axis * axis * axis * axis * axis)) * ((axis > 0) ? 1 : -1);

        return axis;
    }

    public synchronized boolean hasInput() {
        return (Math.abs(getYAxis()) > Constants.Y_AXIS_DEADBAND || Math.abs(getRotation()) > Constants.ROTATION_DEADBAND);
    }

    public synchronized boolean getDriveMode() {
        return (driveControllerState.getTopLeftBumper() || driveControllerState.getTopRightBumper());
    }

//---------- Manipulator ----------  
    public synchronized boolean WinchStop() {
        return operatorControllerState.getLeftActionButton();
    }

    public synchronized boolean shoot() {
        return (operatorControllerState.getRightDPAD() || operatorControllerState.getLeftDPAD()) && (operatorControllerState.getRightActionButton());
    }

    public synchronized boolean catapultReset() {
        return (operatorControllerState.getRightDPAD() || operatorControllerState.getLeftDPAD()) && operatorControllerState.getTopActionButton();
    }

    public synchronized double frontIntakeOverride() {
        return operatorControllerState.getRightYAxis();
    }

    public synchronized double rearIntakeOverride() {
        return operatorControllerState.getLeftYAxis();
    }

    public synchronized boolean frontIntaking() {
        return operatorControllerState.getTopRightBumper();
        //return (!operatorControllerState.getTopActionButton() && operatorControllerState.getTopRightBumper());
    }

    public synchronized boolean frontOuttaking() {
        return operatorControllerState.getBottomRightBumper();
        //return (!operatorControllerState.getTopActionButton() && operatorControllerState.getTopLeftBumper());
    }

    public synchronized boolean rearIntaking() {
        return operatorControllerState.getTopLeftBumper();
        //return (operatorControllerState.getTopActionButton() && operatorControllerState.getTopRightBumper());
    }

    public synchronized boolean rearOuttaking() {
        return operatorControllerState.getBottomLeftBumper();
        //return (operatorControllerState.getTopActionButton() && operatorControllerState.getTopLeftBumper());
    }

    public synchronized boolean catching() {
        return operatorControllerState.getBottomActionButton();
    }

    public synchronized boolean readyToShoot() {
        return operatorControllerState.getRightActionButton();
    }

    public synchronized boolean getShooterStandoffs() {
        return operatorControllerState.getLeftActionButton();
    }

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

    public synchronized boolean frontIntakeRollOverride() {
        return frontIntaking();
    }

    public synchronized boolean getHoopToggle() {
        return operatorControllerState.getLeftStickClick();
    }

    public synchronized boolean frontOuttakeRollOverride() {
        return frontOuttaking();
    }

    public synchronized boolean rearIntakeRollOverride() {
        return rearIntaking();
    }

    public synchronized boolean rearOuttakeRollOverride() {
        return rearOuttaking();
    }

    public synchronized boolean releaseOverride() {
        return operatorControllerState.getRightDPAD() || operatorControllerState.getLeftDPAD();
    }

    public synchronized boolean winchOverride() {
        return operatorControllerState.getRightActionButton();
    }

    public synchronized void pushToDashboard() {
    }
}
