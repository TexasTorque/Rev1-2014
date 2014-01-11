package org.TexasTorque.TexasTorque2014.io.dependency;

import org.TexasTorque.TexasTorque2014.io.*;

public class RobotOutputState
{
    private int lightState;
    
    //----- Pneumatics -----
    private boolean compressorEnabled;
    private boolean highGear;

    //----- Drive Motors -----
    private double leftFrontMotorSpeed;
    private double leftRearMotorSpeed;
    private double rightFrontMotorSpeed;
    private double rightRearMotorSpeed;
    
    public RobotOutputState(RobotOutput output)
    {   
        lightState = output.getLightState();
        //----- Pneumatics -----
        compressorEnabled = output.getCompressorEnabled();
        highGear = output.getShiftState();
        
        //----- Drive Motors -----
        leftFrontMotorSpeed = output.getLeftFrontMotorSpeed();
        leftRearMotorSpeed = output.getLeftRearMotorSpeed();
        rightFrontMotorSpeed = output.getRightFrontMotorSpeed();
        rightRearMotorSpeed = output.getRightRearMotorSpeed();
    }
    public synchronized void setDriveMotors(double leftFrontMotorSpeed, double leftRightMotorSpeed, double rightFrontMotorSpeed, double rightRearMotorSpeed)
    {
        leftFrontMotorSpeed = leftFrontMotorSpeed;
        leftRearMotorSpeed = leftRearMotorSpeed;
        rightFrontMotorSpeed = rightRearMotorSpeed;
        rightRearMotorSpeed = rightFrontMotorSpeed;
    }
    public synchronized void setLightsState(int state)
    {
        lightState = state;
    }
    public synchronized void setShifters(boolean highGear)
    {
        this.highGear = highGear;
    }
    
    public double getLeftFrontMotorSpeed()
    {
        return leftFrontMotorSpeed;
    }
    public double getLeftRearMotorSpeed()
    {
        return leftRearMotorSpeed;
    }
    public double getRightFrontMotorSpeed()
    {
        return rightFrontMotorSpeed;
    }
    public double getRightRearMotorSpeed()
    {
        return rightRearMotorSpeed;
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
