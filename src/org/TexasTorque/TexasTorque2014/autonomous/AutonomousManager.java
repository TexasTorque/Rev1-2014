package org.TexasTorque.TexasTorque2014.autonomous;

import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousFire;
import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousFireMoveIntakes;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousWait;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;
import org.TexasTorque.TorqueLib.util.Parameters;

public class AutonomousManager {

    private AutonomousBuilder autoBuilder;
    private AutonomousCommand[] autoList;
    private Drivebase drivebase;
    private Parameters params;
    private int autoMode;
    private double autoDelay;
    private int currentIndex;
    private boolean firstCycle;
    private boolean loaded;

    public AutonomousManager() {
        autoBuilder = new AutonomousBuilder();

        autoList = null;

        drivebase = Drivebase.getInstance();
        params = Parameters.getTeleopInstance();

        autoMode = Constants.DO_NOTHING_AUTO;
        autoDelay = 0.0;
        currentIndex = 0;
        firstCycle = true;
        loaded = false;
    }

    public void reset() {
        loaded = false;
    }

    public void setAutoMode(int mode) {
        autoMode = mode;
    }

    public void addAutoDelay(double delay) {
        autoDelay = delay;
    }

    public void loadAutonomous() {
        autoBuilder.clearCommands();
        switch (autoMode) {
            case Constants.DO_NOTHING_AUTO:
                doNothingAuto();
                break;
            case Constants.ONE_BALL_AUTO:
                oneBallAuto();
                break;
            default:
                doNothingAuto();
                break;
        }

        firstCycle = true;
        currentIndex = 0;
        autoList = autoBuilder.getAutonomousList();
        loaded = true;
    }

    public void runAutonomous() {
        if (loaded) {
            if (firstCycle) {
                firstCycle = false;
                autoList[currentIndex].reset();
            }

            boolean commandFinished = autoList[currentIndex].run();

            if (commandFinished) {
                currentIndex++;
                autoList[currentIndex].reset();
            }

            drivebase.run();
        }
    }

    public void doNothingAuto() {
    }
    
    public void oneBallAuto()
    {
        autoBuilder.addCommand(new AutonomousWait(1.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes());
        autoBuilder.addCommand(new AutonomousFire());
        autoBuilder.addCommand(new AutonomousWait(1.0));
    }
}
