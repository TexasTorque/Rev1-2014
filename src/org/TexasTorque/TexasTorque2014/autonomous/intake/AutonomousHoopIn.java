package org.TexasTorque.TexasTorque2014.autonomous.intake;

import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousHoopIn extends AutonomousCommand {

    public void reset() {
    }

    public boolean run() {
        Hashtable autonOutputs = new Hashtable();
        autonOutputs.put("hoopIn", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        
        return true;
    }

}
