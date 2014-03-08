package org.TexasTorque.TexasTorque2014.autonomous.intake;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousFrontIntakeDown extends AutonomousCommand {
    
    private boolean firstCycle;
    private double startTime;
    private double timeout;
    
    public AutonomousFrontIntakeDown(double timeout)
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
        Hashtable autonOutputs = new Hashtable();
        
        autonOutputs.put("frontIntakeDown", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        
        drivebase.run();
        manipulator.run();
        
        return (Timer.getFPGATimestamp() - startTime > timeout) || manipulator.catapultReady() || manipulator.catapultReadyForIntake();
    }
    
}
