package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class Catapult extends TorqueSubsystem {
    private static Catapult instance;
    
    private double catapultMotorSpeed;
    private boolean standoffPosition;
    
    private TorquePID pullBackPID;
    
    public static Catapult getInstance()
    {
        return (instance == null) ? instance = new Catapult() : instance;
    }
    
    public Catapult()
    {
        super();
        
        catapultMotorSpeed = Constants.MOTOR_STOPPED;
        pullBackPID = new TorquePID();
    }

    public void run() {
        
        
        setToRobot();
    }
    
    public void setPosition()
    {
        
    }
    
    public void setStandoffs(boolean highShot)
    {
        if (standoffPosition != highShot)
        {
            standoffPosition = highShot;
        }
    }

    public void setToRobot() {
        robotOutput.setCatapultMotor(catapultMotorSpeed);
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
    }

    public String logData() { //no logging
        return "";
    }

    public String getKeyNames() {
        return "";
    }

}
