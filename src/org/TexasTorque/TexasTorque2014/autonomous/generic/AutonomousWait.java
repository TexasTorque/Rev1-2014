package org.TexasTorque.TexasTorque2014.autonomous.generic;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousWait extends AutonomousCommand {
    
    boolean firstCycle;
    double startTime;
    double waitTime;
    
    public AutonomousWait(double timeout) {
        waitTime = timeout;
        this.reset();
    }
    
    public void reset() {
        firstCycle = true;
    }

    public boolean run() {
        if(firstCycle)
        {
            System.err.print("Wait Start: ");
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        if (waitTime < (Timer.getFPGATimestamp() - startTime)) {
            System.err.println("Done");
            return true;
        }
        driverInput.updateAutonData(new Hashtable<Object, Object>());
        return false;
    }
    
}
