package org.TexasTorque.TexasTorque2014.subsystem.drivebase;

import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;
import org.TexasTorque.TorqueLib.util.TorqueTrigMath;

public class Drivebase extends TorqueSubsystem {

    private static Drivebase instance;
    private double frontLeftSpeed;
    private double frontRightSpeed;
    private double rearLeftSpeed;
    private double rearRightSpeed;
    private double frontLeftAngleSpeed;
    private double frontRightAngleSpeed;
    private double rearLeftAngleSpeed;
    private double rearRightAngleSpeed;
    private TorquePID frontLeftPID;
    private TorquePID frontRightPID;
    private TorquePID rearLeftPID;
    private TorquePID rearRightPID;

    public static Drivebase getInstance() {
        return (instance == null) ? instance = new Drivebase() : instance;
    }

    private Drivebase() {
        super();

        frontLeftSpeed = Constants.MOTOR_STOPPED;
        frontRightSpeed = Constants.MOTOR_STOPPED;
        rearLeftSpeed = Constants.MOTOR_STOPPED;
        rearRightSpeed = Constants.MOTOR_STOPPED;

        frontLeftAngleSpeed = Constants.MOTOR_STOPPED;
        frontRightAngleSpeed = Constants.MOTOR_STOPPED;
        rearRightAngleSpeed = Constants.MOTOR_STOPPED;
        rearLeftAngleSpeed = Constants.MOTOR_STOPPED;

        frontLeftPID = new TorquePID(0, 0, 0);
        frontRightPID = new TorquePID(0, 0, 0);
        rearLeftPID = new TorquePID(0, 0, 0);
        rearRightPID = new TorquePID(0, 0, 0);
    }

    public void calcDriveSpeeds(double strafeY, double strafeX, double rotation) {
        // ----- Field Centric Calculations -----
        double temp = strafeY * Math.cos(sensorInput.getGyroAngle()) + strafeX * Math.sin(sensorInput.getGyroAngle());
        strafeY = -strafeY * Math.sin(sensorInput.getGyroAngle()) + strafeX * Math.cos(sensorInput.getGyroAngle());
        strafeX = temp;
        // ----- 3 DoF Inverse Kinematics -----
        double r = Math.sqrt(Constants.WHEEL_DISTANCE * Constants.WHEEL_DISTANCE + Constants.WHEEL_WIDTH * Constants.WHEEL_WIDTH);
        double A = strafeX - rotation * (Constants.WHEEL_DISTANCE / r);
        double B = strafeX + rotation * (Constants.WHEEL_DISTANCE / r);
        double C = strafeY - rotation * (Constants.WHEEL_WIDTH / r);
        double D = strafeY + rotation * (Constants.WHEEL_WIDTH / r);

        double ws1 = Math.sqrt(B * B + C * C);
        double wa1 = TorqueTrigMath.atan2(B, C) * 180 / Math.PI;
        double ws2 = Math.sqrt(B * B + D * D);
        double wa2 = TorqueTrigMath.atan2(B, D) * 180 / Math.PI;
        double ws3 = Math.sqrt(A * A + D * D);
        double wa3 = TorqueTrigMath.atan2(A, D) * 180 / Math.PI;
        double ws4 = Math.sqrt(A * A + C * C);
        double wa4 = TorqueTrigMath.atan2(A, C) * 180 / Math.PI;
        
        double max = Math.max(Math.max(ws1, ws2), Math.max(ws3, ws4));
        if(max > 1)
        {
            ws1/=max;
            ws2/=max;
            ws3/=max;
            ws4/=max;
        }
        frontRightSpeed = ws1;
        frontLeftSpeed = ws2;
        rearLeftSpeed = ws3;
        rearRightSpeed = ws4;
        
        frontRightPID.setSetpoint(wa1);
        frontLeftPID.setSetpoint(wa2);
        rearLeftPID.setSetpoint(wa3);
        rearRightPID.setSetpoint(wa4);
        
        frontRightAngleSpeed = frontRightPID.calculate(sensorInput.getFrontRightDriveAngle());
        frontLeftAngleSpeed = frontLeftPID.calculate(sensorInput.getFrontLeftDriveAngle());
        rearLeftAngleSpeed = rearLeftPID.calculate(sensorInput.getRearLeftDriveAngle());
        rearRightAngleSpeed = rearRightPID.calculate(sensorInput.getRearRightDriveAngle());
    }

    public void run() {
        calcDriveSpeeds(driverInput.getThrottle(), driverInput.getTurn(), driverInput.getTurn());
    }

    public void setToRobot() {
        robotOutput.setDriveSpeedMotors(frontRightSpeed, frontLeftSpeed, rearRightSpeed, rearLeftSpeed);
        robotOutput.setDriveAngleMotors(frontRightAngleSpeed, frontLeftAngleSpeed, rearRightAngleSpeed, rearLeftAngleSpeed);
    }

    public String getKeyNames() {
        String names = "FrontRightSpeed,FrontRightAngleSpeed,FrontRightDesiredAngle,FrontRightAngle,"
                + "FrontLeftSpeed,FrontLeftAngleSpeed,FrontLeftDesiredAngle,FrontLeftAngle,"
                + "RearLeftSpeed,RearLeftAngleSpeed,RearLeftDesiredAngle,RearLeftAngle,"
                + "RearRightSpeed,RearRightAngleSpeed,RearRightDesiredAngle,RearRightAngle,";

        return names;
    }

    public String logData() {
        String data = frontRightSpeed + ",";
        data += frontRightAngleSpeed + ",";
        data += frontRightPID.getSetpoint() + ",";
        data += frontRightPID.getPreviousValue() + ",";

        data += frontLeftSpeed + ",";
        data += frontLeftAngleSpeed + ",";
        data += frontLeftPID.getSetpoint() + ",";
        data += frontLeftPID.getPreviousValue() + ",";
        
        data += rearLeftSpeed + ",";
        data += rearLeftAngleSpeed + ",";
        data += rearLeftPID.getSetpoint() + ",";
        data += rearLeftPID.getPreviousValue() + ",";
        
        data += rearRightSpeed + ",";
        data += rearRightAngleSpeed + ",";
        data += rearRightPID.getSetpoint() + ",";
        data += rearRightPID.getPreviousValue() + ",";

        return data;
    }

    public void loadParameters() {
        double p,i,d,e;
        p = params.getAsDouble("D_FtLftAngP", 0.0);
        i = params.getAsDouble("D_FtLftAngI", 0.0);
        d = params.getAsDouble("D_FtLftAngD", 0.0);
        e = params.getAsDouble("D_FtLftAngE", 0.0);
        frontLeftPID.setPIDGains(p, i, d);
        frontLeftPID.setEpsilon(e);
        
        p = params.getAsDouble("D_FtRtAngP", 0.0);
        i = params.getAsDouble("D_FtRtAngI", 0.0);
        d = params.getAsDouble("D_FtRtAngD", 0.0);
        e = params.getAsDouble("D_FtRtAngE", 0.0);
        frontRightPID.setPIDGains(p, i, d);
        frontRightPID.setEpsilon(e);
        
        p = params.getAsDouble("D_RrRtAngP", 0.0);
        i = params.getAsDouble("D_RrRtAngI", 0.0);
        d = params.getAsDouble("D_RrRtAngD", 0.0);
        e = params.getAsDouble("D_RrRtAngE", 0.0);
        rearRightPID.setPIDGains(p, i, d);
        rearRightPID.setEpsilon(e);
        
        p = params.getAsDouble("D_RrLftAngP", 0.0);
        i = params.getAsDouble("D_RrLftAngI", 0.0);
        d = params.getAsDouble("D_RrLftAngD", 0.0);
        e = params.getAsDouble("D_RrLftAngE", 0.0);
        rearLeftPID.setPIDGains(p, i, d);
        rearLeftPID.setEpsilon(e);
        
    }
}
