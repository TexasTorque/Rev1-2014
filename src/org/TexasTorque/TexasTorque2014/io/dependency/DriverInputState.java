package org.TexasTorque.TexasTorque2014.io.dependency;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

        autonomousData.put("driveMode", new Boolean(Constants.TRACTION_MODE));

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
    public synchronized double getXAxis() {
        return driveControllerState.getLeftXAxis();
    }

    public synchronized double getYAxis() {
        return driveControllerState.getLeftYAxis();
    }

    public synchronized double getRotation() {
        return driveControllerState.getRightXAxis();
    }

    public synchronized boolean hasInput() {
        return (Math.abs(getXAxis()) > Constants.X_AXIS_DEADBAND || Math.abs(getYAxis()) > Constants.Y_AXIS_DEADBAND || Math.abs(getRotation()) > Constants.ROTATION_DEADBAND);
    }

    public synchronized boolean getDriveMode() {
        SmartDashboard.putBoolean("DriveMode", !driveControllerState.getBottomRightBumper());
        return (!driveControllerState.getTopLeftBumper());
    }

//---------- Manipulator ----------  
    public synchronized boolean WinchStop() {
        return operatorControllerState.getLeftActionButton();
    }

    public synchronized boolean ChooChooOverride() {
        return (operatorControllerState.getRightDPAD() || operatorControllerState.getLeftDPAD()) && (operatorControllerState.getRightActionButton());
    }

    public synchronized boolean ChooChooReset() {
        return (operatorControllerState.getRightDPAD() || operatorControllerState.getLeftDPAD()) && operatorControllerState.getTopActionButton();
    }

    public synchronized boolean getCatapultStopAngle() {
        if (operatorControllerState.getLeftDPAD()) {
            catapultStopAngle = true;
        } else if (operatorControllerState.getRightDPAD()) {
            catapultStopAngle = false;
        }
        return catapultStopAngle;
    }
    public synchronized boolean winchOverride() {
        return operatorControllerState.getRightActionButton();
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

    public synchronized boolean shoot() {
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
        return operatorControllerState.getBottomActionButton();
    }
    public synchronized boolean shortShotOverride() {
        return operatorControllerState.getLeftDPAD();
    }

    public synchronized void pushToDashboard() {
        SmartDashboard.putBoolean("LeftDPAD", operatorControllerState.getLeftDPAD());
        SmartDashboard.putBoolean("RightDPAD", operatorControllerState.getRightDPAD());

    }

}
