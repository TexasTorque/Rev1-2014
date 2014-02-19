package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;

public class Manipulator extends TorqueSubsystem {

    private static Manipulator instance;

    private Drivebase drivebase;
    private Intake intake;
    private Catapult catapult;

    public static Manipulator getInstance() {
        return (instance == null) ? instance = new Manipulator() : instance;
    }

    private Manipulator() {
        super();

        drivebase = Drivebase.getInstance();
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
            } else {
                resetFrontIntake();
            }

            if (driverInput.rearIntaking()) {
                intaking = true;
                rearIntake();
            } else if (driverInput.rearOuttaking()) {
                intaking = true;
                rearOuttake();
            } else {
                resetRearIntake();
            }
            
            setShooterStandoffs(driverInput.getShooterStandoffs());
            
            if (driverInput.catching()) {
                catchBall();
            } else if (driverInput.shoot()) {
                shoot();
            } else if (driverInput.restoreToDefault()) {
                restoreDefaultPositions();
            } else if (intaking) {
            } else {
                resetIntakes();
                resetShooter();
            }
            
            setToRobot();
            
        } else {
            calcOverrides();
        }
    }

    public void setToRobot() {
        intake.setToRobot();
        catapult.setToRobot();
    }

    public void frontIntake() {
        intake.setFrontIntakeSpeed(Intake.intakeSpeed);
        intake.setFrontAngle(Intake.downAngle);
    }

    public void rearIntake() {
        intake.setRearIntakeSpeed(Intake.intakeSpeed);
        intake.setRearAngle(Intake.downAngle);
    }

    public void frontOuttake() {
        intake.setFrontIntakeSpeed(Intake.outtakeSpeed);
        intake.setFrontAngle(Intake.upAngle);
        intake.setRearIntakeSpeed(Intake.intakeSpeed);
        intake.setRearAngle(Intake.inAngle);
    }

    public void rearOuttake() {
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
        catapult.setPosition(Catapult.shootPosition);
    }

    public void resetShooter() {
        intake.setFrontAngle(Intake.upAngle);
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
