package org.TexasTorque.TexasTorque2014;

import org.TexasTorque.TexasTorque2014.io.DriverInput;
import org.TexasTorque.TexasTorque2014.io.RobotOutput;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TexasTorque2014.io.dependency.DriverInputState;
import org.TexasTorque.TexasTorque2014.io.dependency.RobotOutputState;
import org.TexasTorque.TexasTorque2014.io.dependency.SensorInputState;
import org.TexasTorque.TorqueLib.util.DashboardManager;
import org.TexasTorque.TorqueLib.util.Parameters;

public abstract class TorqueSubsystem {

    protected DashboardManager dashboardManager;
    protected RobotOutputState robotOutput;
    protected DriverInputState driverInput;
    protected SensorInputState sensorInput;
    protected Parameters params;

    protected TorqueSubsystem() {
        dashboardManager = DashboardManager.getInstance();
        robotOutput = RobotOutput.getState();
        driverInput = DriverInput.getState();
        sensorInput = SensorInput.getState();
        params = Parameters.getTeleopInstance();
    }

    public abstract void run();

    public abstract void setToRobot();

    public abstract void loadParameters();

    public abstract void pushToDashboard();

    public abstract String logData();

    public abstract String getKeyNames();
}
