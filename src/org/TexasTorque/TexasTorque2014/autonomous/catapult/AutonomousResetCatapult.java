package org.TexasTorque.TexasTorque2014.autonomous.catapult;

import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousResetCatapult extends AutonomousCommand {

    
    
    public void reset() {
        manipulator.resetFired();
    }

    public boolean run() {
        
        Hashtable autonOutputs = new Hashtable();
        
        autonOutputs.put("reset", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        
        drivebase.run();
        manipulator.run();
        
        return true;
    }
    
}
