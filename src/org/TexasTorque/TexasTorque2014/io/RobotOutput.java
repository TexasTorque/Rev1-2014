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

    //----- Drive Motors -----
    private Motor leftFrontDriveMotor;
    private Motor leftRearDriveMotor;
    private Motor rightFrontDriveMotor;
    private Motor rightRearDriveMotor;
    private double leftFrontMotorSpeed;
    private double leftRearMotorSpeed;
    private double rightFrontMotorSpeed;
    private double rightRearMotorSpeed;
    
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
        
        //----- Drive Motors -----
        leftFrontDriveMotor = new Motor(new Victor(Ports.LEFT_DRIVE_SIDECAR, Ports.LEFT_FRONT_DRIVE_MOTOR_PORT), false, true);
        leftRearDriveMotor = new Motor(new Victor(Ports.LEFT_DRIVE_SIDECAR, Ports.LEFT_REAR_DRIVE_MOTOR_PORT), false, true);
        rightFrontDriveMotor = new Motor(new Victor(Ports.RIGHT_DRIVE_SIDECAR, Ports.RIGHT_FRONT_DRIVE_MOTOR_PORT), true, true);
        rightRearDriveMotor = new Motor(new Victor(Ports.RIGHT_DRIVE_SIDECAR, Ports.RIGHT_REAR_DRIVE_MOTOR_PORT), true, true);
        rightFrontMotorSpeed = Constants.MOTOR_STOPPED;
        rightRearMotorSpeed = Constants.MOTOR_STOPPED;
        leftFrontMotorSpeed = Constants.MOTOR_STOPPED;
        leftRearMotorSpeed = Constants.MOTOR_STOPPED;
        
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
        state = new RobotOutputState(this);
    }
    public synchronized void pullFromState()
    {
        setLightsState(state.getLightState());
        setDriveMotors(state.getLeftFrontMotorSpeed(),state.getLeftRearMotorSpeed(), state.getRightFrontMotorSpeed(), state.getRightRearMotorSpeed());
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
    
    public void setDriveMotors(double leftFrontSpeed, double leftRearSpeed, double rightFrontSpeed, double rightRearSpeed)
    {
        leftFrontDriveMotor.Set(leftFrontSpeed);
        leftRearDriveMotor.Set(leftRearSpeed);
        rightFrontDriveMotor.Set(rightFrontSpeed);
        rightRearDriveMotor.Set(rightRearSpeed);
        leftFrontMotorSpeed = leftFrontSpeed;
        rightFrontMotorSpeed = rightFrontSpeed;
        leftRearMotorSpeed = leftRearSpeed;
        rightRearMotorSpeed = rightRearSpeed;
    }
    
    public double getLeftFrontMotorSpeed()
    {
        return leftFrontMotorSpeed;
    }
    public double getRightFrontMotorSpeed()
    {
        return rightFrontMotorSpeed;
    }
    public double getLeftRearMotorSpeed()
    {
        return leftRearMotorSpeed;
    }
    public double getRightRearMotorSpeed()
    {
        return rightRearMotorSpeed;
    }
    public boolean getCompressorEnabled()
    {
        return compressor.enabled();
    }
}
