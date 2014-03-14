package org.TexasTorque.TexasTorque2014.autonomous.drive;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousDriveStraightDeadControlled extends AutonomousCommand {
    private boolean firstCycle;
    private double startTime;
    private double timeout;
    private double speed;
    
    public AutonomousDriveStraightDeadControlled(double maxSpeed, double timeout) {
        this.firstCycle = true;
        this.speed = maxSpeed;
        this.timeout = timeout;
        reset();
    }
    
    public void reset() {
        firstCycle = true;
    }

    public boolean run() {
        if(firstCycle) {
            System.err.println("Driving");
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        Hashtable autonOutput = new Hashtable();
        autonOutput.put("leftSpeed", new Double(-speed));
        autonOutput.put("rightSpeed", new Double(-speed));
        autonOutput.put("frontIntakeDown", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutput);
        
        if(Timer.getFPGATimestamp() - startTime > timeout)
        {
            return true;
        }
        return false;
    }
}
