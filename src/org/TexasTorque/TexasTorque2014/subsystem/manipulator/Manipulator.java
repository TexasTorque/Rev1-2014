package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;

public class Manipulator extends TorqueSubsystem {
    
    private static Manipulator instance;
    
    private Intake intake;
    private Catapult catapult;
    
    public static Manipulator getInstance() {
        return (instance == null) ? instance = new Manipulator() : instance;
    }
    
    private Manipulator() {
        super();
        
        intake = Intake.getInstance();
        catapult = Catapult.getInstance();
    }
    
    public void run() {
        if (!driverInput.overrideState()) {
            boolean intaking = false;
            //----- Normal Ops -----

            if (driverInput.frontIntaking() || driverInput.getAutonBool("frontIn", false)) {
                intaking = true;
                frontIntake();
            } else if (driverInput.frontOuttaking() || driverInput.getAutonBool("frontOut", false)) {
                intaking = true;
                frontOuttake();
            } else if (driverInput.rearIntaking() || driverInput.getAutonBool("rearIn", false)) {
                intaking = true;
                rearIntake();
            } else if (driverInput.rearOuttaking() || driverInput.getAutonBool("rearOut", false)) {
                intaking = true;
                rearOuttake();
            }
            
            if (driverInput.catching() || driverInput.getAutonBool("catch", false)) {
                catchBall();
            } else if (driverInput.shoot() || driverInput.getAutonBool("shoot", false)) {
                shoot();
            } else if (driverInput.shootHigh() || driverInput.getAutonBool("shootHigh", false)) {
                shootHigh();
            } else if (driverInput.restoreToDefault() || driverInput.getAutonBool("restore", false)) {
                restoreDefaultPositions();
            } else if (intaking) {
            } else {
                resetIntakes();
                resetShooter();
            }
            if (catapult.getIntakeDownOverride()) {
                intake.setFrontAngle(Intake.frontShootAngle);
                intake.setRearAngle(Intake.rearShootAngle);
            }
            
            SmartDashboard.putBoolean("Intakeing", intaking);
            
            intake.run();
            catapult.run();
            
            setToRobot();
            
        } else {
            calcOverrides();
            setToRobot();
        }
        
    }
    
    public void setToRobot() {
        intake.setToRobot();
        catapult.setToRobot();
    }
    
    public void frontIntake() {
        SmartDashboard.putNumber("IntakeState", 1.0);
        intake.setFrontIntakeSpeed(Intake.intakeSpeed);
        intake.setFrontAngle(Intake.frontDownAngle);
        intake.setRearAngle(Intake.inAngle);
    }
    
    public void rearIntake() {
        SmartDashboard.putNumber("IntakeState", 2.0);
        intake.setRearIntakeSpeed(Intake.intakeSpeed);
        intake.setRearAngle(Intake.rearDownAngle);
        intake.setFrontAngle(Intake.inAngle);
    }
    
    public void frontOuttake() {
        SmartDashboard.putNumber("IntakeState", 3.0);
        intake.setFrontIntakeSpeed(Intake.outtakeSpeed);
        intake.setFrontAngle(Intake.frontIntakeAngle);
        intake.setRearIntakeSpeed(Intake.intakeSpeed);
        intake.setRearAngle(Intake.inAngle);
    }
    
    public void rearOuttake() {
        SmartDashboard.putNumber("IntakeState", 4.0);
        intake.setRearIntakeSpeed(Intake.outtakeSpeed);
        intake.setRearAngle(Intake.rearUpAngle);
        intake.setFrontIntakeSpeed(Intake.intakeSpeed);
        intake.setFrontAngle(Intake.frontUpAngle);
    }
    
    public void resetFrontIntake() {
        intake.setFrontAngle(Intake.frontUpAngle);
        intake.setFrontIntakeSpeed(Constants.MOTOR_STOPPED);
    }
    
    public void resetRearIntake() {
        intake.setRearAngle(Intake.rearUpAngle);
        intake.setRearIntakeSpeed(Constants.MOTOR_STOPPED);
    }
    
    public void catchBall() {
        intake.setFrontAngle(Intake.frontCatchAngle);
        intake.setRearAngle(Intake.rearCatchAngle);
        intake.setFrontIntakeSpeed(Constants.MOTOR_STOPPED);
        intake.setRearIntakeSpeed(Constants.MOTOR_STOPPED);
    }
    
    public void resetIntakes() {
        resetFrontIntake();
        resetRearIntake();
    }
    
    public void shoot() {
        intake.setFrontAngle(Intake.frontShootAngle);
        intake.setRearAngle(Intake.rearShootAngle);
        catapult.setStandoffs(Constants.FAR_SHOT);
    }
    
    public void shootHigh() {
        intake.setFrontAngle(Intake.frontShootAngle);
        intake.setRearAngle(Intake.rearShootAngle);
        catapult.setStandoffs(Constants.HIGH_SHOT);
    }
    
    public void resetShooter() {
    }
    
    public void setShooterStandoffs(boolean highshot) {
        catapult.setStandoffs(highshot);
    }
    
    public String getKeyNames() {
        String names = "InOverrideState,";
        
        return names;
    }
    
    public String logData() {
        String data = driverInput.overrideState() + ",";
        
        return data;
    }
    
    public boolean isFired() {
        return catapult.isFired();
    }
    
    public void resetFired() {
        catapult.resetFired();
    }
    
    public boolean intakesDone() {
        return intake.isDone();
    }
    
    public boolean catapultReady() {
        return catapult.catapultReady();
    }
    
    public void loadParameters() {
        intake.loadParameters();
        catapult.loadParameters();
    }
    
    private void calcOverrides() {
        if (driverInput.ChooChooOverride()) {
            catapult.setMotorSpeed(Catapult.overrideSpeed);
        }
        
        if (Math.abs(driverInput.frontIntakeOverride()) > Constants.OVERRIDE_AXIS_DEADBAND) {
            intake.frontIntakeOverride(driverInput.frontIntakeOverride());
        }
        if (Math.abs(driverInput.rearIntakeOverride()) > Constants.OVERRIDE_AXIS_DEADBAND) {
            intake.rearIntakeOverride(driverInput.rearIntakeOverride());
        }
    }
    
    public void restoreDefaultPositions() {
    }
}
