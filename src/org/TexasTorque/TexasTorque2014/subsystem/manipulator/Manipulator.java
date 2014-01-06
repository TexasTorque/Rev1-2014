package org.TexasTorque.TexasTorque2014.subsystem.manipulator;

import org.TexasTorque.TexasTorque2014.TorqueSubsystem;
import org.TexasTorque.TexasTorque2014.constants.Constants;
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
                setLightsNormal();
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

    public void intakeFrisbees() {
        setLightsNormal();
    }

    public void reverseIntake() {
        setLightsNormal();
    }

    public void shootHigh() {
        setLightsToChecks();
    }

    public void shootLow() {
        setLightsToChecks();
    }

    public void restoreDefaultPositions() {
        setLightsNormal();
    }

    public void setLightsTracking() {
        double currentAlliance = dashboardManager.getDS().getAlliance().value;

        if (currentAlliance == Constants.RED_ALLIANCE) {
            robotOutput.setLightsState(Constants.TRACKING_RED_ALLIANCE);
        } else if (currentAlliance == Constants.BLUE_ALLIANCE) {
            robotOutput.setLightsState(Constants.TRACKING_BLUE_ALLIANCE);
        }
    }

    public void setLightsLocked() {
        double currentAlliance = dashboardManager.getDS().getAlliance().value;

        if (currentAlliance == Constants.RED_ALLIANCE) {
            robotOutput.setLightsState(Constants.LOCKED_RED_ALLIANCE);
        } else if (currentAlliance == Constants.BLUE_ALLIANCE) {
            robotOutput.setLightsState(Constants.LOCKED_BLUE_ALLIANCE);
        }
    }

    public void setLightsNormal() {
        double currentAlliance = dashboardManager.getDS().getAlliance().value;

        if (currentAlliance == Constants.RED_ALLIANCE) {
            if (sensorInput.getPSI() < Constants.PRESSURE_THRESHOLD) {
                robotOutput.setLightsState(Constants.YELLOW_RED_ALLIANCE);
            } else {
                robotOutput.setLightsState(Constants.RED_SOLID);
            }
        } else if (currentAlliance == Constants.BLUE_ALLIANCE) {
            if (sensorInput.getPSI() < Constants.PRESSURE_THRESHOLD) {
                robotOutput.setLightsState(Constants.YELLOW_BLUE_ALLIANCE);
            } else {
                robotOutput.setLightsState(Constants.BLUE_SOLID);
            }
        }
    }

    private void setLightsToChecks() {
        if (true) {
            setLightsLocked();
        } else {
            setLightsTracking();
        }

        if (sensorInput.getPSI() < Constants.PRESSURE_THRESHOLD) {
            robotOutput.setLightsState(Constants.YELLOW_RED_ALLIANCE);
        }

    }
}
