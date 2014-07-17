package org.TexasTorque.TexasTorque2014.autonomous.drive;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class AutonomousDriveStraightFrontIntakeGyro extends AutonomousCommand
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
    
    public AutonomousDriveStraightFrontIntakeGyro(double distance, double speed, double timeout)
    {
        super();
        
        encoderPID = new TorquePID();
        gyroPID = new TorquePID();
        
        gyroPID.setSetpoint(0);
        
        encoderPID.setMinDoneCycles(10);
        gyroPID.setMinDoneCycles(10);
        
        targetDistance = distance * Constants.CLICKS_PER_FOOT;
        
        double p = params.getAsDouble("A_DriveForwardP", 0.05);
        double i = params.getAsDouble("A_DriveForwardI", 0.0);
        double d = params.getAsDouble("A_DriveForwardD", 0.0);
        double e = params.getAsDouble("A_DriveForwardE", 0.0);
        double r = params.getAsDouble("A_DriveForwardDoneRange", 0.0);
        double maxOut = params.getAsDouble("A_DriveForwardMaxOutput", 1.0);
        
        encoderPID.setMaxOutput(maxOut);
        
        encoderPID.setPIDGains(p, i, d);
        encoderPID.setEpsilon(e);
        encoderPID.setDoneRange(r);
        encoderPID.reset();
        encoderPID.setSetpoint(targetDistance);
        
        p = params.getAsDouble("A_GyroTurnP", 0.0);
        i = params.getAsDouble("A_GyroTurnI", 0.0);
        d = params.getAsDouble("A_GyroTurnD", 0.0);
        e = params.getAsDouble("A_GyroTurnE", 0.0);
        r = params.getAsDouble("A_GyroTurnDoneRange", 0.0);
        
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
                
        double averageDistance = (sensorInput.getLeftDrivePosition() + sensorInput.getRightDrivePosition()) / 2.0;
        double currentAngle = sensorInput.getGyroAngle();
        
        double y = encoderPID.calculate(averageDistance);
        double x = -gyroPID.calculate(currentAngle);
        
        double leftSpeed = y + x;
        double rightSpeed = y - x;

        autonOutput.put("leftSpeed", new Double(-leftSpeed));
        autonOutput.put("rightSpeed", new Double(-rightSpeed));
        autonOutput.put("frontIntakeDown", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutput);
        
        isDone = encoderPID.isDone();
        
        if(Timer.getFPGATimestamp() > startTime + timeout)
        {
            return true;
        }
        
        return isDone;
    }
}
