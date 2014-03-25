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

    //----- Encoder -----
    private TorqueCounter leftFrontDriveCounter;
    private TorqueCounter rightFrontDriveCounter;
    private TorqueCounter leftRearDriveCounter;
    private TorqueCounter rightRearDriveCounter;
    private TorqueCounter rightStrafeCounter;
    private TorqueCounter leftStrafeCounter;
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
        leftFrontDriveCounter = new TorqueCounter(Ports.LEFT_FRONT_DRIVE_ENCODER_SIDECAR, Ports.LEFT_FRONT_DRIVE_ENCODER_PORT);
        leftRearDriveCounter = new TorqueCounter(Ports.LEFT_REAR_DRIVE_ENCODER_SIDECAR, Ports.LEFT_REAR_DRIVE_ENCODER_PORT);
        rightFrontDriveCounter = new TorqueCounter(Ports.RIGHT_FRONT_DRIVE_ENCODER_SIDECAR, Ports.RIGHT_FRONT_DRIVE_ENCODER_PORT);
        rightRearDriveCounter = new TorqueCounter(Ports.RIGHT_REAR_DRIVE_ENCODER_SIDECAR, Ports.RIGHT_REAR_DRIVE_ENCODER_PORT);
        leftStrafeCounter = new TorqueCounter(Ports.LEFT_STRAFE_DRIVE_COUNTER_SIDECAR, Ports.LEFT_STRAFE_DRIVE_COUNTER_PORT);
        rightStrafeCounter = new TorqueCounter(Ports.RIGHT_STRAFE_DRIVE_COUNTER_SIDECAR, Ports.RIGHT_STRAFE_DRIVE_COUNTER_PORT);

        catapultEncoder = new TorqueEncoder(Ports.CATAPULT_ENCODER_SIDECAR, Ports.CATAPULT_ENCODER_A_PORT, Ports.CATAPULT_ENCODER_SIDECAR, Ports.CATAPULT_ENCODER_B_PORT, false);

        //----- Gyro -----
        //gyroChannel = new AnalogChannel(Ports.GYRO_PORT);
        //gyro = new Gyro(gyroChannel);
        //gyro.reset();
        //gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
        
        //----- Potentiometers -----
        
        rearIntakeTiltPotentiometer = new TorquePotentiometer(Ports.REAR_INTAKE_TILT_POT_PORT);
        frontIntakeTiltPotentiometer = new TorquePotentiometer(Ports.FRONT_INTAKE_TILT_POT_PORT);
        frontIntakeTiltPotentiometer.setRange(Constants.FRONT_INTAKE_POTENTIOMETER_LOW, Constants.FRONT_INTAKE_POTENTIOMETER_HIGH);
        rearIntakeTiltPotentiometer.setRange(Constants.REAR_INTAKE_POTENTIOMETER_LOW, Constants.REAR_INTAKE_POTENTIOMETER_HIGH);
        
        //----- Misc -----
        catapultLimitSwitch = new DigitalInput(Ports.CATAPULT_LIMIT_SWITCH_B_SIDECAR, Ports.CATAPULT_LIMIT_SWITCH_B_PORT);
        catapultLimitSwitchB = new DigitalInput(Ports.CATAPULT_LIMIT_SWITCH_SIDECAR, Ports.CATAPULT_LIMIT_SWITCH_PORT);
        
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
        leftFrontDriveCounter.start();
        rightFrontDriveCounter.start();
        leftRearDriveCounter.start();
        rightRearDriveCounter.start();
        rightStrafeCounter.start();
        leftStrafeCounter.start();
        catapultEncoder.start();
        frontIntakeTiltPotentiometer.reset();
        rearIntakeTiltPotentiometer.reset();
    }

    public void resetEncoders() {
        leftFrontDriveCounter.reset();
        rightFrontDriveCounter.reset();
        leftRearDriveCounter.reset();
        rightRearDriveCounter.reset();
        rightStrafeCounter.reset();
        leftStrafeCounter.reset();
        catapultEncoder.reset();
        frontIntakeTiltPotentiometer.reset();
        rearIntakeTiltPotentiometer.reset();
    }
    
    public void resetCatapultEncoder()
    {
        catapultEncoder.reset();
    }
    
    public void resetDriveEncoders() {
        leftFrontDriveCounter.reset();
        rightFrontDriveCounter.reset();
        leftRearDriveCounter.reset();
        rightRearDriveCounter.reset();
        rightStrafeCounter.reset();
        leftStrafeCounter.reset();
    }

    public void calcEncoders() {
        leftFrontDriveCounter.calc();
        rightFrontDriveCounter.calc();
        leftRearDriveCounter.calc();
        rightRearDriveCounter.calc();
        rightStrafeCounter.calc();
        leftStrafeCounter.calc();
        catapultEncoder.calc();
        frontIntakeTiltPotentiometer.run();
        rearIntakeTiltPotentiometer.run();
    }

    public void resetGyro() {
        gyro.reset();
        gyro.setSensitivity(Constants.GYRO_SENSITIVITY);
    }

    public double getLeftFrontDriveEncoder() {
        return (leftFrontDriveCounter.get());
    }

    public double getRightFrontDriveEncoder() {
        return (rightFrontDriveCounter.get());
    }

    public double getLeftFrontDriveEncoderRate() {
        return (leftFrontDriveCounter.getRate());
    }

    public double getRightFrontDriveEncoderRate() {
        return (rightFrontDriveCounter.getRate());
    }

    public double getLeftFrontDriveEncoderAcceleration() {
        return (leftFrontDriveCounter.getRate());
    }

    public double getRightFrontDriveEncoderAcceleration() {
        return (rightFrontDriveCounter.getRate());
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

    public double getRightStrafeCounterRate() {
        return rightStrafeCounter.getRate();
    }

    public double getLeftStrafeCounterRate() {
        return leftStrafeCounter.getRate();
    }

    public double getCatapultEncoder() {
        return catapultEncoder.get();
    }

    public boolean getCatapultLimitSwitch() {
        return !catapultLimitSwitch.get();
    }
    
    public boolean getCatapultLimitSwitchB() {
        return !catapultLimitSwitchB.get();
    }

    public double getPSI() {
        return 0.0;//pressureSensor.getVoltage();
    }

    public double getGyroAngle() {
        return 0.0;//limitGyroAngle(-gyro.getAngle() * 2);
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
