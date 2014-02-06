package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;

public class Manipulator extends TorqueSubsystem {

    private static Manipulator instance;
    private Drivebase drivebase;

    public static Manipulator getInstance() {
        return (instance == null) ? instance = new Manipulator() : instance;
    }

    private Manipulator() {
        super();

        drivebase = Drivebase.getInstance();
    }

    public void run() {
        if (!driverInput.overrideState()) {
            //----- Normal Ops -----

            if (driverInput.restoreToDefault()) {
                restoreDefaultPositions();
            } else {
            }
        } else {
            calcOverrides();
        }
    }

    public void setToRobot() {
    }

    public String getKeyNames() {
        String names = "InOverrideState,";

        return names;
    }

    public String logData() {
        String data = driverInput.overrideState() + ",";

        return data;
    }

    public void loadParameters() {
    }

    private void calcOverrides() {
    }

    public void restoreDefaultPositions() {
    }
}
