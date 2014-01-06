package org.TexasTorque.TexasTorque2014.io.dependency;

import org.TexasTorque.TexasTorque2014.io.*;

public class RobotOutputState
{
    private int lightState;
    
    //----- Pneumatics -----
    private boolean compressorEnabled;
    private boolean highGear;

    //----- Drive Motors -----
    private double frontLeftMotorSpeed;
    private double rearLeftMotorSpeed;
    private double frontRightMotorSpeed;
    private double rearRightMotorSpeed;
    
    public RobotOutputState(RobotOutput output)
    {   
        lightState = output.getLightState();
        //----- Pneumatics -----
        compressorEnabled = output.getCompressorEnabled();
        highGear = output.getShiftState();
        
        //----- Drive Motors -----
        frontLeftMotorSpeed = output.getFrontLeftMotorSpeed();
        rearLeftMotorSpeed = output.getRearLeftMotorSpeed();
        frontRightMotorSpeed = output.getFrontRightMotorSpeed();
        rearRightMotorSpeed = output.getRearRightMotorSpeed();
    }
    public synchronized void setDriveMotors(double leftMotorSpeed, double rightMotorSpeed)
    {
        frontLeftMotorSpeed = leftMotorSpeed;
        rearLeftMotorSpeed = leftMotorSpeed;
        frontRightMotorSpeed = rightMotorSpeed;
        rearRightMotorSpeed = rightMotorSpeed;
    }
    public synchronized void setLightsState(int state)
    {
        lightState = state;
    }
    public synchronized void setShifters(boolean highGear)
    {
        this.highGear = highGear;
    }
    
    public double getFrontLeftMotorSpeed()
    {
        return frontLeftMotorSpeed;
    }
    public double getRearLeftMotorSpeed()
    {
        return rearLeftMotorSpeed;
    }
    public double getFrontRightMotorSpeed()
    {
        return frontRightMotorSpeed;
    }
    public double getRearRightMotorSpeed()
    {
        return rearRightMotorSpeed;
    }
    public boolean getCompressorEnabled()
    {
        return compressorEnabled;
    }
    public boolean getShiftState()
    {
        return highGear;
    }
    public int getLightState()
    {
        return lightState;
    }
}
