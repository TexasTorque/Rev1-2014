package org.TexasTorque.TexasTorque2014.autonomous.catapult;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

public class AutonomousFireMoveIntakes extends AutonomousCommand {
    
    private double timeout;
    private boolean firstCycle;
    private double startTime;
    
    public AutonomousFireMoveIntakes(double timeout)
    {
        this.timeout = timeout;
        this.firstCycle = true;
        reset();
    }
    
    public void reset() {
        this.firstCycle = true;
        //manipulator.resetFired();
    }

    public boolean run() {
        if(firstCycle)
        {
            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        
        Hashtable autonOutputs = new Hashtable();
        
        autonOutputs.put("shoot", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        
        drivebase.run();
        manipulator.run();
        
        if(Timer.getFPGATimestamp() > startTime + timeout)
        {
            System.err.println("Fire Timed Out. " + ((manipulator.intakesDone()) ? "Catapult Fail" : "Intake Fail") );
            return true;
        }
        
        //if(manipulator.isFired())
        //{
        //    return true;
        //}
        return false;
    }
    
}
