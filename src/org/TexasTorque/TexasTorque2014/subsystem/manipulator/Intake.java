package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;
import org.TexasTorque.TorqueLib.util.TorqueToggle;

public class Intake extends TorqueSubsystem {

    private static Intake instance;

    private double frontIntakeSpeed;
    private double rearIntakeSpeed;
    private double frontTiltSpeed;
    private double rearTiltSpeed;

    private double desiredFrontTiltAngle;
    private double desiredRearTiltAngle;

    private boolean intaking;
    private boolean outtaking;
    private boolean intakeActive;
    private boolean hasBall;
    private boolean frontButtonWasPushed;
    private boolean rearButtonWasPushed;

    private boolean hoopPosition;
    private boolean firstUp;
    private double hoopUpTime;
    private TorqueToggle hoopToggle;

    private TorquePID frontTiltPID;
    private TorquePID rearTiltPID;

    public static double frontUpAngle;
    public static double rearUpAngle;
    public static double frontDownAngle;
    public static double rearDownAngle;
    public static double frontShootAngle;
    public static double rearShootAngle;
    public static double inAngle;
    public static double intakeSpeed;
    public static double outtakeSpeed;
    public static double tiltDownTollerance;
    public static double frontIntakeAngle;
    public static double frontCatchAngle;
    public static double rearCatchAngle;
    public static double frontOuttakeAngle;
    public static double slowSpeed;
    public static double rearOuttakeAngle;
    public static double hoopWaitTime;

    public static Intake getInstance() {
        return (instance == null) ? instance = new Intake() : instance;
    }

    public Intake() {
        super();

        frontTiltPID = new TorquePID();
        rearTiltPID = new TorquePID();

        frontIntakeSpeed = Constants.MOTOR_STOPPED;
        rearIntakeSpeed = Constants.MOTOR_STOPPED;
        frontTiltSpeed = Constants.MOTOR_STOPPED;
        rearTiltSpeed = Constants.MOTOR_STOPPED;

        desiredFrontTiltAngle = 90.0;
        desiredRearTiltAngle = 90.0;

        hoopToggle = new TorqueToggle();
        hoopToggle.set(false);

        frontButtonWasPushed = false;
        rearButtonWasPushed = false;
        intaking = false;
        outtaking = false;
        intakeActive = false;
        hasBall = false;
    }

    public void run() {
        if (!driverInput.overrideState()) {
            
            if (intaking && ( (frontButtonWasPushed && sensorInput.getFrontIntakeButton() == false) ||
                              (rearButtonWasPushed  && sensorInput.getRearIntakeButton() == false) )) {
                hasBall = true;
            }

            if (outtaking && ( (frontButtonWasPushed && sensorInput.getFrontIntakeButton() == false) ||
                               (rearButtonWasPushed  && sensorInput.getRearIntakeButton() == false) )) {
                hasBall = false;
            }
            
            frontButtonWasPushed = sensorInput.getFrontIntakeButton();
            rearButtonWasPushed = sensorInput.getRearIntakeButton();
            
            intaking = false;
            outtaking = false;

            if ((driverInput.frontIntaking() || driverInput.getAutonBool("frontIn", false)) && !hasBall) {
                frontIntake();
                intaking = true;
            } else if ((driverInput.frontOuttaking() || driverInput.getAutonBool("frontOut", false)) && hasBall) {
                frontOuttake();
                outtaking = true;
            } else if ((driverInput.rearIntaking() || driverInput.getAutonBool("rearIn", false)) && !hasBall) {
                rearIntake();
                intaking = true;
            } else if ((driverInput.rearOuttaking() || driverInput.getAutonBool("rearOut", false)) && hasBall) {
                rearOuttake();
                outtaking = true;
            }
            
            if (driverInput.getHoopIn() || driverInput.getAutonBool("hoopIn", false)) {
                hasBall = true;
                setHoop(Constants.HOOP_IN);
            }

            if (driverInput.getHoopUp()) {
                hasBall = false;
                setHoop(Constants.HOOP_UP);
            }
            
            if (hasBall && isDone() && getIntakesUp())
            {
                setHoop(Constants.HOOP_IN);
            }
            
            if (!hasBall)
            {
                setHoop(Constants.HOOP_UP);
            }
            
            if ( !(intaking || outtaking || intakeActive) )
            {
                resetFrontIntake();
                resetRearIntake();
            }
        }

        double currentFrontAngle = sensorInput.getFrontIntakeTiltAngle();
        double currentRearAngle = sensorInput.getRearIntakeTiltAngle();

        frontTiltSpeed = frontTiltPID.calculate(currentFrontAngle);
        if (frontTiltPID.getSetpoint() == frontDownAngle && Math.abs(frontDownAngle - currentFrontAngle) < tiltDownTollerance) {
            frontTiltSpeed = Constants.MOTOR_STOPPED;
        }
        rearTiltSpeed = rearTiltPID.calculate(currentRearAngle);
        if (rearTiltPID.getSetpoint() == rearDownAngle && Math.abs(rearDownAngle - currentRearAngle) < tiltDownTollerance) {
            rearTiltSpeed = Constants.MOTOR_STOPPED;
        }

        pushToDashboard();
    }

    public boolean isDone() {
        return frontTiltPID.isDone() && rearTiltPID.isDone();
    }

    public boolean shootIsDone() {
        return (frontTiltPID.isDone() && frontTiltPID.getSetpoint() == Intake.frontShootAngle && rearTiltPID.isDone() && rearTiltPID.getSetpoint() == Intake.rearShootAngle && hoopPosition == Constants.HOOP_UP && (Timer.getFPGATimestamp() > hoopUpTime + hoopWaitTime));
    }

    public boolean frontIsDone() {
        return frontTiltPID.isDone();
    }

    public boolean rearIsDone() {
        return rearTiltPID.isDone();
    }

    public void setFrontAngle(double angle) {
        if (angle != frontTiltPID.getSetpoint()) {
            desiredFrontTiltAngle = angle;
            frontTiltPID.setSetpoint(desiredFrontTiltAngle);
        }
    }

    public void setRearAngle(double angle) {
        if (angle != rearTiltPID.getSetpoint()) {
            desiredRearTiltAngle = angle;
            rearTiltPID.setSetpoint(desiredRearTiltAngle);
        }
    }

    public void frontIntakeOverride(double tiltSpeed) {
        frontTiltSpeed = tiltSpeed;
    }

    public void rearIntakeOverride(double tiltSpeed) {
        rearTiltSpeed = tiltSpeed;
    }

    public void frontIntakeOverrideRoll(double speed) {
        frontIntakeSpeed = speed;
    }

    public void rearIntakeOverrideRoll(double speed) {
        rearIntakeSpeed = speed;
    }

    public void setFrontIntakeSpeed(double speed) {
        frontIntakeSpeed = speed;
    }

    public void setRearIntakeSpeed(double speed) {
        rearIntakeSpeed = speed;
    }
    
    public void frontIntake() {
        setFrontIntakeSpeed(Intake.intakeSpeed);
        setFrontAngle(Intake.frontDownAngle);
        setRearAngle(Intake.inAngle);
        setHoop(Constants.HOOP_UP);
    }
    
    public void rearIntake() {
        setRearIntakeSpeed(Intake.intakeSpeed);
        setRearAngle(Intake.rearDownAngle);
        setFrontAngle(Intake.inAngle);
        setHoop(Constants.HOOP_UP);
    }

    public void frontOuttake() {
        setFrontIntakeSpeed(Intake.outtakeSpeed);
        setFrontAngle(Intake.frontOuttakeAngle);
        setRearIntakeSpeed(Intake.intakeSpeed);
        setRearAngle(Intake.inAngle);
        setHoop(Constants.HOOP_UP);
    }

    public void rearOuttake() {
        setRearIntakeSpeed(Intake.outtakeSpeed);
        setRearAngle(Intake.rearOuttakeAngle);
        setFrontIntakeSpeed(Intake.intakeSpeed);
        setFrontAngle(Intake.frontIntakeAngle);
        setHoop(Constants.HOOP_UP);
    }
    
    public void resetFrontIntake() {
        setFrontAngle(Intake.frontUpAngle);
        setFrontIntakeSpeed(Constants.MOTOR_STOPPED);
    }

    public void resetRearIntake() {
        setRearAngle(Intake.rearUpAngle);
        setRearIntakeSpeed(Constants.MOTOR_STOPPED);
    }

    public void setHoop(boolean position) {
        hoopPosition = position;
        hoopToggle.set(position);
        if (position == Constants.HOOP_IN) {
            firstUp = true;
        }
        if (position == Constants.HOOP_UP && firstUp) {
            hoopUpTime = Timer.getFPGATimestamp();
            firstUp = false;
        }
    }

    public void toggleHoop(boolean toggle) {
        hoopToggle.calc(toggle);
        hoopPosition = hoopToggle.get();
    }

    public void setHasBall(boolean has) {
        hasBall = has;
    }
    
    public boolean hasBall()
    {
        return hasBall;
    }
    
    public boolean getIntakesUp()
    {
        return frontTiltPID.getSetpoint() == frontUpAngle && rearTiltPID.getSetpoint() == rearUpAngle;
    }
    
    public void setIntakesActive(boolean active)
    {
        intakeActive = active;
    }

    public void setToRobot() {
        robotOutput.setIntakeMotors(frontIntakeSpeed, rearIntakeSpeed, frontTiltSpeed, rearTiltSpeed);
        robotOutput.setHoop(hoopPosition);
    }

    public void loadParameters() {
        intakeSpeed = params.getAsDouble("I_IntakeSpeed", 1.0);
        outtakeSpeed = params.getAsDouble("I_OuttakeSpeed", -1.0);
        frontDownAngle = params.getAsDouble("I_FrontDownAngle", 41);
        rearDownAngle = params.getAsDouble("I_RearDownAngle", 41);
        frontUpAngle = params.getAsDouble("I_FrontUpAngle", 90);
        rearUpAngle = params.getAsDouble("I_RearUpAngle", 90);
        inAngle = params.getAsDouble("I_InAngle", 110);
        frontShootAngle = params.getAsDouble("I_FrontShootAngle", frontDownAngle);
        rearShootAngle = params.getAsDouble("I_RearShootAngle", rearDownAngle);
        frontIntakeAngle = params.getAsDouble("I_FrontIntakeAngle", 110);
        frontOuttakeAngle = params.getAsDouble("I_FrontOuttakeAngle", 90);
        frontCatchAngle = params.getAsDouble("I_FrontCatchAngle", frontDownAngle);
        rearCatchAngle = params.getAsDouble("I_RearCatchAngle", rearDownAngle);
        slowSpeed = params.getAsDouble("I_SlowSpeed", 0.3);
        rearOuttakeAngle = params.getAsDouble("I_RearOuttakeAngle", 70);
        hoopWaitTime = params.getAsDouble("I_HoopWaitTime", 0.2);

        double frontP = params.getAsDouble("I_FrontIntakeP", 0.0);
        double frontI = params.getAsDouble("I_FrontIntakeI", 0.0);
        double frontD = params.getAsDouble("I_FrontIntakeD", 0.0);
        double frontE = params.getAsDouble("I_FrontIntakeEpsilon", 0.0);
        double frontR = params.getAsDouble("I_FrontIntakeDoneRange", 0.0);
        double frontMaxOut = params.getAsDouble("I_FrontIntakeMaxOutput", 1.0);

        frontTiltPID.setPIDGains(frontP, frontI, frontD);
        frontTiltPID.setEpsilon(frontE);
        frontTiltPID.setDoneRange(frontR);
        frontTiltPID.setMaxOutput(frontMaxOut);
        frontTiltPID.setMinDoneCycles(0);
        frontTiltPID.reset();

        double rearP = params.getAsDouble("I_RearIntakeP", 0.0);
        double rearI = params.getAsDouble("I_RearIntakeI", 0.0);
        double rearD = params.getAsDouble("I_RearIntakeD", 0.0);
        double rearE = params.getAsDouble("I_RearIntakeEpsilon", 0.0);
        double rearR = params.getAsDouble("I_RearIntakeDoneRange", 0.0);
        double rearMaxOut = params.getAsDouble("I_RearIntakeMaxOutput", 1.0);

        rearTiltPID.setPIDGains(rearP, rearI, rearD);
        rearTiltPID.setEpsilon(rearE);
        rearTiltPID.setDoneRange(rearR);
        rearTiltPID.setMaxOutput(rearMaxOut);
        rearTiltPID.setMinDoneCycles(0);
        rearTiltPID.reset();

        intakeSpeed = params.getAsDouble("I_IntakeSpeed", 1.0);
        outtakeSpeed = params.getAsDouble("I_OuttakeSpeed", -1.0);
        tiltDownTollerance = params.getAsDouble("I_TiltDownTollerance", 2.0);
    }

    public void pushToDashboard() {
        SmartDashboard.putNumber("FrontIntakeAngle", sensorInput.getFrontIntakeTiltAngle());
        SmartDashboard.putNumber("RearIntakeAngle", sensorInput.getRearIntakeTiltAngle());
        SmartDashboard.putNumber("FrontIntakeRoller", frontIntakeSpeed);
        SmartDashboard.putNumber("RearIntakeRoller", rearIntakeSpeed);
        SmartDashboard.putBoolean("Hoop", hoopPosition);
    }

    public String logData() { //Have not implemented logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }
}
