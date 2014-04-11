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

    private boolean frontDriveMode;
    private boolean rearDriveMode;
    private boolean strafeMode;

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

        frontDriveMode = Constants.OMNI_MODE;
        rearDriveMode = Constants.OMNI_MODE;
    }

    public void run() {
        if (driverInput.isAuton()) {
            strafeMode = Constants.TRACTION_MODE;
            //setDriveMode(driverInput.getAutonBool("driveMode", Constants.TRACTION_MODE));
            setDriveMode(Constants.TRACTION_MODE, Constants.TRACTION_MODE);
            setDriveSpeeds(driverInput.getAutonDouble("leftSpeed", Constants.MOTOR_STOPPED), driverInput.getAutonDouble("rightSpeed", Constants.MOTOR_STOPPED), Constants.MOTOR_STOPPED);
        } else {
            setDriveMode(((driverInput.getDriveMode()==Constants.TRACTION_MODE) || (driverInput.getFrontDriveMode()==Constants.TRACTION_MODE)) ? Constants.TRACTION_MODE : Constants.OMNI_MODE, ((driverInput.getDriveMode()==Constants.TRACTION_MODE) || (driverInput.getRearDriveMode()==Constants.TRACTION_MODE)) ? Constants.TRACTION_MODE : Constants.OMNI_MODE);
            mixChannels(driverInput.getYAxis(), driverInput.getXAxis(), driverInput.getRotation());
            
        }
//        if(SmartDashboard.getBoolean("firstControllerIsLogitech", false)) {
//            strafeMode = driveMode;
//        }
        setToRobot();
    }

    public void setToRobot() {
        SmartDashboard.putNumber("leftSpeed", leftFrontDriveSpeed);
        SmartDashboard.putNumber("rightSpeed", rightFrontDriveSpeed);
        robotOutput.setDriveMotors(leftFrontDriveSpeed, leftRearDriveSpeed, rightFrontDriveSpeed, rightRearDriveSpeed, strafeDriveSpeed);
        robotOutput.setDriveBaseMode(frontDriveMode, strafeMode, rearDriveMode);
    }

    public void setDriveSpeeds(double leftSpeed, double rightSpeed, double strafeSpeed) {
        leftFrontDriveSpeed = leftSpeed;
        leftRearDriveSpeed = leftSpeed;
        rightFrontDriveSpeed = rightSpeed;
        rightRearDriveSpeed = rightSpeed;
        strafeDriveSpeed = strafeSpeed;
    }

    private void mixChannels(double yAxis, double xAxis, double rotation) {
        yAxis = TorqueUtil.applyDeadband(yAxis, Constants.Y_AXIS_DEADBAND);
        xAxis = TorqueUtil.applyDeadband(xAxis, Constants.X_AXIS_DEADBAND);
        rotation = TorqueUtil.applyDeadband(rotation, Constants.ROTATION_DEADBAND);
        //if(SmartDashboard.getBoolean("firstControllerIsLogitech", false))
        //{
            //xAxis = driverInput.getXAxis() * 1.0;
        //}
        if (frontDriveMode== Constants.OMNI_MODE) {
            SmartDashboard.putBoolean("OmniMode",  true);
            HDrive(yAxis, xAxis, rotation);
        } else {
            SmartDashboard.putBoolean("OmniMode",  false);
            tractionDrive(yAxis, rotation);
        }
    }

    private void tractionDrive(double yAxis, double rotation) {
        double leftSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;
        double strafeSpeed = 0;
        strafeMode = Constants.TRACTION_MODE;
        SmartDashboard.putNumber("YAX", yAxis);
        SmartDashboard.putNumber("RTA", rotation);

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

        setDriveSpeeds(leftSpeed, rightSpeed, strafeSpeed);
    }

    private void HDrive(double yAxis, double xAxis, double rotation) {
        double leftSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;
        double strafeSpeed = xAxis * Constants.STRAFE_COEFFICIENT;
        strafeMode = (strafeSpeed == 0) ? Constants.STRAFE_OFF: Constants.STRAFE_ON;
        
        // bradley this is set up wrong because of 
        //strafeMode = !strafeMode;
        
        //strafeMode = Constants.OMNI_MODE;
                
        SmartDashboard.putNumber("YAX", yAxis);
        SmartDashboard.putNumber("XAX", xAxis);
        SmartDashboard.putNumber("RTA", rotation);

        double max = 1;
        if (Math.abs(leftSpeed) > max) {
            max = Math.abs(leftSpeed);
        }
        if (Math.abs(rightSpeed) > max) {
            max = Math.abs(rightSpeed);
        }
        if (Math.abs(strafeSpeed) > max) {
            max = Math.abs(strafeSpeed);
        }

        if (max > 1) {
            leftSpeed = leftSpeed / max;
            rightSpeed = rightSpeed / max;
            strafeSpeed = strafeSpeed / max;
        }

        setDriveSpeeds(leftSpeed, rightSpeed, strafeSpeed);
    }

    private void setDriveMode(boolean front, boolean rear) {
        if (front != frontDriveMode) {
            frontDriveMode = front;
        }
        if (rear != rearDriveMode) {
            rearDriveMode = rear;
        }
    }

    public String getKeyNames() {
        String names = "LeftDriveSpeed,LeftDriveEncoderPosition,LeftDriveEncoderVelocity,"
                + "RightDriveSpeed,RightDriveEncoderPosition,RightDriveEncoderVelocity,"
                + "GyroAngle";

        return names;
    }

    public void pushToDashboard() {
//        SmartDashboard.putNumber("LeftFrontDriveSpeed", leftFrontDriveSpeed);
//        SmartDashboard.putNumber("RightFrontDriveSpeed", rightFrontDriveSpeed);
//        SmartDashboard.putNumber("LeftRearDriveSpeed", leftRearDriveSpeed);
//        SmartDashboard.putNumber("RightRearDriveSpeed", rightRearDriveSpeed);
        SmartDashboard.putBoolean("Drive", frontDriveMode);
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
