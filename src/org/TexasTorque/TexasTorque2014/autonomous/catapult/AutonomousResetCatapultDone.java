package org.TexasTorque.TexasTorque2014.autonomous.catapult;

import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.constants.Constants;

public class AutonomousResetCatapultDone extends AutonomousCommand {

    
    
    public void reset() {
        manipulator.resetFired();
    }

    public boolean run() {
        
        Hashtable autonOutputs = new Hashtable();
                
        driverInput.updateAutonData(autonOutputs);
        
        drivebase.run();
        manipulator.run();
        
        return manipulator.catapultReady();
    }
    
}
