package org.TexasTorque.TexasTorque2014.subsystem.drivebase;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TorqueLib.util.TorqueUtil;

public class Drivebase extends TorqueSubsystem {

    private static Drivebase instance;
    private double leftFrontDriveSpeed;
    private double leftRearDriveSpeed;
    private double rightFrontDriveSpeed;
    private double rightRearDriveSpeed;
    private double strafeDriveSpeed;

    private boolean omniMode;

    public static Drivebase getInstance() {
        return (instance == null) ? instance = new Drivebase() : instance;
    }

    private Drivebase() {
        super();

        leftFrontDriveSpeed = Constants.MOTOR_STOPPED;
        leftRearDriveSpeed = Constants.MOTOR_STOPPED;
        rightFrontDriveSpeed = Constants.MOTOR_STOPPED;
        rightRearDriveSpeed = Constants.MOTOR_STOPPED;
        strafeDriveSpeed = Constants.MOTOR_STOPPED;
    }

    public void run() {
        setDriveMode(driverInput.getDriveMode());
        mixChannels(driverInput.getYAxis(), driverInput.getXAxis(), driverInput.getRotation());
        setToRobot();
    }

    public void setToRobot() {
        robotOutput.setDriveMotors(leftFrontDriveSpeed, leftRearDriveSpeed, rightFrontDriveSpeed, rightRearDriveSpeed, strafeDriveSpeed);

    }

    public void setDriveSpeeds(double leftFrontSpeed, double leftRearSpeed, double rightFrontSpeed, double rightRearSpeed, double strafeSpeed) {
        leftFrontDriveSpeed = leftFrontSpeed;
        leftRearDriveSpeed = leftRearSpeed;
        rightFrontDriveSpeed = rightFrontSpeed;
        rightRearDriveSpeed = rightRearSpeed;
        strafeDriveSpeed = strafeSpeed;
    }

    private void mixChannels(double yAxis, double xAxis, double rotation) {
        yAxis = TorqueUtil.applyDeadband(yAxis, Constants.Y_AXIS_DEADBAND);
        xAxis = TorqueUtil.applyDeadband(xAxis, Constants.X_AXIS_DEADBAND);
        rotation = TorqueUtil.applyDeadband(rotation, Constants.ROTATION_DEADBAND);

        if (omniMode) {
            HDrive(yAxis, xAxis, rotation);
        } else {
            tractionDrive(yAxis, rotation);
        }
    }

    private void tractionDrive(double yAxis, double rotation) {
        double leftFrontSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightFrontSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;
        double leftRearSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightRearSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;
        double strafeSpeed = 0;

        SmartDashboard.putNumber("YAX", yAxis);
        SmartDashboard.putNumber("RTA", rotation);

        double max = 1;
        if (Math.abs(leftFrontSpeed) > max) {
            max = Math.abs(leftFrontSpeed);
        }
        if (Math.abs(leftRearSpeed) > max) {
            max = Math.abs(leftRearSpeed);
        }
        if (Math.abs(rightFrontSpeed) > max) {
            max = Math.abs(rightFrontSpeed);
        }
        if (Math.abs(rightRearSpeed) > max) {
            max = Math.abs(rightRearSpeed);
        }

        if (max > 1) {
            leftFrontSpeed = leftFrontSpeed / max;
            rightFrontSpeed = rightFrontSpeed / max;
            leftRearSpeed = leftRearSpeed / max;
            rightRearSpeed = rightRearSpeed / max;
        }

        SmartDashboard.putNumber("LeftFrontDriveSpeed", leftFrontSpeed);
        SmartDashboard.putNumber("LeftRearDriveSpeed", leftRearSpeed);
        SmartDashboard.putNumber("RightFrontDriveSpeed", rightFrontSpeed);
        SmartDashboard.putNumber("RightRearDriveSpeed", rightRearSpeed);

        setDriveSpeeds(leftFrontSpeed, leftRearSpeed, rightFrontSpeed, rightRearSpeed, strafeSpeed);

        //pushToDashboard();
    }

    private void HDrive(double yAxis, double xAxis, double rotation) {
        double leftFrontSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightFrontSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;
        double leftRearSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightRearSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;
        double strafeSpeed = xAxis * Constants.STRAFE_COEFFICIENT;

        SmartDashboard.putNumber("YAX", yAxis);
        SmartDashboard.putNumber("XAX", xAxis);
        SmartDashboard.putNumber("RTA", rotation);
        //SmartDashboard.putNumber("RRS", rightRearSpeed);

        double max = 1;
        if (Math.abs(leftFrontSpeed) > max) {
            max = Math.abs(leftFrontSpeed);
        }
        if (Math.abs(leftRearSpeed) > max) {
            max = Math.abs(leftRearSpeed);
        }
        if (Math.abs(rightFrontSpeed) > max) {
            max = Math.abs(rightFrontSpeed);
        }
        if (Math.abs(rightRearSpeed) > max) {
            max = Math.abs(rightRearSpeed);
        }

        if (max > 1) {
            leftFrontSpeed = leftFrontSpeed / max;
            rightFrontSpeed = rightFrontSpeed / max;
            leftRearSpeed = leftRearSpeed / max;
            rightRearSpeed = rightRearSpeed / max;
        }

        SmartDashboard.putNumber("LeftFrontDriveSpeed", leftFrontSpeed);
        SmartDashboard.putNumber("LeftRearDriveSpeed", leftRearSpeed);
        SmartDashboard.putNumber("RightFrontDriveSpeed", rightFrontSpeed);
        SmartDashboard.putNumber("RightRearDriveSpeed", rightRearSpeed);
        SmartDashboard.putNumber("StrafeDriveSpeed", strafeSpeed);

        setDriveSpeeds(leftFrontSpeed, leftRearSpeed, rightFrontSpeed, rightRearSpeed, strafeSpeed);

        //pushToDashboard();
    }
    
    private void setDriveMode(boolean mode)
    {
        omniMode = mode;
    }

    public String getKeyNames() {
        String names = "LeftDriveSpeed,LeftDriveEncoderPosition,LeftDriveEncoderVelocity,"
                + "RightDriveSpeed,RightDriveEncoderPosition,RightDriveEncoderVelocity,"
                + "GyroAngle";

        return names;
    }

    public void pushToDashboard() {
        SmartDashboard.putNumber("LeftFrontDriveSpeed", leftFrontDriveSpeed);
        SmartDashboard.putNumber("LeftRearDriveSpeed", leftRearDriveSpeed);
        SmartDashboard.putNumber("RightFrontDriveSpeed", rightFrontDriveSpeed);
        SmartDashboard.putNumber("RightRearDriveSpeed", rightRearDriveSpeed);
    }

    public String logData() {
        String data = leftFrontDriveSpeed + ",";
        data += sensorInput.getLeftFrontDriveEncoder() + ",";
        data += sensorInput.getLeftFrontDriveEncoderRate() + ",";

        data += leftRearDriveSpeed + ",";
        data += sensorInput.getLeftRearDriveEncoder() + ",";
        data += sensorInput.getLeftRearDriveEncoderRate() + ",";

        data += rightFrontDriveSpeed + ",";
        data += sensorInput.getRightFrontDriveEncoder() + ",";
        data += sensorInput.getRightFrontDriveEncoderRate() + ",";

        data += rightRearDriveSpeed + ",";
        data += sensorInput.getRightRearDriveEncoder() + ",";
        data += sensorInput.getRightRearDriveEncoderRate() + ",";

        data += sensorInput.getGyroAngle() + ",";

        return data;
    }

    public void loadParameters() {

    }
}
