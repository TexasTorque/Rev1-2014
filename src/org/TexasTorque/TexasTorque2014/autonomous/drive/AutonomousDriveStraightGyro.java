package org.TexasTorque.TexasTorque2014.autonomous.drive;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class AutonomousDriveStraightGyro extends AutonomousCommand
{
    private double targetDistance;
    
    private double distanceSetpoint;
    private double angleSetpoint;
    private boolean zeroLock;
    
    private TorquePID encoderPID;
    private TorquePID gyroPID;
    
    private double timeout;
    private boolean firstCycle;
    private boolean isDone;
    private double startTime;
    
    public AutonomousDriveStraightGyro(double distance, double speed, boolean zeroAngle, double timeout)
    {
        super();
        
        encoderPID = new TorquePID();
        gyroPID = new TorquePID();
        
        encoderPID.setSetpoint(distance);
        gyroPID.setSetpoint(sensorInput.getGyroAngle());
        
        encoderPID.setMaxOutput(speed);
        encoderPID.setMinDoneCycles(10);
        gyroPID.setMinDoneCycles(10);
        
        targetDistance = distance * Constants.CLICKS_PER_METER;
        
        double p = params.getAsDouble("D_DriveEncoderP", 0.05);
        double i = params.getAsDouble("D_DriveEncoderI", 0.0);
        double d = params.getAsDouble("D_DriveEncoderD", 0.0);
        double e = params.getAsDouble("D_DriveEncoderEpsilon", 0.0);
        double r = params.getAsDouble("D_DriveEncoderDoneRange", 0.0);
        
        encoderPID.setPIDGains(p, i, d);
        encoderPID.setEpsilon(e);
        encoderPID.setDoneRange(r);
        encoderPID.reset();
        
        p = params.getAsDouble("D_DriveGyroP", 0.0);
        i = params.getAsDouble("D_DriveGyroI", 0.0);
        d = params.getAsDouble("D_DriveGyroD", 0.0);
        e = params.getAsDouble("D_DriveGyroEpsilon", 0.0);
        r = params.getAsDouble("D_DriveGyroDoneRange", 0.0);
        
        gyroPID.setPIDGains(p, i, d);
        gyroPID.setEpsilon(e);
        gyroPID.setDoneRange(r);
        gyroPID.reset();
        
        this.timeout = timeout;
        reset();
    }
    
    public void reset()
    {
        SensorInput.getInstance().resetDriveEncoders();
        SensorInput.getInstance().resetGyro();
        firstCycle = true;
        isDone= false;
    }
    
    public boolean run()
    {
        if(firstCycle) {
            System.err.println("Driving - Straight");
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        
        Hashtable autonOutput = new Hashtable();
                
        double averageDistance = (sensorInput.getLeftFrontDriveEncoder() + sensorInput.getRightFrontDriveEncoder()) / 2.0;
        double currentAngle = sensorInput.getGyroAngle();
        
        double y = encoderPID.calculate(averageDistance);
        double x = -gyroPID.calculate(currentAngle);
        
        double leftSpeed = y + x;
        double rightSpeed = y - x;

        autonOutput.put("leftSpeed", new Double(-leftSpeed));
        autonOutput.put("rightSpeed", new Double(-rightSpeed));
        
        driverInput.updateAutonData(autonOutput);
        
        isDone = encoderPID.isDone() && gyroPID.isDone();
        
        if(Timer.getFPGATimestamp() - startTime > timeout)
        {
            return true;
        }
        return isDone;
    }
}
