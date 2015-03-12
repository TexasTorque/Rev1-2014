package org.TexasTorque.TexasTorque2014.io;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Watchdog;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.constants.Ports;
import org.TexasTorque.TexasTorque2014.io.dependency.SensorInputState;
import org.TexasTorque.TorqueLib.component.TorqueCounter;
import org.TexasTorque.TorqueLib.component.TorqueEncoder;
import org.TexasTorque.TorqueLib.component.TorquePotentiometer;
import org.TexasTorque.TorqueLib.util.Parameters;

public class SensorInput {

    private static SensorInput instance;
    private static SensorInputState state;
    private Watchdog watchdog;
    private Parameters params;

    //----- Limit Switch -----
    private DigitalInput catapultLimitSwitch;
    private DigitalInput catapultLimitSwitchB;
    private DigitalInput frontIntakeButton;
    private DigitalInput rearIntakeButton;

    //----- Encoder -----
    private TorqueEncoder leftFrontDriveEncoder;
    private TorqueEncoder rightFrontDriveEncoder;
    private TorqueCounter leftRearDriveCounter;
    private TorqueCounter rightRearDriveCounter;
    private TorqueEncoder catapultEncoder;

    //----- Analog -----
    private AnalogChannel pressureSensor;
    private AnalogChannel gyroChannel;
    public Gyro gyro;
    private TorquePotentiometer frontIntakeTiltPotentiometer;
    private TorquePotentiometer rearIntakeTiltPotentiometer;

    public SensorInput() {

        watchdog = Watchdog.getInstance();
        params = Parameters.getTeleopInstance();

        //----- Encoders/Counters -----
        leftFrontDriveEncoder = new TorqueEncoder(Ports.LEFT_FRONT_DRIVE_ENCODER_SIDECAR, Ports.LEFT_FRONT_DRIVE_ENCODER_A_PORT, Ports.LEFT_FRONT_DRIVE_ENCODER_SIDECAR, Ports.LEFT_FRONT_DRIVE_ENCODER_B_PORT, false);
        leftRearDriveCounter = new TorqueCounter(Ports.LEFT_REAR_DRIVE_ENCODER_SIDECAR, Ports.LEFT_REAR_DRIVE_ENCODER_PORT);
        rightFrontDriveEncoder = new TorqueEncoder(Ports.RIGHT_FRONT_DRIVE_ENCODER_SIDECAR, Ports.RIGHT_FRONT_DRIVE_ENCODER_A_PORT, Ports.RIGHT_FRONT_DRIVE_ENCODER_SIDECAR, Ports.RIGHT_FRONT_DRIVE_ENCODER_B_PORT,false);
        rightRearDriveCounter = new TorqueCounter(Ports.RIGHT_REAR_DRIVE_ENCODER_SIDECAR, Ports.RIGHT_REAR_DRIVE_ENCODER_PORT);

        catapultEncoder = new TorqueEncoder(Ports.CATAPULT_ENCODER_SIDECAR, Ports.CATAPULT_ENCODER_A_PORT, Ports.CATAPULT_ENCODER_SIDECAR, Ports.CATAPULT_ENCODER_B_PORT, false);

        //----- Gyro -----
        gyroChannel = new AnalogChannel(Ports.GYRO_PORT);
        gyro = new Gyro(gyroChannel);
        gyro.reset();
        gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
        
        //----- Potentiometers -----
        
        rearIntakeTiltPotentiometer = new TorquePotentiometer(Ports.REAR_INTAKE_TILT_POT_PORT);
        frontIntakeTiltPotentiometer = new TorquePotentiometer(Ports.FRONT_INTAKE_TILT_POT_PORT);
        frontIntakeTiltPotentiometer.setRange(Constants.FRONT_INTAKE_POTENTIOMETER_LOW, Constants.FRONT_INTAKE_POTENTIOMETER_HIGH);
        rearIntakeTiltPotentiometer.setRange(Constants.REAR_INTAKE_POTENTIOMETER_LOW, Constants.REAR_INTAKE_POTENTIOMETER_HIGH);
        
        //----- Buttons -----
        catapultLimitSwitch = new DigitalInput(Ports.CATAPULT_LIMIT_SWITCH_B_SIDECAR, Ports.CATAPULT_LIMIT_SWITCH_B_PORT);
        catapultLimitSwitchB = new DigitalInput(Ports.CATAPULT_LIMIT_SWITCH_SIDECAR, Ports.CATAPULT_LIMIT_SWITCH_PORT);
        frontIntakeButton = new DigitalInput(Ports.INTAKE_BUTTON_SIDECAR, Ports.FRONT_INTAKE_BUTTON);
        rearIntakeButton = new DigitalInput(Ports.INTAKE_BUTTON_SIDECAR, Ports.REAR_INTAKE_BUTTON);
        
        //pressureSensor = new AnalogChannel(Ports.ANALOG_PRESSURE_PORT);
        startEncoders();

    }

    public synchronized static SensorInput getInstance() {
        return (instance == null) ? instance = new SensorInput() : instance;
    }

    public synchronized static SensorInputState getState() {
        return (state == null) ? state = new SensorInputState() : state;
    }

    public synchronized void updateState() {
        state.updateState(this);
    }

    private void startEncoders() {
        // 1 foot = ??? clicks
        leftFrontDriveEncoder.start();
        rightFrontDriveEncoder.start();
        leftRearDriveCounter.start();
        rightRearDriveCounter.start();
        catapultEncoder.start();
        frontIntakeTiltPotentiometer.reset();
        rearIntakeTiltPotentiometer.reset();
        gyro.reset();
    }

    public void resetEncoders() {
        leftFrontDriveEncoder.reset();
        rightFrontDriveEncoder.reset();
        leftRearDriveCounter.reset();
        rightRearDriveCounter.reset();
        catapultEncoder.reset();
        frontIntakeTiltPotentiometer.reset();
        rearIntakeTiltPotentiometer.reset();
        gyro.reset();
    }
    
    public void resetCatapultEncoder()
    {
        catapultEncoder.reset();
    }
    
    public void resetDriveEncoders() {
        leftFrontDriveEncoder.reset();
        rightFrontDriveEncoder.reset();
        leftRearDriveCounter.reset();
        rightRearDriveCounter.reset();
    }

    public void calcEncoders() {
        leftFrontDriveEncoder.calc();
        rightFrontDriveEncoder.calc();
        leftRearDriveCounter.calc();
        rightRearDriveCounter.calc();
        catapultEncoder.calc();
        frontIntakeTiltPotentiometer.run();
        rearIntakeTiltPotentiometer.run();
    }

    public void resetGyro() {
        gyro.reset();
        gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
    }

    public double getLeftFrontDriveEncoder() {
        return (leftFrontDriveEncoder.get());
    }

    public double getRightFrontDriveEncoder() {
        return (rightFrontDriveEncoder.get());
    }

    public double getLeftFrontDriveEncoderRate() {
        return (leftFrontDriveEncoder.getRate());
    }

    public double getRightFrontDriveEncoderRate() {
        return (rightFrontDriveEncoder.getRate());
    }

    public double getLeftFrontDriveEncoderAcceleration() {
        return (leftFrontDriveEncoder.getRate());
    }

    public double getRightFrontDriveEncoderAcceleration() {
        return (rightFrontDriveEncoder.getRate());
    }

    public double getLeftRearDriveEncoder() {
        return (leftRearDriveCounter.get());
    }

    public double getRightRearDriveEncoder() {
        return (rightRearDriveCounter.get());
    }

    public double getLeftRearDriveEncoderRate() {
        return (leftRearDriveCounter.getRate());
    }

    public double getRightRearDriveEncoderRate() {
        return (rightRearDriveCounter.getRate());
    }

    public double getLeftRearDriveEncoderAcceleration() {
        return (leftRearDriveCounter.getRate());
    }

    public double getRightRearDriveEncoderAcceleration() {
        return (rightRearDriveCounter.getRate());
    }

    public double getFrontIntakeTiltPotentiometer() {
        return frontIntakeTiltPotentiometer.get();
    }

    public double getRearIntakeTiltPotentiometer() {
        return rearIntakeTiltPotentiometer.get();
    }

    public double getRearIntakeTiltVoltage() {
        return rearIntakeTiltPotentiometer.getRaw();
    }

    public double getFrontIntakeTiltVoltage() {
        return frontIntakeTiltPotentiometer.getRaw();
    }

    public double getCatapultEncoder() {
        return catapultEncoder.get();
    }

    public boolean getCatapultLimitSwitch() {
        return !catapultLimitSwitch.get();
    }
    
    public boolean getCatapultLimitSwitchB() {
        //return !catapultLimitSwitchB.get();
        return false;
    }
    
    public boolean getFrontIntakeButton()
    {
        return !frontIntakeButton.get();
    }
    
    public boolean getRearIntakeButton()
    {
        return !rearIntakeButton.get();
    }

    public double getPSI() {
        return 0.0;//pressureSensor.getVoltage();
    }

    public double getGyroAngle() {
        return limitGyroAngle(-gyro.getAngle() * 2);
    }

    public double limitGyroAngle(double angle) {
        while (angle >= 360.0) {
            watchdog.feed();
            angle -= 360.0;
        }
        while (angle < 0.0) {
            watchdog.feed();
            angle += 360.0;
        }
        if (angle > 180) {
            angle -= 360;
        }
        return angle;
    }
    
    public void loadParameters() {
        
        double frontIntakePotentiometerLow = params.getAsDouble("P_FrontIntakeVoltageLow", 4.5);
        double frontIntakePotentiometerHigh = params.getAsDouble("P_FrontIntakeVoltageHigh", 0.5);
        double rearIntakePotentiometerLow = params.getAsDouble("P_RearIntakeVoltageLow", 4.5);
        double rearIntakePotentiometerHigh = params.getAsDouble("P_RearIntakeVoltageHigh", 0.5);
                                
        
        frontIntakeTiltPotentiometer.setRange(frontIntakePotentiometerLow, frontIntakePotentiometerHigh);
        rearIntakeTiltPotentiometer.setRange(rearIntakePotentiometerLow, rearIntakePotentiometerHigh);

    }
}
