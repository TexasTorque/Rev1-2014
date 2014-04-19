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

    private boolean driveMode;

    public static Drivebase getInstance() {
        return (instance == null) ? instance = new Drivebase() : instance;
    }

    private Drivebase() {
        super();

        leftFrontDriveSpeed = Constants.MOTOR_STOPPED;
        leftRearDriveSpeed = Constants.MOTOR_STOPPED;
        rightFrontDriveSpeed = Constants.MOTOR_STOPPED;
        rightRearDriveSpeed = Constants.MOTOR_STOPPED;

        driveMode = Constants.OMNI_MODE;
    }

    public void run() {
        if (driverInput.isAuton()) {
            //setDriveMode(driverInput.getAutonBool("driveMode", Constants.TRACTION_MODE));
            setDriveMode(Constants.TRACTION_MODE);
            setDriveSpeeds(driverInput.getAutonDouble("leftSpeed", Constants.MOTOR_STOPPED), driverInput.getAutonDouble("rightSpeed", Constants.MOTOR_STOPPED));
        } else {
            setDriveMode(driverInput.getDriveMode());
            mixChannels(driverInput.getYAxis(), driverInput.getRotation());
        }
        setToRobot();
    }

    public void setToRobot() {
        robotOutput.setDriveMotors(leftFrontDriveSpeed, leftRearDriveSpeed, rightFrontDriveSpeed, rightRearDriveSpeed);
    }

    public void setDriveSpeeds(double leftSpeed, double rightSpeed) {
        leftFrontDriveSpeed = leftSpeed;
        leftRearDriveSpeed = leftSpeed;
        rightFrontDriveSpeed = rightSpeed;
        rightRearDriveSpeed = rightSpeed;
    }

    private void mixChannels(double yAxis, double rotation) {
        yAxis = TorqueUtil.applyDeadband(yAxis, Constants.Y_AXIS_DEADBAND);
        rotation = TorqueUtil.applyDeadband(rotation, Constants.ROTATION_DEADBAND);

        if (driveMode == Constants.OMNI_MODE) {
            SmartDashboard.putBoolean("OmniMode", true);
        } else {
            SmartDashboard.putBoolean("OmniMode", false);
            tractionDrive(yAxis, rotation);
        }
    }

    private void tractionDrive(double yAxis, double rotation) {
        double leftSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;

        double max = 1;
        if (Math.abs(leftSpeed) > max) {
            max = Math.abs(leftSpeed);
        }
        if (Math.abs(rightSpeed) > max) {
            max = Math.abs(rightSpeed);
        }

        if (max > 1) {
            leftSpeed = leftSpeed / max;
            rightSpeed = rightSpeed / max;
        }

        setDriveSpeeds(leftSpeed, rightSpeed);
    }

    private void HDrive(double yAxis, double rotation) {
        double leftSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;
        
        double max = 1;
        if (Math.abs(leftSpeed) > max) {
            max = Math.abs(leftSpeed);
        }
        if (Math.abs(rightSpeed) > max) {
            max = Math.abs(rightSpeed);
        }

        if (max > 1) {
            leftSpeed = leftSpeed / max;
            rightSpeed = rightSpeed / max;
        }

        setDriveSpeeds(leftSpeed, rightSpeed);
    }

    private void setDriveMode(boolean omniMode) {
        if (omniMode != driveMode) {
            driveMode = omniMode;
        }
    }

    public String getKeyNames() {
        String names = "LeftDriveSpeed,LeftDriveEncoderPosition,LeftDriveEncoderVelocity,"
                + "RightDriveSpeed,RightDriveEncoderPosition,RightDriveEncoderVelocity,"
                + "GyroAngle";

        return names;
    }

    public void pushToDashboard() {
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
