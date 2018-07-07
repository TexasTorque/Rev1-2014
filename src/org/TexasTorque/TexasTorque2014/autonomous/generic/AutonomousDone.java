
package org.TexasTorque.TexasTorque2014.autonomous.generic;

import java.util.Hashtable;

import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousDone extends AutonomousCommand {
    boolean firstCycle;
    
    public AutonomousDone() {
        firstCycle = true;
    }
    
    public void reset() {
        firstCycle = true;
    }

	public boolean run() {
        if(firstCycle)
        {
            System.err.println("AutonDone");
            firstCycle = false;
        }
        driverInput.updateAutonData(new Hashtable<Object, Object>());
        return false;
    }
    
}
