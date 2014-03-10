package org.TexasTorque.TexasTorque2014.autonomous;

import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousFireMoveIntakes;
import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousResetCatapultDone;
import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousResetCatapultIntakeDown;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveFrontIntake;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveStraight;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousDone;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousWait;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousFrontIntake;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousFrontIntakeDown;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousRearIntake;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousRearIntakeDown;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;
import org.TexasTorque.TexasTorque2014.subsystem.manipulator.Manipulator;
import org.TexasTorque.TorqueLib.util.Parameters;

public class AutonomousManager {

    private AutonomousBuilder autoBuilder;
    private AutonomousCommand[] autoList;
    private Drivebase drivebase;
    private Manipulator manipulator;
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
        manipulator = Manipulator.getInstance();
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
            case Constants.JUST_DRIVE_AUTO:
                justDriveAuto();
                break;
            case Constants.ONE_BALL_AUTO:
                oneBallAuto();
                break;
            case Constants.TWO_BALL_AUTO:
                twoBallAuto();
                break;
            case Constants.THREE_BALL_AUTO:
                threeBallAuto();
                break;
            case Constants.TEST_AUTO:
                testAuto();
                break;
            default:
                doNothingAuto();
                break;
        }
        autoBuilder.addCommand(new AutonomousDone());

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
            manipulator.run();
            drivebase.run();
        }
    }

    public void doNothingAuto() {
    }

    public void testAuto() {
        System.err.println("Loading Test (TwoBall Drive) Auto");

        autoBuilder.addCommand(new AutonomousFireMoveIntakes(10.0));
        double resetTimeout = params.getAsDouble("C_StallTime", 1.0);
        autoBuilder.addCommand(new AutonomousResetCatapultIntakeDown(resetTimeout + .35));
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(5.0));
        double driveDistance = params.getAsDouble("A_DriveDistance", 2.0);
        autoBuilder.addCommand(new AutonomousDriveFrontIntake(driveDistance, 1.0, 3.0));
        autoBuilder.addCommand(new AutonomousWait(0.5));
        autoBuilder.addCommand(new AutonomousResetCatapultDone(5.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(5.0));
    }

    public void oneBallAuto() {
        System.err.println("Loading One Ball Auto");
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(8.0));
        double driveDistance = params.getAsDouble("A_DriveDistance", 2.0);
        autoBuilder.addCommand(new AutonomousDriveStraight(driveDistance, 1.0, 3.0));
    }

    public void twoBallAuto() {
        System.err.println("Loading Two Ball Auto");
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(10.0));
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(5.0));
        autoBuilder.addCommand(new AutonomousFrontIntake(0.8));
        autoBuilder.addCommand(new AutonomousWait(0.8));
        autoBuilder.addCommand(new AutonomousResetCatapultDone(5.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(5.0));
        double driveDistance = params.getAsDouble("A_DriveDistance", 2.0);
        autoBuilder.addCommand(new AutonomousDriveStraight(driveDistance, 1.0, 3.0));
    }
    
    public void threeBallAuto() {
        System.err.println("Loading Three Ball Auto");
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(10.0));
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(5.0));
        autoBuilder.addCommand(new AutonomousFrontIntake(0.8));
        autoBuilder.addCommand(new AutonomousWait(0.5));
        autoBuilder.addCommand(new AutonomousResetCatapultDone(5.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(5.0));
        autoBuilder.addCommand(new AutonomousRearIntakeDown(5.0));
        autoBuilder.addCommand(new AutonomousRearIntake(0.8));
        autoBuilder.addCommand(new AutonomousWait(0.8));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(5.0));
        double driveDistance = params.getAsDouble("A_DriveDistance", 2.0);
        autoBuilder.addCommand(new AutonomousDriveStraight(driveDistance, 1.0, 3.0));
    }

    public void justDriveAuto() {
        System.err.println("Loading Just Drive Auto");
        double driveDistance = params.getAsDouble("A_DriveDistance", 2.0);
        autoBuilder.addCommand(new AutonomousDriveStraight(driveDistance, 1.0, 3.0));
    }
}
