package org.TexasTorque.TexasTorque2014.autonomous.catapult;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousResetCatapultDone extends AutonomousCommand {
    private double timeout;
    private double startTime;
    private boolean firstCycle;
    
    public AutonomousResetCatapultDone(double timeout) {
        this.timeout = timeout;
        this.firstCycle = true;
    }
    
    public void reset() {
        this.firstCycle = true;
    }

    public boolean run() {
        if(firstCycle)
        {
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        
        Hashtable<?, ?> autonOutputs = new Hashtable<Object, Object>();
        
        driverInput.updateAutonData(autonOutputs);
        
        drivebase.run();
        manipulator.run();
        
        if(Timer.getFPGATimestamp() > startTime + timeout) {
            System.err.println("Fire Reset Timeout");
            return true;
        }
        return manipulator.catapultReady();
    }
    
}
