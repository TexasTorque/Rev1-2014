package org.TexasTorque.TexasTorque2014.io.dependency;

import org.TexasTorque.TexasTorque2014.io.*;

public class RobotOutputState {

    private int lightState;
    //----- Pneumatics -----
    private boolean compressorEnabled;
    private boolean highGear;
    //----- Drive Motors -----
    private double frontLeftMotorSpeed;
    private double rearLeftMotorSpeed;
    private double frontRightMotorSpeed;
    private double rearRightMotorSpeed;
    private double frontLeftAngleMotorSpeed;
    private double rearLeftAngleMotorSpeed;
    private double frontRightAngleMotorSpeed;
    private double rearRightAngleMotorSpeed;

    public RobotOutputState(RobotOutput output) {
        /*
        lightState = output.getLightState();
        //----- Pneumatics -----
        compressorEnabled = output.getCompressorEnabled();
        highGear = output.getShiftState();

        //----- Drive Motors -----
        frontLeftMotorSpeed = output.getFrontLeftMotorSpeed();
        rearLeftMotorSpeed = output.getRearLeftMotorSpeed();
        frontRightMotorSpeed = output.getFrontRightMotorSpeed();
        rearRightMotorSpeed = output.getRearRightMotorSpeed();
        frontLeftAngleMotorSpeed = output.getFrontLeftAngleMotorSpeed();
        rearLeftAngleMotorSpeed = output.getRearLeftAngleMotorSpeed();
        frontRightAngleMotorSpeed = output.getFrontRightAngleMotorSpeed();
        rearRightAngleMotorSpeed = output.getRearRightAngleMotorSpeed();
        */
    }

    public void update(RobotOutput output) {
        lightState = output.getLightState();
        //----- Pneumatics -----
        compressorEnabled = output.getCompressorEnabled();
        highGear = output.getShiftState();

        //----- Drive Motors -----
        frontLeftMotorSpeed = output.getFrontLeftMotorSpeed();
        rearLeftMotorSpeed = output.getRearLeftMotorSpeed();
        frontRightMotorSpeed = output.getFrontRightMotorSpeed();
        rearRightMotorSpeed = output.getRearRightMotorSpeed();
        frontLeftAngleMotorSpeed = output.getFrontLeftAngleMotorSpeed();
        rearLeftAngleMotorSpeed = output.getRearLeftAngleMotorSpeed();
        frontRightAngleMotorSpeed = output.getFrontRightAngleMotorSpeed();
        rearRightAngleMotorSpeed = output.getRearRightAngleMotorSpeed();
    }

    public synchronized void setDriveSpeedMotors(double frontRightMotorSpeed, double frontLeftMotorSpeed, double rearRightMotorSpeed, double rearLeftMotorSpeed) {
        this.frontLeftMotorSpeed = frontLeftMotorSpeed;
        this.rearLeftMotorSpeed = rearLeftMotorSpeed;
        this.frontRightMotorSpeed = frontRightMotorSpeed;
        this.rearRightMotorSpeed = rearRightMotorSpeed;
    }

    public synchronized void setDriveAngleMotors(double frontRightMotorSpeed, double frontLeftMotorSpeed, double rearRightMotorSpeed, double rearLeftMotorSpeed) {
        this.frontLeftAngleMotorSpeed = frontLeftMotorSpeed;
        this.rearLeftAngleMotorSpeed = rearLeftMotorSpeed;
        this.frontRightAngleMotorSpeed = frontRightMotorSpeed;
        this.rearRightAngleMotorSpeed = rearRightMotorSpeed;
    }

    public synchronized void setLightsState(int state) {
        lightState = state;
    }

    public synchronized void setShifters(boolean highGear) {
        this.highGear = highGear;
    }

    public double getFrontLeftMotorSpeed() {
        return frontLeftMotorSpeed;
    }

    public double getRearLeftMotorSpeed() {
        return rearLeftMotorSpeed;
    }

    public double getFrontRightMotorSpeed() {
        return frontRightMotorSpeed;
    }

    public double getRearRightMotorSpeed() {
        return rearRightMotorSpeed;
    }

    public double getFrontLeftAngleMotorSpeed() {
        return frontLeftAngleMotorSpeed;
    }

    public double getRearLeftAngleMotorSpeed() {
        return rearLeftAngleMotorSpeed;
    }

    public double getFrontRightAngleMotorSpeed() {
        return frontRightAngleMotorSpeed;
    }

    public double getRearRightAngleMotorSpeed() {
        return rearRightAngleMotorSpeed;
    }

    public boolean getCompressorEnabled() {
        return compressorEnabled;
    }

    public boolean getShiftState() {
        return highGear;
    }

    public int getLightState() {
        return lightState;
    }
}
