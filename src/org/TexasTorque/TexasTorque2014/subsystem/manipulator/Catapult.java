package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class Catapult extends TorqueSubsystem {

    private static Catapult instance;

    private Intake intake;
    private TorquePID pullBackPID;
    private double catapultMotorSpeed;
    private boolean firstCycle;
    private boolean intakeDownOverride;
    private boolean fired;
    private boolean isReady;
    private double slowSpeed;
    private double fireTime;
    private boolean winchSolinoid;

    public static double PIDSetpoint;
    public static double tolerance;
    public static double timeout;
    public static double overrideSpeed;

    public static Catapult getInstance() {
        return (instance == null) ? instance = new Catapult() : instance;
    }

    public Catapult() {
        super();
        intake = Intake.getInstance();
        catapultMotorSpeed = Constants.MOTOR_STOPPED;
        pullBackPID = new TorquePID();
    }

    public void run() {
        double currentValue = sensorInput.getCatapultEncoder();

        if (!firstCycle) {
            if (fired) {
                isReady = false;
                if (Timer.getFPGATimestamp() > fireTime + timeout) {
                    fired = false;
                    SensorInput.getInstance().resetCatapultEncoder();
                    winchSolinoid = false;

                }

            } else {
                if ((driverInput.shoot() || driverInput.getAutonBool("shoot", false)) && intake.shootIsDone()) {
                    catapultMotorSpeed = Constants.MOTOR_STOPPED;
                    fired = true;
                    fireTime = Timer.getFPGATimestamp();
                    winchSolinoid = true;
                    pullBackPID.setSetpoint(PIDSetpoint);
                } else if (sensorInput.getCatapultLimitSwitch()) {
                    isReady = true;
                    catapultMotorSpeed = Constants.MOTOR_STOPPED;

                } else if (currentValue < pullBackPID.getSetpoint()) {
                    isReady = false;
                    catapultMotorSpeed = pullBackPID.calculate(currentValue);
                } else if (currentValue >= pullBackPID.getSetpoint()) {
                    isReady = false;
                    catapultMotorSpeed = slowSpeed;
                }
            }

        } else {
            //First Cycle Clears
            catapultMotorSpeed = 0.0;
            winchSolinoid = false;
            if (driverInput.shoot() || driverInput.catapultReset() || driverInput.getAutonBool("shoot", false)) {
                firstCycle = false;
            }
        }
        if (driverInput.WinchStop()) {
            catapultMotorSpeed = 0.0;
        }
    }

    public boolean getIntakeDownOverride() {
        return intakeDownOverride;
    }

    public void setMotorSpeed(double speed) {
        catapultMotorSpeed = speed;
    }
    
    public void releaseOverride(boolean state) {
        winchSolinoid = state;
    }
    
    public boolean catapultReady() {
        return isReady;
    }

    public boolean isFired() {
        return fired;
    }

    public boolean catapultReadyForIntake() {
        return (sensorInput.getCatapultEncoder() > pullBackPID.getSetpoint() * 2 / 3);
    }

    public boolean catapultReadyForRearIntake() {
        return (sensorInput.getCatapultEncoder() > pullBackPID.getSetpoint() * 5 / 6);
    }

    public void setToRobot() {
        robotOutput.setCatapultMotor(catapultMotorSpeed);
        robotOutput.setWinchSolinoid(winchSolinoid);
    }

    public void loadParameters() {
        firstCycle = true;
        timeout = params.getAsDouble("C_Timeout", 1.0);
        PIDSetpoint = params.getAsDouble("C_FullResetSetpoint", 0.0);
        slowSpeed = params.getAsDouble("C_SlowSpeed", 0.0);
        overrideSpeed = params.getAsDouble("C_OverrideSpeed", 0.75);

        double p = params.getAsDouble("C_PullBackP", 0.0);
        double i = params.getAsDouble("C_PullBackI", 0.0);
        double d = params.getAsDouble("C_PullBackD", 0.0);
        double epsilon = params.getAsDouble("C_PullBackEpsilon", 0.0);

        pullBackPID.setPIDGains(p, i, d);
        pullBackPID.setEpsilon(epsilon);
        pullBackPID.setSetpoint(PIDSetpoint);
    }
    
    public void pushToDashboard()
    {
        SmartDashboard.putNumber("CatapultSetpoint", pullBackPID.getSetpoint());
        SmartDashboard.putNumber("CatapultMotorSpeed", catapultMotorSpeed);
        SmartDashboard.putBoolean("WinchSolinoid", winchSolinoid);
    }

    public String logData() { //no logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }

}
