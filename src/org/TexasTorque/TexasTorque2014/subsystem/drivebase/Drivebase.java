package org.TexasTorque.TexasTorque2014.subsystem.drivebase;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;
import org.TexasTorque.TorqueLib.util.MovingAverageFilter;
import org.TexasTorque.TorqueLib.util.TorqueUtil;

public class Drivebase extends TorqueSubsystem {

    private static Drivebase instance;
    private double leftFrontDriveSpeed;
    private double leftRearDriveSpeed;
    private double rightFrontDriveSpeed;
    private double rightRearDriveSpeed;
    private double visionPowerCoe;
    private double visionStrafeCoe;
    private double visionRotCoe;
    private double targetDistance;
    private double cameraHeight;
    private double cameraElevation;
    private double[] firstBallCoordinate;
    private double firstTime;
    private boolean firstFound;
    private TorquePID visionForwardPID;
    private TorquePID visionStrafePID;
    private double Vix;
    private double Viy;
    private double Viz;
    private MovingAverageFilter xp;
    private MovingAverageFilter yp;
    private MovingAverageFilter zp;
    private double ballAccel;

    public static Drivebase getInstance() {
        return (instance == null) ? instance = new Drivebase() : instance;
    }

    private Drivebase() {
        super();

        leftFrontDriveSpeed = Constants.MOTOR_STOPPED;
        leftRearDriveSpeed = Constants.MOTOR_STOPPED;
        rightFrontDriveSpeed = Constants.MOTOR_STOPPED;
        rightRearDriveSpeed = Constants.MOTOR_STOPPED;

        visionForwardPID = new TorquePID();
        visionStrafePID = new TorquePID();

        xp = new MovingAverageFilter(8);
        yp = new MovingAverageFilter(8);
        zp = new MovingAverageFilter(8);

        firstFound = true;
    }

    public void run() {
        //mixChannels(driverInput.getYAxis(), driverInput.getXAxis(), driverInput.getRotation());
        calcCatchingSpeeds();
        //setToRobot();
    }

    public void setToRobot() {
        robotOutput.setDriveMotors(leftFrontDriveSpeed, leftRearDriveSpeed, rightFrontDriveSpeed, rightRearDriveSpeed);
    }

    public void setDriveSpeeds(double leftFrontSpeed, double leftRearSpeed, double rightFrontSpeed, double rightRearSpeed) {
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

    private void calcCatchingSpeeds() {

        double xStrafe = 0;
        double yStrafe = 0;
        double rotation = 0;
        SmartDashboard.putBoolean("FirstFound", firstFound);
        if (SmartDashboard.getNumber("X_Var", -1.0) != -1 || SmartDashboard.getNumber("Y_Var", -1.0) != -1) {
            SmartDashboard.putBoolean("Found", true);

            double ballSide = SmartDashboard.getNumber("COG_BOX_SIZE", 0.0);
            double screenWidth = SmartDashboard.getNumber("IMAGE_WIDTH", 320);
            double distance = Constants.BALL_DIAMETER / (2 * Math.tan(Constants.kFOV_X * ballSide / (2 * screenWidth)));

            double[] ballCoordinate = TorqueUtil.magD2(distance, SmartDashboard.getNumber("X_Var") * Constants.kFOV_X / 2, (SmartDashboard.getNumber("Y_Var") * Constants.kFOV_Y / 2) + cameraElevation);
            ballCoordinate[2] += cameraHeight - Constants.BALL_DIAMETER / 2;

            SmartDashboard.putNumber("BallY", ballCoordinate[0]);
            SmartDashboard.putNumber("BallX", ballCoordinate[1]);
            SmartDashboard.putNumber("BallZ", ballCoordinate[2]);
            /*if (firstFound) {
             //Vix.reset();
             //Viy.reset();
             //Viz.reset();
             firstBallCoordinate = ballCoordinate;
             firstTime = Timer.getFPGATimestamp();
             ballSide /= 2;
             if(SmartDashboard.getNumber("COG_X", 0.0) - ballSide > 0 && SmartDashboard.getNumber("COG_Y",0.0) - ballSide > 0 && SmartDashboard.getNumber("COG_X", 0.0) + ballSide < SmartDashboard.getNumber("IMAGE_WIDTH", 0.0) && SmartDashboard.getNumber("COG_Y", 0.0) + ballSide < SmartDashboard.getNumber("IMAGE_HEIGHT", 0.0))
             {
             firstFound = false;
             }
             } else {

                
                
             double time = Timer.getFPGATimestamp() - firstTime;
                
             //Vix.setInput((ballCoordinate[1] - firstBallCoordinate[1])/(time));
             //Viy.setInput((ballCoordinate[0] - firstBallCoordinate[0])/(time));
             //Viz.setInput((ballCoordinate[2] - firstBallCoordinate[2])/(time) + ballAccel * time);
             //Vix.run(); Viy.run(); Viz.run();
             Vix = ((ballCoordinate[1] - firstBallCoordinate[1])/(time));
             Viy = ((ballCoordinate[0] - firstBallCoordinate[0])/(time));
             Viz = ((ballCoordinate[2] - firstBallCoordinate[2])/(time) + ballAccel * time);
                

             //double quadPartA = Viz.getAverage() / (2 * ballAccel);
             //double quadPartB = Math.sqrt(Viz.getAverage() * Viz.getAverage() + 4 * ballAccel * firstBallCoordinate[2]) / (2 * ballAccel);
                
             double quadPartA = Viz/ (2 * ballAccel);
             double quadPartB = Math.sqrt(Viz * Viz + 4 * ballAccel * firstBallCoordinate[2]) / (2 * ballAccel);
                
             double quadSolveA = quadPartA + quadPartB;
             double quadSolveB = quadPartA - quadPartB;
                
             SmartDashboard.putNumber("Expected Land Time", Math.max(quadSolveA, quadSolveB));
                
             quadSolveA = Math.max(quadSolveA, quadSolveB);
                
             double predictedX = Vix * quadSolveA + firstBallCoordinate[1];
             double predictedY = Viy * quadSolveA + firstBallCoordinate[0];
             double predictedZ = Viy * quadSolveA + - ballAccel * quadSolveA * quadSolveA + firstBallCoordinate[2];
                
             xp.setInput(predictedX);
             yp.setInput(predictedY);
             zp.setInput(predictedZ);
             xp.run(); yp.run(); zp.run();
                
             SmartDashboard.putNumber("PredictedX", xp.getAverage());
             SmartDashboard.putNumber("PredictedY", yp.getAverage());
             SmartDashboard.putNumber("PredictedZ", zp.getAverage());
                
             //xStrafe = -ballCoordinate[1];// * visionStrafeCoe;
             //yStrafe = (ballCoordinate[0] - targetDistance);// * visionPowerCoe;
             }*/
            xStrafe = -ballCoordinate[1];// * visionStrafeCoe;
            yStrafe = (ballCoordinate[0] - targetDistance);// * visionPowerCoe;   

        }//Case for Found Target
        else {
            SmartDashboard.putBoolean("Found", false);
            firstFound = true;
        }
        rotation = visionStrafePID.calculate(xStrafe);
        xStrafe = 0.0;
        yStrafe = visionForwardPID.calculate(yStrafe);

        mixChannels(yStrafe, xStrafe, rotation);
    }

    private void mecanumDrive(double yAxis, double xAxis, double rotation) {
        double leftFrontSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - xAxis * Constants.STRAFE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightFrontSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + xAxis * Constants.STRAFE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;
        double leftRearSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT + xAxis * Constants.STRAFE_COEFFICIENT - rotation * Constants.ROTATION_COEFFICIENT;
        double rightRearSpeed = yAxis * Constants.FORWARD_REVERSE_COEFFICIENT - xAxis * Constants.STRAFE_COEFFICIENT + rotation * Constants.ROTATION_COEFFICIENT;

        SmartDashboard.putNumber("LeftFrontDriveSpeed", leftFrontSpeed);
        SmartDashboard.putNumber("LeftRearDriveSpeed", leftRearSpeed);
        SmartDashboard.putNumber("RightFrontDriveSpeed", rightFrontSpeed);
        SmartDashboard.putNumber("RightRearDriveSpeed", rightRearSpeed);

        SmartDashboard.putNumber("YAX", yAxis);
        SmartDashboard.putNumber("XAX", xAxis);
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

        setDriveSpeeds(leftFrontSpeed, leftRearSpeed, rightFrontSpeed, rightRearSpeed);

        //pushToDashboard();
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
        visionPowerCoe = params.getAsDouble("D_ForwardMultiplier", 1.0);
        visionStrafeCoe = params.getAsDouble("D_StrafeMultiplier", 1.0);
        visionRotCoe = params.getAsDouble("D_RotationMultiplier", 0.0);
        targetDistance = params.getAsDouble("D_TargetDistance", 6.0);
        cameraHeight = params.getAsDouble("C_Height", 0.0);
        cameraElevation = params.getAsDouble("C_Elevation", 0.0);
        ballAccel = params.getAsDouble("B_Accel", 10.57);
        SmartDashboard.putNumber("VisionPowerCoe", visionPowerCoe);
        SmartDashboard.putNumber("VisionStrafeCoe", visionStrafeCoe);
        SmartDashboard.putNumber("VisionRotationCoe", visionRotCoe);

        double p = params.getAsDouble("D_VisionForwardP", 0.0);
        double i = params.getAsDouble("D_VisionForwardI", 0.0);
        double d = params.getAsDouble("D_VisionForwardD", 0.0);
        visionForwardPID.setPIDGains(p, i, d);

        p = params.getAsDouble("D_VisionStrafeP", 0.0);
        i = params.getAsDouble("D_VisionStrafeI", 0.0);
        d = params.getAsDouble("D_VisionStrafeD", 0.0);
        visionStrafePID.setPIDGains(p, i, d);

        visionForwardPID.setSetpoint(0.0);
        visionStrafePID.setSetpoint(0.0);
    }
}
