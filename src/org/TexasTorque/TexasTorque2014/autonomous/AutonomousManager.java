package org.TexasTorque.TexasTorque2014.autonomous;

import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousFireMoveIntakes;
import org.TexasTorque.TexasTorque2014.autonomous.catapult.AutonomousResetCatapultDone;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveStraight;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveStraightDead;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveStraightFrontIntakeGyro;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousDriveStraightGyro;
import org.TexasTorque.TexasTorque2014.autonomous.drive.AutonomousTurnCheesyGyro;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousDone;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousHotWait;
import org.TexasTorque.TexasTorque2014.autonomous.generic.AutonomousWait;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousFrontIntake;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousFrontIntakeDown;
import org.TexasTorque.TexasTorque2014.autonomous.intake.AutonomousHoopIn;
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
                driveDistanceAuto();
                break;
            case Constants.TEST_AUTO:
                testAuto();
                break;
            case Constants.DRIVE_ONE_BALL_AUTO:
                driveOneBallAuto();
                break;
            case Constants.DRIVE_TWO_BALL_AUTO:
                driveTwoBallAuto();
                break;
            case Constants.CHEESY_TWO_BALL_AUTO:
                cheesyTwoBall();
                break;
            case Constants.ONE_HOT_AUTO:
                hotOneBall();
                break;
<<<<<<< HEAD
            case Constants.ONE_BALL_FIVE_ELASTICS:
                driveOneBallAutoFive();
                break;
            case Constants.ONE_HOT_FIVE_ELASTICS:
                hotOneBallFive();
                break;
            case Constants.TWO_BALL_FIVE_ELASTICS:
                driveTwoBallAutoFive();
                break;
=======
>>>>>>> parent of 85d7aff... Five elastics shit
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

    public void driveOneBallAuto() {
        System.err.println("Loading Drive One Ball Auto");
        
        autoBuilder.addCommand(new AutonomousHoopIn());
        
        double timeout = params.getAsDouble("A_DriveDistanceTimeout", 1.0);
        double distance = params.getAsDouble("A_DriveDistance", 0.0);
        autoBuilder.addCommand(new AutonomousDriveStraightGyro(distance, 1.0, timeout));
        double postDriveWait = params.getAsDouble("A_PostDriveWait", 1.0);
        autoBuilder.addCommand(new AutonomousWait(postDriveWait));
        
        double fireWait = params.getAsDouble("A_FireWait", 8.0);
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireWait));
        double postFireWait = params.getAsDouble("A_PostFireWait", 0.5);
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));

        autoBuilder.addCommand(new AutonomousHoopIn());
    }

    public void driveTwoBallAuto() {
        System.err.println("Loading Drive Two Ball Auto");
        
        autoBuilder.addCommand(new AutonomousHoopIn());
        
        double timeout = params.getAsDouble("A_DriveDistanceTimeout", 1.0);
        double distance = params.getAsDouble("A_DriveDistance", 0.0);
        autoBuilder.addCommand(new AutonomousDriveStraightFrontIntakeGyro(distance, 1.0, timeout));
        
        double postDriveWait = params.getAsDouble("A_PostDriveWait", 1.0);
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postDriveWait));
        
        double fireWait = params.getAsDouble("A_FireWait", 8.0);
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireWait));
        double postFireWait = params.getAsDouble("A_PostFireWait", 0.5);
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));
        
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(5.0));
        double intakeTime = params.getAsDouble("A_IntakeTime", 1.0);
        autoBuilder.addCommand(new AutonomousFrontIntake(intakeTime));
        autoBuilder.addCommand(new AutonomousHoopIn());
        double postIntakeWait = params.getAsDouble("A_PostIntakeWait", 1.0);
        autoBuilder.addCommand(new AutonomousWait(postIntakeWait));
        
        autoBuilder.addCommand(new AutonomousResetCatapultDone(5.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireWait));
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));
    }

    public void cheesyTwoBall() {
        System.err.println("Loading Cheesy");
        
        autoBuilder.addCommand(new AutonomousHoopIn());
        
        double timeout = params.getAsDouble("A_DriveDistanceTimeout", 1.0);
        double distance = params.getAsDouble("A_CheesyDriveDistance", 0.0);
        autoBuilder.addCommand(new AutonomousDriveStraightFrontIntakeGyro(distance, 1.0, timeout));
        
        double degrees = params.getAsDouble("A_CheesyDegrees", 0.0);
        double turnTimeout = params.getAsDouble("A_TurnTimeout", 2.0);
        autoBuilder.addCommand(new AutonomousTurnCheesyGyro(degrees, 1.0, turnTimeout));
        
        double fireWait = params.getAsDouble("A_FireWait", 8.0);
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireWait));
        double postFireWait = params.getAsDouble("A_PostFireWait", 0.5);
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));
        
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(5.0));
        double intakeTime = params.getAsDouble("A_IntakeTime", 1.0);
        autoBuilder.addCommand(new AutonomousFrontIntake(intakeTime));
        double postIntakeWait = params.getAsDouble("A_PostIntakeWait", 1.0);
        
        autoBuilder.addCommand(new AutonomousHoopIn());
        
        autoBuilder.addCommand(new AutonomousWait(postIntakeWait));
        autoBuilder.addCommand(new AutonomousResetCatapultDone(5.0));
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireWait));
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));
    }
    
    public void hotOneBall()
    {
        System.err.println("Loading Hot One Ball Auto");
        
        autoBuilder.addCommand(new AutonomousHoopIn());
        
        double timeout = params.getAsDouble("A_DriveDistanceTimeout", 1.0);
        double distance = params.getAsDouble("A_DriveDistance", 0.0);
        autoBuilder.addCommand(new AutonomousDriveStraightGyro(distance, 1.0, timeout));
        
        autoBuilder.addCommand(new AutonomousHotWait());
        
        double fireTimeout = params.getAsDouble("A_FireWait", 8.0);
        autoBuilder.addCommand(new AutonomousFireMoveIntakes(fireTimeout));
        double postFireWait = params.getAsDouble("A_PostFireWait", 0.5);
        autoBuilder.addCommand(new AutonomousFrontIntakeDown(postFireWait));
    }
}
