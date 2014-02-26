package org.TexasTorque.TexasTorque2014.autonomous.generic;

import edu.wpi.first.wpilibj.Timer;
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
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        
        return (waitTime < (Timer.getFPGATimestamp() - startTime));
    }
    
}
