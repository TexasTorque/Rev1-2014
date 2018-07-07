package org.TexasTorque.TexasTorque2014.autonomous.intake;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousFrontIntake extends AutonomousCommand {
    
    private boolean firstCycle;
    private double startTime;
    private double timeout;
    
    public AutonomousFrontIntake(double timeout)
    {
        this.timeout = timeout;
        this.firstCycle = true;
    }
    
    public void reset() {
        this.firstCycle = true;
    }

    public boolean run() {
        if(firstCycle) {
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        Hashtable<String, Double> autonOutputs = new Hashtable<String, Double>();
        Hashtable<String, Boolean> autonOutput = new Hashtable<String, Boolean>();
        
        autonOutput.put("frontIn", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        
        drivebase.run();
        manipulator.run();
        
        if (manipulator.hasBall() && manipulator.intakesDone() && manipulator.getIntakesUp())
        {
            return true;
        }
        
        return (Timer.getFPGATimestamp() - startTime > timeout);
    }
    
}
