package org.TexasTorque.TexasTorque2014.io.dependency;

import org.TexasTorque.TexasTorque2014.io.*;

public class RobotOutputState {

    //----- Pneumatics -----
    private boolean compressorEnabled;
    private boolean driveBaseMode;
    private boolean backHoop;

    //----- Drive Motors -----
    private double leftFrontMotorSpeed;
    private double leftRearMotorSpeed;
    private double rightFrontMotorSpeed;
    private double rightRearMotorSpeed;

    //----- Intake -----
    private double frontIntakeMotorSpeed;
    private double rearIntakeMotorSpeed;
    private double frontIntakeTiltMotorSpeed;
    private double rearIntakeTiltMotorSpeed;

    //----- Catapult -----
    private double catapultMotorSpeed;
    private boolean winchSolinoid;

    //lights
    private int lightState;

    public RobotOutputState() {

    }

    public void updateState(RobotOutput output) {
        //----- Pneumatics -----
        compressorEnabled = output.getCompressorEnabled();
        driveBaseMode = output.getDriveBaseMode();
        backHoop = output.getHoop();

        //----- Drive Motors -----
        leftFrontMotorSpeed = output.getLeftFrontMotorSpeed();
        leftRearMotorSpeed = output.getLeftRearMotorSpeed();
        rightFrontMotorSpeed = output.getRightFrontMotorSpeed();
        rightRearMotorSpeed = output.getRightRearMotorSpeed();

        //----- Intake -----
        frontIntakeMotorSpeed = output.getFrontIntakeMotorSpeed();
        rearIntakeMotorSpeed = output.getRearIntakeMotorSpeed();
        frontIntakeTiltMotorSpeed = output.getFrontIntakeTiltMotorSpeed();
        rearIntakeTiltMotorSpeed = output.getRearIntakeTiltMotorSpeed();

        //----- Catapult -----
        catapultMotorSpeed = output.getCatapultMotorSpeed();
        winchSolinoid = output.getWinchSolinoid();

        //----- Lights -----
        lightState = output.getLightsState();
    }

    public synchronized void setDriveMotors(double leftFrontSpeed, double leftRearSpeed, double rightFrontSpeed, double rightRearSpeed) {
        leftFrontMotorSpeed = leftFrontSpeed;
        leftRearMotorSpeed = leftRearSpeed;
        rightFrontMotorSpeed = rightFrontSpeed;
        rightRearMotorSpeed = rightRearSpeed;
    }

    public double getLeftFrontMotorSpeed() {
        return leftFrontMotorSpeed;
    }

    public double getLeftRearMotorSpeed() {
        return leftRearMotorSpeed;
    }

    public double getRightFrontMotorSpeed() {
        return rightFrontMotorSpeed;
    }

    public double getRightRearMotorSpeed() {
        return rightRearMotorSpeed;
    }

    public boolean getCompressorEnabled() {
        return compressorEnabled;
    }

    public void setDriveBaseMode(boolean mode) {
        driveBaseMode = mode;
    }
    
    public boolean getDriveBaseMode() {
        return driveBaseMode;
    }

    public void setIntakeMotors(double frontRoller, double rearRoller, double frontTilt, double rearTilt) {
        frontIntakeMotorSpeed = frontRoller;
        rearIntakeMotorSpeed = rearRoller;
        frontIntakeTiltMotorSpeed = frontTilt;
        rearIntakeTiltMotorSpeed = rearTilt;
    }

    public double getFrontIntakeMotorSpeed() {
        return frontIntakeMotorSpeed;
    }

    public double getRearIntakeMotorSpeed() {
        return rearIntakeMotorSpeed;
    }

    public double getFrontIntakeTiltMotorSpeed() {
        return frontIntakeTiltMotorSpeed;
    }

    public double getRearIntakeTiltMotorSpeed() {
        return rearIntakeTiltMotorSpeed;
    }

    public void setCatapultMotor(double speed) {
        catapultMotorSpeed = speed;
    }

    public void setWinchSolinoid(boolean state) {
        winchSolinoid = state;
    }

    public boolean getWinchSolinoid() {
        return winchSolinoid;
    }

    public double getCatapultMotorSpeed() {
        return catapultMotorSpeed;
    }

    public void setLightsState(int state) {
        lightState = state;
    }

    public int getLightsState() {
        return lightState;
    }
    
    public void setHoop(boolean position)
    {
        backHoop = position;
    }
    
    public boolean getHoop()
    {
        return backHoop;
    }
}
