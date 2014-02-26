package org.TexasTorque.TexasTorque2014.autonomous.catapult;

import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.constants.Constants;

public class AutonomousFire extends AutonomousCommand {

    
    
    public void reset() {
    }

    public boolean run() {
        
        Hashtable autonOutputs = new Hashtable();
        
        autonOutputs.put("shoot", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        drivebase.run();
        manipulator.run();
        
        /*if(Math.abs(sensorInput.getCatapultEncoder() - Constants.CATAPULT_FIRED_LOCATION) < 5.0)
        {
            return true;
        }*/
        return false;
    }
    
}
