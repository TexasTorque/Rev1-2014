package org.TexasTorque.TexasTorque2014.io;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.constants.Ports;
import org.TexasTorque.TexasTorque2014.io.dependency.RobotOutputState;
import org.TexasTorque.TorqueLib.component.Motor;

public class RobotOutput {

    private static RobotOutput instance;
    private static RobotOutputState state;

    //----- Pneumatics -----
    private Compressor compressor;
    private Solenoid frontDriveBaseSwitcher;
    private Solenoid middleDriveBaseSwitcher;
    private Solenoid rearDriveBaseSwitcher;
    private boolean driveBaseMode;
    private Solenoid catapultStandoffs;
    private boolean catapultMode;

    //----- Drive Motors -----
    private Motor leftFrontDriveMotor;
    private Motor leftRearDriveMotor;
    private Motor rightFrontDriveMotor;
    private Motor rightRearDriveMotor;
    private Motor leftStrafeMotor;
    private Motor rightStrafeMotor;
    private double leftFrontMotorSpeed;
    private double leftRearMotorSpeed;
    private double rightFrontMotorSpeed;
    private double rightRearMotorSpeed;
    private double strafeMotorSpeed;
    
    //----- Intake -----
    private Motor frontIntakeMotor;
    private Motor rearIntakeMotor;
    private Motor frontIntakeTiltMotor;
    private Motor rearIntakeTiltMotor;
    private double frontIntakeMotorSpeed;
    private double rearIntakeMotorSpeed;
    private double frontIntakeTiltMotorSpeed;
    private double rearIntakeTiltMotorSpeed;
    
    //----- Catapult -----
    private Motor catapultMotor;
    private double catapultMotorSpeed;

    public RobotOutput() {
        //----- Pneumatics -----
        //compressor = new Compressor(Ports.COMPRESSOR_SIDECAR, Ports.PRESSURE_SWITCH_PORT, Ports.COMPRESSOR_SIDECAR, Ports.COMPRESSOR_RELAY_PORT);
        frontDriveBaseSwitcher = new Solenoid(Ports.FRONT_DRIVEBASE_SWITCHER);
        middleDriveBaseSwitcher = new Solenoid(Ports.MIDDLE_DRIVEBASE_SWITCHER);
        rearDriveBaseSwitcher = new Solenoid(Ports.REAR_DRIVEBASE_SWITCHER);
        driveBaseMode = Constants.OMNI_MODE;
        catapultStandoffs = new Solenoid(Ports.CATAPULT_STANDOFFS);
        catapultMode = Constants.FAR_SHOT;

        //----- Drive Motors -----
        leftFrontDriveMotor = new Motor(new Victor(Ports.LEFT_FRONT_DRIVE_SIDECAR, Ports.LEFT_FRONT_DRIVE_MOTOR_PORT), false, true);
        leftRearDriveMotor = new Motor(new Victor(Ports.LEFT_REAR_DRIVE_SIDECAR, Ports.LEFT_REAR_DRIVE_MOTOR_PORT), false, true);
        rightFrontDriveMotor = new Motor(new Victor(Ports.RIGHT_FRONT_DRIVE_SIDECAR, Ports.RIGHT_FRONT_DRIVE_MOTOR_PORT), true, true);
        rightRearDriveMotor = new Motor(new Victor(Ports.RIGHT_REAR_DRIVE_SIDECAR, Ports.RIGHT_REAR_DRIVE_MOTOR_PORT), true, true);
        leftStrafeMotor = new Motor(new Victor(Ports.LEFT_STRAFE_DRIVE_SIDECAR, Ports.LEFT_STRAFE_DRIVE_MOTOR_PORT), false, true);
        rightStrafeMotor = new Motor(new Victor(Ports.RIGHT_STRAFE_DRIVE_SIDECAR, Ports.RIGHT_STRAFE_DRIVE_MOTOR_PORT), false, true);
        rightFrontMotorSpeed = Constants.MOTOR_STOPPED;
        rightRearMotorSpeed = Constants.MOTOR_STOPPED;
        leftFrontMotorSpeed = Constants.MOTOR_STOPPED;
        leftRearMotorSpeed = Constants.MOTOR_STOPPED;
        strafeMotorSpeed = Constants.MOTOR_STOPPED;
        
        //----- Intake -----
        
        frontIntakeMotor = new Motor(new Victor(Ports.FRONT_INTAKE_SIDECAR, Ports.FRONT_INTAKE_MOTOR_PORT), false, true);
        rearIntakeMotor = new Motor(new Victor(Ports.REAR_INTAKE_SIDECAR, Ports.REAR_INTAKE_MOTOR_PORT), false, true);
        frontIntakeTiltMotor = new Motor(new Victor(Ports.FRONT_INTAKE_TILT_SIDECAR, Ports.FRONT_INTAKE_TILT_MOTOR_PORT), false, true);
        rearIntakeTiltMotor = new Motor(new Victor(Ports.FRONT_INTAKE_SIDECAR, Ports.REAR_INTAKE_TILT_MOTOR_PORT), false, true);
        
        frontIntakeMotorSpeed = Constants.MOTOR_STOPPED;
        rearIntakeMotorSpeed = Constants.MOTOR_STOPPED;
        frontIntakeTiltMotorSpeed = Constants.MOTOR_STOPPED;
        rearIntakeTiltMotorSpeed = Constants.MOTOR_STOPPED;
        
        //----- Catapult -----
        catapultMotor = new Motor(new Victor(Ports.CATAPULT_SIDECAR, Ports.CATAPULT_MOTOR_PORT), false, true);
        catapultMotorSpeed = Constants.MOTOR_STOPPED;

        //----- Misc Misc -----
        //compressor.start();
    }

    public synchronized static RobotOutput getInstance() {
        return (instance == null) ? instance = new RobotOutput() : instance;
    }

    public synchronized static RobotOutputState getState() {
        return (state == null) ? state = new RobotOutputState() : state;
    }

    public synchronized void updateState() {
        state.updateState(this);
    }

    public synchronized void pullFromState() {
        setDriveMotors(state.getLeftFrontMotorSpeed(), state.getLeftRearMotorSpeed(), state.getRightFrontMotorSpeed(), state.getRightRearMotorSpeed(), state.getStrafeMotorSpeed());
        setDriveBaseMode(state.getDriveBaseMode());
        setCatapultMotor(state.getCatapultMotorSpeed());
        setCatapultMode(state.getCatapultMode());
    }

    public void setDriveMotors(double leftFrontSpeed, double leftRearSpeed, double rightFrontSpeed, double rightRearSpeed, double strafeSpeed) {
        leftFrontDriveMotor.Set(leftFrontSpeed);
        leftRearDriveMotor.Set(leftRearSpeed);
        rightFrontDriveMotor.Set(rightFrontSpeed);
        rightRearDriveMotor.Set(rightRearSpeed);
        leftStrafeMotor.Set(strafeSpeed);
        rightStrafeMotor.Set(strafeSpeed);
        leftFrontMotorSpeed = leftFrontSpeed;
        rightFrontMotorSpeed = rightFrontSpeed;
        leftRearMotorSpeed = leftRearSpeed;
        rightRearMotorSpeed = rightRearSpeed;
        strafeMotorSpeed = strafeSpeed;
    }

    public double getLeftFrontMotorSpeed() {
        return leftFrontMotorSpeed;
    }

    public double getRightFrontMotorSpeed() {
        return rightFrontMotorSpeed;
    }

    public double getLeftRearMotorSpeed() {
        return leftRearMotorSpeed;
    }

    public double getRightRearMotorSpeed() {
        return rightRearMotorSpeed;
    }

    public double getStrafeMotorSpeed() {
        return strafeMotorSpeed;
    }

    public boolean getCompressorEnabled() {
        return false;
        //return compressor.enabled();
    }

    public void setDriveBaseMode(boolean mode) {
        frontDriveBaseSwitcher.set(mode);
        middleDriveBaseSwitcher.set(mode);
        rearDriveBaseSwitcher.set(mode);
        driveBaseMode = mode;
    }

    public boolean getDriveBaseMode() {
        return driveBaseMode;
    }
    
    public void setIntakeMotors(double frontRoller, double rearRoller, double frontTilt, double rearTilt)
    {
        frontIntakeMotor.Set(frontRoller);
        rearIntakeMotor.Set(rearRoller);
        frontIntakeTiltMotor.Set(frontTilt);
        rearIntakeTiltMotor.Set(rearTilt);
        frontIntakeMotorSpeed = frontRoller;
        rearIntakeMotorSpeed = rearRoller;
        frontIntakeTiltMotorSpeed = frontTilt;
        rearIntakeTiltMotorSpeed = rearTilt;
    }
    
    public double getFrontIntakeMotorSpeed()
    {
        return frontIntakeMotorSpeed;
    }
    
    public double getRearIntakeMotorSpeed()
    {
        return rearIntakeMotorSpeed;
    }
    
    public double getFrontIntakeTiltMotorSpeed()
    {
        return frontIntakeTiltMotorSpeed;
    }
    
    public double getRearIntakeTiltMotorSpeed()
    {
        return rearIntakeTiltMotorSpeed;
    }
    
    public void setCatapultMotor(double speed)
    {
        catapultMotor.Set(speed);
        catapultMotorSpeed = speed;
    }
    
    public double getCatapultMotorSpeed()
    {
        return catapultMotorSpeed;
    }
    
    public void setCatapultMode(boolean mode)
    {
        catapultStandoffs.set(mode);
        catapultMode = mode;
    }
    
    public boolean getCatapultMode()
    {
        return catapultMode;
    }
}
