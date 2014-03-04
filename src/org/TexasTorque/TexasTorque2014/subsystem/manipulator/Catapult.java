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
    private boolean intakeDownOverride;
    private boolean fired;
    private boolean isReady;
    private double goal;

    public static double standardPosition;
    public static double shootPosition;
    public static double overrideSpeed;
    public static double stallTime;
    public static double pidSetpoint;
    public static double tolerance;


    public static Catapult getInstance() {
        return (instance == null) ? instance = new Catapult() : instance;
    }

    public Catapult() {
        super();

        catapultMotorSpeed = Constants.MOTOR_STOPPED;
        desiredPullbackPosition = Catapult.standardPosition;
    }

    public void run() {
        SmartDashboard.putBoolean("ChooChooOverride", driverInput.ChooChooOverride());
        SmartDashboard.putBoolean("ChooChooFirstCycle", firstCycle);
        
        if (!firstCycle) {
            isReady = false;
            if (sensorInput.getCatapultLimitSwitch() == false) {
                firstContact = true;
                if(driverInput.ChooChooReset() && sensorInput.getCatapultEncoder() < 250 || driverInput.getAutonBool("reset", false))
                {
                    goal = pidSetpoint;
                    firstHitSetpoint = false;
                    intakeDownOverride = false;
                }
                else if (driverInput.ChooChooOverride() && sensorInput.getCatapultEncoder() >= 250 || driverInput.getAutonBool("shootFire", false)) {
                    goal = pidSetpoint + pidSetpoint;
                    intakeDownOverride = true;
                    firstHitSetpoint = false;
                } else {
                }
            } else {
                if (firstContact) {
                    contactTime = Timer.getFPGATimestamp();
                    SensorInput.getInstance().resetCatapultEncoder();
                    firstContact = false;
                    fired = true;
                    goal = 0.0;
                } else {
                    if (Timer.getFPGATimestamp() - contactTime > stallTime && (driverInput.ChooChooReset() || driverInput.getAutonBool("reset", false))) {
                        goal = pidSetpoint;
                        firstHitSetpoint = false;
                    } else {
                        goal = 0.0;
                    }
                }
            }

            double currentValue = sensorInput.getCatapultEncoder();

            catapultMotorSpeed = (currentValue < goal) ? 1.0 : 0.0;
            SmartDashboard.putBoolean("CatapultRunning", (currentValue < goal));
            
            if(currentValue >= goal)
            {
                firstHitSetpoint = true;
            }
            
            if(catapultMotorSpeed <= 0 || (Math.abs(currentValue - goal) < tolerance) && firstHitSetpoint)
            {
                catapultMotorSpeed = 0.0;
            }
            
            if((Math.abs(currentValue - pidSetpoint) < tolerance) && firstHitSetpoint)
            {
                isReady = true;
            }
            
            if (Timer.getFPGATimestamp() - contactTime < stallTime) {
                catapultMotorSpeed = 0.0;
            }
            
            SmartDashboard.putNumber("CatapultSetpoint", goal);
            SmartDashboard.putNumber("CatapultActual", currentValue);
            SmartDashboard.putNumber("CatapultMotorSpeed", catapultMotorSpeed);
        } else {
            if(driverInput.ChooChooOverride() || driverInput.ChooChooReset() || driverInput.getAutonBool("shoot", false))
            {
                firstCycle = false;
            }
        }
    }

    public void setPosition(double desired) {
        if (desired != desiredPullbackPosition) {
            desiredPullbackPosition = desired;
            goal = desired;
        }
    }
    
    public boolean getIntakeDownOverride() {
        return intakeDownOverride;
    }

    public void setMotorSpeed(double speed) {
        catapultMotorSpeed = speed;
    }

    public void setStandoffs(boolean highShot) {
        if (standoffPosition != highShot) {
            standoffPosition = highShot;
        }
    }
    
    public void resetFired() {
        fired = false;
    }
    
    public boolean isFired() {
        return fired;
    }
    
    public boolean catapultReady() {
        return isReady;
    }

    public void setToRobot() {
        robotOutput.setCatapultMotor(catapultMotorSpeed);
        robotOutput.setCatapultMode(standoffPosition);
    }

    public void loadParameters() {
        firstCycle = true;
        overrideSpeed = params.getAsDouble("C_ChooChooSpeed", 0.0);
        stallTime = params.getAsDouble("C_StallTime", 1.0);
        pidSetpoint = params.getAsDouble("C_ResetSetpoint", 0.0);
        tolerance = params.getAsDouble("C_Tollerance", 10.0);
        
    }

    public String logData() { //no logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }

}
