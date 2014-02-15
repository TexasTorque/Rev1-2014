package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class Intake extends TorqueSubsystem {
    private static Intake instance;
    
    private double frontIntakeSpeed;
    private double rearIntakeSpeed;
    private double frontTiltSpeed;
    private double rearTiltSpeed;
    
    private double desiredFrontTiltAngle;
    private double desiredRearTiltAngle;
    
    private TorquePID frontTiltPID;
    private TorquePID rearTiltPID;
    
    public static double upAngle;
    public static double downAngle;
    public static double inAngle;
    public static double intakeSpeed;
    public static double outtakeSpeed;
    
    public static Intake getInstance()
    {
        return (instance == null) ? instance = new Intake() : instance;
    }
    
    public Intake()
    {
        super();
        
        frontTiltPID = new TorquePID();
        rearTiltPID = new TorquePID();
        
        frontIntakeSpeed = Constants.MOTOR_STOPPED;
        rearIntakeSpeed = Constants.MOTOR_STOPPED;
        frontTiltSpeed = Constants.MOTOR_STOPPED;
        rearTiltSpeed = Constants.MOTOR_STOPPED;
        
        desiredFrontTiltAngle = 0.0;
        desiredRearTiltAngle = 0.0;
        
    }

    public void run() {
        double currentFrontAngle = sensorInput.getFrontIntakeTiltAngle();
        double currentRearAngle = sensorInput.getRearIntakeTiltAngle();
        
        frontTiltSpeed = frontTiltPID.calculate(currentFrontAngle);
        rearTiltSpeed = rearTiltPID.calculate(currentRearAngle);
        
        setToRobot();
    }
    
    public void setFrontAngle(double angle)
    {
        if (angle != desiredFrontTiltAngle)
        {
            desiredFrontTiltAngle = angle;
            frontTiltPID.setSetpoint(desiredFrontTiltAngle);
        }
    }
    
    public void setRearAngle(double angle)
    {
        if (angle != desiredRearTiltAngle)
        {
            desiredRearTiltAngle = angle;
            rearTiltPID.setSetpoint(desiredRearTiltAngle);
        }
    }
    
    public void setFrontIntakeSpeed(double speed)
    {
        frontIntakeSpeed = speed;
    }
    
    public void setRearIntakeSpeed(double speed)
    {
        rearIntakeSpeed = speed;
    }

    public void setToRobot() {
        robotOutput.setIntakeMotors(frontIntakeSpeed, rearIntakeSpeed, frontTiltSpeed, rearTiltSpeed);
    }

    public void loadParameters() {
        intakeSpeed = params.getAsDouble("I_IntakeSpeed", 1.0);
        outtakeSpeed = params.getAsDouble("I_OuttakeSpeed", -1.0);
        downAngle = params.getAsDouble("I_DownAngle", 45);
        upAngle = params.getAsDouble("I_UpAngle", 90);
        inAngle = params.getAsDouble("I_InAngle", 110);
    }

    public String logData() { //Have not implemented logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }
}
