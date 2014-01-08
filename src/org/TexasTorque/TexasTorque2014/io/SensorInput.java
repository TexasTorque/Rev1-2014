package org.TexasTorque.TexasTorque2014.io;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Watchdog;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.constants.Ports;
import org.TexasTorque.TexasTorque2014.io.dependency.SensorInputState;
import org.TexasTorque.TorqueLib.component.TorqueEncoder;

public class SensorInput
{
    private static SensorInput instance;
    private static SensorInputState state;
    private Watchdog watchdog;

    //----- Encoder -----
    private TorqueEncoder frontLeftDriveEncoder;
    private TorqueEncoder frontRightDriveEncoder;
    private TorqueEncoder rearLeftDriveEncoder;
    private TorqueEncoder rearRightDriveEncoder;

    //----- Analog -----
    private AnalogChannel pressureSensor;
    private AnalogChannel gyroChannel;
    public Gyro gyro;

    public SensorInput()
    {
        watchdog = Watchdog.getInstance();
        
        //----- Encoders/Counters -----
        frontLeftDriveEncoder = new TorqueEncoder(Ports.LEFT_DRIVE_SIDECAR, Ports.FRONT_LEFT_DRIVE_ENCODER_A_PORT, Ports.LEFT_DRIVE_SIDECAR, Ports.FRONT_LEFT_DRIVE_ENCODER_B_PORT, false);
        frontRightDriveEncoder = new TorqueEncoder(Ports.RIGHT_DRIVE_SIDECAR, Ports.FRONT_RIGHT_DRIVE_ENCODER_A_PORT, Ports.RIGHT_DRIVE_SIDECAR, Ports.FRONT_RIGHT_DRIVE_ENCODER_B_PORT, false);
        rearLeftDriveEncoder = new TorqueEncoder(Ports.LEFT_DRIVE_SIDECAR, Ports.REAR_LEFT_DRIVE_ENCODER_A_PORT, Ports.LEFT_DRIVE_SIDECAR, Ports.REAR_LEFT_DRIVE_ENCODER_B_PORT, false);
        rearRightDriveEncoder = new TorqueEncoder(Ports.RIGHT_DRIVE_SIDECAR, Ports.REAR_RIGHT_DRIVE_ENCODER_A_PORT, Ports.RIGHT_DRIVE_SIDECAR, Ports.REAR_RIGHT_DRIVE_ENCODER_B_PORT, false);
        
        //----- Gyro -----
        gyroChannel = new AnalogChannel(Ports.GYRO_PORT);
        gyro = new Gyro(gyroChannel);
        gyro.reset();
        gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
        
        //----- Misc -----
        pressureSensor = new AnalogChannel(Ports.ANALOG_PRESSURE_PORT);
        
        startEncoders();
        
        state = new SensorInputState(this);
    }
    
    public synchronized static SensorInput getInstance()
    {
        return (instance == null) ? instance = new SensorInput() : instance;
    }
    public synchronized static SensorInputState getState()
    {
        return state;
    }
    public synchronized void updateState()
    {
        state.update(this);
    }
    
    private void startEncoders()
    {
        // 1 foot = ??? clicks
        frontLeftDriveEncoder.start();
        frontRightDriveEncoder.start();        
    }
    
    public void resetEncoders()
    {
        frontLeftDriveEncoder.reset();
        frontRightDriveEncoder.reset();
    }
    
    public void calcEncoders()
    {
        frontLeftDriveEncoder.calc();
        frontRightDriveEncoder.calc();
        rearLeftDriveEncoder.calc();
        rearRightDriveEncoder.calc();
    }
    
    public void resetGyro()
    {
        gyro.reset();
        gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
    }
    
    public double getFrontLeftDriveEncoder()
    {
        return (frontLeftDriveEncoder.get()); 
    }
    
    public double getFrontRightDriveEncoder()
    {
        return (frontRightDriveEncoder.get());
    }
    
    public double getFrontLeftDriveEncoderRate()
    {
        return (frontLeftDriveEncoder.getRate());
    }
    
    public double getFrontRightDriveEncoderRate()
    {
        return (frontRightDriveEncoder.getRate());
    }
    
    public double getFrontLeftDriveEncoderAcceleration()
    {
        return (frontLeftDriveEncoder.getAcceleration());
    }
    
    public double getFrontRightDriveEncoderAcceleration()
    {
        return (frontRightDriveEncoder.getAcceleration());
    }
    public double getRearLeftDriveEncoder()
    {
        return (rearLeftDriveEncoder.get()); 
    }
    
    public double getRearRightDriveEncoder()
    {
        return (rearRightDriveEncoder.get());
    }
    
    public double getRearLeftDriveEncoderRate()
    {
        return (rearLeftDriveEncoder.getRate());
    }
    
    public double getRearRightDriveEncoderRate()
    {
        return (rearRightDriveEncoder.getRate());
    }
    
    public double getRearLeftDriveEncoderAcceleration()
    {
        return (rearLeftDriveEncoder.getAcceleration());
    }
    
    public double getRearRightDriveEncoderAcceleration()
    {
        return (rearRightDriveEncoder.getAcceleration());
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
