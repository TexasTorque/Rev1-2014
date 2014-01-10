package org.TexasTorque.TexasTorque2014;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        driverInput = DriverInput.getInstance();
        sensorInput = SensorInput.getInstance();
        robotOutput = RobotOutput.getInstance();
        driverInputState = DriverInput.getState();
        sensorInputState = SensorInput.getState();
        robotOutputState = RobotOutput.getState();
        driverInput.updateState();
        watchdog = getWatchdog();

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

            //sensorInput.calcEncoders();
            numCycles++;
        }
    }

    public void autonomousPeriodic() {
        //robotOutput.pullFromState();
        pushToDashboard();
    }

    public void autonomousContinuous() {
        //sensorInput.updateState();
        //robotOutput.updateState();
    }

    public void teleopPeriodic() {
        drivebase.run();
        manipulator.run();
        pushToDashboard();
        //robotOutput.pullFromState();
        
        
    }

    public void teleopContinuous() {
        driverInput.updateState();
        //sensorInput.updateState();
        //robotOutput.updateState();
        SmartDashboard.putNumber("Period", Timer.getFPGATimestamp() - previousTime);
        SmartDashboard.putNumber("Hertz", 1 / SmartDashboard.getNumber("Period"));
        previousTime = Timer.getFPGATimestamp();
    }

    public void disabledPeriodic() {
        pushToDashboard();
    }

    public void disabledContinuous() {
        //driverInput.updateState();
        //sensorInput.updateState();
    }
    
    public void pushToDashboard()
    {
        SmartDashboard.putNumber("NumCycles", numCycles);
        
        drivebase.pushToDashboard();
        manipulator.pushToDashboard();
        driverInputState.pushToDashboard();
    }
}
