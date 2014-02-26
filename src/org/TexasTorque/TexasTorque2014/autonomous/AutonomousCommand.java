package org.TexasTorque.TexasTorque2014.autonomous;

import org.TexasTorque.TexasTorque2014.io.DriverInput;
import org.TexasTorque.TexasTorque2014.io.RobotOutput;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TexasTorque2014.io.dependency.DriverInputState;
import org.TexasTorque.TexasTorque2014.io.dependency.RobotOutputState;
import org.TexasTorque.TexasTorque2014.io.dependency.SensorInputState;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;
import org.TexasTorque.TexasTorque2014.subsystem.manipulator.Manipulator;
import org.TexasTorque.TorqueLib.util.Parameters;

public abstract class AutonomousCommand
{
    protected RobotOutputState robotOutput;
    protected SensorInputState sensorInput;
    protected DriverInputState driverInput;
    protected Parameters params;
    
    protected Drivebase drivebase;
    protected Manipulator manipulator;
    
    protected AutonomousCommand()
    {
        robotOutput = RobotOutput.getState();
        sensorInput = SensorInput.getState();
        driverInput = DriverInput.getState();
        params = Parameters.getTeleopInstance();
        
        drivebase = Drivebase.getInstance();
        manipulator = Manipulator.getInstance();
    }
    
    public abstract void reset();
    public abstract boolean run();
}
