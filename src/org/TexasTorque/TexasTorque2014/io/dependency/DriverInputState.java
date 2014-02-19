package org.TexasTorque.TexasTorque2014.io.dependency;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.DriverInput;
import org.TexasTorque.TorqueLib.util.TorqueToggle;

public class DriverInputState {

    public GenericControllerState driveControllerState;
    private GenericControllerState operatorControllerState;
    private double autonDelay;
    private int autonMode;
    private boolean inOverrideState;
    private TorqueToggle driveBaseMode;

    public DriverInputState() {
       driveBaseMode = new TorqueToggle();
    }

    public void updateState(DriverInput input) {
        driveControllerState = new GenericControllerState(input.getDriverController());
        operatorControllerState = new GenericControllerState(input.getOperatorController());
        autonDelay = input.getAutonomousDelay();
        autonMode = input.getAutonomousMode();
        inOverrideState = false;
        driveBaseMode.calc(driveControllerState.getLeftStickClick());
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
    
    public synchronized boolean getDriveMode()
    {
        //SmartDashboard.putBoolean("DriveMode", driveBaseMode.get() || operatorControllerState.getBottomRightBumper());
        return (driveBaseMode.get() || driveControllerState.getBottomRightBumper());
    }

//---------- Manipulator ----------  
    public synchronized boolean frontIntaking()
    {
        return (!operatorControllerState.getTopActionButton() && operatorControllerState.getTopRightBumper());
    }
    
    public synchronized boolean frontOuttaking()
    {
        return (!operatorControllerState.getTopActionButton() && operatorControllerState.getTopLeftBumper());
    }
    
    public synchronized boolean rearIntaking()
    {
        return (operatorControllerState.getTopActionButton() && operatorControllerState.getTopRightBumper());
    }
    
    public synchronized boolean rearOuttaking()
    {
        return (operatorControllerState.getTopActionButton() && operatorControllerState.getTopLeftBumper());
    }
    
    public synchronized boolean catching()
    {
        return operatorControllerState.getBottomActionButton();
    }
    
    public synchronized boolean shoot()
    {
        return operatorControllerState.getBottomRightBumper();
    }
    
    public synchronized boolean getShooterStandoffs()
    {
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

}
