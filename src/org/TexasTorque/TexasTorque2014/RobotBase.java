package org.TexasTorque.TexasTorque2014;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousManager;
import org.TexasTorque.TexasTorque2014.io.*;
import org.TexasTorque.TexasTorque2014.io.dependency.*;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;
import org.TexasTorque.TexasTorque2014.subsystem.manipulator.Manipulator;
import org.TexasTorque.TorqueLib.util.DashboardManager;
import org.TexasTorque.TorqueLib.util.Parameters;
import org.TexasTorque.TorqueLib.util.TorqueLogging;

public class RobotBase extends IterativeRobot implements Runnable {

    Thread continuousThread;
    Watchdog watchdog;
    Parameters params;
    TorqueLogging logging;
    DashboardManager dashboardManager;
    DriverInputState driverInputState;
    SensorInputState sensorInputState;
    RobotOutputState robotOutputState;
    SensorInput sensorInput;
    RobotOutput robotOutput;
    DriverInput driverInput;
    Drivebase drivebase;
    Manipulator manipulator;
    AutonomousManager autoManager;
    Timer robotTime;
    boolean logData;
    int logCycles;
    double numCycles;
    double previousTime;

    public void robotInit() {
        drivebase = Drivebase.getInstance();
        manipulator = Manipulator.getInstance();

        continuousThread = new Thread(this);
        continuousThread.start();
    }

    public void autonomousInit() {
    }

    public void teleopInit() {
        drivebase.loadParameters();
        manipulator.loadParameters();
    }

    public void disabledInit() {
        drivebase.loadParameters();
        manipulator.loadParameters();
    }

    public void run() {

        previousTime = Timer.getFPGATimestamp();

        while (true) {
            watchdog.feed();
            if (isAutonomous() && isEnabled()) {
                autonomousContinuous();
                Timer.delay(0.004);
            } else if (isOperatorControl() && isEnabled()) {
                teleopContinuous();
                Timer.delay(0.004);
            } else if (isDisabled()) {
                disabledContinuous();
                Timer.delay(0.05);
            }

            sensorInput.calcEncoders();
            numCycles++;
        }
    }

    public void autonomousPeriodic() {
        //robotOutput.pullFromState();
        drivebase.pushToDashboard();
        manipulator.pushToDashboard();
    }

    public void autonomousContinuous() {
        //sensorInput.updateState();
        //robotOutput.updateState();
    }

    public void teleopPeriodic() {
        drivebase.run();
        manipulator.run();
        drivebase.pushToDashboard();
        manipulator.pushToDashboard();
        //robotOutput.pullFromState();
    }

    public void teleopContinuous() {
        driverInput.updateState();
        //sensorInput.updateState();
        //robotOutput.updateState();
    }

    public void disabledPeriodic() {
        drivebase.pushToDashboard();
        manipulator.pushToDashboard();
    }

    public void disabledContinuous() {
        driverInput.updateState();
        sensorInput.updateState();
    }
}
