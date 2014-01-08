package org.TexasTorque.TexasTorque2014.autonomous;

import org.TexasTorque.TexasTorque2014.io.RobotOutput;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;
import org.TexasTorque.TorqueLib.util.Parameters;

public abstract class AutonomousCommand {

    protected RobotOutput robotOutput;
    protected SensorInput sensorInput;
    protected Parameters params;
    protected Drivebase drivebase;

    protected AutonomousCommand() {
        robotOutput = RobotOutput.getInstance();
        sensorInput = SensorInput.getInstance();
        params = Parameters.getTeleopInstance();

        drivebase = Drivebase.getInstance();
    }

    public abstract void reset();

    public abstract boolean run();
}
