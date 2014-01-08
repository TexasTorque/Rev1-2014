package org.TexasTorque.TexasTorque2014.io;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.constants.Ports;
import org.TexasTorque.TexasTorque2014.io.dependency.RobotOutputState;
import org.TexasTorque.TorqueLib.component.Motor;

public class RobotOutput
{
    private static RobotOutput instance;
    private static RobotOutputState state;
    
//    private AdaFruitLights lights;
    //private Vector lightsVector;
    private int lightState;
    
    //----- Pneumatics -----
    private Compressor compressor;
    private Solenoid driveShifter;
    private boolean shiftState;

    //----- Drive Motors -----
    private Motor frontLeftDriveMotor;
    private Motor rearLeftDriveMotor;
    private Motor frontRightDriveMotor;
    private Motor rearRightDriveMotor;
    private double frontLeftMotorSpeed;
    private double rearLeftMotorSpeed;
    private double frontRightMotorSpeed;
    private double rearRightMotorSpeed;
    private Motor frontLeftAngleDriveMotor;
    private Motor rearLeftAngleDriveMotor;
    private Motor frontRightAngleDriveMotor;
    private Motor rearRightAngleDriveMotor;
    private double frontLeftAngleMotorSpeed;
    private double rearLeftAngleMotorSpeed;
    private double frontRightAngleMotorSpeed;
    private double rearRightAngleMotorSpeed;
    
    public RobotOutput()
    {   
        lightState = 0;
/*        lightsVector = new Vector();
        lightsVector.addElement(new DigitalOutput(Ports.SIDECAR_TWO, Ports.LIGHTS_A_PORT));
        lightsVector.addElement(new DigitalOutput(Ports.SIDECAR_TWO, Ports.LIGHTS_B_PORT));
        lightsVector.addElement(new DigitalOutput(Ports.SIDECAR_TWO, Ports.LIGHTS_C_PORT));
        lightsVector.addElement(new DigitalOutput(Ports.SIDECAR_TWO, Ports.LIGHTS_D_PORT));
        lights = new AdaFruitLights(lightsVector);*/
        
        //----- Pneumatics -----
        compressor = new Compressor(Ports.COMPRESSOR_SIDECAR, Ports.PRESSURE_SWITCH_PORT, Ports.COMPRESSOR_SIDECAR, Ports.COMPRESSOR_RELAY_PORT);
        driveShifter = new Solenoid(Ports.DRIVE_SHIFTER_PORT);
        shiftState = Constants.LOW_GEAR;
        
        //----- Drive Motors -----
        frontLeftDriveMotor = new Motor(new Victor(Ports.LEFT_DRIVE_SIDECAR, Ports.FRONT_LEFT_DRIVE_MOTOR_PORT), false, true);
        rearLeftDriveMotor = new Motor(new Victor(Ports.LEFT_DRIVE_SIDECAR, Ports.REAR_LEFT_DRIVE_MOTOR_PORT), false, true);
        frontRightDriveMotor = new Motor(new Victor(Ports.RIGHT_DRIVE_SIDECAR, Ports.FRONT_RIGHT_DRIVE_MOTOR_PORT), true, true);
        rearRightDriveMotor = new Motor(new Victor(Ports.RIGHT_DRIVE_SIDECAR, Ports.REAR_RIGHT_DRIVE_MOTOR_PORT), true, true);
        frontLeftAngleDriveMotor = new Motor(new Victor(Ports.LEFT_DRIVE_SIDECAR, Ports.FRONT_LEFT_ANGLE_DRIVE_MOTOR_PORT), false, true);
        rearLeftAngleDriveMotor = new Motor(new Victor(Ports.LEFT_DRIVE_SIDECAR, Ports.REAR_LEFT_ANGLE_DRIVE_MOTOR_PORT), false, true);
        frontRightAngleDriveMotor = new Motor(new Victor(Ports.RIGHT_DRIVE_SIDECAR, Ports.FRONT_RIGHT_ANGLE_DRIVE_MOTOR_PORT), true, true);
        rearRightAngleDriveMotor = new Motor(new Victor(Ports.RIGHT_DRIVE_SIDECAR, Ports.REAR_RIGHT_ANGLE_DRIVE_MOTOR_PORT), true, true);
        frontRightMotorSpeed = Constants.MOTOR_STOPPED;
        rearRightMotorSpeed = Constants.MOTOR_STOPPED;
        frontLeftMotorSpeed = Constants.MOTOR_STOPPED;
        rearLeftMotorSpeed = Constants.MOTOR_STOPPED;
        frontRightAngleMotorSpeed = Constants.MOTOR_STOPPED;
        rearRightAngleMotorSpeed = Constants.MOTOR_STOPPED;
        frontLeftAngleMotorSpeed = Constants.MOTOR_STOPPED;
        rearLeftAngleMotorSpeed = Constants.MOTOR_STOPPED;
        
        state = new RobotOutputState(this);
        
        //----- Misc Misc -----
        compressor.start();
        
    }
 
    public synchronized static RobotOutput getInstance()
    {
        return (instance == null) ? instance = new RobotOutput() : instance;
    }
    public synchronized static RobotOutputState getState()
    {
        return state;
    }
    public synchronized void updateState()
    {
        state.update(this);
    }
    public synchronized void pullFromState()
    {
        setDriveSpeedMotors(state.getFrontRightMotorSpeed(), state.getFrontLeftMotorSpeed(), state.getRearRightMotorSpeed(), state.getRearLeftMotorSpeed());
        setDriveAngleMotors(state.getFrontRightAngleMotorSpeed(), state.getFrontLeftAngleMotorSpeed(), state.getRearRightAngleMotorSpeed(), state.getRearLeftAngleMotorSpeed());
        setLightsState(state.getLightState());
        setShifters(state.getShiftState());
    }
    
    public void setLightsState(int state)
    {
        //lights.setDesiredState(state);
    }
    
    public void runLights()
    {
        //lights.run();
    }
    
    public int getLightState()
    {
        return lightState;
    }
    
    public synchronized void setDriveSpeedMotors(double frontRightMotorSpeed, double frontLeftMotorSpeed, double rearRightMotorSpeed, double rearLeftMotorSpeed)
    {
        this.frontLeftMotorSpeed = frontLeftMotorSpeed;
        this.rearLeftMotorSpeed = rearLeftMotorSpeed;
        this.frontRightMotorSpeed = frontRightMotorSpeed;
        this.rearRightMotorSpeed = rearRightMotorSpeed;
        frontLeftDriveMotor.Set(frontLeftMotorSpeed);
        frontRightDriveMotor.Set(frontRightMotorSpeed);
        rearLeftDriveMotor.Set(rearLeftMotorSpeed);
        rearRightDriveMotor.Set(rearRightMotorSpeed);
        
    }
    public synchronized void setDriveAngleMotors(double frontRightMotorSpeed, double frontLeftMotorSpeed, double rearRightMotorSpeed, double rearLeftMotorSpeed)
    {
        this.frontLeftAngleMotorSpeed = frontLeftMotorSpeed;
        this.rearLeftAngleMotorSpeed = rearLeftMotorSpeed;
        this.frontRightAngleMotorSpeed = frontRightMotorSpeed;
        this.rearRightAngleMotorSpeed = rearRightMotorSpeed;
        frontLeftAngleDriveMotor.Set(frontLeftMotorSpeed);
        frontRightAngleDriveMotor.Set(frontRightMotorSpeed);
        rearLeftAngleDriveMotor.Set(rearLeftMotorSpeed);
        rearRightAngleDriveMotor.Set(rearRightMotorSpeed);
    }
    
    public double getFrontLeftMotorSpeed()
    {
        return frontLeftMotorSpeed;
    }
    public double getFrontRightMotorSpeed()
    {
        return frontRightMotorSpeed;
    }
    public double getRearLeftMotorSpeed()
    {
        return rearLeftMotorSpeed;
    }
    public double getRearRightMotorSpeed()
    {
        return rearRightMotorSpeed;
    }
    public double getFrontLeftAngleMotorSpeed()
    {
        return frontLeftAngleMotorSpeed;
    }
    public double getRearLeftAngleMotorSpeed()
    {
        return rearLeftAngleMotorSpeed;
    }
    public double getFrontRightAngleMotorSpeed()
    {
        return frontRightAngleMotorSpeed;
    }
    public double getRearRightAngleMotorSpeed()
    {
        return rearRightAngleMotorSpeed;
    }
    public void setShifters(boolean highGear)
    {
        driveShifter.set(highGear);
        shiftState = highGear;
    }
    public boolean getCompressorEnabled()
    {
        return compressor.enabled();
    }
    public boolean getShiftState()
    {
        return shiftState;
    }
}
