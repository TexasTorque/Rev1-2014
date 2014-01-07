package org.TexasTorque.TexasTorque2014.io.dependency;

import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;

public class SensorInputState
{
    //----- Encoder -----
    private double frontLeftDriveEncoder;
    private double frontRightDriveEncoder;
    private double frontLeftDriveEncoderVelocity;
    private double frontRightDriveEncoderVelocity;
    private double frontLeftDriveEncoderAcceleration;
    private double frontRightDriveEncoderAcceleration;
    private double rearLeftDriveEncoder;
    private double rearRightDriveEncoder;
    private double rearLeftDriveEncoderVelocity;
    private double rearRightDriveEncoderVelocity;
    private double rearLeftDriveEncoderAcceleration;
    private double rearRightDriveEncoderAcceleration;
    private double frontLeftDriveAngle;
    private double frontRightDriveAngle;
    private double rearLeftDriveAngle;
    private double rearRightDriveAngle;

    //----- Analog -----
    private double pressureSensor;
    private double gyroAngle;

    public SensorInputState(SensorInput input)
    {
        //----- Encoders/Counters -----
        frontLeftDriveEncoder = input.getFrontLeftDriveEncoder();
        frontRightDriveEncoder = input.getFrontRightDriveEncoder();
        frontLeftDriveEncoderVelocity = input.getFrontLeftDriveEncoderRate();
        frontRightDriveEncoderVelocity = input.getFrontRightDriveEncoderRate();
        frontLeftDriveEncoderAcceleration = input.getFrontLeftDriveEncoderAcceleration();
        frontRightDriveEncoderAcceleration = input.getFrontRightDriveEncoderAcceleration();
        rearLeftDriveEncoder = input.getRearLeftDriveEncoder();
        rearRightDriveEncoder = input.getRearRightDriveEncoder();
        rearLeftDriveEncoderVelocity = input.getRearLeftDriveEncoderRate();
        rearRightDriveEncoderVelocity = input.getRearRightDriveEncoderRate();
        rearLeftDriveEncoderAcceleration = input.getRearLeftDriveEncoderAcceleration();
        rearRightDriveEncoderAcceleration = input.getRearRightDriveEncoderAcceleration();
        frontLeftDriveAngle = input.getFrontLeftDriveEncoder() / Constants.ENCODER_RESOLUTION * Math.PI * 2;
        rearLeftDriveAngle = input.getRearLeftDriveEncoder() / Constants.ENCODER_RESOLUTION * Math.PI * 2;
        frontRightDriveAngle = input.getFrontRightDriveEncoder() / Constants.ENCODER_RESOLUTION * Math.PI * 2;
        rearRightDriveAngle = input.getRearRightDriveEncoder() / Constants.ENCODER_RESOLUTION * Math.PI * 2;
        
        //----- Gyro -----
        gyroAngle = input.getGyroAngle();
        
        //----- Misc -----
        pressureSensor = input.getPSI();
    }
    public double getFrontLeftDriveAngle()
    {
        return frontLeftDriveAngle;
    }
    public double getFrontRightDriveAngle()
    {
        return frontRightDriveAngle;
    }
    public double getRearLeftDriveAngle()
    {
        return rearLeftDriveAngle;
    }
    public double getRearRightDriveAngle()
    {
        return rearRightDriveAngle;
    }
    
    public double getFrontLeftDriveEncoder()
    {
        return frontLeftDriveEncoder;
    }
    
    public double getFrontRightDriveEncoder()
    {
        return frontRightDriveEncoder;
    }
    
    public double getFrontLeftDriveEncoderRate()
    {
        return frontLeftDriveEncoderVelocity;
    }
    
    public double getFrontRightDriveEncoderRate()
    {
        return frontRightDriveEncoderVelocity;
    }
    
    public double getFrontLeftDriveEncoderAcceleration()
    {
        return frontLeftDriveEncoderAcceleration;
    }
    
    public double getFrontRightDriveEncoderAcceleration()
    {
        return frontRightDriveEncoderAcceleration;
    }
    public double getRearLeftDriveEncoder()
    {
        return rearLeftDriveEncoder;
    }
    
    public double getRearRightDriveEncoder()
    {
        return rearRightDriveEncoder;
    }
    
    public double getRearLeftDriveEncoderRate()
    {
        return rearLeftDriveEncoderVelocity;
    }
    
    public double getRearRightDriveEncoderRate()
    {
        return rearRightDriveEncoderVelocity;
    }
    
    public double getRearLeftDriveEncoderAcceleration()
    {
        return rearLeftDriveEncoderAcceleration;
    }
    
    public double getRearRightDriveEncoderAcceleration()
    {
        return rearRightDriveEncoderAcceleration;
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
