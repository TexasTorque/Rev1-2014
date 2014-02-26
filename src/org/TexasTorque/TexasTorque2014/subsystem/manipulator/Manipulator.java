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

            if (driverInput.frontIntaking()) {
                intaking = true;
                frontIntake();
            } else if (driverInput.frontOuttaking()) {
                intaking = true;
                frontOuttake();
            } else if (driverInput.rearIntaking()) {
                intaking = true;
                rearIntake();
            } else if (driverInput.rearOuttaking()) {
                intaking = true;
                rearOuttake();
            }
            
            setShooterStandoffs(driverInput.getShooterStandoffs());
            
            if (driverInput.catching()) {
                catchBall();
            } else if (driverInput.shoot()) {
                shoot();
            } else if (driverInput.shootHigh()) {
                shootHigh();
            }else if (driverInput.restoreToDefault()) {
                restoreDefaultPositions();
            } else if (intaking) {
            } else {
                resetIntakes();
                resetShooter();
            }
            
            SmartDashboard.putBoolean("Intakeing", intaking);
            
            setToRobot();
            
        } else {
            calcOverrides();
        }
        
        intake.run();
        catapult.run();
    }

    public void setToRobot() {
        intake.setToRobot();
        catapult.setToRobot();
    }

    public void frontIntake() {
        SmartDashboard.putNumber("IntakeState", 1.0);
        intake.setFrontIntakeSpeed(Intake.intakeSpeed);
        intake.setFrontAngle(Intake.downAngle);
        intake.setRearAngle(Intake.inAngle);
    }

    public void rearIntake() {
        SmartDashboard.putNumber("IntakeState", 2.0);
        intake.setRearIntakeSpeed(Intake.intakeSpeed);
        intake.setRearAngle(Intake.downAngle);
        intake.setFrontAngle(Intake.inAngle);
    }

    public void frontOuttake() {
        SmartDashboard.putNumber("IntakeState", 3.0);
        intake.setFrontIntakeSpeed(Intake.outtakeSpeed);
        intake.setFrontAngle(Intake.upAngle);
        intake.setRearIntakeSpeed(Intake.intakeSpeed);
        intake.setRearAngle(Intake.inAngle);
    }

    public void rearOuttake() {
        SmartDashboard.putNumber("IntakeState", 4.0);
        intake.setRearIntakeSpeed(Intake.outtakeSpeed);
        intake.setRearAngle(Intake.upAngle);
        intake.setFrontIntakeSpeed(Intake.intakeSpeed);
        intake.setFrontAngle(Intake.inAngle);
    }

    public void resetFrontIntake() {
        intake.setFrontAngle(Intake.upAngle);
        intake.setFrontIntakeSpeed(Constants.MOTOR_STOPPED);
    }

    public void resetRearIntake() {
        intake.setRearAngle(Intake.upAngle);
        intake.setRearIntakeSpeed(Constants.MOTOR_STOPPED);
    }

    public void catchBall() {
        intake.setFrontAngle(Intake.downAngle);
        intake.setRearAngle(Intake.downAngle);
        intake.setFrontIntakeSpeed(Constants.MOTOR_STOPPED);
        intake.setRearIntakeSpeed(Constants.MOTOR_STOPPED);
    }

    public void resetIntakes() {
        resetFrontIntake();
        resetRearIntake();
    }

    public void shoot() {
        intake.setFrontAngle(Intake.downAngle);
        intake.setRearAngle(Intake.downAngle);
        catapult.setPosition(Catapult.shootPosition);
        catapult.setStandoffs(Constants.FAR_SHOT);
    }
    
    public void shootHigh() {
        intake.setFrontAngle(Intake.downAngle);
        intake.setRearAngle(Intake.downAngle);
        catapult.setPosition(Catapult.shootPosition);
        catapult.setStandoffs(Constants.HIGH_SHOT);
    }

    public void resetShooter() {
        catapult.setPosition(Catapult.standardPosition);
    }
    
    public void setShooterStandoffs(boolean highshot)
    {
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

    public void loadParameters() {
        intake.loadParameters();
        catapult.loadParameters();
    }

    private void calcOverrides() {
    }

    public void restoreDefaultPositions() {
    }
}
