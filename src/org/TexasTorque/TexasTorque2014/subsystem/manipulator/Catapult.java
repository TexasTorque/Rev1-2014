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
    private boolean firstCycle;
    private boolean firstHitSetpoint;

    public static double standardPosition;
    public static double shootPosition;
    public static double overrideSpeed;
    public static double stallTime;
    public static double pidSetpoint;
    public static double tollerance;

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
        SmartDashboard.putBoolean("ChooChooOverride", driverInput.ChooChooOverride());
        SmartDashboard.putBoolean("ChooChooFirstCycle", firstCycle);
        
        if (!firstCycle) {
            if (sensorInput.getCatapultLimitSwitch() == false) {
                firstContact = true;
                if(driverInput.ChooChooOverride() && sensorInput.getCatapultEncoder() < 100)
                {
                    pullBackPID.setSetpoint(pidSetpoint);
                    firstHitSetpoint = false;
                }
                else if (driverInput.ChooChooOverride() || driverInput.getAutonBool("shoot", false)) {
                    SmartDashboard.putBoolean("CHOOCHOO", true);
                    pullBackPID.setSetpoint(pidSetpoint + pidSetpoint);//Fire
                    firstHitSetpoint = false;
                } else {
                    SmartDashboard.putBoolean("CHOOCHOO", false);
                    //pullBackPID.setSetpoint(sensorInput.getCatapultEncoder());//Stop
                }
            } else {
                if (firstContact) {
                    contactTime = Timer.getFPGATimestamp();
                    SensorInput.getInstance().resetCatapultEncoder();
                    firstContact = false;
                } else {
                    if (Timer.getFPGATimestamp() - contactTime > stallTime && driverInput.ChooChooOverride()) {
                        pullBackPID.setSetpoint(pidSetpoint);//ready to move
                        firstHitSetpoint = false;
                    } else {
                        pullBackPID.setSetpoint(sensorInput.getCatapultEncoder());//Stop
                    }
                }
            }

            double currentValue = sensorInput.getCatapultEncoder();
        //if (currentValue < 0.8)
            //{
            //    desiredPullbackPosition = standardPosition;
            //}
            catapultMotorSpeed = pullBackPID.calculate(currentValue);
            
            if(currentValue >= pullBackPID.getSetpoint())
            {
                firstHitSetpoint = true;
            }
            
            if(catapultMotorSpeed <= 0 || (Math.abs(currentValue - pullBackPID.getSetpoint()) < tollerance) && firstHitSetpoint)
            {
                catapultMotorSpeed = 0.0;
            }
            
            SmartDashboard.putNumber("CatapultSetpoint", pullBackPID.getSetpoint());
            SmartDashboard.putNumber("CatapultActual", currentValue);
            SmartDashboard.putNumber("CatapultMotorSpeed", catapultMotorSpeed);
        } else {
            if(driverInput.ChooChooOverride())
            {
                firstCycle = false;
            }
            //SmartDashboard.putBoolean("FirstCycleCatapult", firstCycle);
        }
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
        firstCycle = true;
        SmartDashboard.putBoolean("FirstCycleCatapult", firstCycle);
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

        overrideSpeed = params.getAsDouble("C_ChooChooSpeed", 0.0);
        stallTime = params.getAsDouble("C_StallTime", 1.0);
        pidSetpoint = params.getAsDouble("C_ResetSetpoint", 0.0);
        tollerance = params.getAsDouble("C_Tollerance", 10.0);
        
        System.err.println("ParamsLoaded:"+p+" "+i+" "+d+" "+max+" "+pidSetpoint);
    }

    public String logData() { //no logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }

}
