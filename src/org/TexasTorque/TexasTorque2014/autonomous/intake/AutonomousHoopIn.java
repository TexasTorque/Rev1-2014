package org.TexasTorque.TexasTorque2014.autonomous.intake;

import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousHoopIn extends AutonomousCommand {

    public void reset() {
    }

    public boolean run() {
    	Hashtable<String, Boolean> autonOutputs = new Hashtable<String, Boolean>();
        autonOutputs.put("hoopIn", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        
        return true;
    }

}
