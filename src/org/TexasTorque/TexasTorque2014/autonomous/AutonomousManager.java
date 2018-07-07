package org.TexasTorque.TexasTorque2014.autonomous;

import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousFireMoveIntakes;
import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousResetCatapultDone;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveStraight;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveStraightDead;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveStraightGyro;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousDone;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousHotWait;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousWait;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousFrontIntake;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousFrontIntakeDown;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousHoopIn;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousRearIntake;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;
import org.TexasTorque.TexasTorque2014.subsystem.manipulator.Manipulator;
import org.TexasTorque.torquelib.util.Parameters;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousManager {

    private AutonomousBuilder autoBuilder;
    private AutonomousCommand[] autoList;
    private Drivebase drivebase;
    private Manipulator manipulator;
    private Parameters params;
    private int autoMode;
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
    }

    public void loadAutonomous() {
        autoBuilder.clearCommands();
        switch (autoMode) {
            case Constants.DO_NOTHING_AUTO:
                doNothingAuto();
                break;
            case Constants.JUST_DRIVE_AUTO:
                driveDistanceAuto();
                break;
            case Constants.TEST_AUTO:
                testAuto();
                break;
            case Constants.ONE_BALL_AUTO:
                break;
            case Constants.TWO_BALL_AUTO:
                break;
            case Constants.THREE_BALL_AUTO:
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
        autoBuilder.addCommand(new AutonomousWait(2.0));
        double distance = params.getAsDouble("A_TestDistance", 0.0);
        autoBuilder.addCommand(new AutonomousDriveStraightGyro(distance, 1.0, 5.0));
    }

    public void justDriveAuto() {
        System.err.println("Loading Just Drive Auto");
        double timeout = params.getAsDouble("A_DriveTimeout", 2.0);
        autoBuilder.addCommand(new AutonomousDriveStraightDead(1.0, timeout));
    }

    public void driveDistanceAuto() {
        System.err.println("Loading Drive Distance Auto");
        double timeout = params.getAsDouble("A_DriveDistanceTimeout", 1.0);
        double distance = params.getAsDouble("A_DriveDistance", 0.0);
        autoBuilder.addCommand(new AutonomousDriveStraight(distance, 1.0, timeout));
    }

    public void oneBall() {
        System.err.println("Loading Hot One Ball Auto");

        autoBuilder.addCommand(new AutonomousHoopIn());
        
        autoBuilder.addCommand(new AutonomousWait(0.5));

        double wait = params.getAsDouble("A_OneBallWait", 6.0);
        boolean left = SmartDashboard.getBoolean("Left", true);
        autoBuilder.addCommand(new AutonomousHotWait(wait, left));

        double fireTimeout = params.getAsDouble("A_FireWait", 8.0);
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireTimeout));
        double postFireWait = params.getAsDouble("A_PostFireWait", 0.5);
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));

        autoBuilder.addCommand(new AutonomousDriveStraightDead(1.0, 1.0));
    }

    public void twoBall() {
        System.err.println("Loading Hot Two Ball Auto");

        autoBuilder.addCommand(new AutonomousHoopIn());
        
        autoBuilder.addCommand(new AutonomousWait(0.5));

        double wait = params.getAsDouble("A_TwoBallWait", 6.0);
        boolean left = SmartDashboard.getBoolean("Left", true);
        autoBuilder.addCommand(new AutonomousHotWait(wait, left));

        double fireTimeout = params.getAsDouble("A_FireWait", 8.0);
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireTimeout));
        double postFireWait = params.getAsDouble("A_PostFireWait", 0.5);
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));

        autoBuilder.addCommand(new AutonomousFrontIntake(2.0));
        double postIntakeWait = params.getAsDouble("A_PostIntakeWait", 0.5);
        autoBuilder.addCommand(new AutonomousWait(postIntakeWait));
        
        autoBuilder.addCommand(new AutonomousResetCatapultDone(5.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireTimeout));
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));

        autoBuilder.addCommand(new AutonomousDriveStraightDead(1.0, 1.0));
    }

    public void threeBall() {
        System.err.println("Loading Hot Three Ball Auto");

        autoBuilder.addCommand(new AutonomousHoopIn());
        
        autoBuilder.addCommand(new AutonomousWait(0.5));

        double wait = params.getAsDouble("A_ThreeBallWait", 2.0);
        boolean left = SmartDashboard.getBoolean("Left", true);
        autoBuilder.addCommand(new AutonomousHotWait(wait, left));

        double fireTimeout = params.getAsDouble("A_FireWait", 8.0);
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireTimeout));
        double postFireWait = params.getAsDouble("A_PostFireWait", 0.5);
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));

        autoBuilder.addCommand(new AutonomousFrontIntake(2.0));
        double postIntakeWait = params.getAsDouble("A_PostIntakeWait", 0.5);
        autoBuilder.addCommand(new AutonomousWait(postIntakeWait));
        
        autoBuilder.addCommand(new AutonomousResetCatapultDone(5.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireTimeout));
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));
        
        autoBuilder.addCommand(new AutonomousRearIntake(2.0));
        autoBuilder.addCommand(new AutonomousWait(postIntakeWait));
        
        autoBuilder.addCommand(new AutonomousResetCatapultDone(5.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireTimeout));
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));

        autoBuilder.addCommand(new AutonomousDriveStraightDead(1.0, 1.0));
    }
}
