package org.TexasTorque.TexasTorque2014.io;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.constants.Ports;
import org.TexasTorque.TexasTorque2014.io.dependency.SensorInputState;
import org.TexasTorque.TorqueLib.component.TorqueEncoder;
import org.TexasTorque.TorqueLib.component.TorquePotentiometer;

public class SensorInput
{
    private static SensorInput instance;
    private static SensorInputState state;
    private Watchdog watchdog;

    //----- Encoder -----
    private TorqueEncoder leftFrontDriveEncoder;
    private TorqueEncoder rightFrontDriveEncoder;
    private TorqueEncoder leftRearDriveEncoder;
    private TorqueEncoder rightRearDriveEncoder;

    //----- Analog -----
    private AnalogChannel pressureSensor;
    private AnalogChannel gyroChannel;
    public Gyro gyro;
    private TorquePotentiometer frontIntakeTiltPotentiometer;
    private TorquePotentiometer rearIntakeTiltPotentiometer;

    public SensorInput()
    {
        
        watchdog = Watchdog.getInstance();
        
        //----- Encoders/Counters -----
        leftFrontDriveEncoder = new TorqueEncoder(Ports.LEFT_FRONT_DRIVE_SIDECAR, Ports.LEFT_FRONT_DRIVE_ENCODER_A_PORT, Ports.LEFT_FRONT_DRIVE_SIDECAR, Ports.LEFT_FRONT_DRIVE_ENCODER_B_PORT, false);
        leftRearDriveEncoder = new TorqueEncoder(Ports.LEFT_REAR_DRIVE_SIDECAR, Ports.LEFT_REAR_DRIVE_ENCODER_A_PORT, Ports.LEFT_REAR_DRIVE_SIDECAR, Ports.LEFT_REAR_DRIVE_ENCODER_B_PORT, false);
        rightFrontDriveEncoder = new TorqueEncoder(Ports.RIGHT_FRONT_DRIVE_SIDECAR, Ports.RIGHT_FRONT_DRIVE_ENCODER_A_PORT, Ports.RIGHT_FRONT_DRIVE_SIDECAR, Ports.RIGHT_FRONT_DRIVE_ENCODER_B_PORT, false);
        rightRearDriveEncoder = new TorqueEncoder(Ports.RIGHT_REAR_DRIVE_SIDECAR, Ports.RIGHT_REAR_DRIVE_ENCODER_A_PORT, Ports.RIGHT_REAR_DRIVE_SIDECAR, Ports.RIGHT_REAR_DRIVE_ENCODER_B_PORT, false);
        
        //----- Gyro -----
        //gyroChannel = new AnalogChannel(Ports.GYRO_PORT);
        //gyro = new Gyro(gyroChannel);
        //gyro.reset();
        //gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
        
        //----- Potentiometers -----
        frontIntakeTiltPotentiometer = new TorquePotentiometer(Ports.SIDECAR_ONE, Ports.FRONT_INTAKE_TILT_POT_PORT);
        rearIntakeTiltPotentiometer = new TorquePotentiometer(Ports.SIDECAR_ONE, Ports.REAR_INTAKE_TILT_POT_PORT);
        frontIntakeTiltPotentiometer.setRange(Constants.INTAKE_POTENTIOMETER_LOW, Constants.INTAKE_POTENTIOMETER_HIGH);
        rearIntakeTiltPotentiometer.setRange(Constants.INTAKE_POTENTIOMETER_LOW, Constants.INTAKE_POTENTIOMETER_HIGH);
        
        //----- Misc -----
        //pressureSensor = new AnalogChannel(Ports.ANALOG_PRESSURE_PORT);
        startEncoders();
                
    }
    
    public synchronized static SensorInput getInstance()
    {
        return (instance == null) ? instance = new SensorInput() : instance;
    }
    public synchronized static SensorInputState getState()
    {
        return (state == null) ? state = new SensorInputState() : state;
    }
    public synchronized void updateState()
    {
        state.updateState(this);
    }
    
    private void startEncoders()
    {
        // 1 foot = ??? clicks
        leftFrontDriveEncoder.start();
        rightFrontDriveEncoder.start();    
        leftRearDriveEncoder.start();
        rightRearDriveEncoder.start();
    }
    
    public void resetEncoders()
    {
        leftFrontDriveEncoder.reset();
        rightFrontDriveEncoder.reset();
        leftRearDriveEncoder.reset();
        rightRearDriveEncoder.reset();
    }
    
    public void calcEncoders()
    {
        leftFrontDriveEncoder.calc();
        rightFrontDriveEncoder.calc();
        leftRearDriveEncoder.calc();
        rightRearDriveEncoder.calc();
    }
    
    public void resetGyro()
    {
        gyro.reset();
        gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
    }
    
    public double getLeftFrontDriveEncoder()
    {
        return (leftFrontDriveEncoder.get()); 
    }
    
    public double getRightFrontDriveEncoder()
    {
        return (rightFrontDriveEncoder.get());
    }
    
    public double getLeftFrontDriveEncoderRate()
    {
        return (leftFrontDriveEncoder.getRate());
    }
    
    public double getRightFrontDriveEncoderRate()
    {
        return (rightFrontDriveEncoder.getRate());
    }
    
    public double getLeftFrontDriveEncoderAcceleration()
    {
        return (leftFrontDriveEncoder.getAcceleration());
    }
    
    public double getRightFrontDriveEncoderAcceleration()
    {
        return (rightFrontDriveEncoder.getAcceleration());
    }
    
    public double getLeftRearDriveEncoder()
    {
        return (leftRearDriveEncoder.get()); 
    }
    
    public double getRightRearDriveEncoder()
    {
        return (rightRearDriveEncoder.get());
    }
    
    public double getLeftRearDriveEncoderRate()
    {
        return (leftRearDriveEncoder.getRate());
    }
    
    public double getRightRearDriveEncoderRate()
    {
        return (rightRearDriveEncoder.getRate());
    }
    
    public double getLeftRearDriveEncoderAcceleration()
    {
        return (leftRearDriveEncoder.getAcceleration());
    }
    
    public double getRightRearDriveEncoderAcceleration()
    {
        return (rightRearDriveEncoder.getAcceleration());
    }
    
    public double getFrontIntakeTiltPotentiometer()
    {
        SmartDashboard.putNumber("FrontIntakeRaw", frontIntakeTiltPotentiometer.get());
        return frontIntakeTiltPotentiometer.get();
    }
    
    public double getRearIntakeTiltPotentiometer()
    {
        SmartDashboard.putNumber("RearIntakeRaw", rearIntakeTiltPotentiometer.get());
        return rearIntakeTiltPotentiometer.get();
    }
    
    public double getPSI()
    {
        return 0.0;//pressureSensor.getVoltage();
    }
    
    public double getGyroAngle()
    {
        return 0.0;//limitGyroAngle(-gyro.getAngle() * 2);
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
