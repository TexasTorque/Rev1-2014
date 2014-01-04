package org.TexasTorque.TexasTorque2014.io;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Watchdog;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.constants.Ports;
import org.TexasTorque.TorqueLib.component.TorqueEncoder;

public class SensorInput
{
    private static SensorInput instance;
    private Watchdog watchdog;

    //----- Encoder -----
    private TorqueEncoder leftDriveEncoder;
    private TorqueEncoder rightDriveEncoder;

    //----- Analog -----
    private AnalogChannel pressureSensor;
    private AnalogChannel gyroChannel;
    public Gyro gyro;

    public SensorInput()
    {
        watchdog = Watchdog.getInstance();
        
        //----- Encoders/Counters -----
        leftDriveEncoder = new TorqueEncoder(Ports.LEFT_DRIVE_SIDECAR, Ports.LEFT_DRIVE_ENCODER_A_PORT, Ports.LEFT_DRIVE_SIDECAR, Ports.LEFT_DRIVE_ENCODER_B_PORT, false);
        rightDriveEncoder = new TorqueEncoder(Ports.RIGHT_DRIVE_SIDECAR, Ports.RIGHT_DRIVE_ENCODER_A_PORT, Ports.RIGHT_DRIVE_SIDECAR, Ports.RIGHT_DRIVE_ENCODER_B_PORT, false);
        
        //----- Gyro -----
        gyroChannel = new AnalogChannel(Ports.GYRO_PORT);
        gyro = new Gyro(gyroChannel);
        gyro.reset();
        gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
        
        //----- Misc -----
        pressureSensor = new AnalogChannel(Ports.ANALOG_PRESSURE_PORT);
        startEncoders();
    }
    
    public synchronized static SensorInput getInstance()
    {
        return (instance == null) ? instance = new SensorInput() : instance;
    }
    
    private void startEncoders()
    {
        // 1 foot = ??? clicks
        leftDriveEncoder.start();
        rightDriveEncoder.start();        
    }
    
    public void resetEncoders()
    {
        leftDriveEncoder.reset();
        rightDriveEncoder.reset();
    }
    
    public void calcEncoders()
    {
        leftDriveEncoder.calc();
        rightDriveEncoder.calc();
    }
    
    public void resetGyro()
    {
        gyro.reset();
        gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
    }
    
    public double getLeftDriveEncoder()
    {
        return (leftDriveEncoder.get()); 
    }
    
    public double getRightDriveEncoder()
    {
        return (rightDriveEncoder.get());
    }
    
    public double getLeftDriveEncoderRate()
    {
        return (leftDriveEncoder.getRate());
    }
    
    public double getRightDriveEncoderRate()
    {
        return (rightDriveEncoder.getRate());
    }
    
    public double getLeftDriveEncoderAcceleration()
    {
        return (leftDriveEncoder.getAcceleration());
    }
    
    public double getRightDriveEncoderAcceleration()
    {
        return (rightDriveEncoder.getAcceleration());
    }
    
    public double getPSI()
    {
        return pressureSensor.getVoltage();
    }
    
    public double getGyroAngle()
    {
        return limitGyroAngle(-gyro.getAngle() * 2);
    }
    
    public double limitGyroAngle(double angle)
    {
        while(angle >= 360.0)
        {
            watchdog.feed();
            angle -= 360.0;
        }
        while(angle < 0.0)
        {
            watchdog.feed();
            angle += 360.0;
        }
        if(angle > 180)
        {
            angle -= 360;
        }
        return angle;
    }
}
