package org.TexasTorque.TexasTorque2014.subsystem.drivebase;

import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TorqueLib.util.TorqueUtil;

public class Drivebase extends TorqueSubsystem {

    private static Drivebase instance;
    private double leftFrontDriveSpeed;
    private double leftRearDriveSpeed;
    private double rightFrontDriveSpeed;
    private double rightRearDriveSpeed;

    public static Drivebase getInstance() {
        return (instance == null) ? instance = new Drivebase() : instance;
    }

    private Drivebase() {
        super();

        leftFrontDriveSpeed = Constants.MOTOR_STOPPED;
        leftRearDriveSpeed = Constants.MOTOR_STOPPED;
        rightFrontDriveSpeed = Constants.MOTOR_STOPPED;
        rightRearDriveSpeed = Constants.MOTOR_STOPPED;
    }

    public void run() {
        mixChannels(driverInput.getYAxis(), driverInput.getXAxis(), driverInput.getRotation());
    }

    public void setToRobot() {
        robotOutput.setDriveMotors(leftFrontDriveSpeed, leftRearDriveSpeed, rightFrontDriveSpeed, rightRearDriveSpeed);
    }

    public void setDriveSpeeds(double leftFrontSpeed,double leftRearSpeed, double rightFrontSpeed, double rightRearSpeed) {
        leftFrontDriveSpeed = leftFrontSpeed;
        leftRearDriveSpeed = leftRearSpeed;
        rightFrontDriveSpeed = rightFrontSpeed;
        rightRearDriveSpeed = rightRearSpeed;
    }

    private void mixChannels(double yAxis, double xAxis, double rotation) {
        yAxis = TorqueUtil.applyDeadband(yAxis, Constants.Y_AXIS_DEADBAND);
        xAxis = TorqueUtil.applyDeadband(xAxis, Constants.X_AXIS_DEADBAND);
        rotation = TorqueUtil.applyDeadband(rotation, Constants.ROTATION_DEADBAND);

        mecanumDrive(yAxis, xAxis, rotation);
    }
    
    private void mecanumDrive(double yAxis, double xAxis, double rotation)
    {
        double leftFrontSpeed = yAxis*Constants.FORWARD_REVERSE_COEFFICIENT + xAxis*Constants.STRAFE_COEFFICIENT + rotation*Constants.ROTATION_COEFFICIENT;
        double leftRearSpeed = yAxis*Constants.FORWARD_REVERSE_COEFFICIENT - xAxis*Constants.STRAFE_COEFFICIENT + rotation*Constants.ROTATION_COEFFICIENT;
        double rightFrontSpeed = yAxis*Constants.FORWARD_REVERSE_COEFFICIENT - xAxis*Constants.STRAFE_COEFFICIENT - rotation*Constants.ROTATION_COEFFICIENT;
        double rightRearSpeed = yAxis*Constants.FORWARD_REVERSE_COEFFICIENT + xAxis*Constants.STRAFE_COEFFICIENT - rotation*Constants.ROTATION_COEFFICIENT;
        
        double max = 1;
        if (leftFrontSpeed > max)
        {
            max = leftFrontSpeed;
        }
        if (leftRearSpeed > max)
        {
            max = leftRearSpeed;
        }
        if (rightFrontSpeed > max)
        {
            max = rightFrontSpeed;
        }
        if (rightRearSpeed > max)
        {
            max = rightRearSpeed;
        }
        
        if (max > 1)
        {
            leftFrontSpeed = leftFrontSpeed / max;
            rightFrontSpeed = rightFrontSpeed / max;
            leftRearSpeed = leftRearSpeed / max;
            rightRearSpeed = rightRearSpeed / max;
        }
        
        setDriveSpeeds(leftFrontSpeed, leftRearSpeed, rightFrontSpeed, rightRearSpeed);
    }

    public String getKeyNames() {
        String names = "LeftDriveSpeed,LeftDriveEncoderPosition,LeftDriveEncoderVelocity,"
                + "RightDriveSpeed,RightDriveEncoderPosition,RightDriveEncoderVelocity,"
                + "GyroAngle";

        return names;
    }

    public String logData() {
        String data = leftFrontDriveSpeed + ",";
        data += sensorInput.getLeftDriveEncoder() + ",";
        data += sensorInput.getLeftDriveEncoderRate() + ",";

        data += rightFrontDriveSpeed + ",";
        data += sensorInput.getRightDriveEncoder() + ",";
        data += sensorInput.getRightDriveEncoderRate() + ",";

        data += sensorInput.getGyroAngle() + ",";

        return data;
    }

    public void loadParameters() {

    }
}
