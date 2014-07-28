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
    
    double longWait;
    boolean leftSide;

    public AutonomousHotWait(double wait, boolean left) {
        
        cheese = CheesyVisionServer.getInstance();
        
        longWait = wait;
        leftSide = left;

        this.reset();
    }

    public void reset() {
        firstCycle = true;
    }

    public boolean run() {

        if (firstCycle) {
            System.err.print("Wait Start: ");
            startTime = Timer.getFPGATimestamp();
            System.err.println("L: "+cheese.getLeftCount() + " R: "+cheese.getRightCount());
            if (leftSide && cheese.getLeftCount() > cheese.getRightCount())
            {
                waitTime = 0.0;
            } else if (!leftSide && cheese.getRightCount() > cheese.getLeftCount())
            {
                waitTime = 0.0;
            } else {
                waitTime = longWait;
            }

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
