package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;
import org.TexasTorque.TorqueLib.util.TorqueToggle;

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
    private boolean catapultStopAngle;
    private TorqueToggle stopAngleToggle;

    public static double fullPidSetpoint;
    public static double shortPidSetpoint;
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
        stopAngleToggle = new TorqueToggle();
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
                if ((driverInput.ChooChooOverride() || driverInput.getAutonBool("shoot", false)) && intake.shootIsDone()) {
                    catapultMotorSpeed = Constants.MOTOR_STOPPED;
                    fired = true;
                    fireTime = Timer.getFPGATimestamp();
                    winchSolinoid = true;
                    pullBackPID.setSetpoint((catapultStopAngle) ? shortPidSetpoint : fullPidSetpoint);
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
            
            if (driverInput.isAuton()) {
                catapultStopAngle = driverInput.getAutonBool("CatapultAngle", false);
            } else {
                catapultStopAngle = driverInput.getCatapultStopAngle();
            }

        } else {
            //First Cycle Clears
            catapultMotorSpeed = 0.0;
            winchSolinoid = false;
            if (driverInput.ChooChooOverride() || driverInput.ChooChooReset() || driverInput.getAutonBool("shoot", false)) {
                firstCycle = false;
            }
        }
        if (driverInput.WinchStop()) {
            catapultMotorSpeed = 0.0;
        }
        SmartDashboard.putNumber("CatapultSetpoint", pullBackPID.getSetpoint());
        SmartDashboard.putNumber("CatapultActual", currentValue);
        SmartDashboard.putNumber("CatapultMotorSpeed", catapultMotorSpeed);
        SmartDashboard.putBoolean("WinchSolinoid", winchSolinoid);
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
    
    public void shortShotOverride(boolean state) {
        catapultStopAngle = state;
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
        robotOutput.setCatapultAngle(catapultStopAngle);
    }

    public void loadParameters() {
        firstCycle = true;
        timeout = params.getAsDouble("C_Timeout", 1.0);
        fullPidSetpoint = params.getAsDouble("C_FullResetSetpoint", 0.0);
        shortPidSetpoint = params.getAsDouble("C_ShortResetSetpoint", 0.0);
        slowSpeed = params.getAsDouble("C_SlowSpeed", 0.0);
        overrideSpeed = params.getAsDouble("C_OverrideSpeed", 0.75);

        double p = params.getAsDouble("C_PullBackP", 0.0);
        double i = params.getAsDouble("C_PullBackI", 0.0);
        double d = params.getAsDouble("C_PullBackD", 0.0);
        double epsilon = params.getAsDouble("C_PullBackEpsilon", 0.0);

        pullBackPID.setPIDGains(p, i, d);
        pullBackPID.setEpsilon(epsilon);
        pullBackPID.setSetpoint(shortPidSetpoint);
    }

    public String logData() { //no logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }

}
