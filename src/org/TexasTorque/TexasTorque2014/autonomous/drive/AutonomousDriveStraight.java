package org.TexasTorque.TexasTorque2014.autonomous.drive;

import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class AutonomousDriveStraight extends AutonomousCommand {
    private double target;
    private TorquePID forwardDrive;
    private TorquePID turnDrive;
    private boolean isDone;
    public AutonomousDriveStraight(double distance, double maxSpeed) {
        target = distance;
        forwardDrive = new TorquePID();
        
        turnDrive = new TorquePID();
        
        forwardDrive.setSetpoint(target);
        turnDrive.setSetpoint(0.0);
        isDone= false;
        reset();
    }
    
    public void reset() {
        SensorInput.getInstance().resetDriveEncoders();
    }

    public boolean run() {
        Hashtable autonOutput = new Hashtable();
        
        double power = forwardDrive.calculate(sensorInput.getForwardDrivePosition());
        double turning = turnDrive.calculate(0.0);
        
        if(sensorInput.getForwardDrivePosition() > target) {
            power = 0.0;
            isDone = true;
        }
        
        autonOutput.put("yAxis", new Double(power));
        autonOutput.put("rotation", new Double(turning));
        
        return isDone;
    }
    
}
