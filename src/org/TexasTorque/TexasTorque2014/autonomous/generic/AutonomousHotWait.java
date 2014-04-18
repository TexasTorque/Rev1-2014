package org.TexasTorque.TexasTorque2014.autonomous.generic;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TorqueLib.component.CheesyVisionServer;

public class AutonomousHotWait extends AutonomousCommand {
    
    boolean firstCycle;
    double startTime;
    double waitTime;
    CheesyVisionServer cheese;
    
    public AutonomousHotWait() {
        cheese = CheesyVisionServer.getInstance();
        
        this.reset();
    }
    
    public void reset() {
        cheese.reset();
        firstCycle = true;
    }

    public boolean run() {
        if (cheese.getLeftCount() > cheese.getRightCount())
        {
            waitTime = 3.0;
        } else {
            waitTime = 0.5;
        }
        
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
        driverInput.updateAutonData(new Hashtable());
        return false;
    }
    
}
