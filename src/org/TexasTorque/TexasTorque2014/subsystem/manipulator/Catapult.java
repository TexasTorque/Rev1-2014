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

    public static double pidSetpoint;
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
                if(Timer.getFPGATimestamp() > fireTime + timeout)
                {
                    fired = false;
                    SensorInput.getInstance().resetCatapultEncoder();
                }
                
            } else {

                if (sensorInput.getCatapultLimitSwitch()) {
                    isReady = true;
                    catapultMotorSpeed = 0.0;
                    if ((driverInput.ChooChooOverride() || driverInput.getAutonBool("shoot", false)) && intake.isDone()) {
                        fired = true;
                        fireTime = Timer.getFPGATimestamp();
                        //fire Solinoid
                    }
                } else if (currentValue < pidSetpoint) {
                    isReady = false;
                    catapultMotorSpeed = pullBackPID.calculate(currentValue);
                } else if (currentValue >= pidSetpoint) {
                    isReady = false;
                    catapultMotorSpeed = slowSpeed;
                }
            }
        } else {

            catapultMotorSpeed = 0.0;
            if (driverInput.ChooChooOverride() || driverInput.ChooChooReset() || driverInput.getAutonBool("shoot", false)) {
                firstCycle = false;
            }
        }
        SmartDashboard.putNumber("CatapultSetpoint", pidSetpoint);
        SmartDashboard.putNumber("CatapultActual", currentValue);
        SmartDashboard.putNumber("CatapultMotorSpeed", catapultMotorSpeed);
    }

    //public void setPosition(double desired) {
    //    if (desired != desiredPullbackPosition) {
    //        desiredPullbackPosition = desired;
    //        goal = desired;
    //    }
    //}
    public boolean getIntakeDownOverride() {
        return intakeDownOverride;
    }

    public void setMotorSpeed(double speed) {
        catapultMotorSpeed = speed;
    }

    public boolean catapultReady() {
        return isReady;
    }

    public boolean catapultReadyForIntake() {
        return (sensorInput.getCatapultEncoder() > 300);
    }

    public void setToRobot() {
        robotOutput.setCatapultMotor(catapultMotorSpeed);
    }

    public void loadParameters() {
        firstCycle = true;
        timeout = params.getAsDouble("C_Timeout", 1.0);
        pidSetpoint = params.getAsDouble("C_ResetSetpoint", 0.0);
        slowSpeed = params.getAsDouble("C_SlowSpeed", 0.0);
        overrideSpeed = params.getAsDouble("C_OverrideSpeed", 0.0);

        double p = params.getAsDouble("C_PullBackP", 0.0);
        double i = params.getAsDouble("C_PullBackI", 0.0);
        double d = params.getAsDouble("C_PullBackD", 0.0);
        double epsilon = params.getAsDouble("C_PullBackEpsilon", 0.0);

        pullBackPID.setPIDGains(p, i, d);
        pullBackPID.setEpsilon(epsilon);

    }

    public String logData() { //no logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }

}
