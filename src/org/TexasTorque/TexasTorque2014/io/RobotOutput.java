package org.TexasTorque.TexasTorque2014.io;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import java.util.Vector;
import org.TexasTorque.TexasTorque2014.constants.Ports;
import org.TexasTorque.TorqueLib.component.Motor;

public class RobotOutput
{
    private static RobotOutput instance;
    
//    private AdaFruitLights lights;
    private Vector lightsVector;
    
    //----- Pneumatics -----
    private Compressor compressor;
    private Solenoid driveShifter;

    //----- Drive Motors -----
    private Motor frontLeftDriveMotor;
    private Motor rearLeftDriveMotor;
    private Motor frontRightDriveMotor;
    private Motor rearRightDriveMotor;
    
    public RobotOutput()
    {   
        
/*        lightsVector = new Vector();
        lightsVector.addElement(new DigitalOutput(Ports.SIDECAR_TWO, Ports.LIGHTS_A_PORT));
        lightsVector.addElement(new DigitalOutput(Ports.SIDECAR_TWO, Ports.LIGHTS_B_PORT));
        lightsVector.addElement(new DigitalOutput(Ports.SIDECAR_TWO, Ports.LIGHTS_C_PORT));
        lightsVector.addElement(new DigitalOutput(Ports.SIDECAR_TWO, Ports.LIGHTS_D_PORT));
        lights = new AdaFruitLights(lightsVector);*/
        
        //----- Pneumatics -----
        compressor = new Compressor(Ports.COMPRESSOR_SIDECAR, Ports.PRESSURE_SWITCH_PORT, Ports.COMPRESSOR_SIDECAR, Ports.COMPRESSOR_RELAY_PORT);
        driveShifter = new Solenoid(Ports.DRIVE_SHIFTER_PORT);
        
        //----- Drive Motors -----
        frontLeftDriveMotor = new Motor(new Victor(Ports.LEFT_DRIVE_SIDECAR, Ports.FRONT_LEFT_DRIVE_MOTOR_PORT), false, true);
        rearLeftDriveMotor = new Motor(new Victor(Ports.LEFT_DRIVE_SIDECAR, Ports.REAR_LEFT_DRIVE_MOTOR_PORT), false, true);
        frontRightDriveMotor = new Motor(new Victor(Ports.RIGHT_DRIVE_SIDECAR, Ports.FRONT_RIGHT_DRIVE_MOTOR_PORT), true, true);
        rearRightDriveMotor = new Motor(new Victor(Ports.RIGHT_DRIVE_SIDECAR, Ports.REAR_RIGHT_DRIVE_MOTOR_PORT), true, true);
        
        //----- Misc Misc -----
        compressor.start();
    }
 
    public synchronized static RobotOutput getInstance()
    {
        return (instance == null) ? instance = new RobotOutput() : instance;
    }
    
    public void setLightsState(int state)
    {
        //lights.setDesiredState(state);
    }
    
    public void runLights()
    {
        //lights.run();
    }
    
    public void setDriveMotors(double leftSpeed, double rightSpeed)
    {
        frontLeftDriveMotor.Set(leftSpeed);
        rearLeftDriveMotor.Set(leftSpeed);
        frontRightDriveMotor.Set(rightSpeed);
        rearRightDriveMotor.Set(rightSpeed);
    }
    
    public void setShifters(boolean highGear)
    {
        driveShifter.set(highGear);
    }
}
