package org.TexasTorque.TexasTorque2014.autonomous.catapult;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.subsystem.manipulator.Catapult;

public class AutonomousResetCatapult extends AutonomousCommand {

    private double timeout;
    private double startTime;
    private boolean firstCycle;
    
    public AutonomousResetCatapult(double timeout)
    {
        this.timeout = timeout;
        this.firstCycle = true;
        reset();
    }
    
    public void reset() {
        this.firstCycle = true;
        manipulator.resetFired();
    }

    public boolean run() {
        if(firstCycle)
        {
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        
        Hashtable autonOutputs = new Hashtable();
        
        autonOutputs.put("reset", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        
        drivebase.run();
        manipulator.run();
        
        if(Timer.getFPGATimestamp() > startTime + timeout) {
            System.err.println("Reset Timeout");
            return true;
        }
        
        return manipulator.isResetting();
    }
    
}
