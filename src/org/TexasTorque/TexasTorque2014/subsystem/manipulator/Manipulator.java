package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;

public class Manipulator extends TorqueSubsystem {

    private static Manipulator instance;

    private Intake intake;
    private Catapult catapult;
    private double intakeOverrideConst;

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
            } else if (driverInput.getAutonBool("frontIntakeDown", false)) {
                intaking = true;
                frontIntakeDown();
            } else if (driverInput.getAutonBool("rearIntakeDown", false)) {
                intaking = true;
                rearIntakeDown();
            }

            if (driverInput.catching() || driverInput.getAutonBool("catch", false)) {
                catchBall();
            } else if (driverInput.readyToShoot() || driverInput.getAutonBool("shoot", false)) {
                shoot();
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
            
            if (driverInput.getHoopToggle() || driverInput.getAutonBool("hoopIn", false))
            {
                intake.setHoop(Constants.HOOP_IN);
            }
            
            intake.run();
            catapult.run();

            setToRobot();

        } else {
            calcOverrides();
            setToRobot();
        }

        double currentAlliance = dashboardManager.getDS().getAlliance().value;

        if (catapultReady()) {
            if (currentAlliance == Constants.RED_ALLIANCE) {
                robotOutput.setLightsState(Constants.LIGHTS_RED);
            } else if (currentAlliance == Constants.BLUE_ALLIANCE) {
                robotOutput.setLightsState(Constants.LIGHTS_BLUE);
            }
        } else {
            if (currentAlliance == Constants.RED_ALLIANCE) {
                robotOutput.setLightsState(Constants.LIGHTS_RED_GREEN);
            } else if (currentAlliance == Constants.BLUE_ALLIANCE) {
                robotOutput.setLightsState(Constants.LIGHTS_BLUE_GREEN);
            }
        }

    }

    public void setToRobot() {
        intake.setToRobot();
        catapult.setToRobot();
    }

    public void frontIntake() {
        intake.setFrontIntakeSpeed(Intake.intakeSpeed);
        intake.setFrontAngle(Intake.frontDownAngle);
        intake.setRearAngle(Intake.inAngle);
        intake.setHoop(Constants.HOOP_UP);
    }

    public void frontIntakeDown() {
        intake.setFrontAngle(Intake.frontDownAngle);
        intake.setFrontIntakeSpeed(Intake.slowSpeed);
        intake.setRearAngle(Intake.rearUpAngle);
        intake.setRearIntakeSpeed(Constants.MOTOR_STOPPED);
    }

    public void rearIntakeDown() {
        intake.setFrontAngle(Intake.frontUpAngle);
        intake.setFrontIntakeSpeed(Constants.MOTOR_STOPPED);
        intake.setRearAngle(Intake.rearDownAngle);
        intake.setRearIntakeSpeed(Constants.MOTOR_STOPPED);
    }

    public void rearIntake() {
        intake.setRearIntakeSpeed(Intake.intakeSpeed);
        intake.setRearAngle(Intake.rearDownAngle);
        intake.setFrontAngle(Intake.inAngle);
        intake.setHoop(Constants.HOOP_UP);
    }

    public void frontOuttake() {
        intake.setFrontIntakeSpeed(Intake.outtakeSpeed);
        intake.setFrontAngle(Intake.frontOuttakeAngle);
        intake.setRearIntakeSpeed(Intake.intakeSpeed);
        intake.setRearAngle(Intake.inAngle);
        intake.setHoop(Constants.HOOP_UP);
    }

    public void rearOuttake() {
        intake.setRearIntakeSpeed(Intake.outtakeSpeed);
        intake.setRearAngle(Intake.rearOuttakeAngle);
        intake.setFrontIntakeSpeed(Intake.intakeSpeed);
        intake.setFrontAngle(Intake.frontIntakeAngle);
        intake.setHoop(Constants.HOOP_UP);
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
        intake.setHoop(Constants.HOOP_UP);
    }

    public void resetIntakes() {
        resetFrontIntake();
        resetRearIntake();
    }

    public void shoot() {
        intake.setFrontAngle(Intake.frontShootAngle);
        intake.setRearAngle(Intake.rearShootAngle);

        if (driverInput.shoot() || driverInput.getAutonBool("shoot", false)) {
            intake.setHoop(Constants.HOOP_UP);
        }
    }

    public void shootHigh() {
        intake.setFrontAngle(Intake.frontShootAngle);
        intake.setRearAngle(Intake.rearShootAngle);
    }

    public void resetShooter() {
    }

    public String getKeyNames() {
        String names = "InOverrideState,";

        return names;
    }

    public String logData() {
        String data = driverInput.overrideState() + ",";

        return data;
    }

    public boolean intakesDone() {
        return intake.isDone();
    }

    public boolean isFired() {
        return catapult.isFired();
    }

    public boolean catapultReady() {
        return catapult.catapultReady();
    }

    public boolean catapultReadyForIntake() {
        return catapult.catapultReadyForIntake();
    }

    public void loadParameters() {
        intake.loadParameters();
        catapult.loadParameters();
        intakeOverrideConst = params.getAsDouble("I_OverrideConst", 0.8);
    }

    private void calcOverrides() {
        if (!sensorInput.getCatapultLimitSwitch() && driverInput.winchOverride()) {
            catapult.setMotorSpeed(Catapult.overrideSpeed);
        } else {
            catapult.setMotorSpeed(Constants.MOTOR_STOPPED);
        }

        if (Math.abs(driverInput.frontIntakeOverride()) > Constants.OVERRIDE_AXIS_DEADBAND) {
            intake.frontIntakeOverride(driverInput.frontIntakeOverride() * intakeOverrideConst);
        } else {
            intake.frontIntakeOverride(Constants.MOTOR_STOPPED);
        }
        if (Math.abs(driverInput.rearIntakeOverride()) > Constants.OVERRIDE_AXIS_DEADBAND) {
            intake.rearIntakeOverride(driverInput.rearIntakeOverride() * intakeOverrideConst);
        } else {
            intake.rearIntakeOverride(Constants.MOTOR_STOPPED);
        }

        if (driverInput.frontIntakeRollOverride()) {
            intake.frontIntakeOverrideRoll(1.0);
        } else if (driverInput.frontOuttakeRollOverride()) {
            intake.frontIntakeOverrideRoll(-1.0);
        } else {
            intake.frontIntakeOverrideRoll(Constants.MOTOR_STOPPED);
        }

        if (driverInput.rearIntakeRollOverride()) {
            intake.rearIntakeOverrideRoll(1.0);
        } else if (driverInput.rearOuttakeRollOverride()) {
            intake.rearIntakeOverrideRoll(-1.0);
        } else {
            intake.rearIntakeOverrideRoll(Constants.MOTOR_STOPPED);
        }
        
        intake.toggleHoop(driverInput.getHoopToggle());

        catapult.releaseOverride(driverInput.releaseOverride());
        catapult.shortShotOverride(driverInput.shortShotOverride());
    }
    
    public void pushToDashboard()
    {
        intake.pushToDashboard();
        catapult.pushToDashboard();
    }

    public void restoreDefaultPositions() {
    }
}
