package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class Catapult extends TorqueSubsystem {

    private static Catapult instance;

    private double catapultMotorSpeed;
    private double desiredPullbackPosition;
    private boolean standoffPosition;
    private double contactTime;
    private boolean firstContact;

    public static double standardPosition;
    public static double shootPosition;
    public static double overrideSpeed;
    public static double stallTime;

    private TorquePID pullBackPID;

    public static Catapult getInstance() {
        return (instance == null) ? instance = new Catapult() : instance;
    }

    public Catapult() {
        super();

        catapultMotorSpeed = Constants.MOTOR_STOPPED;
        pullBackPID = new TorquePID();
        desiredPullbackPosition = Catapult.standardPosition;
    }

    public void run() {
        if (sensorInput.getCatapultLimitSwitch() == false) {
            firstContact = true;
            if (driverInput.ChooChooOverride()) {
                SmartDashboard.putBoolean("CHOOCHOO", true);
                catapultMotorSpeed = overrideSpeed;
            } else {
                SmartDashboard.putBoolean("CHOOCHOO", false);
                catapultMotorSpeed = Constants.MOTOR_STOPPED;
            }
        } else {
            if (firstContact) {
                contactTime = Timer.getFPGATimestamp();
                SensorInput.getInstance().resetCatapultEncoder();
                firstContact = false;
            } else {
                if (Timer.getFPGATimestamp() - contactTime > stallTime) {
                    catapultMotorSpeed = Constants.MOTOR_STOPPED;
                } else  {
                    catapultMotorSpeed = Constants.MOTOR_STOPPED;
                }
            }
        }
        //double currentValue = sensorInput.getCatapultEncoder();
        //if (currentValue < 0.8)
        //{
        //    desiredPullbackPosition = standardPosition;
        //}
        //catapultMotorSpeed = pullBackPID.calculate(currentValue);

    }

    public void setPosition(double desired) {
        if (desired != desiredPullbackPosition) {
            desiredPullbackPosition = desired;
            pullBackPID.setSetpoint(desired);
        }
    }
    
    public void setMotorSpeed(double speed) {
        catapultMotorSpeed = speed;
    }

    public void setStandoffs(boolean highShot) {
        if (standoffPosition != highShot) {
            standoffPosition = highShot;
        }
    }

    public void setToRobot() {
        robotOutput.setCatapultMotor(catapultMotorSpeed);
        robotOutput.setCatapultMode(standoffPosition);
    }

    public void loadParameters() {
        double p = params.getAsDouble("C_PullbackP", 0.0);
        double i = params.getAsDouble("C_PullbackI", 0.0);
        double d = params.getAsDouble("C_PullbackD", 0.0);
        double e = params.getAsDouble("C_PullbackEpsilon", 0.0);
        double r = params.getAsDouble("C_PullbackDoneRange", 0.0);
        double max = params.getAsDouble("C_PullbackMaxOutput", 1.0);

        pullBackPID.setPIDGains(p, i, d);
        pullBackPID.setEpsilon(e);
        pullBackPID.setDoneRange(r);
        pullBackPID.setMaxOutput(max);
        pullBackPID.reset();

        standardPosition = params.getAsDouble("C_StandardPullback", 0.9);
        shootPosition = params.getAsDouble("C_ShootPullback", 1.9);
        overrideSpeed = params.getAsDouble("C_ChooChooSpeed", 0.0);
        stallTime = params.getAsDouble("C_StallTime", 1.0);
    }

    public String logData() { //no logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }

}
