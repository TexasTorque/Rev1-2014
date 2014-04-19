package org.TexasTorque.TexasTorque2014.autonomous.drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class AutonomousDriveStraightFrontIntake extends AutonomousCommand {
    private double target;
    private TorquePID leftDrive;
    private TorquePID rightDrive;
    private boolean isDone;
    private boolean firstCycle;
    private double startTime;
    private double timeout;
    
    public AutonomousDriveStraightFrontIntake(double distance, double maxSpeed, double timeout) {
        target = distance * Constants.CLICKS_PER_METER;
        leftDrive = new TorquePID();
        rightDrive = new TorquePID();
        
        leftDrive.setSetpoint(target);
        rightDrive.setSetpoint(target);
        
        double p = params.getAsDouble("A_DriveForwardP", 0.0);
        double i = params.getAsDouble("A_DriveForwardI", 0.0);
        double d = params.getAsDouble("A_DriveForwardD", 0.0);
        double e = params.getAsDouble("A_DriveForwardE", 0.0);
        leftDrive.setPIDGains(p, i, d);
        rightDrive.setPIDGains(p, i, d);
        leftDrive.setEpsilon(e);
        rightDrive.setEpsilon(e);
        double doneRange = params.getAsDouble("A_DriveForwardDoneRange", 8.0);
        leftDrive.setDoneRange(doneRange);
        rightDrive.setDoneRange(doneRange);
        leftDrive.setMinDoneCycles((int)params.getAsDouble("A_DoneCycles", 50));
        rightDrive.setMinDoneCycles((int)params.getAsDouble("A_DoneCycles", 50));
        isDone= false;
        this.timeout = timeout;
        SensorInput.getInstance().resetDriveEncoders();
        reset();
    }
    
    public void reset() {
        firstCycle = true;
        isDone= false;
        SensorInput.getInstance().resetDriveEncoders();
    }

    public boolean run() {
        if(firstCycle) {
            System.err.println("Driving - Straight");
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        Hashtable autonOutput = new Hashtable();
        double left = leftDrive.calculate(sensorInput.getLeftDrivePosition());
        double right = rightDrive.calculate(sensorInput.getRightDrivePosition());
        
        isDone = leftDrive.isDone() && rightDrive.isDone();
        
        autonOutput.put("leftSpeed", new Double(-left));
        autonOutput.put("rightSpeed", new Double(-right));
        autonOutput.put("frontIntakeDown", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutput);
        
        if(Timer.getFPGATimestamp() - startTime > timeout)
        {
            return true;
        }
        return isDone;
    }
}
