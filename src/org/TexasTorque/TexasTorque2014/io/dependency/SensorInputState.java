package org.TexasTorque.TexasTorque2014.io.dependency;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.io.SensorInput;

public class SensorInputState
{
    //----- Encoder -----
    private double leftFrontDriveEncoder;
    private double rightFrontDriveEncoder;
    private double leftFrontDriveEncoderVelocity;
    private double rightFrontDriveEncoderVelocity;
    private double leftFrontDriveEncoderAcceleration;
    private double rightFrontDriveEncoderAcceleration;
    private double leftRearDriveEncoder;
    private double rightRearDriveEncoder;
    private double leftRearDriveEncoderVelocity;
    private double rightRearDriveEncoderVelocity;
    private double leftRearDriveEncoderAcceleration;
    private double rightRearDriveEncoderAcceleration;

    //----- Analog -----
    private double pressureSensor;
    private double gyroAngle;

    public SensorInputState()
    {
        
    }
    public synchronized void updateState(SensorInput input)
    {
        //----- Encoders/Counters -----
        leftFrontDriveEncoder = input.getLeftFrontDriveEncoder();
        rightFrontDriveEncoder = input.getRightFrontDriveEncoder();
        leftFrontDriveEncoderVelocity = input.getLeftFrontDriveEncoderRate();
        rightFrontDriveEncoderVelocity = input.getRightFrontDriveEncoderRate();
        leftFrontDriveEncoderAcceleration = input.getLeftFrontDriveEncoderAcceleration();
        rightFrontDriveEncoderAcceleration = input.getRightFrontDriveEncoderAcceleration();
        leftRearDriveEncoder = input.getLeftRearDriveEncoder();
        rightRearDriveEncoder = input.getRightRearDriveEncoder();
        leftRearDriveEncoderVelocity = input.getLeftRearDriveEncoderRate();
        rightRearDriveEncoderVelocity = input.getRightRearDriveEncoderRate();
        leftRearDriveEncoderAcceleration = input.getLeftRearDriveEncoderAcceleration();
        rightRearDriveEncoderAcceleration = input.getRightRearDriveEncoderAcceleration();
        
        //----- Gyro -----
        gyroAngle = input.getGyroAngle();
        
        //----- Misc -----
        pressureSensor = input.getPSI();
    }
    
    public synchronized double getLeftFrontDriveEncoder()
    {
        return leftFrontDriveEncoder;
    }
    
    public synchronized double getRightFrontDriveEncoder()
    {
        return rightFrontDriveEncoder;
    }
    
    public synchronized double getLeftFrontDriveEncoderRate()
    {
        return leftFrontDriveEncoderVelocity;
    }
    
    public synchronized double getRightFrontDriveEncoderRate()
    {
        return rightFrontDriveEncoderVelocity;
    }
    
    public synchronized double getLeftFrontDriveEncoderAcceleration()
    {
        return leftFrontDriveEncoderAcceleration;
    }
    
    public synchronized double getRightFrontDriveEncoderAcceleration()
    {
        return rightFrontDriveEncoderAcceleration;
    }
    
    public synchronized double getLeftRearDriveEncoder()
    {
        return leftRearDriveEncoder;
    }
    
    public synchronized double getRightRearDriveEncoder()
    {
        return rightRearDriveEncoder;
    }
    
    public synchronized double getLeftRearDriveEncoderRate()
    {
        return leftRearDriveEncoderVelocity;
    }
    
    public synchronized double getRightRearDriveEncoderRate()
    {
        return rightRearDriveEncoderVelocity;
    }
    
    public synchronized double getLeftRearDriveEncoderAcceleration()
    {
        return leftRearDriveEncoderAcceleration;
    }
    
    public synchronized double getRightRearDriveEncoderAcceleration()
    {
        return rightRearDriveEncoderAcceleration;
    }
    
    public synchronized double getPSI()
    {
        return pressureSensor;
    }
    
    public synchronized double getGyroAngle()
    {
        return gyroAngle;
    }
    
    public void pushToDashboard()
    {
        SmartDashboard.putNumber("FrontLeftSpeed", getLeftFrontDriveEncoderRate());
        SmartDashboard.putNumber("FrontRightSpeed", getRightFrontDriveEncoderRate());
        SmartDashboard.putNumber("RearLeftSpeed", getLeftRearDriveEncoderRate());
        SmartDashboard.putNumber("RearRightSpeed", getRightRearDriveEncoderRate());
        
    }
}
