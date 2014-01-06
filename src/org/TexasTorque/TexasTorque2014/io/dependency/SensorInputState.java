package org.TexasTorque.TexasTorque2014.io.dependency;

import org.TexasTorque.TexasTorque2014.io.SensorInput;

public class SensorInputState
{
    //----- Encoder -----
    private double leftDriveEncoder;
    private double rightDriveEncoder;
    private double leftDriveEncoderVelocity;
    private double rightDriveEncoderVelocity;
    private double leftDriveEncoderAcceleration;
    private double rightDriveEncoderAcceleration;

    //----- Analog -----
    private double pressureSensor;
    private double gyroAngle;

    public SensorInputState(SensorInput input)
    {
        //----- Encoders/Counters -----
        leftDriveEncoder = input.getLeftDriveEncoder();
        rightDriveEncoder = input.getRightDriveEncoder();
        leftDriveEncoderVelocity = input.getLeftDriveEncoderRate();
        rightDriveEncoderVelocity = input.getRightDriveEncoderRate();
        leftDriveEncoderAcceleration = input.getLeftDriveEncoderAcceleration();
        rightDriveEncoderAcceleration = input.getRightDriveEncoderAcceleration();
        
        //----- Gyro -----
        gyroAngle = input.getGyroAngle();
        
        //----- Misc -----
        pressureSensor = input.getPSI();
    }
    
    public double getLeftDriveEncoder()
    {
        return leftDriveEncoder;
    }
    
    public double getRightDriveEncoder()
    {
        return rightDriveEncoder;
    }
    
    public double getLeftDriveEncoderRate()
    {
        return leftDriveEncoderVelocity;
    }
    
    public double getRightDriveEncoderRate()
    {
        return rightDriveEncoderVelocity;
    }
    
    public double getLeftDriveEncoderAcceleration()
    {
        return leftDriveEncoderAcceleration;
    }
    
    public double getRightDriveEncoderAcceleration()
    {
        return rightDriveEncoderAcceleration;
    }
    
    public double getPSI()
    {
        return pressureSensor;
    }
    
    public double getGyroAngle()
    {
        return gyroAngle;
    }
}
