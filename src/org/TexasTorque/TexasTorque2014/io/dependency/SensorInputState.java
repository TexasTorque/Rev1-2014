package org.TexasTorque.TexasTorque2014.io.dependency;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.util.Parameters;

public class SensorInputState {

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
    private double catapultEncoder;

    //----- Analog -----
    private double pressureSensor;
    private double gyroAngle;
    private double frontIntakeTiltPotentiometer;
    private double rearIntakeTiltPotentiometer;
    private double frontIntakeTiltVoltage;
    private double rearIntakeTiltVoltage;
    
    //----- Angles -----
    private double minFrontIntakeAngle;
    private double maxFrontIntakeAngle;
    private double minRearIntakeAngle;
    private double maxRearIntakeAngle;
    
    Parameters params;

    public SensorInputState() {
        params = Parameters.getTeleopInstance();
    }

    public void updateState(SensorInput input) {
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
        catapultEncoder = input.getCatapultEncoder();

        //----- Potentiometers -----
        frontIntakeTiltPotentiometer = input.getFrontIntakeTiltPotentiometer();
        rearIntakeTiltPotentiometer = input.getRearIntakeTiltPotentiometer();
        frontIntakeTiltVoltage = input.getFrontIntakeTiltVoltage();
        rearIntakeTiltVoltage = input.getRearIntakeTiltVoltage();
        
        //----- Gyro -----
        gyroAngle = input.getGyroAngle();

        //----- Misc -----
        pressureSensor = input.getPSI();
    }

    public double getLeftFrontDriveEncoder() {
        return leftFrontDriveEncoder;
    }

    public double getRightFrontDriveEncoder() {
        return rightFrontDriveEncoder;
    }

    public double getLeftFrontDriveEncoderRate() {
        return leftFrontDriveEncoderVelocity;
    }

    public double getRightFrontDriveEncoderRate() {
        return rightFrontDriveEncoderVelocity;
    }

    public double getLeftFrontDriveEncoderAcceleration() {
        return leftFrontDriveEncoderAcceleration;
    }

    public double getRightFrontDriveEncoderAcceleration() {
        return rightFrontDriveEncoderAcceleration;
    }

    public double getLeftRearDriveEncoder() {
        return leftRearDriveEncoder;
    }

    public double getRightRearDriveEncoder() {
        return rightRearDriveEncoder;
    }

    public double getLeftRearDriveEncoderRate() {
        return leftRearDriveEncoderVelocity;
    }

    public double getRightRearDriveEncoderRate() {
        return rightRearDriveEncoderVelocity;
    }

    public double getLeftRearDriveEncoderAcceleration() {
        return leftRearDriveEncoderAcceleration;
    }

    public double getRightRearDriveEncoderAcceleration() {
        return rightRearDriveEncoderAcceleration;
    }

    public double getPSI() {
        return pressureSensor;
    }

    public double getGyroAngle() {
        return gyroAngle;
    }
    
    public double getFrontIntakeTiltPotentiometer()
    {
        return frontIntakeTiltPotentiometer;
    }
    
    public double getRearIntakeTiltPotentiometer()
    {
        return rearIntakeTiltPotentiometer;
    }
    
    public double getRearIntakeTiltVoltage()
    {
        return rearIntakeTiltVoltage;
    }
    
    public double getFrontIntakeTiltVoltage()
    {
        return frontIntakeTiltVoltage;
    }

    public double getFrontIntakeTiltAngle()
    {
        return getFrontIntakeTiltPotentiometer()*(maxFrontIntakeAngle - minFrontIntakeAngle) + minFrontIntakeAngle;
    }
    
    public double getRearIntakeTiltAngle()
    {
        return getRearIntakeTiltPotentiometer()*(maxRearIntakeAngle - minRearIntakeAngle) + minRearIntakeAngle;
    }
    
    public double getCatapultEncoder()
    {
        return catapultEncoder;
    }
    
    public void pushToDashboard() {
        SmartDashboard.putNumber("FrontLeftSpeed", getLeftFrontDriveEncoderRate());
        SmartDashboard.putNumber("FrontRightSpeed", getRightFrontDriveEncoderRate());
        SmartDashboard.putNumber("RearLeftSpeed", getLeftRearDriveEncoderRate());
        SmartDashboard.putNumber("RearRightSpeed", getRightRearDriveEncoderRate());
        SmartDashboard.putNumber("FrontIntakeVoltage", frontIntakeTiltVoltage);
        SmartDashboard.putNumber("RearIntakeVoltage", rearIntakeTiltVoltage);
        SmartDashboard.putNumber("CatapultEncoder", catapultEncoder);

    }
    public void loadParamaters()
    {
        minFrontIntakeAngle = params.getAsDouble("I_MinFrontIntakeAngle", 0.0);
        maxFrontIntakeAngle = params.getAsDouble("I_MaxFrontIntakeAngle", 100.0);
        minRearIntakeAngle = params.getAsDouble("I_MinRearIntakeAngle", 0.0);
        maxRearIntakeAngle = params.getAsDouble("I_MaxRearIntakeAngle", 100.0);
    }
}
