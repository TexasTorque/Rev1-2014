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
    private Solenoid driveBaseSwitcher;
    private double driveBaseState;

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

    public RobotOutput() {
        //----- Pneumatics -----
        compressor = new Compressor(Ports.COMPRESSOR_SIDECAR, Ports.PRESSURE_SWITCH_PORT, Ports.COMPRESSOR_SIDECAR, Ports.COMPRESSOR_RELAY_PORT);
        driveBaseSwitcher = new Solenoid(Ports.DRIVEBASE_SWITCHER);

        //----- Drive Motors -----
        leftFrontDriveMotor = new Motor(new Victor(Ports.SIDECAR_ONE, Ports.LEFT_FRONT_DRIVE_MOTOR_PORT), false, false);
        leftRearDriveMotor = new Motor(new Victor(Ports.SIDECAR_ONE, Ports.LEFT_REAR_DRIVE_MOTOR_PORT), false, false);
        rightFrontDriveMotor = new Motor(new Victor(Ports.SIDECAR_ONE, Ports.RIGHT_FRONT_DRIVE_MOTOR_PORT), true, false);
        rightRearDriveMotor = new Motor(new Victor(Ports.SIDECAR_ONE, Ports.RIGHT_REAR_DRIVE_MOTOR_PORT), true, false);
        leftStrafeMotor = new Motor(new Victor(Ports.SIDECAR_ONE, Ports.LEFT_STRAFE_DRIVE_MOTOR_PORT), true, false);
        rightStrafeMotor = new Motor(new Victor(Ports.SIDECAR_ONE, Ports.RIGHT_STRAFE_DRIVE_MOTOR_PORT), true, false);
        rightFrontMotorSpeed = Constants.MOTOR_STOPPED;
        rightRearMotorSpeed = Constants.MOTOR_STOPPED;
        leftFrontMotorSpeed = Constants.MOTOR_STOPPED;
        leftRearMotorSpeed = Constants.MOTOR_STOPPED;
        strafeMotorSpeed = Constants.MOTOR_STOPPED;

        //----- Misc Misc -----
        compressor.start();
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
    
    public void setDriveBaseState(boolean mode)
    {
        driveBaseSwitcher.set(mode);
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
    
    public double getStrafeMotorSpeed()
    {
        return strafeMotorSpeed;
    }

    public boolean getCompressorEnabled() {
        return compressor.enabled();
    }
}
